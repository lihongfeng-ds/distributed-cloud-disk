<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html>
		<html>
		  <head>
		     <link href="css/bootstrap.css" rel="stylesheet" type="text/css" media="all" />
		    <link rel="stylesheet" href="css/poposlides.css">
		    <link rel="stylesheet" href="css/layout.css">
		    <title></title>
		     <meta charset="UTF-8" >
		      <script>
			    	//退出
			    	function logout(){
			    		if(confirm("确定要注销退出吗")==true)
			    		{
			    			window.location.href="UserServlet?flag=logout"; 
			    		}
			    	}
			    </script>
		  </head>  
		  <body>
		    <div class="frame-all">
		        <div class="layoutHeader">
		            <div class="listLeft">
		                <img src="images/my_sign.jpg" height="57" >
		                <a href="javascript:f1()" class="active">网盘</a>
		                <a href="analysis.jsp" target="centerFrame" onclick="f2()">数据分析</a>
		                <a href="#">分享</a>
		                <a href="#">找资源</a>
		                <a href="#">更多</a>
		            </div>
		            <div class="listRight">
		                <img src="images/my_avatar.jpg" class="photo">
		                <span>${session_user.userName} </span>
		                <img src="images/level.png">
		                <img src="images/btn.png">
		                <i></i>
		                <a> <img width="27px" onclick="logout()" title="注销退出" src="images/logout.png" ></a>
		                
		                <img src="images/icon1.png">
		                <img src="images/icon2.png">
		                <img src="images/icon3.png">
		                <a href="#">会员中心</a>
		            </div>
		        </div>
		    </div>
		  </body>
		</html>