package com.servlet;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import com.beans.UserInfo;
import com.dao.HdfsDao;
import com.dao.UserDao;
import com.dao.impl.UserDaoImpl;
import com.dao.impl.HdfsDaoImpl;
					
@WebServlet("/UserServlet")
public class UserServlet extends HttpServlet {
	UserDao userDao=new UserDaoImpl();
	HdfsDao hdfsDao=new HdfsDaoImpl();				
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
	request.setCharacterEncoding("UTF-8");
							
	String flag =request.getParameter("flag");
							
	if(flag.equals("register")) {
		register(request,response );
		}
							
	else if(flag.equals("login")) {
		login(request,response);
	  }
	else if(flag.equals("logout")) {
		logout(request,response);
	  }				
	}
	
	private void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String userName=request.getParameter("userName");
		String password=request.getParameter("password");
		
		UserInfo user =userDao.login(userName,password);
		if(user!=null) {
			request.getSession().setAttribute("session_user", user);
			response.sendRedirect(request.getContextPath() + "/index.html");
		}
		else {
			request.setAttribute("msg", "账号或密码错误,登录失败");
			request.getRequestDispatcher("/login.jsp").forward(request, response);
		}
	}
					
	private void register(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    String userName =request.getParameter("userName");
        String password =request.getParameter("password");
		String phone =request.getParameter("phone");
		String note =request.getParameter("note");
							
		UserInfo user=new UserInfo();
		user.setUserName(userName);
		user.setPassword(password);
		user.setNote(note);
		user.setPhone(phone);
							
		userDao.addUser(user);
		
		hdfsDao.createUserRoot(userName);
							
		request.setAttribute("registerFlag",true );
							
		request.getRequestDispatcher("/register.jsp").forward(request, response);		
		}		
	private void logout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getSession().invalidate();
		String script="<script>window.top.location.href='login.jsp'</script>";
		response.getWriter().write(script);
	  }
    }