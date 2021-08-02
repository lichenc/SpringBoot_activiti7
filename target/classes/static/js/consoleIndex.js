$(document).ready(function(){
    loadingOpen();
    $.ajax({
        url: "/getAdminCount",
        type: "POST",
        success: function (data) {
            document.getElementById("sumCount").innerText= data.sumCount;
            document.getElementById("waitTryAgainCount").innerText=data.waitTryAgainCount;
            document.getElementById("approvalCount").innerText=data.approvalCount;
            document.getElementById("historyCount").innerText=data.historyCount;

            document.getElementById("todaySumCount").innerText= "今日新增"+data.todaySumCount;
            document.getElementById("todayApprovalCount").innerText="今日新增"+data.todayApprovalCount;
            document.getElementById("todayWaitTryAgainCount").innerText="今日新增"+data.todayWaitTryAgainCount;
            document.getElementById("todayHistoryCount").innerText="今日新增"+data.todayHistoryCount;


            var days=[];
            var count=[];
            var pieDays=[];
            var datas=data.lineChartEntityList;
            var pieData=data.pieChartEntityList;
            for(var i=0;i<datas.length;i++){
                days.push(datas[i].days)
                count.push(datas[i].counts)
            }
            for(var i=0;i<pieData.length;i++){
                pieDays.push(pieData[i].days)
            }
            // 基于准备好的dom，初始化echarts实例
            var myChart = echarts.init(document.getElementById('EchartOrder'));
            // 基于准备好的dom，初始化echarts实例
            var pieEchartOrder = echarts.init(document.getElementById('PieEchartOrder'));
            // 指定图表的配置项和数据
            var option = {
                title: {
                    text: '每日流程折线图'
                },
                tooltip: {},
                xAxis: {
                    data: days
                },
                yAxis: {},
                series: [{
                    name: '日期',
                    type: 'line',
                    data: count
                }]
            };
            var optionPie = {
                title: {
                    text: '各流程数量统计',
                    left: 'left'
                },
                tooltip: {
                    trigger: 'item'
                },
                legend: {
                    orient: 'vertical',
                    left: 'right',
                    data:pieDays
                },
                series: [
                    {
                        name: '各流程数量统计',
                        type: 'pie',
                        radius: '70%',
                        data:(
                            function(){
                                var res=[];
                                for(var i=0;i<pieData.length;i++) {
                                    res.push({
                                        name:pieData[i].days,
                                        value:pieData[i].counts
                                    });
                                }
                                return res;
                            })

                        (),
                        emphasis: {
                            itemStyle: {
                                shadowBlur: 10,
                                shadowOffsetX: 0,
                                shadowColor: 'rgba(0, 0, 0, 0.5)'
                            }
                        }
                    }
                ]
            };

            // 使用刚指定的配置项和数据显示图表。
            myChart.setOption(option);
            pieEchartOrder.setOption(optionPie);
        }


    });
    loadingClose();

});


layui.use('element', function () {
    var $ = layui.jquery;
    var element = layui.element;
    /**
     * 菜单点击事件
     */
    $("body").on("click", ".huanzi-menu", function (e) {
        if ($(this).next("dl").length > 0) {
            return;
        }
        var url = $(this).data("url");
        var id = $(this).data("id");
        var text = $(this).text();

        //如果不存在
        if ($(".layui-tab-title").find("li[lay-id='" + id + "']").length <= 0) {
            element.tabAdd('demo', {
                title: text,
                //iframe子页面onload新增监听src为/loginPage是父页面跳转/loginPage，解决“俄罗斯套娃”问题
                content: "<iframe class='tab-content-div'  width='100%' height='900px' frameborder='0' scrolling='auto' src=\"" + url + "\"></iframe>",
                id: id
            });
        }
        element.tabChange('demo', id);
    });

})
;


/**
 * 退出登录确认
 */
function logout() {
    var r = confirm("确定要退出登录吗？");
    if (r == true) {
        document.getElementById("submit_from").submit();
    } else {

    }
}
