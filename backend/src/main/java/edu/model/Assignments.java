package edu.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "assignments")
public class Assignments {

	@XmlElement(name = "assignment", type = Assignment.class)
	private List<Assignment> assignment = new ArrayList<Assignment>();

	public Assignments() {
	}

	public Assignments(List<Assignment> assignments) {
		this.assignment = assignments;
	}

	public List<Assignment> getAssignments() {
		return assignment;
	}

	public void setAssignments(List<Assignment> assignments) {
		this.assignment = assignments;
	}
}