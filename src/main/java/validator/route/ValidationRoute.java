package validator.route;

import jakarta.validation.ConstraintViolation;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.springframework.stereotype.Component;
import validator.payload.ValidationClass;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;

@Component
public class ValidationRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("direct:validatePayload")
                .unmarshal(new JacksonDataFormat(ValidationClass.class))
                .process(exchange -> {
                    ValidationClass request = exchange.getIn().getBody(ValidationClass.class);

                    // Use Bean Validation to validate the parsed object
                    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
                    Validator validator = factory.getValidator();
                    Set<ConstraintViolation<ValidationClass>> violations = validator.validate(request);

                    if (!violations.isEmpty()) {
                        // Handle validation errors, e.g., throw an exception
                        StringBuilder errorMessage = new StringBuilder("Validation failed: ");
                        for (ConstraintViolation<ValidationClass> violation : violations) {
                            errorMessage.append(violation.getMessage()).append("; ");
                        }
                        throw new IllegalArgumentException(errorMessage.toString());
                    }

                })
                .to("direct:processValidPayload");
    }
}
