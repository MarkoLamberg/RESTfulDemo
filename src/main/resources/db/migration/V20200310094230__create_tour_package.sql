CREATE DATABASE IF NOT EXISTS restfuldemodb;

CREATE TABLE `tour_package`
(
  `code`                CHAR(2)         NOT NULL,
  `name`                VARCHAR(50)     NOT NULL,
  `location`            VARCHAR(20)     NOT NULL,
  UNIQUE (code)
);