package ru.shchegol.deal.entity.jsonb;


import lombok.*;

import javax.persistence.Embeddable;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class Passport {

    private String Series;
    private String Number;
    private String IssueBranch;
    private Date IssueDate;

    public Passport(String series, String number) {
        Series = series;
        Number = number;
    }
}
