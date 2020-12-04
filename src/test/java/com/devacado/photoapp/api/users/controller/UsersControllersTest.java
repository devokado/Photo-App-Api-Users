package com.devacado.photoapp.api.users.controller;

import com.devacado.photoapp.api.users.service.UsersService;
import com.devacado.photoapp.api.users.shared.UserDTO;
import com.devacado.photoapp.api.users.ui.model.CreateUserRequestModel;
import com.devacado.photoapp.api.users.ui.model.CreateUserResponseModel;
import com.devacado.photoapp.api.users.ui.model.LoginRequestModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application_test.properties")
public class UsersControllersTest {

    @MockBean
    private UsersService usersService;

    @Autowired
    private MockMvc mockMvc;


    @Test
    @DisplayName("Test Create new user - POST /users")
    public void testCreateUser() throws Exception {

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        // Prepare mock user
        CreateUserRequestModel newUser = new CreateUserRequestModel("Khatereh", "Tajfar", "khatereh@gmail.com", "12345678");
        CreateUserResponseModel mockUser = new CreateUserResponseModel("Khatereh", "Tajfar", "khatereh@gmail.com", "1");

        // Prepare mock service method
        doReturn(modelMapper.map(mockUser, UserDTO.class)).when(usersService).createUser(ArgumentMatchers.any());

        // Perform POST request
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(new ObjectMapper().writeValueAsString(newUser)))

                // Validate 201 CREATED and JSON response type received
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))

                // Validate response body
                .andExpect(jsonPath("$.userId", is("1")))
                .andExpect(jsonPath("$.firstName", is("Khatereh")))
                .andExpect(jsonPath("$.lastName", is("Tajfar")))
                .andExpect(jsonPath("$.email", is("khatereh@gmail.com")));
    }

    @Test
    @DisplayName("Test Login User - POST /users/login")
    public void testLoginUser() throws Exception {
        LoginRequestModel user = new LoginRequestModel("khatereh@gmail.com","12345678");
        //TODO: this test needs to be completed

    }


}
