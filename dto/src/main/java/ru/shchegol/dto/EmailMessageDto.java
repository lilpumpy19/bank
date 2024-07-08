package ru.shchegol.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.shchegol.dto.enums.Theme;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EmailMessageDto {
    private String address;
    private Theme theme;
    private String statementId;
}
