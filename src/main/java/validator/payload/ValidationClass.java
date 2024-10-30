package validator.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

import static jakarta.validation.Validation.buildDefaultValidatorFactory;

public class ValidationClass {
    @NotNull
    @JsonProperty
    private Integer user;

    @NotBlank
    @JsonProperty
    private String password;

    @NotNull
    @JsonProperty
    private Integer sid;

    @NotBlank
    @JsonProperty
    private String groupName;

    public static boolean validateBody(String jsonPayload) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ValidationClass request = objectMapper.readValue(jsonPayload, ValidationClass.class);

            // Use Bean Validation to validate the parsed object
            ValidatorFactory factory = buildDefaultValidatorFactory();
            Validator validator = factory.getValidator();
            Set<ConstraintViolation<ValidationClass>> violations = validator.validate(request);

            if (!violations.isEmpty()) {
                // Handle validation errors
                for (ConstraintViolation<ValidationClass> violation : violations) {
                    System.out.println("Validation Error: " + violation.getMessage());
                }
                return false;
            }

            return true;
        } catch (JsonProcessingException e) {
            // Handle JSON parsing errors
            System.out.println("JSON Parsing Error: " + e.getMessage());
            return false;
        }
    }

}