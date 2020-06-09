use restfuldemodb;
INSERT INTO customer (title, name )
VALUES
('mr', 'Customer One'),
('mr', 'Customer Two'),
('mr', 'Customer Three'),
('mr', 'Customer Four'),
('mr', 'Customer Five');

INSERT INTO tour_booking (tour_id, customer_id, pickup_date, pickup_location, num_of_participants)
VALUES (1, 4, '20-03-2020', 'Hotel Intercontinental', 1),
       (2, 5, '20-03-2020', 'Hotel Ibis', 2);