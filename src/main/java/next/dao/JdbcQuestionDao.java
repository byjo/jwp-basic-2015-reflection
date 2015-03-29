package next.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import next.ResourceNotFoundException;
import next.model.Question;
import core.jdbc.JdbcTemplate;
import core.jdbc.RowMapper;
import core.mvc.annotation.Bean;
import core.mvc.annotation.Inject;

@Bean
public class JdbcQuestionDao implements QuestionDao {
	private JdbcTemplate jdbcTemplate;
	private AnswerDao answerDao;
	
	@Inject
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	@Inject
	public void setJdbcAnswerDao(AnswerDao answerDao) {
		this.answerDao = answerDao;
	}
	
	/* (non-Javadoc)
	 * @see next.dao.QDao#insert(next.model.Question)
	 */
	@Override
	public void insert(Question question) {
		String sql = "INSERT INTO QUESTIONS (writer, title, contents, createdDate, countOfComment) VALUES (?, ?, ?, ?, ?)";
		jdbcTemplate.update(sql, 
				question.getWriter(), 
				question.getTitle(), 
				question.getContents(),
				new Timestamp(question.getTimeFromCreateDate()), 
				question.getCountOfComment());
	}
	
	/* (non-Javadoc)
	 * @see next.dao.QDao#findAll()
	 */
	@Override
	public List<Question> findAll() {
		String sql = "SELECT questionId, writer, title, createdDate, countOfComment FROM QUESTIONS "
				+ "order by questionId desc";
		
		RowMapper<Question> rm = new RowMapper<Question>() {
			@Override
			public Question mapRow(ResultSet rs) throws SQLException {
				return new Question(rs.getLong("questionId"),
						rs.getString("writer"), rs.getString("title"), null,
						rs.getTimestamp("createdDate"),
						rs.getInt("countOfComment"));
			}
			
		};
		
		return jdbcTemplate.query(sql, rm);
	}

	/* (non-Javadoc)
	 * @see next.dao.QDao#findById(long)
	 */
	@Override
	public Question findById(long questionId) {
		String sql = "SELECT questionId, writer, title, contents, createdDate, countOfComment FROM QUESTIONS "
				+ "WHERE questionId = ?";
		
		RowMapper<Question> rm = new RowMapper<Question>() {
			@Override
			public Question mapRow(ResultSet rs) throws SQLException {
				return new Question(rs.getLong("questionId"),
						rs.getString("writer"), rs.getString("title"),
						rs.getString("contents"),
						rs.getTimestamp("createdDate"),
						rs.getInt("countOfComment"));
			}
			
		};
		
		return jdbcTemplate.queryForObject(sql, rm, questionId);
	}

	/* (non-Javadoc)
	 * @see next.dao.QDao#updateCommentCount(long)
	 */
	@Override
	public void updateCommentCount(long questionId) {
		String sql = "UPDATE QUESTIONS set countOfComment = countOfComment + 1 WHERE questionId = ?";
		jdbcTemplate.update(sql, questionId);
	}

	/* (non-Javadoc)
	 * @see next.dao.QDao#delete(long)
	 */
	@Override
	public void delete(long questionId) {
		answerDao.delete(questionId);
		
		String sql = "DELETE FROM QUESTIONS WHERE questionId = ?";
		jdbcTemplate.update(sql, questionId);
	}

	@Override
	public Question findWithAnswersById(long questionId) throws ResourceNotFoundException {
		Question question = findById(questionId);
		if (question == null) {
			throw new ResourceNotFoundException("존재하지 않는 질문입니다.");
		}
		question.setAnswers(answerDao.findAllByQuestionId(questionId));
		return question;
	}
}
