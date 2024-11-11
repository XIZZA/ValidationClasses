package validator.payload;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class PaginationValidatorTest {

    private PaginationValidator paginationValidator;
    private Validator validator;

    @BeforeEach
    void setUp() {
        paginationValidator = new PaginationValidator();
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testFieldValidations() {
        // Set invalid values to trigger constraint violations
        paginationValidator.setFromUserId(null);  // Should not be null
        paginationValidator.setLimit(-10);  // Should be > 0
        paginationValidator.setOffset(-5);  // Should be >= 0

        // Validate the fields using the validator
        Set<ConstraintViolation<PaginationValidator>> violations = validator.validate(paginationValidator);

        // Assert that there are three constraint violations
        assertEquals(3, violations.size());

        // Check for specific field validation messages
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("fromUserId") && v.getMessage().equals("must not be null")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("limit") && v.getMessage().matches("must be greater than 0")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("offset") && v.getMessage().matches("must be greater than or equal to 0")));
    }

    @Test
    void testValidPagination() {
        // Set valid values
        paginationValidator.setFromUserId(123);
        paginationValidator.setLimit(10);
        paginationValidator.setOffset(0);

        // Validate the fields using the validator
        Set<ConstraintViolation<PaginationValidator>> violations = validator.validate(paginationValidator);

        // Assert that there are no violations
        assertTrue(violations.isEmpty());

        // Assert that validate method returns true
        assertTrue(PaginationValidator.validate(paginationValidator));
    }

    @Test
    void testInvalidLimit() {
        paginationValidator.setFromUserId(1);
        paginationValidator.setLimit(0);  // Invalid limit
        paginationValidator.setOffset(0);

        Set<ConstraintViolation<PaginationValidator>> violations = validator.validate(paginationValidator);

        // Assert that there is one violation for limit
        assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("limit") && v.getMessage().matches("must be greater than 0")));

        // Assert that validate method returns false
        assertFalse(PaginationValidator.validate(paginationValidator));
    }

    @Test
    void testNegativeOffset() {
        paginationValidator.setFromUserId(1);
        paginationValidator.setLimit(5);
        paginationValidator.setOffset(-1);  // Invalid offset

        Set<ConstraintViolation<PaginationValidator>> violations = validator.validate(paginationValidator);

        // Assert that there is one violation for offset
        assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("offset") && v.getMessage().matches("must be greater than or equal to 0")));

        // Assert that validate method returns false
        assertFalse(PaginationValidator.validate(paginationValidator));
    }

    @Test
    void testNullFromUserId() {
        paginationValidator.setFromUserId(null);  // Invalid fromUserId
        paginationValidator.setLimit(5);
        paginationValidator.setOffset(0);

        Set<ConstraintViolation<PaginationValidator>> violations = validator.validate(paginationValidator);

        // Assert that there is one violation for fromUserId
        assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("fromUserId") && v.getMessage().equals("must not be null")));

        // Assert that validate method returns false
        assertFalse(PaginationValidator.validate(paginationValidator));
    }
}
