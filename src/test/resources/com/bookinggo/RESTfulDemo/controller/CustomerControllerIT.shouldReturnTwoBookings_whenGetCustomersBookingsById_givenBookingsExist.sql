use restfuldemodb;
INSERT INTO customer (title, name )
VALUES
('Mr', 'Customer One'),
('Mr', 'Customer Two'),
('Mr', 'Customer Three'),
('Mr', 'Customer Four');

INSERT INTO tour (tour_package_code, title, duration, price )
VALUES
('LS','London City Sightseeing Tour','2 hours','45'),
('LF','Arsenal Football Tour','2 hours','50'),
('PS','Paris Sightseeing Tour','2 hours','40'),
('PF','Paris Fashion Experience','4 hours','65');

INSERT INTO tour_booking (tour_id, customer_id, pickup_date_time, pickup_location, participants, created_when)
VALUES (1, 1, CURRENT_TIMESTAMP + INTERVAL 1 WEEK, 'Hotel Intercontinental', 1, CURRENT_TIMESTAMP),
       (1, 4, CURRENT_TIMESTAMP + INTERVAL 1 WEEK, 'Hotel Intercontinental', 1, CURRENT_TIMESTAMP),
       (2, 1, CURRENT_TIMESTAMP + INTERVAL 1 WEEK, 'Hotel Intercontinental', 1, CURRENT_TIMESTAMP);