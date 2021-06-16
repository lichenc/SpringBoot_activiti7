/**
 * 请求个人信息页面的账号集合
 */
$(document).ready(function(){
    $.ajax({
        url: "/getAccountList",
        type: "POST",
        success: function (data) {
            $("#sn").val(data.sn);
            $("#name").val(data.name);
            $("#orgName").val(data.orgName);
            if(data.sex==1){
                $("#sex").val("男");
            }else{
                $("#sex").val("女");
            }

            //获取账号集合
            var accountlist=data.accountEntities;
            var accountstr;
            //循环遍历账号集合
            for(var i=0;i<accountlist.length;i++){
                //将状态Code换成对应的值
                if(accountlist[i].status==1){
                    accountlist[i].status="启用";
                }else{
                    accountlist[i].status="禁用";
                }
                if(accountlist[i].acctType=="1"){
                    accountlist[i].acctType="一般账号";
                }else if(accountlist[i].acctType=="2"){
                    accountlist[i].acctType="公共账号";
                }else if(accountlist[i].acctType="3"){
                    accountlist[i].acctType="接口账号";
                }
                //拼接账号串
                accountstr= accountstr+"<tr>"
                    +"<td><input type='text' value='"+accountlist[i].appId+"'  class='layui-input' style='border:none;'></td>"
                    +"<td><input type='text' value='"+accountlist[i].appName+"' class='layui-input' style='border:none;'></td>"
                    +" <td><input type='text' value='"+accountlist[i].loginName+"' class='layui-input' style='border:none;'></td>"
                    +" <td><input type='text' value='"+accountlist[i].status+"' class='layui-input' style='border:none;'></td>"
                    +"  <td><input type='text' value='"+accountlist[i].acctType+"' class='layui-input' style='border:none;'></td>"
                    +"  <td></td>"
                    +" <td><button  class='layui-btn'"
                    +" value='"+accountlist[i].id+"' id='account'>查看账号</button >"
                    +"   <button  value='"+accountlist[i].id+"' class='layui-btn'"
                    +"  id='resetPassword'>重置密码</button >"
                    +"  </td>"
                    +" </tr>";
            }
            //获取用户岗位集合
            var positionList=data.positionEntityList;
            var positionStr;
            if(positionList!=null){
                //循环遍历岗位集合，拼接成串
                for(var i=0;i<positionList.length;i++){
                    positionStr=positionStr+"<tr>"
                        +"<td><input type='text' value='"+positionList[i].sn+"'  class='layui-input' style='border:none;'></td>"
                        +"<td><input type='text' value='"+positionList[i].name+"' class='layui-input' style='border:none;'></td>"
                        +" <td><input type='text' value='"+positionList[i].remark+"' class='layui-input' style='border:none;'></td>"
                        +"  <td></td>"
                        +" </tr>";
                }
            }

            //获取用户组集合
            var groupList=data.groupEntities;
            var groupStr;
            if(groupList!=null){
                for(var i=0;i<groupList.length;i++){
                    groupStr=groupStr+"<tr>"
                        +"<td><input type='text' value='"+groupList[i].sn+"'  class='layui-input' style='border:none;'></td>"
                        +"<td><input type='text' value='"+groupList[i].name+"' class='layui-input' style='border:none;'></td>"
                        +" <td><input type='text' value='"+groupList[i].remark+"' class='layui-input' style='border:none;'></td>"
                        +"<td></td>"
                        +" </tr>";
                }
            }
            //渲染分页
            layui.use('laypage', function () {
                var laypage = layui.laypage;
                //账号Tab分页
                laypage.render({
                    elem: 'accountPage'
                    , count: data.accountCount //数据总数，从服务端得到
                });
                if(data.positionCount!=null){
                    //岗位Tab分页
                    laypage.render({
                        elem: 'positionPage'
                        , count: data.positionCount //数据总数，从服务端得到
                    })
                }
                if(ata.groupCount!=null){
                    //用户组Tab分页
                    laypage.render({
                        elem: 'groupPage'
                        , count: data.groupCount //数据总数，从服务端得到
                    })
                }
            });
            //给表格追加账号tr
            $("#accountList").append(accountstr);
            //给表格追加岗位tr
            $("#positionList").append(positionStr)
            $("#groupList").append(groupStr)
        }, error: function (XMLHttpRequest, textStatus, errorThrown) {
            alert(XMLHttpRequest.status);
        }

    });

});
var accountID;
/**
 * 打开修改密码弹窗
 * */
$("body").on("click", "#resetPassword", function (e) {
    //获取账号ID
    var accountId = $(this).val();
    accountID = accountId;
    layui.use('layer', function () {
        var layer = layui.layer;
        layer.open({
            type: 1,
            title: "重置密码",   //标题
            area: ['360px', '250px'],    //弹窗大小
            shadeClose: false,      //禁止点击空白关闭
            scrollbar: false,      //禁用滚动条
            move: false,       //禁用移动
            scrolling: 'no',
            resize: false,
            closeBtn: 1,
            content: $('#updatePassword'),
            end: function () {
                $('#updatePassword').hide();
            }
        });
    });
    document.getElementById("password").value = null;
    document.getElementById("repassword").value = null;

});

/**
 * 修改密码
 * */
function updatePassword() {
    //获取新密码
    var password = document.getElementById("password").value;
    //获取确认密码
    var repassword = document.getElementById("repassword").value;
    //判断两次密码是否一致
    if (repassword != password) {
        layer.alert("新密码与确认密码不一致！");
        return false;
    }
    if (password != null && password != '' && repassword != null && repassword != '' && repassword == password) {
        $.ajax({
            url: "/updateAccount",
            type: "POST",
            dataType: "json",
            data: {
                id: accountID,
                loginPwd: password
            },
            success: function (data) {
                if (data != 0) {
                    layer.alert("修改成功！");
                    layer.close(layer.index - 1);
                } else {
                    layer.alert("修改失败！");
                }
            }, error: function (XMLHttpRequest, textStatus, errorThrown) {
                alert(XMLHttpRequest.status);
            }
        });
    }
}

/**
 * 查看账号详情
 */
$("body").on("click", "#account", function (e) {
    //获取账号ID
    var
        accountId = $(this).val();
    $.ajax({
        url: "/getAccountDetail",
        type: "POST",
        data: {
            id: accountId
        },
        success: function (data) {
            $("#accountName").val(data.loginName);
            if (data.acctType == 1) {
                $("#accountType").val("一般账号");
            } else if (data.acctType == 2) {
                $("#accountType").val("公共账号");
            } else {
                $("#accountType").val("接口账号");
            }
            if (data.status == 1) {
                $("#accountStatus1").attr("checked", "checked");
            } else {
                $("#accountStatus2").attr("checked", "checked");
            }
        }, error: function (XMLHttpRequest, textStatus, errorThrown) {
            alert(XMLHttpRequest.status);
        }

    });
    //打开账号详情弹窗
    layui.use('layer', function () {
        var layer = layui.layer;
        layer.open({
            type: 1,
            title: "账号详情",   //标题
            area: ['360px', '300px'],    //弹窗大小
            shadeClose: false,      //禁止点击空白关闭
            scrollbar: false,      //禁用滚动条
            move: false,       //禁用移动
            scrolling: 'no',
            resize: false,
            closeBtn: 1,
            content: $('#selectAccount'),
            end: function () {
                $('#selectAccount').hide();
            }
        });
    });

});
//渲染表单元素
layui.use(['form', 'jquery', function () {
    var form = layui.form;
    $ = layui.jquery;
}]);