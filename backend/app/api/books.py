"""
Book catalog endpoints for browsing, searching, and managing books.
"""
from typing import Annotated
import uuid
import math

from fastapi import APIRouter, Depends, HTTPException, status, Query
from sqlalchemy.ext.asyncio import AsyncSession
from sqlalchemy import select, func, or_
from sqlalchemy.orm import selectinload

from app.database import get_db
from app.models.book import Book, BookFormat, BookCategory
from app.models.review import Review
from app.models.user import User
from app.schemas.book import (
    BookCreate,
    BookUpdate,
    BookResponse,
    BookListResponse,
)
from app.schemas.review import ReviewCreate, ReviewResponse, ReviewListResponse
from app.core.dependencies import get_current_active_user, get_current_admin_user

router = APIRouter(prefix="/books", tags=["Books"])


@router.get("", response_model=BookListResponse)
async def list_books(
    db: Annotated[AsyncSession, Depends(get_db)],
    page: int = Query(default=1, ge=1),
    page_size: int = Query(default=20, ge=1, le=100),
    category: BookCategory | None = None,
    format: BookFormat | None = None,
    is_free: bool | None = None,
    is_featured: bool | None = None,
    is_bestseller: bool | None = None,
    sort_by: str = Query(default="created_at"),
    sort_order: str = Query(default="desc"),
) -> BookListResponse:
    """
    List books with filtering and pagination.

    Args:
        db: Database session
        page: Page number
        page_size: Items per page
        category: Filter by category
        format: Filter by format
        is_free: Filter free books
        is_featured: Filter featured books
        is_bestseller: Filter bestsellers
        sort_by: Sort field
        sort_order: Sort order (asc/desc)

    Returns:
        Paginated list of books
    """
    # Build query
    query = select(Book).where(Book.is_available == True)

    # Apply filters
    if category:
        query = query.where(Book.category == category)
    if format:
        query = query.where(Book.format == format)
    if is_free is not None:
        query = query.where(Book.is_free == is_free)
    if is_featured is not None:
        query = query.where(Book.is_featured == is_featured)
    if is_bestseller is not None:
        query = query.where(Book.is_bestseller == is_bestseller)

    # Apply sorting
    sort_column = getattr(Book, sort_by, Book.created_at)
    if sort_order.lower() == "desc":
        query = query.order_by(sort_column.desc())
    else:
        query = query.order_by(sort_column.asc())

    # Get total count
    count_query = select(func.count()).select_from(query.subquery())
    result = await db.execute(count_query)
    total = result.scalar_one()

    # Apply pagination
    offset = (page - 1) * page_size
    query = query.offset(offset).limit(page_size)

    # Execute query
    result = await db.execute(query)
    books = result.scalars().all()

    # Calculate total pages
    pages = math.ceil(total / page_size) if total > 0 else 0

    return BookListResponse(
        items=[BookResponse.model_validate(book) for book in books],
        total=total,
        page=page,
        page_size=page_size,
        pages=pages,
    )


@router.get("/search", response_model=BookListResponse)
async def search_books(
    db: Annotated[AsyncSession, Depends(get_db)],
    q: str = Query(..., min_length=1),
    page: int = Query(default=1, ge=1),
    page_size: int = Query(default=20, ge=1, le=100),
) -> BookListResponse:
    """
    Search books by title, author, or ISBN.

    Args:
        db: Database session
        q: Search query
        page: Page number
        page_size: Items per page

    Returns:
        Paginated list of matching books
    """
    search_term = f"%{q}%"

    # Build search query
    query = select(Book).where(
        Book.is_available == True,
        or_(
            Book.title.ilike(search_term),
            Book.author.ilike(search_term),
            Book.isbn.ilike(search_term),
            Book.description.ilike(search_term),
        )
    ).order_by(Book.rating.desc(), Book.created_at.desc())

    # Get total count
    count_query = select(func.count()).select_from(query.subquery())
    result = await db.execute(count_query)
    total = result.scalar_one()

    # Apply pagination
    offset = (page - 1) * page_size
    query = query.offset(offset).limit(page_size)

    # Execute query
    result = await db.execute(query)
    books = result.scalars().all()

    # Calculate total pages
    pages = math.ceil(total / page_size) if total > 0 else 0

    return BookListResponse(
        items=[BookResponse.model_validate(book) for book in books],
        total=total,
        page=page,
        page_size=page_size,
        pages=pages,
    )


@router.get("/featured", response_model=list[BookResponse])
async def get_featured_books(
    db: Annotated[AsyncSession, Depends(get_db)],
    limit: int = Query(default=10, ge=1, le=50),
) -> list[BookResponse]:
    """
    Get featured books.

    Args:
        db: Database session
        limit: Maximum number of books to return

    Returns:
        List of featured books
    """
    query = select(Book).where(
        Book.is_available == True,
        Book.is_featured == True
    ).order_by(Book.rating.desc()).limit(limit)

    result = await db.execute(query)
    books = result.scalars().all()

    return [BookResponse.model_validate(book) for book in books]


@router.get("/bestsellers", response_model=list[BookResponse])
async def get_bestsellers(
    db: Annotated[AsyncSession, Depends(get_db)],
    limit: int = Query(default=10, ge=1, le=50),
) -> list[BookResponse]:
    """
    Get bestseller books.

    Args:
        db: Database session
        limit: Maximum number of books to return

    Returns:
        List of bestseller books
    """
    query = select(Book).where(
        Book.is_available == True,
        Book.is_bestseller == True
    ).order_by(Book.purchase_count.desc()).limit(limit)

    result = await db.execute(query)
    books = result.scalars().all()

    return [BookResponse.model_validate(book) for book in books]


@router.get("/{book_id}", response_model=BookResponse)
async def get_book(
    book_id: uuid.UUID,
    db: Annotated[AsyncSession, Depends(get_db)],
) -> BookResponse:
    """
    Get a book by ID.

    Args:
        book_id: Book ID
        db: Database session

    Returns:
        Book details

    Raises:
        HTTPException: If book not found
    """
    result = await db.execute(select(Book).where(Book.id == book_id))
    book = result.scalar_one_or_none()

    if not book:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail="Book not found"
        )

    # Increment view count
    book.view_count += 1
    await db.commit()
    await db.refresh(book)

    return BookResponse.model_validate(book)


@router.post("", response_model=BookResponse, status_code=status.HTTP_201_CREATED)
async def create_book(
    book_data: BookCreate,
    current_user: Annotated[User, Depends(get_current_admin_user)],
    db: Annotated[AsyncSession, Depends(get_db)],
) -> BookResponse:
    """
    Create a new book (admin only).

    Args:
        book_data: Book creation data
        current_user: Current admin user
        db: Database session

    Returns:
        Created book

    Raises:
        HTTPException: If ISBN already exists
    """
    # Check if ISBN already exists
    result = await db.execute(select(Book).where(Book.isbn == book_data.isbn))
    existing_book = result.scalar_one_or_none()

    if existing_book:
        raise HTTPException(
            status_code=status.HTTP_400_BAD_REQUEST,
            detail="Book with this ISBN already exists"
        )

    # Create book
    book_dict = book_data.model_dump()
    new_book = Book(**book_dict)

    db.add(new_book)
    await db.commit()
    await db.refresh(new_book)

    return BookResponse.model_validate(new_book)


@router.patch("/{book_id}", response_model=BookResponse)
async def update_book(
    book_id: uuid.UUID,
    book_update: BookUpdate,
    current_user: Annotated[User, Depends(get_current_admin_user)],
    db: Annotated[AsyncSession, Depends(get_db)],
) -> BookResponse:
    """
    Update a book (admin only).

    Args:
        book_id: Book ID
        book_update: Book update data
        current_user: Current admin user
        db: Database session

    Returns:
        Updated book

    Raises:
        HTTPException: If book not found
    """
    result = await db.execute(select(Book).where(Book.id == book_id))
    book = result.scalar_one_or_none()

    if not book:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail="Book not found"
        )

    # Update fields
    update_data = book_update.model_dump(exclude_unset=True)
    for field, value in update_data.items():
        setattr(book, field, value)

    await db.commit()
    await db.refresh(book)

    return BookResponse.model_validate(book)


@router.delete("/{book_id}", status_code=status.HTTP_204_NO_CONTENT)
async def delete_book(
    book_id: uuid.UUID,
    current_user: Annotated[User, Depends(get_current_admin_user)],
    db: Annotated[AsyncSession, Depends(get_db)],
) -> None:
    """
    Delete a book (admin only).

    Args:
        book_id: Book ID
        current_user: Current admin user
        db: Database session

    Raises:
        HTTPException: If book not found
    """
    result = await db.execute(select(Book).where(Book.id == book_id))
    book = result.scalar_one_or_none()

    if not book:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail="Book not found"
        )

    await db.delete(book)
    await db.commit()


# Book Reviews


@router.get("/{book_id}/reviews", response_model=ReviewListResponse)
async def get_book_reviews(
    book_id: uuid.UUID,
    db: Annotated[AsyncSession, Depends(get_db)],
    page: int = Query(default=1, ge=1),
    page_size: int = Query(default=20, ge=1, le=100),
) -> ReviewListResponse:
    """
    Get reviews for a book.

    Args:
        book_id: Book ID
        db: Database session
        page: Page number
        page_size: Items per page

    Returns:
        Paginated list of reviews

    Raises:
        HTTPException: If book not found
    """
    # Verify book exists
    result = await db.execute(select(Book).where(Book.id == book_id))
    book = result.scalar_one_or_none()

    if not book:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail="Book not found"
        )

    # Build query
    query = select(Review).where(
        Review.book_id == book_id,
        Review.is_approved == True
    ).order_by(Review.created_at.desc())

    # Get total count
    count_query = select(func.count()).select_from(query.subquery())
    result = await db.execute(count_query)
    total = result.scalar_one()

    # Apply pagination
    offset = (page - 1) * page_size
    query = query.offset(offset).limit(page_size)

    # Execute query
    result = await db.execute(query)
    reviews = result.scalars().all()

    # Calculate total pages and average rating
    pages = math.ceil(total / page_size) if total > 0 else 0

    return ReviewListResponse(
        items=[ReviewResponse.model_validate(review) for review in reviews],
        total=total,
        page=page,
        page_size=page_size,
        pages=pages,
        average_rating=book.rating,
    )


@router.post("/{book_id}/reviews", response_model=ReviewResponse, status_code=status.HTTP_201_CREATED)
async def create_review(
    book_id: uuid.UUID,
    review_data: ReviewCreate,
    current_user: Annotated[User, Depends(get_current_active_user)],
    db: Annotated[AsyncSession, Depends(get_db)],
) -> ReviewResponse:
    """
    Create a review for a book.

    Args:
        book_id: Book ID
        review_data: Review data
        current_user: Current authenticated user
        db: Database session

    Returns:
        Created review

    Raises:
        HTTPException: If book not found or user already reviewed
    """
    # Verify book exists
    result = await db.execute(select(Book).where(Book.id == book_id))
    book = result.scalar_one_or_none()

    if not book:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail="Book not found"
        )

    # Check if user already reviewed
    result = await db.execute(
        select(Review).where(
            Review.book_id == book_id,
            Review.user_id == current_user.id
        )
    )
    existing_review = result.scalar_one_or_none()

    if existing_review:
        raise HTTPException(
            status_code=status.HTTP_400_BAD_REQUEST,
            detail="You have already reviewed this book"
        )

    # Create review
    new_review = Review(
        user_id=current_user.id,
        book_id=book_id,
        rating=review_data.rating,
        title=review_data.title,
        comment=review_data.comment,
    )

    db.add(new_review)

    # Update book rating (simple average for now)
    result = await db.execute(
        select(func.avg(Review.rating), func.count(Review.id))
        .where(Review.book_id == book_id, Review.is_approved == True)
    )
    avg_rating, review_count = result.one()

    book.rating = avg_rating or 0
    book.review_count = review_count or 0

    await db.commit()
    await db.refresh(new_review)

    return ReviewResponse.model_validate(new_review)
