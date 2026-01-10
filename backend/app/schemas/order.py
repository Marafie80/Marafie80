"""
Pydantic schemas for order-related requests and responses.
"""
from datetime import datetime
from decimal import Decimal
from typing import List
import uuid

from pydantic import BaseModel, Field

from app.models.order import OrderStatus, PaymentMethod, PaymentStatus


class OrderItemCreate(BaseModel):
    """Schema for creating an order item."""
    book_id: uuid.UUID
    quantity: int = Field(default=1, ge=1, le=100)


class OrderItemResponse(BaseModel):
    """Schema for order item response."""
    id: uuid.UUID
    book_id: uuid.UUID | None
    book_title: str
    book_author: str
    book_isbn: str
    book_cover_url: str
    book_format: str
    unit_price: Decimal
    quantity: int
    subtotal: Decimal
    download_url: str | None = None
    download_count: int
    created_at: datetime

    class Config:
        from_attributes = True


class OrderCreate(BaseModel):
    """Schema for creating an order."""
    items: List[OrderItemCreate] = Field(..., min_length=1)
    payment_method: PaymentMethod
    shipping_address: dict
    customer_notes: str | None = Field(None, max_length=1000)


class OrderResponse(BaseModel):
    """Schema for order response."""
    id: uuid.UUID
    order_number: str
    user_id: uuid.UUID
    status: OrderStatus
    subtotal: Decimal
    tax: Decimal
    shipping_cost: Decimal
    discount: Decimal
    total: Decimal
    payment_method: PaymentMethod
    payment_status: PaymentStatus
    payment_id: str | None
    paid_at: datetime | None
    shipping_address: dict
    tracking_number: str | None
    customer_notes: str | None
    items: List[OrderItemResponse]
    created_at: datetime
    updated_at: datetime
    shipped_at: datetime | None
    delivered_at: datetime | None

    class Config:
        from_attributes = True


class OrderListResponse(BaseModel):
    """Schema for paginated order list response."""
    items: List[OrderResponse]
    total: int
    page: int
    page_size: int
    pages: int
