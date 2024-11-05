package validator.payload;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PaginationValidator {
    // Getters and Setters
    @NotNull private Integer fromUserId;
    @NotNull private Integer limit;
    @NotNull private Integer offset;

    public static boolean validate(PaginationValidator dto) {
        return dto.getFromUserId() != null && dto.getFromUserId() > 0 &&
                dto.getLimit() != null && dto.getLimit() > 0 &&
                dto.getOffset() != null && dto.getOffset() >= 0;
    }
}
