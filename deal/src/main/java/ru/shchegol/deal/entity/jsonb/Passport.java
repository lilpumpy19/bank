package ru.shchegol.deal.entity.jsonb;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
//import org.hibernate.annotations.TypeDef;

import javax.persistence.Embeddable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
//@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class Passport {

    private String Series;
    private String Number;
    private String IssueBranch;
    private Date IssueDate;
}
