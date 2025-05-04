CREATE TABLE carts
(
    id UUID DEFAULT gen_random_uuid () UNIQUE NOT NULL ,
    date_created DATE DEFAULT(CURRENT_DATE) NOT NULL
);
CREATE TABLE cart_items
(
    id      BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    cart_id  UUID NOT NULL,
    product_id BIGINT NOT NULL,
    quantity  INT DEFAULT 1 NOT NULL,
    CONSTRAINT cart_items_cart_id_fk
        FOREIGN KEY (cart_id) REFERENCES carts (id) ON DELETE CASCADE,
    CONSTRAINT cart_items_product_id_fk
        FOREIGN KEY (product_id) REFERENCES products (id) ON DELETE CASCADE,
    CONSTRAINT cart_product_unique
        UNIQUE (product_id , cart_id)
);