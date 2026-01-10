"""
Order and OrderItem models for purchase transactions.
"""
import uuid
from datetime import datetime
from decimal import Decimal
from typing import List

from sqlalchemy import String, DateTime, Numeric, Integer, Text, Enum as SQLEnum, ForeignKey, JSON
from sqlalchemy.dialects.postgresql import UUID
from sqlalchemy.orm import Mapped, mapped_column, relationship
import enum

from app.database import Base


class OrderStatus(str, enum.Enum):
    """Order status enumeration."""
    PENDING = "pending"
    PROCESSING = "processing"
    SHIPPED = "shipped"
    DELIVERED = "delivered"
    CANCELLED = "cancelled"
    REFUNDED = "refunded"


class PaymentMethod(str, enum.Enum):
    """Payment method enumeration."""
    CREDIT_CARD = "credit_card"
    DEBIT_CARD = "debit_card"
    PAYPAL = "paypal"
    STRIPE = "stripe"
    WALLET = "wallet"


class PaymentStatus(str, enum.Enum):
    """Payment status enumeration."""
    PENDING = "pending"
    COMPLETED = "completed"
    FAILED = "failed"
    REFUNDED = "refunded"


class Order(Base):
    """Order model for purchase transactions."""

    __tablename__ = "orders"

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

    # Order Information
    order_number: Mapped[str] = mapped_column(
        String(50),
        unique=True,
        nullable=False,
        index=True
    )
    status: Mapped[OrderStatus] = mapped_column(
        SQLEnum(OrderStatus, name="order_status"),
        default=OrderStatus.PENDING,
        nullable=False,
        index=True
    )

    # Pricing
    subtotal: Mapped[Decimal] = mapped_column(Numeric(10, 2), nullable=False)
    tax: Mapped[Decimal] = mapped_column(Numeric(10, 2), default=Decimal("0.0"), nullable=False)
    shipping_cost: Mapped[Decimal] = mapped_column(Numeric(10, 2), default=Decimal("0.0"), nullable=False)
    discount: Mapped[Decimal] = mapped_column(Numeric(10, 2), default=Decimal("0.0"), nullable=False)
    total: Mapped[Decimal] = mapped_column(Numeric(10, 2), nullable=False)

    # Payment
    payment_method: Mapped[PaymentMethod] = mapped_column(
        SQLEnum(PaymentMethod, name="payment_method"),
        nullable=False
    )
    payment_status: Mapped[PaymentStatus] = mapped_column(
        SQLEnum(PaymentStatus, name="payment_status"),
        default=PaymentStatus.PENDING,
        nullable=False,
        index=True
    )
    payment_id: Mapped[str | None] = mapped_column(String(255))  # External payment gateway ID
    paid_at: Mapped[datetime | None] = mapped_column(DateTime(timezone=True))

    # Shipping Address (denormalized snapshot)
    shipping_address: Mapped[dict] = mapped_column(JSON, nullable=False)

    # Tracking
    tracking_number: Mapped[str | None] = mapped_column(String(100))
    shipped_at: Mapped[datetime | None] = mapped_column(DateTime(timezone=True))
    delivered_at: Mapped[datetime | None] = mapped_column(DateTime(timezone=True))

    # Notes
    customer_notes: Mapped[str | None] = mapped_column(Text)
    admin_notes: Mapped[str | None] = mapped_column(Text)

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
    cancelled_at: Mapped[datetime | None] = mapped_column(DateTime(timezone=True))

    # Relationships
    user: Mapped["User"] = relationship("User", back_populates="orders")
    items: Mapped[List["OrderItem"]] = relationship(
        "OrderItem",
        back_populates="order",
        cascade="all, delete-orphan"
    )

    def __repr__(self) -> str:
        return f"<Order {self.order_number} - {self.status.value}>"


class OrderItem(Base):
    """Order item model for individual books in an order."""

    __tablename__ = "order_items"

    # Primary Key
    id: Mapped[uuid.UUID] = mapped_column(
        UUID(as_uuid=True),
        primary_key=True,
        default=uuid.uuid4,
        index=True
    )

    # Foreign Keys
    order_id: Mapped[uuid.UUID] = mapped_column(
        UUID(as_uuid=True),
        ForeignKey("orders.id", ondelete="CASCADE"),
        nullable=False,
        index=True
    )
    book_id: Mapped[uuid.UUID] = mapped_column(
        UUID(as_uuid=True),
        ForeignKey("books.id", ondelete="SET NULL"),
        nullable=True,
        index=True
    )

    # Book Snapshot (denormalized for historical accuracy)
    book_title: Mapped[str] = mapped_column(String(500), nullable=False)
    book_author: Mapped[str] = mapped_column(String(255), nullable=False)
    book_isbn: Mapped[str] = mapped_column(String(20), nullable=False)
    book_cover_url: Mapped[str] = mapped_column(String(500), nullable=False)
    book_format: Mapped[str] = mapped_column(String(20), nullable=False)

    # Pricing (snapshot at time of purchase)
    unit_price: Mapped[Decimal] = mapped_column(Numeric(10, 2), nullable=False)
    quantity: Mapped[int] = mapped_column(Integer, default=1, nullable=False)
    subtotal: Mapped[Decimal] = mapped_column(Numeric(10, 2), nullable=False)

    # Download information (for digital books)
    download_url: Mapped[str | None] = mapped_column(String(500))
    download_count: Mapped[int] = mapped_column(Integer, default=0, nullable=False)
    download_expires_at: Mapped[datetime | None] = mapped_column(DateTime(timezone=True))

    # Timestamps
    created_at: Mapped[datetime] = mapped_column(
        DateTime(timezone=True),
        default=datetime.utcnow,
        nullable=False
    )

    # Relationships
    order: Mapped["Order"] = relationship("Order", back_populates="items")

    def __repr__(self) -> str:
        return f"<OrderItem {self.book_title} x{self.quantity}>"
