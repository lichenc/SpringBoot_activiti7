
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
                                        + "<td><input type='text' value=''  class='layui-input' style='border:none;'></td>"
                                        + " <td>"
                                        + "<button value='" + item.event + "' class='layui-btn' id='retry'>重新发起审批</button>"
                                        + "<button value='" + item.id + "' class='layui-btn' id='update'>编辑</button>"
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
var value1;
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
                    }
                }
            });
        } else {
        }
    });
    $("body").on("click", "#update", function (e) {
         value1=  $(this).val();
        $('.select option').remove();

        layui.use('form', function () {
            var form = layui.form; //只有执行了这一步，部分表单元素才会自动修饰成功

            layui.use('layer', function () {
                var layer = layui.layer;
                layer.open({
                    type: 1,
                    title: "岗位申请修改",   //标题
                    area: ['400px', '450px'],    //弹窗大小
                    shadeClose: false,      //禁止点击空白关闭
                    scrollbar: false,      //禁用滚动条
                    move: false,       //禁用移动
                    scrolling: 'no',
                    resize: false,
                    closeBtn: 1,
                    content: $('#positionApply'),
                    end: function () {
                        $('#positionApply').hide();
                    }
                });
            });
            $.ajax({
                url: "/getPositionList",
                type: "POST",
                dataType: "json",
                data:{
                  type:"修改",
                    id:value1
                },
                success: function (data) {
                    var po  ="<option value='0'>请选择岗位</option>";
                    if(data.positionEntityList!=null){
                        var positionStro=data.positionEntityList;
                        for(var i=0;i<positionStro.length;i++){
                            po=po+  "<option  value='"+positionStro[i].name+"'>"+positionStro[i].name+"</option>";
                        }
                        $('#position').append(po);
                        document.getElementById("applyPerson").value=data.userSn;
                    }
                    // 遍历select
                    $("#position").each(function() {
                        // this代表的是<option></option>，对option再进行遍历
                        $(this).children("option").each(function() {
                            // 判断需要对那个选项进行回显
                            if (this.value == data.position) {
                                $(this).attr("selected","selected");
                            }
                        });
                    })
                    document.getElementById("approvedPerson1").value = data.approvedPerson;
                    document.getElementById("applyReason").value = data.applyReason;
                    //渲染select，不然无法显示值
                    form.render();
                }
            });
        });
    });
});

/**
 * 修改岗位申请
 */
function updatePosition() {
    //岗位
    var position=document.getElementById("position").value;
    //申请原因
    var applyReason=document.getElementById("applyReason").value;
    //审批人
    var approvedPerson=document.getElementById("approvedPerson1").value;
    if (position==0){
        layer.alert("请选择岗位!")
        return false;
    }
  /*  if(approvedPerson==null){
        layer.alert("请选择审批人!")
        return false;
    }*/
    if (applyReason==null||applyReason==""){
        layer.alert("输入申请理由!")
        return false;
    }
    if(position != 0 && position !=null && applyReason != null && applyReason!="" && approvedPerson !=0 && approvedPerson != null) {
        $.ajax({
            url: "/updatePosition",
            type: "POST",
            dataType: "json",
            data: {
                position: position,
                applyReason: applyReason,
                id:value1
            },
            success: function (data) {
                if(data==1){
                    layer.alert("你已拥有该岗位，请勿重复申请！");
                }else{
                    layer.alert("修改成功！");
                    layer.close(layer.index - 1);
                    document.getElementById("selectTask").click()
                }
            }
        });
    }
}