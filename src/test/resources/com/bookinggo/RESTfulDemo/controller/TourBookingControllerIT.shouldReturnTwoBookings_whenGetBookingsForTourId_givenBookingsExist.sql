use restfuldemodb;
INSERT INTO customer (title, name )
VALUES
('Mr', 'Customer One'),
('Mr', 'Customer Two'),
('Mr', 'Customer Three'),
('Mr', 'Customer Four');

INSERT INTO tour_booking (tour_id, customer_id, pickup_date_time, pickup_location, num_of_participants, created_when)
VALUES (1, 4, CURRENT_TIMESTAMP + INTERVAL 1 WEEK, 'Hotel Intercontinental', 1, CURRENT_TIMESTAMP),
       (1, 2, CURRENT_TIMESTAMP + INTERVAL 1 WEEK, 'Hotel Intercontinental', 1, CURRENT_TIMESTAMP),
       (2, 4, CURRENT_TIMESTAMP + INTERVAL 1 WEEK, 'Hotel Intercontinental', 1, CURRENT_TIMESTAMP);
