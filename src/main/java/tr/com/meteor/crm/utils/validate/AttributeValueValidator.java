package tr.com.meteor.crm.utils.validate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import tr.com.meteor.crm.domain.AttributeValue;
import tr.com.meteor.crm.service.AttributeValueService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.MessageFormat;
import java.util.Collection;

public class AttributeValueValidator implements ConstraintValidator<AttributeValueValidate, Object> {

    private final Logger log = LoggerFactory.getLogger(AttributeValueValidator.class);

    private AttributeValueValidate attributeValueValidate;

    @Autowired
    private AttributeValueService attributeValueService;

    @Override
    public void initialize(AttributeValueValidate attributeValueValidate) {
        this.attributeValueValidate = attributeValueValidate;
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        if (o == null) return true;

        if (o instanceof AttributeValue) {
            AttributeValue attributeValue = (AttributeValue) o;

            return isAttributeValueValid(attributeValue);
        } else if (o instanceof Collection) {
            Collection collection = (Collection) o;

            if (collection.isEmpty()) return true;

            for (Object obj : collection) {
                if (!(obj instanceof AttributeValue)) {
                    log.error(MessageFormat.format("AttributeValueValidator only supports Attribute Value or " +
                            "Attribute Value Collection! Annotated Class: {0}. Annotated Field: {1}",
                        obj.getClass().getName()));
                    return false;
                }

                AttributeValue attributeValue = (AttributeValue) obj;

                if (!isAttributeValueValid(attributeValue)) {
                    return false;
                }
            }

            return true;
        } else {
            log.error(MessageFormat.format("AttributeValueValidator only supports Attribute Value or " +
                    "Attribute Value Collection! Annotated Class: {0}. Annotated Field: {1}",
                o.getClass().getName()));

            return false;
        }
    }

    private boolean isAttributeValueValid(AttributeValue attributeValue) {
        return attributeValue.getId() != null
            && attributeValue.getId().startsWith(attributeValueValidate.attributeId())
            && attributeValueService.getAll()
            .stream()
            .anyMatch(av -> av.getId().equals(attributeValue.getId())
                && av.getAttribute().getId().equals(attributeValueValidate.attributeId()));
    }
}
