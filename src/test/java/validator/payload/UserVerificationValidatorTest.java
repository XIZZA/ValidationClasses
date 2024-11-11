package validator.payload;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserVerificationValidatorTest {

    private UserVerificationValidator validatorInstance;
    private Validator validator;

    @BeforeEach
    void setUp() {
        validatorInstance = new UserVerificationValidator();
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testFieldValidations() {
        // Setting invalid values to trigger constraint violations
        validatorInstance.setUser(null);  // Should trigger @NotNull validation
        validatorInstance.setIsVerified(null);  // Should trigger @NotNull validation
        validatorInstance.setVerified(null);  // Should trigger @NotNull validation

        // Validate the fields using the validator
        Set<ConstraintViolation<UserVerificationValidator>> violations = validator.validate(validatorInstance);

        // Assert that there are three constraint violations
        assertEquals(3, violations.size());

        // Check for specific field validation messages
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("user") && v.getMessage().equals("must not be null")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("isVerified") && v.getMessage().equals("must not be null")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("verified") && v.getMessage().equals("must not be null")));
    }

    @Test
    void testValidateMethodSuccess() {
        // Setting valid values
        validatorInstance.setUser(123);
        validatorInstance.setIsVerified(true);
        validatorInstance.setVerified(true);

        // Validate using the static validate method
        boolean result = UserVerificationValidator.validate(validatorInstance);

        // Assert that the result is true since all fields are correctly set
        assertTrue(result);
    }

    @Test
    void testValidateMethodFailureForNullUser() {
        // Set fields with an invalid user value
        validatorInstance.setUser(null);
        validatorInstance.setIsVerified(true);
        validatorInstance.setVerified(true);

        // Validate using the static validate method
        boolean result = UserVerificationValidator.validate(validatorInstance);

        // Assert that the result is false due to user being null
        assertFalse(result);
    }

    @Test
    void testValidateMethodFailureForNegativeUser() {
        // Set fields with a negative user value
        validatorInstance.setUser(-1);
        validatorInstance.setIsVerified(true);
        validatorInstance.setVerified(true);

        // Validate using the static validate method
        boolean result = UserVerificationValidator.validate(validatorInstance);

        // Assert that the result is false due to user being negative
        assertFalse(result);
    }

    @Test
    void testValidateMethodFailureForNullIsVerifiedAndVerified() {
        // Set fields with null values for isVerified and verified
        validatorInstance.setUser(123);
        validatorInstance.setIsVerified(null);
        validatorInstance.setVerified(null);

        // Validate using the static validate method
        boolean result = UserVerificationValidator.validate(validatorInstance);

        // Assert that the result is false due to isVerified and verified being null
        assertFalse(result);
    }
}
