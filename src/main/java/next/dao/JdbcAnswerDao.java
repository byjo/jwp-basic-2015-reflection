package next.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import next.model.Answer;
import core.jdbc.JdbcTemplate;
import core.jdbc.RowMapper;
import core.mvc.annotation.Bean;
import core.mvc.annotation.Inject;

@Bean
public class JdbcAnswerDao implements AnswerDao {
	private JdbcTemplate jdbcTemplate;
	
	@Inject
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	
	/* (non-Javadoc)
	 * @see next.dao.IAnswerDao#insert(next.model.Answer)
	 */
	@Override
	public void insert(Answer answer) {
		String sql = "INSERT INTO ANSWERS (writer, contents, createdDate, questionId) VALUES (?, ?, ?, ?)";
		jdbcTemplate.update(sql, answer.getWriter(),
				answer.getContents(),
				new Timestamp(answer.getTimeFromCreateDate()),
				answer.getQuestionId());
	}

	/* (non-Javadoc)
	 * @see next.dao.IAnswerDao#findAllByQuestionId(long)
	 */
	@Override
	public List<Answer> findAllByQuestionId(long questionId) {
		String sql = "SELECT answerId, writer, contents, createdDate FROM ANSWERS WHERE questionId = ? "
				+ "order by answerId desc";
		
		RowMapper<Answer> rm = new RowMapper<Answer>() {
			@Override
			public Answer mapRow(ResultSet rs) throws SQLException {
				return new Answer(
						rs.getLong("answerId"),
						rs.getString("writer"), 
						rs.getString("contents"),
						rs.getTimestamp("createdDate"), 
						questionId);
			}
		};
		
		return jdbcTemplate.query(sql, rm, questionId);
	}

	@Override
	public void delete(long questionId) {
		String sql = "DELETE FROM QUESTIONS WHERE questionId = ?";
		jdbcTemplate.update(sql, questionId);
	}
}
