package com.servlet;

import com.beans.UserInfo;
import com.jdbc.DBUtil;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/LogServlet")
public class LogServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. 获取当前登录的用户
        UserInfo currentUser = (UserInfo) request.getSession().getAttribute("session_user");
        if (currentUser == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        String currentUserName = currentUser.getUserName();

        List<Map<String, Object>> logList = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConn();
            // 2. 加上 WHERE 条件，只查当前用户自己的日志！
            String sql = "SELECT * FROM t_operation_log WHERE user_name = ? ORDER BY create_time DESC LIMIT 50";
            ps = conn.prepareStatement(sql);
            ps.setString(1, currentUserName); // 传入当前用户名
            rs = ps.executeQuery();
            
            while (rs.next()) {
                Map<String, Object> log = new HashMap<>();
                log.put("userName", rs.getString("user_name"));
                log.put("action", rs.getString("action"));
                log.put("ip", rs.getString("ip_address"));
                log.put("time", rs.getString("create_time"));
                logList.add(log);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if(rs!=null) rs.close(); if(ps!=null) ps.close(); if(conn!=null) conn.close(); } catch(Exception e){}
        }
        
        request.setAttribute("logList", logList);
        request.getRequestDispatcher("/log.jsp").forward(request, response);
    }
}