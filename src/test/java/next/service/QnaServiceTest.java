package next.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import next.ExistedAnotherUserException;
import next.ResourceNotFoundException;
import next.dao.MockAnswerDao;
import next.dao.MockQuestionDao;
import next.model.Answer;
import next.model.Question;

import org.junit.Before;
import org.junit.Test;

public class QnaServiceTest {
	private MockQuestionDao questionDao;
	private MockAnswerDao answerDao;

	private QnaService qnaService;

	@Before
	public void setup() {
		questionDao = new MockQuestionDao();
		answerDao = new MockAnswerDao();
		qnaService = new QnaService(questionDao, answerDao);
	}

	@Test(expected = ResourceNotFoundException.class)
	public void delete_질문_존재하지_않는_경우() throws Exception {
		qnaService.delete(1L);
	}
	
	@Test
	public void delete_답변_존재하지_않는_경우() throws Exception {
		// given
		long questionId = 1L;
		Question question = new Question(questionId, "writer", "title", "content", new Date(), 0);
		questionDao.insert(question);
		
		// when
		qnaService.delete(questionId);
		
		// then
		assertThat(questionDao.findById(questionId), is(nullValue()));
	}
	
	@Test
	public void delete_답변_존재_글쓴이_답변자_같음() throws Exception {
		// given
		long questionId = 1L;
		Question question = new Question(questionId, "writer", "title", "content", new Date(), 0);
		Answer answer1 = new Answer(2L, "writer", "answered", new Date(), questionId);
		Answer answer2 = new Answer(3L, "writer", "answered", new Date(), questionId);
		List<Answer> answers = Arrays.asList(answer1, answer2);
		question.setAnswers(answers);
		questionDao.insert(question);
		
		// when
		qnaService.delete(questionId);
		
		// then
		assertThat(questionDao.findById(questionId), is(nullValue()));
	}
	
	@Test(expected=ExistedAnotherUserException.class)
	public void delete_답변_존재_글쓰이_답변자_다름() throws Exception {
		// given
		long questionId = 1L;
		Question question = new Question(questionId, "writer", "title", "content", new Date(), 0);
		Answer answer1 = new Answer(2L, "another", "answered", new Date(), questionId);
		Answer answer2 = new Answer(3L, "writer", "answered", new Date(), questionId);
		List<Answer> answers = Arrays.asList(answer1, answer2);
		question.setAnswers(answers);
		questionDao.insert(question);

		// when
		qnaService.delete(questionId);
		
		// then
		assertThat(questionDao.findById(questionId), is(nullValue()));
	}
}
