-- Create Product Table
CREATE TABLE products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    sku VARCHAR(100) NOT NULL,
    base_price DECIMAL(10, 2) NOT NULL,
    category VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
);

-- Create Pricing Table
CREATE TABLE pricing (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    base_price DECIMAL(10, 2) NOT NULL,
    discount DECIMAL(5, 2),
    pricing_strategy VARCHAR(50) NOT NULL,
    product_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_product_pricing FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE
);

-- Optional: Insert ENUM values into PricingStrategy (if necessary in the database)
-- CREATE TYPE pricing_strategy AS ENUM ('FIXED', 'DISCOUNTED', 'MONTHLY', 'YEARLY', 'VOLUME_BASED', 'DYNAMIC');