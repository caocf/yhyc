package com.aug3.yhyc.interceptors;

import java.lang.reflect.Method;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

import com.aug3.sys.rs.response.RespObj;
import com.aug3.sys.rs.response.RespType;
import com.aug3.sys.util.JSONUtil;
import com.aug3.yhyc.dao.UserDao;
import com.aug3.yhyc.interceptors.annotation.AccessToken;
import com.aug3.yhyc.interceptors.annotation.AccessTrace;

@Aspect
public class AccessControllerAspect {

	private static final Logger logger = Logger
			.getLogger(AccessControllerAspect.class);

	@Around("within(com.aug3.yhyc.service..*) && @annotation(traceAnnotation) && args(request,..)")
	public Object accessLog(final ProceedingJoinPoint pjp,
			final AccessTrace traceAnnotation, HttpServletRequest request)
			throws Throwable {

		String ip = null;
		String uuid = null;
		String ver = null;
		if (request != null) {
			ip = request.getHeader("X-Forwarded-For");
			if (ip == null)
				ip = request.getRemoteAddr();
			uuid = request.getHeader("uuid");
			ver = request.getHeader("ver");
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

		Method targetMethod = ((MethodSignature) pjp.getSignature())
				.getMethod();

		logger.info(new StringBuilder("ip:[").append(ip).append("][")
				.append(uuid).append("][").append(ver).append("] called:[")
				.append(targetMethod.getDeclaringClass().getSimpleName())
				.append(".").append(targetMethod.getName()).append("] takes:[")
				.append(end - start).append("]ms, params:[")
				.append(getParameters(request)).append("]").toString());

		return retVal;

	}

	private String getParameters(HttpServletRequest request) {
		StringBuilder sb = new StringBuilder();
		Enumeration<String> keys = request.getParameterNames();
		if (keys != null) {
			String key;
			boolean first = true;
			while (keys.hasMoreElements()) {
				if (first) {
					first = false;
				} else {
					sb.append(",");
				}

				key = keys.nextElement();
				sb.append(key).append("=>").append(request.getParameter(key));
			}
		}
		return sb.toString();
	}

	@Around("within(com.aug3.yhyc.service..*) && @annotation(tokenAnnotation) && args(request,token,..)")
	public Object accessToken(final ProceedingJoinPoint pjp,
			final AccessToken tokenAnnotation, HttpServletRequest request,
			String token) throws Throwable {

		boolean isValid = UserDao.isValidToken(token);

		if (!isValid) {
			RespObj returnDTO = new RespObj();
			returnDTO.setCode(RespType.NO_PERMISSION.getCode());
			returnDTO.setType("Invalid User!");
			returnDTO.setMessage("ERR: " + RespType.NO_PERMISSION);
			return JSONUtil.toJson(returnDTO);
		}

		try {
			return pjp.proceed();
		} catch (Throwable t) {
			System.out.println("error occured");
			throw t;
		}

	}

}
