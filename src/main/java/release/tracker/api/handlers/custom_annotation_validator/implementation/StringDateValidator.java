package release.tracker.api.handlers.custom_annotation_validator.implementation;

import release.tracker.api.handlers.custom_annotation_validator.DateStringValidation;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.validator.GenericValidator;

public class StringDateValidator implements ConstraintValidator<DateStringValidation, String> {

    @Override
    public void initialize(DateStringValidation constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String dateString, ConstraintValidatorContext constraintValidatorContext) {

        return GenericValidator.isDate(dateString, "yyyy-MM-dd", true);
    }
}
