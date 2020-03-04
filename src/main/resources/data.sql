
insert into tour_package (code, name) values
('BC', 'Backpack Cal'),
('CC', 'California Calm'),
('CH', 'California Hot springs'),
('CY', 'Cycle California'),
('DS', 'From Desert to Sea'),
('KC', 'Kids California'),
('NW', 'Nature Watch'),
('SC', 'Snowboard Cali'),
('TC', 'Taste of California');

insert into tour  (tour_package_code, title, duration, price, region) values
  (
    'BC',
    'Big Sur Retreat',
    '3 days',
    '750',
    'North'
  );
  insert into tour  (tour_package_code, title, duration, price, region) values

  (
   'BC',
    'In the Steps of John Muir',
    '3 days',
    '600',
    'West'
  );
  insert into tour  (tour_package_code, title, duration, price, region) values

  (
   'BC',
    'The Death Valley Survivor''s Trek',
    '2 days',
    '250',
    'East'
  );
  insert into tour  (tour_package_code, title, duration, price, region) values

  (
   'BC',
    'The Mt. Whitney Climbers Tour',
    '4 days',
    '650',
    'North'
  ),
  (
   'BC',
    'Channel Islands Excursion',
    '1 day',
    '150',
    'South'
  );

  insert into tour_rating (tour_id, customer_id, score, comment) values
  (1, 4, 5, 'I loved it'),
  (2, 100, 5, 'I really thought it could have been better');

