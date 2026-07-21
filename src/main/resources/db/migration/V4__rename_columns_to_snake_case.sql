ALTER TABLE `store_api`.`carts`
  RENAME COLUMN `dateCreated` TO `date_created`;

ALTER TABLE `store_api`.`cart_items`
  RENAME COLUMN `cartId` TO `cart_id`,
  RENAME COLUMN `productId` TO `product_id`;