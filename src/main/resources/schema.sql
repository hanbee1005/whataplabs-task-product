DROP TABLE IF EXISTS PRODUCT;

-- CREATE PRODUCT TABLE
CREATE TABLE PRODUCT (
    product_id bigint AUTO_INCREMENT NOT NULL,
    name varchar(255) NOT NULL,
    price numeric(19, 2) NOT NULL,
    amount integer NOT NULL,
    created_at timestamp default now(),
    last_modified_at timestamp,
    primary key (product_id)
);