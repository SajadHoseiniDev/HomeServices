package nycto.homeservices.util;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

import java.util.Set;
import java.util.stream.Collectors;

public class ValidationUtil {
    private final Validator validator;


    public ValidationUtil(Validator validator) {
        this.validator = validator;
    }

    public <T> boolean validate(T object) {
        Set<ConstraintViolation<T>> violations = validator.validate(object);
        if (violations.isEmpty()) {
            return true;
        } else {
            System.out.println("Invalid data found:");
            for (ConstraintViolation<T> violation : violations) {
                System.out.println(violation.getMessage());
            }
            return false;
        }
    }

}
