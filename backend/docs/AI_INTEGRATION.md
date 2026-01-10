# AI Integration Architecture

## Overview

This document describes the AI integration architecture for the BookMart platform. The design follows a **modular, provider-agnostic approach** that allows easy swapping of AI providers (OpenAI, Anthropic, Google, local models, etc.) without changing business logic.

## Architecture Principles

### 1. **Abstraction Layer**
- All AI functionality goes through abstract service interfaces
- Business logic never directly calls specific AI providers
- Easy to swap providers or use multiple providers simultaneously

### 2. **Provider Agnostic**
- Support multiple AI providers (OpenAI, Anthropic Claude, Google Gemini, etc.)
- Configuration-based provider selection
- Graceful fallback mechanisms

### 3. **Cost Optimization**
- Caching for expensive AI operations
- Rate limiting to prevent abuse
- Smart prompt engineering to minimize token usage
- Batch processing where applicable

### 4. **Monitoring & Logging**
- Track AI usage metrics (tokens, cost, latency)
- Log all AI requests/responses for debugging
- Error tracking and alerting

---

## AI Features

### Feature 1: Book Recommendation Engine

**Purpose**: Provide personalized book recommendations based on user preferences, reading history, and behavior.

**Inputs**:
- User profile (reader level, favorite genres, reading goals)
- User's reading history (purchased/reviewed books)
- Current browsing context (optional)
- Wishlist items

**Outputs**:
- List of recommended books with relevance scores
- Explanation for each recommendation
- Confidence level

**AI Provider**: OpenAI GPT-4 or Claude

**Implementation Strategy**:
```python
# Prompt Template
You are a book recommendation expert. Based on the user's profile and history,
recommend 10 books that match their interests.

User Profile:
- Reader Level: {reader_level}
- Favorite Genres: {genres}
- Reading History: {history}

Provide recommendations in JSON format with: book_id, title, author,
relevance_score (0-100), reason (1 sentence).
```

**Caching Strategy**: Cache recommendations per user for 24 hours

**Cost Estimation**: ~500 tokens per request × $0.01/1K tokens = $0.005 per recommendation

---

### Feature 2: Reader Level Estimation

**Purpose**: Automatically determine a user's reading proficiency level based on their preferences and reading history.

**Inputs**:
- Books the user has read (titles, genres, complexity)
- User's favorite genres
- Review comments (optional)
- Reading pace (optional)

**Outputs**:
- Estimated reader level (Beginner, Intermediate, Advanced, Expert)
- Confidence score (0-100%)
- Explanation
- Suggested next level books

**AI Provider**: OpenAI GPT-4 Turbo (faster, cheaper)

**Implementation Strategy**:
```python
# Prompt Template
Analyze the user's reading history and estimate their reading proficiency level.

Reading History:
{book_titles_and_authors}

Favorite Genres: {genres}

Classify into: Beginner, Intermediate, Advanced, or Expert.
Provide confidence score and explanation.

Return JSON: {level, confidence, explanation, suggestions}
```

**Caching Strategy**: Cache per user, recompute only when reading history changes significantly

**Cost Estimation**: ~300 tokens per request × $0.005/1K tokens = $0.0015 per estimation

---

### Feature 3: AI-Powered Support Chat

**Purpose**: Provide intelligent customer support with context-aware responses.

**Inputs**:
- User's question/message
- Conversation history (last 10 messages)
- User's order history (for order-related questions)
- Knowledge base context (FAQs, policies)

**Outputs**:
- AI-generated response
- Suggested actions (e.g., "View order", "Contact human agent")
- Confidence score

**AI Provider**: OpenAI GPT-4 or Claude (for better reasoning)

**Implementation Strategy**:
```python
# System Prompt
You are a customer support agent for BookMart, an online book marketplace.
Help users with:
- Order status and tracking
- Book recommendations
- Account issues
- General questions about books

Use the provided context to answer accurately. If unsure, suggest contacting
a human agent.

Context:
- User's Orders: {recent_orders}
- FAQ: {relevant_faqs}

Conversation History:
{messages}

User: {current_message}
Assistant:
```

**Special Features**:
- Escalation to human agent when confidence < 70%
- Context injection from database (orders, books)
- Sentiment analysis for priority routing

**Cost Estimation**: ~800 tokens per message × $0.01/1K tokens = $0.008 per message

---

### Feature 4: Smart Book Search Enhancement

**Purpose**: Enhance search with semantic understanding and natural language queries.

**Inputs**:
- User's natural language query (e.g., "books like Harry Potter but for adults")
- Search filters (optional)

**Outputs**:
- Enhanced search query for database/Elasticsearch
- Book recommendations matching semantic intent
- Query interpretation explanation

**AI Provider**: OpenAI Embeddings + GPT-4 Turbo

**Implementation Strategy**:
```python
# Step 1: Generate embedding for query
embedding = openai.embeddings.create(
    model="text-embedding-3-small",
    input=user_query
)

# Step 2: Vector similarity search in book database
# (Requires storing book embeddings)

# Step 3: Use GPT-4 to interpret complex queries
# e.g., "books like X but Y" → extract features
```

**Cost Estimation**: Embedding: $0.00002 per request, GPT: ~200 tokens = $0.001 total

---

### Feature 5: Automated Book Categorization (Admin Tool)

**Purpose**: Automatically categorize and tag new books when added to catalog.

**Inputs**:
- Book title, author, description
- Table of contents (optional)
- Sample text (optional)

**Outputs**:
- Primary category
- Sub-categories (genres)
- Tags (themes, topics)
- Reader level suggestion
- Similar books

**AI Provider**: OpenAI GPT-4 Turbo

**Implementation Strategy**:
```python
# Prompt Template
Analyze this book and provide categorization.

Title: {title}
Author: {author}
Description: {description}

Return JSON with:
- category (from: fiction, non_fiction, mystery, romance, etc.)
- genres (array of sub-genres)
- tags (array of thematic tags)
- reader_level (beginner/intermediate/advanced/expert)
- similar_books (if you know any)
```

**Cost Estimation**: ~600 tokens per book × $0.005/1K tokens = $0.003 per book

---

## Service Architecture

### Layer 1: Abstract Service Interface

```python
# app/services/ai/base.py
from abc import ABC, abstractmethod
from typing import List, Dict, Any

class AIServiceProvider(ABC):
    """Abstract base class for AI service providers."""

    @abstractmethod
    async def generate_recommendations(
        self,
        user_profile: Dict[str, Any],
        limit: int = 10
    ) -> List[Dict[str, Any]]:
        """Generate book recommendations."""
        pass

    @abstractmethod
    async def estimate_reader_level(
        self,
        reading_history: List[Dict[str, Any]]
    ) -> Dict[str, Any]:
        """Estimate user's reading proficiency level."""
        pass

    @abstractmethod
    async def chat_support(
        self,
        message: str,
        conversation_history: List[Dict[str, str]],
        context: Dict[str, Any]
    ) -> Dict[str, Any]:
        """Generate support chat response."""
        pass
```

### Layer 2: Provider Implementations

```python
# app/services/ai/openai_provider.py
class OpenAIProvider(AIServiceProvider):
    """OpenAI implementation of AI services."""

    def __init__(self, api_key: str):
        self.client = OpenAI(api_key=api_key)

    async def generate_recommendations(self, ...):
        # OpenAI-specific implementation
        response = await self.client.chat.completions.create(
            model="gpt-4-turbo",
            messages=[...],
            response_format={"type": "json_object"}
        )
        return parse_recommendations(response)

# app/services/ai/claude_provider.py
class ClaudeProvider(AIServiceProvider):
    """Anthropic Claude implementation (alternative)."""
    pass

# app/services/ai/local_provider.py
class LocalModelProvider(AIServiceProvider):
    """Local model implementation (e.g., Llama, Mistral)."""
    pass
```

### Layer 3: Service Factory

```python
# app/services/ai/factory.py
class AIServiceFactory:
    """Factory to create AI service provider based on config."""

    @staticmethod
    def create_provider(provider_name: str) -> AIServiceProvider:
        if provider_name == "openai":
            return OpenAIProvider(api_key=settings.OPENAI_API_KEY)
        elif provider_name == "claude":
            return ClaudeProvider(api_key=settings.ANTHROPIC_API_KEY)
        elif provider_name == "local":
            return LocalModelProvider()
        else:
            raise ValueError(f"Unknown provider: {provider_name}")

# Usage in app
ai_service = AIServiceFactory.create_provider(settings.AI_PROVIDER)
```

### Layer 4: Caching & Monitoring

```python
# app/services/ai/cache.py
class CachedAIService:
    """Wrapper that adds caching to AI services."""

    def __init__(self, provider: AIServiceProvider, cache: Redis):
        self.provider = provider
        self.cache = cache

    async def generate_recommendations(self, user_profile, limit=10):
        cache_key = f"recommendations:{user_profile['user_id']}"

        # Check cache
        cached = await self.cache.get(cache_key)
        if cached:
            return json.loads(cached)

        # Generate new
        result = await self.provider.generate_recommendations(
            user_profile, limit
        )

        # Store in cache (24 hours)
        await self.cache.setex(cache_key, 86400, json.dumps(result))

        return result
```

---

## API Endpoints

### 1. Recommendations
- `GET /api/v1/ai/recommendations` - Get personalized recommendations
- `GET /api/v1/ai/recommendations/similar/{book_id}` - Get similar books

### 2. Reader Level
- `POST /api/v1/ai/estimate-level` - Estimate reader level
- `GET /api/v1/ai/level-suggestions` - Get books for next level

### 3. Support Chat
- `POST /api/v1/ai/support/chat` - Send message to AI support
- `GET /api/v1/ai/support/history/{ticket_id}` - Get chat history

### 4. Search Enhancement
- `POST /api/v1/ai/search/semantic` - Semantic search with AI

### 5. Admin Tools
- `POST /api/v1/ai/admin/categorize-book` - Auto-categorize book

---

## Configuration

```env
# AI Provider Selection
AI_PROVIDER=openai  # Options: openai, claude, local

# OpenAI
OPENAI_API_KEY=sk-...
OPENAI_MODEL_RECOMMENDATIONS=gpt-4-turbo
OPENAI_MODEL_CHAT=gpt-4
OPENAI_MODEL_EMBEDDINGS=text-embedding-3-small

# Anthropic Claude (Alternative)
ANTHROPIC_API_KEY=sk-ant-...
ANTHROPIC_MODEL=claude-3-5-sonnet-20241022

# AI Feature Flags
AI_RECOMMENDATIONS_ENABLED=true
AI_READER_LEVEL_ENABLED=true
AI_SUPPORT_CHAT_ENABLED=true
AI_SEMANTIC_SEARCH_ENABLED=true

# Caching
AI_CACHE_TTL_RECOMMENDATIONS=86400  # 24 hours
AI_CACHE_TTL_READER_LEVEL=604800    # 7 days
AI_CACHE_ENABLED=true

# Rate Limiting
AI_RATE_LIMIT_RECOMMENDATIONS=10/hour/user
AI_RATE_LIMIT_CHAT=30/hour/user
```

---

## Cost Estimation (Monthly)

Assuming 10,000 active users:

| Feature | Usage | Cost/Request | Monthly Cost |
|---------|-------|--------------|--------------|
| Recommendations | 10K users × 5/month | $0.005 | $250 |
| Reader Level | 10K users × 1/month | $0.0015 | $15 |
| Support Chat | 2K tickets × 5 msgs | $0.008 | $80 |
| Semantic Search | 50K searches | $0.001 | $50 |
| Book Categorization | 500 new books | $0.003 | $1.50 |
| **Total** | | | **~$400/month** |

---

## Implementation Priority

1. **Phase 1** (MVP):
   - ✅ Basic recommendation engine
   - ✅ Reader level estimation
   - ✅ Abstract service layer

2. **Phase 2**:
   - ✅ AI support chat
   - ✅ Caching layer
   - ✅ Usage monitoring

3. **Phase 3**:
   - Semantic search
   - Book categorization
   - Multiple provider support

---

## Security & Privacy

- Never log user PII in AI requests
- Anonymize data sent to AI providers
- Implement request sanitization
- Rate limiting per user
- Cost caps and alerts
- GDPR compliance (user consent)

---

## Monitoring Metrics

- AI request latency (p50, p95, p99)
- Token usage per feature
- Cost per user per month
- Cache hit rate
- Error rate by provider
- User satisfaction scores

---

This architecture provides a **flexible, scalable, and cost-effective** AI integration that can grow with the platform.
