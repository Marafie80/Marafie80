# BookMart Deployment Guide

## Overview

This document provides comprehensive deployment instructions for the BookMart platform across all three components: Backend (FastAPI), Web (React + Next.js), and Mobile (Flutter).

---

## Table of Contents

1. [Prerequisites](#prerequisites)
2. [Backend Deployment (FastAPI)](#backend-deployment)
3. [Web Deployment (Next.js)](#web-deployment)
4. [Mobile Deployment (Flutter)](#mobile-deployment)
5. [Database Setup](#database-setup)
6. [Environment Configuration](#environment-configuration)
7. [CI/CD Pipeline](#cicd-pipeline)
8. [Monitoring & Logging](#monitoring--logging)
9. [Backup & Disaster Recovery](#backup--disaster-recovery)

---

## Prerequisites

### Required Infrastructure

- **Cloud Provider**: AWS, Google Cloud, or Azure
- **Domain Name**: e.g., `bookmart.com`
- **SSL Certificate**: Let's Encrypt or cloud provider certificate
- **PostgreSQL Database**: Version 15+ (managed service recommended)
- **Redis**: Version 7+ (for caching and sessions)
- **Elasticsearch**: Version 8+ (optional, for search)
- **CDN**: CloudFlare, AWS CloudFront, or similar
- **Object Storage**: AWS S3, Google Cloud Storage, or similar

### Required Tools

- Docker & Docker Compose
- Kubernetes (kubectl) - optional
- Git
- Node.js 18+ & npm/yarn
- Python 3.11+
- Flutter SDK 3.16+
- PostgreSQL client

---

## Backend Deployment

### Option 1: Docker Deployment (Recommended)

#### Step 1: Create Dockerfile

**File: `backend/Dockerfile`**

```dockerfile
FROM python:3.11-slim

# Set working directory
WORKDIR /app

# Install system dependencies
RUN apt-get update && apt-get install -y \
    gcc \
    postgresql-client \
    && rm -rf /var/lib/apt/lists/*

# Copy requirements
COPY requirements.txt .

# Install Python dependencies
RUN pip install --no-cache-dir -r requirements.txt

# Copy application code
COPY . .

# Create non-root user
RUN useradd -m -u 1000 appuser && chown -R appuser:appuser /app
USER appuser

# Expose port
EXPOSE 8000

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s \
  CMD python -c "import requests; requests.get('http://localhost:8000/health')"

# Run application
CMD ["uvicorn", "app.main:app", "--host", "0.0.0.0", "--port", "8000", "--workers", "4"]
```

#### Step 2: Create docker-compose.yml

**File: `backend/docker-compose.yml`**

```yaml
version: '3.8'

services:
  api:
    build: .
    ports:
      - "8000:8000"
    environment:
      - DATABASE_URL=postgresql+asyncpg://postgres:password@db:5432/bookmart
      - REDIS_URL=redis://redis:6379/0
      - ELASTICSEARCH_URL=http://elasticsearch:9200
    env_file:
      - .env
    depends_on:
      - db
      - redis
      - elasticsearch
    restart: unless-stopped
    volumes:
      - ./keys:/app/keys:ro

  db:
    image: postgres:15-alpine
    environment:
      - POSTGRES_DB=bookmart
      - POSTGRES_USER=postgres
      - POSTGRES_PASSWORD=password
    volumes:
      - postgres_data:/var/lib/postgresql/data
    ports:
      - "5432:5432"
    restart: unless-stopped

  redis:
    image: redis:7-alpine
    ports:
      - "6379:6379"
    restart: unless-stopped
    volumes:
      - redis_data:/data

  elasticsearch:
    image: elasticsearch:8.12.0
    environment:
      - discovery.type=single-node
      - xpack.security.enabled=false
    ports:
      - "9200:9200"
    volumes:
      - elasticsearch_data:/usr/share/elasticsearch/data
    restart: unless-stopped

  nginx:
    image: nginx:alpine
    ports:
      - "80:80"
      - "443:443"
    volumes:
      - ./nginx.conf:/etc/nginx/nginx.conf:ro
      - ./ssl:/etc/nginx/ssl:ro
    depends_on:
      - api
    restart: unless-stopped

volumes:
  postgres_data:
  redis_data:
  elasticsearch_data:
```

#### Step 3: Build and Deploy

```bash
# Build Docker image
docker-compose build

# Start services
docker-compose up -d

# View logs
docker-compose logs -f api

# Run database migrations (first time)
docker-compose exec api alembic upgrade head

# Check health
curl http://localhost:8000/health
```

---

### Option 2: Kubernetes Deployment

#### Step 1: Create Kubernetes Manifests

**File: `backend/k8s/deployment.yaml`**

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: bookmart-api
  labels:
    app: bookmart-api
spec:
  replicas: 3
  selector:
    matchLabels:
      app: bookmart-api
  template:
    metadata:
      labels:
        app: bookmart-api
    spec:
      containers:
      - name: api
        image: bookmart/api:latest
        ports:
        - containerPort: 8000
        env:
        - name: DATABASE_URL
          valueFrom:
            secretKeyRef:
              name: bookmart-secrets
              key: database-url
        - name: OPENAI_API_KEY
          valueFrom:
            secretKeyRef:
              name: bookmart-secrets
              key: openai-api-key
        resources:
          requests:
            memory: "512Mi"
            cpu: "500m"
          limits:
            memory: "1Gi"
            cpu: "1000m"
        livenessProbe:
          httpGet:
            path: /health
            port: 8000
          initialDelaySeconds: 30
          periodSeconds: 10
        readinessProbe:
          httpGet:
            path: /health
            port: 8000
          initialDelaySeconds: 5
          periodSeconds: 5
---
apiVersion: v1
kind: Service
metadata:
  name: bookmart-api-service
spec:
  selector:
    app: bookmart-api
  ports:
  - protocol: TCP
    port: 80
    targetPort: 8000
  type: LoadBalancer
```

#### Step 2: Deploy to Kubernetes

```bash
# Create secrets
kubectl create secret generic bookmart-secrets \
  --from-literal=database-url='postgresql+asyncpg://...' \
  --from-literal=openai-api-key='sk-...' \
  --from-literal=jwt-secret-key='...'

# Apply deployment
kubectl apply -f k8s/deployment.yaml

# Check status
kubectl get pods
kubectl get services

# View logs
kubectl logs -f deployment/bookmart-api

# Scale deployment
kubectl scale deployment/bookmart-api --replicas=5
```

---

### Option 3: Cloud Platform Deployment

#### AWS (Elastic Beanstalk)

```bash
# Install EB CLI
pip install awsebcli

# Initialize EB application
eb init bookmart-api --platform python-3.11 --region us-east-1

# Create environment
eb create bookmart-api-prod --instance-type t3.medium

# Set environment variables
eb setenv \
  DATABASE_URL='postgresql+asyncpg://...' \
  OPENAI_API_KEY='sk-...' \
  AI_PROVIDER='openai'

# Deploy
eb deploy

# View logs
eb logs

# Open application
eb open
```

#### Google Cloud (Cloud Run)

```bash
# Build and push Docker image
gcloud builds submit --tag gcr.io/PROJECT_ID/bookmart-api

# Deploy to Cloud Run
gcloud run deploy bookmart-api \
  --image gcr.io/PROJECT_ID/bookmart-api \
  --platform managed \
  --region us-central1 \
  --allow-unauthenticated \
  --set-env-vars DATABASE_URL='...',OPENAI_API_KEY='...'

# View logs
gcloud run logs read --service bookmart-api
```

#### Azure (App Service)

```bash
# Create resource group
az group create --name bookmart-rg --location eastus

# Create App Service plan
az appservice plan create \
  --name bookmart-plan \
  --resource-group bookmart-rg \
  --sku B1 \
  --is-linux

# Create web app
az webapp create \
  --resource-group bookmart-rg \
  --plan bookmart-plan \
  --name bookmart-api \
  --runtime "PYTHON:3.11"

# Configure environment variables
az webapp config appsettings set \
  --resource-group bookmart-rg \
  --name bookmart-api \
  --settings DATABASE_URL='...' OPENAI_API_KEY='...'

# Deploy code
az webapp up --name bookmart-api --resource-group bookmart-rg
```

---

## Web Deployment

### Build Process

```bash
cd web

# Install dependencies
npm install

# Build for production
npm run build

# Test production build locally
npm start
```

### Option 1: Vercel (Recommended for Next.js)

```bash
# Install Vercel CLI
npm install -g vercel

# Deploy
vercel --prod

# Configure environment variables in Vercel dashboard
# - NEXT_PUBLIC_API_URL=https://api.bookmart.com
# - NEXT_PUBLIC_ENV=production
```

**Automatic Deployments**:
1. Connect GitHub repository to Vercel
2. Every push to `main` branch deploys automatically
3. Preview deployments for pull requests

---

### Option 2: Netlify

```bash
# Install Netlify CLI
npm install -g netlify-cli

# Build
npm run build

# Deploy
netlify deploy --prod --dir=.next

# Configure environment variables
netlify env:set NEXT_PUBLIC_API_URL https://api.bookmart.com
```

**netlify.toml**:
```toml
[build]
  command = "npm run build"
  publish = ".next"

[[redirects]]
  from = "/*"
  to = "/index.html"
  status = 200
```

---

### Option 3: Self-Hosted (Nginx)

#### Step 1: Build Application

```bash
npm run build
```

#### Step 2: Configure Nginx

**File: `/etc/nginx/sites-available/bookmart-web`**

```nginx
server {
    listen 80;
    server_name bookmart.com www.bookmart.com;

    # Redirect to HTTPS
    return 301 https://$server_name$request_uri;
}

server {
    listen 443 ssl http2;
    server_name bookmart.com www.bookmart.com;

    ssl_certificate /etc/letsencrypt/live/bookmart.com/fullchain.pem;
    ssl_certificate_key /etc/letsencrypt/live/bookmart.com/privkey.pem;

    root /var/www/bookmart/web/.next;
    index index.html;

    location / {
        proxy_pass http://localhost:3000;
        proxy_http_version 1.1;
        proxy_set_header Upgrade $http_upgrade;
        proxy_set_header Connection 'upgrade';
        proxy_set_header Host $host;
        proxy_cache_bypass $http_upgrade;
    }

    # Static assets
    location /_next/static {
        alias /var/www/bookmart/web/.next/static;
        expires 365d;
        add_header Cache-Control "public, immutable";
    }

    # Security headers
    add_header X-Frame-Options "SAMEORIGIN" always;
    add_header X-Content-Type-Options "nosniff" always;
    add_header X-XSS-Protection "1; mode=block" always;
}
```

#### Step 3: Start Next.js Server

```bash
# Using PM2
npm install -g pm2
pm2 start npm --name "bookmart-web" -- start
pm2 save
pm2 startup
```

---

## Mobile Deployment

### Android Deployment (Google Play Store)

#### Step 1: Prepare for Release

```bash
cd mobile

# Update version in pubspec.yaml
version: 1.0.0+1  # version_name+build_number

# Build release APK
flutter build apk --release

# Build App Bundle (recommended)
flutter build appbundle --release
```

#### Step 2: Sign the App

**File: `android/key.properties`**

```properties
storePassword=YOUR_STORE_PASSWORD
keyPassword=YOUR_KEY_PASSWORD
keyAlias=bookmart
storeFile=/path/to/keystore.jks
```

**Generate Keystore**:
```bash
keytool -genkey -v -keystore bookmart-keystore.jks \
  -keyalg RSA -keysize 2048 -validity 10000 \
  -alias bookmart
```

#### Step 3: Upload to Google Play Console

1. Create app in Google Play Console
2. Upload signed App Bundle (`.aab` file)
3. Fill in store listing (title, description, screenshots)
4. Set content rating
5. Set pricing & distribution
6. Submit for review

**Automated Upload** (using Fastlane):

```bash
# Install Fastlane
gem install fastlane

# Configure Fastlane
fastlane init

# Deploy to Play Store
fastlane android deploy
```

---

### iOS Deployment (App Store)

#### Step 1: Prepare for Release

```bash
# Update version
# Edit ios/Runner/Info.plist

# Build iOS release
flutter build ios --release
```

#### Step 2: Code Signing

1. Open `ios/Runner.xcworkspace` in Xcode
2. Select "Runner" target
3. Go to "Signing & Capabilities"
4. Select your development team
5. Choose automatic signing

#### Step 3: Archive and Upload

1. In Xcode: Product → Archive
2. Once archived, click "Distribute App"
3. Select "App Store Connect"
4. Upload to App Store Connect
5. Complete app metadata in App Store Connect
6. Submit for review

**Automated Upload** (using Fastlane):

```bash
fastlane ios deploy
```

---

## Database Setup

### PostgreSQL (Production)

#### Managed Database (Recommended)

**AWS RDS**:
```bash
aws rds create-db-instance \
  --db-instance-identifier bookmart-db \
  --db-instance-class db.t3.medium \
  --engine postgres \
  --engine-version 15.4 \
  --master-username admin \
  --master-user-password SecurePassword123! \
  --allocated-storage 100 \
  --storage-type gp3 \
  --backup-retention-period 7 \
  --multi-az
```

**Google Cloud SQL**:
```bash
gcloud sql instances create bookmart-db \
  --database-version=POSTGRES_15 \
  --tier=db-n1-standard-2 \
  --region=us-central1 \
  --backup-start-time=03:00 \
  --enable-bin-log
```

#### Self-Hosted

```bash
# Install PostgreSQL 15
sudo apt-get install postgresql-15

# Create database and user
sudo -u postgres psql
CREATE DATABASE bookmart_db;
CREATE USER bookmart_user WITH PASSWORD 'secure_password';
GRANT ALL PRIVILEGES ON DATABASE bookmart_db TO bookmart_user;
```

#### Run Migrations

```bash
cd backend

# Install Alembic
pip install alembic

# Initialize Alembic (first time)
alembic init alembic

# Create migration
alembic revision --autogenerate -m "Initial schema"

# Apply migrations
alembic upgrade head

# Rollback (if needed)
alembic downgrade -1
```

---

### Redis (Caching)

**Managed Redis**:

**AWS ElastiCache**:
```bash
aws elasticache create-cache-cluster \
  --cache-cluster-id bookmart-redis \
  --cache-node-type cache.t3.medium \
  --engine redis \
  --engine-version 7.0 \
  --num-cache-nodes 1
```

**Self-Hosted**:
```bash
# Install Redis
sudo apt-get install redis-server

# Configure persistence
# Edit /etc/redis/redis.conf
appendonly yes
appendfsync everysec

# Restart Redis
sudo systemctl restart redis
```

---

### Elasticsearch (Search)

**Managed Elasticsearch**:

**AWS OpenSearch**:
```bash
aws opensearch create-domain \
  --domain-name bookmart-search \
  --engine-version OpenSearch_2.11 \
  --cluster-config InstanceType=t3.medium.search,InstanceCount=2
```

**Self-Hosted**:
```bash
# Install Elasticsearch
wget https://artifacts.elastic.co/downloads/elasticsearch/elasticsearch-8.12.0-linux-x86_64.tar.gz
tar -xzf elasticsearch-8.12.0-linux-x86_64.tar.gz
cd elasticsearch-8.12.0/
./bin/elasticsearch
```

---

## Environment Configuration

### Backend (.env)

```bash
# Production Environment
ENVIRONMENT=production
DEBUG=False
APP_NAME=BookMart API
APP_VERSION=1.0.0
API_V1_PREFIX=/api/v1

# Server
HOST=0.0.0.0
PORT=8000

# Database (Use managed database URL)
DATABASE_URL=postgresql+asyncpg://user:pass@db.xxxxx.rds.amazonaws.com:5432/bookmart
DATABASE_POOL_SIZE=20
DATABASE_MAX_OVERFLOW=10

# Redis (Use managed Redis URL)
REDIS_URL=redis://bookmart-redis.xxxxx.cache.amazonaws.com:6379/0
REDIS_CACHE_TTL=3600

# Elasticsearch
ELASTICSEARCH_URL=https://bookmart-search.xxxxx.es.amazonaws.com
ELASTICSEARCH_INDEX_PREFIX=bookmart_prod

# JWT Security (GENERATE NEW KEYS FOR PRODUCTION!)
JWT_SECRET_KEY=GENERATE_SECURE_RANDOM_KEY_HERE
JWT_ALGORITHM=RS256
JWT_ACCESS_TOKEN_EXPIRE_MINUTES=15
JWT_REFRESH_TOKEN_EXPIRE_DAYS=7

# CORS (Production domains only)
CORS_ORIGINS=https://bookmart.com,https://www.bookmart.com,https://api.bookmart.com
CORS_ALLOW_CREDENTIALS=True

# AWS S3 (Book files, covers)
AWS_ACCESS_KEY_ID=AKIA...
AWS_SECRET_ACCESS_KEY=...
AWS_REGION=us-east-1
S3_BUCKET_NAME=bookmart-production-files

# External APIs
STRIPE_API_KEY=sk_live_...
STRIPE_WEBHOOK_SECRET=whsec_...

# AI Configuration
AI_PROVIDER=openai
OPENAI_API_KEY=sk-...
OPENAI_MODEL=gpt-4-turbo
AI_RECOMMENDATIONS_ENABLED=true
AI_READER_LEVEL_ENABLED=true
AI_SUPPORT_CHAT_ENABLED=true
AI_CACHE_ENABLED=true

# Email (Production SMTP)
SMTP_HOST=smtp.sendgrid.net
SMTP_PORT=587
SMTP_USER=apikey
SMTP_PASSWORD=SG.xxxxx
EMAIL_FROM=noreply@bookmart.com
```

### Web (.env.production)

```bash
NEXT_PUBLIC_API_URL=https://api.bookmart.com
NEXT_PUBLIC_ENV=production
NEXT_PUBLIC_STRIPE_PUBLISHABLE_KEY=pk_live_...
NEXT_PUBLIC_GA_TRACKING_ID=G-XXXXXXXXXX
```

### Mobile (Flutter)

**File: `mobile/lib/config/environment.dart`**

```dart
class Environment {
  static const String apiUrl = String.fromEnvironment(
    'API_URL',
    defaultValue: 'https://api.bookmart.com',
  );

  static const String environment = String.fromEnvironment(
    'ENVIRONMENT',
    defaultValue: 'production',
  );
}
```

**Build with environment**:
```bash
flutter build apk --dart-define=API_URL=https://api.bookmart.com --dart-define=ENVIRONMENT=production
```

---

## CI/CD Pipeline

### GitHub Actions (Recommended)

**File: `.github/workflows/deploy.yml`**

```yaml
name: Deploy BookMart

on:
  push:
    branches: [ main ]

jobs:
  deploy-backend:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3

      - name: Build Docker image
        run: |
          cd backend
          docker build -t bookmart-api:latest .

      - name: Push to registry
        run: |
          echo ${{ secrets.DOCKER_PASSWORD }} | docker login -u ${{ secrets.DOCKER_USERNAME }} --password-stdin
          docker tag bookmart-api:latest ${{ secrets.DOCKER_USERNAME }}/bookmart-api:latest
          docker push ${{ secrets.DOCKER_USERNAME }}/bookmart-api:latest

      - name: Deploy to production
        run: |
          # SSH into server and pull new image
          ssh ${{ secrets.SSH_USER }}@${{ secrets.SSH_HOST }} '
            cd /var/www/bookmart/backend
            docker-compose pull
            docker-compose up -d
          '

  deploy-web:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3

      - name: Setup Node.js
        uses: actions/setup-node@v3
        with:
          node-version: '18'

      - name: Install dependencies
        run: |
          cd web
          npm ci

      - name: Build
        run: |
          cd web
          npm run build
        env:
          NEXT_PUBLIC_API_URL: ${{ secrets.API_URL }}

      - name: Deploy to Vercel
        run: |
          npm install -g vercel
          cd web
          vercel --prod --token=${{ secrets.VERCEL_TOKEN }}
```

---

## Monitoring & Logging

### Backend Monitoring

**Sentry (Error Tracking)**:

```bash
pip install sentry-sdk[fastapi]
```

```python
# app/main.py
import sentry_sdk

sentry_sdk.init(
    dsn="https://xxxxx@sentry.io/xxxxx",
    environment="production",
    traces_sample_rate=0.1,
)
```

**Prometheus + Grafana (Metrics)**:

```python
# Install
pip install prometheus-fastapi-instrumentator

# app/main.py
from prometheus_fastapi_instrumentator import Instrumentator

Instrumentator().instrument(app).expose(app)
```

### Application Logging

```python
# Use structured logging
import logging
import json

logging.basicConfig(
    level=logging.INFO,
    format='%(asctime)s - %(name)s - %(levelname)s - %(message)s'
)

# Log to JSON for easier parsing
logger = logging.getLogger(__name__)
logger.info(json.dumps({
    "event": "user_login",
    "user_id": user_id,
    "timestamp": datetime.utcnow().isoformat()
}))
```

### Web Monitoring

**Google Analytics**:

```jsx
// pages/_app.tsx
import Script from 'next/script'

export default function App({ Component, pageProps }) {
  return (
    <>
      <Script
        src={`https://www.googletagmanager.com/gtag/js?id=${GA_TRACKING_ID}`}
        strategy="afterInteractive"
      />
      <Component {...pageProps} />
    </>
  )
}
```

---

## Backup & Disaster Recovery

### Database Backups

**Automated Backups** (AWS RDS):
- Automatic daily backups (7-day retention)
- Point-in-time recovery
- Snapshot before major deployments

**Manual Backup**:
```bash
# Create backup
pg_dump -h db-host -U username -d bookmart_db > backup_$(date +%Y%m%d).sql

# Restore backup
psql -h db-host -U username -d bookmart_db < backup_20240101.sql
```

### Application Backups

- Source code: GitHub (version controlled)
- Environment configs: Encrypted secrets in password manager
- User uploads: S3 with versioning enabled
- Database snapshots: Daily automated snapshots

---

## Health Checks

### Backend Health Endpoint

Already implemented at `GET /health`:

```json
{
  "status": "healthy",
  "version": "1.0.0",
  "environment": "production"
}
```

### Uptime Monitoring

Use services like:
- UptimeRobot
- Pingdom
- StatusCake

Configure alerts for:
- API downtime (> 1 minute)
- High error rate (> 5%)
- Slow response time (> 500ms p95)
- Database connection failures

---

## SSL/TLS Configuration

### Let's Encrypt (Free SSL)

```bash
# Install Certbot
sudo apt-get install certbot python3-certbot-nginx

# Generate certificate
sudo certbot --nginx -d bookmart.com -d www.bookmart.com -d api.bookmart.com

# Auto-renew
sudo certbot renew --dry-run
```

---

## Conclusion

This deployment guide covers all aspects of deploying the BookMart platform to production. Follow the steps appropriate for your chosen infrastructure, and ensure all security best practices are implemented.

**Pre-Launch Checklist**:
- [ ] Database migrated and backed up
- [ ] Environment variables configured
- [ ] SSL certificates installed
- [ ] DNS records configured
- [ ] Monitoring and logging enabled
- [ ] Error tracking configured
- [ ] CI/CD pipeline tested
- [ ] Load testing completed
- [ ] Security audit performed
- [ ] Backup and recovery tested

**Ready for production launch! 🚀**
