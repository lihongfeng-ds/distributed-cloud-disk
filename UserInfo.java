package com.beans;

public class UserInfo{
	public String toString() {
		return "UserInfo [id=" + id + ", userName=" + userName + ", password=" + password + ", note=" + note
				+ ", phone=" + phone + "]";
	}
	  private int id;
	  private String userName;
	  private String password;
	  private String note;
	  private String phone;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	  
}