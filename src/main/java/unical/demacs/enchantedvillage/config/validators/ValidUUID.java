package unical.demacs.enchantedvillage.config.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.hibernate.validator.internal.constraintvalidators.hv.UUIDValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)

@Constraint(validatedBy = UUIDValidator.class)
public @interface ValidUUID {
    String message() default "UUID not valid";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}