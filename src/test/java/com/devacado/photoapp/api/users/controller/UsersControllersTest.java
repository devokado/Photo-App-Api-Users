package com.devacado.photoapp.api.users.controller;

import com.devacado.photoapp.api.users.service.UsersService;
import com.devacado.photoapp.api.users.shared.UserDTO;
import com.devacado.photoapp.api.users.ui.model.CreateUserRequestModel;
import com.devacado.photoapp.api.users.ui.model.CreateUserResponseModel;
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

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations="classpath:application_test.properties")
public class UsersControllersTest {

    @MockBean
    private UsersService usersService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private static Validator validator;

    @Test
    @DisplayName("Test Create new user - POST /users-ws/users")
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
    @DisplayName("Test Create new user with firstName equals to Null - POST /users-ws/users")
    public void whenFirstNameIsNull_thenReturnErrorMessage() throws Exception {

        validator = Validation.buildDefaultValidatorFactory().getValidator();
        CreateUserRequestModel newUser = new CreateUserRequestModel(null, "Tajfar", "khatere@gmail.com", "12345678");
        validator.validate(newUser).stream().forEach(violation -> System.out.println(violation.getMessage()));
        Set<ConstraintViolation<CreateUserRequestModel>> violations = validator.validate(newUser);

        assertThat(violations.size()).isEqualTo(1);

    }

    @Test
    @DisplayName("Test Create new user with lastName equals to Null - POST /users-ws/users")
    public void whenLastNameIsNull_thenReturnErrorMessage() throws Exception {

        validator = Validation.buildDefaultValidatorFactory().getValidator();
        CreateUserRequestModel newUser = new CreateUserRequestModel("Khatereh", null, "khatere@gmail.com", "12345678");
        validator.validate(newUser).stream().forEach(violation -> System.out.println(violation.getMessage()));
        Set<ConstraintViolation<CreateUserRequestModel>> violations = validator.validate(newUser);

        assertThat(violations.size()).isEqualTo(1);

    }

    @Test
    @DisplayName("Test Create new user with email equals to Null - POST /users-ws/users")
    public void whenEmailIsNull_thenReturnErrorMessage() throws Exception {


        validator = Validation.buildDefaultValidatorFactory().getValidator();
        CreateUserRequestModel newUser = new CreateUserRequestModel("Khatereh", "Tajfar", null, "12345678");
        validator.validate(newUser).stream().forEach(violation -> System.out.println(violation.getMessage()));
        Set<ConstraintViolation<CreateUserRequestModel>> violations = validator.validate(newUser);

        assertThat(violations.size()).isEqualTo(1);

    }

    @Test
    @DisplayName("Test Create new user with password equals to Null - POST /users-ws/users")
    public void whenPasswordIsNull_thenReturnErrorMessage() throws Exception {


        validator = Validation.buildDefaultValidatorFactory().getValidator();
        CreateUserRequestModel newUser = new CreateUserRequestModel("Khatereh", "Tajfar", "khatereh@gmail.com", null);
        validator.validate(newUser).stream().forEach(violation -> System.out.println(violation.getMessage()));
        Set<ConstraintViolation<CreateUserRequestModel>> violations = validator.validate(newUser);

        assertThat(violations.size()).isEqualTo(1);

    }

    @Test
    @DisplayName("Test Create new user with firstName Less than 2 characters - POST /users-ws/users")
    public void whenFirstNameIsLessThan2Chars_thenReturnErrorMessage() throws Exception {


        validator = Validation.buildDefaultValidatorFactory().getValidator();
        CreateUserRequestModel newUser = new CreateUserRequestModel("K", "Tajfar", "khatereh@gmail.com", "12345678");
        validator.validate(newUser).stream().forEach(violation -> System.out.println(violation.getMessage()));
        Set<ConstraintViolation<CreateUserRequestModel>> violations = validator.validate(newUser);

        assertThat(violations.size()).isEqualTo(1);

    }

    @Test
    @DisplayName("Test Create new user with lastName Less than 2 characters  - POST /users-ws/users")
    public void whenLastNameIsLessThan2Chars_thenReturnErrorMessage() throws Exception {

        validator = Validation.buildDefaultValidatorFactory().getValidator();
        CreateUserRequestModel newUser = new CreateUserRequestModel("Khatereh", "T", "khatereh@gmail.com", "12345678");
        validator.validate(newUser).stream().forEach(violation -> System.out.println(violation.getMessage()));
        Set<ConstraintViolation<CreateUserRequestModel>> violations = validator.validate(newUser);

        assertThat(violations.size()).isEqualTo(1);

    }

    @Test
    @DisplayName("Test Create new user with an Invalid email address  - POST /users-ws/users")
    public void whenEmailIsNotValid_thenReturnErrorMessage() throws Exception {

        validator = Validation.buildDefaultValidatorFactory().getValidator();
        CreateUserRequestModel newUser = new CreateUserRequestModel("Khatereh", "Tajfar", "khatereh", "12345678");
        validator.validate(newUser).stream().forEach(violation -> System.out.println(violation.getMessage()));
        Set<ConstraintViolation<CreateUserRequestModel>> violations = validator.validate(newUser);

        assertThat(violations.size()).isEqualTo(1);

    }

    @Test
    @DisplayName("Test Create new user with password Less than 8 characters  - POST /users-ws/users")
    public void whenPasswordIsLessThan8Chars_thenReturnErrorMessage() throws Exception {

        validator = Validation.buildDefaultValidatorFactory().getValidator();
        CreateUserRequestModel newUser = new CreateUserRequestModel("Khatereh", "Tajfar", "khatereh@gmail.com", "123456");
        validator.validate(newUser).stream().forEach(violation -> System.out.println(violation.getMessage()));
        Set<ConstraintViolation<CreateUserRequestModel>> violations = validator.validate(newUser);

        assertThat(violations.size()).isEqualTo(1);

    }

    @Test
    @DisplayName("Test Create new user with password greater than 16 characters  - POST /users-ws/users")
    public void whenPasswordIsGreaterThan16Chars_thenReturnErrorMessage() throws Exception {

        validator = Validation.buildDefaultValidatorFactory().getValidator();
        CreateUserRequestModel newUser = new CreateUserRequestModel("Khatereh", "Tajfar", "khatereh@gmail.com", "12345678900000000");
        validator.validate(newUser).stream().forEach(violation -> System.out.println(violation.getMessage()));
        Set<ConstraintViolation<CreateUserRequestModel>> violations = validator.validate(newUser);

        assertThat(violations.size()).isEqualTo(1);

    }

}
