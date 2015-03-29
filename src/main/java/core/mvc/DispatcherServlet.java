package core.mvc;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet(name="dispatcher", urlPatterns="*.next", loadOnStartup=1)
public class DispatcherServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);
	
	private Mapper mapper;

	@Override
	public void init() throws ServletException {
		mapper = new Mapper();
		mapper.init("next", "core.jdbc");
	}

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		HandlerExecution handlerExecution = mapper.getHandler(req);
		logger.debug("Method : {}, Request URI : {}", req.getMethod(), req.getRequestURI());
		
		if (handlerExecution == null) {
			throw new ServletException(String.format("%s uri에 해당하는 Controller를 찾을 수 없다.", req.getRequestURI()));
		}

		logger.debug("handlerExecution | class : {}, Method : {}", handlerExecution.getDeclaringClass(), handlerExecution.getMethod());
		ModelAndView mav;
		try {
			mav = handlerExecution.execute(req, resp);
			View view = mav.getView();
			view.render(mav.getModel(), req, resp);
		} catch (Throwable e) {
			logger.error("Exception : {}", e);
			throw new ServletException(e.getMessage());
		}
	}
}
