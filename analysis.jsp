<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>数据分析</title>
    <!-- 引入 ECharts -->
    <script src="https://cdn.jsdelivr.net/npm/echarts@5.4.3/dist/echarts.min.js"></script>
    <style>
        body { margin: 0; padding: 20px; background-color: #f5f7fa; font-family: 'Microsoft YaHei', sans-serif; }
        .card { background: #fff; border-radius: 10px; box-shadow: 0 2px 12px 0 rgba(0,0,0,.1); padding: 20px; text-align: center; }
        h2 { color: #333; margin-bottom: 20px; }
        #main { width: 100%; height: 500px; }
    </style>
</head>
<body>
<div class="card">
    <h2>📁 我的文件类型分布</h2>
    <div id="main"></div>
</div>

<script>
    // 初始化 echarts 实例
    var myChart = echarts.init(document.getElementById('main'));

    // 发送 AJAX 请求获取后端数据
    var xhr = new XMLHttpRequest();
    xhr.open("GET", "AnalysisServlet", true);
    xhr.onreadystatechange = function() {
        if (xhr.readyState == 4 && xhr.status == 200) {
            var data = JSON.parse(xhr.responseText);
            
            // 渲染图表配置
            var option = {
                tooltip: { trigger: 'item', formatter: '{a} <br/>{b}: {c}个 ({d}%)' },
                legend: { top: '5%', left: 'center' },
                color: ['#5470c6', '#91cc75', '#fac858', '#ee6666', '#73c0de'],
                series: [
                    {
                        name: '文件数量',
                        type: 'pie',
                        radius: ['40%', '70%'], // 环形图
                        avoidLabelOverlap: false,
                        itemStyle: {
                            borderRadius: 10,
                            borderColor: '#fff',
                            borderWidth: 2
                        },
                        label: { show: false, position:'center' },
                        emphasis: {
                            label: { show: true, fontSize: 20, fontWeight: 'bold' }
                        },
                        labelLine: { show: false },
                        data: data
                    }
                ]
            };
            myChart.setOption(option);
        }
    };
    xhr.send();

    // 窗口大小改变时自适应
    window.addEventListener('resize', function() {
        myChart.resize();
    });
</script>
</body>
</html>