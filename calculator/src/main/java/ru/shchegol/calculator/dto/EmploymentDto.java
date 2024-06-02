package ru.shchegol.calculator.dto;

import lombok.Data;
import ru.shchegol.calculator.dto.enums.EmploymentStatus;
import ru.shchegol.calculator.dto.enums.Position;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
@Data
public class EmploymentDto {

    @NotNull
    private EmploymentStatus employmentStatus;

    @NotBlank
    private String employerINN;

    @NotNull
    @DecimalMin(value = "10000")
    private BigDecimal salary;

    @NotNull
    private Position position;

    @NotNull
    @Min(value = 0)
    private Integer workExperienceTotal;

    @NotNull
    @Min(value = 0)
    private Integer workExperienceCurrent;
}
