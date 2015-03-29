package next.model;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Question {
	private static final Logger logger = LoggerFactory.getLogger(Question.class);
	
	private long questionId;

	private String writer;

	private String title;

	private String contents;

	private Date createdDate;

	private int countOfComment;

	private List<Answer> answers;

	public Question(String writer, String title, String contents) {
		this(0, writer, title, contents, new Date(), 0);
	}

	public Question(long questionId, String writer, String title, String contents, Date createdDate, int countOfComment) {
		this.questionId = questionId;
		this.writer = writer;
		this.title = title;
		this.contents = contents;
		this.createdDate = createdDate;
		this.countOfComment = countOfComment;
	}

	public long getQuestionId() {
		return questionId;
	}

	public String getWriter() {
		return writer;
	}

	public String getTitle() {
		return title;
	}

	public String getContents() {
		return contents;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public long getTimeFromCreateDate() {
		return this.createdDate.getTime();
	}

	public int getCountOfComment() {
		return countOfComment;
	}
	
	public void setAnswers(List<Answer> answers) {
		this.answers = answers;
	}
	
	public List<Answer> getAnswers() {
		return answers;
	}

	public boolean canDelete() {
		if (answers == null || answers.isEmpty()) {
			return true;
		}
		
		for (Answer answer : answers) {
			logger.debug("question writer : {}, answer writer : {}", writer, answer.getWriter());
			if (!writer.equals(answer.getWriter())) {
				return false;
			}
		}
		
		return true;
	}

	@Override
	public String toString() {
		return "Question [questionId=" + questionId + ", writer=" + writer + ", title=" + title + ", contents=" + contents + ", createdDate="
				+ createdDate + ", countOfComment=" + countOfComment + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (questionId ^ (questionId >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Question other = (Question) obj;
		if (questionId != other.questionId)
			return false;
		return true;
	}
}
