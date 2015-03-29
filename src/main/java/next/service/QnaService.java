package next.service;

import java.util.List;

import core.mvc.annotation.Bean;
import core.mvc.annotation.Inject;
import next.ExistedAnotherUserException;
import next.ResourceNotFoundException;
import next.dao.AnswerDao;
import next.dao.QuestionDao;
import next.model.Answer;
import next.model.Question;

@Bean
public class QnaService {
	private QuestionDao questionDao;
	private AnswerDao answerDao;

	@Inject
	public void setQnaService(QuestionDao questionDao, AnswerDao answerDao) {
		this.questionDao = questionDao;
		this.answerDao = answerDao;
	}

	public void delete(final long questionId) throws ResourceNotFoundException, ExistedAnotherUserException {
		Question question = questionDao.findWithAnswersById(questionId);
		
		if (!question.canDelete()) {
			throw new ExistedAnotherUserException("다른 사용자가 추가한 댓글이 존재해 삭제할 수 없습니다.");
		}

		questionDao.delete(questionId);
	}
	
	public Question findById(long questionId) {
		return questionDao.findById(questionId);
	}

	public List<Answer> findAnswersByQuestionId(long questionId) {
		return answerDao.findAllByQuestionId(questionId);
	}
}