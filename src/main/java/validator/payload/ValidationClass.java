package validator.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

public class ValidationClass {
    @NotNull
    @JsonProperty
    private Integer user;

    @Setter
    @Getter
    @NotBlank
    @JsonProperty
    private String password;

    @NotNull
    @JsonProperty
    private Integer sid;

    @Getter
    @Setter
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

    @Getter
    @Setter
    @JsonProperty
    @NotNull(message = "Custom fields cannot be null")
    @Valid
    private Map<@NotBlank(message = "Custom field key cannot be blank") String,
            @NotBlank(message = "Custom field value cannot be blank") String> customFields;


    boolean validateBody(String jsonPayload) throws JsonParseException {
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
    public void setUser(Object user) {
        this.user = (Integer) user;
    }
    public Object getUser() {
        return user;
    }
    public void setSid(Object sid) {
        this.sid = (Integer) sid;
    }
    public Object getSid() {
        return sid;
    }
    public void setInitialBalance(int initialBalance) {
        this.initialBalance = initialBalance;
    }
    public int getInitialBalance() {
        return initialBalance;
    }
    public void setCustomFields(Map<String, String> customFields) {
    }
}