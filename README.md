# RetailGPT - AI-Powered eCommerce Platform 🚀

> **An intelligent eCommerce solution built with Spring Boot, React.js, and AWS Bedrock for Indian small retailers**

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.1-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![React](https://img.shields.io/badge/React-18.x-blue.svg)](https://reactjs.org/)
[![AWS Bedrock](https://img.shields.io/badge/AWS-Bedrock-yellow.svg)](https://aws.amazon.com/bedrock/)
[![LangChain4j](https://img.shields.io/badge/LangChain4j-0.35.0-purple.svg)](https://github.com/langchain4j/langchain4j)

## 🌟 Project Overview

RetailGPT is a GenAI-enhanced eCommerce platform designed specifically for small retailers in India. It combines traditional eCommerce functionality with cutting-edge AI capabilities to automate store management, provide intelligent business insights, and enhance customer experience.

### 🎯 Key Features

- **🤖 Auto-Cataloging**: AI-powered product creation from images using AWS Bedrock
- **📊 Conversational BI**: Natural language business analytics in Hindi/English
- **💬 Smart Buyer Assistant**: WhatsApp-integrated customer support with RAG
- **🔍 Semantic Search**: Vector-based product discovery
- **� Multi-language Support**: Hindi and English interface
- **📱 Mobile-First Design**: Responsive PWA for all devices
- **🔐 Enterprise Security**: JWT authentication with role-based access

## 🏗️ Architecture

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
    │         (LangChain4j + AWS Bedrock)          │
    └───────────────────────┬───────────────────────┘
                            │
    ┌───────────────────────▼───────────────────────┐
    │              AWS Bedrock                      │
    │    (Claude 3, Titan, Stable Diffusion)       │
    └───────────────────────────────────────────────┘

┌─────────────┐  ┌─────────────┐  ┌─────────────┐
│   MySQL     │  │  Vector DB  │  │   Redis     │
│(Transactional)│  │(Semantic   │  │  (Cache)    │
│             │  │ Search)     │  │             │
└─────────────┘  └─────────────┘  └─────────────┘
```

## 🚀 Quick Start

### Prerequisites

- **Java 21+**
- **Node.js 18+**
- **MySQL 8.0+**
- **Redis 6.0+**
- **AWS Account** with Bedrock access
- **Maven 3.8+**

### 1. Clone the Repository

```bash
git clone https://github.com/Nikitha954/retailgpt-ecommerce.git
cd retailgpt-ecommerce
```

### 2. Backend Setup (Spring Boot)

```bash
cd sb-ecom

# Configure database
cp src/main/resources/application.properties.example src/main/resources/application.properties
# Edit application.properties with your database credentials

# Install dependencies and run
./mvnw clean install
./mvnw spring-boot:run
```

### 3. Frontend Setup (React)

```bash
cd ecom-frontend

# Install dependencies
npm install

# Start development server
npm run dev
```

### 4. AWS Bedrock Configuration

```bash
# Configure AWS credentials
aws configure

# Update application.properties
aws.bedrock.region=us-east-1
aws.bedrock.model=anthropic.claude-3-sonnet-20240229-v1:0
```

## 📁 Project Structure

```
retailgpt-ecommerce/
├── 📄 README.md                    # This file
├── 📄 Requirements.md              # Detailed requirements
├── 📄 Design.md                   # System architecture
├── 🗂️ sb-ecom/                    # Spring Boot Backend
│   ├── 🗂️ src/main/java/com/ecommerce/project/
│   │   ├── 🗂️ controller/         # REST Controllers
│   │   ├── 🗂️ service/           # Business Logic
│   │   ├── 🗂️ model/             # JPA Entities
│   │   ├── 🗂️ config/            # Configuration
│   │   └── 🗂️ security/          # Security & JWT
│   ├── 📄 pom.xml                # Maven dependencies
│   └── 📄 MARKETING_SERVICE_README.md
├── 🗂️ ecom-frontend/              # React Frontend
│   ├── 🗂️ src/
│   │   ├── 🗂️ components/        # React Components
│   │   ├── 🗂️ store/            # Redux Store
│   │   └── 🗂️ api/              # API Integration
│   └── 📄 package.json
├── 🗂️ FirstSpring/               # Spring Boot Examples
├── 🗂️ SpringExample/             # Spring Core Examples
└── 🗂️ media/                     # Social Media Project
```

## 🔧 Configuration

### Database Configuration

```properties
# MySQL Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/retailgpt
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
```

### AWS Bedrock Configuration

```properties
# AWS Bedrock Settings
aws.bedrock.region=us-east-1
aws.bedrock.model=anthropic.claude-3-sonnet-20240229-v1:0
aws.bedrock.temperature=0.7
aws.bedrock.max-tokens=500
```

### Security Configuration

```properties
# JWT Configuration
spring.app.jwtSecret=your-secret-key
spring.app.jwtExpirationMs=86400000
```

## 🔌 API Endpoints

### Product Management
```http
POST   /api/products/auto-catalog          # AI-powered product creation
GET    /api/products                       # List products
GET    /api/products/search/semantic       # Semantic search
POST   /api/products/{id}/generate-description # AI marketing copy
```

### Marketing AI Service
```http
GET    /api/marketing/description?productName=Wireless Headphones
GET    /api/marketing/description/detailed?productName=Laptop&category=Electronics&price=50000
```

### Analytics & BI
```http
POST   /api/analytics/query                # Natural language queries
GET    /api/analytics/reports/{type}       # Business reports
```

### Customer Assistant
```http
POST   /api/assistant/chat                 # Chat with AI assistant
GET    /api/assistant/recommendations      # Product recommendations
```

## 🤖 AI Features

### 1. Auto-Cataloging Service

Upload product images and get AI-generated:
- Product names (Hindi/English)
- Detailed descriptions
- Category suggestions
- Price estimates

```java
@PostMapping("/auto-catalog")
public ResponseEntity<ProductDTO> createProductFromImage(
    @RequestParam("image") MultipartFile image,
    @RequestParam(defaultValue = "en") String language
) {
    // AI processes image and generates product details
}
```

### 2. Conversational Business Intelligence

Ask business questions in natural language:
- "Which products sold most last week?"
- "कल कितनी बिक्री हुई?" (How much sales yesterday?)
- "Do I need to restock rice?"

### 3. Smart Customer Assistant

WhatsApp-integrated customer support:
- Product recommendations
- Order assistance
- Query resolution in Hindi/English

## 🛠️ Development

### Running Tests

```bash
# Backend tests
cd sb-ecom
./mvnw test

# Frontend tests
cd ecom-frontend
npm test
```

### Building for Production

```bash
# Backend
./mvnw clean package -DskipTests

# Frontend
npm run build
```

### Docker Deployment

```bash
# Build Docker image
docker build -t retailgpt/backend:latest ./sb-ecom

# Run with Docker Compose
docker-compose up -d
```

## 📊 Features Demo

### Auto-Cataloging Flow
1. Upload product image
2. AI analyzes image using AWS Bedrock
3. Generates product details automatically
4. Admin reviews and publishes

### Conversational BI Example
```
User: "Show me top selling products this month"
AI: Analyzing sales data...
Response: "Top 3 products: 1) Basmati Rice (₹45,000), 2) Cotton Sarees (₹32,000), 3) Spice Mix (₹28,000)"
```

### Customer Assistant Example
```
Customer: "Do you have something for wedding under ₹2000?"
Assistant: "Yes! I found 3 perfect options for weddings under ₹2000:
1. Silk Saree - ₹1,800
2. Gold-plated Jewelry Set - ₹1,500  
3. Wedding Decoration Items - ₹1,200"
```

## 🔐 Security Features

- **JWT Authentication** with refresh tokens
- **Role-based Access Control** (Admin/Customer)
- **API Rate Limiting** with Redis
- **Input Validation** and sanitization
- **SQL Injection Prevention**
- **CORS Configuration**
- **Data Encryption** at rest and in transit

## 📈 Performance Optimization

- **Redis Caching** for frequently accessed data
- **Database Indexing** for optimized queries
- **Lazy Loading** for large datasets
- **Connection Pooling** with HikariCP
- **CDN Integration** for static assets
- **Async Processing** for AI operations

## 🌐 Deployment

### AWS Deployment
- **ECS/EKS** for container orchestration
- **RDS MySQL** for database
- **ElastiCache Redis** for caching
- **S3** for file storage
- **CloudFront** for CDN
- **Application Load Balancer**

### CI/CD Pipeline
```yaml
# GitHub Actions workflow included
- Automated testing
- Docker image building
- AWS deployment
- Environment promotion
```

## 🤝 Contributing

1. Fork the repository
2. Create feature branch (`git checkout -b feature/amazing-feature`)
3. Commit changes (`git commit -m 'Add amazing feature'`)
4. Push to branch (`git push origin feature/amazing-feature`)
5. Open Pull Request

### Development Guidelines
- Follow Java coding standards
- Write comprehensive tests
- Update documentation
- Use conventional commit messages

## 📝 Documentation

- **[Requirements.md](Requirements.md)** - Detailed project requirements
- **[Design.md](Design.md)** - Complete system architecture
- **[Marketing Service README](sb-ecom/MARKETING_SERVICE_README.md)** - AI service setup

## 🐛 Troubleshooting

### Common Issues

**Database Connection Issues:**
```bash
# Check MySQL service
sudo systemctl status mysql

# Verify credentials in application.properties
```

**AWS Bedrock Access:**
```bash
# Verify AWS credentials
aws sts get-caller-identity

# Check Bedrock permissions
aws bedrock list-foundation-models
```

**Frontend Build Issues:**
```bash
# Clear node modules and reinstall
rm -rf node_modules package-lock.json
npm install
```

## 📊 Monitoring & Analytics

- **Application Metrics** with Micrometer
- **Health Checks** for all services
- **Logging** with structured JSON format
- **Performance Monitoring** with custom dashboards
- **Error Tracking** and alerting

## 🔮 Future Roadmap

- [ ] **Multi-tenant Architecture** for multiple retailers
- [ ] **Mobile Apps** (iOS/Android)
- [ ] **Voice Commerce** integration
- [ ] **AR/VR Product Visualization**
- [ ] **Blockchain Supply Chain** tracking
- [ ] **Advanced Analytics** with ML predictions
- [ ] **Social Commerce** integration
- [ ] **Marketplace Features**

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👥 Team

- **Developer:** Nikitha
- **Architecture:** RetailGPT Team
- **AI Integration:** LangChain4j + AWS Bedrock

## 📞 Support

- **Email:** support@retailgpt.com
- **Documentation:** [Wiki](https://github.com/Nikitha954/retailgpt-ecommerce/wiki)
- **Issues:** [GitHub Issues](https://github.com/Nikitha954/retailgpt-ecommerce/issues)

## 🙏 Acknowledgments

- **Spring Boot Team** for the excellent framework
- **LangChain4j** for AI integration capabilities
- **AWS Bedrock** for powerful AI models
- **React Community** for frontend tools
- **Open Source Contributors**

---

**⭐ Star this repository if you find it helpful!**

**🔗 [Live Demo](https://retailgpt-demo.com) | [Documentation](https://docs.retailgpt.com) | [API Docs](https://api.retailgpt.com/swagger-ui.html)**


