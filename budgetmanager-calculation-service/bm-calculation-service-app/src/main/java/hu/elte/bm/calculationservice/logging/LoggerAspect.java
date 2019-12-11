package hu.elte.bm.calculationservice.logging;

import java.text.MessageFormat;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;

import hu.elte.bm.calculationservice.exceptions.schema.StatisticsSchemaException;
import hu.elte.bm.calculationservice.exceptions.schema.StatisticsSchemaNotFoundException;
import hu.elte.bm.transactionservice.exceptions.maincategory.MainCategoryException;

@Aspect
@Component
public class LoggerAspect {

    private static final String ARGUMENT_SEPARATOR = ", ";
    private static final String PATTERN = "{0}: {1}";
    private static final String ERROR_PATTERN = "Error message: {0} Called end point: {1} Input object: {2}";
    private static final String STACK_TRACE = "STACK TRACE:";
    private static final String END_LINE = "\n";
    private static final String TAB = "  ";

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggerAspect.class);

    @Value("${logging.level.hu.elte.bm.calculationservice:ERROR}")
    private String loggingLevel;

    @Value("${logging.logging_with_stack:false}")
    private Boolean loggingWithStackTrace;

    @Pointcut("execution(* hu.elte.bm.calculationservice.dal..*Repository.*(..))")
    public void repositoryClassMethods() {
    }

    @Pointcut("execution(* hu.elte.bm.calculationservice.web..*Controller.*(..))")
    public void controllerClassMethods() {
    }

    @Pointcut("execution(* hu.elte.bm.calculationservice.web.errorhandling.ControllerAdvice.*(..))")
    public void controllerAdviceClassMethods() {
    }

    @Before("repositoryClassMethods()")
    public void logRepositoryCalls(final JoinPoint joinPoint) {
        logInfoWithParameters(joinPoint);
    }

    @Before("controllerClassMethods()")
    public void logControllerCalls(final JoinPoint joinPoint) {
        logInfoWithParameters(joinPoint);
    }

    @AfterReturning(value = "controllerClassMethods()", returning = "returnValue")
    public void logControllerReturns(final JoinPoint joinPoint, final Object returnValue) {
        logInfoWithReturnValue(joinPoint, returnValue);
    }

    @Before("controllerAdviceClassMethods()")
    public void logControllerAdviceCalls(final JoinPoint joinPoint) {
        logError(joinPoint);
    }

    private void logInfoWithParameters(final JoinPoint joinPoint) {
        if ("INFO".equals(loggingLevel)) {
            LOGGER.info(createInfoLog(joinPoint.getSignature().toShortString(), joinPoint.getArgs()));
        }
    }

    private void logInfoWithReturnValue(final JoinPoint joinPoint, final Object result) {
        if ("INFO".equals(loggingLevel)) {
            LOGGER.info(MessageFormat.format(PATTERN, joinPoint.getSignature().toShortString(), result));
        }
    }

    private void logError(final JoinPoint joinPoint) {
        LOGGER.error(createErrorLog(joinPoint.getArgs()));
    }

    private String createInfoLog(final String shortName, final Object[] args) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Object arg : args) {
            if (arg != null) {
                stringBuilder.append(arg.toString());
            } else {
                stringBuilder.append("null");
            }
            if (arg != args[args.length - 1]) {
                stringBuilder.append(ARGUMENT_SEPARATOR);
            }
        }
        return MessageFormat.format(PATTERN, shortName, stringBuilder.toString());
    }

    private String createErrorLog(final Object[] args) {
        Object e = args[0];
        Object target;
        target = e;
        if (e instanceof StatisticsSchemaException) {
            target = getTargetFromSchemaException(e);
        } else if (e instanceof MainCategoryException) {
            MainCategoryException exception = (MainCategoryException) e;
            target = exception.getMainCategory();
        }
        Object servletWebRequest = args[1];
        return createExceptionLog(e, target, servletWebRequest);
    }

    private Object getTargetFromSchemaException(final Object e) {
        Object target;
        if (e instanceof StatisticsSchemaNotFoundException) {
            StatisticsSchemaNotFoundException exception = (StatisticsSchemaNotFoundException) e;
            if (exception.getSchema() != null) {
                target = exception.getSchema();
            } else if (exception.getSchemaId() != null) {
                target = "schemaId=" + exception.getSchemaId();
            } else {
                target = "schemaTitle=" + exception.getSchemaTitle();
            }
        } else {
            StatisticsSchemaException exception = (StatisticsSchemaException) e;
            target = exception.getSchema();
        }
        return target;
    }

    private String createExceptionLog(final Object e, final Object target, final Object servletWebRequest) {
        String log;
        if (e instanceof RuntimeException && servletWebRequest instanceof ServletWebRequest && target != null) {
            RuntimeException exception = (RuntimeException) e;
            HttpServletRequest request = ((ServletWebRequest) servletWebRequest).getRequest();
            StringBuilder builder = new StringBuilder(MessageFormat.format(ERROR_PATTERN, exception.getMessage(), request.getRequestURI(), target.toString()));
            if (loggingWithStackTrace) {
                logStackTrace(exception, builder);
            }
            log = builder.toString();
        } else {
            log = e.toString() + " " + servletWebRequest.toString();
        }
        return log;
    }

    private void logStackTrace(final RuntimeException exception, final StringBuilder builder) {
        builder.append(END_LINE)
            .append(TAB)
            .append(STACK_TRACE);
        for (StackTraceElement stackTraceElement : exception.getStackTrace()) {
            builder.append(END_LINE)
                .append(TAB)
                .append(TAB)
                .append(stackTraceElement);
        }
    }

}
