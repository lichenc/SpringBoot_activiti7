var value1;
/**
 * 加载岗位申请列表
 * */
$(document).ready(function(){
    getPositionList();
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
                    content: $('#positionUpdate1'),
                    end: function () {
                        $('#positionUpdate1').hide();
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
                        $('#positionUpdate').append(po);
                        document.getElementById("applyPersonUpdate").value=data.userSn;
                    }
                    // 遍历select
                    $("#positionUpdate").each(function() {
                        // this代表的是<option></option>，对option再进行遍历
                        $(this).children("option").each(function() {
                            // 判断需要对那个选项进行回显
                            if (this.value == data.position) {
                                $(this).attr("selected","selected");
                            }
                        });
                    })
                    document.getElementById("approvedPersonUpdate").value = data.approvedPerson;
                    document.getElementById("applyReasonUpdate").value = data.applyReason;
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
    var positionUpdate=document.getElementById("positionUpdate").value;
    //申请原因
    var applyReasonUpdate=document.getElementById("applyReasonUpdate").value;
    //审批人
    var approvedPersonUpdate=document.getElementById("approvedPersonUpdate").value;
    if (positionUpdate==0){
        layer.alert("请选择岗位!")
        return false;
    }
    /*  if(approvedPerson==null){
          layer.alert("请选择审批人!")
          return false;
      }*/
    if (applyReasonUpdate==null||applyReasonUpdate==""){
        layer.alert("输入申请理由!")
        return false;
    }
    if(positionUpdate != 0 && positionUpdate !=null && applyReasonUpdate != null && applyReasonUpdate !="" && approvedPersonUpdate !=0 && approvedPersonUpdate != null) {
        $.ajax({
            url: "/updatePosition",
            type: "POST",
            dataType: "json",
            data: {
                position: positionUpdate,
                applyReason: applyReasonUpdate,
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

function getPositionList() {
    //渲染分页
    layui.use('laypage', function () {
    //清空之前的列表记录
    $('.tbody tr').remove()
    var startTime=document.getElementById("startTime").value;
    var endTime=document.getElementById("endTime").value;
    $.ajax({
        url: "/getPositionParamList",
        type: "POST",
        data:{
            startTime:startTime,
            endTime:endTime
        },
        success: function (data) {
            if(data!=null){
                var laypage = layui.laypage;
                //账号Tab分页
                laypage.render({
                    elem: 'positionPage',
                    count: data.length, //数据总数，从服务端得到
                    limit:10,
                    jump: function(obj){
                        //模拟渲染
                        document.getElementById('positionList').innerHTML = function(){
                            var arr =[]
                            thisData =data.concat().splice(obj.curr*obj.limit - obj.limit, obj.limit);
                            layui.each(thisData, function(index, item){
                                var positionStr= "<tr>"
                                    +"<td><input type='text' value='"+item.procInstId+"'  class='layui-input' style='border:none;'></td>"
                                    +"<td><input type='text' value='"+item.applyPerson+"' class='layui-input' style='border:none;'></td>"
                                    +"<td><input type='text' value='"+item.applyCreateTime+"' class='layui-input' style='border:none;'></td>"
                                    +"<td><input type='text' value='"+item.position+"' class='layui-input' style='border:none;'></td>"
                                    +"<td><input type='text' value='"+item.approvedPerson+"' class='layui-input' style='border:none;'></td>"
                                    +"<td><input type='text' value='"+item.taskType+"' class='layui-input' style='border:none;'></td>"
                                    +"<td><input type='text' value='"+item.applyReason+"' class='layui-input' style='border:none;'></td>"
                                    +"<td>";

                                //如果是待审批的状态，只可以撤回，不可编辑和重新提交
                                if (item.processName=="审批岗位"){

                                   positionStr=positionStr + "<button value='" + item.procInstId + "' class='layui-btn' id='backProcess'>撤回</button>";
                                   //还在申请结点，可以编辑之后重新提交
                                }else{
                                    positionStr=positionStr+ "<button value='" + item.procInstId + "' class='layui-btn' id='update'>编辑</button>"
                                        + "<button value='" + item.id + "' class='layui-btn' id='reSubmit'>重新发起审批</button>";
                                }
                                positionStr=positionStr+ "</td>"
                                +"</tr>";
                                arr.push(positionStr);
                            });
                            return arr.join('');
                        }();

                    }
                });
            }
        }
    });
});
}


/**
 * 撤回岗位申请
 */
$(document).ready(function() {

    $("body").on("click", "#backProcess", function (e) {
            var layer = layui.layer;
            var r = confirm("确定要撤回吗？");
            if (r == true) {
                //获取taskId
                var value = $(this).val();
                $.ajax({
                    url: "/backProcess",
                    type: "POST",
                    data: {
                        id: value
                    },
                    success: function (data) {
                        //撤回成功
                        if (data == 1) {
                            alert("撤回成功!")
                            document.getElementById("selectTask").click();
                        } else if (data == 2) {
                            alert("流程未启动或已执行完成，无法撤回!");
                        } else {
                            alert("撤回失败!")
                            document.getElementById("selectTask").click();
                        }
                    }
                });
            } else {
            }
    });
    $("body").on("click", "#reSubmit", function (e) {
            var r = confirm("确定要重新发起审批吗？");
            if (r == true) {
                //获取taskId
                var value = $(this).val();
                $.ajax({
                    url: "/reSubmit",
                    type: "POST",
                    data: {
                        id: value
                    },
                    success: function (data) {
                        //撤回成功
                        if (data == 1) {
                            alert("重新发起审批成功!")
                            document.getElementById("selectTask").click();
                        } else {
                            alert("发起审批失败!")
                            document.getElementById("selectTask").click();
                        }
                    }
                });
            } else {
            }
        });
});




/**
 * 打开岗位申请弹窗
 */
function positionApply() {
    $('.select option').remove();
    document.getElementById("applyReason").value=null;
    layui.use('form', function(){
        var form = layui.form; //只有执行了这一步，部分表单元素才会自动修饰成功

        layui.use('layer', function () {
            var layer = layui.layer;
            layer.open({
                type: 1,
                title: "岗位申请",   //标题
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
                var approverStr  ="<option value='0'>请选择审批人</option>";
                if(data.approverList!=null){
                    var approverList=data.approverList;
                    for(var i=0;i<approverList.length;i++){
                        approverStr=approverStr+ "<option  value='"+approverList[i].audit+"'>"+approverList[i].audit+"</option>";
                    }
                    $('#approvedPerson').append(approverStr);

                }
                //渲染select，不然无法显示值
                form.render();
            }
        });

    }); }

/**
 * 添加岗位申请流程
 */
function addPosition() {
    //岗位
    var position=document.getElementById("position").value;
    //申请原因
    var applyReason=document.getElementById("applyReason").value;
    //审批人
    var approvedPerson=document.getElementById("approvedPerson").value;
    if (position==0){
        layer.alert("请选择岗位!")
        return false;
    }
    if(approvedPerson==0){
        layer.alert("请选择审批人!")
        return false;
    }
    if (applyReason==null||applyReason==""){
        layer.alert("输入申请理由!")
        return false;
    }
    if(position != 0 && position !=null && applyReason != null && applyReason!="" && approvedPerson !=0 && approvedPerson != null) {
        $.ajax({
            url: "/addPositionProcess",
            type: "POST",
            dataType: "json",
            data: {
                position: position,
                applyReason: applyReason,
                approvedPerson:approvedPerson
            },
            success: function (data) {
                if(data==1){
                    layer.alert("你已拥有该岗位，请勿重复申请！");
                }else{
                    layer.alert("申请成功！");
                    layer.close(layer.index - 1);
                    getPositionList();
                }
            }
        });
    }
}

/**
 * 重置操作
 */
function reset() {
    document.getElementById("startTime").value=null;
    document.getElementById("endTime").value=null;
}

