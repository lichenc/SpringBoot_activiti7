function formatDate(date) {
    var d = new Date(date),
        month = '' + (d.getMonth() + 1),
        day = '' + d.getDate(),
        year = d.getFullYear();

    if (month.length < 2) month = '0' + month;
    if (day.length < 2) day = '0' + day;

    return [year, month, day].join('-');
}
/**
 * 请求个人信息页面的账号集合
 */
$(document).ready(function(){
    $.ajax({
        url: "/getAccountList",
        type: "POST",
        success: function (data) {
            $("#id").val(data.id);
            $("#sn").val(data.sn);
            $("#name").val(data.name);
            $("#orgName").val(data.orgName);
            if(data.sex==1){
                $("#sex").val("男");
            }else{
                $("#sex").val("女");
            }
            if(data.status==1){
                $("#status").val("在职");
            }else{
                $("#status").val("离职");
            }
            $("#userTypeId").val(data.userTypeId);
            $("#telephone").val(data.telephone);
            $("#email").val(data.email);
            $("#createTime").val(formatDate(data.createTime));
            $("#optUser").val(data.optUser);
            //获取账号集合
            var accountlist=data.accountEntities;
            //var accountstr;
            //循环遍历账号集合
           // for(var i=0;i<accountlist.length;i++){

            layui.use('laypage', function () {
                var laypage = layui.laypage;
                //账号Tab分页
                laypage.render({
                    elem: 'accountPage',
                    count: accountlist.length, //数据总数，从服务端得到
                    limit:4,
                    jump: function (obj) {
                        //模拟渲染
                        document.getElementById('accountList').innerHTML = function () {
                            var arr = []
                            thisData = accountlist.concat().splice(obj.curr * obj.limit - obj.limit, obj.limit);
                            layui.each(thisData, function (index, item) {
                                //将状态Code换成对应的值
                                if(item.status==1){
                                    item.status="启用";
                                }else{
                                    item.status="禁用";
                                }
                                if(item.acctType=="1"){
                                    item.acctType="一般账号";
                                }else if(item.acctType=="2"){
                                    item.acctType="公共账号";
                                }else if(item.acctType="3"){
                                    item.acctType="接口账号";
                                }
                                //拼接账号串
                                var accountstr = "<tr>"
                                    + "<td><input type='text' value='" + item.appId + "'  class='layui-input' style='border:none;'></td>"
                                    + "<td><input type='text' value='" + item.appName + "' class='layui-input' style='border:none;'></td>"
                                    + " <td><input type='text' value='" + item.loginName + "' class='layui-input' style='border:none;'></td>"
                                    + " <td><input type='text' value='" + item.status + "' class='layui-input' style='border:none;'></td>"
                                    + "  <td><input type='text' value='" + item.acctType + "' class='layui-input' style='border:none;'></td>"
                                    + "  <td></td>"
                                    + " <td><button  class='layui-btn'"
                                    + " value='" + item.id + "' id='account'>查看账号</button >"
                                    + "   <button  value='" + item.id + "' class='layui-btn'"
                                    + "  id='resetPassword'>重置密码</button >"
                                    + "  </td>"
                                    + " </tr>";
                                arr.push(accountstr);
                            });
                            return arr.join('');
                        }();
                    }
                });
            });

           // }
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
                /*  laypage.render({
                     elem: 'accountPage'
                     , count: data.accountCount //数据总数，从服务端得到
                 });*/
                if(data.positionCount!=null){
                    //岗位Tab分页
                    laypage.render({
                        elem: 'positionPage'
                        , count: data.positionCount //数据总数，从服务端得到
                    })
                }
                if(data.groupCount!=null){
                    //用户组Tab分页
                    laypage.render({
                        elem: 'groupPage'
                        , count: data.groupCount //数据总数，从服务端得到
                    })
                }
            });
            //给表格追加账号tr
           // $("#accountList").append(accountstr);
            //给表格追加岗位tr
            $("#positionList").append(positionStr)
            $("#groupList").append(groupStr)
            getExtraAttrs();
        }, error: function (XMLHttpRequest, textStatus, errorThrown) {
            alert(XMLHttpRequest.status);
        }

    });

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

});

/**
 * 获取扩展字段
 */
function getExtraAttrs() {
    $.ajax({
        url: "/getExtraAttrs",
        type: "POST",
        success: function (data) {
            //转换为json对象
            var last= JSON.parse(data);
            var extraAttrs=last.extraAttrs;
            var extraAttrsStr="";
            var iden=0;
            //循环遍历岗位集合，拼接成串
            for(var key in extraAttrs){
                     extraAttrsStr =extraAttrsStr+"<div class='layui-inline' id='jian"+iden+"'>"
                        + "<label class='layui-form-label' id='iden"+iden+"'></label>"
                        + "<div class='layui-input-inline'>"
                        + "<input type='text'  autocomplete='off' id='createTime'"
                        + " class='layui-input' style='width: 300px' value='"+extraAttrs[key]+"'/>"
                        + "</div>"
                        + " </div>";
                     iden++;

            }
            $("#block").append(extraAttrsStr);
            var iden1=0;
            for(var key in extraAttrs){
                document.getElementById("iden"+iden1).innerText=key;
                if (iden1!=0){
                    $('#jian'+iden1).attr('style', 'padding-left: 250px');
                }

                iden1++;

            }

        }
    });
}
var accountID;


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
                if (data == 1) {
                    layer.alert("修改成功！");
                    layer.close(layer.index - 1);
                }else if(data==11){
                    layer.alert("密码长度必须在8-15位之间！");
                }else if(data==22){
                    layer.alert("密码中必须包含数字！");
                }else if(data==33){
                    layer.alert("密码中必须包含特殊字符！");
                }else if(data==44){
                    layer.alert("密码中必须包含英文字母！");
                }else{
                    layer.alert("注册失败！");
                }

            }, error: function (XMLHttpRequest, textStatus, errorThrown) {
                alert(XMLHttpRequest.status);
            }
        });
    }
}


//渲染表单元素
layui.use(['form', 'jquery', function () {
    var form = layui.form;
    $ = layui.jquery;
}]);

/**
 * 折叠模块收起展开事件
 */
function changeShow(){

    var span=$("#span").html();

    if(span=="查看↓↓↓↓"){
        $("#span").html("收起↑↑↑↑");
        document.getElementById("block").style.display="block";
    }else{
        $("#span").html("查看↓↓↓↓");
        document.getElementById("block").style.display="none";
    }
}
