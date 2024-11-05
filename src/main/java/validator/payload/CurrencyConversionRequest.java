package validator.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

@Setter
@Getter
@Validated
public class CurrencyConversionRequest {
    // Getters and Setters
    @NotBlank
    @Pattern(regexp = "^[A-Z]{3}$", message = "fromCurrency must be a valid 3-letter currency code.")
    @JsonProperty private String fromCurrency;

    @NotBlank
    @Pattern(regexp = "^[A-Z]{3}$", message = "toCurrency must be a valid 3-letter currency code.")
    @JsonProperty private String toCurrency;

    @DecimalMin(value = "0.01", message = "Amount must be greater than 0.")
    @JsonProperty private Double amount;

}
