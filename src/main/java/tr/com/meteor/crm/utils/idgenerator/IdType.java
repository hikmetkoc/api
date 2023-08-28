package tr.com.meteor.crm.utils.idgenerator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface IdType {
    IdTypeEnum idType();

    String sequenceName() default "";

    enum IdTypeEnum {
        Long, Integer, String, UUID
    }
}
