-- product table
CREATE TABLE Product (
    id SERIAL PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    price NUMERIC(10,2) NOT NULL,
    creation_datetime TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- product_category table
CREATE TABLE Product_category (
    id SERIAL PRIMARY KEY ,
    name VARCHAR(200) NOT NULL,
    product_id INT NOT NULL,
    CONSTRAINT foreign_key_product
        FOREIGN KEY (product_id)
        REFERENCES product(id)
        ON DELETE CASCADE
);