"""
Pydantic schemas for authentication and authorization.
"""
from pydantic import BaseModel, EmailStr

from app.schemas.user import UserResponse


class TokenData(BaseModel):
    """Schema for token payload data."""
    access_token: str
    refresh_token: str
    token_type: str = "bearer"
    expires_in: int  # seconds


class TokenResponse(BaseModel):
    """Schema for authentication response with tokens."""
    user: UserResponse
    tokens: TokenData


class RefreshTokenRequest(BaseModel):
    """Schema for refresh token request."""
    refresh_token: str


class PasswordResetRequest(BaseModel):
    """Schema for password reset request."""
    email: EmailStr


class PasswordResetConfirm(BaseModel):
    """Schema for password reset confirmation."""
    token: str
    new_password: str


class PasswordChange(BaseModel):
    """Schema for password change."""
    current_password: str
    new_password: str
