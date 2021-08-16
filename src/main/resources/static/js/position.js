var value1;

function over(e){
    var ca=e;
    if(ca!=null && ca!=""){
        layui.use('layer', function () {
            var layer=layui.layer;
            layer.tips(e, "#repulseReason",{
                tips: 1,
                time: 2000
            });
        });
    }
}
function over1(e){
    var ca=e;
    if(ca!=null && ca!=""){
        layui.use('layer', function () {
            var layer=layui.layer;
            layer.tips(e, "#applyReasonlist",{
                tips: 1,
                time: 2000
            });
        });
    }
}
/**
 * 加载岗位申请列表
 * */
$(document).ready(function(){


    getPositionList();
    $("body").on("click", "#history", function (e) {
        var proId=  $(this).val();
        layui.use('form', function () {
            var form = layui.form;
            layui.use('layer', function () {
                var layer = layui.layer;
                layer.open({
                    type: 1,
                    title: "查看流程图",   //标题
                    area: ['800px', '390px'],    //弹窗大小
                    shadeClose: false,      //禁止点击空白关闭
                    scrollbar: false,      //禁用滚动条
                    move: false,       //禁用移动
                    scrolling: 'no',
                    resize: false,
                    closeBtn: 1,
                    content: $('#procdefPicture'),
                    end: function () {
                        $('#procdefPicture').hide();
                    }
                });
            });

        });
        $("#picture").attr("src","../getProcefPicture?taskId="+proId+"");
    });
    /**
     * 打开修改岗位弹窗
     */
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
                    if(data.positionEntityList!=null){
                        layui.config({
                            base: '../js/'
                        }).extend({
                            xmSelect: 'xm-select'
                        }).use(['xmSelect'], function(){
                            var xmSelect = layui.xmSelect;
                            //渲染多选
                            demo1 = xmSelect.render({
                                el: '#positions',
                                paging: true,
                                pageSize: 2,
                                filterable: true,
                                pageEmptyShow: false,
                                toolbar: {show: true,},
                                autoRow: true,
                                tips:'请选择岗位',
                                searchTips: '您需要什么岗位',
                                empty: '呀, 还没有岗位呢',
                                data: data.positionEntityList,
                            })

                        })
                        document.getElementById("applyPerson").value=data.userSn;
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
     if(approvedPerson==null){
          layer.alert("请选择审批人!")
          return false;
      }
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
                    layer.alert("修改成功！",{icon: 6,time:3000});
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
        var approvedPerson=document.getElementById("approvedPerson").value;
        $.ajax({
            url: "/getPositionParamList",
            type: "POST",
            data:{
                startTime:startTime,
                endTime:endTime,
                approvedPerson:approvedPerson
            },
            success: function (data) {
                if(data!=null){
                    var laypage = layui.laypage;
                    //账号Tab分页
                    laypage.render({
                        elem: 'positionPage',
                        count: data.length, //数据总数，从服务端得到
                        limit:10,
                        limits:[10,20,30],
                        layout: ['prev', 'page', 'count','next',  'skip'],
                        jump: function(obj){
                            //模拟渲染
                            document.getElementById('positionList').innerHTML = function(){
                                var arr =[]
                                thisData =data.concat().splice(obj.curr*obj.limit - obj.limit, obj.limit);
                                layui.each(thisData, function(index, item){
                                    var positionStr= "<tr>"
                                        +"<td><input type='text' value='"+item.procInstId+"'  class='layui-input' style='border:none;'></td>"
                                        +"<td><input type='text' value='"+item.applyPerson+"' class='layui-input' style='border:none;'></td>"
                                        +"<td><input type='text' value='"+item.applyCreateTime.slice(0,item.applyCreateTime.indexOf("."))+"' class='layui-input' style='border:none;'></td>"
                                        +"<td><input type='text' value='"+item.position+"' class='layui-input' style='border:none;'></td>"
                                        +"<td><input type='text' value='"+item.approvedPerson+"' class='layui-input' style='border:none;'></td>"
                                        /*  +"<td><input type='text' value='"+item.taskType+"' class='layui-input' style='border:none;'></td>"*/
                                        +"<td><input type='text' value='"+item.applyReason+"' id='applyReasonlist' onmouseover='over1(this.value)' class='layui-input' style='border:none;'></td>"
                                        +"<td><input type='text' value='"+item.repulseReason+"' id='repulseReason' onmouseover='over(this.value)' class='layui-input' style='border:none;'></td>"
                                        +"<td style='width: 300px'>";

                                    //如果是待审批的状态，只可以撤回，不可编辑和重新提交
                                    if (item.processName=="审批岗位"){

                                        positionStr=positionStr + "<button value='" + item.procInstId + "' class='layui-btn' id='backProcess'>撤回</button>";
                                        //还在申请结点，可以编辑之后重新提交
                                    }else{
                                        positionStr=positionStr+ "<button value='" + item.procInstId + "' class='layui-btn' id='update'>编辑</button>"
                                            + "<button value='" + item.id + "' class='layui-btn' id='reSubmit'>发起审批</button>";
                                    }
                                    positionStr=positionStr+ "<button value='" + item.procInstId + "' class='layui-btn' id='history'>查看进度</button></td>"
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
                        layer.alert("撤回成功!", {icon: 6,time:3000})
                        document.getElementById("selectTask").click();
                    } else if (data == 2) {
                        layer.alert("流程未启动或已执行完成，无法撤回!");
                    } else {
                        layer.alert("撤回失败!",{icon: 5,time:3000})
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
var demo1;
function positionApply() {
    $('.select option').remove();
    document.getElementById("applyReason").value=null;
    layui.use('form', function(){
        var form = layui.form; //只有执行了这一步，部分表单元素才会自动修饰成功
        layui.use('layer', function () {
            var layer = layui.layer;
            layer.open({
                type: 1,
                title: "用户岗位申请",   //标题
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

                if(data.positionEntityList!=null){
                    layui.config({
                        base: '../js/'
                    }).extend({
                        xmSelect: 'xm-select'
                    }).use(['xmSelect'], function(){
                        var xmSelect = layui.xmSelect;
                        //渲染多选
                        demo1 = xmSelect.render({
                            el: '#positions',
                            paging: true,
                            pageSize: 2,
                            filterable: true,
                            pageEmptyShow: false,
                            toolbar: {show: true,},
                            autoRow: true,
                            tips:'请选择岗位',
                            searchTips: '您需要什么岗位',
                            empty: '呀, 还没有岗位呢',
                            data: data.positionEntityList,
                        })

                    })
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

    });
}

/**
 * 添加岗位申请流程
 */
function addPosition(classNames) {
    //审批人
    var approvedPerson=document.getElementById("approvedPerson").value;
    //岗位
    var position=(document.getElementsByClassName("label-content"))[0].title;
    //申请原因
    var applyReason=document.getElementById("applyReason").value;
    //用户所属组织
    var orgName = document.getElementById("orgName").value;
    //用户所属组织ID
    var orgId = document.getElementById("orgId").value;
    if (orgName==null||orgName==""){

        layer.alert("请选择用户所属组织!")
        return false;
    }
    if (position==0){
        layer.alert("请选择岗位!")
        return false;
    }
    if(approvedPerson==0){
        layer.alert("请选择审批人!")
        return false;
    }
    if (applyReason==null||applyReason==""){
        layer.alert("请输入申请理由!")
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
                approvedPerson:approvedPerson,
                orgName:orgName,
                orgId:orgId
            },
            success: function (data) {
                if(data==1){
                    layer.alert("你已拥有该岗位，请勿重复申请！");
                }else{
                    layer.close(layer.index-1);
                    layer.alert("申请成功！", {icon: 6,time:3000});
                    document.getElementById("selectTask").click();
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
    document.getElementById("approvedPerson").value=null;
}

/*组织树*/
function org() {
    layui.use('form', function () {
        var form = layui.form; //只有执行了这一步，部分表单元素才会自动修饰成功
        layui.use('layer', function () {
            var layer = layui.layer;
            layer.open({
                type: 1,
                title: "组织树",   //标题
                area: ['300px', '350px'],    //弹窗大小
                shadeClose: false,      //禁止点击空白关闭
                scrollbar: false,      //禁用滚动条
                move: false,       //禁用移动
                scrolling: 'no',
                resize: false,
                closeBtn: 1,
                content: $('#userOrgTree'),
                end: function () {
                    $('#userOrgTree').hide();
                }
            });
        });
        $.ajax({
            url: "/org",
            type: "POST",
            success: function (orgTrees) {
                layui.use(['tree', 'util'], function () {
                    var tree = layui.tree
                        , layer = layui.layer
                        , util = layui.util
                        , data = orgTrees

                    tree.render({
                        elem: '#test',
                        data: data,
                        showLine: true,  //是否开启连接线,
                        spread:false,
                        click:function (obj) {
                            userMove(obj.data.title)
                            $("#orgName").val(obj.data.title);
                            $("#orgId").val(obj.data.id);
                            position(obj.data.id);
                            layer.close(layer.index -1);
                        }
                    });
                })
                form.render();
            }
        })
    })

}



/*
* 确认用户移动的账号
* */

function userMove(nameOrg) {
    $('.avtTable').remove();
    layui.use('form', function () {
        var form = layui.form; //只有执行了这一步，部分表单元素才会自动修饰成功
        layui.use('layer', function () {
            var layer = layui.layer;
            layer.open({
                type: 1,
                title: "账号移动提示",   //标题
                area: ['550px', '300px'],    //弹窗大小
                shadeClose: false,      //禁止点击空白关闭
                scrollbar: false,      //禁用滚动条
                move: false,       //禁用移动
                scrolling: 'no',
                resize: false,
                closeBtn: 1,
                content: $('#act'),
                end: function () {
                    $('#act').hide();
                }
            });
        });
        $.ajax({
            url: "/actMove",
            type: "POST",
            success: function (act) {
                var message = "<table class='layui-table avtTable'><thead><tr><th>账号名</th><th>所属应用</th><th>现在所属组织</th><th>状态</th><th>移动后所属组织</th></tr></thead><tbody>"
                for (var a = 0; a < act.length; a++) {
                    message = message + "<tr><td>" + act[a].loginName + "</td><td>" + act[a].appName + "</td><td>" + act[a].accountOrg + "</td>"
                    if (act[a].status == '1') {
                        message = message + "<td>启用</td>"
                    } else {
                        message = message + "<td>禁用</td>"
                    }
                    if(act[a].accountOrg==''){
                        message = message + "<td>" + act[a].accountOrg + "</td></tr>";
                    }else {
                        message = message + "<td>" + nameOrg + "</td></tr>";
                    }

                }
                message = message + "</tbody>";
                $('#message').append(message);
            }
        })
    })
}

/*
* 修改组织时获取默认值
* */
function position(org) {
    $.ajax({
        url: "/userOrg",
        type: 'POST',
        data: {org: org},
        dataType: "json",
        success: function (res) {
            //清空默认选项
            demo1.setValue([ ])
            //重新赋默认选项
            demo1.setValue(res);

        }
    })

}
