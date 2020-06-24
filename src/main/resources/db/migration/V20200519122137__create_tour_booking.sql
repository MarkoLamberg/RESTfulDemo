CREATE DATABASE IF NOT EXISTS restfuldemodb;

CREATE TABLE `tour_booking` (
    `id`                    BIGINT AUTO_INCREMENT,
    `tour_id`               BIGINT,
    `customer_id`           BIGINT,
    `pickup_date_time`      TIMESTAMP,
    `pickup_location`       VARCHAR(100),
    `participants`          BIGINT,
    `created_when`          TIMESTAMP   DEFAULT CURRENT_TIMESTAMP,
    `modified_when`         TIMESTAMP NULL DEFAULT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (tour_id)   REFERENCES tour(id),
    FOREIGN KEY (customer_id) REFERENCES customer(id)
);