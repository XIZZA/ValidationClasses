package validator.payload;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.*;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AccountIDTest {

    @Mock
    private AccountService accountService;

    @InjectMocks
    private AccountID accountID;

    private Validator validator;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
        objectMapper = new ObjectMapper();
    }

    @Test
    void testValidAccountRequest() {
        AccountID.AccountRequest validRequest = new AccountID.AccountRequest("123");

        // Validate the fields using the validator
        Set<ConstraintViolation<AccountID.AccountRequest>> violations = validator.validate(validRequest);

        // Assert that there are no violations
        assertTrue(violations.isEmpty());
    }

    @Test
    void testInvalidAccountRequest() {
        AccountID.AccountRequest invalidRequest = new AccountID.AccountRequest("abc"); // Invalid accountId (non-numeric)

        // Validate the fields using the validator
        Set<ConstraintViolation<AccountID.AccountRequest>> violations = validator.validate(invalidRequest);

        // Assert that there is one violation for accountId
        assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("accountId") && v.getMessage().equals("Account ID must be a numeric string.")));
    }

    @Test
    void testGetAssociatedAccountsSuccess() {
        // Mock the behavior of accountService
        when(accountService.getAssociatedAccounts("1")).thenReturn(List.of("AccountA", "AccountB"));

        // Create a valid request and call the method
        AccountID.AccountRequest request = new AccountID.AccountRequest("1");
        ResponseEntity<?> response = accountID.getAssociatedAccounts(request);

        // Assert that the response is successful and contains the expected accounts
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(List.of("AccountA", "AccountB"), response.getBody());
    }

    @Test
    void testGetAssociatedAccountsNoAccountsFound() {
        // Mock the behavior of accountService to throw an exception
        when(accountService.getAssociatedAccounts("999")).thenThrow(new IllegalArgumentException("No associated accounts found for account ID 999"));

        // Create a valid request and call the method
        AccountID.AccountRequest request = new AccountID.AccountRequest("999");
        ResponseEntity<?> response = accountID.getAssociatedAccounts(request);

        // Assert that the response is a bad request and contains the error message
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("No associated accounts found for account ID 999", response.getBody());
    }

    @Test
    void testDeserializeAccountRequest() throws JsonProcessingException {
        // JSON payload representing a valid AccountRequest
        String jsonPayload = "{\"accountId\":\"123\"}";

        // Deserialize the JSON to an AccountRequest object
        AccountID.AccountRequest request = objectMapper.readValue(jsonPayload, AccountID.AccountRequest.class);

        // Assert that the deserialized object contains the expected accountId
        assertEquals("123", request.getAccountId());
    }

    @Test
    void testDeserializeInvalidAccountRequest() throws JsonProcessingException {
        // JSON payload representing an invalid AccountRequest
        String jsonPayload = "{\"accountId\":\"abc\"}";

        // Deserialize the JSON to an AccountRequest object
        AccountID.AccountRequest request = objectMapper.readValue(jsonPayload, AccountID.AccountRequest.class);

        // Validate the deserialized object and assert the constraint violations
        Set<ConstraintViolation<AccountID.AccountRequest>> violations = validator.validate(request);
        assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("accountId") && v.getMessage().equals("Account ID must be a numeric string.")));
    }
}
