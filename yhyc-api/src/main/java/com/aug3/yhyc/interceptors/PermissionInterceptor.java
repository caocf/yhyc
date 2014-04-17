package com.aug3.yhyc.interceptors;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;

/**
 * check permission for every export option
 */
public class PermissionInterceptor extends AbstractPhaseInterceptor<Message> {

	private FilterConfig filterConfig;

	public PermissionInterceptor(String phase) {
		super(phase);
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
			ServletException {

		String needFilter = filterConfig.getInitParameter("needfilter");
		if (!"true".equals(needFilter)) {
			chain.doFilter(request, response);
			return;
		}

	}

	@Override
	public void handleMessage(Message message) throws Fault {
		// To change body of implemented methods use File | Settings | File
		// Templates.
	}
}
