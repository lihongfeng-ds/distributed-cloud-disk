<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>文件分享提取</title>
    <style>
        body { display: flex; justify-content: center; align-items: center; height: 100vh; background: #f0f2f5; font-family: 'Microsoft YaHei', sans-serif; }
        .box { background: white; padding: 40px; border-radius: 10px; box-shadow: 0 4px 12px rgba(0,0,0,0.1); text-align: center; width: 400px; }
        h2 { color: #333; }
        input { padding: 10px; font-size: 16px; width: 80%; margin-bottom: 20px; border: 1px solid #ccc; border-radius: 5px; text-align: center; }
        button { padding: 10px 30px; font-size: 16px; background: #1890ff; color: white; border: none; border-radius: 5px; cursor: pointer; }
        button:hover { background: #40a9ff; }
        .file-info { margin-top: 20px; color: #555; display: none; }
        .download-btn { display: inline-block; margin-top: 10px; text-decoration: none; background: #52c41a; color: white; padding: 10px 20px; border-radius: 5px; cursor: pointer; }
    </style>
</head>
<body>
<div class="box">
    <h2>🔒 请输入提取码</h2>
    <input type="text" id="codeInput" placeholder="4位提取码" value="${param.code}">
    <button onclick="checkCode()">提取文件</button>

    <div class="file-info" id="fileInfo">
        <p>文件路径：<span id="fileName"></span></p >
        <span id="downloadBtn" class="download-btn">立即下载</span>
    </div>
</div>

<script>
    var currentCode = ""; // 记录当前输入的提取码

    function checkCode() {
        var code = document.getElementById("codeInput").value;
        if (!code) { alert("请输入提取码"); return; }

        var xhr = new XMLHttpRequest();
        xhr.open("GET", "ShareServlet?action=check&code=" + code, true);
        xhr.onreadystatechange = function() {
            if (xhr.readyState == 4 && xhr.status == 200) {
                var res = JSON.parse(xhr.responseText);
                if (res.status === "success") {
                    currentCode = code;
                    document.getElementById("fileInfo").style.display = "block";
                    document.getElementById("fileName").innerText = res.filePath;
                } else {
                    alert("提取码错误或已失效！");
                }
            }
        };
        xhr.send();
    }

    // 点击下载按钮
    document.getElementById("downloadBtn").onclick = function() {
        if (currentCode) {
            // 直接用 window.location.href 触发浏览器的下载行为
            window.location.href = "ShareServlet?action=download&code=" + currentCode;
        }
    };
</script>
</body>
</html>