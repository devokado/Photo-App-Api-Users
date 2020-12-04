package com.devacado.photoapp.api.users.service;

import com.devacado.photoapp.api.users.data.UserEntity;
import com.devacado.photoapp.api.users.data.UsersRepository;
import com.devacado.photoapp.api.users.shared.UserDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.AssertionErrors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application_test.properties")
public class UsersServicesTest {

    @Autowired
    private UsersService usersService;

    @MockBean
    private UsersRepository usersRepository;

    @Test
    @DisplayName("Register new user successfully")
    public void testSuccessfulUserRegister() {

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        UserEntity mockUser = new UserEntity(1, "khatereh", "tajfar", "khatere@gmail.com", "1", "123");
        UserDTO userDTO = modelMapper.map(mockUser, UserDTO.class);
        userDTO.setPassword("12345678");

        doReturn(mockUser).when(usersRepository).save(any());

        UserDTO savedUser = usersService.createUser(userDTO);

        AssertionErrors.assertNotNull("User should not be null", savedUser);
        Assertions.assertSame("khatereh", savedUser.getFirstName());
        Assertions.assertSame("tajfar", savedUser.getLastName());
        Assertions.assertSame("khatere@gmail.com", savedUser.getEmail());
    }

    @Test
    @DisplayName("Find user details by Email")
    public void testFindUserDetailsByEmail() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        UserEntity mockUser = new UserEntity(1, "khatereh", "tajfar", "khatere@gmail.com", "1", "123");

        doReturn(mockUser).when(usersRepository).findByEmail("khatere@gmail.com");

        UserDTO retrievedUser = usersService.getUserDetailsByEmail("khatere@gmail.com");

        AssertionErrors.assertNotNull("User should not be null", retrievedUser);
        Assertions.assertSame("khatereh", retrievedUser.getFirstName());
        Assertions.assertSame("tajfar", retrievedUser.getLastName());
        Assertions.assertSame("1", retrievedUser.getUserId());

    }
}
