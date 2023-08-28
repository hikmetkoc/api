package tr.com.meteor.crm.utils.validate;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = AttributeValueValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AttributeValueValidate {

    String message() default "";
    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
    String attributeId();
}
