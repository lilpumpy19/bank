package ru.shchegol.deal.entity.jsonb;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
//import org.hibernate.annotations.TypeDef;
import ru.shchegol.deal.entity.enums.ChangeType;


import javax.persistence.Embeddable;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class StatusHistory {
    private String status;
    private Timestamp time;
    private ChangeType changeType;
}
