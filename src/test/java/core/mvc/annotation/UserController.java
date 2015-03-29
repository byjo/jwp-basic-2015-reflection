package core.mvc.annotation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import core.mvc.ModelAndView;
import core.utils.ServletRequestUtils;

@Controller
public class UserController {
	private UserDao userDao;
	
	@Inject
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
	
	@RequestMapping("/users/updateForm.next")
	public ModelAndView updateForm(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String userId = ServletRequestUtils.getRequiredStringParameter(request, "userId");
		User user = userDao.findById(userId);
		ModelAndView mav = new ModelAndView();
		mav.addObject("user", user);
		return mav;
	}
	
	@RequestMapping(value="/users/update.next", method=RequestMethod.POST)
	public ModelAndView update(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// update user
		return null;
	}
}
