package ru.shchegol.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.shchegol.dto.enums.Theme;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EmailMassageDto {
    private String address;
    private Theme theme;
    private Long statementId;
}
