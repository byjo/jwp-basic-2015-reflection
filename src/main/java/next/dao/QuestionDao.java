package next.dao;

import java.util.List;

import core.jdbc.JdbcTemplate;
import next.ResourceNotFoundException;
import next.model.Question;

public interface QuestionDao {

	void setJdbcTemplate(JdbcTemplate jdbcTemplate);
	
	void setJdbcAnswerDao(AnswerDao answerDao);
	
	void insert(Question question);

	List<Question> findAll();

	Question findById(long questionId);
	
	Question findWithAnswersById(long questionId) throws ResourceNotFoundException;
	
	void updateCommentCount(long questionId);

	void delete(long questionId);

}