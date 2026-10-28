package com.test;
import java.sql.Connection;

import com.beans.UserInfo;
import com.jdbc.DBUtil;
public class Test1 {
     public static void main(String[] args) {
    	demo4();
    	System.out.println("ok");
     }
     static void demo1() {
    	 Connection conn=DBUtil.getConn();
    	 System.out.println(conn);
     }
     static void demo2() {
    	 String sql="select * from userInfo where id=?";
    	 UserInfo user=DBUtil.getSingleObj(sql, UserInfo.class, 2);
         System.out.println(user);
     }
     static void demo3() {
    	 String sql="select * from userInfo where userName=? and password=?";
    	 UserInfo user=DBUtil.getSingleObj(sql, UserInfo.class, "admin","123");
         System.out.println(user);
     }
     static void demo4() {
    	 String sql="insert into userInfo (userName,password,note,phone) values (?,?,?,?)";
    	 DBUtil.update(sql, "代一凡","123","师大学生","13566677888");
     }
}
