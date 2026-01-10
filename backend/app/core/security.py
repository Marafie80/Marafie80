"""
Security utilities for authentication and authorization.
Handles JWT token creation/verification and password hashing.
"""
from datetime import datetime, timedelta
from typing import Any, Dict
import os

from jose import jwt, JWTError
from passlib.context import CryptContext

from app.config import settings

# Password hashing context using bcrypt
pwd_context = CryptContext(schemes=["bcrypt"], deprecated="auto")


def verify_password(plain_password: str, hashed_password: str) -> bool:
    """
    Verify a plain password against a hashed password.

    Args:
        plain_password: The plain text password
        hashed_password: The hashed password from database

    Returns:
        True if password matches, False otherwise
    """
    return pwd_context.verify(plain_password, hashed_password)


def get_password_hash(password: str) -> str:
    """
    Hash a password using bcrypt.

    Args:
        password: Plain text password

    Returns:
        Hashed password
    """
    return pwd_context.hash(password)


def create_access_token(data: Dict[str, Any]) -> str:
    """
    Create a JWT access token.

    Args:
        data: Dictionary containing token claims (e.g., {"sub": user_id})

    Returns:
        Encoded JWT token string
    """
    to_encode = data.copy()
    expire = datetime.utcnow() + timedelta(minutes=settings.JWT_ACCESS_TOKEN_EXPIRE_MINUTES)
    to_encode.update({
        "exp": expire,
        "iat": datetime.utcnow(),
        "type": "access"
    })

    # Use HS256 algorithm with secret key for simplicity
    # In production with RS256, load private key from file
    if settings.JWT_ALGORITHM == "RS256" and os.path.exists(settings.JWT_PRIVATE_KEY_PATH):
        with open(settings.JWT_PRIVATE_KEY_PATH, "r") as f:
            private_key = f.read()
        encoded_jwt = jwt.encode(to_encode, private_key, algorithm=settings.JWT_ALGORITHM)
    else:
        # Fallback to HS256 for development
        encoded_jwt = jwt.encode(to_encode, settings.JWT_SECRET_KEY, algorithm="HS256")

    return encoded_jwt


def create_refresh_token(data: Dict[str, Any]) -> str:
    """
    Create a JWT refresh token.

    Args:
        data: Dictionary containing token claims (e.g., {"sub": user_id})

    Returns:
        Encoded JWT refresh token string
    """
    to_encode = data.copy()
    expire = datetime.utcnow() + timedelta(days=settings.JWT_REFRESH_TOKEN_EXPIRE_DAYS)
    to_encode.update({
        "exp": expire,
        "iat": datetime.utcnow(),
        "type": "refresh"
    })

    # Use HS256 algorithm with secret key for simplicity
    if settings.JWT_ALGORITHM == "RS256" and os.path.exists(settings.JWT_PRIVATE_KEY_PATH):
        with open(settings.JWT_PRIVATE_KEY_PATH, "r") as f:
            private_key = f.read()
        encoded_jwt = jwt.encode(to_encode, private_key, algorithm=settings.JWT_ALGORITHM)
    else:
        # Fallback to HS256 for development
        encoded_jwt = jwt.encode(to_encode, settings.JWT_SECRET_KEY, algorithm="HS256")

    return encoded_jwt


def decode_token(token: str) -> Dict[str, Any]:
    """
    Decode and verify a JWT token.

    Args:
        token: The JWT token string

    Returns:
        Dictionary containing token payload

    Raises:
        JWTError: If token is invalid or expired
    """
    try:
        # Use HS256 algorithm with secret key for simplicity
        if settings.JWT_ALGORITHM == "RS256" and os.path.exists(settings.JWT_PUBLIC_KEY_PATH):
            with open(settings.JWT_PUBLIC_KEY_PATH, "r") as f:
                public_key = f.read()
            payload = jwt.decode(token, public_key, algorithms=[settings.JWT_ALGORITHM])
        else:
            # Fallback to HS256 for development
            payload = jwt.decode(token, settings.JWT_SECRET_KEY, algorithms=["HS256"])

        return payload
    except JWTError as e:
        raise e


def generate_password_reset_token(email: str) -> str:
    """
    Generate a password reset token.

    Args:
        email: User's email address

    Returns:
        Password reset token
    """
    data = {"sub": email, "type": "password_reset"}
    expire = datetime.utcnow() + timedelta(hours=1)
    data.update({"exp": expire})

    encoded_jwt = jwt.encode(data, settings.JWT_SECRET_KEY, algorithm="HS256")
    return encoded_jwt


def verify_password_reset_token(token: str) -> str | None:
    """
    Verify a password reset token and extract email.

    Args:
        token: Password reset token

    Returns:
        Email if token is valid, None otherwise
    """
    try:
        payload = jwt.decode(token, settings.JWT_SECRET_KEY, algorithms=["HS256"])
        if payload.get("type") != "password_reset":
            return None
        return payload.get("sub")
    except JWTError:
        return None
