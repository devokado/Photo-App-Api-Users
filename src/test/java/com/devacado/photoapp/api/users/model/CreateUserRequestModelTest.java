package com.devacado.photoapp.api.users.model;

import com.devacado.photoapp.api.users.ui.model.CreateUserRequestModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application_test.properties")
public class CreateUserRequestModelTest {

    @Autowired
    private static Validator validator;

    @Test
    @DisplayName("Test firstName Equals to Null")
    public void whenFirstNameIsNull_thenReturnErrorMessage() {

        validator = Validation.buildDefaultValidatorFactory().getValidator();
        CreateUserRequestModel newUser = new CreateUserRequestModel(null, "Tajfar", "khatere@gmail.com", "12345678");
        validator.validate(newUser).forEach(violation -> System.out.println(violation.getMessage()));
        Set<ConstraintViolation<CreateUserRequestModel>> violations = validator.validate(newUser);

        assertThat(violations.size()).isEqualTo(1);

    }

    @Test
    @DisplayName("Test lastName Equals to Null")
    public void whenLastNameIsNull_thenReturnErrorMessage() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
        CreateUserRequestModel newUser = new CreateUserRequestModel("Khatereh", null, "khatere@gmail.com", "12345678");
        validator.validate(newUser).forEach(violation -> System.out.println(violation.getMessage()));
        Set<ConstraintViolation<CreateUserRequestModel>> violations = validator.validate(newUser);

        assertThat(violations.size()).isEqualTo(1);

    }

    @Test
    @DisplayName("Test email Equals to Null")
    public void whenEmailIsNull_thenReturnErrorMessage() {


        validator = Validation.buildDefaultValidatorFactory().getValidator();
        CreateUserRequestModel newUser = new CreateUserRequestModel("Khatereh", "Tajfar", null, "12345678");
        validator.validate(newUser).forEach(violation -> System.out.println(violation.getMessage()));
        Set<ConstraintViolation<CreateUserRequestModel>> violations = validator.validate(newUser);

        assertThat(violations.size()).isEqualTo(1);

    }

    @Test
    @DisplayName("Test password Equals to Null")
    public void whenPasswordIsNull_thenReturnErrorMessage() {


        validator = Validation.buildDefaultValidatorFactory().getValidator();
        CreateUserRequestModel newUser = new CreateUserRequestModel("Khatereh", "Tajfar", "khatereh@gmail.com", null);
        validator.validate(newUser).forEach(violation -> System.out.println(violation.getMessage()));
        Set<ConstraintViolation<CreateUserRequestModel>> violations = validator.validate(newUser);

        assertThat(violations.size()).isEqualTo(1);

    }

    @Test
    @DisplayName("Test firstName Less than 2 characters ")
    public void whenFirstNameIsLessThan2Chars_thenReturnErrorMessage() {


        validator = Validation.buildDefaultValidatorFactory().getValidator();
        CreateUserRequestModel newUser = new CreateUserRequestModel("K", "Tajfar", "khatereh@gmail.com", "12345678");
        validator.validate(newUser).forEach(violation -> System.out.println(violation.getMessage()));
        Set<ConstraintViolation<CreateUserRequestModel>> violations = validator.validate(newUser);

        assertThat(violations.size()).isEqualTo(1);

    }

    @Test
    @DisplayName("Test lastName Less than 2 characters")
    public void whenLastNameIsLessThan2Chars_thenReturnErrorMessage() {

        validator = Validation.buildDefaultValidatorFactory().getValidator();
        CreateUserRequestModel newUser = new CreateUserRequestModel("Khatereh", "T", "khatereh@gmail.com", "12345678");
        validator.validate(newUser).forEach(violation -> System.out.println(violation.getMessage()));
        Set<ConstraintViolation<CreateUserRequestModel>> violations = validator.validate(newUser);

        assertThat(violations.size()).isEqualTo(1);

    }

    @Test
    @DisplayName("Test Invalid email address")
    public void whenEmailIsNotValid_thenReturnErrorMessage() {

        validator = Validation.buildDefaultValidatorFactory().getValidator();
        CreateUserRequestModel newUser = new CreateUserRequestModel("Khatereh", "Tajfar", "khatereh", "12345678");
        validator.validate(newUser).forEach(violation -> System.out.println(violation.getMessage()));
        Set<ConstraintViolation<CreateUserRequestModel>> violations = validator.validate(newUser);

        assertThat(violations.size()).isEqualTo(1);

    }

    @Test
    @DisplayName("Test password Less than 8 characters")
    public void whenPasswordIsLessThan8Chars_thenReturnErrorMessage() {

        validator = Validation.buildDefaultValidatorFactory().getValidator();
        CreateUserRequestModel newUser = new CreateUserRequestModel("Khatereh", "Tajfar", "khatereh@gmail.com", "123456");
        validator.validate(newUser).forEach(violation -> System.out.println(violation.getMessage()));
        Set<ConstraintViolation<CreateUserRequestModel>> violations = validator.validate(newUser);

        assertThat(violations.size()).isEqualTo(1);

    }

    @Test
    @DisplayName("Test password greater than 16 characters")
    public void whenPasswordIsGreaterThan16Chars_thenReturnErrorMessage() {

        validator = Validation.buildDefaultValidatorFactory().getValidator();
        CreateUserRequestModel newUser = new CreateUserRequestModel("Khatereh", "Tajfar", "khatereh@gmail.com", "12345678900000000");
        validator.validate(newUser).forEach(violation -> System.out.println(violation.getMessage()));
        Set<ConstraintViolation<CreateUserRequestModel>> violations = validator.validate(newUser);

        assertThat(violations.size()).isEqualTo(1);

    }

}
