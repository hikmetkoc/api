package tr.com.meteor.crm.utils.jasper.rest.errors;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.text.MessageFormat;

public class ObjectAccessDeniedException extends AbstractThrowableProblem {

    private String objectName;
    private AccessType accessType;

    public ObjectAccessDeniedException(AccessType accessType, String objectName) {
        super(null, "Hatalı İşlem.",
            Status.FORBIDDEN, MessageFormat.format("Bu işlem için yetkiniz yoktur. Bölüm: {0}. İşlem: {1}.", objectName, accessType),
            null, null, null);
        this.objectName = objectName;
        this.accessType = accessType;
    }

    public enum AccessType {
        READ, UPDATE, DELETE
    }
}
