package core.mvc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import core.mvc.annotation.Controller;
import core.mvc.annotation.RequestMapping;
import core.mvc.annotation.RequestMethod;

@Controller
public class TestController {
	private static final Logger logger = LoggerFactory.getLogger(TestService.class);
	
	@RequestMapping("/test.next")
	public void testUrl() {

	}

	@RequestMapping(value="/test.next", method=RequestMethod.DELETE)
	public void testUrlAndMethod() {
		
	}
	
	
}
