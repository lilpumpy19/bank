package ru.shchegol.calculator.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "Employment status of the client", example = "EMPLOYED")
    private EmploymentStatus employmentStatus;

    @NotBlank
    @Schema(description = "Employer's INN", example = "1234567890")
    private String employerINN;

    @NotNull
    @DecimalMin(value = "10000")
    @Schema(description = "Client's monthly salary", example = "50000")
    private BigDecimal salary;

    @NotNull
    @Schema(description = "Client's position", example = "MANAGER")
    private Position position;

    @NotNull
    @Min(value = 0)
    @Schema(description = "Total work experience in months", example = "60")
    private Integer workExperienceTotal;

    @NotNull
    @Min(value = 0)
    @Schema(description = "Current work experience in months at current job", example = "24")
    private Integer workExperienceCurrent;
}
