package com.devacado.photoapp.api.users.repository;

import com.devacado.photoapp.api.users.data.UserEntity;
import com.devacado.photoapp.api.users.data.UsersRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;

@ExtendWith({SpringExtension.class})
@SpringBootTest
public class UserRepositoryTest {

    @Autowired
    private UsersRepository usersRepository;

    private static File DATA_JSON = Paths.get("src", "test", "resources", "users.json").toFile();

    @BeforeEach
    public void setup() throws IOException {
        // Deserialize users from JSON file to userEntity array
        UserEntity[] users = new ObjectMapper().readValue(DATA_JSON, UserEntity[].class);

        // Save each user to database
        Arrays.stream(users).forEach(usersRepository::save);
    }

    @AfterEach
    public void cleanup() {
        // Cleanup the database after each test
        usersRepository.deleteAll();
    }

    @Test
    @DisplayName("Test user found by email existing in database")
    public void testUserFoundByEmailExisting() {
        // Given two users in the database

        // When
        UserEntity retrievedUser = usersRepository.findByEmail("aghaei@gmail.com");

        // Then
        Assertions.assertNotNull(retrievedUser, "user with email address = aghaei@gmail.com should exist");
        Assertions.assertEquals("masoud",retrievedUser.getFirstName());
    }

}
