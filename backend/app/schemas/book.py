"""
Pydantic schemas for book-related requests and responses.
"""
from datetime import datetime
from decimal import Decimal
from typing import List
import uuid

from pydantic import BaseModel, Field, field_validator

from app.models.book import BookFormat, BookCategory, BookBadge


class BookBase(BaseModel):
    """Base book schema with common fields."""
    isbn: str = Field(..., min_length=10, max_length=20)
    title: str = Field(..., min_length=1, max_length=500)
    subtitle: str | None = Field(None, max_length=500)
    author: str = Field(..., min_length=1, max_length=255)
    publisher: str | None = Field(None, max_length=255)
    description: str = Field(..., min_length=10)
    language: str = Field(default="en", max_length=10)
    page_count: int | None = Field(None, gt=0)
    category: BookCategory
    genres: List[str] = Field(default_factory=list)
    tags: List[str] = Field(default_factory=list)
    format: BookFormat
    price: Decimal = Field(..., ge=0, decimal_places=2)
    original_price: Decimal | None = Field(None, ge=0, decimal_places=2)
    is_free: bool = False
    cover_url: str = Field(..., max_length=500)
    preview_url: str | None = Field(None, max_length=500)


class BookCreate(BookBase):
    """Schema for creating a new book."""
    file_url: str | None = Field(None, max_length=500)
    stock_quantity: int = Field(default=0, ge=0)
    is_available: bool = True
    badge: BookBadge | None = None
    is_featured: bool = False
    is_bestseller: bool = False

    @field_validator("original_price")
    @classmethod
    def validate_original_price(cls, v: Decimal | None, info) -> Decimal | None:
        """Ensure original price is greater than current price if provided."""
        if v is not None and "price" in info.data:
            price = info.data["price"]
            if v <= price:
                raise ValueError("Original price must be greater than current price")
        return v


class BookUpdate(BaseModel):
    """Schema for updating a book."""
    title: str | None = Field(None, min_length=1, max_length=500)
    subtitle: str | None = Field(None, max_length=500)
    author: str | None = Field(None, min_length=1, max_length=255)
    publisher: str | None = Field(None, max_length=255)
    description: str | None = Field(None, min_length=10)
    language: str | None = Field(None, max_length=10)
    page_count: int | None = Field(None, gt=0)
    category: BookCategory | None = None
    genres: List[str] | None = None
    tags: List[str] | None = None
    price: Decimal | None = Field(None, ge=0, decimal_places=2)
    original_price: Decimal | None = Field(None, ge=0, decimal_places=2)
    is_free: bool | None = None
    cover_url: str | None = Field(None, max_length=500)
    preview_url: str | None = Field(None, max_length=500)
    file_url: str | None = Field(None, max_length=500)
    stock_quantity: int | None = Field(None, ge=0)
    is_available: bool | None = None
    badge: BookBadge | None = None
    is_featured: bool | None = None
    is_bestseller: bool | None = None


class BookResponse(BookBase):
    """Schema for book response."""
    id: uuid.UUID
    rating: Decimal
    review_count: int
    stock_quantity: int
    is_available: bool
    badge: BookBadge | None = None
    is_featured: bool
    is_bestseller: bool
    view_count: int
    purchase_count: int
    created_at: datetime
    updated_at: datetime
    discount_percentage: int | None = None

    class Config:
        from_attributes = True


class BookListResponse(BaseModel):
    """Schema for paginated book list response."""
    items: List[BookResponse]
    total: int
    page: int
    page_size: int
    pages: int


class BookSearchQuery(BaseModel):
    """Schema for book search query parameters."""
    q: str | None = Field(None, description="Search query")
    category: BookCategory | None = Field(None, description="Filter by category")
    format: BookFormat | None = Field(None, description="Filter by format")
    min_price: Decimal | None = Field(None, ge=0, description="Minimum price")
    max_price: Decimal | None = Field(None, ge=0, description="Maximum price")
    is_free: bool | None = Field(None, description="Filter free books")
    is_featured: bool | None = Field(None, description="Filter featured books")
    is_bestseller: bool | None = Field(None, description="Filter bestsellers")
    sort_by: str = Field(default="created_at", description="Sort field")
    sort_order: str = Field(default="desc", description="Sort order (asc/desc)")
    page: int = Field(default=1, ge=1, description="Page number")
    page_size: int = Field(default=20, ge=1, le=100, description="Items per page")
