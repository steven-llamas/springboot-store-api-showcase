ALTER TABLE `store_api`.`cart_items`
DROP INDEX `cartId_UNIQUE`,
ADD UNIQUE KEY `cart_id_product_id_UNIQUE` (`cart_id`, `product_id`);