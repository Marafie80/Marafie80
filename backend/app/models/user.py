"""
User model for authentication and profile management.
"""
import uuid
from datetime import datetime
from typing import List

from sqlalchemy import String, DateTime, Enum as SQLEnum, Boolean, JSON
from sqlalchemy.dialects.postgresql import UUID
from sqlalchemy.orm import Mapped, mapped_column, relationship
import enum

from app.database import Base


class UserRole(str, enum.Enum):
    """User role enumeration."""
    CUSTOMER = "customer"
    AUTHOR = "author"
    ADMIN = "admin"


class ReaderLevel(str, enum.Enum):
    """Reader level enumeration."""
    BEGINNER = "beginner"
    INTERMEDIATE = "intermediate"
    ADVANCED = "advanced"
    EXPERT = "expert"


class User(Base):
    """User model for authentication and profile."""

    __tablename__ = "users"

    # Primary Key
    id: Mapped[uuid.UUID] = mapped_column(
        UUID(as_uuid=True),
        primary_key=True,
        default=uuid.uuid4,
        index=True
    )

    # Authentication
    email: Mapped[str] = mapped_column(String(255), unique=True, nullable=False, index=True)
    password_hash: Mapped[str] = mapped_column(String(255), nullable=False)
    is_active: Mapped[bool] = mapped_column(Boolean, default=True, nullable=False)
    is_verified: Mapped[bool] = mapped_column(Boolean, default=False, nullable=False)

    # Profile Information
    full_name: Mapped[str] = mapped_column(String(255), nullable=False)
    phone: Mapped[str | None] = mapped_column(String(20))
    avatar_url: Mapped[str | None] = mapped_column(String(500))

    # Role & Permissions
    role: Mapped[UserRole] = mapped_column(
        SQLEnum(UserRole, name="user_role"),
        default=UserRole.CUSTOMER,
        nullable=False
    )

    # Reader Profile
    reader_level: Mapped[ReaderLevel | None] = mapped_column(
        SQLEnum(ReaderLevel, name="reader_level"),
        default=ReaderLevel.BEGINNER
    )
    favorite_genres: Mapped[List[str] | None] = mapped_column(JSON, default=list)
    reading_goals: Mapped[dict | None] = mapped_column(JSON, default=dict)

    # Wishlist (array of book IDs)
    wishlist: Mapped[List[str]] = mapped_column(JSON, default=list, nullable=False)

    # Timestamps
    created_at: Mapped[datetime] = mapped_column(
        DateTime(timezone=True),
        default=datetime.utcnow,
        nullable=False
    )
    updated_at: Mapped[datetime] = mapped_column(
        DateTime(timezone=True),
        default=datetime.utcnow,
        onupdate=datetime.utcnow,
        nullable=False
    )
    last_login: Mapped[datetime | None] = mapped_column(DateTime(timezone=True))

    # Relationships
    orders: Mapped[List["Order"]] = relationship(
        "Order",
        back_populates="user",
        cascade="all, delete-orphan"
    )
    reviews: Mapped[List["Review"]] = relationship(
        "Review",
        back_populates="user",
        cascade="all, delete-orphan"
    )
    support_tickets: Mapped[List["SupportTicket"]] = relationship(
        "SupportTicket",
        back_populates="user",
        cascade="all, delete-orphan"
    )

    def __repr__(self) -> str:
        return f"<User {self.email} ({self.role.value})>"
