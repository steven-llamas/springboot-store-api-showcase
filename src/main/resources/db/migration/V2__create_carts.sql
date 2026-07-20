CREATE TABLE IF NOT EXISTS `store_api`.`carts` (
  `id`          BINARY(16) DEFAULT (uuid_to_bin(uuid())) NOT NULL,
  `dateCreated` DATE DEFAULT (curdate()) NOT NULL,

  PRIMARY KEY (`id`),
  INDEX `idx_cart_date_created` (`dateCreated` ASC) VISIBLE);