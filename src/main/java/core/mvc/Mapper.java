package core.mvc;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import core.mvc.annotation.Bean;
import core.mvc.annotation.Controller;
import core.mvc.annotation.Inject;
import core.mvc.annotation.RequestMapping;
import core.mvc.annotation.RequestMethod;

public class Mapper {
	private static final Logger logger = LoggerFactory.getLogger(Mapper.class);
	private Map<Class<?>, Object> beans = new HashMap<Class<?>, Object>();
	private Map<HandlerKey, HandlerExecution> rm = new HashMap<HandlerKey, HandlerExecution>();
	
	public void init(String... packageName) {	
		Reflections reflections = new Reflections(packageName, new MethodAnnotationsScanner(), new TypeAnnotationsScanner(), new SubTypesScanner());
		
		Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(Bean.class);
		Set<Class<?>> annotatedController = reflections.getTypesAnnotatedWith(Controller.class);
		Set<Method> resources = reflections.getMethodsAnnotatedWith(Inject.class);
		Set<Method> rmMethod = reflections.getMethodsAnnotatedWith(RequestMapping.class);
		
		for (Class<?> clazz : annotated) {
			try {
				beans.put(clazz, clazz.newInstance());
				logger.debug("class mapping : {}", clazz.getName());
			} catch (InstantiationException | IllegalAccessException e) {
				logger.error("class mapping error : {} {} ", e.getMessage(), e.getClass());
			}
		}
		for (Class<?> controller : annotatedController) {
			try {
				beans.put(controller, controller.newInstance());
				logger.debug("controller mapping : {}", controller.getName());
			} catch (InstantiationException | IllegalAccessException e) {
				logger.error("controller mapping error : {}", e.getMessage());
			}
		}
		logger.debug("beans mappings {}", beans.size());
		
		for (Method method : resources) {
			Parameter[] parameters = method.getParameters();
			List<Object> p = new ArrayList<Object>();
			for (Parameter parameter : parameters) {
				Object instance = beans.get(parameter.getType());
				
				if (instance == null) {
					Set<Class<?>> classSet = beans.keySet();
					for (Class<?> clazz : classSet) {
						Class<?>[] interfaces = clazz.getInterfaces();
						for (Class<?> interfacee : interfaces) {
							if (interfacee == parameter.getType())
								instance = beans.get(clazz);
						}
					}
				}
				logger.debug("{}", instance.getClass());
				p.add(instance);
			}
			try {
				method.invoke(beans.get(method.getDeclaringClass()), p.toArray());
				logger.debug("invoke method : {}, parameters: {}", method, p.toString());
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				logger.error(e.getMessage());
			}
		}
		System.out.println("beans size" + beans.size());
		
		for (Method method : rmMethod) {
			logger.debug("Method : {}, URI : {}", method.getAnnotation(RequestMapping.class).value(), method.getAnnotation(RequestMapping.class).method());
			rm.put(new HandlerKey(method.getAnnotation(RequestMapping.class).value(), method.getAnnotation(RequestMapping.class).method()), new HandlerExecution(beans.get(method.getDeclaringClass()), method));
		}
		System.out.println("rm size" + rm.size());
	}

	public HandlerExecution getHandler(HttpServletRequest request) {
		String url = urlExceptParameter(request.getRequestURI());
		RequestMethod method = RequestMethod.valueOf(request.getMethod());
		return rm.get(new HandlerKey(url, method));
	}
	
	String urlExceptParameter(String forwardUrl) {
		int index = forwardUrl.indexOf("?");
		if (index > 0) {
			return forwardUrl.substring(0, index);
		}
		
		return forwardUrl;
	}
	
}
