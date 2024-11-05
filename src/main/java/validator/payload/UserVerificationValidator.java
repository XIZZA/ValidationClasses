package validator.payload;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserVerificationValidator {
    // Getters and Setters
    @NotNull private Integer user;
    @NotNull private Boolean isVerified;
    @NotNull private Boolean verified;

    public static boolean validate(UserVerificationValidator dto) {
        return dto.getUser() != null && dto.getUser() > 0 &&
                dto.getIsVerified() != null && dto.getVerified() != null;
    }
}
