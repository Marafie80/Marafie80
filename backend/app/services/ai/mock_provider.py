"""
Mock AI provider for development and testing.
Returns realistic mock data without calling external APIs.
"""
from typing import List, Dict, Any
from datetime import datetime
import random

from app.services.ai.base import AIServiceProvider


class MockAIProvider(AIServiceProvider):
    """
    Mock implementation of AI services for development/testing.
    Does not call external APIs, returns realistic mock data.
    """

    async def generate_recommendations(
        self,
        user_profile: Dict[str, Any],
        reading_history: List[Dict[str, Any]],
        limit: int = 10
    ) -> Dict[str, Any]:
        """Generate mock book recommendations."""

        mock_recommendations = [
            {
                "title": "The Midnight Library",
                "author": "Matt Haig",
                "relevance_score": 92,
                "reason": "Matches your interest in philosophical fiction",
                "genre": "fiction"
            },
            {
                "title": "Project Hail Mary",
                "author": "Andy Weir",
                "relevance_score": 88,
                "reason": "Similar to sci-fi books in your reading history",
                "genre": "science_fiction"
            },
            {
                "title": "The Seven Husbands of Evelyn Hugo",
                "author": "Taylor Jenkins Reid",
                "relevance_score": 85,
                "reason": "Highly rated by readers with similar tastes",
                "genre": "fiction"
            },
            {
                "title": "Educated",
                "author": "Tara Westover",
                "relevance_score": 82,
                "reason": "Compelling memoir matching your non-fiction interests",
                "genre": "biography"
            },
            {
                "title": "The Silent Patient",
                "author": "Alex Michaelides",
                "relevance_score": 80,
                "reason": "Gripping mystery thriller you haven't read yet",
                "genre": "mystery"
            },
        ]

        # Return limited number
        recommendations = mock_recommendations[:limit]

        return {
            "recommendations": recommendations,
            "generated_at": datetime.utcnow().isoformat(),
            "model": "mock-ai-v1"
        }

    async def estimate_reader_level(
        self,
        reading_history: List[Dict[str, Any]],
        favorite_genres: List[str]
    ) -> Dict[str, Any]:
        """Return mock reader level estimation."""

        # Simple heuristic based on reading history length
        history_count = len(reading_history)

        if history_count < 5:
            level = "beginner"
            confidence = 70
        elif history_count < 15:
            level = "intermediate"
            confidence = 80
        elif history_count < 30:
            level = "advanced"
            confidence = 85
        else:
            level = "expert"
            confidence = 90

        return {
            "level": level,
            "confidence": confidence,
            "explanation": f"Based on your reading history of {history_count} books "
                          f"and interest in {', '.join(favorite_genres[:3])}, "
                          f"you demonstrate {level}-level reading proficiency.",
            "suggestions": [
                "Consider exploring literary fiction to enhance your skills",
                "Try reading authors known for complex narratives",
                "Challenge yourself with philosophical works"
            ]
        }

    async def chat_support(
        self,
        message: str,
        conversation_history: List[Dict[str, str]],
        user_context: Dict[str, Any]
    ) -> Dict[str, Any]:
        """Return mock support chat response."""

        # Simple keyword-based responses
        message_lower = message.lower()

        if "order" in message_lower or "track" in message_lower:
            response = "I can help you track your order! Could you please provide your order number? You can find it in your order confirmation email."
            confidence = 85
            actions = [{"action": "view_orders"}]
            escalate = False

        elif "return" in message_lower or "refund" in message_lower:
            response = "We offer a 30-day return policy for all books. I can help you initiate a return. Which order would you like to return?"
            confidence = 90
            actions = [{"action": "view_orders"}, {"action": "open_return"}]
            escalate = False

        elif "recommend" in message_lower or "suggest" in message_lower:
            response = "I'd be happy to recommend some books! What genre are you interested in? Or I can suggest books based on your reading history."
            confidence = 95
            actions = [{"action": "view_recommendations"}]
            escalate = False

        else:
            response = "Thank you for contacting BookMart support! I'm here to help with orders, returns, and book recommendations. What can I assist you with today?"
            confidence = 75
            actions = []
            escalate = False

        return {
            "response": response,
            "confidence": confidence,
            "suggested_actions": actions,
            "escalate_to_human": escalate
        }

    async def enhance_search_query(
        self,
        query: str,
        filters: Dict[str, Any] = None
    ) -> Dict[str, Any]:
        """Return mock search enhancement."""

        query_lower = query.lower()

        # Mock interpretation
        keywords = []
        suggested_filters = {}

        if "like" in query_lower and "but" in query_lower:
            keywords = ["similar", "alternative", "comparable"]
            suggested_filters["recommendation_based"] = True

        if "easy" in query_lower or "beginner" in query_lower:
            keywords.append("simple")
            suggested_filters["reader_level"] = "beginner"

        if "award" in query_lower:
            keywords.append("acclaimed")
            suggested_filters["badge"] = "winner"

        return {
            "interpreted_query": query,
            "semantic_keywords": keywords or ["general"],
            "suggested_filters": suggested_filters,
            "explanation": f"Searching for books related to: {query}"
        }

    async def categorize_book(
        self,
        title: str,
        author: str,
        description: str,
        isbn: str = None
    ) -> Dict[str, Any]:
        """Return mock book categorization."""

        # Simple keyword-based categorization
        desc_lower = description.lower()
        title_lower = title.lower()

        category = "fiction"
        genres = ["general"]
        tags = ["contemporary", "bestseller"]

        if any(word in desc_lower or word in title_lower for word in ["magic", "wizard", "dragon"]):
            category = "fantasy"
            genres = ["epic fantasy", "magic"]
            tags = ["magic", "adventure", "quest"]

        elif any(word in desc_lower or word in title_lower for word in ["murder", "detective", "crime"]):
            category = "mystery"
            genres = ["detective", "thriller"]
            tags = ["suspense", "investigation", "crime"]

        elif any(word in desc_lower or word in title_lower for word in ["space", "alien", "future"]):
            category = "science_fiction"
            genres = ["space opera", "hard sci-fi"]
            tags = ["technology", "future", "exploration"]

        return {
            "category": category,
            "genres": genres,
            "tags": tags,
            "reader_level": "intermediate",
            "similar_books": ["Similar Book 1", "Similar Book 2", "Similar Book 3"]
        }

    async def generate_book_summary(
        self,
        title: str,
        author: str,
        description: str
    ) -> Dict[str, Any]:
        """Return mock book summary."""

        # Generate simple summary
        first_sentence = description.split('.')[0] if description else f"A book by {author}"

        return {
            "short_summary": first_sentence[:150],
            "key_themes": ["identity", "perseverance", "love"],
            "target_audience": "Readers who enjoy thought-provoking fiction with compelling characters"
        }
