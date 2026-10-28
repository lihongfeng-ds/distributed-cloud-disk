package com.servlet;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.hadoop.fs.FileStatus;

import com.beans.DiskFileInfo;
import com.beans.UserInfo;
import com.dao.HdfsDao;
import com.dao.impl.HdfsDaoImpl;

import cn.hutool.core.util.StrUtil;

@WebServlet("/HdfsServlet")
public class HdfsServlet extends HttpServlet {
	HdfsDao hdfsDao=new HdfsDaoImpl();

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String flag=request.getParameter("flag");
		if (flag == null) {
            // 如果没带参数，默认跳回主页或者提示
            response.sendRedirect("main.jsp"); 
            return;
        }
		if("manager".equals(flag)) {
			manager(request,response);
		}
		else if("managerSubFiles".equals(flag)) {
			managerSubFiles(request,response);
		}
		else if ("upload".equals(flag)) {
            upload(request, response);
        }
        // 如果你还有 download、delete 等，继续在后面加 else if
        else if ("download".equals(flag)) {
            download(request, response);
        }
        else if ("delete".equals(flag)) {
            delete(request, response);
        }
        else if("createFolder".equals(flag)) {
        	createFolder(request,response);
        }
        else if("search".equals(flag)) {
        	search(request,response);
        }
        else if("searchFiles".equals(flag)) {
        	searchFiles(request,response);
        }
	}
	private void searchFiles(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		UserInfo user=(UserInfo)request.getSession().getAttribute("session_user");
		
		//取出要查询的文件的类型
		String fileType=request.getParameter("type");
		List<DiskFileInfo> hdfsFileList =hdfsDao.getFileListByType(user.getUserName(),fileType);
		request.setAttribute("hdfsFileList", hdfsFileList);
		request.getRequestDispatcher("main.jsp").forward(request, response);
	}
	//搜索文件
	private void search(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String fileName=request.getParameter("fileName");
		UserInfo user=(UserInfo)request.getSession().getAttribute("session_user");
		
		List<DiskFileInfo> hdfsFileList= hdfsDao.getFileListByName(user.getUserName(),fileName);
		request.setAttribute("hdfsFileList", hdfsFileList);
		request.getRequestDispatcher("/main.jsp").forward(request, response);
	}
	private void download(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String fileName=request.getParameter("fileName");
		String downname=fileName.substring(fileName.lastIndexOf("/")+1);
		response.setContentType("application/octet-stream; charset=utf-8");
		response.addHeader("Content-Disposition", "attachment; filename="+ URLEncoder.encode(downname, "UTF-8"));
		hdfsDao.downLoadFileAsStream(fileName, response.getOutputStream());
	}

	private void managerSubFiles(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String parent=request.getParameter("parent");
		
		if(StrUtil.isEmpty(parent)) {
			parent=(String) request.getAttribute("parent");
			if(parent==null) {
			    UserInfo user=(UserInfo)request.getSession().getAttribute("session_user");
                parent=user.getUserName();
			}
		}
		
		DiskFileInfo[] hdfsFileList=hdfsDao.getSubFileList(parent);
		request.setAttribute("hdfsFileList", hdfsFileList);
		
		
		//以下要处理前台导航
		List<String> urlList=new ArrayList<String>();
		urlList.add(parent);
		
		while(parent.lastIndexOf("/")!=-1) {
			parent=parent.substring(0,parent.lastIndexOf("/"));
			urlList.add(0,parent);
		}
		
		/*
		admin/doc/day1/aaa
		admin/doc/day1
		admin/doc
		admin 
		*/
			
		urlList.remove(0);  //前用户的用户目录没有必要在前台显示,所以移除
		
		for(int i=0;i<urlList.size();i++) {
			String url=urlList.get(i);
			String suffix=url.substring(url.lastIndexOf("/")+1);
			url=url+"_"+suffix;
			urlList.set(i, url);
		}

			
		request.setAttribute("urlList",urlList);
		request.setAttribute("hdfsFileList",hdfsFileList);
		request.getRequestDispatcher("/main.jsp").forward(request, response);
	}
	
	
	private void manager(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		UserInfo user=(UserInfo)request.getSession().getAttribute("session_user");
	
	
	    DiskFileInfo[] hdfsFileList= hdfsDao.getRootFileList(user.getUserName());
		
		request.setAttribute("hdfsFileList", hdfsFileList);
		request.getRequestDispatcher("/main.jsp").forward(request, response);
	}
	//创建文件夹
			private void createFolder(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
					String parent=request.getParameter("parent");
					String folderName=request.getParameter("forderName");
					
					//如果用户是自已的根目录创建的
					if(StrUtil.isEmpty(parent)) {
						UserInfo user=(UserInfo)request.getSession().getAttribute("session_user");
						parent=user.getUserName();
					}
					
					 boolean result=hdfsDao.createFolder(parent,folderName);
					 if(result==true) {
						 request.setAttribute("msg", "文件夹 "+folderName+" 创建成功");
					 }
					 else {
						 request.setAttribute("msg", "存在同名目录,创建失败 "); 
					 }
					
					request.setAttribute("parent", parent);
					managerSubFiles(request, response);	
			}
	private void delete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String fileName=request.getParameter("fileName");   //admin/doc/java/lesson1/a.txt
		String parent=fileName.substring(0,fileName.lastIndexOf("/")+1);
		
		hdfsDao.deleteFile(fileName);
		
		request.setAttribute("msg", "删除成功");
		request.setAttribute("parent", parent);
		request.setAttribute("refreshScript", "window.parent.leftFrame.location.reload()");
		
		managerSubFiles(request, response);	
	}


	private void upload(HttpServletRequest request, HttpServletResponse response) {
		DiskFileItemFactory factory = new DiskFileItemFactory();

		// 设置内存缓冲区，超过后写入临时文件
		factory.setSizeThreshold(50 * 1024 * 1024); //50M
		
		//创建一个新的文件上传处理程序
		ServletFileUpload upload = new ServletFileUpload(factory);

		upload.setHeaderEncoding("utf-8");
		
		//设置上传文件最大为10G
		upload.setSizeMax(10 * 1024 * 1024 * 1024); //10G
		
		//解析获取新文件
		List<FileItem> fileItems;
		
		try {
			fileItems = upload.parseRequest(request);
			//得到上传的文件的父级路径
			String parent = fileItems.get(0).getString("UTF-8");
			
			//如果用户是在根路径上传
			UserInfo user = null;
			if (StrUtil.isEmpty(parent)) {
				user = (UserInfo) request.getSession().getAttribute("session_user");
				parent = user.getUserName();
			}
			
			for (FileItem item : fileItems) {
				if (item.isFormField()) {
					/*如果是普通输入项 ,得到input中的name属性的值,和它对应的value
					String name=item.getFieldName();
					String value=item.getString("UTF-8");
					System.out.println("name="+name+"  value="+value); */
				}
				else {
					//上传的是文件，获得文件上传字段中的文件名
					//注意IE或FireFox中获取的文件名是不一样的，IE中是绝对路径，FireFox中只是文件名。
					String fileName = item.getName(); // D://aaa/bbb/c.txt
					
					//将FileItem对象中保存的主体内容保存到某个指定的文件中。
					// 如果FileItem对象中的主体内容是保存在某个临时文件中，该方法顺利完成后，临时文件有可能会被清除
					fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
					String localPath = getServletContext().getRealPath("/upload_files") + "/" + fileName;
					
					item.write(new File(localPath));
					
					hdfsDao.uploadFile(parent, localPath);
					request.setAttribute("msg", "文件上传成功!");
					request.setAttribute("refreshScript", "window.parent.leftFrame.location.reload()");
					item.delete();
					request.setAttribute("parent", parent);
					
					managerSubFiles(request, response);
				}
				
			}
			
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
	}
}

