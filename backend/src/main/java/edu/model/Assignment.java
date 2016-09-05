package edu.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.Table;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "assignment")
@Table(value = "assignments")

public class Assignment {

	@Column("id")
	private String id;

	@Column("title")
	private String title;

	@Column("subject")
	private String subject;

	@Column("description")
	private String description;

	public Assignment() {

		super();
	}

	public String getID() {
		return id;
	}

	public void setID(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Assignment(String id, String title, String subject, String description) {
		super();
		this.setID(id);
		this.setTitle(title);
		this.setSubject(subject);
		this.setDescription(description);

	}

}
