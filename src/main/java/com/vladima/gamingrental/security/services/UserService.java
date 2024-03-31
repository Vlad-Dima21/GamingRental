package com.vladima.gamingrental.security.services;

import com.vladima.gamingrental.helpers.BaseService;
import com.vladima.gamingrental.security.dto.UserClientDTO;
import com.vladima.gamingrental.security.dto.UserDTO;
import com.vladima.gamingrental.security.dto.UserResponseDTO;
import com.vladima.gamingrental.security.models.User;

public interface UserService extends BaseService<User, UserDTO> {

    UserResponseDTO login(UserDTO userDTO);
    UserResponseDTO registerClient(UserClientDTO userClientDTO);
}
