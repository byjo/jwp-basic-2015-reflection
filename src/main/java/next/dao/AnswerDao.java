package next.dao;

import java.util.List;

import core.jdbc.JdbcTemplate;
import next.model.Answer;

public interface AnswerDao {
	
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate);
	
	void insert(Answer answer);

	List<Answer> findAllByQuestionId(long questionId);

	void delete(long questionId);

}