use restfuldemodb;
use restfuldemodb;
INSERT INTO customer (title, name )
VALUES
('mr', 'Customer One'),
('mr', 'Customer Two'),
('mr', 'Customer Three'),
('mr', 'Customer Four'),
('mr', 'Customer Five');

INSERT INTO tour_booking (tour_id, customer_id, pickup_date_time, pickup_location, num_of_participants, created_when)
VALUES (1,4, '2020-03-22T14:00:00', 'Hotel Intercontinental',1, '2020-01-20T12:00:00'),
       (2,4, '2020-03-23T14:00:00', 'Hotel Intercontinental', 1, '2020-01-20T12:00:00'),
       (2,5, '2020-03-22T14:00:00', 'Hotel Paris',2, '2020-01-20T12:00:00');