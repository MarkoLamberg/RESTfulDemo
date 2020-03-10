    CREATE DATABASE IF NOT EXISTS restfuldemodb;

    CREATE TABLE `tour` (
      `id`                  BIGINT AUTO_INCREMENT,
      `tour_package_code`   CHAR(2)         NOT NULL,
      `title`               VARCHAR(100)    NOT NULL,
      `price`               VARCHAR(10)     NOT NULL,
      `duration`            VARCHAR(32)     NOT NULL,
      PRIMARY KEY (`id`)
    );
    ALTER TABLE tour ADD FOREIGN KEY (tour_package_code) REFERENCES tour_package(code);