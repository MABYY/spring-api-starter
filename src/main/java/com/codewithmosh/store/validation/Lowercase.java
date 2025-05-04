package com.codewithmosh.store.validation;

import com.codewithmosh.store.dtos.LowercaseValidator;
import jakarta.persistence.Table;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = LowercaseValidator.class)
public @interface Lowercase {
    String message() default "Email must be lowecase";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
