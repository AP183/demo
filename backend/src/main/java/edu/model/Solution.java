package edu.model;

import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.Table;

@Table(value = "assignment_by_students")
public class Solution {

	@Column("id")
	private String id;
	@Column("title")
	private String title;
	@Column("description")
	private String description;
	@Column("subject")
	private String subject;
	@Column("email")
	private String email;

	public Solution() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public Solution(String email, String id, String title, String subject, String description) {
		super();
		this.setEmail(email);
		this.setId(id);
		this.setTitle(title);
		this.setSubject(subject);
		this.setDescription(description);
	}
}
