package tr.com.meteor.crm.utils.jasper.rest.errors;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.text.MessageFormat;

public class OperationAccessDeniedException extends AbstractThrowableProblem {

    private String operationId;

    public OperationAccessDeniedException(String operationId) {
        super(null, "Access Denied.",
            Status.FORBIDDEN, MessageFormat.format("You do not have operation to access this service. Required Operation Id: {0}.", operationId),
            null, null, null);
        this.operationId = operationId;
    }
}
