package core.mvc;

public abstract class AbstractController {
	protected ModelAndView jstlView(String forwardUrl) {
		return new ModelAndView(new JstlView(forwardUrl));
	}
	
	protected ModelAndView jsonView() {
		return new ModelAndView(new JsonView());
	}	
}
