# BookMart API Endpoints

## 📡 API Overview

**Base URL:** `https://api.bookmart.com`
**API Version:** `v1`
**Protocol:** HTTPS only
**Format:** JSON
**Authentication:** JWT Bearer Token

---

## 🔐 Authentication

### POST `/api/v1/auth/register`

Create a new user account.

**Request:**
```json
{
  "email": "user@example.com",
  "password": "SecurePass123!",
  "full_name": "John Doe",
  "phone": "+1234567890"
}
```

**Response:** `201 Created`
```json
{
  "user": {
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "email": "user@example.com",
    "full_name": "John Doe",
    "role": "customer",
    "created_at": "2026-01-09T12:00:00Z"
  },
  "tokens": {
    "access_token": "eyJhbGciOiJSUzI1NiIs...",
    "refresh_token": "eyJhbGciOiJSUzI1NiIs...",
    "token_type": "Bearer",
    "expires_in": 900
  }
}
```

**Validation Rules:**
- Email: Valid email format, unique
- Password: Min 8 chars, 1 uppercase, 1 lowercase, 1 number
- Full name: Min 2 chars, max 255 chars

---

### POST `/api/v1/auth/login`

Authenticate user and get access tokens.

**Request:**
```json
{
  "email": "user@example.com",
  "password": "SecurePass123!"
}
```

**Response:** `200 OK`
```json
{
  "user": {
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "email": "user@example.com",
    "full_name": "John Doe",
    "role": "customer",
    "reader_level": "avid",
    "books_read": 24
  },
  "tokens": {
    "access_token": "eyJhbGciOiJSUzI1NiIs...",
    "refresh_token": "eyJhbGciOiJSUzI1NiIs...",
    "token_type": "Bearer",
    "expires_in": 900
  }
}
```

**Error:** `401 Unauthorized`
```json
{
  "error": {
    "code": "INVALID_CREDENTIALS",
    "message": "Invalid email or password"
  }
}
```

---

### POST `/api/v1/auth/refresh`

Refresh access token using refresh token.

**Request:**
```json
{
  "refresh_token": "eyJhbGciOiJSUzI1NiIs..."
}
```

**Response:** `200 OK`
```json
{
  "access_token": "eyJhbGciOiJSUzI1NiIs...",
  "token_type": "Bearer",
  "expires_in": 900
}
```

---

### POST `/api/v1/auth/logout`

Invalidate user tokens (blacklist).

**Headers:**
```
Authorization: Bearer eyJhbGciOiJSUzI1NiIs...
```

**Response:** `204 No Content`

---

### POST `/api/v1/auth/forgot-password`

Request password reset email.

**Request:**
```json
{
  "email": "user@example.com"
}
```

**Response:** `200 OK`
```json
{
  "message": "Password reset email sent if account exists"
}
```

---

### POST `/api/v1/auth/reset-password`

Reset password with token from email.

**Request:**
```json
{
  "token": "reset_token_from_email",
  "new_password": "NewSecurePass123!"
}
```

**Response:** `200 OK`
```json
{
  "message": "Password successfully reset"
}
```

---

## 👤 Users

### GET `/api/v1/users/me`

Get current user profile.

**Headers:**
```
Authorization: Bearer {access_token}
```

**Response:** `200 OK`
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "email": "user@example.com",
  "full_name": "John Doe",
  "phone": "+1234567890",
  "avatar_url": "https://cdn.bookmart.com/avatars/user_123.jpg",
  "role": "customer",
  "reader_level": "avid",
  "books_read": 24,
  "review_count": 12,
  "wishlist_count": 8,
  "is_verified": true,
  "created_at": "2024-03-01T10:00:00Z",
  "last_login_at": "2026-01-09T11:30:00Z"
}
```

---

### PUT `/api/v1/users/me`

Update current user profile.

**Headers:**
```
Authorization: Bearer {access_token}
```

**Request:**
```json
{
  "full_name": "John Updated Doe",
  "phone": "+1234567890",
  "avatar_url": "https://cdn.bookmart.com/avatars/new_avatar.jpg"
}
```

**Response:** `200 OK`
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "email": "user@example.com",
  "full_name": "John Updated Doe",
  "phone": "+1234567890",
  "avatar_url": "https://cdn.bookmart.com/avatars/new_avatar.jpg",
  "updated_at": "2026-01-09T12:00:00Z"
}
```

---

### GET `/api/v1/users/me/wishlist`

Get user's wishlist.

**Headers:**
```
Authorization: Bearer {access_token}
```

**Query Parameters:**
- `page` (optional, default: 1)
- `limit` (optional, default: 20, max: 100)

**Response:** `200 OK`
```json
{
  "items": [
    {
      "id": "wishlist_item_id",
      "book": {
        "id": "book_id",
        "title": "The Midnight Garden",
        "author": "Sarah Williams",
        "cover_url": "https://cdn.bookmart.com/covers/book_123.jpg",
        "price": 24.99,
        "rating": 4.9,
        "format": "digital"
      },
      "added_at": "2026-01-05T10:00:00Z"
    }
  ],
  "pagination": {
    "page": 1,
    "limit": 20,
    "total": 8,
    "pages": 1
  }
}
```

---

### POST `/api/v1/users/me/wishlist/{book_id}`

Add book to wishlist.

**Headers:**
```
Authorization: Bearer {access_token}
```

**Response:** `201 Created`
```json
{
  "id": "wishlist_item_id",
  "book_id": "550e8400-e29b-41d4-a716-446655440000",
  "added_at": "2026-01-09T12:00:00Z"
}
```

---

### DELETE `/api/v1/users/me/wishlist/{book_id}`

Remove book from wishlist.

**Headers:**
```
Authorization: Bearer {access_token}
```

**Response:** `204 No Content`

---

## 📚 Books

### GET `/api/v1/books`

List books with pagination and filters.

**Query Parameters:**
- `page` (optional, default: 1)
- `limit` (optional, default: 20, max: 100)
- `category` (optional, filter by category)
- `format` (optional, filter by format)
- `min_price` (optional, minimum price)
- `max_price` (optional, maximum price)
- `min_rating` (optional, minimum rating)
- `sort` (optional, values: `price_asc`, `price_desc`, `rating_desc`, `newest`)

**Response:** `200 OK`
```json
{
  "books": [
    {
      "id": "550e8400-e29b-41d4-a716-446655440000",
      "isbn": "978-1234567890",
      "title": "The Midnight Garden",
      "author": "Sarah Williams",
      "cover_url": "https://cdn.bookmart.com/covers/book_123.jpg",
      "price": 24.99,
      "currency": "USD",
      "format": "digital",
      "category": "Fiction",
      "subcategory": "Mystery",
      "publisher": "HarperCollins",
      "published_date": "2025-03-01",
      "pages": 384,
      "language": "English",
      "description": "A mesmerizing tale of love, loss, and mystery...",
      "rating": 4.9,
      "review_count": 3421,
      "is_free": false,
      "badge": "featured"
    }
  ],
  "pagination": {
    "page": 1,
    "limit": 20,
    "total": 142,
    "pages": 8
  },
  "filters": {
    "categories": ["Fiction", "Non-Fiction", "Mystery"],
    "formats": ["digital", "paperback", "hardcover", "audiobook"],
    "price_range": {"min": 0, "max": 50}
  }
}
```

---

### GET `/api/v1/books/{book_id}`

Get detailed book information.

**Response:** `200 OK`
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "isbn": "978-1234567890",
  "title": "The Midnight Garden",
  "author": "Sarah Williams",
  "cover_url": "https://cdn.bookmart.com/covers/book_123.jpg",
  "price": 24.99,
  "currency": "USD",
  "format": "digital",
  "category": "Fiction",
  "subcategory": "Mystery",
  "publisher": "HarperCollins",
  "published_date": "2025-03-01",
  "pages": 384,
  "language": "English",
  "description": "A mesmerizing tale of love, loss, and mystery set in a forgotten English garden. When Emma inherits her grandmother's estate...",
  "rating": 4.9,
  "review_count": 3421,
  "is_free": false,
  "is_available": true,
  "badge": "featured",
  "metadata": {
    "awards": ["Book of the Month March 2025"],
    "series": null,
    "edition": "First Edition"
  },
  "related_books": [
    {
      "id": "book_2_id",
      "title": "Similar Book Title",
      "author": "Another Author",
      "cover_url": "https://cdn.bookmart.com/covers/book_456.jpg",
      "price": 19.99,
      "rating": 4.7
    }
  ],
  "created_at": "2025-02-01T10:00:00Z",
  "updated_at": "2026-01-05T15:30:00Z"
}
```

**Error:** `404 Not Found`
```json
{
  "error": {
    "code": "BOOK_NOT_FOUND",
    "message": "Book not found",
    "details": {
      "book_id": "invalid_id"
    }
  }
}
```

---

### GET `/api/v1/books/search`

Search books using Elasticsearch.

**Query Parameters:**
- `q` (required, search query)
- `page` (optional, default: 1)
- `limit` (optional, default: 20)
- `category` (optional, filter by category)
- `format` (optional, filter by format)

**Response:** `200 OK`
```json
{
  "query": "science fiction",
  "results": [
    {
      "id": "book_id",
      "title": "Digital Dreams",
      "author": "Lisa Chen",
      "cover_url": "https://cdn.bookmart.com/covers/book_789.jpg",
      "price": 19.99,
      "rating": 4.8,
      "format": "digital",
      "highlight": {
        "title": "<em>Science</em> <em>Fiction</em> Classic",
        "description": "A journey through virtual reality..."
      }
    }
  ],
  "pagination": {
    "page": 1,
    "limit": 20,
    "total": 142
  },
  "suggestions": ["sci-fi", "fantasy", "dystopian"],
  "aggregations": {
    "categories": {
      "Science Fiction": 89,
      "Fantasy": 34,
      "Dystopian": 19
    }
  }
}
```

---

### GET `/api/v1/books/featured`

Get featured books.

**Response:** `200 OK`
```json
{
  "book_of_month": {
    "id": "book_id",
    "title": "The Midnight Garden",
    "author": "Sarah Williams",
    "cover_url": "https://cdn.bookmart.com/covers/book_123.jpg",
    "price": 24.99,
    "rating": 4.9,
    "description": "A mesmerizing tale...",
    "badge": "featured"
  },
  "book_of_year": [
    {
      "id": "book_id",
      "title": "Starlight Chronicles",
      "badge": "winner"
    }
  ],
  "new_releases": [],
  "bestsellers": []
}
```

---

### GET `/api/v1/books/recommendations`

Get personalized book recommendations (AI-powered).

**Headers:**
```
Authorization: Bearer {access_token}
```

**Response:** `200 OK`
```json
{
  "recommendations": [
    {
      "id": "book_id",
      "title": "Recommended Book",
      "author": "Author Name",
      "cover_url": "https://cdn.bookmart.com/covers/book.jpg",
      "price": 22.99,
      "rating": 4.7,
      "reason": "Based on your reading of The Midnight Garden"
    }
  ],
  "algorithm": "collaborative_filtering",
  "generated_at": "2026-01-09T12:00:00Z"
}
```

---

## ⭐ Reviews

### GET `/api/v1/books/{book_id}/reviews`

Get reviews for a book.

**Query Parameters:**
- `page` (optional, default: 1)
- `limit` (optional, default: 10)
- `sort` (optional, values: `helpful`, `recent`, `rating_high`, `rating_low`)

**Response:** `200 OK`
```json
{
  "reviews": [
    {
      "id": "review_id",
      "user": {
        "id": "user_id",
        "name": "BookLover2025",
        "avatar_url": "https://cdn.bookmart.com/avatars/user.jpg"
      },
      "rating": 5,
      "title": "Absolutely captivating!",
      "content": "I couldn't put this book down. The characters are so well developed...",
      "verified_purchase": true,
      "helpful_count": 234,
      "not_helpful_count": 5,
      "created_at": "2026-01-07T14:30:00Z"
    }
  ],
  "pagination": {
    "page": 1,
    "limit": 10,
    "total": 3421
  },
  "rating_summary": {
    "average": 4.9,
    "total": 3421,
    "distribution": {
      "5": 2876,
      "4": 423,
      "3": 89,
      "2": 22,
      "1": 11
    }
  }
}
```

---

### POST `/api/v1/books/{book_id}/reviews`

Create a review for a book.

**Headers:**
```
Authorization: Bearer {access_token}
```

**Request:**
```json
{
  "rating": 5,
  "title": "Amazing book!",
  "content": "This book exceeded all my expectations. Highly recommended!"
}
```

**Response:** `201 Created`
```json
{
  "id": "review_id",
  "book_id": "book_id",
  "user_id": "user_id",
  "rating": 5,
  "title": "Amazing book!",
  "content": "This book exceeded all my expectations...",
  "verified_purchase": true,
  "helpful_count": 0,
  "created_at": "2026-01-09T12:00:00Z"
}
```

**Error:** `400 Bad Request` (if user already reviewed)
```json
{
  "error": {
    "code": "REVIEW_ALREADY_EXISTS",
    "message": "You have already reviewed this book"
  }
}
```

---

## 🛒 Shopping Cart

### GET `/api/v1/cart`

Get user's shopping cart.

**Headers:**
```
Authorization: Bearer {access_token}
```

**Response:** `200 OK`
```json
{
  "items": [
    {
      "id": "cart_item_id",
      "book": {
        "id": "book_id",
        "title": "The Midnight Garden",
        "author": "Sarah Williams",
        "cover_url": "https://cdn.bookmart.com/covers/book_123.jpg",
        "format": "digital"
      },
      "quantity": 1,
      "unit_price": 24.99,
      "total_price": 24.99
    }
  ],
  "summary": {
    "subtotal": 64.97,
    "tax": 5.20,
    "discount": 0.00,
    "total": 70.17,
    "currency": "USD",
    "item_count": 3
  }
}
```

---

### POST `/api/v1/cart/items`

Add item to cart.

**Headers:**
```
Authorization: Bearer {access_token}
```

**Request:**
```json
{
  "book_id": "550e8400-e29b-41d4-a716-446655440000",
  "format": "digital",
  "quantity": 1
}
```

**Response:** `201 Created`
```json
{
  "id": "cart_item_id",
  "book_id": "550e8400-e29b-41d4-a716-446655440000",
  "quantity": 1,
  "unit_price": 24.99,
  "added_at": "2026-01-09T12:00:00Z"
}
```

---

### PUT `/api/v1/cart/items/{item_id}`

Update cart item quantity.

**Headers:**
```
Authorization: Bearer {access_token}
```

**Request:**
```json
{
  "quantity": 2
}
```

**Response:** `200 OK`
```json
{
  "id": "cart_item_id",
  "quantity": 2,
  "unit_price": 24.99,
  "total_price": 49.98,
  "updated_at": "2026-01-09T12:00:00Z"
}
```

---

### DELETE `/api/v1/cart/items/{item_id}`

Remove item from cart.

**Headers:**
```
Authorization: Bearer {access_token}
```

**Response:** `204 No Content`

---

### DELETE `/api/v1/cart`

Clear entire cart.

**Headers:**
```
Authorization: Bearer {access_token}
```

**Response:** `204 No Content`

---

## 📦 Orders

### POST `/api/v1/orders`

Create order from cart.

**Headers:**
```
Authorization: Bearer {access_token}
```

**Request:**
```json
{
  "shipping_address_id": "address_id",
  "payment_method": "card",
  "payment_details": {
    "stripe_payment_method_id": "pm_123456"
  },
  "promo_code": "WELCOME10"
}
```

**Response:** `201 Created`
```json
{
  "id": "order_id",
  "order_number": "BM-2026-00142",
  "status": "pending",
  "payment_status": "pending",
  "items": [
    {
      "book_id": "book_id",
      "title": "The Midnight Garden",
      "format": "digital",
      "quantity": 1,
      "unit_price": 24.99,
      "total_price": 24.99
    }
  ],
  "subtotal": 64.97,
  "tax": 5.20,
  "discount": 5.00,
  "total": 65.17,
  "currency": "USD",
  "shipping_address": {
    "full_name": "John Doe",
    "line1": "123 Main St",
    "city": "New York",
    "state": "NY",
    "postal_code": "10001",
    "country": "US"
  },
  "created_at": "2026-01-09T12:00:00Z"
}
```

---

### GET `/api/v1/orders`

List user's orders.

**Headers:**
```
Authorization: Bearer {access_token}
```

**Query Parameters:**
- `page` (optional, default: 1)
- `limit` (optional, default: 20)
- `status` (optional, filter by status)

**Response:** `200 OK`
```json
{
  "orders": [
    {
      "id": "order_id",
      "order_number": "BM-2026-00142",
      "status": "in_transit",
      "total": 65.17,
      "currency": "USD",
      "item_count": 3,
      "created_at": "2026-01-09T12:00:00Z",
      "estimated_delivery": "2026-01-16"
    }
  ],
  "pagination": {
    "page": 1,
    "limit": 20,
    "total": 15
  }
}
```

---

### GET `/api/v1/orders/{order_id}`

Get order details.

**Headers:**
```
Authorization: Bearer {access_token}
```

**Response:** `200 OK`
```json
{
  "id": "order_id",
  "order_number": "BM-2026-00142",
  "status": "in_transit",
  "payment_status": "completed",
  "payment_method": "Visa ending in 4242",
  "items": [
    {
      "id": "item_id",
      "book": {
        "id": "book_id",
        "title": "The Midnight Garden",
        "author": "Sarah Williams",
        "cover_url": "https://cdn.bookmart.com/covers/book_123.jpg"
      },
      "format": "digital",
      "quantity": 1,
      "unit_price": 24.99,
      "total_price": 24.99
    }
  ],
  "subtotal": 64.97,
  "tax": 5.20,
  "discount": 5.00,
  "total": 65.17,
  "currency": "USD",
  "shipping_address": {
    "full_name": "John Doe",
    "line1": "123 Main St, Apt 4B",
    "city": "New York",
    "state": "NY",
    "postal_code": "10001",
    "country": "US"
  },
  "tracking_number": "1Z999AA1012345678",
  "tracking_url": "https://tracking.ups.com/track?tracknum=1Z999AA1012345678",
  "estimated_delivery": "2026-01-16",
  "created_at": "2026-01-09T12:00:00Z",
  "updated_at": "2026-01-11T14:20:00Z"
}
```

---

### GET `/api/v1/orders/{order_id}/tracking`

Get order tracking information.

**Headers:**
```
Authorization: Bearer {access_token}
```

**Response:** `200 OK`
```json
{
  "order_number": "BM-2026-00142",
  "status": "in_transit",
  "tracking_number": "1Z999AA1012345678",
  "carrier": "UPS Ground",
  "current_location": "Distribution Center, Newark, NJ",
  "estimated_delivery": "2026-01-16",
  "history": [
    {
      "status": "in_transit",
      "location": "Newark, NJ",
      "description": "Package in transit",
      "timestamp": "2026-01-11T14:20:00Z"
    },
    {
      "status": "departed",
      "location": "Philadelphia, PA",
      "description": "Departed facility",
      "timestamp": "2026-01-11T06:45:00Z"
    },
    {
      "status": "arrived",
      "location": "Philadelphia, PA",
      "description": "Arrived at facility",
      "timestamp": "2026-01-10T21:15:00Z"
    },
    {
      "status": "picked_up",
      "location": "New York, NY",
      "description": "Picked up",
      "timestamp": "2026-01-10T09:15:00Z"
    },
    {
      "status": "order_placed",
      "location": "Online",
      "description": "Order placed",
      "timestamp": "2026-01-09T12:00:00Z"
    }
  ]
}
```

---

### POST `/api/v1/orders/{order_id}/cancel`

Cancel an order.

**Headers:**
```
Authorization: Bearer {access_token}
```

**Request:**
```json
{
  "reason": "Changed my mind"
}
```

**Response:** `200 OK`
```json
{
  "id": "order_id",
  "order_number": "BM-2026-00142",
  "status": "cancelled",
  "cancelled_at": "2026-01-09T13:00:00Z",
  "cancellation_reason": "Changed my mind",
  "refund_status": "pending",
  "refund_amount": 65.17
}
```

---

## 💬 Support

### GET `/api/v1/support/tickets`

List user's support tickets.

**Headers:**
```
Authorization: Bearer {access_token}
```

**Query Parameters:**
- `page` (optional, default: 1)
- `limit` (optional, default: 20)
- `status` (optional, filter by status)

**Response:** `200 OK`
```json
{
  "tickets": [
    {
      "id": "ticket_id",
      "ticket_number": "ST-142",
      "subject": "Order delivery inquiry",
      "status": "active",
      "priority": "normal",
      "order_id": "order_id",
      "assigned_agent": "Sarah",
      "created_at": "2026-01-09T10:30:00Z",
      "updated_at": "2026-01-09T10:34:00Z"
    }
  ],
  "pagination": {
    "page": 1,
    "limit": 20,
    "total": 3
  }
}
```

---

### POST `/api/v1/support/tickets`

Create a support ticket.

**Headers:**
```
Authorization: Bearer {access_token}
```

**Request:**
```json
{
  "subject": "Order delivery inquiry",
  "category": "order_issue",
  "order_id": "order_id",
  "initial_message": "I haven't received my order yet"
}
```

**Response:** `201 Created`
```json
{
  "id": "ticket_id",
  "ticket_number": "ST-142",
  "subject": "Order delivery inquiry",
  "status": "active",
  "priority": "normal",
  "category": "order_issue",
  "order_id": "order_id",
  "created_at": "2026-01-09T10:30:00Z"
}
```

---

### GET `/api/v1/support/tickets/{ticket_id}`

Get ticket details and messages.

**Headers:**
```
Authorization: Bearer {access_token}
```

**Response:** `200 OK`
```json
{
  "id": "ticket_id",
  "ticket_number": "ST-142",
  "subject": "Order delivery inquiry",
  "status": "active",
  "priority": "normal",
  "category": "order_issue",
  "order": {
    "id": "order_id",
    "order_number": "BM-2026-00142",
    "status": "in_transit"
  },
  "messages": [
    {
      "id": "message_id",
      "sender": {
        "type": "user",
        "name": "John Doe"
      },
      "content": "I haven't received my order yet",
      "created_at": "2026-01-09T10:30:00Z"
    },
    {
      "id": "message_id_2",
      "sender": {
        "type": "ai",
        "name": "AI Assistant"
      },
      "content": "I can help you with that! I found your order #BM-2026-00142...",
      "created_at": "2026-01-09T10:31:00Z"
    }
  ],
  "created_at": "2026-01-09T10:30:00Z",
  "updated_at": "2026-01-09T10:34:00Z"
}
```

---

### POST `/api/v1/support/tickets/{ticket_id}/messages`

Send message in a ticket.

**Headers:**
```
Authorization: Bearer {access_token}
```

**Request:**
```json
{
  "content": "Yes, but I need it urgently for a gift"
}
```

**Response:** `201 Created`
```json
{
  "id": "message_id",
  "ticket_id": "ticket_id",
  "sender": {
    "type": "user",
    "name": "John Doe"
  },
  "content": "Yes, but I need it urgently for a gift",
  "created_at": "2026-01-09T10:32:00Z"
}
```

---

### PUT `/api/v1/support/tickets/{ticket_id}/close`

Close a support ticket.

**Headers:**
```
Authorization: Bearer {access_token}
```

**Response:** `200 OK`
```json
{
  "id": "ticket_id",
  "ticket_number": "ST-142",
  "status": "closed",
  "closed_at": "2026-01-09T11:00:00Z"
}
```

---

## 📍 Addresses

### GET `/api/v1/addresses`

List user's saved addresses.

**Headers:**
```
Authorization: Bearer {access_token}
```

**Response:** `200 OK`
```json
{
  "addresses": [
    {
      "id": "address_id",
      "full_name": "John Doe",
      "line1": "123 Main St",
      "line2": "Apt 4B",
      "city": "New York",
      "state": "NY",
      "postal_code": "10001",
      "country": "US",
      "phone": "+1234567890",
      "is_default": true
    }
  ]
}
```

---

### POST `/api/v1/addresses`

Add new address.

**Headers:**
```
Authorization: Bearer {access_token}
```

**Request:**
```json
{
  "full_name": "John Doe",
  "line1": "123 Main St",
  "line2": "Apt 4B",
  "city": "New York",
  "state": "NY",
  "postal_code": "10001",
  "country": "US",
  "phone": "+1234567890",
  "is_default": false
}
```

**Response:** `201 Created`
```json
{
  "id": "address_id",
  "full_name": "John Doe",
  "line1": "123 Main St",
  "line2": "Apt 4B",
  "city": "New York",
  "state": "NY",
  "postal_code": "10001",
  "country": "US",
  "phone": "+1234567890",
  "is_default": false,
  "created_at": "2026-01-09T12:00:00Z"
}
```

---

## 🌐 Rate Limiting

All endpoints are rate-limited to prevent abuse:

- **Authentication endpoints:** 5 requests / 15 minutes per IP
- **API endpoints (authenticated):** 100 requests / minute per user
- **API endpoints (unauthenticated):** 20 requests / minute per IP
- **Search endpoints:** 30 requests / minute per user

**Rate Limit Headers:**
```
X-RateLimit-Limit: 100
X-RateLimit-Remaining: 95
X-RateLimit-Reset: 1641733200
```

**Error Response:** `429 Too Many Requests`
```json
{
  "error": {
    "code": "RATE_LIMIT_EXCEEDED",
    "message": "Rate limit exceeded. Please try again later.",
    "retry_after": 60
  }
}
```

---

## 📊 Pagination

All list endpoints support pagination:

**Query Parameters:**
- `page` (default: 1)
- `limit` (default: 20, max: 100)

**Response Format:**
```json
{
  "data": [...],
  "pagination": {
    "page": 1,
    "limit": 20,
    "total": 142,
    "pages": 8,
    "has_next": true,
    "has_prev": false
  }
}
```

---

## 🔍 Filtering & Sorting

**Filter Parameters:**
- Use query parameters for filters (e.g., `?category=Fiction&format=digital`)
- Multiple values: `?category=Fiction&category=Mystery`

**Sort Parameters:**
- `?sort=price_asc` (ascending)
- `?sort=price_desc` (descending)
- `?sort=rating_desc`
- `?sort=newest`

---

## ⚠️ Error Responses

**Standard Error Format:**
```json
{
  "error": {
    "code": "ERROR_CODE",
    "message": "Human-readable error message",
    "details": {
      "field": "additional context"
    },
    "timestamp": "2026-01-09T12:00:00Z",
    "request_id": "req_abc123"
  }
}
```

**Common Error Codes:**
- `UNAUTHORIZED` (401)
- `FORBIDDEN` (403)
- `NOT_FOUND` (404)
- `VALIDATION_ERROR` (422)
- `RATE_LIMIT_EXCEEDED` (429)
- `INTERNAL_SERVER_ERROR` (500)

---

*API Documentation Version: 1.0*
*Last Updated: January 2026*
*Status: Production-Ready Specifications*
