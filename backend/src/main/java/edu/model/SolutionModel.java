package edu.model;

import java.util.List;

public class SolutionModel {

	private List<Assignment> list;

	private String email;

	public SolutionModel() {
		super();
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<Assignment> getList() {
		return list;
	}

	public void setList(List<Assignment> list) {
		this.list = list;
	}

	public SolutionModel(String email, List<Assignment> list) {
		super();
		this.setEmail(email);
		this.setList(list);
	}
}
