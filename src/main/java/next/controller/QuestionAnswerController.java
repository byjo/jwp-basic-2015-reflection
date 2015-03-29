package next.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.media.jfxmedia.logging.Logger;

import next.ExistedAnotherUserException;
import next.ResourceNotFoundException;
import next.dao.AnswerDao;
import next.dao.QuestionDao;
import next.model.Answer;
import next.model.Question;
import next.service.QnaService;
import core.mvc.AbstractController;
import core.mvc.ModelAndView;
import core.mvc.annotation.Controller;
import core.mvc.annotation.Inject;
import core.mvc.annotation.RequestMapping;
import core.mvc.annotation.RequestMethod;
import core.utils.ServletRequestUtils;

@Controller
public class QuestionAnswerController extends AbstractController{
	private QuestionDao questionDao;
	private AnswerDao answerDao;
	private QnaService qnaService;

	@Inject
	public void setQuestionAnswerController(QuestionDao questionDao, AnswerDao answerDao, QnaService qnaService) {
		this.questionDao = questionDao;
		this.answerDao = answerDao;
		this.qnaService = qnaService;
		System.out.println("setQAController complete!");
		if (questionDao == null)
			System.out.println("no qeustionDao");
		if (answerDao == null)
			System.out.println("no answerDao");
	}
	
	@RequestMapping("/show.next")
	public ModelAndView show(HttpServletRequest request, HttpServletResponse response) throws Exception {
		long questionId = ServletRequestUtils.getRequiredLongParameter(request, "questionId");
		Question question = questionDao.findById(questionId);
		List<Answer> answers = answerDao.findAllByQuestionId(questionId);
		ModelAndView mav = jstlView("show.jsp");
		mav.addObject("question", question);
		mav.addObject("answers", answers);
		return mav;
	}
	
	@RequestMapping("/list.next")	
	public ModelAndView list(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		if (questionDao==null)
			System.out.println("T_T");
		List<Question> questions = questionDao.findAll();
		
		ModelAndView mav = jstlView("list.jsp");
		mav.addObject("questions", questions);
		return mav;
	}
	
	@RequestMapping("/form.next")
	public ModelAndView form(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return jstlView("form.jsp");
	}

	@RequestMapping(value="/save.next", method=RequestMethod.POST)
	public ModelAndView save(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String writer = ServletRequestUtils.getRequiredStringParameter(request, "writer");
		String title = ServletRequestUtils.getRequiredStringParameter(request, "title");
		String contents = ServletRequestUtils.getRequiredStringParameter(request, "contents");
		questionDao.insert(new Question(writer, title, contents));
		return jstlView("redirect:/");
	}
	
	@RequestMapping(value="/delete.next", method=RequestMethod.GET)
	public ModelAndView delete(HttpServletRequest request, HttpServletResponse response) throws Exception {
		long questionId = ServletRequestUtils.getRequiredLongParameter(request, "questionId");
		
		try {
			qnaService.delete(questionId);
			return jstlView("redirect:/list.next");
		} catch (ResourceNotFoundException|ExistedAnotherUserException ex) {
			ModelAndView mav = jstlView("show.jsp");
			mav.addObject("question", qnaService.findById(questionId));
			mav.addObject("answers", qnaService.findAnswersByQuestionId(questionId));
			mav.addObject("errorMessage", "다른 사용자가 추가한 댓글이 존재해 삭제할 수 없습니다.");
			return mav;
		}
	}
}
