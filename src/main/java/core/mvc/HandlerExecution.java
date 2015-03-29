package core.mvc;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HandlerExecution {
	private static final Logger logger = LoggerFactory.getLogger(HandlerExecution.class);
	private Object declaringClass;
	private Method method;
	
	public HandlerExecution(Object declaringClass, Method method) {
		this.declaringClass = declaringClass;
		this.method = method;
	}

	public ModelAndView execute(HttpServletRequest req, HttpServletResponse resp) {
		try {
			logger.debug("method : {} excute", method);
			return (ModelAndView) method.invoke(declaringClass, req, resp);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			logger.error(e.getMessage());
			throw new RuntimeException(e);
		}
	}

	public Object getDeclaringClass() {
		return declaringClass;
	}

	public Method getMethod() {
		return method;
	}
	
	
}
