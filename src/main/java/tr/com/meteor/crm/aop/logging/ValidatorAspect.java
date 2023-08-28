package tr.com.meteor.crm.aop.logging;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;
import tr.com.meteor.crm.utils.jasper.rest.errors.ErrorConstants;
import tr.com.meteor.crm.utils.jasper.rest.errors.FieldErrorVM;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Aspect
public class ValidatorAspect {

    private final Validator validator;

    public ValidatorAspect(Validator validator) {
        this.validator = validator;
    }

    @Around("execution(* tr.com.meteor.crm.repository.*.save(..)) && args(entity)")
    public Object validate(ProceedingJoinPoint joinPoint, Object entity) throws Throwable {
        standardValidate(entity);

        return joinPoint.proceed();
    }

    private void standardValidate(Object entity) {
        Set<ConstraintViolation<Object>> errors = validator.validate(entity);

        if (errors.size() > 0) {
            List<FieldErrorVM> fieldErrors = new ArrayList<>();

            for (ConstraintViolation<Object> violation : errors) {
                fieldErrors.add(new FieldErrorVM(entity.getClass().getSimpleName(), violation.getPropertyPath().toString(), violation.getMessage()));
            }

            throw Problem.builder()
                .withType(ErrorConstants.CONSTRAINT_VIOLATION_TYPE)
                .withTitle("Field validation error!")
                .withStatus(Status.BAD_REQUEST)
                .with("message", "Field validation error!")
                .with("fieldErrors", fieldErrors)
                .build();
        }
    }
}


