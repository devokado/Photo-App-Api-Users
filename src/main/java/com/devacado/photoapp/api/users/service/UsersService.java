package com.devacado.photoapp.api.users.service;

import com.devacado.photoapp.api.users.shared.UserDTO;

public interface UsersService {
    UserDTO createUser(UserDTO userDetails);
}
