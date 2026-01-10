"""
AI service layer for book recommendations, reader level estimation, and support chat.
"""
from app.services.ai.factory import get_ai_service

__all__ = ["get_ai_service"]
