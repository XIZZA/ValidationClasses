package validator.payload;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class TransactionRequestValidatorTest {

    private TransactionRequestValidator.Transaction transaction;
    private Validator validator;
    private TransactionRequestValidator transactionValidator;

    @BeforeEach
    void setUp() {
        transaction = new TransactionRequestValidator.Transaction();
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        transactionValidator = new TransactionRequestValidator();
    }

    @Test
    void testFieldValidations() {
        // Setting invalid values to trigger constraint violations
        transaction.setAmount(-100.0);  // Invalid amount (should be > 0)
        transaction.setSid(0);  // Invalid sid (should be > 0)
        transaction.setManager("");  // Invalid manager (should not be blank)
        transaction.setLogin("");  // Invalid login (should not be blank)
        transaction.setCurrency("JPY");  // Invalid currency (not supported)
        transaction.setComment("");  // Invalid comment (should not be blank)

        // Validate the fields using the validator
        Set<ConstraintViolation<TransactionRequestValidator.Transaction>> violations = validator.validate(transaction);

        // Assert that there are six constraint violations
        assertEquals(6, violations.size());

        // Check for specific field validation messages
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("amount") && v.getMessage().matches("must be greater than 0")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("sid") && v.getMessage().equals("must be greater than 0")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("manager") && v.getMessage().equals("must not be blank")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("login") && v.getMessage().equals("must not be blank")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("currency") && v.getMessage().matches("must be a supported currency")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("comment") && v.getMessage().equals("must not be blank")));
    }

    @Test
    void testValidTransaction() {
        // Setting valid values
        transaction.setAmount(150.0);
        transaction.setSid(123);
        transaction.setManager("12345");
        transaction.setLogin("54321");
        transaction.setCurrency("USD");
        transaction.setComment("Valid transaction");

        // Validate the fields using the validator
        Set<ConstraintViolation<TransactionRequestValidator.Transaction>> violations = validator.validate(transaction);

        // Assert that there are no violations
        assertTrue(violations.isEmpty());

        // Assert that validate method returns true
        assertTrue(transactionValidator.validate(transaction));
    }

    @Test
    void testUnsupportedCurrency() {
        transaction.setAmount(100.0);
        transaction.setSid(123);
        transaction.setManager("12345");
        transaction.setLogin("54321");
        transaction.setCurrency("INR");  // Unsupported currency
        transaction.setComment("Transaction with unsupported currency");

        // Validate the fields using the validator
        Set<ConstraintViolation<TransactionRequestValidator.Transaction>> violations = validator.validate(transaction);

        // Assert that the currency is not supported
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("currency") && v.getMessage().matches("must be a supported currency")));

        // Assert that validate method returns false for unsupported currency
        assertFalse(transactionValidator.validate(transaction));
    }

    @Test
    void testBlankFields() {
        transaction.setAmount(50.0);
        transaction.setSid(1);
        transaction.setManager("");  // Blank field
        transaction.setLogin("");  // Blank field
        transaction.setCurrency("USD");
        transaction.setComment("");  // Blank field

        Set<ConstraintViolation<TransactionRequestValidator.Transaction>> violations = validator.validate(transaction);

        // Assert that there are three violations for blank fields
        assertEquals(3, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("manager") && v.getMessage().equals("must not be blank")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("login") && v.getMessage().equals("must not be blank")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("comment") && v.getMessage().equals("must not be blank")));

        // Assert that validate method returns false
        assertFalse(transactionValidator.validate(transaction));
    }

    @Test
    void testInvalidAmount() {
        transaction.setAmount(-10.0);  // Invalid amount
        transaction.setSid(1);
        transaction.setManager("12345");
        transaction.setLogin("54321");
        transaction.setCurrency("USD");
        transaction.setComment("Negative amount");

        Set<ConstraintViolation<TransactionRequestValidator.Transaction>> violations = validator.validate(transaction);

        // Assert that there is one violation for amount
        assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("amount") && v.getMessage().matches("must be greater than 0")));

        // Assert that validate method returns false
        assertFalse(transactionValidator.validate(transaction));
    }

    @Test
    void testInvalidSid() {
        transaction.setAmount(100.0);
        transaction.setSid(0);  // Invalid SID
        transaction.setManager("12345");
        transaction.setLogin("54321");
        transaction.setCurrency("EUR");
        transaction.setComment("Invalid SID");

        Set<ConstraintViolation<TransactionRequestValidator.Transaction>> violations = validator.validate(transaction);

        // Assert that there is one violation for SID
        assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("sid") && v.getMessage().matches("must be greater than 0")));

        // Assert that validate method returns false
        assertFalse(transactionValidator.validate(transaction));
    }
}
