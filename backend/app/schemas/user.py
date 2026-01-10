"""
Pydantic schemas for user-related requests and responses.
"""
from datetime import datetime
from typing import List
import uuid

from pydantic import BaseModel, EmailStr, Field, field_validator

from app.models.user import UserRole, ReaderLevel


class UserBase(BaseModel):
    """Base user schema with common fields."""
    email: EmailStr
    full_name: str = Field(..., min_length=2, max_length=255)


class UserCreate(UserBase):
    """Schema for user registration."""
    password: str = Field(..., min_length=8, max_length=100)
    phone: str | None = Field(None, max_length=20)

    @field_validator("password")
    @classmethod
    def validate_password(cls, v: str) -> str:
        """Validate password strength."""
        if len(v) < 8:
            raise ValueError("Password must be at least 8 characters long")
        if not any(c.isupper() for c in v):
            raise ValueError("Password must contain at least one uppercase letter")
        if not any(c.islower() for c in v):
            raise ValueError("Password must contain at least one lowercase letter")
        if not any(c.isdigit() for c in v):
            raise ValueError("Password must contain at least one digit")
        return v


class UserLogin(BaseModel):
    """Schema for user login."""
    email: EmailStr
    password: str


class UserUpdate(BaseModel):
    """Schema for updating user profile."""
    full_name: str | None = Field(None, min_length=2, max_length=255)
    phone: str | None = Field(None, max_length=20)
    avatar_url: str | None = None
    reader_level: ReaderLevel | None = None
    favorite_genres: List[str] | None = None
    reading_goals: dict | None = None


class UserResponse(UserBase):
    """Schema for user response (public profile)."""
    id: uuid.UUID
    role: UserRole
    phone: str | None = None
    avatar_url: str | None = None
    reader_level: ReaderLevel | None = None
    is_active: bool
    is_verified: bool
    created_at: datetime

    class Config:
        from_attributes = True


class UserProfile(UserResponse):
    """Schema for detailed user profile (private)."""
    favorite_genres: List[str] = []
    reading_goals: dict = {}
    wishlist: List[str] = []
    last_login: datetime | None = None
    updated_at: datetime

    class Config:
        from_attributes = True


class WishlistAdd(BaseModel):
    """Schema for adding book to wishlist."""
    book_id: uuid.UUID


class WishlistRemove(BaseModel):
    """Schema for removing book from wishlist."""
    book_id: uuid.UUID
