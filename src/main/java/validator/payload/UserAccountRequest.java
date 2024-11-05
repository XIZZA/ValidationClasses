package validator.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

@Setter
@Getter
@Validated
public class UserAccountRequest {
    // Getters and Setters
    @NotBlank @JsonProperty private String firstName;
    @NotBlank @JsonProperty private String lastName;
    @JsonProperty private String middleName;  // Optional
    @NotBlank @Pattern(regexp = "^[A-Z]{2}$") @JsonProperty private String country; // Country code
    @NotBlank @Pattern(regexp = "^\\+\\d{1,15}$") @JsonProperty private String phone; // Phone number
    @NotBlank @Email @JsonProperty private String email;
    @NotBlank @JsonProperty private String clientType;
    @JsonProperty private Boolean emailVerified;

}
