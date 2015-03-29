package core.mvc;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

public class DispatcherServletTest {
	private DispatcherServlet dispatcherServlet;
	
	@Before
	public void setup() {
		dispatcherServlet = new DispatcherServlet();
	}

	@Test
	public void urlExceptParameter() throws Exception {
//		assertThat(dispatcherServlet.urlExceptParameter("/show.next"), is("/show.next"));
//		assertThat(dispatcherServlet.urlExceptParameter("/show.next?id=2"), is("/show.next"));
	}
}
