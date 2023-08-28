package tr.com.meteor.crm.utils.metadata;

import tr.com.meteor.crm.utils.request.SortOrder;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface EntityMetadataAnn {
    String apiName();

    String displayField();

    String masterPath() default "";

    String segmentPath() default "";

    int size() default 20;

    String sortBy() default "id";

    SortOrder sortOrder() default SortOrder.ASC;

    String title();

    String pluralTitle();

    String ownerPath() default "createdBy.id";

    String assignerPath() default "createdBy.id";

    String secondAssignerPath() default "createdBy.id";

    String otherPath() default "createdBy.id";
}
