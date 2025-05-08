CREATE TABLE orders (
    id      SERIAL  PRIMARY KEY NOT NULL,
    customer_id BIGINT NOT NULL,
    status  VARCHAR(16) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    total_price DECIMAL(10,2) NOT NULL,
    CONSTRAINT orders_users_id_fk FOREIGN KEY (customer_id) REFERENCES users(id)
) ;

CREATE TABLE orders_items (
    id       SERIAL  PRIMARY KEY NOT NULL,
    order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    unit_price DECIMAL(10,2) NOT NULL,
    quantity INT NOT NULL,
    total_price DECIMAL(10,2) NOT NULL,
    CONSTRAINT orders_items_orders_id_fk  FOREIGN KEY (order_id) REFERENCES orders (id),
    CONSTRAINT orders_items_product_id_fk FOREIGN KEY (product_id) REFERENCES products (id)
)