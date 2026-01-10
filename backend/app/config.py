"""
Application configuration using Pydantic Settings.
Reads from environment variables and .env file.
"""
from functools import lru_cache
from typing import List
from pydantic import AnyHttpUrl, PostgresDsn, RedisDsn, field_validator
from pydantic_settings import BaseSettings, SettingsConfigDict


class Settings(BaseSettings):
    """Application settings loaded from environment variables."""

    model_config = SettingsConfigDict(
        env_file=".env",
        env_file_encoding="utf-8",
        case_sensitive=False,
        extra="ignore"
    )

    # Application
    APP_NAME: str = "BookMart API"
    APP_VERSION: str = "1.0.0"
    ENVIRONMENT: str = "development"
    DEBUG: bool = True
    API_V1_PREFIX: str = "/api/v1"

    # Server
    HOST: str = "0.0.0.0"
    PORT: int = 8000

    # Database
    DATABASE_URL: PostgresDsn
    DATABASE_POOL_SIZE: int = 20
    DATABASE_MAX_OVERFLOW: int = 10

    # Redis
    REDIS_URL: RedisDsn
    REDIS_CACHE_TTL: int = 3600  # 1 hour default

    # Elasticsearch
    ELASTICSEARCH_URL: AnyHttpUrl
    ELASTICSEARCH_INDEX_PREFIX: str = "bookmart"

    # JWT Security
    JWT_SECRET_KEY: str
    JWT_ALGORITHM: str = "RS256"
    JWT_ACCESS_TOKEN_EXPIRE_MINUTES: int = 15
    JWT_REFRESH_TOKEN_EXPIRE_DAYS: int = 7
    JWT_PRIVATE_KEY_PATH: str = "keys/jwt_private.pem"
    JWT_PUBLIC_KEY_PATH: str = "keys/jwt_public.pem"

    # CORS
    CORS_ORIGINS: List[str] = ["http://localhost:3000", "http://localhost:8080"]
    CORS_ALLOW_CREDENTIALS: bool = True

    @field_validator("CORS_ORIGINS", mode="before")
    @classmethod
    def parse_cors_origins(cls, v):
        """Parse comma-separated CORS origins."""
        if isinstance(v, str):
            return [origin.strip() for origin in v.split(",")]
        return v

    # Rate Limiting
    RATE_LIMIT_AUTH: str = "5/15minutes"
    RATE_LIMIT_API: str = "100/1minute"

    # File Storage (AWS S3)
    AWS_ACCESS_KEY_ID: str = ""
    AWS_SECRET_ACCESS_KEY: str = ""
    AWS_REGION: str = "us-east-1"
    S3_BUCKET_NAME: str = "bookmart-files"

    # External APIs
    STRIPE_API_KEY: str = ""
    STRIPE_WEBHOOK_SECRET: str = ""

    # AI Configuration
    AI_PROVIDER: str = "mock"  # Options: openai, mock
    OPENAI_API_KEY: str = ""
    OPENAI_MODEL: str = "gpt-4-turbo"  # gpt-4-turbo, gpt-4, gpt-3.5-turbo
    AI_RECOMMENDATIONS_ENABLED: bool = True
    AI_READER_LEVEL_ENABLED: bool = True
    AI_SUPPORT_CHAT_ENABLED: bool = True
    AI_CACHE_ENABLED: bool = True
    AI_CACHE_TTL_RECOMMENDATIONS: int = 86400  # 24 hours
    AI_CACHE_TTL_READER_LEVEL: int = 604800  # 7 days

    # Email
    SMTP_HOST: str = "smtp.gmail.com"
    SMTP_PORT: int = 587
    SMTP_USER: str = ""
    SMTP_PASSWORD: str = ""
    EMAIL_FROM: str = "noreply@bookmart.com"

    @property
    def is_production(self) -> bool:
        """Check if running in production environment."""
        return self.ENVIRONMENT.lower() == "production"

    @property
    def is_development(self) -> bool:
        """Check if running in development environment."""
        return self.ENVIRONMENT.lower() == "development"


@lru_cache
def get_settings() -> Settings:
    """
    Get cached settings instance.
    Uses lru_cache to create singleton pattern.
    """
    return Settings()


# Global settings instance
settings = get_settings()
