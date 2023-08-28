package tr.com.meteor.crm.utils.jasper.rest.errors;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.text.MessageFormat;

public class WrongFilterPathException extends AbstractThrowableProblem {

    private final String objectName;
    private final String path;
    private final String wrongPathPart;

    public WrongFilterPathException(String objectName, String path, String wrongPathPart) {
        super(null, "Filter is not valid.",
            Status.BAD_REQUEST, MessageFormat.format("''{0}'' not found on object ''{1}''. Full Path: ''{2}''", wrongPathPart, objectName, path),
            null, null, null);
        this.objectName = objectName;
        this.path = path;
        this.wrongPathPart = wrongPathPart;
    }
}
