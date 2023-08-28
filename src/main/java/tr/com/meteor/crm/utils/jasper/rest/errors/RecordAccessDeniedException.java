package tr.com.meteor.crm.utils.jasper.rest.errors;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.text.MessageFormat;

public class RecordAccessDeniedException extends AbstractThrowableProblem {

    private String objectName;
    private AccessType accessType;
    private Object id;

    public RecordAccessDeniedException(AccessType accessType, String objectName, Object id) {
        super(null, "Access Denied.",
            Status.FORBIDDEN, MessageFormat.format("You do not have permission to access the record. Entity: {0}. Id: {1}. AccessType: {2}.", objectName, id, accessType),
            null, null, null);
        this.id = id;
        this.objectName = objectName;
        this.accessType = accessType;
    }

    public enum AccessType {
        READ, UPDATE, DELETE
    }
}
