package ru.shchegol.deal.entity.jsonb;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
//import org.hibernate.annotations.TypeDef;
import ru.shchegol.deal.entity.enums.EmploymentStatus;
import ru.shchegol.deal.entity.enums.Position;

import javax.persistence.Embeddable;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
//@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class Employment {
    private EmploymentStatus status;
    private String employerINN;
    private BigDecimal Salary;
    private Position position;
    private int workExperienceTotal;
    private int workExperienceCurrent;
}
