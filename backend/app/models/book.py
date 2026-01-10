"""
Book model for the marketplace catalog.
"""
import uuid
from datetime import datetime
from decimal import Decimal
from typing import List

from sqlalchemy import String, DateTime, Numeric, Integer, Text, Boolean, Enum as SQLEnum, JSON
from sqlalchemy.dialects.postgresql import UUID
from sqlalchemy.orm import Mapped, mapped_column, relationship
import enum

from app.database import Base


class BookFormat(str, enum.Enum):
    """Book format enumeration."""
    DIGITAL = "digital"
    PAPERBACK = "paperback"
    HARDCOVER = "hardcover"
    AUDIOBOOK = "audiobook"


class BookCategory(str, enum.Enum):
    """Book category enumeration."""
    FICTION = "fiction"
    NON_FICTION = "non_fiction"
    MYSTERY = "mystery"
    ROMANCE = "romance"
    SCIENCE_FICTION = "science_fiction"
    FANTASY = "fantasy"
    BIOGRAPHY = "biography"
    HISTORY = "history"
    SELF_HELP = "self_help"
    BUSINESS = "business"
    CHILDREN = "children"
    YOUNG_ADULT = "young_adult"
    POETRY = "poetry"
    THRILLER = "thriller"
    HORROR = "horror"


class BookBadge(str, enum.Enum):
    """Book badge enumeration."""
    NEW = "new"
    BESTSELLER = "bestseller"
    FEATURED = "featured"
    AWARD_WINNER = "winner"
    AWARD_NOMINEE = "nominee"


class Book(Base):
    """Book model for marketplace catalog."""

    __tablename__ = "books"

    # Primary Key
    id: Mapped[uuid.UUID] = mapped_column(
        UUID(as_uuid=True),
        primary_key=True,
        default=uuid.uuid4,
        index=True
    )

    # Book Identifiers
    isbn: Mapped[str] = mapped_column(String(20), unique=True, nullable=False, index=True)
    title: Mapped[str] = mapped_column(String(500), nullable=False, index=True)
    subtitle: Mapped[str | None] = mapped_column(String(500))
    author: Mapped[str] = mapped_column(String(255), nullable=False, index=True)
    publisher: Mapped[str | None] = mapped_column(String(255))
    publication_date: Mapped[datetime | None] = mapped_column(DateTime(timezone=True))

    # Description & Content
    description: Mapped[str] = mapped_column(Text, nullable=False)
    language: Mapped[str] = mapped_column(String(10), default="en", nullable=False)
    page_count: Mapped[int | None] = mapped_column(Integer)

    # Categorization
    category: Mapped[BookCategory] = mapped_column(
        SQLEnum(BookCategory, name="book_category"),
        nullable=False,
        index=True
    )
    genres: Mapped[List[str]] = mapped_column(JSON, default=list, nullable=False)
    tags: Mapped[List[str]] = mapped_column(JSON, default=list, nullable=False)

    # Format & Pricing
    format: Mapped[BookFormat] = mapped_column(
        SQLEnum(BookFormat, name="book_format"),
        nullable=False,
        index=True
    )
    price: Mapped[Decimal] = mapped_column(
        Numeric(10, 2),
        nullable=False,
        index=True
    )
    original_price: Mapped[Decimal | None] = mapped_column(Numeric(10, 2))
    is_free: Mapped[bool] = mapped_column(Boolean, default=False, nullable=False, index=True)

    # Media
    cover_url: Mapped[str] = mapped_column(String(500), nullable=False)
    preview_url: Mapped[str | None] = mapped_column(String(500))  # Preview PDF/EPUB
    file_url: Mapped[str | None] = mapped_column(String(500))  # Full book file (for digital)

    # Ratings & Reviews (denormalized for performance)
    rating: Mapped[Decimal] = mapped_column(
        Numeric(3, 2),
        default=Decimal("0.0"),
        nullable=False,
        index=True
    )
    review_count: Mapped[int] = mapped_column(Integer, default=0, nullable=False)

    # Stock & Availability
    stock_quantity: Mapped[int] = mapped_column(Integer, default=0, nullable=False)
    is_available: Mapped[bool] = mapped_column(Boolean, default=True, nullable=False, index=True)

    # Features & Badges
    badge: Mapped[BookBadge | None] = mapped_column(
        SQLEnum(BookBadge, name="book_badge"),
        index=True
    )
    is_featured: Mapped[bool] = mapped_column(Boolean, default=False, nullable=False, index=True)
    is_bestseller: Mapped[bool] = mapped_column(Boolean, default=False, nullable=False, index=True)

    # Metadata
    view_count: Mapped[int] = mapped_column(Integer, default=0, nullable=False)
    purchase_count: Mapped[int] = mapped_column(Integer, default=0, nullable=False)

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

    # Relationships
    reviews: Mapped[List["Review"]] = relationship(
        "Review",
        back_populates="book",
        cascade="all, delete-orphan"
    )

    def __repr__(self) -> str:
        return f"<Book {self.title} by {self.author}>"

    @property
    def discount_percentage(self) -> int | None:
        """Calculate discount percentage if original price exists."""
        if self.original_price and self.original_price > self.price:
            return int(((self.original_price - self.price) / self.original_price) * 100)
        return None
