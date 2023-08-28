package tr.com.meteor.crm.utils.metadata;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface FieldMetadataAnn {
    String title() default "";

    String type() default "";

    boolean active() default true;

    boolean display() default false;

    boolean readOnly() default false;

    boolean required() default false;

    String defaultValue() default "";

    int priority() default Integer.MAX_VALUE;

    boolean search() default false;

    boolean filterable() default false;
}
