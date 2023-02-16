package release.tracker.api.handlers.custom_annotation_validator;


import release.tracker.api.handlers.custom_annotation_validator.implementation.StringDateValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;

@Target({FIELD, PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = StringDateValidator.class)
public @interface DateStringValidation {
    public String message() default "Date is not in correct format";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
