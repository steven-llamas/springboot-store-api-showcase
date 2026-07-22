SELECT IF(
    (SELECT COUNT(*) FROM information_schema.COLUMNS
     WHERE TABLE_SCHEMA = 'store_api'
     AND TABLE_NAME = 'carts'
     AND COLUMN_NAME = 'dateCreated') > 0,
    'ALTER TABLE `store_api`.`carts` RENAME COLUMN `dateCreated` TO `date_created`',
    'SELECT 1'
) INTO @sql;
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;


SELECT IF(
    (SELECT COUNT(*) FROM information_schema.COLUMNS
     WHERE TABLE_SCHEMA = 'store_api'
     AND TABLE_NAME = 'cart_items'
     AND COLUMN_NAME = 'cartId') > 0,
    'ALTER TABLE `store_api`.`cart_items` RENAME COLUMN `cartId` TO `cart_id`',
    'SELECT 1'
) INTO @sql;
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;


SELECT IF(
    (SELECT COUNT(*) FROM information_schema.COLUMNS
     WHERE TABLE_SCHEMA = 'store_api'
     AND TABLE_NAME = 'cart_items'
     AND COLUMN_NAME = 'productId') > 0,
    'ALTER TABLE `store_api`.`cart_items` RENAME COLUMN `productId` TO `product_id`',
    'SELECT 1'
) INTO @sql;
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;