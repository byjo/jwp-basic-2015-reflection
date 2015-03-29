package core.mvc;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;

public class MapperTest {
	private Mapper mapper;
	
	@Before
	public void setup() {
		mapper = new Mapper();
		mapper.init("core.mvc");
	}
	
	@Test
	public void findController() throws Exception {
//
//		String url = "test.next";
//		HandlerExecution he = mapper.getHandler()
//		rm.put(url, controller);
//		
//		Controller actual = rm.findController(url);
//		assertThat(actual, is(controller));
	}

}
