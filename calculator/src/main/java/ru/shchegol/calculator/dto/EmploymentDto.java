package ru.shchegol.calculator.dto;

import lombok.Data;
import ru.shchegol.calculator.dto.dependencies.EmploymentStatus;
import ru.shchegol.calculator.dto.dependencies.Position;

import java.math.BigDecimal;
@Data
public class EmploymentDto {
    private EmploymentStatus employmentStatus;
    private String employerINN;
    private BigDecimal salary;
    private Position position;
    private Integer workExperienceTotal;
    private Integer workExperienceCurrent;
}
