package com.dao.impl;
import com.beans.UserInfo;
import com.dao.UserDao;
import com.jdbc.DBUtil;
				
public class UserDaoImpl implements UserDao {
	//Ìí¼ÓÓÃ»§
	public void addUser(UserInfo user) {
		String sql="insert into  userInfo (userName,password,phone) values (?,?,?)";
		DBUtil.update(sql, user.getUserName(),user.getPassword(),user.getPhone());
		}
	//µÇÂ¼
	public UserInfo login(String userName, String password) {
		String sql="select * from userInfo where userName=? and password=?" ; 
		return DBUtil.getSingleObj(sql, UserInfo.class, userName,password);
	}
}