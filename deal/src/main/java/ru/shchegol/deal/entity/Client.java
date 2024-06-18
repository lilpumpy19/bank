package ru.shchegol.deal.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import ru.shchegol.deal.entity.jsonb.Employment;
import ru.shchegol.deal.entity.jsonb.Passport;
import ru.shchegol.dto.enums.Gender;
import ru.shchegol.dto.enums.MaritalStatus;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "client")
@Builder
public class Client {


    @Id
    @Column(name = "client_id")
    @GeneratedValue(generator = "uuid-hibernate-generator")
    @GenericGenerator(name = "uuid-hibernate-generator", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "email")
    private String email;

    @Column(name="gender")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "marital_status")
    @Enumerated(EnumType.STRING)
    private MaritalStatus maritalStatus;

    @Column(name = "dependent_amount")
    private Integer dependentAmount;

    @Column(columnDefinition = "jsonb",name = "passport")
    @Embedded
    private Passport passport;

    @Column(columnDefinition = "jsonb", name = "employment")
    @Embedded
    private Employment employment;

    @Column(name = "account_number")
    private String accountNumber;

    


}
