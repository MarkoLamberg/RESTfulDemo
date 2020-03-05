
CREATE TABLE tour_package(
  code CHAR(2) NOT NULL UNIQUE,
  name VARCHAR(50) NOT NULL,
  location VARCHAR(20) NOT NULL
);

CREATE TABLE tour (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  tour_package_code CHAR(2) NOT NULL,
  title VARCHAR(100) NOT NULL,
  price VARCHAR(10) not null,
  duration VARCHAR(32) NOT NULL
);
ALTER TABLE tour ADD FOREIGN KEY (tour_package_code) REFERENCES tour_package(code);


CREATE TABLE tour_booking (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    tour_id BIGINT,
    customer_id BIGINT,
    pickup_date VARCHAR(32),
    pickup_location VARCHAR(100),
    num_of_partisipants BIGINT
);
ALTER TABLE tour_booking ADD FOREIGN KEY (tour_id) REFERENCES tour(id);
ALTER TABLE tour_booking ADD UNIQUE MyConstraint (tour_id, customer_id);
