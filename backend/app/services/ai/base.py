"""
Abstract base class for AI service providers.
Defines the interface that all AI providers must implement.
"""
from abc import ABC, abstractmethod
from typing import List, Dict, Any
from datetime import datetime


class AIServiceProvider(ABC):
    """
    Abstract base class for AI service providers.

    This interface allows swapping between different AI providers
    (OpenAI, Anthropic Claude, local models, etc.) without changing
    business logic.
    """

    @abstractmethod
    async def generate_recommendations(
        self,
        user_profile: Dict[str, Any],
        reading_history: List[Dict[str, Any]],
        limit: int = 10
    ) -> Dict[str, Any]:
        """
        Generate personalized book recommendations.

        Args:
            user_profile: User profile data (reader_level, favorite_genres, etc.)
            reading_history: List of books user has read/purchased
            limit: Maximum number of recommendations

        Returns:
            {
                "recommendations": [
                    {
                        "book_id": "uuid",
                        "title": "Book Title",
                        "author": "Author Name",
                        "relevance_score": 85,  # 0-100
                        "reason": "Matches your interest in fantasy"
                    },
                    ...
                ],
                "generated_at": "2024-01-10T12:00:00Z",
                "model": "gpt-4-turbo"
            }
        """
        pass

    @abstractmethod
    async def estimate_reader_level(
        self,
        reading_history: List[Dict[str, Any]],
        favorite_genres: List[str]
    ) -> Dict[str, Any]:
        """
        Estimate user's reading proficiency level.

        Args:
            reading_history: Books the user has read
            favorite_genres: User's favorite genres

        Returns:
            {
                "level": "intermediate",  # beginner|intermediate|advanced|expert
                "confidence": 85,  # 0-100
                "explanation": "Based on your reading history...",
                "suggestions": [
                    {
                        "book_title": "Next Level Book",
                        "reason": "Will challenge your skills"
                    }
                ]
            }
        """
        pass

    @abstractmethod
    async def chat_support(
        self,
        message: str,
        conversation_history: List[Dict[str, str]],
        user_context: Dict[str, Any]
    ) -> Dict[str, Any]:
        """
        Generate AI-powered support chat response.

        Args:
            message: User's current message
            conversation_history: Previous messages [{"role": "user"|"assistant", "content": "..."}]
            user_context: User data (orders, books, etc.) for context

        Returns:
            {
                "response": "AI-generated response text",
                "confidence": 90,  # 0-100
                "suggested_actions": [
                    {"action": "view_order", "order_id": "123"},
                    {"action": "contact_agent"}
                ],
                "escalate_to_human": false
            }
        """
        pass

    @abstractmethod
    async def enhance_search_query(
        self,
        query: str,
        filters: Dict[str, Any] = None
    ) -> Dict[str, Any]:
        """
        Enhance search with semantic understanding.

        Args:
            query: User's natural language search query
            filters: Additional search filters

        Returns:
            {
                "interpreted_query": "Enhanced query for database",
                "semantic_keywords": ["keyword1", "keyword2"],
                "suggested_filters": {
                    "category": "fantasy",
                    "min_rating": 4.0
                },
                "explanation": "I interpreted your query as..."
            }
        """
        pass

    @abstractmethod
    async def categorize_book(
        self,
        title: str,
        author: str,
        description: str,
        isbn: str = None
    ) -> Dict[str, Any]:
        """
        Automatically categorize and tag a book (admin tool).

        Args:
            title: Book title
            author: Book author
            description: Book description
            isbn: ISBN (optional, for lookup)

        Returns:
            {
                "category": "fiction",
                "genres": ["fantasy", "adventure"],
                "tags": ["magic", "coming-of-age"],
                "reader_level": "intermediate",
                "similar_books": ["Book1", "Book2"]
            }
        """
        pass

    @abstractmethod
    async def generate_book_summary(
        self,
        title: str,
        author: str,
        description: str
    ) -> Dict[str, Any]:
        """
        Generate a concise book summary.

        Args:
            title: Book title
            author: Book author
            description: Full description

        Returns:
            {
                "short_summary": "One sentence summary",
                "key_themes": ["theme1", "theme2"],
                "target_audience": "Young adults interested in..."
            }
        """
        pass


class AIServiceError(Exception):
    """Base exception for AI service errors."""
    pass


class AIProviderError(AIServiceError):
    """Exception raised when AI provider API fails."""
    pass


class AIRateLimitError(AIServiceError):
    """Exception raised when rate limit is exceeded."""
    pass


class AIInvalidResponseError(AIServiceError):
    """Exception raised when AI returns invalid response."""
    pass
