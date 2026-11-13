from openai import OpenAI
import json
import gradio as gr
import requests

# ==========================================
# ⚠️ 第一步：换成你自己的 DeepSeek API Key
# ==========================================
API_KEY = "你的DeepSeek_API_Key"

client = OpenAI(
    api_key=API_KEY,
    base_url="https://api.deepseek.com"
)

# ==========================================
# 2. 搜索函数（现在去请求你的真实 Java 接口）
# ==========================================
def search_cloud_files(keyword):
    print(f"\n[Agent 正在向 Java 后端发送请求，搜索关键字: {keyword}]")
    
    # ⚠️ 第二步：确保这里的项目名是 disk，跟你浏览器里测试成功的一样
    url = f"http://localhost:8080/disk/api/search?keyword={keyword}"
    
    try:
        response = requests.get(url, timeout=5)
        return response.text
    except Exception as e:
        print("连接 Java 后端失败:", e)
        return "[]"

# ==========================================
# 3. 告诉大模型，你有这个工具可以调用
# ==========================================
tools = [{
    "type": "function",
    "function": {
        "name": "search_cloud_files",
        "description": "在云盘系统中搜索文件",
        "parameters": {
            "type": "object",
            "properties": {
                "keyword": {"type": "string", "description": "搜索的文件关键字，比如'财务'或'jpg'"}
            },
            "required": ["keyword"]
        }
    }
}]

# ==========================================
# 4. 核心 Agent 逻辑（已修复多工具调用的 Bug）
# ==========================================
def run_agent(user_input):
    messages = [{"role": "user", "content": user_input}]
    
    # 第一次请求：让大模型思考
    response = client.chat.completions.create(
        model="deepseek-chat",
        messages=messages,
        tools=tools,
        tool_choice="auto"
    )
    
    msg = response.choices[0].message
    
    if msg.tool_calls:
        messages.append(msg) 
        
        # 循环处理大模型发来的所有请求
        for tool_call in msg.tool_calls:
            args = json.loads(tool_call.function.arguments)
            keyword = args.get("keyword")
            
            search_results = search_cloud_files(keyword)
            
            messages.append({
                "role": "tool",
                "tool_call_id": tool_call.id,
                "content": search_results
            })
            
        # 第二次请求：让大模型总结搜索结果
        final_response = client.chat.completions.create(
            model="deepseek-chat",
            messages=messages
        )
        return final_response.choices[0].message.content
    else:
        return msg.content

# ==========================================
# 5. 网页界面部分
# ==========================================
def chat_interface(message, history):
    reply = run_agent(message)
    return reply

demo = gr.ChatInterface(
    fn=chat_interface,
    title="☁️ 智能云盘助手 (接入真实 HDFS)",
    description="基于大模型与 Function Calling 技术开发，你可以尝试输入：帮我找找 jpg 图片",
)

if __name__ == "__main__":
    demo.launch()