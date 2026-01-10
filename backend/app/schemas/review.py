"""
Pydantic schemas for review-related requests and responses.
"""
from datetime import datetime
from decimal import Decimal
from typing import List
import uuid

from pydantic import BaseModel, Field

from app.schemas.user import UserResponse


class ReviewCreate(BaseModel):
    """Schema for creating a review."""
    book_id: uuid.UUID
    rating: Decimal = Field(..., ge=1.0, le=5.0, decimal_places=1)
    title: str | None = Field(None, max_length=200)
    comment: str = Field(..., min_length=10, max_length=5000)


class ReviewUpdate(BaseModel):
    """Schema for updating a review."""
    rating: Decimal | None = Field(None, ge=1.0, le=5.0, decimal_places=1)
    title: str | None = Field(None, max_length=200)
    comment: str | None = Field(None, min_length=10, max_length=5000)


class ReviewResponse(BaseModel):
    """Schema for review response."""
    id: uuid.UUID
    user_id: uuid.UUID
    book_id: uuid.UUID
    rating: Decimal
    title: str | None
    comment: str
    is_verified_purchase: bool
    is_approved: bool
    helpful_count: int
    created_at: datetime
    updated_at: datetime

    class Config:
        from_attributes = True


class ReviewWithUser(ReviewResponse):
    """Schema for review with user information."""
    user: UserResponse


class ReviewListResponse(BaseModel):
    """Schema for paginated review list response."""
    items: List[ReviewResponse]
    total: int
    page: int
    page_size: int
    pages: int
    average_rating: Decimal
