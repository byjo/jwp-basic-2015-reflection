package next.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import next.ResourceNotFoundException;
import next.model.Question;

public class MockQuestionDao implements QuestionDao {
	private Map<Long, Question> questions = new HashMap<Long, Question>();

	@Override
	public void insert(Question question) {
		questions.put(question.getQuestionId(), question);
	}

	@Override
	public List<Question> findAll() {
		return new ArrayList<Question>(questions.values());
	}

	@Override
	public Question findById(long questionId) {
		return questions.get(questionId);
	}

	@Override
	public void updateCommentCount(long questionId) {
	}

	@Override
	public void delete(long questionId) {
		questions.remove(questionId);
	}

	@Override
	public Question findWithAnswersById(long questionId) throws ResourceNotFoundException {
		Question question = findById(questionId);
		if (question == null) {
			throw new ResourceNotFoundException("존재하지 않는 질문입니다.");
		}
		return question;
	}
}
