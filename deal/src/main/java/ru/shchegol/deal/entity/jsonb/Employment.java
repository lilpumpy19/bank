package ru.shchegol.deal.entity.jsonb;


import lombok.*;
import ru.shchegol.dto.enums.EmploymentStatus;
import ru.shchegol.dto.enums.Position;


import javax.persistence.Embeddable;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class Employment {
    private EmploymentStatus status;
    private String employerINN;
    private BigDecimal Salary;
    private Position position;
    private Integer workExperienceTotal;
    private Integer workExperienceCurrent;
}
