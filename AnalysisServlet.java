package com.servlet;

import com.beans.UserInfo;
import com.dao.impl.HdfsDaoImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/AnalysisServlet")
public class AnalysisServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=utf-8");
        PrintWriter out = response.getWriter();

        // 1. 获取当前登录用户
        UserInfo user = (UserInfo) request.getSession().getAttribute("session_user");
        if (user == null) {
            out.print("[]");
            return;
        }
        String username = user.getUserName();
        HdfsDaoImpl hdfsDao = new HdfsDaoImpl();

        try {
            // 2. 调用老师写好的方法，获取各类文件的数量 (List的size就是数量)
            int picCount = hdfsDao.getFileListByType(username, "picture").size();
            int txtCount = hdfsDao.getFileListByType(username, "txt").size();
            int aviCount = hdfsDao.getFileListByType(username, "avi").size();
            int soundCount = hdfsDao.getFileListByType(username, "sound").size();
            int gzCount = hdfsDao.getFileListByType(username, "gz").size();

            // 3. 拼接成 ECharts 需要的 JSON 格式
            String json = "[" +
                    "{\"name\":\"图片\", \"value\":" + picCount + "}," +
                    "{\"name\":\"文档\", \"value\":" + txtCount + "}," +
                    "{\"name\":\"视频\", \"value\":" + aviCount + "}," +
                    "{\"name\":\"音乐\", \"value\":" + soundCount + "}," +
                    "{\"name\":\"压缩包\", \"value\":" + gzCount + "}" +
                    "]";
            out.print(json);
        } catch (Exception e) {
            e.printStackTrace();
            out.print("[]");
        }
    }
}