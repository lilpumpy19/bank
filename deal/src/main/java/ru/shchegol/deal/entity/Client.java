package ru.shchegol.deal.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import ru.shchegol.deal.entity.enums.Gender;
import ru.shchegol.deal.entity.enums.MaritalStatus;
import ru.shchegol.deal.entity.jsonb.Employment;
import ru.shchegol.deal.entity.jsonb.Passport;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "client")
public class Client {


    @Id
    @Column(name = "client_id")
    @GeneratedValue(generator = "uuid-hibernate-generator")
    @GenericGenerator(name = "uuid-hibernate-generator", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Column(name = "last_name")
    private String LastName;

    @Column(name = "first_name")
    private String FirstName;

    @Column(name = "middle_name")
    private String MiddleName;

    @Column(name = "birth_date")
    private Date BirthDate;

    @Column(name = "email")
    private String Email;

    @Column(name="gender")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "marital_status")
    @Enumerated(EnumType.STRING)
    private MaritalStatus maritalStatus;

    @Column(name = "dependent_amount")
    private int dependentAmount;

    @Column(columnDefinition = "jsonb",name = "passport")
    @Embedded
    private Passport passport;

    @Column(columnDefinition = "jsonb", name = "employment")
    @Embedded
    private Employment employment;

    @Column(name = "account_number")
    private String AccountNumber;

    


}
