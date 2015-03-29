package next.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import next.ExistedAnotherUserException;
import next.ResourceNotFoundException;
import next.dao.AnswerDao;
import next.dao.QuestionDao;
import next.model.Answer;
import next.model.Result;
import next.service.QnaService;
import core.mvc.AbstractController;
import core.mvc.ModelAndView;
import core.mvc.annotation.Controller;
import core.mvc.annotation.Inject;
import core.mvc.annotation.RequestMapping;
import core.mvc.annotation.RequestMethod;
import core.utils.ServletRequestUtils;

@Controller
public class ApiQnaController extends AbstractController{
	private QuestionDao questionDao;
	private AnswerDao answerDao;
	private QnaService qnaService;
	
	@Inject
	public void setApiQnaController(QuestionDao questionDao, AnswerDao answerDao, QnaService qnaService) {
		this.questionDao = questionDao;
		this.answerDao = answerDao;
		this.qnaService = qnaService;
	}
	
	@RequestMapping("/api/list.next")
	public ModelAndView list(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView mav = jsonView();
		mav.addObject("questions", questionDao.findAll());
		return mav;
	}

	@RequestMapping(value="/api/addanswer.next", method=RequestMethod.POST)
	public ModelAndView add(HttpServletRequest request, HttpServletResponse response) throws Exception {
		long questionId = ServletRequestUtils.getRequiredLongParameter(request, "questionId");
		String writer = ServletRequestUtils.getRequiredStringParameter(request, "writer");
		String contents = ServletRequestUtils.getRequiredStringParameter(request, "contents");
		Answer answer = new Answer(writer, contents, questionId);
		answerDao.insert(answer);
		questionDao.updateCommentCount(questionId);
		return jsonView();
	}
	
	@RequestMapping(value="/api/delete.next", method=RequestMethod.GET)
	public ModelAndView delete(HttpServletRequest request, HttpServletResponse response) throws Exception {
		long questionId = ServletRequestUtils.getRequiredLongParameter(request, "questionId");
		
		ModelAndView mav = jsonView();
		try {
			qnaService.delete(questionId);
			mav.addObject("result", Result.ok());
		} catch (ResourceNotFoundException | ExistedAnotherUserException ex) {
			mav.addObject("result", Result.fail(ex.getMessage()));
		}
		
		return mav;
	}
}
