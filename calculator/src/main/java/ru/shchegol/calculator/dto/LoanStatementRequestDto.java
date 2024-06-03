package ru.shchegol.calculator.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import ru.shchegol.calculator.anno.MinAge;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class LoanStatementRequestDto {

    @NotNull
    @DecimalMin(value = "30000")
    @Schema(description = "Loan amount", example = "50000")
    private BigDecimal amount;

    @NotNull
    @Min(value = 6)
    @Schema(description = "Loan term in months", example = "12")
    private Integer term;

    @NotBlank
    @Size(min = 2, max = 30)
    @Pattern(regexp = "[a-zA-Z-]+")
    @Schema(description = "First name of the client", example = "John")
    private String firstName;

    @NotBlank
    @Size(min = 2, max = 30)
    @Pattern(regexp = "[a-zA-Z-]+")
    @Schema(description = "Last name of the client", example = "Doe")
    private String lastName;

    @Size(min = 2, max = 30)
    @Pattern(regexp = "[a-zA-Z-]+")
    @Schema(description = "Middle name of the client", example = "Michael")
    private String middleName;

    @NotBlank
    @Pattern(regexp = "[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+")
    @Schema(description = "Email of the client", example = "john.doe@example.com")
    private String email;

    @NotNull
    @MinAge(value = 18)
    @Schema(description = "Birthdate of the client", example = "1990-01-01")
    private LocalDate birthdate;

    @NotBlank
    @Size(min = 4, max = 4)
    @Pattern(regexp = "[0-9]+")
    @Schema(description = "Passport series", example = "1234")
    private String passportSeries;

    @NotBlank
    @Size(min = 6, max = 6)
    @Pattern(regexp = "[0-9]+")
    @Schema(description = "Passport number", example = "567890")
    private String passportNumber;
}