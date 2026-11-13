# 分布式企业云盘系统 (Distributed Enterprise Cloud Disk)

基于 JavaWeb + Hadoop HDFS + MySQL 的分布式文件存储系统。采用 MVC 设计模式，将文件实体存储于 HDFS 中，元数据及用户信息存储于 MySQL，解决了传统单机存储容量受限的问题。

## 🛠️ 技术栈
- **后端**：Java, Servlet, Filter, JDBC
- **前端**：JSP, AJAX, ECharts
- **存储与数据库**：Hadoop HDFS, MySQL 5.7
- **开发工具**：Eclipse, Maven

## ✨ 核心功能
- **分布式存储**：基于 HDFS API 实现文件的上传、下载与读取，突破单机存储瓶颈。
- **文件分享与提取码**：一键生成4位随机提取码，陌生用户凭码免登录下载，模仿主流网盘体验。
- **数据可视化大屏**：递归遍历 HDFS 目录，按文件后缀名聚合统计，使用 ECharts 渲染环形图展示空间占比。
- **安全审计日志**：利用 Filter 拦截记录用户敏感操作，通过 SQL 实现个人日志数据隔离，并基于 Session 实现防抖（防止日志爆炸）。

## 🚀 如何运行
1. 启动 Hadoop HDFS 集群。
2. 在 MySQL 中新建数据库，导入根目录下的 `disk.sql` 文件。
3. 修改项目中数据库连接的配置文件（改为你的本地用户名/密码）。
4. 将项目部署至 Tomcat 服务器并启动。

## 👨‍💻 作者
**李红枫** 
- 邮箱：[1928971092@qq.com]
- 求职意向：数据开发 / Java后端开发实习生

## 🤖 AI Agent 智能模块 (最新扩展)
集成 Python 与大语言模型 (DeepSeek)，实现自然语言对话式文件检索。
- **微服务架构**：Python 作为独立的 AI 服务，通过 HTTP 接口与 Java 后端解耦交互。
- **Function Calling**：利用大模型解析用户意图，自动提取关键词并调用后端 HDFS 搜索接口。
- **前端界面**：基于 Gradio 快速构建响应式聊天 UI。

<img width="3072" height="1826" alt="屏幕截图 2026-11-13 112700_AI看图" src="https://github.com/user-attachments/assets/1bee2291-27c0-42fa-a03d-cefedadbb7e9" />
