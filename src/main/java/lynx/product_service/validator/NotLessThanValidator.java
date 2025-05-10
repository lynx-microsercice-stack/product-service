package lynx.product_service.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lynx.product_service.annotation.NotLessThan;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;

public class NotLessThanValidator implements ConstraintValidator<NotLessThan, Number> {
    
    private String referenceFieldName;
    
    @Override
    public void initialize(NotLessThan constraintAnnotation) {
        this.referenceFieldName = constraintAnnotation.value();
    }
    
    @Override
    public boolean isValid(Number value, ConstraintValidatorContext context) {
        // Null values are considered valid (use @NotNull for null validation)
        if (value == null) {
            return true;
        }
        
        try {
            // Get the object being validated
            Object object = context.unwrap(Object.class);
            if (object == null) {
                return true; // Can't validate without context
            }
            
            // Use BeanWrapper for safer property access
            BeanWrapper wrapper = PropertyAccessorFactory.forBeanPropertyAccess(object);
            if (!wrapper.isReadableProperty(referenceFieldName)) {
                return true; // Property doesn't exist or isn't readable
            }
            
            // Get the value of the reference field
            Object referenceValue = wrapper.getPropertyValue(referenceFieldName);
            if (referenceValue == null) {
                return true; // Reference is null, can't compare
            }
            
            if (!(referenceValue instanceof Number)) {
                return false; // Reference is not a number
            }
            
            // Compare the values
            return value.doubleValue() >= ((Number) referenceValue).doubleValue();
            
        } catch (Exception e) {
            // If any errors occur, return true to avoid blocking validation
            return true;
        }
    }
} 