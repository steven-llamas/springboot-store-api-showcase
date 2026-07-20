CREATE TABLE IF NOT EXISTS `store_api`.`cart_items` (
  `id`          bigint NOT NULL AUTO_INCREMENT,
  `cartId`      binary(16) NOT NULL,
  `productId`   bigint unsigned DEFAULT NULL,
  `quantity`    int unsigned DEFAULT 1 NOT NULL,

  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `cartId_UNIQUE` (`cartId`),
  KEY `fk_product_id_product_idx` (`productId`),
  CONSTRAINT `fk_cart_item_cartid_cart` FOREIGN KEY (`cartId`) REFERENCES `store_api`.`carts` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_cart_item_productid_product` FOREIGN KEY (`productId`) REFERENCES `product` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;