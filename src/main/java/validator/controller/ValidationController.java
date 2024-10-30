package validator.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import validator.payload.ValidationClass;

@RestController
@RequestMapping("/api")
public class ValidationController {

    @PostMapping("/validate")
    public ResponseEntity<String> validateRequest(@RequestBody @Valid ValidationClass request) {
        // If validation passes (through @Valid annotation), process the request
        return ResponseEntity.ok("Request validated successfully");
    }
}