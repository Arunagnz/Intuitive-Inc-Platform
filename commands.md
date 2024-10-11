**For Development purpose only**

## To start postgres instance

```powershell
docker run --name postgres-db -e POSTGRES_PASSWORD=password -d -p 5432:5432 postgres
```

## To start prometheus instance

```powershell
docker run --name prometheus -p 9090:9090 -v C:\Users\aruna\Documents\Intuitve-inc-platform\prometheus.yml:/etc/prometheus/prometheus.yml prom/prometheus -d
```

## To build service images

### Inside product-service directory

```powershell
docker build -t intuitive-inc/product-service:latest .
```

### Inside pricing-service directory

```powershell
docker build -t intuitive-inc/pricing-service:latest .
```

## To push to AWS ECR

```powershell
aws ecr create-repository --repository-name intuitive-inc/product-service

aws ecr create-repository --repository-name intuitive-inc/pricing-service
```

## To authenticate Docker to AWS ECR:

```shell
aws ecr get-login-password --region <your-region> | docker login --username AWS --password-stdin <aws_account_id>.dkr.ecr.<region>.amazonaws.com
```

### Tag and push the images:

```shell
docker tag intuitive-inc/product-service:latest <aws_account_id>.dkr.ecr.<region>.amazonaws.com/intuitive-inc/product-service:latest

docker push <aws_account_id>.dkr.ecr.<region>.amazonaws.com/intuitive-inc/product-service:latest
```

**_Repeat for all other services also_**

## To Deploy helm charts on EKS

**_Once the Helm charts are ready, use kubectl and Helm to deploy the services._**

#### Ensure your EKS cluster is set up and your kubectl is configured to use it. Then, deploy the services:

```bash
helm install product-service ./helm/product-service

helm install pricing-service ./helm/pricing-service
```
