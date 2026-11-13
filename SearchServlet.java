package com.servlet;

// 引入你项目里原本的类
import com.dao.HdfsDao;
import com.dao.impl.HdfsDaoImpl;
import com.beans.DiskFileInfo;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/api/search")
public class SearchServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        // 1. 拿到 AI 传过来的关键词
        String keyword = request.getParameter("keyword");
        if (keyword == null) keyword = "";

        // 2. 准备 JSON 结果
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("[");

        try {
            // 3. 实例化你的 DAO
            HdfsDao hdfsDao = new HdfsDaoImpl();
            
            // ⚠️ 这里的 userName，如果你的系统需要获取当前登录人，可以用 session 取
            // 比如：String userName = (String) request.getSession().getAttribute("username");
            // 为了测试，先写死 "admin" （因为你的截图里分享路径是 admin/my_avatar.jpg）
            String userName = "admin"; 

            // 4. 直接调用你项目里现成的 HDFS 搜索方法！
            List<DiskFileInfo> fileList = hdfsDao.getFileListByName(userName, keyword);
            
            // 5. 遍历结果，拼装成 JSON
            boolean first = true;
            for (DiskFileInfo file : fileList) {
                if (!first) {
                    jsonBuilder.append(",");
                }
                
                // ⚠️ 注意：去 DiskFileInfo 类里看一眼，获取文件名的方法叫什么？
                // 通常是 getFileName()，如果不是，请换成你类里真实的方法名（比如 getName()）
                String fileName = file.getPath(); 
                
                jsonBuilder.append(String.format("{\"filename\":\"%s\"}", fileName));
                first = false;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        jsonBuilder.append("]");

        // 6. 返回给 Python
        PrintWriter out = response.getWriter();
        out.print(jsonBuilder.toString());
        out.flush();
        out.close();
    }
}