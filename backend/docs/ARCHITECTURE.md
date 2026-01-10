# BookMart Backend - System Architecture

## 🏗️ High-Level Architecture

```
┌─────────────────────────────────────────────────────────────────────────┐
│                           CLIENT LAYER                                  │
├──────────────────────────┬──────────────────────────────────────────────┤
│   Web App (Next.js)      │   Mobile App (Flutter)                      │
│   - React Components     │   - Flutter Widgets                         │
│   - State Management     │   - Riverpod State                          │
│   - HTTP Client (Axios)  │   - HTTP Client (Dio)                       │
└──────────────────────────┴──────────────────────────────────────────────┘
                                    │
                                    │ HTTPS/REST API
                                    ▼
┌─────────────────────────────────────────────────────────────────────────┐
│                          API GATEWAY LAYER                              │
├─────────────────────────────────────────────────────────────────────────┤
│   NGINX / Load Balancer                                                 │
│   - SSL Termination                                                     │
│   - Rate Limiting                                                       │
│   - Request Routing                                                     │
└─────────────────────────────────────────────────────────────────────────┘
                                    │
                                    ▼
┌─────────────────────────────────────────────────────────────────────────┐
│                        APPLICATION LAYER                                │
├─────────────────────────────────────────────────────────────────────────┤
│                     FastAPI Application Server                          │
│                                                                         │
│   ┌──────────────┐  ┌──────────────┐  ┌──────────────┐               │
│   │   Auth API   │  │   Books API  │  │  Orders API  │               │
│   │   /api/auth  │  │  /api/books  │  │ /api/orders  │               │
│   └──────────────┘  └──────────────┘  └──────────────┘               │
│   ┌──────────────┐  ┌──────────────┐  ┌──────────────┐               │
│   │  Users API   │  │ Support API  │  │   Cart API   │               │
│   │  /api/users  │  │ /api/support │  │  /api/cart   │               │
│   └──────────────┘  └──────────────┘  └──────────────┘               │
│                                                                         │
│   Middleware:                                                           │
│   - CORS                                                                │
│   - Authentication (JWT)                                                │
│   - Request Validation                                                  │
│   - Error Handling                                                      │
│   - Logging                                                             │
└─────────────────────────────────────────────────────────────────────────┘
                                    │
                    ┌───────────────┼───────────────┐
                    ▼               ▼               ▼
┌─────────────────────────────────────────────────────────────────────────┐
│                         DATA LAYER                                      │
├──────────────────────┬──────────────────────┬───────────────────────────┤
│   PostgreSQL         │   Redis Cache        │   Elasticsearch          │
│   (Primary DB)       │   (Session/Cache)    │   (Search Engine)        │
│                      │                      │                          │
│   Tables:            │   Keys:              │   Indices:               │
│   - users            │   - sessions:{id}    │   - books_index          │
│   - books            │   - cache:books:{id} │   - reviews_index        │
│   - orders           │   - cache:search:*   │                          │
│   - order_items      │   - cart:{user_id}   │   Features:              │
│   - reviews          │   - rate_limit:*     │   - Full-text search     │
│   - addresses        │                      │   - Fuzzy matching       │
│   - support_tickets  │   TTL: 1-24 hours    │   - Faceted search       │
│   - support_messages │                      │   - Aggregations         │
└──────────────────────┴──────────────────────┴───────────────────────────┘
                                    │
                                    ▼
┌─────────────────────────────────────────────────────────────────────────┐
│                     EXTERNAL SERVICES LAYER                             │
├─────────────────────────────────────────────────────────────────────────┤
│   ┌──────────────┐  ┌──────────────┐  ┌──────────────┐               │
│   │   Stripe     │  │    AWS S3    │  │   SendGrid   │               │
│   │   Payment    │  │  Book Covers │  │    Email     │               │
│   └──────────────┘  └──────────────┘  └──────────────┘               │
│   ┌──────────────┐  ┌──────────────┐  ┌──────────────┐               │
│   │  OpenAI API  │  │   Twilio     │  │  CloudWatch  │               │
│   │     AI       │  │     SMS      │  │   Logging    │               │
│   └──────────────┘  └──────────────┘  └──────────────┘               │
└─────────────────────────────────────────────────────────────────────────┘
```

---

## 📊 Component Details

### 1. **API Gateway Layer**

**Purpose:** Entry point for all client requests

**Components:**
- NGINX or AWS Application Load Balancer
- SSL/TLS termination
- Rate limiting (per IP/user)
- Request routing and load balancing
- DDoS protection

**Configuration:**
```nginx
upstream fastapi_backend {
    least_conn;
    server backend1:8000;
    server backend2:8000;
    server backend3:8000;
}

server {
    listen 443 ssl http2;
    server_name api.bookmart.com;

    ssl_certificate /etc/ssl/bookmart.crt;
    ssl_certificate_key /etc/ssl/bookmart.key;

    location /api/ {
        proxy_pass http://fastapi_backend;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }
}
```

---

### 2. **Application Layer (FastAPI)**

**Purpose:** Business logic and API endpoints

**Key Features:**
- RESTful API design
- Async/await for performance
- Automatic OpenAPI documentation
- JWT-based authentication
- Request validation with Pydantic
- Dependency injection

**Structure:**
```
backend/
├── app/
│   ├── api/              # API routes
│   ├── core/             # Core configuration
│   ├── db/               # Database models
│   ├── schemas/          # Pydantic schemas
│   ├── services/         # Business logic
│   ├── utils/            # Utilities
│   └── main.py           # Application entry
```

---

### 3. **Data Layer**

#### **PostgreSQL (Primary Database)**

**Purpose:** Persistent data storage

**Schema:**
- Normalized relational design
- Foreign key constraints
- Indexes for performance
- JSONB for flexible data
- Full ACID compliance

**Key Tables:**
- `users` - User accounts
- `books` - Book catalog
- `orders` - Order records
- `order_items` - Order line items
- `reviews` - Book reviews
- `addresses` - Shipping addresses
- `support_tickets` - Customer support
- `support_messages` - Support chat messages

---

#### **Redis (Caching & Sessions)**

**Purpose:** In-memory data store for performance

**Use Cases:**
1. **Session Storage:**
   - User sessions (JWT refresh tokens)
   - Shopping cart data
   - Temporary order data

2. **Caching:**
   - Book details (most viewed)
   - Search results
   - User preferences
   - API rate limiting

3. **Real-time Data:**
   - Active users count
   - Popular books trending
   - Flash sale countdowns

**TTL Strategy:**
- Sessions: 7 days
- Book cache: 1 hour
- Search cache: 15 minutes
- Cart: 24 hours

---

#### **Elasticsearch (Search Engine)**

**Purpose:** Full-text search and analytics

**Use Cases:**
1. **Book Search:**
   - Title, author, description search
   - Fuzzy matching for typos
   - Synonym expansion
   - Relevance ranking

2. **Filters:**
   - Category faceting
   - Price range filtering
   - Rating filtering
   - Format filtering

3. **Recommendations:**
   - Similar books (More Like This query)
   - Trending books (aggregations)

**Indices:**
```json
{
  "books_index": {
    "mappings": {
      "properties": {
        "title": {"type": "text", "analyzer": "english"},
        "author": {"type": "text"},
        "description": {"type": "text"},
        "category": {"type": "keyword"},
        "price": {"type": "float"},
        "rating": {"type": "float"}
      }
    }
  }
}
```

---

## 🔐 Authentication & Authorization

### JWT Token Flow

```
┌────────┐                                      ┌────────┐
│ Client │                                      │  API   │
└───┬────┘                                      └───┬────┘
    │                                               │
    │  POST /api/auth/login                        │
    │  {email, password}                           │
    ├──────────────────────────────────────────────>
    │                                               │
    │                     Verify credentials        │
    │                     Generate JWT tokens       │
    │                                               │
    │  200 OK                                       │
    │  {access_token, refresh_token}                │
    <──────────────────────────────────────────────┤
    │                                               │
    │  GET /api/books                               │
    │  Authorization: Bearer {access_token}         │
    ├──────────────────────────────────────────────>
    │                                               │
    │                     Validate JWT              │
    │                     Process request           │
    │                                               │
    │  200 OK                                       │
    │  {books: [...]}                               │
    <──────────────────────────────────────────────┤
    │                                               │
```

**Token Structure:**
- **Access Token:** Short-lived (15 minutes), contains user ID and role
- **Refresh Token:** Long-lived (7 days), stored in Redis
- **Algorithm:** RS256 (asymmetric)
- **Claims:** user_id, email, role, exp, iat

---

## 🚀 API Endpoints

### Authentication
- `POST /api/auth/register` - Create new account
- `POST /api/auth/login` - Login and get tokens
- `POST /api/auth/refresh` - Refresh access token
- `POST /api/auth/logout` - Invalidate tokens
- `POST /api/auth/forgot-password` - Request password reset
- `POST /api/auth/reset-password` - Reset password with token

### Users
- `GET /api/users/me` - Get current user profile
- `PUT /api/users/me` - Update user profile
- `GET /api/users/me/orders` - Get user orders
- `GET /api/users/me/wishlist` - Get user wishlist
- `POST /api/users/me/wishlist/{book_id}` - Add to wishlist
- `DELETE /api/users/me/wishlist/{book_id}` - Remove from wishlist

### Books
- `GET /api/books` - List books with pagination and filters
- `GET /api/books/{book_id}` - Get book details
- `GET /api/books/search` - Search books (Elasticsearch)
- `GET /api/books/featured` - Get featured books
- `GET /api/books/recommendations` - Get personalized recommendations
- `GET /api/books/{book_id}/reviews` - Get book reviews
- `POST /api/books/{book_id}/reviews` - Create review

### Cart
- `GET /api/cart` - Get user cart
- `POST /api/cart/items` - Add item to cart
- `PUT /api/cart/items/{item_id}` - Update cart item
- `DELETE /api/cart/items/{item_id}` - Remove from cart
- `DELETE /api/cart` - Clear cart

### Orders
- `POST /api/orders` - Create order from cart
- `GET /api/orders` - List user orders
- `GET /api/orders/{order_id}` - Get order details
- `GET /api/orders/{order_id}/tracking` - Get tracking info
- `POST /api/orders/{order_id}/cancel` - Cancel order

### Support
- `GET /api/support/tickets` - List user tickets
- `POST /api/support/tickets` - Create support ticket
- `GET /api/support/tickets/{ticket_id}` - Get ticket details
- `POST /api/support/tickets/{ticket_id}/messages` - Send message
- `PUT /api/support/tickets/{ticket_id}/close` - Close ticket

---

## 📈 Performance Optimization

### Caching Strategy

**1. Database Query Caching:**
```python
@cache(ttl=3600)
def get_book(book_id: str):
    return db.query(Book).filter(Book.id == book_id).first()
```

**2. API Response Caching:**
```python
@app.get("/api/books/featured")
@cache_response(ttl=300)
async def get_featured_books():
    return await book_service.get_featured()
```

**3. Search Result Caching:**
```python
cache_key = f"search:{query}:{filters}"
cached_result = redis.get(cache_key)
if cached_result:
    return cached_result
```

### Database Optimization

**Indexes:**
```sql
-- Books table
CREATE INDEX idx_books_category ON books(category);
CREATE INDEX idx_books_rating ON books(rating);
CREATE INDEX idx_books_price ON books(price);
CREATE INDEX idx_books_published_date ON books(published_date);

-- Orders table
CREATE INDEX idx_orders_user_id ON orders(user_id);
CREATE INDEX idx_orders_status ON orders(status);
CREATE INDEX idx_orders_created_at ON orders(created_at);

-- Full-text search
CREATE INDEX idx_books_fulltext ON books
USING GIN (to_tsvector('english', title || ' ' || description));
```

---

## 🔒 Security Measures

### 1. **Authentication Security**
- Bcrypt password hashing (cost factor: 12)
- JWT with RS256 asymmetric encryption
- Refresh token rotation
- Rate limiting on auth endpoints (5 attempts/15 min)

### 2. **API Security**
- CORS with whitelist
- CSRF protection
- SQL injection prevention (parameterized queries)
- XSS protection (input sanitization)
- Rate limiting (100 requests/minute per user)

### 3. **Data Security**
- PII encryption at rest
- SSL/TLS for data in transit
- Database access via connection pooling
- Regular security audits
- Dependency vulnerability scanning

---

## 🌐 Scalability Plan

### Horizontal Scaling

```
┌──────────────────────────────────────┐
│      Load Balancer (NGINX)           │
└──────────────┬───────────────────────┘
               │
       ┌───────┼────────┐
       ▼       ▼        ▼
    ┌────┐  ┌────┐  ┌────┐
    │API1│  │API2│  │API3│  (Auto-scaling)
    └──┬─┘  └──┬─┘  └──┬─┘
       │       │       │
       └───────┼───────┘
               ▼
    ┌─────────────────────┐
    │  PostgreSQL Master  │
    │  (Read Replicas x3) │
    └─────────────────────┘
```

### Caching Layers

```
Request → CDN → API Cache → Redis → Database
           ↓       ↓         ↓        ↓
       Static   Response   Session   Persistent
       Assets   Cache      Data      Data
```

---

## 📊 Monitoring & Logging

### Metrics to Track

1. **Application Metrics:**
   - Request rate (req/sec)
   - Response time (p50, p95, p99)
   - Error rate (4xx, 5xx)
   - Active users

2. **Database Metrics:**
   - Query performance
   - Connection pool usage
   - Slow queries log
   - Cache hit rate

3. **Business Metrics:**
   - Orders per hour
   - Conversion rate
   - Average order value
   - Cart abandonment rate

### Logging Strategy

```python
import structlog

logger = structlog.get_logger()

logger.info(
    "order_created",
    order_id=order.id,
    user_id=user.id,
    total=order.total,
    items_count=len(order.items)
)
```

---

## 🚨 Error Handling

### Error Response Format

```json
{
  "error": {
    "code": "BOOK_NOT_FOUND",
    "message": "The requested book does not exist",
    "details": {
      "book_id": "123e4567-e89b-12d3-a456-426614174000"
    },
    "timestamp": "2026-01-09T12:00:00Z",
    "request_id": "req_abc123"
  }
}
```

### HTTP Status Codes

- `200 OK` - Success
- `201 Created` - Resource created
- `400 Bad Request` - Invalid input
- `401 Unauthorized` - Not authenticated
- `403 Forbidden` - Not authorized
- `404 Not Found` - Resource not found
- `409 Conflict` - Resource conflict
- `422 Unprocessable Entity` - Validation error
- `429 Too Many Requests` - Rate limit exceeded
- `500 Internal Server Error` - Server error

---

## 📦 Deployment Architecture

### Production Environment

```
┌─────────────────────────────────────────────┐
│              AWS / GCP Cloud                 │
├─────────────────────────────────────────────┤
│                                             │
│  ┌──────────────┐    ┌─────────────────┐  │
│  │  CloudFront  │───>│   S3 (Assets)   │  │
│  │     (CDN)    │    └─────────────────┘  │
│  └──────┬───────┘                          │
│         │                                   │
│         ▼                                   │
│  ┌──────────────┐                          │
│  │     ALB      │                          │
│  │(Load Balancer)                          │
│  └──────┬───────┘                          │
│         │                                   │
│    ┌────┴─────┐                            │
│    ▼          ▼                            │
│  ┌────┐    ┌────┐                          │
│  │ECS │    │ECS │  (Fargate Containers)    │
│  │Task│    │Task│                          │
│  └──┬─┘    └──┬─┘                          │
│     │         │                             │
│     └────┬────┘                             │
│          │                                  │
│    ┌─────┴──────┐                          │
│    ▼            ▼                          │
│  ┌────┐      ┌────┐                        │
│  │ RDS│      │ElastiCache                  │
│  │(PG)│      │(Redis)│                     │
│  └────┘      └────┘                        │
│                                             │
└─────────────────────────────────────────────┘
```

---

## 🔄 CI/CD Pipeline

```
┌──────────┐     ┌──────────┐     ┌──────────┐     ┌──────────┐
│   Git    │────>│  GitHub  │────>│  Docker  │────>│   AWS    │
│   Push   │     │  Actions │     │   Build  │     │   ECS    │
└──────────┘     └────┬─────┘     └──────────┘     └──────────┘
                      │
                      ├─> Lint & Format
                      ├─> Unit Tests
                      ├─> Integration Tests
                      ├─> Security Scan
                      ├─> Build Container
                      └─> Deploy to Staging → Manual Approve → Deploy to Prod
```

---

This architecture is designed for:
- ✅ **Scalability** - Can handle millions of users
- ✅ **Performance** - <100ms API response time
- ✅ **Reliability** - 99.9% uptime SLA
- ✅ **Security** - Enterprise-grade protection
- ✅ **Maintainability** - Clean, modular code

---

*System Architecture Version: 1.0*
*Last Updated: January 2026*
*Status: Production-Ready Design*
