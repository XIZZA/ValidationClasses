package validator.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/account")
@Validated
public class AccountID {

    private final AccountService accountService;

    public AccountID(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/getAssociatedAccounts")
    public ResponseEntity<?> getAssociatedAccounts(@Valid @RequestBody AccountRequest request) {
        try {
            List<String> associatedAccounts = accountService.getAssociatedAccounts(request.accountId);
            return ResponseEntity.ok(associatedAccounts);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Nested static class for the request payload, combining model and validation
    @Getter
    @Setter
    public static class AccountRequest {
        @NotBlank @Pattern(regexp = "^\\d+$", message = "Account ID must be a numeric string.")
        @JsonProperty private String accountId;

        // Constructor for JSON deserialization
        public AccountRequest(@JsonProperty("accountId") String accountId) {
            this.accountId = accountId;
        }

    }
}

// Service class to handle account associations
@Service
class AccountService {
    private static final Map<String, List<String>> ACCOUNT_ASSOCIATIONS = Map.of(
            "1", List.of("AccountA", "AccountB"), "2", List.of("AccountC")
    );

    public List<String> getAssociatedAccounts(String accountId) {
        List<String> accounts = ACCOUNT_ASSOCIATIONS.get(accountId);
        if (accounts == null || accounts.isEmpty()) {
            throw new IllegalArgumentException("No associated accounts found for account ID " + accountId);
        }
        return accounts;
    }
}
