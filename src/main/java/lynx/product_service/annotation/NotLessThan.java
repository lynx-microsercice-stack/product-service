package lynx.product_service.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import lynx.product_service.validator.NotLessThanValidator;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NotLessThanValidator.class)
public @interface NotLessThan {
    String value() default "";
    
    String message() default "Value must not be less than the reference field";
    
    Class<?>[] groups() default {};
    
    Class<? extends Payload>[] payload() default {};
}
