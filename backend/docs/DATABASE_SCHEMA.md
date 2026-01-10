# BookMart Database Schema

## 📊 Database Overview

**Database System:** PostgreSQL 15+
**ORM:** SQLAlchemy 2.0
**Migrations:** Alembic
**Additional Storage:** Redis (cache), Elasticsearch (search)

---

## 🗄️ Entity Relationship Diagram

```
┌─────────────┐         ┌─────────────┐         ┌─────────────┐
│    users    │         │   books     │         │   orders    │
├─────────────┤         ├─────────────┤         ├─────────────┤
│ id (PK)     │         │ id (PK)     │         │ id (PK)     │
│ email       │◄───┐    │ isbn        │    ┌───>│ user_id(FK) │
│ password    │    │    │ title       │    │    │ total       │
│ name        │    │    │ author      │    │    │ status      │
│ role        │    │    │ price       │    │    │ created_at  │
│ created_at  │    │    │ category    │    │    └─────────────┘
└─────────────┘    │    │ publisher   │    │           │
       │           │    │ rating      │    │           │
       │           │    └─────────────┘    │           │
       │           │           │           │           │
       │           │           │           │           ▼
       │           │           ▼           │    ┌─────────────┐
       │           │    ┌─────────────┐    │    │order_items  │
       │           │    │  reviews    │    │    ├─────────────┤
       │           │    ├─────────────┤    │    │ id (PK)     │
       │           └────┤ user_id(FK) │    │    │ order_id(FK)│
       │                │ book_id(FK) │◄───┘    │ book_id(FK) │
       │                │ rating      │         │ quantity    │
       │                │ content     │         │ price       │
       │                │ created_at  │         └─────────────┘
       │                └─────────────┘
       │
       ▼
┌─────────────┐         ┌──────────────────┐
│ addresses   │         │ support_tickets  │
├─────────────┤         ├──────────────────┤
│ id (PK)     │         │ id (PK)          │
│ user_id(FK) │         │ user_id(FK)      │
│ full_name   │         │ order_id(FK)     │
│ line1       │         │ subject          │
│ city        │         │ status           │
│ postal_code │         │ created_at       │
└─────────────┘         └──────────────────┘
                               │
                               ▼
                        ┌──────────────────┐
                        │support_messages  │
                        ├──────────────────┤
                        │ id (PK)          │
                        │ ticket_id(FK)    │
                        │ sender_type      │
                        │ content          │
                        │ created_at       │
                        └──────────────────┘
```

---

## 📋 Table Definitions

### 1. **users**

Stores user account information.

```sql
CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    email VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    full_name VARCHAR(255) NOT NULL,
    phone VARCHAR(20),
    avatar_url VARCHAR(500),
    role VARCHAR(20) NOT NULL DEFAULT 'customer',
    reader_level VARCHAR(20) DEFAULT 'beginner',
    books_read INTEGER DEFAULT 0,
    is_active BOOLEAN DEFAULT TRUE,
    is_verified BOOLEAN DEFAULT FALSE,
    email_verified_at TIMESTAMP,
    last_login_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_role ON users(role);
CREATE INDEX idx_users_created_at ON users(created_at);
```

**Fields:**
- `id` - UUID primary key
- `email` - Unique email address (login identifier)
- `password_hash` - Bcrypt hashed password
- `full_name` - User's full name
- `phone` - Optional phone number
- `avatar_url` - Profile picture URL
- `role` - User role: `customer`, `admin`, `support`
- `reader_level` - AI-calculated: `beginner`, `intermediate`, `advanced`, `avid`
- `books_read` - Count of books read (for gamification)
- `is_active` - Account status
- `is_verified` - Email verification status
- `email_verified_at` - Email verification timestamp
- `last_login_at` - Last login timestamp
- `created_at` - Account creation date
- `updated_at` - Last update timestamp

---

### 2. **books**

Stores book catalog information.

```sql
CREATE TABLE books (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    isbn VARCHAR(20) UNIQUE NOT NULL,
    title VARCHAR(500) NOT NULL,
    author VARCHAR(255) NOT NULL,
    cover_url VARCHAR(500),
    price DECIMAL(10, 2) NOT NULL,
    currency VARCHAR(3) DEFAULT 'USD',
    format VARCHAR(20) NOT NULL,
    category VARCHAR(100) NOT NULL,
    subcategory VARCHAR(100),
    publisher VARCHAR(255) NOT NULL,
    published_date DATE,
    pages INTEGER,
    language VARCHAR(50) DEFAULT 'English',
    description TEXT,
    rating DECIMAL(3, 2) DEFAULT 0.0,
    review_count INTEGER DEFAULT 0,
    is_free BOOLEAN DEFAULT FALSE,
    is_available BOOLEAN DEFAULT TRUE,
    badge VARCHAR(20),
    metadata JSONB,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_books_isbn ON books(isbn);
CREATE INDEX idx_books_category ON books(category);
CREATE INDEX idx_books_author ON books(author);
CREATE INDEX idx_books_rating ON books(rating DESC);
CREATE INDEX idx_books_price ON books(price);
CREATE INDEX idx_books_published_date ON books(published_date DESC);
CREATE INDEX idx_books_format ON books(format);

-- Full-text search index
CREATE INDEX idx_books_fulltext ON books
USING GIN (to_tsvector('english', title || ' ' || author || ' ' || description));
```

**Fields:**
- `id` - UUID primary key
- `isbn` - International Standard Book Number
- `title` - Book title
- `author` - Author name(s)
- `cover_url` - Book cover image URL
- `price` - Book price
- `currency` - Price currency code (ISO 4217)
- `format` - `digital`, `paperback`, `hardcover`, `audiobook`
- `category` - Main category (e.g., Fiction, Non-Fiction)
- `subcategory` - Subcategory (e.g., Mystery, Romance)
- `publisher` - Publisher name
- `published_date` - Publication date
- `pages` - Number of pages
- `language` - Book language
- `description` - Book description
- `rating` - Average rating (0.0-5.0)
- `review_count` - Number of reviews
- `is_free` - Whether book is free
- `is_available` - Whether book is available for purchase
- `badge` - `new`, `bestseller`, `featured`, `winner`, `nominee`
- `metadata` - Additional metadata as JSON (flexible schema)
- `created_at` - Record creation timestamp
- `updated_at` - Last update timestamp

---

### 3. **orders**

Stores order information.

```sql
CREATE TABLE orders (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    order_number VARCHAR(50) UNIQUE NOT NULL,
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    subtotal DECIMAL(10, 2) NOT NULL,
    tax DECIMAL(10, 2) NOT NULL DEFAULT 0.0,
    discount DECIMAL(10, 2) NOT NULL DEFAULT 0.0,
    total DECIMAL(10, 2) NOT NULL,
    currency VARCHAR(3) DEFAULT 'USD',
    status VARCHAR(20) NOT NULL DEFAULT 'pending',
    payment_method VARCHAR(50),
    payment_status VARCHAR(20) DEFAULT 'pending',
    payment_id VARCHAR(255),
    shipping_address_id UUID REFERENCES addresses(id),
    tracking_number VARCHAR(100),
    tracking_url VARCHAR(500),
    estimated_delivery DATE,
    delivered_at TIMESTAMP,
    cancelled_at TIMESTAMP,
    cancellation_reason TEXT,
    notes TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_orders_user_id ON orders(user_id);
CREATE INDEX idx_orders_order_number ON orders(order_number);
CREATE INDEX idx_orders_status ON orders(status);
CREATE INDEX idx_orders_created_at ON orders(created_at DESC);
```

**Fields:**
- `id` - UUID primary key
- `order_number` - Human-readable order number (BM-2026-XXXXX)
- `user_id` - Foreign key to users table
- `subtotal` - Sum of all items
- `tax` - Tax amount
- `discount` - Discount amount (coupons, promos)
- `total` - Final total amount
- `currency` - Order currency
- `status` - `pending`, `processing`, `shipped`, `in_transit`, `delivered`, `cancelled`
- `payment_method` - Payment method used
- `payment_status` - `pending`, `completed`, `failed`, `refunded`
- `payment_id` - External payment processor ID (Stripe)
- `shipping_address_id` - Foreign key to addresses table
- `tracking_number` - Shipment tracking number
- `tracking_url` - Tracking URL
- `estimated_delivery` - Estimated delivery date
- `delivered_at` - Actual delivery timestamp
- `cancelled_at` - Cancellation timestamp
- `cancellation_reason` - Reason for cancellation
- `notes` - Internal notes
- `created_at` - Order creation timestamp
- `updated_at` - Last update timestamp

---

### 4. **order_items**

Stores individual items in an order.

```sql
CREATE TABLE order_items (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    order_id UUID NOT NULL REFERENCES orders(id) ON DELETE CASCADE,
    book_id UUID NOT NULL REFERENCES books(id),
    title VARCHAR(500) NOT NULL,
    author VARCHAR(255) NOT NULL,
    format VARCHAR(20) NOT NULL,
    quantity INTEGER NOT NULL DEFAULT 1,
    unit_price DECIMAL(10, 2) NOT NULL,
    total_price DECIMAL(10, 2) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_order_items_order_id ON order_items(order_id);
CREATE INDEX idx_order_items_book_id ON order_items(book_id);
```

**Fields:**
- `id` - UUID primary key
- `order_id` - Foreign key to orders table
- `book_id` - Foreign key to books table
- `title` - Book title (snapshot at time of order)
- `author` - Author name (snapshot)
- `format` - Book format
- `quantity` - Quantity ordered
- `unit_price` - Price per unit at time of order
- `total_price` - Total for this line item
- `created_at` - Record creation timestamp

---

### 5. **reviews**

Stores user reviews for books.

```sql
CREATE TABLE reviews (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    book_id UUID NOT NULL REFERENCES books(id) ON DELETE CASCADE,
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    rating INTEGER NOT NULL CHECK (rating >= 1 AND rating <= 5),
    title VARCHAR(255),
    content TEXT NOT NULL,
    verified_purchase BOOLEAN DEFAULT FALSE,
    helpful_count INTEGER DEFAULT 0,
    not_helpful_count INTEGER DEFAULT 0,
    is_visible BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(book_id, user_id)
);

CREATE INDEX idx_reviews_book_id ON reviews(book_id);
CREATE INDEX idx_reviews_user_id ON reviews(user_id);
CREATE INDEX idx_reviews_rating ON reviews(rating);
CREATE INDEX idx_reviews_created_at ON reviews(created_at DESC);
```

**Fields:**
- `id` - UUID primary key
- `book_id` - Foreign key to books table
- `user_id` - Foreign key to users table
- `rating` - Star rating (1-5)
- `title` - Review title (optional)
- `content` - Review text
- `verified_purchase` - Whether user purchased the book
- `helpful_count` - Number of "helpful" votes
- `not_helpful_count` - Number of "not helpful" votes
- `is_visible` - Whether review is visible (moderation)
- `created_at` - Review creation timestamp
- `updated_at` - Last update timestamp

**Constraints:**
- One review per user per book

---

### 6. **addresses**

Stores user shipping addresses.

```sql
CREATE TABLE addresses (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    full_name VARCHAR(255) NOT NULL,
    line1 VARCHAR(255) NOT NULL,
    line2 VARCHAR(255),
    city VARCHAR(100) NOT NULL,
    state VARCHAR(100),
    postal_code VARCHAR(20) NOT NULL,
    country VARCHAR(2) NOT NULL,
    phone VARCHAR(20),
    delivery_instructions TEXT,
    is_default BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_addresses_user_id ON addresses(user_id);
```

**Fields:**
- `id` - UUID primary key
- `user_id` - Foreign key to users table
- `full_name` - Recipient name
- `line1` - Address line 1
- `line2` - Address line 2 (optional)
- `city` - City
- `state` - State/Province
- `postal_code` - Postal/ZIP code
- `country` - ISO 3166-1 alpha-2 country code
- `phone` - Contact phone number
- `delivery_instructions` - Special delivery instructions
- `is_default` - Whether this is the default address
- `created_at` - Record creation timestamp
- `updated_at` - Last update timestamp

---

### 7. **support_tickets**

Stores customer support tickets.

```sql
CREATE TABLE support_tickets (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    ticket_number VARCHAR(50) UNIQUE NOT NULL,
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    order_id UUID REFERENCES orders(id) ON DELETE SET NULL,
    subject VARCHAR(255) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'active',
    priority VARCHAR(20) DEFAULT 'normal',
    assigned_agent_id UUID REFERENCES users(id) ON DELETE SET NULL,
    category VARCHAR(50),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    resolved_at TIMESTAMP,
    closed_at TIMESTAMP
);

CREATE INDEX idx_support_tickets_user_id ON support_tickets(user_id);
CREATE INDEX idx_support_tickets_ticket_number ON support_tickets(ticket_number);
CREATE INDEX idx_support_tickets_status ON support_tickets(status);
CREATE INDEX idx_support_tickets_created_at ON support_tickets(created_at DESC);
```

**Fields:**
- `id` - UUID primary key
- `ticket_number` - Human-readable ticket number (ST-XXXXX)
- `user_id` - Foreign key to users table
- `order_id` - Related order (optional)
- `subject` - Ticket subject
- `status` - `active`, `waiting`, `resolved`, `closed`
- `priority` - `low`, `normal`, `high`, `urgent`
- `assigned_agent_id` - Support agent assigned
- `category` - Ticket category (e.g., "Order Issue", "Technical Support")
- `created_at` - Ticket creation timestamp
- `updated_at` - Last update timestamp
- `resolved_at` - Resolution timestamp
- `closed_at` - Closure timestamp

---

### 8. **support_messages**

Stores messages within support tickets.

```sql
CREATE TABLE support_messages (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    ticket_id UUID NOT NULL REFERENCES support_tickets(id) ON DELETE CASCADE,
    sender_id UUID REFERENCES users(id) ON DELETE SET NULL,
    sender_type VARCHAR(20) NOT NULL,
    content TEXT NOT NULL,
    attachments JSONB,
    is_internal BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_support_messages_ticket_id ON support_messages(ticket_id);
CREATE INDEX idx_support_messages_created_at ON support_messages(created_at DESC);
```

**Fields:**
- `id` - UUID primary key
- `ticket_id` - Foreign key to support_tickets table
- `sender_id` - Foreign key to users table (can be null for AI)
- `sender_type` - `user`, `ai`, `agent`
- `content` - Message content
- `attachments` - Array of attachment URLs (JSON)
- `is_internal` - Whether message is internal (agent notes)
- `created_at` - Message timestamp

---

### 9. **wishlist_items**

Stores user wishlists.

```sql
CREATE TABLE wishlist_items (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    book_id UUID NOT NULL REFERENCES books(id) ON DELETE CASCADE,
    added_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(user_id, book_id)
);

CREATE INDEX idx_wishlist_items_user_id ON wishlist_items(user_id);
CREATE INDEX idx_wishlist_items_book_id ON wishlist_items(book_id);
```

---

### 10. **reading_progress** (Optional - Future Enhancement)

Tracks user reading progress for digital books.

```sql
CREATE TABLE reading_progress (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    book_id UUID NOT NULL REFERENCES books(id) ON DELETE CASCADE,
    progress_percent DECIMAL(5, 2) DEFAULT 0.0,
    current_page INTEGER,
    total_pages INTEGER,
    last_read_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    completed_at TIMESTAMP,
    UNIQUE(user_id, book_id)
);

CREATE INDEX idx_reading_progress_user_id ON reading_progress(user_id);
```

---

## 🔄 Database Triggers

### Update `updated_at` Automatically

```sql
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Apply to all tables with updated_at
CREATE TRIGGER update_users_updated_at
    BEFORE UPDATE ON users
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

-- Repeat for: books, orders, reviews, addresses, support_tickets
```

### Update Book Rating on Review Insert/Update/Delete

```sql
CREATE OR REPLACE FUNCTION update_book_rating()
RETURNS TRIGGER AS $$
BEGIN
    UPDATE books SET
        rating = (SELECT AVG(rating)::DECIMAL(3,2) FROM reviews WHERE book_id = COALESCE(NEW.book_id, OLD.book_id)),
        review_count = (SELECT COUNT(*) FROM reviews WHERE book_id = COALESCE(NEW.book_id, OLD.book_id))
    WHERE id = COALESCE(NEW.book_id, OLD.book_id);
    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER update_book_rating_on_review
    AFTER INSERT OR UPDATE OR DELETE ON reviews
    FOR EACH ROW
    EXECUTE FUNCTION update_book_rating();
```

---

## 📊 Database Views (Optional)

### View: Popular Books (Last 30 Days)

```sql
CREATE VIEW popular_books_30d AS
SELECT
    b.*,
    COUNT(DISTINCT oi.order_id) as order_count,
    SUM(oi.quantity) as units_sold
FROM books b
JOIN order_items oi ON b.id = oi.book_id
JOIN orders o ON oi.order_id = o.id
WHERE o.created_at >= CURRENT_DATE - INTERVAL '30 days'
    AND o.status != 'cancelled'
GROUP BY b.id
ORDER BY units_sold DESC;
```

---

## 🗂️ Redis Schema

### Key Patterns

```
# User Sessions
session:{user_id}:{session_id}          # TTL: 7 days
refresh_token:{token}                   # TTL: 7 days

# Caching
cache:book:{book_id}                    # TTL: 1 hour
cache:featured_books                    # TTL: 5 minutes
cache:search:{query_hash}               # TTL: 15 minutes

# Shopping Cart
cart:{user_id}                          # TTL: 24 hours (HASH)

# Rate Limiting
rate_limit:auth:{ip}                    # TTL: 15 minutes (COUNTER)
rate_limit:api:{user_id}                # TTL: 1 minute (COUNTER)

# Real-time Data
active_users                            # SET
trending_books:{category}               # ZSET (sorted by score)
```

### Cart Structure (Hash)

```
cart:user_123 {
    "item_1": "{book_id, quantity, format}",
    "item_2": "{book_id, quantity, format}"
}
```

---

## 🔍 Elasticsearch Schema

### Books Index Mapping

```json
{
  "settings": {
    "number_of_shards": 3,
    "number_of_replicas": 2,
    "analysis": {
      "analyzer": {
        "book_analyzer": {
          "type": "custom",
          "tokenizer": "standard",
          "filter": ["lowercase", "stop", "snowball"]
        }
      }
    }
  },
  "mappings": {
    "properties": {
      "id": {"type": "keyword"},
      "isbn": {"type": "keyword"},
      "title": {
        "type": "text",
        "analyzer": "book_analyzer",
        "fields": {
          "keyword": {"type": "keyword"}
        }
      },
      "author": {
        "type": "text",
        "analyzer": "book_analyzer"
      },
      "description": {
        "type": "text",
        "analyzer": "book_analyzer"
      },
      "category": {"type": "keyword"},
      "subcategory": {"type": "keyword"},
      "publisher": {"type": "keyword"},
      "price": {"type": "float"},
      "rating": {"type": "float"},
      "review_count": {"type": "integer"},
      "published_date": {"type": "date"},
      "format": {"type": "keyword"},
      "language": {"type": "keyword"},
      "is_free": {"type": "boolean"},
      "badge": {"type": "keyword"}
    }
  }
}
```

---

## 🔐 Data Security

### PII Encryption

Sensitive fields should be encrypted at application level:
- `users.email` (searchable encryption)
- `addresses.phone`
- `addresses.line1`, `addresses.line2`

### Anonymized Analytics

Create anonymized views for analytics:

```sql
CREATE VIEW analytics_orders AS
SELECT
    id,
    order_number,
    MD5(user_id::TEXT) as user_hash,
    total,
    status,
    created_at
FROM orders;
```

---

## 📈 Performance Considerations

### Connection Pooling

```python
# Database connection pool settings
POOL_SIZE = 20
MAX_OVERFLOW = 10
POOL_TIMEOUT = 30
POOL_RECYCLE = 3600
```

### Query Optimization

- Use appropriate indexes
- Avoid SELECT *
- Use JOINs efficiently
- Implement pagination (LIMIT/OFFSET or cursor-based)
- Use EXPLAIN ANALYZE for slow queries

---

## 🔄 Backup Strategy

### PostgreSQL Backups

- **Continuous Archiving:** WAL archiving to S3
- **Daily Snapshots:** Full database backup at 2 AM UTC
- **Retention:** 30 days for daily, 12 months for monthly
- **Point-in-Time Recovery:** Supported via WAL

### Redis Backups

- **RDB Snapshots:** Every 15 minutes if changes exist
- **AOF:** Append-only file for durability
- **Replication:** Master-slave replication

---

*Database Schema Version: 1.0*
*Last Updated: January 2026*
*Status: Production-Ready Schema*
