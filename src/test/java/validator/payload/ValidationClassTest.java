package validator.payload;

import com.fasterxml.jackson.core.JsonParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ValidationClassTest {

    private ValidationClass validationClass;
    private Validator validator;

    @BeforeEach
    void setUp() {
        validationClass = new ValidationClass();
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testFieldValidations() {
        validationClass.setUser(null);  // This should trigger a @NotNull validation error
        validationClass.setPassword("");  // This should trigger a @NotBlank validation error
        validationClass.setSid(null);  // This should trigger a @NotNull validation error
        validationClass.setGroupName("");  // This should trigger a @NotBlank validation error
        validationClass.setInitialBalance(-1);  // This should trigger a @PositiveOrZero validation error

        // Testing customFields map validation
        Map<String, String> customFields = new HashMap<>();
        customFields.put("", "value");  // Key should not be blank
        customFields.put("key", "");  // Value should not be blank
        validationClass.setCustomFields(customFields);

        Set<ConstraintViolation<ValidationClass>> violations = validator.validate(validationClass);

        // Assert that the expected number of violations is present
        assertEquals(6, violations.size());

        // Check specific violation messages
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("must not be null") && v.getPropertyPath().toString().equals("user")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("must not be blank") && v.getPropertyPath().toString().equals("password")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("must not be null") && v.getPropertyPath().toString().equals("sid")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("must not be blank") && v.getPropertyPath().toString().equals("groupName")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Initial balance must be zero or positive") && v.getPropertyPath().toString().equals("initialBalance")));
        assertFalse(violations.stream().anyMatch(v -> v.getMessage().equals("Custom field key cannot be blank")));
        assertFalse(violations.stream().anyMatch(v -> v.getMessage().equals("Custom field value cannot be blank")));
    }

    @Test
    void testValidateBodySuccess() throws JsonParseException {
        String validJsonPayload = """
                {
                    "user": 123,
                    "password": "password123",
                    "sid": 456,
                    "groupName": "testGroup",
                    "leverage": 100,
                    "initialBalance": 1000,
                    "notifyDisable": true,
                    "readOnly": false,
                    "customFields": {
                        "field1": "value1",
                        "field2": "value2"
                    }
                }
                """;
        assertTrue(validationClass.validateBody(validJsonPayload));
    }

    @Test
    void testValidateBodyFailure() throws JsonParseException {
        String invalidJsonPayload = """
                {
                    "user": 123,
                    "password": "password123"
                }
                """;
        assertFalse(validationClass.validateBody(invalidJsonPayload));
    }

    @Test
    void testValidateBodyJsonParsingError() {
        String malformedJsonPayload = "{ \"user\": 123, ";  // Incomplete JSON

        Executable executable = () -> validationClass.validateBody(malformedJsonPayload);
        assertDoesNotThrow(executable);  // Ensure that JSON parsing errors are handled gracefully
        try {
            assertFalse(validationClass.validateBody(malformedJsonPayload));
        } catch (JsonParseException e) {
            throw new RuntimeException(e);
        }
    }
}
