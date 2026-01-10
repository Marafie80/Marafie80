"""
User management endpoints for profile and wishlist operations.
"""
from typing import Annotated
import uuid

from fastapi import APIRouter, Depends, HTTPException, status
from sqlalchemy.ext.asyncio import AsyncSession
from sqlalchemy import select

from app.database import get_db
from app.models.user import User
from app.models.book import Book
from app.schemas.user import (
    UserProfile,
    UserUpdate,
    WishlistAdd,
    WishlistRemove,
)
from app.schemas.book import BookResponse
from app.core.dependencies import get_current_active_user

router = APIRouter(prefix="/users", tags=["Users"])


@router.get("/me/profile", response_model=UserProfile)
async def get_my_profile(
    current_user: Annotated[User, Depends(get_current_active_user)],
) -> UserProfile:
    """
    Get current user's detailed profile.

    Args:
        current_user: Current authenticated user

    Returns:
        Detailed user profile
    """
    return UserProfile.model_validate(current_user)


@router.patch("/me/profile", response_model=UserProfile)
async def update_my_profile(
    user_update: UserUpdate,
    current_user: Annotated[User, Depends(get_current_active_user)],
    db: Annotated[AsyncSession, Depends(get_db)],
) -> UserProfile:
    """
    Update current user's profile.

    Args:
        user_update: User update data
        current_user: Current authenticated user
        db: Database session

    Returns:
        Updated user profile
    """
    # Update fields if provided
    update_data = user_update.model_dump(exclude_unset=True)

    for field, value in update_data.items():
        setattr(current_user, field, value)

    await db.commit()
    await db.refresh(current_user)

    return UserProfile.model_validate(current_user)


@router.get("/me/wishlist", response_model=list[BookResponse])
async def get_my_wishlist(
    current_user: Annotated[User, Depends(get_current_active_user)],
    db: Annotated[AsyncSession, Depends(get_db)],
) -> list[BookResponse]:
    """
    Get current user's wishlist books.

    Args:
        current_user: Current authenticated user
        db: Database session

    Returns:
        List of books in wishlist
    """
    if not current_user.wishlist:
        return []

    # Convert string IDs to UUID
    book_ids = [uuid.UUID(book_id) for book_id in current_user.wishlist]

    # Fetch books
    result = await db.execute(
        select(Book).where(Book.id.in_(book_ids))
    )
    books = result.scalars().all()

    return [BookResponse.model_validate(book) for book in books]


@router.post("/me/wishlist", status_code=status.HTTP_201_CREATED)
async def add_to_wishlist(
    wishlist_add: WishlistAdd,
    current_user: Annotated[User, Depends(get_current_active_user)],
    db: Annotated[AsyncSession, Depends(get_db)],
) -> dict:
    """
    Add a book to current user's wishlist.

    Args:
        wishlist_add: Book to add
        current_user: Current authenticated user
        db: Database session

    Returns:
        Success message

    Raises:
        HTTPException: If book not found or already in wishlist
    """
    # Verify book exists
    result = await db.execute(select(Book).where(Book.id == wishlist_add.book_id))
    book = result.scalar_one_or_none()

    if not book:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail="Book not found"
        )

    # Check if already in wishlist
    book_id_str = str(wishlist_add.book_id)
    if book_id_str in current_user.wishlist:
        raise HTTPException(
            status_code=status.HTTP_400_BAD_REQUEST,
            detail="Book already in wishlist"
        )

    # Add to wishlist
    current_user.wishlist.append(book_id_str)
    await db.commit()

    return {"message": "Book added to wishlist"}


@router.delete("/me/wishlist")
async def remove_from_wishlist(
    wishlist_remove: WishlistRemove,
    current_user: Annotated[User, Depends(get_current_active_user)],
    db: Annotated[AsyncSession, Depends(get_db)],
) -> dict:
    """
    Remove a book from current user's wishlist.

    Args:
        wishlist_remove: Book to remove
        current_user: Current authenticated user
        db: Database session

    Returns:
        Success message

    Raises:
        HTTPException: If book not in wishlist
    """
    book_id_str = str(wishlist_remove.book_id)

    if book_id_str not in current_user.wishlist:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail="Book not in wishlist"
        )

    # Remove from wishlist
    current_user.wishlist.remove(book_id_str)
    await db.commit()

    return {"message": "Book removed from wishlist"}


@router.get("/{user_id}/profile", response_model=UserProfile)
async def get_user_profile(
    user_id: uuid.UUID,
    db: Annotated[AsyncSession, Depends(get_db)],
) -> UserProfile:
    """
    Get a user's public profile by ID.

    Args:
        user_id: User ID
        db: Database session

    Returns:
        User profile

    Raises:
        HTTPException: If user not found
    """
    result = await db.execute(select(User).where(User.id == user_id))
    user = result.scalar_one_or_none()

    if not user:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail="User not found"
        )

    return UserProfile.model_validate(user)
