
insert into tour_package (code, name, location ) values
('LS', 'London Sightseeing', 'London'),
('LF', 'London Football', 'London'),
('PS', 'Paris Sightseeing', 'Paris'),
('PF', 'Paris Fashion', 'Paris'),
('RS', 'Rome Sightseeing', 'Rome'),
('RH', 'Rome History', 'Rome'),
('BS', 'Barcelona Sightseeing', 'Barcelona'),
('BW', 'Barcelona Wine Tasting', 'Barcelona');

  insert into tour  (tour_package_code, title, duration, price) values
  (
    'LS',
    'London City Sightseeing Tour',
    '2 hours',
    '45'
  );
  insert into tour  (tour_package_code, title, duration, price) values
  (
   'LF',
    'Arsenal Football Tour',
    '2 hours',
    '50'
  );
  insert into tour  (tour_package_code, title, duration, price) values
  (
   'PS',
    'Paris Sightseeing Tour',
    '2 hours',
    '40'
  );
  insert into tour  (tour_package_code, title, duration, price) values
  (
   'PF',
    'Paris Fashion Experience',
    '4 hours',
    '65'
  );
  insert into tour  (tour_package_code, title, duration, price) values
  (
   'RS',
    'Rome Sightseeing Tour',
    '2 hour',
    '35'
  );
  insert into tour  (tour_package_code, title, duration, price) values
    (
   'RH',
    'Roman History Excursion',
    '3 hour',
    '45'
  );
  insert into tour  (tour_package_code, title, duration, price) values
  (
   'BS',
    'Barcelona Sightseeing Tour',
    '2 hour',
    '25'
  );
  insert into tour  (tour_package_code, title, duration, price) values
  (
   'BW',
    'Spanish Wine Excursion',
    '4 hour',
    '75'
  );
  insert into tour_booking (tour_id, customer_id, pickup_date, pickup_location) values
  (1, 4, '20-03-2020', 'Hotel Intercontinental');
  insert into tour_booking (tour_id, customer_id, pickup_date, pickup_location) values
  (1, 5, '21-03-2020', 'Hotel Intercontinental');
  insert into tour_booking (tour_id, customer_id, pickup_date, pickup_location) values
  (2, 6, '22-03-2020', 'Hotel Ibis');
  insert into tour_booking (tour_id, customer_id, pickup_date, pickup_location) values
  (2, 7, '22-03-2020', 'Hotel President');

