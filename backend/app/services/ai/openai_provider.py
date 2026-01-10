"""
OpenAI implementation of AI service provider.
Uses GPT-4 for recommendations, reader level estimation, and support chat.
"""
import json
from typing import List, Dict, Any
from datetime import datetime
import asyncio

try:
    from openai import AsyncOpenAI
    OPENAI_AVAILABLE = True
except ImportError:
    OPENAI_AVAILABLE = False

from app.services.ai.base import (
    AIServiceProvider,
    AIProviderError,
    AIInvalidResponseError,
)
from app.config import settings


class OpenAIProvider(AIServiceProvider):
    """OpenAI implementation of AI services using GPT-4."""

    def __init__(self, api_key: str, model: str = "gpt-4-turbo"):
        """
        Initialize OpenAI provider.

        Args:
            api_key: OpenAI API key
            model: Model to use (default: gpt-4-turbo)
        """
        if not OPENAI_AVAILABLE:
            raise ImportError(
                "OpenAI package not installed. "
                "Install with: pip install openai"
            )

        self.client = AsyncOpenAI(api_key=api_key)
        self.model = model
        self.max_retries = 3

    async def _call_gpt(
        self,
        messages: List[Dict[str, str]],
        response_format: Dict[str, str] = None,
        temperature: float = 0.7,
        max_tokens: int = 1500
    ) -> str:
        """
        Call GPT model with retry logic.

        Args:
            messages: Chat messages
            response_format: Optional JSON response format
            temperature: Creativity (0-2)
            max_tokens: Max response tokens

        Returns:
            Response text

        Raises:
            AIProviderError: If API call fails
        """
        for attempt in range(self.max_retries):
            try:
                kwargs = {
                    "model": self.model,
                    "messages": messages,
                    "temperature": temperature,
                    "max_tokens": max_tokens,
                }

                if response_format:
                    kwargs["response_format"] = response_format

                response = await self.client.chat.completions.create(**kwargs)

                return response.choices[0].message.content

            except Exception as e:
                if attempt == self.max_retries - 1:
                    raise AIProviderError(f"OpenAI API error: {str(e)}")
                await asyncio.sleep(2 ** attempt)  # Exponential backoff

    async def generate_recommendations(
        self,
        user_profile: Dict[str, Any],
        reading_history: List[Dict[str, Any]],
        limit: int = 10
    ) -> Dict[str, Any]:
        """Generate personalized book recommendations using GPT-4."""

        # Build reading history summary
        history_summary = "\n".join([
            f"- {book['title']} by {book['author']} ({book.get('category', 'Unknown')})"
            for book in reading_history[:20]  # Limit to avoid token overflow
        ])

        # Build prompt
        system_prompt = """You are an expert book recommendation engine for BookMart.
Analyze the user's profile and reading history to suggest books they'll love.
Provide diverse recommendations across their favorite genres.
Return ONLY valid JSON, no additional text."""

        user_prompt = f"""User Profile:
- Reader Level: {user_profile.get('reader_level', 'intermediate')}
- Favorite Genres: {', '.join(user_profile.get('favorite_genres', ['general']))}

Reading History (books they enjoyed):
{history_summary or 'No reading history yet'}

Generate {limit} book recommendations. For each recommendation, provide:
- title: Book title
- author: Author name
- relevance_score: 0-100 (how well it matches their profile)
- reason: One sentence explaining why they'll like it
- genre: Primary genre

Return as JSON array: {{"recommendations": [...]}}"""

        messages = [
            {"role": "system", "content": system_prompt},
            {"role": "user", "content": user_prompt}
        ]

        try:
            response_text = await self._call_gpt(
                messages,
                response_format={"type": "json_object"},
                temperature=0.8  # Higher creativity for recommendations
            )

            # Parse JSON response
            result = json.loads(response_text)

            # Validate structure
            if "recommendations" not in result:
                raise AIInvalidResponseError("Missing 'recommendations' in response")

            # Add metadata
            result["generated_at"] = datetime.utcnow().isoformat()
            result["model"] = self.model

            return result

        except json.JSONDecodeError as e:
            raise AIInvalidResponseError(f"Invalid JSON response: {str(e)}")

    async def estimate_reader_level(
        self,
        reading_history: List[Dict[str, Any]],
        favorite_genres: List[str]
    ) -> Dict[str, Any]:
        """Estimate user's reading proficiency level using GPT-4."""

        # Build book list
        book_list = "\n".join([
            f"- {book['title']} by {book['author']}"
            for book in reading_history[:30]
        ])

        system_prompt = """You are a reading proficiency assessment expert.
Analyze the user's reading history and classify their reading level.

Levels:
- Beginner: Simple language, short books, popular fiction
- Intermediate: Moderate complexity, varied genres, some classics
- Advanced: Complex narratives, literary fiction, dense non-fiction
- Expert: Academic texts, philosophical works, experimental literature

Return ONLY valid JSON, no additional text."""

        user_prompt = f"""Reading History:
{book_list or 'No reading history yet'}

Favorite Genres: {', '.join(favorite_genres)}

Classify the reader's proficiency level and provide:
- level: beginner|intermediate|advanced|expert
- confidence: 0-100 (how confident you are)
- explanation: 2-3 sentences explaining your assessment
- suggestions: Array of 3 book titles to help them progress

Return as JSON: {{"level": "...", "confidence": 0, "explanation": "...", "suggestions": [...]}}"""

        messages = [
            {"role": "system", "content": system_prompt},
            {"role": "user", "content": user_prompt}
        ]

        try:
            response_text = await self._call_gpt(
                messages,
                response_format={"type": "json_object"},
                temperature=0.5  # Lower temperature for classification
            )

            result = json.loads(response_text)

            # Validate response
            required_fields = ["level", "confidence", "explanation", "suggestions"]
            if not all(field in result for field in required_fields):
                raise AIInvalidResponseError("Missing required fields in response")

            return result

        except json.JSONDecodeError as e:
            raise AIInvalidResponseError(f"Invalid JSON response: {str(e)}")

    async def chat_support(
        self,
        message: str,
        conversation_history: List[Dict[str, str]],
        user_context: Dict[str, Any]
    ) -> Dict[str, Any]:
        """Generate AI-powered support chat response using GPT-4."""

        # Build context summary
        orders_summary = "No recent orders"
        if user_context.get("recent_orders"):
            orders_summary = "\n".join([
                f"- Order #{order['order_number']}: {order['status']} (${order['total']})"
                for order in user_context["recent_orders"][:5]
            ])

        system_prompt = f"""You are a helpful customer support agent for BookMart, an online book marketplace.

Context about the user:
{orders_summary}

Your capabilities:
- Answer questions about orders, shipping, and returns
- Provide book recommendations
- Help with account issues
- Explain our policies

Guidelines:
- Be friendly, professional, and concise
- If you don't know something, admit it and suggest contacting human support
- Provide specific actions when possible
- Always verify order details before making claims

Return your response as JSON with:
- response: Your message to the user
- confidence: 0-100 (how confident you are in this answer)
- suggested_actions: Array of action objects {{"action": "type", "data": {{...}}}}
- escalate_to_human: true/false (whether human agent should take over)

Return ONLY valid JSON, no additional text."""

        # Build messages
        messages = [{"role": "system", "content": system_prompt}]

        # Add conversation history (last 10 messages)
        messages.extend(conversation_history[-10:])

        # Add current message
        messages.append({"role": "user", "content": message})

        try:
            response_text = await self._call_gpt(
                messages,
                response_format={"type": "json_object"},
                temperature=0.7,
                max_tokens=500
            )

            result = json.loads(response_text)

            # Validate response
            required_fields = ["response", "confidence", "escalate_to_human"]
            if not all(field in result for field in required_fields):
                raise AIInvalidResponseError("Missing required fields in response")

            # Add defaults
            if "suggested_actions" not in result:
                result["suggested_actions"] = []

            return result

        except json.JSONDecodeError as e:
            raise AIInvalidResponseError(f"Invalid JSON response: {str(e)}")

    async def enhance_search_query(
        self,
        query: str,
        filters: Dict[str, Any] = None
    ) -> Dict[str, Any]:
        """Enhance search query with semantic understanding."""

        system_prompt = """You are a search query interpreter for a book marketplace.
Analyze the user's natural language query and extract structured search parameters.

Return ONLY valid JSON, no additional text."""

        user_prompt = f"""User's search query: "{query}"

Interpret this query and extract:
- interpreted_query: Cleaned/enhanced query for database search
- semantic_keywords: Array of relevant keywords
- suggested_filters: Object with suggested filters (category, min_rating, format, etc.)
- explanation: One sentence explaining your interpretation

Examples:
- "books like Harry Potter but for adults" → fantasy with mature themes
- "easy sci-fi for beginners" → science fiction, beginner level
- "award winning mystery novels" → mystery, bestseller/award winner badge

Return as JSON: {{"interpreted_query": "...", "semantic_keywords": [...], "suggested_filters": {{}}, "explanation": "..."}}"""

        messages = [
            {"role": "system", "content": system_prompt},
            {"role": "user", "content": user_prompt}
        ]

        try:
            response_text = await self._call_gpt(
                messages,
                response_format={"type": "json_object"},
                temperature=0.6
            )

            return json.loads(response_text)

        except json.JSONDecodeError as e:
            raise AIInvalidResponseError(f"Invalid JSON response: {str(e)}")

    async def categorize_book(
        self,
        title: str,
        author: str,
        description: str,
        isbn: str = None
    ) -> Dict[str, Any]:
        """Automatically categorize and tag a book."""

        system_prompt = """You are a book cataloging expert.
Analyze book information and provide accurate categorization.

Available categories: fiction, non_fiction, mystery, romance, science_fiction, fantasy,
biography, history, self_help, business, children, young_adult, poetry, thriller, horror

Return ONLY valid JSON, no additional text."""

        user_prompt = f"""Book Information:
Title: {title}
Author: {author}
ISBN: {isbn or 'N/A'}

Description:
{description[:1000]}  # Limit to avoid token overflow

Provide:
- category: Primary category from the list above
- genres: Array of 2-4 sub-genres
- tags: Array of 5-10 thematic tags (e.g., "magic", "revenge", "dystopian")
- reader_level: beginner|intermediate|advanced|expert
- similar_books: Array of 3-5 similar well-known books (if you know any)

Return as JSON: {{"category": "...", "genres": [...], "tags": [...], "reader_level": "...", "similar_books": [...]}}"""

        messages = [
            {"role": "system", "content": system_prompt},
            {"role": "user", "content": user_prompt}
        ]

        try:
            response_text = await self._call_gpt(
                messages,
                response_format={"type": "json_object"},
                temperature=0.4  # Lower temperature for categorization
            )

            return json.loads(response_text)

        except json.JSONDecodeError as e:
            raise AIInvalidResponseError(f"Invalid JSON response: {str(e)}")

    async def generate_book_summary(
        self,
        title: str,
        author: str,
        description: str
    ) -> Dict[str, Any]:
        """Generate a concise book summary."""

        system_prompt = """You are a book summarization expert.
Create concise, engaging summaries that capture the essence of books.

Return ONLY valid JSON, no additional text."""

        user_prompt = f"""Book: {title} by {author}

Description:
{description[:1500]}

Generate:
- short_summary: One compelling sentence (under 150 characters)
- key_themes: Array of 3-5 main themes
- target_audience: One sentence describing ideal readers

Return as JSON: {{"short_summary": "...", "key_themes": [...], "target_audience": "..."}}"""

        messages = [
            {"role": "system", "content": system_prompt},
            {"role": "user", "content": user_prompt}
        ]

        try:
            response_text = await self._call_gpt(
                messages,
                response_format={"type": "json_object"},
                temperature=0.7
            )

            return json.loads(response_text)

        except json.JSONDecodeError as e:
            raise AIInvalidResponseError(f"Invalid JSON response: {str(e)}")
