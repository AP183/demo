package edu.model;

import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.Table;

@Table(value = "users")
public class User {

	@Column("lastname")
	private String lastname;

	@Column("firstname")
	private String firstname;

	@Column("email")
	private String email;

	@Column("password")
	private String password;

	@Column("phone")
	private int phone;

	@Column(value = "role")
	private String role;

	@Column("status")
	private String status;
 
	public User() {
		super();
	}

	public User(String email, String lastname, String firstname, String password, int phone, String role, String status) {
		super();
		this.setLastname(lastname);
		this.setFirstname(firstname);
		this.setEmail(email);
		this.setPassword(password);
		this.setPhone(phone);
		this.setRole(role);
		this.setStatus(status);
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getPhone() {
		return phone;
	}

	public void setPhone(int phone) {
		this.phone = phone;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	@Override
	public String toString() {
		return "Employee [ name=" + lastname + ", password=" + password + ", phone=" + phone + "]";
	}
}