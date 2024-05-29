package ru.shchegol.calculator.dto;

import lombok.Data;
import ru.shchegol.calculator.anno.MinAge;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class LoanStatementRequestDto {

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
    private String lastName;

    @Size(min = 2, max = 30)
    @Pattern(regexp = "[a-zA-Z-]+")
    private String middleName;

    @NotBlank
    @Pattern(regexp = "[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+")
    private String email;

    @NotNull
    @MinAge(value = 18)
    private LocalDate birthdate;

    @NotBlank
    @Size(min = 4, max = 4)
    private String passportSeries;

    @NotBlank
    @Size(min = 6, max = 6)
    private String passportNumber;
}
