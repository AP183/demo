package edu.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "assignment")
public class AssignmentXML {

	private String id;
	private String title;
	private String subject;
	private String description;

	public AssignmentXML() {
		super();
	}

	public String getGtin() {
		return id;
	}

	public void setGtin(String gtin) {
		this.id = gtin;
	}

	public String getName() {
		return title;
	}

	public void setName(String name) {
		this.title = name;
	}

	public String getBrand() {
		return subject;
	}

	public void setBrand(String brand) {
		this.subject = brand;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public AssignmentXML(String id, String title, String subject, String description) {
		super();
		this.setGtin(id);
		this.setName(description);
		this.setBrand(title);
		this.setDescription(subject);

	}

}
