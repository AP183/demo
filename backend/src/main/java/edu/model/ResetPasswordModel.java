package edu.model;

public class ResetPasswordModel {
	
	private String email;
	private String oldPassword;
	private String newPassword;
	private String newPasswordConfirmed;
	
	public ResetPasswordModel() {
		super();
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getOldpassword() {
		return oldPassword;
	}

	public void setOldpassword(String oldpassword) {
		this.oldPassword = oldpassword;
	}

	public String getNewpassword() {
		return newPassword;
	}

	public void setNewpassword(String newpassword) {
		this.newPassword = newpassword;
	}

	public String getNewpasswordconfirmed() {
		return newPasswordConfirmed;
	}

	public void setNewpasswordconfirmed(String newpasswordconfirmed) {
		this.newPasswordConfirmed = newpasswordconfirmed;
	}

	public ResetPasswordModel(String email, String oldpasssword, String newpassword, String newpasswordconfirmed) {
		super();
		this.setEmail(email);
		this.setOldpassword(oldpasssword);
		this.setNewpassword(newpassword);
		this.setNewpasswordconfirmed(newpasswordconfirmed);
	}
}
