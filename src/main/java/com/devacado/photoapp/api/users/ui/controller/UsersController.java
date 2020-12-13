package com.devacado.photoapp.api.users.ui.controller;

import com.devacado.photoapp.api.users.service.UsersService;
import com.devacado.photoapp.api.users.shared.UserDTO;
import com.devacado.photoapp.api.users.ui.model.CreateUserRequestModel;
import com.devacado.photoapp.api.users.ui.model.CreateUserResponseModel;
import com.devacado.photoapp.api.users.ui.model.UserResponseModel;
import org.apache.catalina.User;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
        return "Working on port" + environment.getProperty("local.server.port") +
                " , with token = " + environment.getProperty("token.secret");
    }

    @PostMapping(consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<CreateUserResponseModel> createUser(@Valid @RequestBody CreateUserRequestModel userDetails) {

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        UserDTO userDTO = modelMapper.map(userDetails, UserDTO.class);
        UserDTO createdUser = usersService.createUser(userDTO);

        CreateUserResponseModel returnValue = modelMapper.map(createdUser, CreateUserResponseModel.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(returnValue);

    }

    @GetMapping(value = "/{userId}", produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<UserResponseModel> getUser(@PathVariable("userId") String userId) {
        UserDTO userDTO = usersService.getUserByUserId(userId);
        UserResponseModel returnValue = new ModelMapper().map(userDTO, UserResponseModel.class);
        return ResponseEntity.status(HttpStatus.OK).body(returnValue);
    }
}
