-- Create Partner Table
CREATE TABLE partners (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255),
    address VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

ALTER TABLE products ADD COLUMN partner_id BIGINT;
ALTER TABLE pricing ADD COLUMN partner_id BIGINT;

ALTER TABLE products ADD CONSTRAINT fk_partner_product FOREIGN KEY (partner_id) REFERENCES partners(id);
ALTER TABLE pricing ADD CONSTRAINT fk_partner_pricing FOREIGN KEY (partner_id) REFERENCES partners(id);
