package ru.shchegol.calculator.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import ru.shchegol.calculator.anno.MinAge;
import ru.shchegol.calculator.dto.enums.Gender;
import ru.shchegol.calculator.dto.enums.MaritalStatus;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ScoringDataDto {
    @NotNull
    @DecimalMin(value = "30000")
    @Schema(description = "Credit amount", example = "50000")
    private BigDecimal amount;

    @NotNull
    @Min(value = 6)
    @Schema(description = "Term in months", example = "12")
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

    @NotNull
    @Schema(description = "Gender of the client", example = "MALE")
    private Gender gender;

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

    @NotNull
    @Past
    @Schema(description = "Passport issue date", example = "2010-01-01")
    private LocalDate passportIssueDate;

    @NotBlank
    @Schema(description = "Passport issue branch", example = "UFMS Russia")
    private String passportIssueBranch;

    @NotNull
    @Schema(description = "Marital status", example = "SINGLE")
    private MaritalStatus maritalStatus;

    @NotNull
    @Min(value = 0)
    @Schema(description = "Number of dependents", example = "0")
    private Integer dependentAmount;

    @NotNull
    @Valid
    @Schema(description = "Employment details")
    private EmploymentDto employment;

    @NotBlank
    @Schema(description = "Account number", example = "12345678901234567890")
    private String accountNumber;

    @NotNull
    @Schema(description = "Insurance enabled", example = "true")
    private Boolean isInsuranceEnabled;

    @NotNull
    @Schema(description = "Salary client", example = "true")
    private Boolean isSalaryClient;
}
