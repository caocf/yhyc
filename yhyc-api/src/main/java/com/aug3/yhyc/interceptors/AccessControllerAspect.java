package com.aug3.yhyc.interceptors;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

import com.aug3.yhyc.interceptors.annotation.AccessToken;
import com.aug3.yhyc.interceptors.annotation.AccessTrace;

@Aspect
public class AccessControllerAspect {

	private static final Logger logger = Logger.getLogger(AccessControllerAspect.class);

	@Around("within(com.aug3.yhyc.services..*) && @annotation(traceAnnotation) && args(request,..)")
	public Object accessLog(final ProceedingJoinPoint pjp, final AccessTrace traceAnnotation, HttpServletRequest request)
			throws Throwable {

		String ip = null;
		if (request != null) {
			ip = request.getRemoteAddr();
		}

		long start = System.currentTimeMillis();
		Object retVal = null;
		try {
			retVal = pjp.proceed();
		} catch (Throwable t) {
			System.out.println("error occured");
			throw t;
		}
		long end = System.currentTimeMillis();

		Method targetMethod = ((MethodSignature) pjp.getSignature()).getMethod();

		logger.info("ip:[ " + ip + " ] called API:[" + targetMethod.getDeclaringClass().getName() + "."
				+ targetMethod.getName() + "] takes:[" + (end - start) + "] ms, with param:"
				+ request.getParameterMap().toString());

		return retVal;

	}

	@Around("within(com.aug3.yhyc.services..*) && @annotation(tokenAnnotation) && args(request,token,..)")
	public Object accessToken(final ProceedingJoinPoint pjp, final AccessToken tokenAnnotation,
			HttpServletRequest request, String token) throws Throwable {

		return null;

	}

}
