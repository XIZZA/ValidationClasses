package validator.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import validator.payload.*;

@RestController
@RequestMapping("/api")
public class ValidationController {

    private final ObjectMapper mapper = new ObjectMapper();

    @PostMapping("/validate")
    public ResponseEntity<String> validateRequest(@RequestBody @Valid ValidationClass request) {
        // If validation passes (through @Valid annotation), process the request
        return ResponseEntity.ok("Request validated successfully");
    }

    @PostMapping("/validate/transaction")
    public ResponseEntity<String> validateTransaction(@Valid @RequestBody TransactionRequestValidator.Transaction transaction) {
        // If validation passes (through @Valid annotation), process the request
        return ResponseEntity.ok("Transaction is valid.");
    }

    @PostMapping("/validate/create")
    public ResponseEntity<String> createUser(@Valid @RequestBody UserAccountRequest request) {
        if (request.getEmailVerified() != null && !request.getEmailVerified()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email must be verified to complete registration.");
        }
        return ResponseEntity.ok("User account created successfully.");
    }

    @PostMapping("/convert")
    public ResponseEntity<String> convertCurrency(@Valid @RequestBody CurrencyConversionRequest request) {
        // Here you would typically call a service to perform the conversion.
        return ResponseEntity.ok("Currency conversion requested: " + request.getFromCurrency() + " to " + request.getToCurrency() + " for amount " + request.getAmount());
    }

    @PostMapping("/validate/user")
    public ResponseEntity<String> validateUserPayload(@RequestBody String json) {
        try {
            JsonNode jsonNode = mapper.readTree(json);  // Parse JSON once
            if (jsonNode.has("user") && jsonNode.has("isVerified") && jsonNode.has("verified")) {
                UserVerificationValidator dto = mapper.treeToValue(jsonNode, UserVerificationValidator.class);
                boolean isValid = UserVerificationValidator.validate(dto);
                return new ResponseEntity<>(isValid ? "User Verification Payload Valid" : "Invalid User Verification Payload", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Invalid User Verification Payload structure.", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to parse JSON: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/validate/pagination")
    public ResponseEntity<String> validatePaginationPayload(@RequestBody String json) {
        try {
            JsonNode jsonNode = mapper.readTree(json);  // Parse JSON once
            if (jsonNode.has("fromUserId") && jsonNode.has("limit") && jsonNode.has("offset")) {
                PaginationValidator dto = mapper.treeToValue(jsonNode, PaginationValidator.class);
                boolean isValid = PaginationValidator.validate(dto);
                return new ResponseEntity<>(isValid ? "Pagination Payload Valid" : "Invalid Pagination Payload", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Invalid Pagination Payload structure.", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to parse JSON: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}