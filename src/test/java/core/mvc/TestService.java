package core.mvc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import core.mvc.annotation.Bean;
import core.mvc.annotation.Inject;

@Bean
public class TestService {
	private static final Logger logger = LoggerFactory.getLogger(TestService.class);
	
	@Inject
	public void test(TestDao testDao) {
		logger.debug("T_T : {}", testDao.test);
	}
}
