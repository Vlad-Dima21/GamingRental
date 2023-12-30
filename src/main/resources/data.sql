
-- init devices
insert into device_base(device_base_id, device_base_name, device_base_producer, device_base_year_of_release)
    values(1, 'PS5 Slim', 'Sony', 2018);
insert into device_base(device_base_id, device_base_name, device_base_producer, device_base_year_of_release)
    values(2, 'Xbox Series S', 'Microsoft', 2019);
insert into device_base(device_base_id, device_base_name, device_base_producer, device_base_year_of_release)
    values(3, 'Xbox Series X', 'Microsoft', 2014);
insert into device_base(device_base_id, device_base_name, device_base_producer, device_base_year_of_release)
    values(4, 'Nintendo Switch', 'Nintendo', 2016);

-- init units
insert into device(device_id, device_is_available, device_number_of_controllers, device_base_id)
    values(1, true, 1, 1);
insert into device(device_id, device_is_available, device_number_of_controllers, device_base_id)
    values(2, true, 2, 1);
insert into device(device_id, device_is_available, device_number_of_controllers, device_base_id)
    values(3, false, 1, 1);

insert into device(device_id, device_is_available, device_number_of_controllers, device_base_id)
    values(4, false, 3, 3);
insert into device(device_id, device_is_available, device_number_of_controllers, device_base_id)
    values(5, true, 1, 3);
insert into device(device_id, device_is_available, device_number_of_controllers, device_base_id)
    values(6, false, 2, 3);

insert into device(device_id, device_is_available, device_number_of_controllers, device_base_id)
    values(7, true, 1, 4);