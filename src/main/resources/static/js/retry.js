var taskStr;
$(document).ready(function(){
    selectTaskList();
});
function selectTaskList(){
    //清空之前的列表记录
    $('.tbody tr').remove();
    taskStr=null;
    //获取申请人
    var applyPerson=document.getElementById("applyPerson").value;
    //获取审批人
    var approvedPerson=document.getElementById("approvedPerson").value;
    $.ajax({
        url: "/waitTryAgain",
        type: "POST",
        data: {
            applyPerson: applyPerson,
            approvedPerson: approvedPerson
        },
        success: function (data) {
            //获取任务集合
            var taskList=data.taskEntities;
            if(taskList!=null){
                //循环遍历任务集合
                //var userSn=document.getElementById("userSn").value;
                for(var i=0;i<taskList.length;i++){
                    taskStr=taskStr
                        + "<tr>"
                        + " <td><input type='text' value='"+taskList[i].event+"'  class='layui-input' style='border:none;'></td>"
                        + " <td><input type='text' value='"+taskList[i].eventType+"'  class='layui-input' style='border:none;'></td>"
                        + " <td><input type='text' value=''  class='layui-input' style='border:none;'></td>"
                        + " <td><input type='text' value='"+taskList[i].approvedPerson+"'  class='layui-input' style='border:none;'></td>"
                        +  " <td><input type='text' value='"+taskList[i].taskType+"'  class='layui-input' style='border:none;'></td>"
                        +  " <td>"
                        +  "<button value='"+taskList[i].event+"' class='layui-btn' id='retry'>重试</button>"
                        +  "  </td>"
                        + "  </tr>";
                }
               $("#eventList").append(taskStr);
                //渲染分页
                layui.use('laypage', function () {
                    var laypage = layui.laypage;
                    //账号Tab分页
                    laypage.render({
                        elem: 'eventPage'
                        , count: data.taskCount //数据总数，从服务端得到
                    });
                });
            }else{
                var prompt = document.getElementById("prompt");
                //设置表头文字颜色
                prompt.style.display = "block";
            }

        }
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