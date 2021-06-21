
$(document).ready(function(){
    selectTaskList();
});
function selectTaskList(){
    //渲染分页
    layui.use('laypage', function () {
        //清空之前的列表记录
        $('.tbody tr').remove();
        //获取申请人
       var applyPerson = document.getElementById("applyPerson").value;
        //获取审批人
        var approvedPerson = document.getElementById("approvedPerson").value;
        $.ajax({
            url: "/waitTryAgain",
            type: "POST",
            data: {
                applyPerson: applyPerson,
                approvedPerson: approvedPerson
            },
            success: function (data) {
                var laypage = layui.laypage;
                //获取任务集合
                var taskList = data.taskEntities;
                if (taskList != null) {
                    laypage.render({
                        elem: 'eventPage',
                        count: taskList.length, //数据总数，从服务端得到
                        limit: 10,
                        jump: function (obj) {
                            //模拟渲染
                            document.getElementById('eventList').innerHTML = function () {
                                var arr = []
                                thisData = taskList.concat().splice(obj.curr * obj.limit - obj.limit, obj.limit);
                                layui.each(thisData, function (index, item) {
                                    var taskStr =
                                        "<tr>"
                                        + "<td><input type='text' value='" + item.event + "'  class='layui-input' style='border:none;'></td>"
                                        + "<td><input type='text' value='" + item.eventType + "'  class='layui-input' style='border:none;'></td>"
                                        + "<td><input type='text' value='"+item.applyPerson+"'  class='layui-input' style='border:none;'></td>"
                                        + "<td><input type='text' value='" + item.approvedPerson + "'  class='layui-input' style='border:none;'></td>"
                                        + "<td><input type='text' value='" + item.taskType + "'  class='layui-input' style='border:none;'></td>"
                                        + "<td><input type='text' value='" + item.applyReason + "'  class='layui-input' style='border:none;'></td>"
                                        + " <td>"
                                        + "<button value='" + item.event + "' class='layui-btn' id='retry'>重试</button>"
                                        + "</td>"
                                        + "</tr>";
                                    arr.push(taskStr);
                                });
                                return arr.join('');

                            }();

                        }
                    });

                } else {
                   /* var prompt = document.getElementById("prompt");
                    prompt.style.display = "block";*/
                }

            }
        });
    });
}
/**
 * 重置查询条件操作
 */
function reset(){
    document.getElementById("applyPerson").value="";
    document.getElementById("approvedPerson").value="";
}
/**
 * 重试操作
 */
$(document).ready(function () {
    $("body").on("click", "#retry", function (e) {
        var r = confirm("确定要重试吗？");
        if (r == true) {
            //获取taskId
            var value=  $(this).val();
            $.ajax({
                url: "/taskRetry",
                type: "POST",
                data: {
                    id: value
                },
                success: function (data) {
                    //修改成功
                    if (data != 0) {
                        selectTaskList();
                    } else {
                        alert("修改失败!")
                        selectTaskList();
                    }
                }
            });
        } else {
        }
    });
});