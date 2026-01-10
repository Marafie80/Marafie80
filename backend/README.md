# BookMart Backend API

Production-ready FastAPI backend for the BookMart book marketplace platform.

## Features

- ✅ **Authentication & Authorization**: JWT-based auth with access/refresh tokens
- ✅ **User Management**: Registration, login, profile management, wishlist
- ✅ **Book Catalog**: Full CRUD operations, search, filtering, pagination
- ✅ **Reviews & Ratings**: User reviews with verified purchase badges
- ✅ **Database**: PostgreSQL with SQLAlchemy 2.0 async ORM
- ✅ **Validation**: Pydantic schemas for request/response validation
- ✅ **Security**: Password hashing (bcrypt), CORS, trusted hosts
- ✅ **Error Handling**: Comprehensive exception handlers
- ✅ **API Documentation**: Auto-generated OpenAPI/Swagger docs

## Tech Stack

- **Framework**: FastAPI 0.109+
- **Database**: PostgreSQL 15+ with asyncpg driver
- **ORM**: SQLAlchemy 2.0 (async)
- **Authentication**: JWT (python-jose)
- **Password Hashing**: Bcrypt (passlib)
- **Validation**: Pydantic 2.5+
- **Server**: Uvicorn with auto-reload

## Project Structure

```
backend/
├── app/
│   ├── api/              # API route handlers
│   │   ├── auth.py       # Authentication endpoints
│   │   ├── users.py      # User management endpoints
│   │   └── books.py      # Book catalog endpoints
│   ├── core/             # Core utilities
│   │   ├── security.py   # JWT & password hashing
│   │   └── dependencies.py  # FastAPI dependencies
│   ├── models/           # SQLAlchemy models
│   │   ├── user.py       # User model
│   │   ├── book.py       # Book model
│   │   ├── order.py      # Order models
│   │   ├── review.py     # Review model
│   │   └── support.py    # Support ticket models
│   ├── schemas/          # Pydantic schemas
│   │   ├── user.py       # User schemas
│   │   ├── book.py       # Book schemas
│   │   ├── auth.py       # Auth schemas
│   │   ├── order.py      # Order schemas
│   │   └── review.py     # Review schemas
│   ├── config.py         # Configuration management
│   ├── database.py       # Database connection
│   └── main.py           # FastAPI application
├── docs/                 # Documentation
│   ├── ARCHITECTURE.md   # System architecture
│   ├── DATABASE_SCHEMA.md  # Database schema
│   └── API_ENDPOINTS.md  # API specifications
├── requirements.txt      # Python dependencies
├── .env.example          # Environment variables template
└── README.md             # This file
```

## Getting Started

### Prerequisites

- Python 3.11+
- PostgreSQL 15+
- Redis (optional, for caching)
- Elasticsearch (optional, for search)

### Installation

1. **Clone the repository**
   ```bash
   cd backend
   ```

2. **Create virtual environment**
   ```bash
   python -m venv venv
   source venv/bin/activate  # On Windows: venv\Scripts\activate
   ```

3. **Install dependencies**
   ```bash
   pip install -r requirements.txt
   ```

4. **Set up environment variables**
   ```bash
   cp .env.example .env
   # Edit .env with your configuration
   ```

5. **Set up database**
   ```bash
   # Create PostgreSQL database
   createdb bookmart_db

   # Database tables will be created automatically on first run (development mode)
   ```

### Running the Application

**Development mode** (with auto-reload):
```bash
uvicorn app.main:app --reload --host 0.0.0.0 --port 8000
```

**Production mode**:
```bash
uvicorn app.main:app --host 0.0.0.0 --port 8000 --workers 4
```

**Or use the main.py directly**:
```bash
python -m app.main
```

### Accessing the API

- **API Base URL**: http://localhost:8000
- **API Documentation**: http://localhost:8000/docs (Swagger UI)
- **Alternative Docs**: http://localhost:8000/redoc (ReDoc)
- **Health Check**: http://localhost:8000/health

## API Endpoints

### Authentication (`/api/v1/auth`)

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| POST | `/register` | Register new user | No |
| POST | `/login` | Login user | No |
| POST | `/refresh` | Refresh access token | No |
| POST | `/logout` | Logout user | Yes |
| GET | `/me` | Get current user info | Yes |

### Users (`/api/v1/users`)

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| GET | `/me/profile` | Get my profile | Yes |
| PATCH | `/me/profile` | Update my profile | Yes |
| GET | `/me/wishlist` | Get my wishlist | Yes |
| POST | `/me/wishlist` | Add to wishlist | Yes |
| DELETE | `/me/wishlist` | Remove from wishlist | Yes |
| GET | `/{user_id}/profile` | Get user profile | No |

### Books (`/api/v1/books`)

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| GET | `/` | List books (paginated) | No |
| GET | `/search?q={query}` | Search books | No |
| GET | `/featured` | Get featured books | No |
| GET | `/bestsellers` | Get bestsellers | No |
| GET | `/{book_id}` | Get book details | No |
| POST | `/` | Create book | Yes (Admin) |
| PATCH | `/{book_id}` | Update book | Yes (Admin) |
| DELETE | `/{book_id}` | Delete book | Yes (Admin) |
| GET | `/{book_id}/reviews` | Get book reviews | No |
| POST | `/{book_id}/reviews` | Create review | Yes |

## Authentication

The API uses JWT bearer token authentication.

**1. Register or login to get tokens:**
```bash
curl -X POST http://localhost:8000/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email": "user@example.com", "password": "password123"}'
```

**Response:**
```json
{
  "user": {...},
  "tokens": {
    "access_token": "eyJhbGc...",
    "refresh_token": "eyJhbGc...",
    "token_type": "bearer",
    "expires_in": 900
  }
}
```

**2. Use access token in requests:**
```bash
curl http://localhost:8000/api/v1/users/me/profile \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN"
```

## Database Migrations

For production, use Alembic for database migrations:

```bash
# Initialize Alembic (first time only)
alembic init alembic

# Create migration
alembic revision --autogenerate -m "Description"

# Run migrations
alembic upgrade head

# Rollback migration
alembic downgrade -1
```

## Testing

```bash
# Install test dependencies
pip install pytest pytest-asyncio httpx

# Run tests
pytest

# Run with coverage
pytest --cov=app --cov-report=html
```

## Environment Variables

Key environment variables (see `.env.example` for full list):

- `DATABASE_URL`: PostgreSQL connection string
- `JWT_SECRET_KEY`: Secret key for JWT tokens
- `CORS_ORIGINS`: Allowed CORS origins (comma-separated)
- `ENVIRONMENT`: `development` or `production`
- `DEBUG`: Enable/disable debug mode

## Security Considerations

- ✅ Password hashing with bcrypt
- ✅ JWT token expiration (15 min access, 7 days refresh)
- ✅ CORS configuration
- ✅ SQL injection protection (SQLAlchemy ORM)
- ✅ Input validation (Pydantic)
- ✅ Rate limiting (recommended: add slowapi or similar)
- ⚠️  HTTPS required in production
- ⚠️  Environment variables should be secured
- ⚠️  Use RS256 for JWT in production (generate key pairs)

## Production Deployment

**Recommended stack:**
- **Server**: Uvicorn with Gunicorn
- **Reverse Proxy**: Nginx
- **Database**: PostgreSQL 15+
- **Cache**: Redis
- **Search**: Elasticsearch
- **Containerization**: Docker + Docker Compose
- **Orchestration**: Kubernetes (optional)

**Docker deployment:**
```bash
# Build image
docker build -t bookmart-api .

# Run container
docker run -d -p 8000:8000 --env-file .env bookmart-api
```

## Documentation

- [System Architecture](docs/ARCHITECTURE.md) - High-level system design
- [Database Schema](docs/DATABASE_SCHEMA.md) - Database structure
- [API Endpoints](docs/API_ENDPOINTS.md) - Complete API reference

## License

Proprietary - All rights reserved

## Support

For issues or questions, contact: support@bookmart.com
