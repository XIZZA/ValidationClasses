package validator.payload;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserAccountRequestTest {

    private UserAccountRequest userAccountRequest;
    private Validator validator;

    @BeforeEach
    void setUp() {
        userAccountRequest = new UserAccountRequest();
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testFieldValidations() {
        // Setting invalid values to trigger constraint violations
        userAccountRequest.setFirstName("");  // Should trigger @NotBlank validation
        userAccountRequest.setLastName("");  // Should trigger @NotBlank validation
        userAccountRequest.setCountry("USA");  // Should trigger @Pattern validation (expects two characters)
        userAccountRequest.setPhone("12345");  // Should trigger @Pattern validation (expects phone format)
        userAccountRequest.setEmail("invalid-email");  // Should trigger @Email validation
        userAccountRequest.setClientType("");  // Should trigger @NotBlank validation

        // Validate the fields using the validator
        Set<ConstraintViolation<UserAccountRequest>> violations = validator.validate(userAccountRequest);

        // Assert that there are six constraint violations
        assertEquals(6, violations.size());

        // Check for specific field validation messages
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("firstName") && v.getMessage().equals("must not be blank")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("lastName") && v.getMessage().equals("must not be blank")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("country") && v.getMessage().matches("must match")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("phone") && v.getMessage().matches("must match")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email") && v.getMessage().matches("must be a well-formed email address")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("clientType") && v.getMessage().equals("must not be blank")));
    }

    @Test
    void testValidUserAccountRequest() {
        // Setting valid values
        userAccountRequest.setFirstName("John");
        userAccountRequest.setLastName("Doe");
        userAccountRequest.setMiddleName("M");
        userAccountRequest.setCountry("US");
        userAccountRequest.setPhone("+1234567890");
        userAccountRequest.setEmail("john.doe@example.com");
        userAccountRequest.setClientType("Individual");
        userAccountRequest.setEmailVerified(true);

        // Validate the fields using the validator
        Set<ConstraintViolation<UserAccountRequest>> violations = validator.validate(userAccountRequest);

        // Assert that there are no violations
        assertTrue(violations.isEmpty());
    }

    @Test
    void testInvalidCountryCode() {
        userAccountRequest.setFirstName("John");
        userAccountRequest.setLastName("Doe");
        userAccountRequest.setCountry("USA");  // Invalid country code format

        Set<ConstraintViolation<UserAccountRequest>> violations = validator.validate(userAccountRequest);

        // Assert that there is one violation for the country code
        assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("country") && v.getMessage().matches("must match")));
    }

    @Test
    void testInvalidPhoneFormat() {
        userAccountRequest.setFirstName("John");
        userAccountRequest.setLastName("Doe");
        userAccountRequest.setPhone("12345");  // Invalid phone format

        Set<ConstraintViolation<UserAccountRequest>> violations = validator.validate(userAccountRequest);

        // Assert that there is one violation for the phone format
        assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("phone") && v.getMessage().matches("must match")));
    }

    @Test
    void testInvalidEmailFormat() {
        userAccountRequest.setFirstName("John");
        userAccountRequest.setLastName("Doe");
        userAccountRequest.setEmail("invalid-email");  // Invalid email format

        Set<ConstraintViolation<UserAccountRequest>> violations = validator.validate(userAccountRequest);

        // Assert that there is one violation for the email format
        assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email") && v.getMessage().matches("must be a well-formed email address")));
    }
}
