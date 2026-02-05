# Marketing Service with LangChain4j and AWS Bedrock

This service provides AI-powered marketing description generation for products using LangChain4j with AWS Bedrock.

## Features

- Generate marketing descriptions for products using AI
- Support for basic product name input
- Enhanced descriptions with category and price context
- Fallback descriptions in case of service failures
- RESTful API endpoints for easy integration

## Setup

### 1. AWS Configuration

Ensure you have AWS credentials configured. You can use any of these methods:

- AWS CLI: `aws configure`
- Environment variables: `AWS_ACCESS_KEY_ID` and `AWS_SECRET_ACCESS_KEY`
- IAM roles (if running on EC2)
- AWS credentials file

### 2. Required AWS Permissions

Your AWS user/role needs the following permissions:

```json
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Effect": "Allow",
            "Action": [
                "bedrock:InvokeModel"
            ],
            "Resource": "arn:aws:bedrock:*:*:foundation-model/*"
        }
    ]
}
```

### 3. Application Configuration

The following properties can be configured in `application.properties`:

```properties
# AWS Bedrock Configuration
aws.bedrock.region=us-east-1
aws.bedrock.model=anthropic.claude-3-sonnet-20240229-v1:0
aws.bedrock.temperature=0.7
aws.bedrock.max-tokens=500
```

### 4. Available Models

You can use different Bedrock models by changing the `aws.bedrock.model` property:

- `anthropic.claude-3-sonnet-20240229-v1:0` (default)
- `anthropic.claude-3-haiku-20240307-v1:0`
- `amazon.titan-text-express-v1`
- `ai21.j2-ultra-v1`

## Usage

### Service Methods

```java
@Autowired
private MarketingService marketingService;

// Basic usage
String description = marketingService.generateMarketingDescription("Wireless Headphones");

// With context
String detailedDescription = marketingService.generateMarketingDescription(
    "Gaming Laptop", 
    "Electronics", 
    1299.99
);
```

### REST API Endpoints

#### Generate Basic Marketing Description
```
GET /api/marketing/description?productName=Wireless Headphones
```

#### Generate Detailed Marketing Description
```
GET /api/marketing/description/detailed?productName=Gaming Laptop&category=Electronics&price=1299.99
```

## Dependencies Added

The following dependencies were added to `pom.xml`:

```xml
<!-- LangChain4j dependencies -->
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j</artifactId>
    <version>0.35.0</version>
</dependency>
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-bedrock</artifactId>
    <version>0.35.0</version>
</dependency>
<dependency>
    <groupId>software.amazon.awssdk</groupId>
    <artifactId>bedrock-runtime</artifactId>
    <version>2.29.15</version>
</dependency>
```

## Error Handling

The service includes fallback descriptions in case the AI service is unavailable, ensuring your application continues to function even if AWS Bedrock is temporarily inaccessible.

## Testing

Run the tests with:
```bash
mvn test
```

Note: The tests use mocked dependencies to avoid actual AWS API calls during testing.

## Cost Considerations

AWS Bedrock charges per token. Monitor your usage through the AWS console and consider implementing caching for frequently requested products to reduce costs.