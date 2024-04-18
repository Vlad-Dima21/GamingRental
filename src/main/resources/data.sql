-- init devices
insert into device_base(device_base_id, device_base_name, device_base_producer, device_base_year_of_release, device_base_image_url)
    values(1, 'PS5 Slim', 'Sony', 2018, 'https://cdn.ozone.ro/media/catalog/product/cache/1/image/400x498/a4e40ebdc3e371adff845072e1c73f37/k/o/0fb9fa76b73891f5e77e5f27f7dac893/consola-playstation-5-slim-31.jpg');
insert into device_base(device_base_id, device_base_name, device_base_producer, device_base_year_of_release, device_base_image_url)
    values(2, 'Xbox Series S', 'Microsoft', 2019, 'https://lcdn.altex.ro/resize/media/catalog/product/c/o/2bd48d28d1c32adea0e55139a4e6434a/consola_microsoft_xbox_series_s_digital_edition_carbon_black_01_f214bea0.jpg');
insert into device_base(device_base_id, device_base_name, device_base_producer, device_base_year_of_release, device_base_image_url)
    values(3, 'Xbox Series X', 'Microsoft', 2014, 'https://s13emagst.akamaized.net/products/32508/32507400/images/res_206705d9f4ec5f4ef02090a56ad9af50.jpg');
insert into device_base(device_base_id, device_base_name, device_base_producer, device_base_year_of_release, device_base_image_url)
    values(4, 'Nintendo Switch', 'Nintendo', 2016, 'https://lcdn.altex.ro/resize/media/catalog/product/C/o/2bd48d28d1c32adea0e55139a4e6434a/Consola_Nintendo_Switch_Red_Blue_2019_HAD_Baterie_noua_Cutia_04c66c4b.jpg');

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
    values(2, 'vlad@email.com', 'vlad', '0720 XXX XXX');
insert into client(client_id, client_email, client_name, client_phone)
    values(3, 'gheorghe@email.com', 'Gheorghe Ionut', '0721 987 123');
insert into client(client_id, client_email, client_name, client_phone)
    values(4, 'vasile_t@email.com', 'Vasile Turcu', '0754 098 126');

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
