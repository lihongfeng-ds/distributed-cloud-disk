<%@ page language="java" import="java.util.*" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html>
  <head>
      <base href="<%=basePath%>">
    <title>海康云盘系统</title>
    <link rel="stylesheet" href="css/poposlides.css">
    <link rel="stylesheet" href="css/layout.css">
    <script src="js/jquery-2.1.4.min.js" ></script>
    <meta charset="UTF-8">
    <script>
    
       		//这里写一些页面加载的时候要做的事
		    	$(function(){
		    		${refreshScript}
		    		initTrEvent();
		    	})
		    	
		    	//下面是声明一个javacript函数
		    	function initTrEvent(){
		    		//.给选中的元素加一个鼠标滑过的事件
		    		$("#center_table tr").mouseover(function(){
		    				//给当前元素加一个样式 
			    			$(this).addClass("highlightTd");
			    			//给当前元素的兄弟元素去掉一个样式
			    			$(this).siblings().removeClass("highlightTd");
			    			
			    			//控制某些元素的显示和隐藏
			    			$(this).find("div").show(); //显示出分享,下载,删除
			    			$(this).siblings().find("div").hide();
		    		});
		    	}
		    	
		    	function downloadFile(path){
		    	 	   window.location.href="  HdfsServlet?flag=download&fileName="+encodeURI(path);
		       }

		    	function uploadFile(){
		     		$("#img1").show();  //显示动画
		     		document.form1.submit();
		     }
		    	
		    	function deleteFile(url){
		    		if(confirm("确定要删除吗")){
		    			window.location.href="HdfsServlet?flag=delete&fileName="+encodeURI(url);
		    		}	
	       }

		    	
		    	function getSubFiles(parent){
		    		window.location.href="HdfsServlet?flag=managerSubFiles&parent="+ encodeURI(parent);
		    	}
		    	
		    	function createNewTr(){
		    		var innerTR=
		    		"<tr>"+
		    		"<td><img src='images/fileIcons/folder.png' /> <input id='txtFolderName'  value='请输入文件夹名称'><button class='newrow-btn' onclick='mkDir()'>√</button><button  class='newrow-btn'  onclick='cancleCreateDir(this)'>X</button>   </td><td>b</td><td>c</td><td>d</td>";
		    		"</tr>";
		
		    		$("#center_table").prepend(innerTR);
		    		document.getElementById("txtFolderName").select();
		    	}
	    	
						function mkDir(){
			    		var parent=encodeURI($("#parent").val());
			    		var folderName=encodeURI($("#txtFolderName").val());
			    		window.location.href="HdfsServlet?flag=createFolder&parent="+parent+"&forderName="+folderName;
			    	}
			    	
			    	function cancleCreateDir(btn){
			    		$(btn).parent().parent().remove();
			    	}
		    	
      </script>
    
  </head>
   <body>
  <img id="img1" style="display:none" src="images/processing.gif" />
     <form style="display:none" name="form1" action="HdfsServlet?flag=upload" method="post" enctype="multipart/form-data">
       <input name="parent" value="${param.parent==null?parent:param.parent}" >
   		 <input type="file" id="file1" name="file1" onchange="uploadFile()" >
   </form>
   <div class="frame-center">
            <div class="frame-nav">
                <div class="centerLeft">
                    <a  onclick="document.getElementById('file1').click()" class="active"><img src="images/upload.png"  />上传</a >
                    <a  onclick="createNewTr()"><img src="images/createfolder.png"/>新建文件夹</a >
                    <a><img src="images/download.png" />离线下载</a >
                    <a><img src="images/machine.png" />我的设备</a >
                </div>
             <div class="centerRight"> 
            	<form id="form1" action="HdfsServlet?flag=search" method="post">
                   <p>
                      <input   name="fileName" placeholder="搜素你的文件" value="">
                      <img onclick="$('#form1').submit()" src="images/search.png"  title="搜索"  />
                  </p>
                     <img src="images/menu1.png">
                     <img src="images/menu2.png"> 
                </form>     
            </div>
     </div>
                
            <div class="frame-title">
        	   <span>   
	        	     <a href="HdfsServlet?flag=manager"> 全部文件  | </a>
	        	    <c:forEach var="url" items="${urlList }">
		     	   <a onclick='getSubFiles("${fn:split( url ,"_")[0]  }")' >  &gt; ${fn:split( url ,"_")[1]} </a >
	      </c:forEach>
	        	    
              	</span>
              	<span>
            		  已全部加载，共  <label>${fn:length(hdfsFileList) } </label>个
                </span>    
            </div>
           
            <div class="tab">
                <table>  
                    <tr>
                        <td><input type="checkbox">文件名</td>
                        <td></td>
                        <td>大小</td>
                        <td>修改日期</td>
                     </tr>
                </table>
                
                <div class="datas">
	                <table id="center_table">	
	                	
	                	<c:forEach var="f" items="${hdfsFileList}">
	                	 <tr class="highlightTd">
	                	    <td>
	                	       &nbsp; <input type="checkbox"><img src="images/fileIcons/${f.icon}">
	                	        <label 
	                	          <c:if test="${f.directory}">
	                	          onclick="getSubFiles('${f.realPath }')"
	                	           style="cursor:pointer" 
	                	          </c:if>
	                	          
	                	          > 
	                	        
	                	          ${f.path}
	                	        </label>
	                	     </td>
	                	      <td >
	                	        <div style="display:inline-block">
	                	        	<a onclick="shareFile('${f.realPath}')" style="cursor:pointer;"><img src="images/a.png" title="分享" /></a >
	                	        	<a href="javascript:void(0)" onclick="downloadFile('${f.realPath }')"><img src="images/b.png"  title="下载"  /></a>  
	                	            <a onclick="deleteFile('${f.realPath }')"><img src="images/c.png"  title="删除"  /></a >
	                	            
	                	         </div>
	                	      </td>
	                        <td> ${f.len}</td>
	                        <td> ${f.modificationTime}</td>
	                        </tr>
	                         
	           </c:forEach>
	      
	                </table>
                </div>
            </div>
        </div>
        <script>
        var msg="${msg}";
        if(msg!="")
        	alert(msg);
        </script>
        
        <script>
     function shareFile(filePath) {
    var xhr = new XMLHttpRequest();
    xhr.open("POST", "ShareServlet?action=create&filePath=" + encodeURIComponent(filePath), true);
    xhr.onreadystatechange = function() {
        if (xhr.readyState == 4 && xhr.status == 200) {
            var res = JSON.parse(xhr.responseText);
            if (res.status === "success") {
                var shareLink = "http://localhost:8080/disk/share.jsp?code=" + res.code;
                
                // 把数据塞进弹窗的输入框里
                document.getElementById("modalCode").value = res.code;
                document.getElementById("modalLink").value = shareLink;
                
                // 显示我们自定义的弹窗
                document.getElementById("shareModal").style.display = "flex"; 
            } else {
                alert("分享失败：" + res.msg);
            }
        }
    };
    xhr.send();
}
</script>
<!-- 自定义分享弹窗 -->
<div id="shareModal" style="display:none; position:fixed; top:0; left:0; width:100%; height:100%; background:rgba(0,0,0,0.5); z-index:9999; align-items:center; justify-content:center;">
    <div style="background:#fff; padding:20px; border-radius:8px; width:350px; text-align:center; box-shadow: 0 4px 12px rgba(0,0,0,0.15);">
        <h3 style="margin-top:0;">🎉 分享成功</h3>
        
        <p style="text-align:left; margin: 10px 0 5px 0; font-size:14px;">提取码：</p >
        <input type="text" id="modalCode" readonly style="width:90%; padding:8px; text-align:center; font-weight:bold; color:red; border:1px solid #ccc; border-radius:4px; font-size:16px;">
        
        <p style="text-align:left; margin: 15px 0 5px 0; font-size:14px;">分享链接：</p >
        <input type="text" id="modalLink" readonly style="width:90%; padding:8px; border:1px solid #ccc; border-radius:4px; font-size:12px; color:#555;">
        
        <button onclick="document.getElementById('shareModal').style.display='none'" style="margin-top:20px; padding:8px 30px; background:#1890ff; color:white; border:none; border-radius:4px; cursor:pointer; font-size:14px;">我知道了</button>
    </div>
</div>
  </body>
</html>