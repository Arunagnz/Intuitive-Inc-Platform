# Intuitive Inc. Platform

<p align="center">
   <a href="https://github.com/arunagnz/slam-book/blob/master/LICENSE">
      <img src="https://img.shields.io/badge/license-MIT-blue.svg" />
   </a>
   <a href="https://github.com/arunagnz/slam-book/actions/new">
      <img src="https://img.shields.io/badge/build-passing-brightgreen" />
   </a>
   <a href="https://github.com/arunagnz/slam-book/issues">
      <img src="https://img.shields.io/badge/coverage-100%25-brightgreen" />
   </a>
   <a href="https://github.com/arunagnz/slam-book/pulls">
      <img src="https://img.shields.io/badge/PRs-welcome-brightgreen.svg" />
   </a>
</p>

## Overview

The **Intuitive Inc. Platform** is a cloud-based platform that enables partners to sell subscriptions for home appliances such as TVs, refrigerators, and water purifiers. The platform allows partners to manage products, pricing, and promotions, while the system dynamically adjusts prices based on supply and demand. The solution is designed to be highly scalable, extensible, and performant, supporting various pricing strategies, including monthly, yearly, and volume-based pricing.

## Key Features

- **Partner Management**: Partners can create and define products, pricing models, and promotions.
- **Dynamic Pricing**: The platform adjusts pricing based on market demand to maximize commission while allowing partners to set minimum selling prices.
- **Custom Pricing Strategies**: Support for custom pricing models such as monthly/yearly subscriptions and volume-based pricing.
- **Scalability & High Availability**: Designed to handle large volumes of requests and scale horizontally in a cloud-native environment.
- **Cloud Infrastructure**: Hosted on AWS using Kubernetes (EKS), Docker containers, and AWS managed services like API Gateway, Lambda, and S3.
- **Microservices Architecture**: The platform consists of multiple microservices for product, pricing, and promotion management.

## Technology Stack

- **Backend**: Spring Boot, Java
- **Database**: PostgreSQL
- **Authentication**: JWT-based security
- **Infrastructure**: Docker, Kubernetes (EKS), AWS API Gateway, AWS Lambda, AWS S3
- **Monitoring**: Prometheus, Grafana

## Setup Instructions

### Prerequisites

- Java 17+
- Docker
- Kubernetes (EKS) setup with kubectl
- AWS CLI for managing AWS resources

### Local Development

1. **Clone the repository**:
   ```bash
   git clone https://github.com/Arunagnz/Intuitive-Inc-Platform.git
   cd Intuitive-Inc-Platform
   ```

2. **Set up environment variables**:
   Create an `.env` file to store the environment variables for local development:
   ```bash
   ACCESS_KEY=<your-access-key>
   SECRET_KEY=<your-secret-key>
   DATABASE_URL=<your-database-url>
   ```

3. **Build the services**:
   Run the following command to build the services using Maven:
   ```bash
   mvn clean install
   ```

4. **Running locally**:
   Start the services using Docker Compose:
   ```bash
   docker-compose up --build
   ```

### Deployment

The platform can be deployed to AWS EKS. The services are containerized using Docker and managed with Kubernetes Helm charts for scaling and configuration.

1. **Build Docker images**:
   ```bash
   docker build -t <your-docker-image>:latest .
   ```

2. **Push to AWS ECR**:
   Ensure your AWS CLI is configured, and push the Docker image to your ECR repository:
   ```bash
   aws ecr get-login-password --region <region> | docker login --username AWS --password-stdin <aws_account_id>.dkr.ecr.<region>.amazonaws.com
   docker tag <your-docker-image>:latest <aws_account_id>.dkr.ecr.<region>.amazonaws.com/<your-repo>:latest
   docker push <aws_account_id>.dkr.ecr.<region>.amazonaws.com/<your-repo>:latest
   ```

3. **Deploy to EKS**:
   Use Helm charts to deploy the application to EKS:
   ```bash
   helm install intuitive-inc ./helm-chart
   ```

### Testing

Run unit tests:
```bash
mvn test
```

## Usage

- **API Gateway**: The API Gateway is the entry point for all external requests. Partners can create products, define pricing models, and manage promotions through the API endpoints.
- **Dynamic Pricing**: The system dynamically adjusts product prices based on market trends to maximize partner revenue.

## Roadmap

- **Phase 1: Core Product & Pricing System** (Completed)
- **Phase 2: Partner Onboarding & Promotion Management**
- **Phase 3: Advanced Pricing Strategies**
- **Phase 4: Machine Learning-based Pricing Adjustments**

## Contributing

Contributions are welcome! Please follow the standard GitHub flow:

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/new-feature`)
3. Commit your changes (`git commit -am 'Add some feature'`)
4. Push to the branch (`git push origin feature/new-feature`)
5. Create a new Pull Request

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
