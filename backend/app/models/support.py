"""
Support ticket and message models for customer support.
"""
import uuid
from datetime import datetime
from typing import List

from sqlalchemy import String, DateTime, Text, ForeignKey, Enum as SQLEnum, Boolean
from sqlalchemy.dialects.postgresql import UUID
from sqlalchemy.orm import Mapped, mapped_column, relationship
import enum

from app.database import Base


class TicketStatus(str, enum.Enum):
    """Support ticket status enumeration."""
    OPEN = "open"
    IN_PROGRESS = "in_progress"
    RESOLVED = "resolved"
    CLOSED = "closed"


class TicketPriority(str, enum.Enum):
    """Support ticket priority enumeration."""
    LOW = "low"
    MEDIUM = "medium"
    HIGH = "high"
    URGENT = "urgent"


class MessageSender(str, enum.Enum):
    """Message sender type enumeration."""
    USER = "user"
    AGENT = "agent"
    SYSTEM = "system"


class SupportTicket(Base):
    """Support ticket model for customer support."""

    __tablename__ = "support_tickets"

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
    assigned_agent_id: Mapped[uuid.UUID | None] = mapped_column(
        UUID(as_uuid=True),
        ForeignKey("users.id", ondelete="SET NULL"),
        index=True
    )

    # Ticket Information
    ticket_number: Mapped[str] = mapped_column(
        String(50),
        unique=True,
        nullable=False,
        index=True
    )
    subject: Mapped[str] = mapped_column(String(200), nullable=False)
    category: Mapped[str] = mapped_column(String(50), nullable=False, index=True)

    # Status & Priority
    status: Mapped[TicketStatus] = mapped_column(
        SQLEnum(TicketStatus, name="ticket_status"),
        default=TicketStatus.OPEN,
        nullable=False,
        index=True
    )
    priority: Mapped[TicketPriority] = mapped_column(
        SQLEnum(TicketPriority, name="ticket_priority"),
        default=TicketPriority.MEDIUM,
        nullable=False,
        index=True
    )

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
    resolved_at: Mapped[datetime | None] = mapped_column(DateTime(timezone=True))
    closed_at: Mapped[datetime | None] = mapped_column(DateTime(timezone=True))

    # Relationships
    user: Mapped["User"] = relationship("User", foreign_keys=[user_id], back_populates="support_tickets")
    messages: Mapped[List["SupportMessage"]] = relationship(
        "SupportMessage",
        back_populates="ticket",
        cascade="all, delete-orphan"
    )

    def __repr__(self) -> str:
        return f"<SupportTicket {self.ticket_number} - {self.status.value}>"


class SupportMessage(Base):
    """Support message model for ticket conversations."""

    __tablename__ = "support_messages"

    # Primary Key
    id: Mapped[uuid.UUID] = mapped_column(
        UUID(as_uuid=True),
        primary_key=True,
        default=uuid.uuid4,
        index=True
    )

    # Foreign Keys
    ticket_id: Mapped[uuid.UUID] = mapped_column(
        UUID(as_uuid=True),
        ForeignKey("support_tickets.id", ondelete="CASCADE"),
        nullable=False,
        index=True
    )
    user_id: Mapped[uuid.UUID | None] = mapped_column(
        UUID(as_uuid=True),
        ForeignKey("users.id", ondelete="SET NULL"),
        index=True
    )

    # Message Content
    message: Mapped[str] = mapped_column(Text, nullable=False)
    sender_type: Mapped[MessageSender] = mapped_column(
        SQLEnum(MessageSender, name="message_sender"),
        nullable=False
    )

    # Metadata
    is_internal: Mapped[bool] = mapped_column(
        Boolean,
        default=False,
        nullable=False
    )  # Internal notes not visible to customer

    # Timestamps
    created_at: Mapped[datetime] = mapped_column(
        DateTime(timezone=True),
        default=datetime.utcnow,
        nullable=False
    )

    # Relationships
    ticket: Mapped["SupportTicket"] = relationship("SupportTicket", back_populates="messages")

    def __repr__(self) -> str:
        return f"<SupportMessage {self.sender_type.value} on Ticket {self.ticket_id}>"
