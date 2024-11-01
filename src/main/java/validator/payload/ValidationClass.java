package validator.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.Map;

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

    @NotNull(message = "Initial balance cannot be null")
    @PositiveOrZero(message = "Initial balance must be zero or positive")
    @JsonProperty
    private Integer initialBalance;

    @JsonProperty
    private Boolean notifyDisable;

    @JsonProperty
    private Boolean readOnly;

    @JsonProperty
    @NotNull(message = "Custom fields cannot be null")
    @Valid
    private Map<@NotBlank(message = "Custom field key cannot be blank") String,
            @NotBlank(message = "Custom field value cannot be blank") String> customFields;


    private boolean validateBody(String jsonPayload) throws JsonParseException {
        ObjectMapper mapper = new ObjectMapper();
        try {
            var data = mapper.readValue(jsonPayload, Map.class);
            return data.containsKey("user") &&
                    data.containsKey("password") &&
                    data.containsKey("sid") &&
                    data.containsKey("groupName") &&
                    data.containsKey("leverage") &&
                    data.containsKey("initialBalance") &&
                    data.containsKey("notifyDisable") &&
                    data.containsKey("readOnly") &&
                    data.containsKey("customFields") &&
                    data.get("customFields") instanceof Map; // Check if customFields is a Map
        } catch (JsonProcessingException e) {
            // Handle JSON parsing errors
            System.out.println("JSON Parsing Error: " + e.getMessage());
            return false;
        }
    }

}