package ru.shchegol.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.shchegol.dto.enums.Topic;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EmailMessageDto {
    private String address;
    private Topic topic;
    private String statementId;
}
