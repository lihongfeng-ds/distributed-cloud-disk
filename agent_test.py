from openai import OpenAI
import json

# 1. 填入你的 API Key
API_KEY = "你的DeepSeek_API_Key"

client = OpenAI(
    api_key=API_KEY,
    base_url="https://api.deepseek.com"
)

# 2. 模拟你云盘数据库里的文件（为了今晚先跑通概念）
mock_cloud_files = [
    {"filename": "2023年度财务报表.pdf", "type": "pdf", "upload_time": "2023-12-01"},
    {"filename": "项目开发需求.docx", "type": "docx", "upload_time": "2023-12-05"},
    {"filename": "团队聚餐照片.jpg", "type": "jpg", "upload_time": "2023-12-10"},
    {"filename": "2024财务预算表.xlsx", "type": "xlsx", "upload_time": "2023-12-15"}
]

# 3. 定义一个本地搜索函数（以后你可以改成用 requests 去请求你的 Java 接口）
def search_cloud_files(keyword):
    print(f"\n[Agent 正在云盘里搜索关键字: {keyword}]")
    results = [f for f in mock_cloud_files if keyword in f["filename"]]
    return json.dumps(results, ensure_ascii=False)

# 4. 告诉大模型，你有这个工具可以调用
tools = [{
    "type": "function",
    "function": {
        "name": "search_cloud_files",
        "description": "在云盘系统中搜索文件",
        "parameters": {
            "type": "object",
            "properties": {
                "keyword": {"type": "string", "description": "搜索的文件关键字，比如'财务'或'照片'"}
            },
            "required": ["keyword"]
        }
    }
}]

def run_agent(user_input):
    print(f"\n用户问: {user_input}")
    messages = [{"role": "user", "content": user_input}]
    
    # 第一次请求：让大模型思考
    response = client.chat.completions.create(
        model="deepseek-chat",
        messages=messages,
        tools=tools,
        tool_choice="auto"
    )
    
    msg = response.choices[0].message
    
    # 如果大模型决定调用工具
    if msg.tool_calls:
        tool_call = msg.tool_calls[0]
        # 提取大模型给的参数
        args = json.loads(tool_call.function.arguments)
        keyword = args.get("keyword")
        
        # 执行你本地的搜索函数
        search_results = search_cloud_files(keyword)
        
        # 把搜索结果交还给大模型，让它总结
        messages.append(msg)
        messages.append({
            "role": "tool",
            "tool_call_id": tool_call.id,
            "content": search_results
        })
        
        # 第二次请求：让大模型总结
        final_response = client.chat.completions.create(
            model="deepseek-chat",
            messages=messages
        )
        print(f"\n=== AI 助手回复 ===\n{final_response.choices[0].message.content}\n")
    else:
        print(f"\n=== AI 直接回复 ===\n{msg.content}\n")

# 5. 测试运行
if __name__ == "__main__":
    run_agent("我最近好像上传过一份关于财务的文件，帮我找找是哪几个？")
    run_agent("帮我找找包含'照片'两个字的文件")