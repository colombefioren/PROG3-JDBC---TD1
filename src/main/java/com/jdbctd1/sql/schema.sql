-- product table
CREATE TABLE Product (
    id serial primary key,
    name varchar(200) not null,
    price numeric(0,2) not null,
    creation_datetime timestamp default current_timestamp
);

-- product_category table
CREATE TABLE Product_category (
    id serial primary key,
    name varchar(200) not null,
    product_id int unique not null,
    constraint  foreign_key_product
        foreign key (product_id)
        references product(id)
)