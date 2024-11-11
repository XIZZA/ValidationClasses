package validator.payload;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CurrencyConversionRequestTest {

    private CurrencyConversionRequest currencyConversionRequest;
    private Validator validator;

    @BeforeEach
    void setUp() {
        currencyConversionRequest = new CurrencyConversionRequest();
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testFieldValidations() {
        // Set invalid values to trigger constraint violations
        currencyConversionRequest.setFromCurrency("USD$");  // Invalid currency code
        currencyConversionRequest.setToCurrency("E");       // Invalid currency code
        currencyConversionRequest.setAmount(-1.0);          // Invalid amount

        // Validate the fields using the validator
        Set<ConstraintViolation<CurrencyConversionRequest>> violations = validator.validate(currencyConversionRequest);

        // Assert that there are three constraint violations
        assertEquals(3, violations.size());

        // Check for specific field validation messages
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("fromCurrency") && v.getMessage().equals("fromCurrency must be a valid 3-letter currency code.")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("toCurrency") && v.getMessage().equals("toCurrency must be a valid 3-letter currency code.")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("amount") && v.getMessage().equals("Amount must be greater than 0.")));
    }

    @Test
    void testValidCurrencyConversionRequest() {
        // Set valid values
        currencyConversionRequest.setFromCurrency("USD");
        currencyConversionRequest.setToCurrency("EUR");
        currencyConversionRequest.setAmount(100.0);

        // Validate the fields using the validator
        Set<ConstraintViolation<CurrencyConversionRequest>> violations = validator.validate(currencyConversionRequest);

        // Assert that there are no violations
        assertTrue(violations.isEmpty());
    }

    @Test
    void testInvalidFromCurrency() {
        currencyConversionRequest.setFromCurrency("US");  // Invalid currency code
        currencyConversionRequest.setToCurrency("EUR");
        currencyConversionRequest.setAmount(50.0);

        Set<ConstraintViolation<CurrencyConversionRequest>> violations = validator.validate(currencyConversionRequest);

        // Assert that there is one violation for fromCurrency
        assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("fromCurrency") && v.getMessage().equals("fromCurrency must be a valid 3-letter currency code.")));
    }

    @Test
    void testInvalidToCurrency() {
        currencyConversionRequest.setFromCurrency("USD");
        currencyConversionRequest.setToCurrency("EU");  // Invalid currency code
        currencyConversionRequest.setAmount(50.0);

        Set<ConstraintViolation<CurrencyConversionRequest>> violations = validator.validate(currencyConversionRequest);

        // Assert that there is one violation for toCurrency
        assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("toCurrency") && v.getMessage().equals("toCurrency must be a valid 3-letter currency code.")));
    }

    @Test
    void testInvalidAmount() {
        currencyConversionRequest.setFromCurrency("USD");
        currencyConversionRequest.setToCurrency("EUR");
        currencyConversionRequest.setAmount(0.0);  // Invalid amount (less than minimum)

        Set<ConstraintViolation<CurrencyConversionRequest>> violations = validator.validate(currencyConversionRequest);

        // Assert that there is one violation for amount
        assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("amount") && v.getMessage().equals("Amount must be greater than 0.")));
    }
}
