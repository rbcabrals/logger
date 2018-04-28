/**
 * 
 */
package io.rlogger.handlers;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author rcabral
 *
 */
@Aspect
public class LoggingHandler {

	@Pointcut("within(@io.rlogger.annotations.RLogger *)")
	public void logger() {
	}

	@Pointcut("execution(* *.*(..))")
	protected void loggerAllMethod() {
	}

	@Pointcut("execution(public * *(..))")
	protected void loggerPublicMethod() {
	}

	@Before("logger() && loggerPublicMethod()")
	public void loggerAnyResourceBeforeOperation(final JoinPoint joinPoint) {
		final Signature signature = joinPoint.getSignature();
		final Logger logger = LoggerFactory.getLogger(signature.getDeclaringType());
		logger.info("method={}, params={}", signature.getName(), getParams(joinPoint.getArgs()));
	}

	private String getParams(final Object[] params) {
		final StringBuilder builder = new StringBuilder();
		for (final Object param : params) {
			builder.append(toString(param));
			builder.append(", ");
		}

		return builder.toString();
	}

	@AfterReturning(pointcut = "logger() && loggerPublicMethod()", returning = "result")
	public void loggerAnyResourceAfterOperation(JoinPoint joinPoint, Object result) {
		final Signature signature = joinPoint.getSignature();
		final Logger logger = LoggerFactory.getLogger(signature.getDeclaringType());

		logger.info("method={}, return={}", signature.getName(), toString(result));
	}

	@AfterThrowing(pointcut = "logger() && loggerPublicMethod()", throwing = "exception")
	public void loggerAfterThrowable(JoinPoint joinPoint, Throwable exception) {
		final Signature signature = joinPoint.getSignature();
		final Logger logger = LoggerFactory.getLogger(signature.getDeclaringType());

		logger.info("method={}, cause={}", signature.getName(), toString(exception.getCause()));
	}

	@Around("logger() && loggerPublicMethod()")
	public Object getProceed(ProceedingJoinPoint joinPoint) throws Throwable {
		final Signature signature = joinPoint.getSignature();
		final Logger logger = LoggerFactory.getLogger(signature.getDeclaringType());

		final Long timeOfStart = System.currentTimeMillis();

		Object result = joinPoint.proceed();
		final Long timeOfEnd = System.currentTimeMillis() - timeOfStart;
		logger.info("method={}, executionTime={} in ms", signature.getName(), timeOfEnd);

		return result;
	}

	private String toString(final Object value) {
		if (value == null) {
			return StringUtils.EMPTY;
		}

		if (hasReflection(value)) {
			return ReflectionToStringBuilder.toString(value, ToStringStyle.SHORT_PREFIX_STYLE);
		}

		return value.toString();
	}

	private static Boolean hasReflection(final Object value) {
		return value.toString().endsWith("@" + Integer.toHexString(value.hashCode()));
	}
}