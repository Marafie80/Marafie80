"""
Review model for book reviews and ratings.
"""
import uuid
from datetime import datetime
from decimal import Decimal

from sqlalchemy import String, DateTime, Numeric, Integer, Text, ForeignKey, Boolean
from sqlalchemy.dialects.postgresql import UUID
from sqlalchemy.orm import Mapped, mapped_column, relationship

from app.database import Base


class Review(Base):
    """Review model for book ratings and reviews."""

    __tablename__ = "reviews"

    # Primary Key
    id: Mapped[uuid.UUID] = mapped_column(
        UUID(as_uuid=True),
        primary_key=True,
        default=uuid.uuid4,
        index=True
    )

    # Foreign Keys
    user_id: Mapped[uuid.UUID] = mapped_column(
        UUID(as_uuid=True),
        ForeignKey("users.id", ondelete="CASCADE"),
        nullable=False,
        index=True
    )
    book_id: Mapped[uuid.UUID] = mapped_column(
        UUID(as_uuid=True),
        ForeignKey("books.id", ondelete="CASCADE"),
        nullable=False,
        index=True
    )

    # Review Content
    rating: Mapped[Decimal] = mapped_column(
        Numeric(2, 1),
        nullable=False,
        index=True
    )  # 1.0 to 5.0
    title: Mapped[str | None] = mapped_column(String(200))
    comment: Mapped[str] = mapped_column(Text, nullable=False)

    # Verification
    is_verified_purchase: Mapped[bool] = mapped_column(
        Boolean,
        default=False,
        nullable=False
    )

    # Moderation
    is_approved: Mapped[bool] = mapped_column(Boolean, default=True, nullable=False)
    is_flagged: Mapped[bool] = mapped_column(Boolean, default=False, nullable=False)

    # Engagement
    helpful_count: Mapped[int] = mapped_column(Integer, default=0, nullable=False)
    reported_count: Mapped[int] = mapped_column(Integer, default=0, nullable=False)

    # Timestamps
    created_at: Mapped[datetime] = mapped_column(
        DateTime(timezone=True),
        default=datetime.utcnow,
        nullable=False,
        index=True
    )
    updated_at: Mapped[datetime] = mapped_column(
        DateTime(timezone=True),
        default=datetime.utcnow,
        onupdate=datetime.utcnow,
        nullable=False
    )

    # Relationships
    user: Mapped["User"] = relationship("User", back_populates="reviews")
    book: Mapped["Book"] = relationship("Book", back_populates="reviews")

    def __repr__(self) -> str:
        return f"<Review {self.rating}★ by User {self.user_id}>"
