layui.use('element', function () {
    var $ = layui.jquery;
    let element = layui.element;
    //首页内容高度
    $("#indexTabContent").css("height", ($(".layui-body")[0].offsetHeight - 94) + "px");

    /**
     * 菜单点击事件
     */
    $("body").on("click", ".huanzi-menu", function (e) {
        if ($(this).next("dl").length > 0) {
            return;
        }
        let url = $(this).data("url");
        let id = $(this).data("id");
        let text = $(this).text();

        //如果不存在
        if ($(".layui-tab-title").find("li[lay-id='" + id + "']").length <= 0) {
            element.tabAdd('demo', {
                title: text,
                //iframe子页面onload新增监听src为/loginPage是父页面跳转/loginPage，解决“俄罗斯套娃”问题
                content: '<iframe data-frameid="' + id + '" scrolling="auto" frameborder="0" src=\"' + url + '\" style="width: 100%;height: 100%"></iframe>',
                id: id
            });
        }
        element.tabChange('demo', id);
    });
    /**
     * 监听浏览器可视高度，自适应设置高度
     */
    window.onresize = function (ev) {
        let h = 94;
        if ($(".layui-show .tab-content-div")[0].nodeName === "IFRAME") {
            h = 61;
        }
        $(".tab-content-div").css("height", ($(".layui-body")[0].offsetHeight - h) + "px");
    }

})
;


/**
 * 退出登录确认
 */
function logout() {
    var r = confirm("确定要退出登录吗？");
    if (r == true) {
        var logout = document.getElementsByClassName("logout")[0];
        logout.setAttribute("href", "/logout");
    } else {

    }
}

//数据分页
layui.use('laypage', function () {
    var laypage = layui.laypage;
    var accountCount = '[[${accountCount}]]';

    //执行一个laypage实例
    laypage.render({
        elem: 'accountPage'
        , count: accountCount //数据总数，从服务端得到
    });
});
var accountID;
/**
 * 打开修改密码弹窗
 * */
$("body").on("click", "#resetPassword", function (e) {
    //获取账号ID
    let accountId = $(this).data("id");
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
    let
        accountId = $(this).data("id");
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