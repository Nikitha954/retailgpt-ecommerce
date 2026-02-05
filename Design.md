# RetailGPT - System Design Document

## Architecture Overview

RetailGPT follows a microservices architecture with clear separation between the core eCommerce backend (Spring Boot) and the AI processing layer (Python/AWS Bedrock). The system is designed for scalability, maintainability, and seamless integration between traditional eCommerce operations and GenAI capabilities.

## High-Level Architecture

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   React.js      │    │   WhatsApp      │    │   Admin Panel   │
│   Frontend      │    │   Business API  │    │   Dashboard     │
└─────────────────┘    └─────────────────┘    └─────────────────┘
         │                       │                       │
         └───────────────────────┼───────────────────────┘
                                 │
         ┌─────────────────────────────────────────────────┐
         │              API Gateway                        │
         │           (Spring Cloud Gateway)                │
         └─────────────────────────────────────────────────┘
                                 │
    ┌────────────────────────────┼────────────────────────────┐
    │                            │                            │
┌───▼────┐  ┌──────────┐  ┌─────▼─────┐  ┌──────────────┐   │
│Product │  │Order     │  │User       │  │Analytics     │   │
│Service │  │Service   │  │Service    │  │Service       │   │
└────────┘  └──────────┘  └───────────┘  └──────────────┘   │
    │           │              │              │              │
    └───────────┼──────────────┼──────────────┼──────────────┘
                │              │              │
         ┌──────▼──────────────▼──────────────▼──────┐
         │           Message Queue                   │
         │         (Apache Kafka/RabbitMQ)          │
         └──────────────────┬───────────────────────┘
                            │
    ┌───────────────────────▼───────────────────────┐
    │              AI Processing Layer              │
    │         (Python FastAPI / LangChain4j)       │
    └───────────────────────┬───────────────────────┘
                            │
    ┌───────────────────────▼───────────────────────┐
    │              AWS Bedrock                      │
    │    (Titan, Claude, Stable Diffusion)         │
    └───────────────────────────────────────────────┘

┌─────────────┐  ┌─────────────┐  ┌─────────────┐
│   MySQL     │  │  Vector DB  │  │   Redis     │
│(Transactional)│  │(Semantic   │  │  (Cache)    │
│             │  │ Search)     │  │             │
└─────────────┘  └─────────────┘  └─────────────┘
```

## Core Components

### 1. Spring Boot Microservices

#### Product Service
**Responsibilities:**
- Traditional CRUD operations for products
- Integration with AI Auto-Cataloging service
- Image storage and management
- Category and inventory management

**Key APIs:**
```java
@RestController
@RequestMapping("/api/products")
public class ProductController {
    
    @PostMapping("/auto-catalog")
    public ResponseEntity<ProductDTO> createProductFromImage(
        @RequestParam("image") MultipartFile image,
        @RequestParam(value = "language", defaultValue = "en") String language
    );
    
    @GetMapping("/search/semantic")
    public ResponseEntity<List<ProductDTO>> semanticSearch(
        @RequestParam String query,
        @RequestParam(defaultValue = "10") int limit
    );
    
    @PostMapping("/{id}/generate-description")
    public ResponseEntity<String> generateMarketingDescription(
        @PathVariable Long id,
        @RequestParam(defaultValue = "en") String language
    );
}
```

#### User Service
**Responsibilities:**
- User authentication and authorization
- Customer profile management
- Role-based access control (Admin, Customer)
- Integration with WhatsApp Business API

**Key APIs:**
```java
@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @PostMapping("/register")
    public ResponseEntity<UserDTO> registerUser(@RequestBody SignupRequest request);
    
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> authenticateUser(@RequestBody LoginRequest request);
    
    @GetMapping("/profile")
    public ResponseEntity<UserDTO> getUserProfile();
}
```

#### Order Service
**Responsibilities:**
- Order lifecycle management
- Payment processing integration
- Order status tracking
- Integration with analytics service

**Key APIs:**
```java
@RestController
@RequestMapping("/api/orders")
public class OrderController {
    
    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(@RequestBody OrderRequestDTO request);
    
    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrder(@PathVariable Long id);
    
    @PutMapping("/{id}/status")
    public ResponseEntity<OrderDTO> updateOrderStatus(
        @PathVariable Long id, 
        @RequestBody OrderStatusUpdateDto status
    );
}
```

#### Analytics Service
**Responsibilities:**
- Business intelligence queries processing
- Natural language to SQL conversion
- Report generation
- Performance metrics calculation

**Key APIs:**
```java
@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {
    
    @PostMapping("/query")
    public ResponseEntity<AnalyticsResponse> processNaturalLanguageQuery(
        @RequestBody String query,
        @RequestParam(defaultValue = "en") String language
    );
    
    @GetMapping("/reports/{type}")
    public ResponseEntity<ReportDTO> generateReport(
        @PathVariable String type,
        @RequestParam String period
    );
}
```

### 2. AI Processing Layer

#### Auto-Cataloging Service
**Technology Stack:** Python FastAPI + AWS Bedrock + Computer Vision
**Responsibilities:**
- Multimodal image analysis
- Product information extraction
- Price estimation
- Category classification

**Key Components:**
```python
class AutoCatalogService:
    def analyze_product_image(self, image_data: bytes) -> ProductAnalysis:
        # Use AWS Bedrock Titan for image analysis
        # Extract product features, colors, materials
        pass
    
    def generate_product_details(self, analysis: ProductAnalysis, language: str) -> ProductDetails:
        # Use Claude 3 for generating descriptions
        # Support Hindi/English content generation
        pass
    
    def estimate_pricing(self, product_features: dict) -> PriceEstimate:
        # Market-based pricing suggestions
        pass
```

#### Conversational BI Service
**Technology Stack:** LangChain4j + AWS Bedrock + SQL Agent
**Responsibilities:**
- Natural language query processing
- SQL generation and execution
- Business insights generation
- Multi-language support

**Key Components:**
```java
@Service
public class ConversationalBIService {
    
    public AnalyticsResponse processQuery(String naturalLanguageQuery, String language) {
        // Convert natural language to SQL using Claude 3
        String sql = generateSQL(naturalLanguageQuery, language);
        
        // Execute query safely
        QueryResult result = executeQuery(sql);
        
        // Generate natural language response
        return generateResponse(result, language);
    }
}
```

#### Smart Buyer Assistant
**Technology Stack:** LangChain4j + RAG + Vector Database
**Responsibilities:**
- Customer query understanding
- Product recommendation
- Order assistance
- WhatsApp integration

**Key Components:**
```java
@Service
public class BuyerAssistantService {
    
    public ChatResponse processCustomerQuery(String query, String customerId) {
        // Understand customer intent
        Intent intent = analyzeIntent(query);
        
        // Retrieve relevant products using RAG
        List<Product> relevantProducts = semanticSearch(query);
        
        // Generate contextual response
        return generateResponse(intent, relevantProducts, customerId);
    }
}
```

## Database Design

### 1. Relational Database Schema (MySQL)

#### Core Entities
```sql
-- Users table
CREATE TABLE users (
    user_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    phone_number VARCHAR(15),
    role ENUM('ADMIN', 'CUSTOMER') DEFAULT 'CUSTOMER',
    language_preference VARCHAR(5) DEFAULT 'en',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Categories table
CREATE TABLE categories (
    category_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    category_name VARCHAR(100) NOT NULL,
    category_name_hi VARCHAR(100), -- Hindi name
    description TEXT,
    parent_category_id BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (parent_category_id) REFERENCES categories(category_id)
);

-- Products table
CREATE TABLE products (
    product_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    product_name VARCHAR(255) NOT NULL,
    product_name_hi VARCHAR(255), -- Hindi name
    description TEXT,
    description_hi TEXT, -- Hindi description
    price DECIMAL(10,2) NOT NULL,
    discount DECIMAL(5,2) DEFAULT 0,
    special_price DECIMAL(10,2),
    quantity INTEGER DEFAULT 0,
    category_id BIGINT,
    seller_id BIGINT,
    ai_generated BOOLEAN DEFAULT FALSE,
    confidence_score DECIMAL(3,2), -- AI confidence
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES categories(category_id),
    FOREIGN KEY (seller_id) REFERENCES users(user_id)
);

-- Product images table
CREATE TABLE product_images (
    image_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    product_id BIGINT NOT NULL,
    image_url VARCHAR(500) NOT NULL,
    image_type ENUM('PRIMARY', 'SECONDARY') DEFAULT 'SECONDARY',
    alt_text VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE CASCADE
);

-- Orders table
CREATE TABLE orders (
    order_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    customer_id BIGINT NOT NULL,
    order_status ENUM('PENDING', 'CONFIRMED', 'SHIPPED', 'DELIVERED', 'CANCELLED') DEFAULT 'PENDING',
    total_amount DECIMAL(10,2) NOT NULL,
    payment_status ENUM('PENDING', 'COMPLETED', 'FAILED') DEFAULT 'PENDING',
    shipping_address TEXT,
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    delivery_date TIMESTAMP NULL,
    FOREIGN KEY (customer_id) REFERENCES users(user_id)
);

-- Order items table
CREATE TABLE order_items (
    order_item_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INTEGER NOT NULL,
    unit_price DECIMAL(10,2) NOT NULL,
    total_price DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(product_id)
);

-- Customer interactions table (for AI training)
CREATE TABLE customer_interactions (
    interaction_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    customer_id BIGINT,
    query_text TEXT NOT NULL,
    query_language VARCHAR(5) DEFAULT 'en',
    response_text TEXT,
    intent VARCHAR(100),
    satisfaction_score INTEGER, -- 1-5 rating
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES users(user_id)
);
```

### 2. Vector Database Schema (Pinecone/Weaviate)

```python
# Product embeddings for semantic search
product_vector_schema = {
    "id": "product_id",
    "vector": [768], # Embedding dimensions
    "metadata": {
        "product_name": "string",
        "product_name_hi": "string",
        "description": "string",
        "category": "string",
        "price": "float",
        "tags": ["string"],
        "language": "string"
    }
}

# Customer query embeddings
query_vector_schema = {
    "id": "query_id",
    "vector": [768],
    "metadata": {
        "query_text": "string",
        "intent": "string",
        "language": "string",
        "timestamp": "datetime"
    }
}
```

## API Design

### 1. RESTful API Endpoints

#### Product Management APIs
```
POST   /api/products/auto-catalog          # Auto-catalog from image
GET    /api/products                       # List products with filters
GET    /api/products/{id}                  # Get product details
PUT    /api/products/{id}                  # Update product
DELETE /api/products/{id}                  # Delete product
GET    /api/products/search/semantic       # Semantic search
POST   /api/products/{id}/generate-description # AI description
```

#### Analytics APIs
```
POST   /api/analytics/query                # Natural language query
GET    /api/analytics/reports/{type}       # Generate reports
GET    /api/analytics/dashboard            # Dashboard data
POST   /api/analytics/insights             # Business insights
```

#### Customer Assistant APIs
```
POST   /api/assistant/chat                 # Chat with assistant
GET    /api/assistant/recommendations      # Product recommendations
POST   /api/assistant/feedback             # Feedback on responses
```

#### WhatsApp Integration APIs
```
POST   /api/whatsapp/webhook               # WhatsApp webhook
POST   /api/whatsapp/send-message          # Send WhatsApp message
GET    /api/whatsapp/status                # Message status
```

### 2. WebSocket APIs for Real-time Features

```javascript
// Real-time order updates
ws://api.retailgpt.com/ws/orders/{orderId}

// Live chat support
ws://api.retailgpt.com/ws/chat/{sessionId}

// Real-time analytics dashboard
ws://api.retailgpt.com/ws/analytics/{userId}
```

## Security Architecture

### 1. Authentication & Authorization

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/products/search/**").permitAll()
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
            .build();
    }
}
```

### 2. API Security

- **JWT Tokens:** Stateless authentication with refresh tokens
- **Rate Limiting:** Redis-based rate limiting for API endpoints
- **Input Validation:** Comprehensive validation for all inputs
- **SQL Injection Prevention:** Parameterized queries and ORM usage
- **CORS Configuration:** Proper CORS setup for frontend integration

### 3. Data Security

- **Encryption at Rest:** Database encryption for sensitive data
- **Encryption in Transit:** TLS 1.3 for all API communications
- **PII Protection:** Anonymization of customer data for AI training
- **Access Logging:** Comprehensive audit logs for all operations

## Deployment Architecture

### 1. AWS Cloud Infrastructure

```yaml
# Infrastructure as Code (Terraform/CloudFormation)
Resources:
  # Application Load Balancer
  ApplicationLoadBalancer:
    Type: AWS::ElasticLoadBalancingV2::LoadBalancer
    Properties:
      Type: application
      Scheme: internet-facing
      
  # ECS Cluster for microservices
  ECSCluster:
    Type: AWS::ECS::Cluster
    Properties:
      CapacityProviders:
        - FARGATE
        - FARGATE_SPOT
        
  # RDS MySQL Instance
  DatabaseInstance:
    Type: AWS::RDS::DBInstance
    Properties:
      Engine: mysql
      EngineVersion: 8.0
      MultiAZ: true
      
  # ElastiCache Redis
  RedisCluster:
    Type: AWS::ElastiCache::ReplicationGroup
    Properties:
      Engine: redis
      NumCacheClusters: 2
      
  # S3 Buckets
  ImageStorageBucket:
    Type: AWS::S3::Bucket
    Properties:
      PublicAccessBlockConfiguration:
        BlockPublicAcls: true
```

### 2. Container Orchestration

```dockerfile
# Spring Boot Microservice Dockerfile
FROM openjdk:21-jre-slim
COPY target/retailgpt-*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

```yaml
# Kubernetes Deployment
apiVersion: apps/v1
kind: Deployment
metadata:
  name: product-service
spec:
  replicas: 3
  selector:
    matchLabels:
      app: product-service
  template:
    metadata:
      labels:
        app: product-service
    spec:
      containers:
      - name: product-service
        image: retailgpt/product-service:latest
        ports:
        - containerPort: 8080
        env:
        - name: DB_HOST
          valueFrom:
            secretKeyRef:
              name: db-secret
              key: host
```

### 3. CI/CD Pipeline

```yaml
# GitHub Actions Workflow
name: Deploy RetailGPT
on:
  push:
    branches: [main]

jobs:
  test:
    runs-on: ubuntu-latest
    steps:
    - uses: actions/checkout@v3
    - name: Set up JDK 21
      uses: actions/setup-java@v3
      with:
        java-version: '21'
    - name: Run tests
      run: ./mvnw test
      
  build-and-deploy:
    needs: test
    runs-on: ubuntu-latest
    steps:
    - name: Build Docker image
      run: docker build -t retailgpt/app:${{ github.sha }} .
    - name: Deploy to ECS
      run: aws ecs update-service --cluster retailgpt --service app
```

## Performance Optimization

### 1. Caching Strategy

```java
@Service
public class ProductService {
    
    @Cacheable(value = "products", key = "#id")
    public ProductDTO getProduct(Long id) {
        return productRepository.findById(id);
    }
    
    @Cacheable(value = "search-results", key = "#query.hashCode()")
    public List<ProductDTO> semanticSearch(String query) {
        return vectorSearchService.search(query);
    }
}
```

### 2. Database Optimization

- **Indexing Strategy:** Composite indexes on frequently queried columns
- **Read Replicas:** Separate read replicas for analytics queries
- **Connection Pooling:** HikariCP with optimized pool settings
- **Query Optimization:** Lazy loading and pagination for large datasets

### 3. AI Service Optimization

- **Model Caching:** Cache frequently used AI model responses
- **Batch Processing:** Batch multiple requests to reduce API calls
- **Async Processing:** Non-blocking AI operations using CompletableFuture
- **Circuit Breaker:** Resilience patterns for external AI services

## Monitoring and Observability

### 1. Application Monitoring

```java
@Component
public class MetricsConfig {
    
    @Bean
    public MeterRegistry meterRegistry() {
        return new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);
    }
    
    @EventListener
    public void handleOrderCreated(OrderCreatedEvent event) {
        Metrics.counter("orders.created", "status", "success").increment();
    }
}
```

### 2. Logging Strategy

```yaml
# Logback configuration
logging:
  level:
    com.ecommerce.project: INFO
    org.springframework.security: DEBUG
  pattern:
    console: "%d{yyyy-MM-dd HH:mm:ss} - %msg%n"
    file: "%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n"
```

### 3. Health Checks

```java
@Component
public class CustomHealthIndicator implements HealthIndicator {
    
    @Override
    public Health health() {
        // Check database connectivity
        // Check AI service availability
        // Check external API status
        return Health.up()
            .withDetail("database", "UP")
            .withDetail("ai-service", "UP")
            .build();
    }
}
```

## Data Flow Diagrams

### 1. Auto-Cataloging Flow

```
Image Upload → Image Analysis (Bedrock) → Feature Extraction → 
Product Details Generation → Validation → Database Storage → 
Vector Embedding → Search Index Update
```

### 2. Conversational BI Flow

```
Natural Language Query → Intent Analysis → SQL Generation → 
Query Validation → Database Execution → Result Processing → 
Natural Language Response → User Interface
```

### 3. Customer Assistant Flow

```
Customer Query → Intent Recognition → Product Search (RAG) → 
Context Building → Response Generation → WhatsApp/Web Response → 
Feedback Collection → Model Improvement
```

## Integration Patterns

### 1. Event-Driven Architecture

```java
@EventListener
@Async
public class OrderEventHandler {
    
    public void handleOrderCreated(OrderCreatedEvent event) {
        // Update inventory
        // Send confirmation email
        // Update analytics
        // Trigger recommendations update
    }
}
```

### 2. Saga Pattern for Distributed Transactions

```java
@SagaOrchestrationStart
public class OrderProcessingSaga {
    
    @SagaOrchestrationStep
    public void reserveInventory(OrderCreatedEvent event) {
        // Reserve product inventory
    }
    
    @SagaOrchestrationStep
    public void processPayment(InventoryReservedEvent event) {
        // Process payment
    }
    
    @SagaOrchestrationStep
    public void confirmOrder(PaymentProcessedEvent event) {
        // Confirm order
    }
}
```

## Scalability Considerations

### 1. Horizontal Scaling

- **Microservices:** Independent scaling of services based on load
- **Database Sharding:** Partition data by tenant/region
- **CDN Integration:** CloudFront for static content delivery
- **Auto-scaling:** ECS/EKS auto-scaling based on metrics

### 2. Performance Targets

- **API Response Time:** < 200ms for 95th percentile
- **Image Processing:** < 30 seconds for auto-cataloging
- **Search Latency:** < 100ms for semantic search
- **Concurrent Users:** Support 10,000+ concurrent users

## Risk Mitigation

### 1. Technical Risks

- **AI Service Downtime:** Fallback to cached responses and manual processes
- **Database Failures:** Multi-AZ deployment with automated failover
- **API Rate Limits:** Circuit breakers and exponential backoff
- **Security Breaches:** WAF, DDoS protection, and incident response plan

### 2. Business Risks

- **Data Quality:** Human validation workflows for AI-generated content
- **User Adoption:** Progressive rollout and extensive user training
- **Cost Management:** Budget alerts and usage monitoring for AI services
- **Compliance:** Regular security audits and compliance checks

## Future Enhancements

### 1. Advanced AI Features

- **Predictive Analytics:** Demand forecasting using historical data
- **Dynamic Pricing:** AI-powered pricing optimization
- **Visual Search:** Image-based product search capabilities
- **Voice Commerce:** Voice-activated shopping experience

### 2. Platform Extensions

- **Multi-tenant Architecture:** Support multiple retailers
- **Mobile Apps:** Native iOS/Android applications
- **Social Commerce:** Integration with social media platforms
- **AR/VR Features:** Augmented reality product visualization

This comprehensive design document provides a complete blueprint for implementing RetailGPT, covering all aspects from architecture to deployment, ensuring scalability, security, and maintainability while meeting the specific needs of Indian small retailers.