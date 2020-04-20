CREATE DATABASE IF NOT EXISTS restfuldemodb;

CREATE TABLE `tour_booking` (
    `id`                    BIGINT AUTO_INCREMENT,
    `tour_id`               BIGINT,
    `customer_id`           BIGINT,
    `pickup_date`           VARCHAR(32),
    `pickup_location`       VARCHAR(100),
    `num_of_participants`   BIGINT,
    `createdWhen`           DATETIME             DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    FOREIGN KEY (tour_id) REFERENCES tour(id)
);