"""
Factory for creating AI service provider instances.
Supports multiple providers and caching.
"""
from functools import lru_cache
from typing import Optional

from app.services.ai.base import AIServiceProvider
from app.services.ai.openai_provider import OpenAIProvider, OPENAI_AVAILABLE
from app.services.ai.mock_provider import MockAIProvider
from app.config import settings


class AIServiceFactory:
    """
    Factory for creating AI service providers.
    Handles provider selection based on configuration.
    """

    _instance: Optional[AIServiceProvider] = None

    @classmethod
    def create_provider(cls, provider_name: str = None) -> AIServiceProvider:
        """
        Create an AI service provider instance.

        Args:
            provider_name: Provider to use ("openai", "mock").
                          If None, uses AI_PROVIDER from settings.

        Returns:
            AIServiceProvider instance

        Raises:
            ValueError: If provider is unknown or not configured
        """
        provider_name = provider_name or getattr(settings, "AI_PROVIDER", "mock")

        if provider_name == "openai":
            if not OPENAI_AVAILABLE:
                print("⚠️  OpenAI package not installed, falling back to mock provider")
                return MockAIProvider()

            api_key = getattr(settings, "OPENAI_API_KEY", None)
            if not api_key:
                print("⚠️  OPENAI_API_KEY not configured, falling back to mock provider")
                return MockAIProvider()

            model = getattr(settings, "OPENAI_MODEL", "gpt-4-turbo")
            print(f"✅ Using OpenAI provider with model: {model}")
            return OpenAIProvider(api_key=api_key, model=model)

        elif provider_name == "mock":
            print("✅ Using Mock AI provider (no external API calls)")
            return MockAIProvider()

        else:
            raise ValueError(
                f"Unknown AI provider: {provider_name}. "
                f"Supported providers: openai, mock"
            )

    @classmethod
    def get_instance(cls) -> AIServiceProvider:
        """
        Get singleton instance of AI service provider.

        Returns:
            Cached AIServiceProvider instance
        """
        if cls._instance is None:
            cls._instance = cls.create_provider()
        return cls._instance

    @classmethod
    def reset_instance(cls):
        """Reset the singleton instance (useful for testing)."""
        cls._instance = None


@lru_cache
def get_ai_service() -> AIServiceProvider:
    """
    Get AI service provider instance (dependency injection).

    Usage:
        from app.services.ai import get_ai_service

        @app.get("/recommendations")
        async def get_recommendations(
            ai_service: AIServiceProvider = Depends(get_ai_service)
        ):
            recommendations = await ai_service.generate_recommendations(...)
            return recommendations

    Returns:
        AIServiceProvider instance
    """
    return AIServiceFactory.get_instance()
