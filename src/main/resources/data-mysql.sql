-- init roles
insert into role(role_id, authority) values(2, 'CLIENT');

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
    values(3, true, 1, 1);

insert into device(device_id, device_is_available, device_number_of_controllers, device_base_id)
    values(4, true, 3, 3);
insert into device(device_id, device_is_available, device_number_of_controllers, device_base_id)
    values(5, true, 1, 3);
insert into device(device_id, device_is_available, device_number_of_controllers, device_base_id)
    values(6, true, 2, 3);

insert into device(device_id, device_is_available, device_number_of_controllers, device_base_id)
    values(7, true, 1, 4);

-- init clients
insert into client(client_id, client_email, client_name, client_phone)
    values(1, 'vlad@email.com', 'vlad', '0720 XXX XXX');
insert into client(client_id, client_email, client_name, client_phone)
    values(2, 'gheorghe@email.com', 'Gheorghe Ionut', '0721 987 123');
insert into client(client_id, client_email, client_name, client_phone)
    values(3, 'vasile_t@email.com', 'Vasile Turcu', '0754 098 126');

-- init games
insert into game(game_id, game_genre, game_name)
    values(1, 'action', 'GTA 5');
insert into game(game_id, game_genre, game_name)
    values(2, 'rpg', 'Alt joc nu prea stiu multe');

-- init game copies
    -- gta 5
insert into game_copy(game_copy_id, game_id, device_base_id, is_available)
    values(1, 1, 1, true);
insert into game_copy(game_copy_id, game_id, device_base_id, is_available)
    values(2, 1, 1, true);
insert into game_copy(game_copy_id, game_id, device_base_id, is_available)
    values(3, 1, 1, true);
    -- alt joc
insert into game_copy(game_copy_id, game_id, device_base_id, is_available)
    values(4, 2, 4, true);
