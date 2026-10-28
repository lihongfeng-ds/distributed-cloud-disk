package com.servlet;

import com.jdbc.DBUtil; 
import com.dao.impl.HdfsDaoImpl; // ★ 引入老师的Dao实现类

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Random;

@WebServlet("/ShareServlet")
public class ShareServlet extends HttpServlet {

    // 1. 生成分享码
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=utf-8");
        PrintWriter out = response.getWriter();
        
        String filePath = request.getParameter("filePath");
        String action = request.getParameter("action");

        if ("create".equals(action)) {
            String code = generateCode();
            Connection conn = null;
            PreparedStatement ps = null;
            try {
                conn = DBUtil.getConn();
                String sql = "INSERT INTO t_file_share (file_path, share_code) VALUES (?, ?)";
                ps = conn.prepareStatement(sql);
                ps.setString(1, filePath);
                ps.setString(2, code);
                ps.executeUpdate();
                
                out.print("{\"status\":\"success\", \"code\":\"" + code + "\"}");
            } catch (Exception e) {
                e.printStackTrace();
                out.print("{\"status\":\"error\", \"msg\":\"数据库异常\"}");
            } finally {
                try { if(ps!=null) ps.close(); if(conn!=null) conn.close(); } catch(Exception e){}
            }
        }
    }

    // 2. 验证提取码 或 执行下载
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        String code = request.getParameter("code");

        if ("check".equals(action)) {
            // --- 验证提取码逻辑 ---
            response.setContentType("application/json;charset=utf-8");
            PrintWriter out = response.getWriter();
            Connection conn = null;
            PreparedStatement ps = null;
            ResultSet rs = null;
            try {
                conn = DBUtil.getConn();
                String sql = "SELECT file_path FROM t_file_share WHERE share_code = ?";
                ps = conn.prepareStatement(sql);
                ps.setString(1, code);
                rs = ps.executeQuery();
                
                if (rs.next()) {
                    String filePath = rs.getString("file_path");
                    out.print("{\"status\":\"success\", \"filePath\":\"" + filePath + "\"}");
                } else {
                    out.print("{\"status\":\"error\", \"msg\":\"提取码错误\"}");
                }
            } catch (Exception e) {
                e.printStackTrace();
                out.print("{\"status\":\"error\", \"msg\":\"服务器异常\"}");
            } finally {
                try { if(rs!=null) rs.close(); if(ps!=null) ps.close(); if(conn!=null) conn.close(); } catch(Exception e){}
            }

        } else if ("download".equals(action)) {
            // --- 真实下载逻辑（绕开登录，直接拉取HDFS流） ---
            Connection conn = null;
            PreparedStatement ps = null;
            ResultSet rs = null;
            try {
                conn = DBUtil.getConn();
                String sql = "SELECT file_path FROM t_file_share WHERE share_code = ?";
                ps = conn.prepareStatement(sql);
                ps.setString(1, code);
                rs = ps.executeQuery();
                
                if (rs.next()) {
                    String filePath = rs.getString("file_path");
                    // 提取文件名
                    String downname = filePath.substring(filePath.lastIndexOf("/") + 1);
                    
                    // 设置响应头（和老师写的一模一样）
                    response.setContentType("application/octet-stream; charset=utf-8");
                    response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(downname, "UTF-8"));
                    
                    // ★ 核心：调用老师的 Dao 类，直接把 HDFS 的文件流写给浏览器
                    HdfsDaoImpl hdfsDao = new HdfsDaoImpl();
                    hdfsDao.downLoadFileAsStream(filePath, response.getOutputStream());
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try { if(rs!=null) rs.close(); if(ps!=null) ps.close(); if(conn!=null) conn.close(); } catch(Exception e){}
            }
        }
    }

    private String generateCode() {
        String str = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            sb.append(str.charAt(random.nextInt(str.length())));
        }
        return sb.toString();
    }
}