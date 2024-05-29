package ru.shchegol.calculator.dto;

import lombok.Data;
import ru.shchegol.calculator.anno.MinAge;
import ru.shchegol.calculator.dto.dependencies.Gender;
import ru.shchegol.calculator.dto.dependencies.MaritalStatus;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ScoringDataDto {
    @NotNull
    @DecimalMin(value = "30000")
    private BigDecimal amount;

    @NotNull
    @Min(value = 6)
    private Integer term;

    @NotBlank
    @Size(min = 2, max = 30)
    @Pattern(regexp = "[a-zA-Z-]+")
    private String firstName;

    @NotBlank
    @Size(min = 2, max = 30)
    @Pattern(regexp = "[a-zA-Z-]+")

    @NotBlank
    @Size(min = 2, max = 30)
    @Pattern(regexp = "[a-zA-Z-]+")
    private String lastName;

    @Size(min = 2, max = 30)
    @Pattern(regexp = "[a-zA-Z-]+")
    private String middleName;

    @NotNull
    private Gender gender;

    @MinAge(value = 18)
    private LocalDate birthdate;

    @NotBlank
    @Size(min = 4, max = 4)
    @Pattern(regexp = "[0-9]+")
    private String passportSeries;

    @NotBlank
    @Size(min = 6, max = 6)
    @Pattern(regexp = "[0-9]+")
    private String passportNumber;

    @NotNull
    @Past
    private LocalDate passportIssueDate;

    @NotBlank
    private String passportIssueBranch;

    @NotNull
    private MaritalStatus maritalStatus;

    @NotNull
    @Min(value = 0)
    private Integer dependentAmount;

    @NotNull
    @Valid
    private EmploymentDto employment;

    @NotBlank
    private String accountNumber;

    @NotNull
    private Boolean isInsuranceEnabled;

    @NotNull
    private Boolean isSalaryClient;
}
