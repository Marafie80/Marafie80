# Scalability & Security Guidelines

## Overview

This document provides comprehensive guidelines for scaling the BookMart platform to handle increased traffic and ensuring robust security across all components.

---

## Part 1: Scalability

### Current Capacity

**Based on current architecture**:
- **Concurrent Users**: ~1,000
- **API Requests**: ~100 req/sec
- **Database Connections**: 20 (pool size)
- **AI Requests**: ~50/min (cached)

**Bottlenecks**:
1. Single database instance
2. Synchronous AI calls
3. No CDN for static assets
4. Limited caching

---

### Scaling to 10,000 Users

#### 1. Database Scaling

**Read Replicas** (PostgreSQL):
```
Primary DB (writes) → Read Replica 1 (reads)
                  → Read Replica 2 (reads)
                  → Read Replica 3 (reads)
```

**Implementation**:
```python
# app/database.py
from sqlalchemy import create_engine

# Write engine (primary)
write_engine = create_async_engine(settings.DATABASE_WRITE_URL)

# Read engines (replicas)
read_engines = [
    create_async_engine(url)
    for url in settings.DATABASE_READ_URLS
]

# Round-robin load balancing
import itertools
read_engine_cycle = itertools.cycle(read_engines)

async def get_read_db():
    """Get read-only database session."""
    engine = next(read_engine_cycle)
    async with AsyncSession(engine) as session:
        yield session
```

**Connection Pooling**:
- Increase pool size: `DATABASE_POOL_SIZE=50`
- Enable connection pooler (PgBouncer):
  ```bash
  # Install PgBouncer
  sudo apt-get install pgbouncer

  # Configure /etc/pgbouncer/pgbouncer.ini
  [databases]
  bookmart = host=db-host port=5432 dbname=bookmart_db

  [pgbouncer]
  pool_mode = transaction
  max_client_conn = 1000
  default_pool_size = 25
  ```

---

#### 2. Application Scaling (Horizontal)

**Load Balancer** (Nginx):

```nginx
upstream bookmart_backend {
    least_conn;  # Load balancing method
    server api1.bookmart.com:8000 weight=1;
    server api2.bookmart.com:8000 weight=1;
    server api3.bookmart.com:8000 weight=1;
}

server {
    listen 443 ssl;
    server_name api.bookmart.com;

    location / {
        proxy_pass http://bookmart_backend;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;

        # Health check
        proxy_next_upstream error timeout http_502 http_503 http_504;
    }
}
```

**Auto-Scaling** (Kubernetes):

```yaml
apiVersion: autoscaling/v2
kind: HorizontalPodAutoscaler
metadata:
  name: bookmart-api-hpa
spec:
  scaleTargetRef:
    apiVersion: apps/v1
    kind: Deployment
    name: bookmart-api
  minReplicas: 3
  maxReplicas: 20
  metrics:
  - type: Resource
    resource:
      name: cpu
      target:
        type: Utilization
        averageUtilization: 70
  - type: Resource
    resource:
      name: memory
      target:
        type: Utilization
        averageUtilization: 80
```

---

#### 3. Caching Strategy

**Multi-Layer Caching**:

```
Client → CDN (CloudFlare) → Application Cache (Redis) → Database
```

**Redis Caching Implementation**:

```python
# app/services/cache.py
import redis.asyncio as redis
import json
from functools import wraps

class CacheService:
    def __init__(self, redis_url: str):
        self.redis = redis.from_url(redis_url)

    async def get(self, key: str):
        """Get cached value."""
        value = await self.redis.get(key)
        return json.loads(value) if value else None

    async def set(self, key: str, value, ttl: int = 3600):
        """Set cached value with TTL."""
        await self.redis.setex(
            key,
            ttl,
            json.dumps(value)
        )

    def cached(self, ttl: int = 3600):
        """Decorator for caching function results."""
        def decorator(func):
            @wraps(func)
            async def wrapper(*args, **kwargs):
                # Generate cache key
                cache_key = f"{func.__name__}:{args}:{kwargs}"

                # Check cache
                cached_result = await self.get(cache_key)
                if cached_result:
                    return cached_result

                # Execute function
                result = await func(*args, **kwargs)

                # Store in cache
                await self.set(cache_key, result, ttl)

                return result
            return wrapper
        return decorator

# Usage
cache = CacheService(settings.REDIS_URL)

@cache.cached(ttl=86400)  # 24 hours
async def get_featured_books(limit: int = 10):
    # Expensive database query
    return await db.query(...)
```

**Cache Invalidation**:
```python
# Invalidate on book update
@router.patch("/books/{book_id}")
async def update_book(...):
    # Update database
    await db.update(book)

    # Invalidate cache
    await cache.redis.delete(f"book:{book_id}")
    await cache.redis.delete("featured_books:*")
    await cache.redis.delete("bestsellers:*")
```

**CDN Configuration** (CloudFlare):
- Cache static assets: 1 year
- Cache API responses: 5 minutes (with stale-while-revalidate)
- Purge cache on deployments

---

#### 4. AI Scaling

**Queue-Based Processing** (Celery + Redis):

```python
# Install
pip install celery[redis]

# app/celery_app.py
from celery import Celery

celery_app = Celery(
    'bookmart',
    broker=settings.REDIS_URL,
    backend=settings.REDIS_URL
)

# AI task
@celery_app.task
async def generate_recommendations_task(user_id: str):
    ai_service = get_ai_service()
    recommendations = await ai_service.generate_recommendations(...)

    # Store in cache
    await cache.set(f"recommendations:{user_id}", recommendations, ttl=86400)

    return recommendations

# API endpoint (non-blocking)
@router.get("/recommendations")
async def get_recommendations(user_id: str):
    # Check cache first
    cached = await cache.get(f"recommendations:{user_id}")
    if cached:
        return cached

    # Queue async task
    task = generate_recommendations_task.delay(user_id)

    # Return task ID (client can poll for results)
    return {"task_id": task.id, "status": "processing"}
```

**AI Response Caching**:
- Recommendations: 24 hours
- Reader level: 7 days
- Search enhancement: 1 hour

---

#### 5. Database Optimization

**Indexes** (Critical):
```sql
-- User lookups
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_created_at ON users(created_at);

-- Book searches
CREATE INDEX idx_books_title ON books USING gin(to_tsvector('english', title));
CREATE INDEX idx_books_author ON books(author);
CREATE INDEX idx_books_category ON books(category);
CREATE INDEX idx_books_is_featured ON books(is_featured) WHERE is_featured = true;
CREATE INDEX idx_books_rating ON books(rating DESC);

-- Order queries
CREATE INDEX idx_orders_user_id ON orders(user_id);
CREATE INDEX idx_orders_status ON orders(status);
CREATE INDEX idx_orders_created_at ON orders(created_at DESC);

-- Composite indexes
CREATE INDEX idx_books_category_rating ON books(category, rating DESC);
CREATE INDEX idx_orders_user_status ON orders(user_id, status);
```

**Query Optimization**:
```python
# Use select_related and joinedload to prevent N+1 queries
from sqlalchemy.orm import selectinload

result = await db.execute(
    select(Order)
    .options(selectinload(Order.items))  # Load order items in one query
    .where(Order.user_id == user_id)
)
```

**Partitioning** (for large tables):
```sql
-- Partition orders by year
CREATE TABLE orders (
    id UUID,
    created_at TIMESTAMP,
    ...
) PARTITION BY RANGE (EXTRACT(YEAR FROM created_at));

CREATE TABLE orders_2024 PARTITION OF orders
    FOR VALUES FROM (2024) TO (2025);

CREATE TABLE orders_2025 PARTITION OF orders
    FOR VALUES FROM (2025) TO (2026);
```

---

#### 6. Monitoring & Performance

**Application Performance Monitoring** (New Relic/DataDog):

```python
# Install
pip install newrelic

# Configure
# newrelic.ini

# Initialize
import newrelic.agent
newrelic.agent.initialize('newrelic.ini')

# Instrument
app = newrelic.agent.WSGIApplicationWrapper(app)
```

**Key Metrics to Monitor**:
- API latency (p50, p95, p99)
- Database query time
- Cache hit rate
- Error rate
- Concurrent connections
- CPU and memory usage
- AI API latency and costs

**Alerts**:
- API latency > 500ms (p95)
- Error rate > 1%
- Database connections > 80% of pool
- Cache hit rate < 80%
- Disk usage > 80%

---

### Scaling to 100,000 Users

**Additional Requirements**:

1. **Microservices Architecture**:
   - Separate services: Auth, Books, Orders, AI, Search
   - API Gateway (Kong, AWS API Gateway)
   - Service mesh (Istio)

2. **Database Sharding**:
   - Shard by user_id or region
   - Use distributed SQL (CockroachDB, YugabyteDB)

3. **Elasticsearch Cluster**:
   - 3+ node cluster
   - Replicas for redundancy
   - Index optimization

4. **Message Queue** (RabbitMQ/Kafka):
   - Async order processing
   - Event-driven architecture
   - Pub/sub for notifications

5. **Global CDN**:
   - CloudFlare Enterprise
   - Edge caching
   - DDoS protection

6. **Multi-Region Deployment**:
   - US East, US West, EU, Asia
   - GeoDNS routing
   - Data replication

---

## Part 2: Security

### Security Principles

1. **Defense in Depth**: Multiple layers of security
2. **Least Privilege**: Minimum necessary permissions
3. **Zero Trust**: Verify everything, trust nothing
4. **Security by Design**: Build security in from the start

---

### 1. Authentication & Authorization

#### JWT Security ✅ (Already Implemented)

**Current Implementation**:
- RS256 asymmetric encryption
- 15-minute access tokens
- 7-day refresh tokens
- Secure token storage

**Enhancements**:

```python
# Token Blacklist (for logout/revocation)
class TokenBlacklist:
    async def revoke_token(self, token: str):
        """Add token to blacklist."""
        jti = decode_token(token).get("jti")  # JWT ID
        ttl = get_token_ttl(token)
        await cache.redis.setex(f"blacklist:{jti}", ttl, "1")

    async def is_revoked(self, token: str) -> bool:
        """Check if token is blacklisted."""
        jti = decode_token(token).get("jti")
        return await cache.redis.exists(f"blacklist:{jti}")

# Check in dependency
async def get_current_user(token: str = Depends(security)):
    if await token_blacklist.is_revoked(token):
        raise HTTPException(401, "Token revoked")
    # ... rest of validation
```

**Rate Limiting** (Prevent brute force):

```python
# Install
pip install slowapi

# app/main.py
from slowapi import Limiter, _rate_limit_exceeded_handler
from slowapi.util import get_remote_address
from slowapi.errors import RateLimitExceeded

limiter = Limiter(key_func=get_remote_address)
app.state.limiter = limiter
app.add_exception_handler(RateLimitExceeded, _rate_limit_exceeded_handler)

# Apply to endpoints
@router.post("/login")
@limiter.limit("5/minute")  # 5 attempts per minute
async def login(request: Request, ...):
    ...
```

---

### 2. Input Validation & Sanitization

#### SQL Injection Prevention ✅ (Already Implemented)

**Using SQLAlchemy ORM** - parameterized queries prevent SQL injection.

**Additional Protection**:
```python
from pydantic import validator
import re

class BookCreate(BaseModel):
    title: str

    @validator('title')
    def sanitize_title(cls, v):
        # Remove potential XSS
        v = re.sub(r'<[^>]*>', '', v)
        # Limit length
        if len(v) > 500:
            raise ValueError('Title too long')
        return v.strip()
```

#### XSS Prevention

**Backend** (Content Security Policy):
```python
from fastapi.middleware.trustedhost import TrustedHostMiddleware
from starlette.middleware.httpsredirect import HTTPSRedirectMiddleware

# Force HTTPS in production
if settings.is_production:
    app.add_middleware(HTTPSRedirectMiddleware)

# CSP Headers
@app.middleware("http")
async def add_security_headers(request: Request, call_next):
    response = await call_next(request)
    response.headers["X-Content-Type-Options"] = "nosniff"
    response.headers["X-Frame-Options"] = "DENY"
    response.headers["X-XSS-Protection"] = "1; mode=block"
    response.headers["Content-Security-Policy"] = "default-src 'self'; script-src 'self' 'unsafe-inline' https://cdn.jsdelivr.net; style-src 'self' 'unsafe-inline';"
    response.headers["Strict-Transport-Security"] = "max-age=31536000; includeSubDomains"
    return response
```

**Frontend** (React):
- Use React's built-in XSS protection (escapes by default)
- Sanitize user input with DOMPurify:
  ```jsx
  import DOMPurify from 'dompurify';

  const cleanHTML = DOMPurify.sanitize(userInput);
  ```

---

### 3. Data Encryption

#### Encryption at Rest

**Database** (AWS RDS):
- Enable encryption: `--storage-encrypted`
- KMS key management

**File Storage** (S3):
```python
import boto3

s3 = boto3.client('s3')

# Upload with server-side encryption
s3.put_object(
    Bucket='bookmart-files',
    Key='book.pdf',
    Body=file_data,
    ServerSideEncryption='aws:kms',
    SSEKMSKeyId='arn:aws:kms:...'
)
```

#### Encryption in Transit

- ✅ HTTPS/TLS for all API communication
- ✅ SSL for database connections
- ✅ Secure websockets (WSS) for real-time features

**Environment Variables Encryption**:
```bash
# Use AWS Secrets Manager
aws secretsmanager create-secret \
  --name bookmart/production/database_url \
  --secret-string "postgresql://..."

# Retrieve in application
import boto3

secrets = boto3.client('secretsmanager')
response = secrets.get_secret_value(SecretId='bookmart/production/database_url')
database_url = response['SecretString']
```

---

### 4. Sensitive Data Protection

#### Password Security ✅ (Already Implemented)

- Bcrypt hashing (cost factor: 12)
- Minimum 8 characters, complexity requirements
- Never log passwords

#### PII Protection

```python
# Anonymize logs
import logging

class PIIFilter(logging.Filter):
    def filter(self, record):
        # Remove email addresses
        record.msg = re.sub(r'\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Z|a-z]{2,}\b', '[EMAIL]', str(record.msg))
        # Remove phone numbers
        record.msg = re.sub(r'\b\d{3}[-.]?\d{3}[-.]?\d{4}\b', '[PHONE]', str(record.msg))
        return True

logger = logging.getLogger(__name__)
logger.addFilter(PIIFilter())
```

#### GDPR Compliance

**User Data Export**:
```python
@router.get("/users/me/data-export")
async def export_user_data(current_user: User, db: AsyncSession):
    """Export all user data (GDPR right to data portability)."""
    user_data = {
        "profile": UserProfile.model_validate(current_user).dict(),
        "orders": [OrderResponse.model_validate(o).dict() for o in current_user.orders],
        "reviews": [ReviewResponse.model_validate(r).dict() for r in current_user.reviews],
        "wishlist": current_user.wishlist,
    }
    return JSONResponse(content=user_data)
```

**User Data Deletion**:
```python
@router.delete("/users/me")
async def delete_account(current_user: User, db: AsyncSession):
    """Delete user account (GDPR right to be forgotten)."""
    # Anonymize instead of hard delete (for audit trail)
    current_user.email = f"deleted_{current_user.id}@deleted.com"
    current_user.full_name = "Deleted User"
    current_user.is_active = False
    current_user.password_hash = "deleted"

    # Delete PII
    current_user.phone = None
    current_user.avatar_url = None
    current_user.wishlist = []

    await db.commit()
    return {"message": "Account deleted"}
```

---

### 5. API Security

#### CORS Configuration ✅ (Already Implemented)

```python
# Strict CORS in production
CORS_ORIGINS = ["https://bookmart.com", "https://www.bookmart.com"]
```

#### Rate Limiting (Per User)

```python
from slowapi import Limiter
from slowapi.util import get_remote_address

limiter = Limiter(key_func=lambda: get_current_user().id)

@router.post("/ai/recommendations")
@limiter.limit("10/hour")  # 10 AI requests per hour per user
async def get_recommendations(...):
    ...
```

#### API Key Management (For External Partners)

```python
# Generate API keys
import secrets

def generate_api_key() -> str:
    return f"bm_{secrets.token_urlsafe(32)}"

# Validate API key
async def validate_api_key(api_key: str = Header(...)):
    result = await db.execute(
        select(APIKey).where(
            APIKey.key == api_key,
            APIKey.is_active == True
        )
    )
    key = result.scalar_one_or_none()
    if not key:
        raise HTTPException(401, "Invalid API key")
    return key
```

---

### 6. Third-Party Security

#### OpenAI API Security

```python
# Never log API responses containing user data
logger.info("AI request", extra={
    "user_id": user_id,
    # Don't log: reading_history, personal preferences
})

# Sanitize before sending to AI
def anonymize_for_ai(data: dict) -> dict:
    """Remove PII before sending to external AI."""
    return {
        "reader_level": data["reader_level"],
        "genres": data["genres"],
        # Don't send: email, name, address
    }
```

#### Payment Security (Stripe)

```python
# Never store credit card data
# Use Stripe tokens only

@router.post("/checkout")
async def create_checkout_session(...):
    import stripe

    # Create Stripe checkout session
    session = stripe.checkout.Session.create(
        payment_method_types=['card'],
        line_items=[{
            'price_data': {
                'currency': 'usd',
                'product_data': {'name': book.title},
                'unit_amount': int(book.price * 100),
            },
            'quantity': 1,
        }],
        mode='payment',
        success_url='https://bookmart.com/success',
        cancel_url='https://bookmart.com/cancel',
    )

    return {"session_id": session.id}

# Verify webhook signatures
@router.post("/stripe/webhook")
async def stripe_webhook(request: Request):
    payload = await request.body()
    sig_header = request.headers.get('stripe-signature')

    try:
        event = stripe.Webhook.construct_event(
            payload, sig_header, settings.STRIPE_WEBHOOK_SECRET
        )
    except ValueError:
        raise HTTPException(400, "Invalid payload")
    except stripe.error.SignatureVerificationError:
        raise HTTPException(400, "Invalid signature")

    # Process event
    if event['type'] == 'payment_intent.succeeded':
        # Mark order as paid
        ...
```

---

### 7. Dependency Security

#### Regular Updates

```bash
# Check for vulnerabilities
pip install safety
safety check

# Update dependencies
pip list --outdated
pip install --upgrade package_name
```

#### Lock File

```bash
# Generate requirements.txt with exact versions
pip freeze > requirements.txt

# Or use pipenv/poetry for better dependency management
pipenv install
pipenv lock
```

---

### 8. Security Auditing

#### Logging Security Events

```python
# Security event logging
security_logger = logging.getLogger('security')

# Log authentication attempts
@router.post("/login")
async def login(...):
    try:
        # Authenticate
        user = await authenticate(email, password)
        security_logger.info(f"Login success: {email}")
    except AuthError:
        security_logger.warning(f"Login failed: {email}")
        raise HTTPException(401, "Invalid credentials")
```

#### Regular Security Audits

**Automated Scanning**:
- SAST (Static): Bandit, SonarQube
- DAST (Dynamic): OWASP ZAP
- Dependency: Snyk, Dependabot

```bash
# Run Bandit (Python security linter)
pip install bandit
bandit -r backend/app

# Run OWASP ZAP
docker run -t owasp/zap2docker-stable zap-baseline.py -t https://api.bookmart.com
```

---

### Security Checklist

#### Backend
- [x] JWT authentication with short-lived tokens
- [x] Password hashing with bcrypt
- [x] HTTPS/TLS encryption
- [x] SQL injection prevention (ORM)
- [x] CORS configuration
- [x] Input validation (Pydantic)
- [ ] Rate limiting (implement SlowAPI)
- [ ] Token blacklist (implement Redis)
- [ ] API key management (for partners)
- [ ] Security headers middleware
- [ ] Secrets management (AWS Secrets Manager)
- [ ] Regular dependency updates
- [ ] Security logging and monitoring

#### Frontend
- [x] XSS prevention (React escaping)
- [ ] CSP headers
- [ ] Sanitize user input (DOMPurify)
- [ ] Secure cookie settings (HttpOnly, Secure, SameSite)
- [ ] Content Security Policy
- [ ] Subresource Integrity (SRI)

#### Infrastructure
- [ ] Database encryption at rest
- [ ] Automated backups
- [ ] VPC/Private subnets
- [ ] Security groups/firewall rules
- [ ] DDoS protection (CloudFlare)
- [ ] Web Application Firewall (WAF)
- [ ] Intrusion Detection System (IDS)

#### Compliance
- [ ] GDPR compliance (data export/deletion)
- [ ] PCI DSS (if storing payment data - use Stripe instead)
- [ ] Privacy policy
- [ ] Terms of service
- [ ] Cookie consent banner

---

## Conclusion

This document provides a roadmap for scaling BookMart from 1,000 to 100,000+ users while maintaining robust security. Implement these guidelines incrementally based on your growth trajectory.

**Scaling Priority**:
1. Database read replicas (10K users)
2. Redis caching layer (10K users)
3. Horizontal API scaling (50K users)
4. Microservices architecture (100K+ users)

**Security Priority**:
1. Rate limiting (immediate)
2. Token blacklist (immediate)
3. Security headers (immediate)
4. Secrets management (before production)
5. Regular audits (ongoing)

**Next Steps**: Implement monitoring, test under load, and iterate based on real-world performance data.
