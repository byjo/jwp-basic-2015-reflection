package next.model;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Test;

public class QuestionTest {
	@Test
	public void canDelete_답변_존재하지_않는_경우() {
		// given
		long questionId = 1L;
		Question question = new Question(questionId, "writer", "title", "content", new Date(), 0);

		// when
		assertThat(question.canDelete(), is(true));
	}
	
	@Test
	public void canDelete_답변_존재_글쓴이_답변자_같음() throws Exception {
		// given
		long questionId = 1L;
		Answer answer1 = new Answer(2L, "writer", "answered", new Date(), questionId);
		Answer answer2 = new Answer(3L, "writer", "answered", new Date(), questionId);
		List<Answer> answers = Arrays.asList(answer1, answer2);
		Question question = new Question(questionId, "writer", "title", "content", new Date(), 0);
		question.setAnswers(answers);
		
		// when
		assertThat(question.canDelete(), is(true));
	}

	@Test
	public void canDelete_답변_존재_글쓴이_답변자_다름() throws Exception {
		// given
		long questionId = 1L;
		Answer answer1 = new Answer(2L, "another", "answered", new Date(), questionId);
		Answer answer2 = new Answer(3L, "writer", "answered", new Date(), questionId);
		List<Answer> answers = Arrays.asList(answer1, answer2);
		Question question = new Question(questionId, "writer", "title", "content", new Date(), 0);
		question.setAnswers(answers);
		
		// when
		assertThat(question.canDelete(), is(false));
	}
}
