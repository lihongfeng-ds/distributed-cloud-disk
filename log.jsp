<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>系统操作日志</title>
    <style>
        body { padding: 20px; font-family: 'Microsoft YaHei', sans-serif; background-color: #f9f9f9; }
        h2 { text-align: center; color: #333; }
        table { width: 100%; border-collapse: collapse; background: #fff; box-shadow: 0 2px 8px rgba(0,0,0,0.1); }
        th, td { padding: 12px; text-align: center; border-bottom: 1px solid #eee; }
        th { background-color: #1890ff; color: white; font-weight: normal; }
        tr:hover { background-color: #f5f7fa; }
    </style>
</head>
<body>
    <h2>📋 系统安全操作日志 (最近50条)</h2>
    <table>
        <tr>
            <th>操作用户</th>
            <th>操作行为</th>
            <th>IP 地址</th>
            <th>操作时间</th>
        </tr>
        <c:forEach var="log" items="${logList}">
            <tr>
                <td><b>${log.userName}</b></td>
                <td>${log.action}</td>
                <td>${log.ip}</td>
                <td>${log.time}</td>
            </tr>
        </c:forEach>
    </table>
</body>
</html>