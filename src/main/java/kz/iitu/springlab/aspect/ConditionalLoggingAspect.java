package kz.iitu.springlab.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import java.util.Arrays;

@Aspect
@Component
@Order(4)
public class ConditionalLoggingAspect {

    private static final Logger log = LoggerFactory.getLogger(ConditionalLoggingAspect.class);

    @Value("${app.aspect.verbose:false}")
    private boolean verbose;

    @Around("kz.iitu.springlab.aspect.Pointcuts.serviceOperation()")
    public Object logConditionally(ProceedingJoinPoint pjp) throws Throwable {
        String name = pjp.getSignature().toShortString();
        if (verbose) {
            log.info("[COND] -> {} args={}", name, Arrays.toString(pjp.getArgs()));
        }
        Object result = pjp.proceed();
        if (verbose) {
            log.info("[COND] <- {} result={}", name, result);
        } else {
            log.info("[COND] {} completed", name);
        }
        return result;
    }
}