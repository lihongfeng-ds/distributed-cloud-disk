package com.dao;
import com.beans.UserInfo;
				
public interface UserDao {
	void addUser(UserInfo user);
	UserInfo login(String userName, String password);
}
