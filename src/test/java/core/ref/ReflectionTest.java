package core.ref;

import java.lang.reflect.*;

import next.model.Question;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReflectionTest {
	private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);
	
	@Test
	public void showClass() {
		Class<Question> clazz = Question.class;
		logger.debug(clazz.getName());
		
		Field[] fields = clazz.getFields();
		for (Field field : fields) {
			logger.debug(field.getName());
		}
		Constructor<?>[] constructors = clazz.getConstructors();
		for (Constructor<?> constructor : constructors) {
			logger.debug(constructor.getName());
		}
		Method[] methods = clazz.getMethods();
		for (Method method : methods) {
			logger.debug(method.getName());
		}
	}
}
