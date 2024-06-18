package ru.shchegol.dto.anno;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = MinAgeValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MinAge {
    int value() default 0;
    String message() default "age must be greater than {value}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
