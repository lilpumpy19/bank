package ru.shchegol.deal.entity.jsonb;


import lombok.*;
import ru.shchegol.dto.enums.ChangeType;


import javax.persistence.Embeddable;
import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class StatusHistory {
    private String status;
    private Timestamp time;
    private ChangeType changeType;
}
