//查询历史任务
$(document).ready(function() {
        $.ajax({
            url: "/hisTask",
            type: "POST",
            data: {
                id:""
            },
            success: function (dat) {
                //渲染分页
                layui.use('laypage', function () {
                    //账号Tab分
                    var laypage = layui.laypage;
                    laypage.render({
                        elem: 'hisTaskPages',
                        count: dat.length, //数据总数，从服务端得到
                        limit: 4,
                        limits: [1, 2, 3, 4, 5, 10, 20, 30, 50, 100],
                        layout: ['count', 'prev', 'page', 'next','skip', 'limit'],
                        jump: function (obj) {
                            //模拟渲染
                            document.getElementById('hisTasks').innerHTML = function () {
                                var arr = [];
                                thisData = dat.concat().splice(obj.curr * obj.limit - obj.limit, obj.limit);
                                layui.each(thisData, function (index, item) {
                                    var positionStr = "<tr>"
                                        + "<td>" + item.id + "</td>"
                                        + "<td>" + item.taskType + "</td>"
                                        + "<td>" + item.applyPerson + "</td>"
                                        + "<td>" + item.approvedPerson + "</td>"
                                        + "<td>" + item.createTime + "</td>"
                                        + "<td>" + item.endTime + "</td>"
                                        + "<td>" + item.status + "</td><td style='width: 210px;'>"
                                        + "<button value='" + item.processInstanceId + "' class='layui-btn'   id='processView'>任务流程视图</button>"
                                        + "<button value='" + item.processInstanceId + "' class='layui-btn' id='selectParticular'>详细信息</button></td></tr>";
                                    arr.push(positionStr);
                                });
                                return arr.join('');

                            }();

                        }
                    });
                });
            }
        })
})
function histask(){
    var taskTy=document.getElementById("taskTy").value;
    var applyPer=document.getElementById("applyPer").value;
    var taskStatus=document.getElementById("taskStatus").value;
    $.ajax({
        url: "/hisTask",
        type: "POST",
        data: {
            id:"",
            taskTy:taskTy,
            applyPer:applyPer,
            taskStatus:taskStatus
        },
        success: function (dat) {
            //渲染分页
            layui.use('laypage', function () {
                //账号Tab分
                var laypage = layui.laypage;
                laypage.render({
                    elem: 'hisTaskPages',
                    count: dat.length, //数据总数，从服务端得到
                    limit: 4,
                    limits: [1, 2, 3, 4, 5, 10, 20, 30, 50, 100],
                    layout: ['count', 'prev', 'page', 'next','skip', 'limit'],
                    jump: function (obj) {
                        //模拟渲染
                        document.getElementById('hisTasks').innerHTML = function () {
                            var arr = [];
                            thisData = dat.concat().splice(obj.curr * obj.limit - obj.limit, obj.limit);
                            layui.each(thisData, function (index, item) {
                                var positionStr = "<tr>"
                                    + "<td>" + item.id + "</td>"
                                    + "<td>" + item.taskType + "</td>"
                                    + "<td>" + item.applyPerson + "</td>"
                                    + "<td>" + item.approvedPerson + "</td>"
                                    + "<td>" + item.createTime + "</td>"
                                    + "<td>" + item.endTime + "</td>"
                                    + "<td>" + item.status + "</td><td style='width: 210px;'>"
                                    + "<button value='" + item.processInstanceId + "' class='layui-btn'   id='processView'>任务流程视图</button>"
                                    + "<button value='" + item.processInstanceId + "' class='layui-btn' id='selectParticular'>详细信息</button></td></tr>";
                                arr.push(positionStr);
                            });
                            return arr.join('');

                        }();

                    }
                });
            });
        }
    })
}


/*历史任务详细信息*/
$(document).ready(function() {
    $("body").on("click", "#selectParticular", function (e) {
        var value = $(this).val();
        $('.div').remove();
        layui.use('form', function () {
            var form = layui.form; //只有执行了这一步，部分表单元素才会自动修饰成功
            layui.use('layer', function () {
                var layer = layui.layer;
                layer.open({
                    type: 1,
                    title: "历史任务详细信息",   //标题
                    area: ['400px', '400px'],    //弹窗大小
                    shadeClose: false,      //禁止点击空白关闭
                    scrollbar: false,      //禁用滚动条
                    move: false,       //禁用移动
                    scrolling: 'no',
                    resize: false,
                    closeBtn: 1,
                    content: $('#histaskParticular'),
                    end: function () {
                        $('#histaskParticular').hide();
                    }
                });
            });

            $.ajax({
                url: "/hisTask",
                type: "POST",
                dataType: "json",
                data: {
                    id: value
                },
                success: function (data) {
                    var userName = "<div class='layui-form-item div'><label class='layui-form-label' style='font-size:12px;'>申请者： </label>"
                        + "<div class='layui-input-inline layui-form' lay-filter='test'>"
                        + " <input id='userName' type='text' disabled   lay-vertype='tips' required class='layui-input' value='" + data[0].applyPerson + "'  ></div></div>"

                    $('#userNames3').append(userName);
                    var selectTs = "<div class='layui-form-item div' ><label class='layui-form-label' style='font-size:12px;'>任务类型： </label>"
                        + "<div class='layui-input-inline layui-form' lay-filter='test2'>"
                        + " <input id='taskType' class='layui-input' lay-filter='taskty' disabled lay-verify='required'   required value='" + data[0].taskType + "'  ></div></div>"
                    $('#taskTapys3').append(selectTs);
                    var selectSa = "<div class='layui-form-item div'><label class='layui-form-label' style='font-size:12px;'>审批人： </label>"
                        + "<div class='layui-input-inline layui-form' lay-filter='test2'>"
                        + " <input id='audit'  class='select layui-input' disabled  lay-verify='required'   required value='" + data[0].approvedPerson + "'  ></div></div>"

                    $('#aud3').append(selectSa);
                        if(data[0].taskType!="用户移动"){

                            if(data[0].taskType!="帐号启用") {
                                var selectactOrg = "<div class='layui-form-item div'><label class='layui-form-label' style='font-size:12px;'>账号所属组织： </label>"
                                    + "<div class='layui-input-inline layui-form' lay-filter='test2'>"
                                    + " <input type='text' id='accountName' disabled lay-vertype='accountField' required class='layui-input' value='" + data[0].accountOrg + "'  ></div></div>"

                                $('#field3').append(selectactOrg);
                                var select = "<div class='layui-form-item div'><label class='layui-form-label' style='font-size:12px;'>帐号类型： </label>"
                                    + "<div class='layui-input-inline layui-form' lay-filter='test2'>"
                                if (data[0].actType == "1") {
                                    select = select + "<input id='actType' class='layui-input' disabled lay-filter='accountType'  lay-verify='required'   required  value='一般账号'></div></div>"

                                } else if (data[0].actType == "2") {
                                    select = select + "<input id='actType' class='layui-input' disabled lay-filter='accountType'  lay-verify='required'   required  value='公共账号'></div></div>"

                                } else if (data[0].actType == "3") {
                                    select = select + "<input id='actType' class='layui-input' disabled lay-filter='accountType'  lay-verify='required'   required  value='接口账号'></div></div>"

                                }
                                $('#field3').append(select);
                                for (var textVl = 0; textVl < data[0].textLists.length; textVl++) {
                                    var textVls = "<div id='div' class='layui-form-item div actDiv actDivs'><label class='layui-form-label labelText' style='font-size:12px;'>" + data[0].textLists[textVl].remarks + "：</label>"
                                        + "<div class='layui-input-inline layui-form' lay-filter='test2'>"
                                        + " <input type='text' class='text layui-input' disabled  required   value='" + data[0].textLists[textVl].defaultValues + "'  name='account' ></div></div>"
                                    $('#field3').append(textVls);
                                }
                                for (var passwordVl = 0; passwordVl < data[0].passwordLists.length; passwordVl++) {
                                    var passwordVls = "<div id='div' class='layui-form-item div actDiv actDivs'><label class='layui-form-label labelText' style='font-size:12px;'>" + data[0].passwordLists[passwordVl].remarks + "：</label>"
                                        + "<div class='layui-input-inline layui-form' lay-filter='test2'>"
                                        + " <input type='password' class='text layui-input' disabled  required   value='" + data[0].passwordLists[passwordVl].defaultValues + "'  name='account' ></div></div>"
                                    $('#field3').append(passwordVls);
                                }
                                for (var selectVl = 0; selectVl < data[0].selectLists.length; selectVl++) {
                                    var selectVls = "<div id='div' class='layui-form-item div actDiv actDivs'><label class='layui-form-label labelText' style='font-size:12px;'>" + data[0].selectLists[selectVl].remarks + "：</label>"
                                        + "<div class='layui-input-inline layui-form' lay-filter='test2'>"
                                        + " <input type='text' class='text layui-input'  disabled required   value='" + data[0].selectLists[selectVl].defaultValues + "'  name='account' ></div></div>"
                                    $('#field3').append(selectVls);
                                }
                                if(data[0].dateLists.length!=0){
                                    for (var dateVl = 0; dateVl < data[0].dateLists.length; dateVl++) {
                                        var dateVls = "<div id='div' class='layui-form-item div actDiv actDivs'><label class='layui-form-label labelText' style='font-size:12px;'>" + data[0].dateLists[dateVl].remarks + "：</label>"
                                            + "<div class='layui-input-inline layui-form' lay-filter='test2'>"
                                            + " <input type='text' class='text layui-input' disabled  required   value='" + data[0].dateLists[dateVl].defaultValues + "'  name='account' ></div></div>"
                                        $('#field3').append(dateVls);
                                    }
                                }
                            }
                            var selectA = "<div class='layui-form-item div'><label class='layui-form-label' style='font-size:12px;'>系统资源： </label>"
                                + "<div class='layui-input-inline layui-form' lay-filter='test2'>"
                                + " <input id='appName' class='layui-input' lay-filter='app'  disabled lay-verify='required'   required value='" + data[0].app + "'  ></div></div>"
                            selectA = selectA + "</select></div></div>"
                            $('#types3').append(selectA);

                            var selectName2 = "<div class='layui-form-item div'><label class='layui-form-label' style='font-size:12px;'>登陆名： </label>"
                                + "<div class='layui-input-inline layui-form' lay-filter='test2'>"
                                + " <input type='text' id='accountName' disabled lay-vertype='accountField' required class='layui-input' value='" + data[0].account + "'  ></div></div>"

                            $('#field3').append(selectName2);

                        }else {
                            var orgName = "<div class='layui-form-item div'><label class='layui-form-label' style='font-size:12px;'>用户所属组织： </label>"
                                + "<div class='layui-input-inline layui-form' lay-filter='test'>"
                                + " <input id='orgName' type='text' disabled  lay-vertype='tips' required class='layui-input' value='" + data[0].orgName + "'  ></div></div>"

                            $('#orgNames3').append(orgName);
                            var postion = "<div class='layui-form-item div'><label class='layui-form-label' style='font-size:12px;'>岗位： </label>"
                                + "<div class='layui-input-inline layui-form' lay-filter='test'>"
                                + " <input id='position' type='text' disabled  lay-vertype='tips' required class='layui-input' value='" + data[0].position + "'  ></div></div>"

                            $('#orgNames3').append(postion);
                        }

                    var applyReasont = "<div  class='layui-form-item div' id='div'><label class='layui-form-label' style='font-size:12px;'>申请理由： </label>"
                        + "<div class='layui-input-inline layui-form' lay-filter='test2'>"
                        + " <p  id='applyReason' required  disabled autocomplete='off' class='layui-text'  >" + data[0].applyReason + "</p></div></div>"
                    $('#applyReasonss3').append(applyReasont);

                    var selectRole = "<div class='layui-form-item div'><label class='layui-form-label' style='font-size:12px;'>审批角色： </label>"
                        + "<div class='layui-input-inline layui-form' lay-filter='test2'>"
                        + " <input id='role'  class='select layui-input' disabled  lay-verify='required'   required value='" + data[0].role + "'  ></div></div>"

                    $('#roles3').append(selectRole);
                    if (data[0].comment.length == 0) {

                    } else {
                        var approvalOpinion = "<div  class='layui-form-item div' id='div'><label class='layui-form-label' style='font-size:12px;'>历史批注： </label>"
                            + "<div class='layui-input-inline layui-form' lay-filter='test2'>"
                            + " <p   disabled autocomplete='off' class='layui-text'  >"
                        for (var co = 0; co < data[0].comment.length; co++) {
                            var num = co + 1;
                            approvalOpinion = approvalOpinion + "第" + num + "次批注</br>批注人：" + data[0].comment[co].userId + "</br>批注时间：" + data[0].comment[co].time + "</br>批注内容：" + data[0].comment[co].fullMessage + "</br></br>"
                        }
                        approvalOpinion = approvalOpinion + "</p></div></div>"
                        $('#approvalOpinions3').append(approvalOpinion);
                    }
                    form.render('select', 'test2');
                }
            })

        })
    })
})


/*历史任务流程视图*/
$(document).ready(function() {
    $("body").on("click", "#processView", function (e) {
        var proId = $(this).val();
        layui.use('form', function () {
            var form = layui.form;
            layui.use('layer', function () {
                var layer = layui.layer;
                layer.open({
                    type: 1,
                    title: "历史任务流程图",   //标题
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
        $("#picture").attr("src", "../getProcefPicture?taskId=" + proId + "");
    });
})


$(document).ready(function() {
    layui.use(['table', 'layer'], function () {
        var table = layui.table;
        //导出表格配置
        table.render({
            elem: '#layui-table',
            id: 'exportTable',
            title: '历史任务数据报表',
            cols: [[ //表头
                {
                    field: 'uid',
                    title: 'ID',
                }, {
                    field: 'uname',
                    title: '姓名',
                }
            ]]
        });
        //导出事件
        $('#export').on('click', function () {
            //使用ajax请求获取所有数据
            $.ajax({
                url: "/hisTask",
                type: 'post',
                success: function (res) {
                    //使用table.exportFile()导出数据
                    table.exportFile('exportTable', res.date, 'xls');
                }
            });
        });
    });
})
