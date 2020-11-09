package com.devacado.photoapp.api.users.ui.controller;

import com.devacado.photoapp.api.users.service.UsersService;
import com.devacado.photoapp.api.users.shared.UserDTO;
import com.devacado.photoapp.api.users.ui.model.CreateUserRequestModel;
import org.apache.catalina.User;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
public class UsersController {

    @Autowired
    private Environment environment;

    @Autowired
    UsersService usersService;

    @GetMapping("/status/check")
    public String checkStatus() {
        return "Working on port" + environment.getProperty("local.server.port");
    }

    @PostMapping
    public String createUser(@Valid @RequestBody CreateUserRequestModel userDetails) {

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        UserDTO userDTO = modelMapper.map(userDetails, UserDTO.class);
        usersService.createUser(userDTO);

        return "create user was called";
    }
}
