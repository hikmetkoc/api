package tr.com.meteor.crm.utils.jasper.rest.errors;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.text.MessageFormat;

public class RecordNotFoundException extends AbstractThrowableProblem {

    private String objectName;
    private Object recordId;

    public RecordNotFoundException(String objectName, Object recordId) {
        super(null, "Record not found.",
            Status.NOT_FOUND, MessageFormat.format("Record not found. Entity: {0}. Record Id: {1}.", objectName, recordId),
            null, null, null);
        this.objectName = objectName;
        this.recordId = recordId;
    }
}
