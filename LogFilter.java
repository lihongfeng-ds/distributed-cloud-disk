package com.aop;

import com.jdbc.DBUtil;
import com.beans.UserInfo;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

@WebFilter("/*")
public class LogFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("====== LogFilter 初始化成功 ======");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        String uri = req.getRequestURI();
        System.out.println("LogFilter 拦截到请求 URI: " + uri); // 打印所有经过的请求
        
        // 1. 过滤掉静态资源
        if (uri.endsWith(".css") || uri.endsWith(".js") || uri.endsWith(".png") || 
            uri.endsWith(".jpg") || uri.endsWith(".jpeg") || uri.endsWith(".gif") || uri.endsWith(".ico")) {
            chain.doFilter(request, response);
            return;
        }

     // 2. 判断是否是敏感操作
        String action = null;
        if (uri.contains("UserServlet")) {
            String flag = req.getParameter("flag");
            if ("login".equals(flag)) {
                action = "用户登录";
            } else if ("logout".equals(flag)) {
                action = "注销退出"; // 加上这一句！
            }
        } else if (uri.contains("HdfsServlet")) {
            String flag = req.getParameter("flag");
            if ("upload".equals(flag)) action = "上传文件";
            else if ("download".equals(flag)) action = "下载文件";
            else if ("delete".equals(flag)) action = "删除文件";
        }

     // 3. 记录日志（增加防抖机制）
        if (action != null) {
            HttpSession session = req.getSession();
            
            // --- 防抖逻辑开始 ---
            long now = System.currentTimeMillis();
            Long lastLogTime = (Long) session.getAttribute("last_operation_time");
            String lastAction = (String) session.getAttribute("last_operation_action");

            // 如果是同一个操作，且距离上次操作小于 3000 毫秒（3秒），就不记录
            if (lastLogTime != null && action.equals(lastAction) && (now - lastLogTime < 3000)) {
                System.out.println("=== 拦截到重复操作，忽略记录：" + action + " ===");
            } else {
                // 记录日志
                System.out.println("=== 开始记录日志，操作：" + action + " ===");
                UserInfo user = (UserInfo) session.getAttribute("session_user");
                String username = (user != null) ? user.getUserName() : req.getParameter("userName"); 
                if (username == null || username.trim().isEmpty()) username = "未知用户";
                
                // --- IP 转换逻辑放这里，只处理一次 ---
                String ip = req.getRemoteAddr();
                if ("0:0:0:0:0:0:0:1".equals(ip) || "::1".equals(ip)) {
                    ip = "127.0.0.1";
                }
                
                // 保存日志（这里只调用一次！）
                saveLog(username, action, ip);
                
                // 更新 Session 里的最后一次操作时间和动作
                session.setAttribute("last_operation_time", now);
                session.setAttribute("last_operation_action", action);
            }
            // --- 防抖逻辑结束 ---
        }

        chain.doFilter(request, response); // 这一行保留，记得在后面把多余的大括号删掉
    }

    private void saveLog(String username, String action, String ip) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConn();
            String sql = "INSERT INTO t_operation_log (user_name, action, ip_address) VALUES (?, ?, ?)";
            ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, action);
            ps.setString(3, ip);
            ps.executeUpdate();
            System.out.println("=== 日志成功写入数据库！ ===");
        } catch (Exception e) {
            System.out.println("=== 日志写入数据库失败！ ===");
            e.printStackTrace();
        } finally {
            try { if(ps!=null) ps.close(); if(conn!=null) conn.close(); } catch(Exception e){}
        }
    }

    @Override
    public void destroy() {}
}