
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
