package tr.com.meteor.crm.aop.logging;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import tr.com.meteor.crm.security.SecurityUtils;
import tr.com.meteor.crm.service.BaseUserService;

@Aspect
@Component
public class UserIsActiveAspect {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final BaseUserService baseUserService;

    public UserIsActiveAspect(BaseUserService baseUserService) {
        this.baseUserService = baseUserService;
    }

    @Around("execution(* tr.com.meteor.crm.utils.jasper.rest*.*(..)) && !execution(* tr.com.meteor.crm.utils.jasper.rest.UserJWTController.authorize(..))")
    public Object applySoftDeleteFilter(ProceedingJoinPoint joinPoint) throws Throwable {
        if (!baseUserService.getUserFullFetched(SecurityUtils.getCurrentUserId().get()).get().getActivated()) {
            log.debug("Kullanıcı aktif değil! Username: " + SecurityUtils.getCurrentUserLogin().get());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Kullanıcı aktif değil!");
        }

        return joinPoint.proceed();
    }
}
