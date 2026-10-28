package com.aop;
			import java.io.IOException;
			import javax.servlet.*;
			import javax.servlet.http.*;
			
			public class SessionFilter implements Filter {
				public void init(FilterConfig filterConfig) throws ServletException {	
				}
			
				public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
						throws IOException, ServletException {
					
					HttpServletRequest req=(HttpServletRequest)request;
					String uri=req.getRequestURI();
					System.out.println(uri);
					
					if (uri.endsWith(".css") || uri.endsWith(".js") || 
					        uri.endsWith(".png") || uri.endsWith(".jpg") || 
					        uri.endsWith(".jpeg") || uri.endsWith(".gif") || 
					        uri.endsWith(".ico") || uri.endsWith(".svg")) {
					        chain.doFilter(request, response); // 直接放行
					        return; // 终止当前过滤器，不再往下执行判断
					    }
					
					HttpSession session =req.getSession();
					
					String [] excludePaths= {
							"login.jsp",
							"register.jsp",
							"UserServlet",
							"share.jsp",       // 允许打开分享页面
						    "ShareServlet"   // 允许处理分享请求（生成、验证、下载）
					};
					
					
					for(String path:excludePaths) {
						if(uri.contains(path)) {
							chain.doFilter(request, response);
							return;
						}	
					}
		
					if(session.getAttribute("session_user")==null) {
						String script="<script>alert('session timeout!');window.top.location.href='login.jsp'</script>";
						response.getWriter().print(script);
					}
					else {
						chain.doFilter(request, response);
					}					
				}
			
				public void destroy() {
				}
			}
