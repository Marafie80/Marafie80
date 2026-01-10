"""
AI-powered endpoints for recommendations, reader level estimation, and support chat.
"""
from typing import Annotated, List, Dict, Any
import uuid

from fastapi import APIRouter, Depends, HTTPException, status, Query
from sqlalchemy.ext.asyncio import AsyncSession
from sqlalchemy import select
from pydantic import BaseModel, Field

from app.database import get_db
from app.models.user import User
from app.models.book import Book
from app.models.order import Order
from app.core.dependencies import get_current_active_user
from app.services.ai import get_ai_service
from app.services.ai.base import AIServiceProvider, AIServiceError
from app.config import settings

router = APIRouter(prefix="/ai", tags=["AI Features"])


# ============================================================================
# Pydantic Schemas
# ============================================================================

class RecommendationItem(BaseModel):
    """Single book recommendation."""
    title: str
    author: str
    relevance_score: int = Field(..., ge=0, le=100)
    reason: str
    genre: str


class RecommendationsResponse(BaseModel):
    """Book recommendations response."""
    recommendations: List[RecommendationItem]
    generated_at: str
    model: str


class ReaderLevelResponse(BaseModel):
    """Reader level estimation response."""
    level: str
    confidence: int = Field(..., ge=0, le=100)
    explanation: str
    suggestions: List[str]


class ChatMessage(BaseModel):
    """Chat message."""
    role: str = Field(..., pattern="^(user|assistant)$")
    content: str


class ChatRequest(BaseModel):
    """Support chat request."""
    message: str = Field(..., min_length=1, max_length=1000)
    conversation_history: List[ChatMessage] = Field(default_factory=list)


class ChatResponse(BaseModel):
    """Support chat response."""
    response: str
    confidence: int = Field(..., ge=0, le=100)
    suggested_actions: List[Dict[str, Any]] = Field(default_factory=list)
    escalate_to_human: bool


class SearchEnhanceRequest(BaseModel):
    """Search enhancement request."""
    query: str = Field(..., min_length=1)
    filters: Dict[str, Any] = Field(default_factory=dict)


class SearchEnhanceResponse(BaseModel):
    """Search enhancement response."""
    interpreted_query: str
    semantic_keywords: List[str]
    suggested_filters: Dict[str, Any]
    explanation: str


class BookCategorizationRequest(BaseModel):
    """Book categorization request."""
    title: str
    author: str
    description: str
    isbn: str | None = None


class BookCategorizationResponse(BaseModel):
    """Book categorization response."""
    category: str
    genres: List[str]
    tags: List[str]
    reader_level: str
    similar_books: List[str]


# ============================================================================
# Endpoints
# ============================================================================

@router.get("/recommendations", response_model=RecommendationsResponse)
async def get_recommendations(
    current_user: Annotated[User, Depends(get_current_active_user)],
    db: Annotated[AsyncSession, Depends(get_db)],
    ai_service: Annotated[AIServiceProvider, Depends(get_ai_service)],
    limit: int = Query(default=10, ge=1, le=50),
) -> RecommendationsResponse:
    """
    Get personalized book recommendations based on user profile and reading history.

    Args:
        current_user: Current authenticated user
        db: Database session
        ai_service: AI service provider
        limit: Maximum number of recommendations

    Returns:
        List of personalized book recommendations

    Raises:
        HTTPException: If AI service is disabled or fails
    """
    if not settings.AI_RECOMMENDATIONS_ENABLED:
        raise HTTPException(
            status_code=status.HTTP_503_SERVICE_UNAVAILABLE,
            detail="AI recommendations are currently disabled"
        )

    try:
        # Build user profile
        user_profile = {
            "user_id": str(current_user.id),
            "reader_level": current_user.reader_level.value if current_user.reader_level else "intermediate",
            "favorite_genres": current_user.favorite_genres or [],
            "reading_goals": current_user.reading_goals or {},
        }

        # Get reading history (from orders - books they've purchased)
        result = await db.execute(
            select(Order)
            .where(Order.user_id == current_user.id)
            .order_by(Order.created_at.desc())
            .limit(20)
        )
        orders = result.scalars().all()

        # Extract book IDs from orders (simplified - would need to join order_items)
        reading_history = []
        for order in orders:
            # In a real implementation, you'd join with order_items and books
            # For now, we'll use a simplified approach
            reading_history.append({
                "title": f"Book from Order {order.order_number}",
                "author": "Various",
                "category": "general"
            })

        # Generate recommendations
        recommendations = await ai_service.generate_recommendations(
            user_profile=user_profile,
            reading_history=reading_history,
            limit=limit
        )

        return RecommendationsResponse(**recommendations)

    except AIServiceError as e:
        raise HTTPException(
            status_code=status.HTTP_500_INTERNAL_SERVER_ERROR,
            detail=f"AI service error: {str(e)}"
        )


@router.post("/estimate-reader-level", response_model=ReaderLevelResponse)
async def estimate_reader_level(
    current_user: Annotated[User, Depends(get_current_active_user)],
    db: Annotated[AsyncSession, Depends(get_db)],
    ai_service: Annotated[AIServiceProvider, Depends(get_ai_service)],
) -> ReaderLevelResponse:
    """
    Estimate user's reading proficiency level based on their reading history.

    Args:
        current_user: Current authenticated user
        db: Database session
        ai_service: AI service provider

    Returns:
        Estimated reader level with confidence and suggestions

    Raises:
        HTTPException: If AI service is disabled or fails
    """
    if not settings.AI_READER_LEVEL_ENABLED:
        raise HTTPException(
            status_code=status.HTTP_503_SERVICE_UNAVAILABLE,
            detail="AI reader level estimation is currently disabled"
        )

    try:
        # Get reading history
        result = await db.execute(
            select(Order)
            .where(Order.user_id == current_user.id)
            .order_by(Order.created_at.desc())
            .limit(30)
        )
        orders = result.scalars().all()

        reading_history = []
        for order in orders:
            reading_history.append({
                "title": f"Book from Order {order.order_number}",
                "author": "Various"
            })

        # Estimate level
        result = await ai_service.estimate_reader_level(
            reading_history=reading_history,
            favorite_genres=current_user.favorite_genres or []
        )

        # Optionally update user's reader level
        # current_user.reader_level = result["level"]
        # await db.commit()

        return ReaderLevelResponse(**result)

    except AIServiceError as e:
        raise HTTPException(
            status_code=status.HTTP_500_INTERNAL_SERVER_ERROR,
            detail=f"AI service error: {str(e)}"
        )


@router.post("/support/chat", response_model=ChatResponse)
async def chat_support(
    request: ChatRequest,
    current_user: Annotated[User, Depends(get_current_active_user)],
    db: Annotated[AsyncSession, Depends(get_db)],
    ai_service: Annotated[AIServiceProvider, Depends(get_ai_service)],
) -> ChatResponse:
    """
    Get AI-powered support chat response.

    Args:
        request: Chat request with message and history
        current_user: Current authenticated user
        db: Database session
        ai_service: AI service provider

    Returns:
        AI-generated response with suggested actions

    Raises:
        HTTPException: If AI service is disabled or fails
    """
    if not settings.AI_SUPPORT_CHAT_ENABLED:
        raise HTTPException(
            status_code=status.HTTP_503_SERVICE_UNAVAILABLE,
            detail="AI support chat is currently disabled"
        )

    try:
        # Get user context (recent orders for context)
        result = await db.execute(
            select(Order)
            .where(Order.user_id == current_user.id)
            .order_by(Order.created_at.desc())
            .limit(5)
        )
        orders = result.scalars().all()

        user_context = {
            "user_id": str(current_user.id),
            "email": current_user.email,
            "recent_orders": [
                {
                    "order_number": order.order_number,
                    "status": order.status.value,
                    "total": float(order.total),
                    "created_at": order.created_at.isoformat()
                }
                for order in orders
            ]
        }

        # Convert conversation history to dict
        conversation_history = [
            {"role": msg.role, "content": msg.content}
            for msg in request.conversation_history
        ]

        # Get AI response
        result = await ai_service.chat_support(
            message=request.message,
            conversation_history=conversation_history,
            user_context=user_context
        )

        return ChatResponse(**result)

    except AIServiceError as e:
        raise HTTPException(
            status_code=status.HTTP_500_INTERNAL_SERVER_ERROR,
            detail=f"AI service error: {str(e)}"
        )


@router.post("/search/enhance", response_model=SearchEnhanceResponse)
async def enhance_search(
    request: SearchEnhanceRequest,
    ai_service: Annotated[AIServiceProvider, Depends(get_ai_service)],
) -> SearchEnhanceResponse:
    """
    Enhance search query with semantic understanding.

    Args:
        request: Search enhancement request
        ai_service: AI service provider

    Returns:
        Enhanced search query with suggestions

    Raises:
        HTTPException: If AI service fails
    """
    try:
        result = await ai_service.enhance_search_query(
            query=request.query,
            filters=request.filters
        )

        return SearchEnhanceResponse(**result)

    except AIServiceError as e:
        raise HTTPException(
            status_code=status.HTTP_500_INTERNAL_SERVER_ERROR,
            detail=f"AI service error: {str(e)}"
        )


@router.post("/admin/categorize-book", response_model=BookCategorizationResponse)
async def categorize_book(
    request: BookCategorizationRequest,
    current_user: Annotated[User, Depends(get_current_active_user)],
    ai_service: Annotated[AIServiceProvider, Depends(get_ai_service)],
) -> BookCategorizationResponse:
    """
    Automatically categorize and tag a book using AI (admin only).

    Args:
        request: Book categorization request
        current_user: Current admin user
        ai_service: AI service provider

    Returns:
        Book categorization and tagging suggestions

    Raises:
        HTTPException: If user is not admin or AI service fails
    """
    # Check admin permission
    from app.models.user import UserRole
    if current_user.role != UserRole.ADMIN:
        raise HTTPException(
            status_code=status.HTTP_403_FORBIDDEN,
            detail="Admin access required"
        )

    try:
        result = await ai_service.categorize_book(
            title=request.title,
            author=request.author,
            description=request.description,
            isbn=request.isbn
        )

        return BookCategorizationResponse(**result)

    except AIServiceError as e:
        raise HTTPException(
            status_code=status.HTTP_500_INTERNAL_SERVER_ERROR,
            detail=f"AI service error: {str(e)}"
        )


@router.get("/status")
async def ai_status(
    ai_service: Annotated[AIServiceProvider, Depends(get_ai_service)]
) -> Dict[str, Any]:
    """
    Get AI service status and configuration.

    Returns:
        AI service status information
    """
    return {
        "provider": settings.AI_PROVIDER,
        "features": {
            "recommendations": settings.AI_RECOMMENDATIONS_ENABLED,
            "reader_level": settings.AI_READER_LEVEL_ENABLED,
            "support_chat": settings.AI_SUPPORT_CHAT_ENABLED,
        },
        "cache_enabled": settings.AI_CACHE_ENABLED,
        "model": getattr(settings, "OPENAI_MODEL", "mock"),
        "status": "operational"
    }
