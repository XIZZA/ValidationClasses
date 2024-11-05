package validator.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

public class TransactionRequestValidator {

    private static final Set<String> SUPPORTED_CURRENCIES = Set.of("USD", "EUR", "KES");

    // Validation logic
    private boolean validate(Transaction transaction) {
        return transaction.getAmount() > 0 &&
                transaction.getSid() > 0 &&
                transaction.getManager() != null && transaction.getManager().matches("\\d+") &&
                transaction.getLogin() != null && transaction.getLogin().matches("\\d+") &&
                SUPPORTED_CURRENCIES.contains(transaction.getCurrency().toUpperCase()) &&
                transaction.getComment() != null && !transaction.getComment().trim().isEmpty();
    }

    // Model class for JSON payload
    @Setter
    @Getter
    public static class Transaction {
        @NotNull private double amount;
        @NotNull private int sid;
        @NotBlank private String manager;
        @NotBlank private String login;
        @NotBlank private String currency;
        @NotBlank private String comment;

        @JsonProperty("amount") public double getAmount() { return amount; }
        @JsonProperty("sid") public int getSid() { return sid; }
        @JsonProperty("manager") public String getManager() { return manager; }
        @JsonProperty("login") public String getLogin() { return login; }
        @JsonProperty("currency") public String getCurrency() { return currency; }
        @JsonProperty("comment") public String getComment() { return comment; }
    }
}
