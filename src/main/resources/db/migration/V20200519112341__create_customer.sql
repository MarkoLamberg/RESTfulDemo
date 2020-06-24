    CREATE DATABASE IF NOT EXISTS restfuldemodb;

    CREATE TABLE `customer` (
      `id`                  BIGINT AUTO_INCREMENT,
      `title`               VARCHAR(5)    NOT NULL,
      `name`                VARCHAR(50)   NOT NULL,
      PRIMARY KEY (id)
    );