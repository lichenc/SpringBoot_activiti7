$(document).ready(function(){
    loadingOpen();
    selectProcdefList();
    loadingClose();

    /**
     * 激活或挂起流程
     */
    $("body").on("click", "#updateStatus", function (e) {
        var r = confirm("确定要激活流程吗？");
        if (r == true) {
            //获取taskId
            var value = $(this).val();
            $.ajax({
                url: "/updateProcdefStatus",
                type: "POST",
                data: {
                    id: value,
                    type:'激活'
                },
                success: function (data) {
                    //撤回成功
                    if (data != 0) {
                        alert("激活成功!")
                        document.getElementById("selectTask").click();
                    } else {
                        alert("激活失败!")
                        document.getElementById("selectTask").click();
                    }
                }
            });
        } else {
        }
    });
    $("body").on("click", "#updateStatus1", function (e) {
        var r = confirm("确定要挂起流程吗？");
        if (r == true) {
            //获取taskId
            var value = $(this).val();
            $.ajax({
                url: "/updateProcdefStatus",
                type: "POST",
                data: {
                    id: value,
                    type:'挂起'
                },
                success: function (data) {
                    //撤回成功
                    if (data != 0) {
                        alert("挂起成功!")
                        document.getElementById("selectTask").click();
                    } else {
                        alert("挂起失败!")
                        document.getElementById("selectTask").click();
                    }
                }
            });
        } else {
        }
    });
});

/**
 * 重置操作
 */
function reset() {
    document.getElementById("name").value="";
    document.getElementById("keyName").value="";

}
/**
 * 加载流程列表
 */
function selectProcdefList() {
    var name=document.getElementById("name").value;
    var keyName=document.getElementById("keyName").value;
    //渲染分页
    layui.use('laypage', function () {
        //清空之前的列表记录
        $('.tbody tr').remove();
        $.ajax({
            url: "/selectAllProcdef",
            type: "POST",
            data:{
              name:name,
              keyName:keyName
            },
            success: function (data) {
                //清空之前的列表记录
                $('.tbody tr').remove();
                var laypage = layui.laypage;
                //获取流程集合
                if (data != null) {
                    laypage.render({
                        elem: 'procdefPage',
                        count: data.length, //数据总数，从服务端得到
                        limit: 10,
                        limits:[10,20,30],
                        layout: ['prev', 'page', 'count','next',  'skip'],
                        jump: function (obj) {
                            //模拟渲染
                            document.getElementById('procdefList').innerHTML = function () {
                                var arr = []
                                thisData = data.concat().splice(obj.curr * obj.limit - obj.limit, obj.limit);
                                layui.each(thisData, function (index, item) {
                                    var taskStr =
                                        "<tr>"
                                        + "<td><input type='text' value='" + item.procdefId + "'  class='layui-input' style='border:none;'></td>"
                                        + "<td><input type='text' value='" + item.version + "'  class='layui-input' style='border:none;'></td>"
                                        + "<td><input type='text' value='" + item.name+"'  class='layui-input' style='border:none;'></td>"
                                        + "<td><input type='text' value='" + item.keyName + "'  class='layui-input' style='border:none;'></td>"
                                        + "<td><input type='text' value='" + item.resourceName + "'  class='layui-input' style='border:none;'></td>"
                                        + "<td><input type='text' value='" + item.deploymentId + "'  class='layui-input' style='border:none;'></td>"
                                        if(item.status==1){
                                            taskStr=taskStr  + "<td><input type='text' value='激活'  class='layui-input' style='border:none;'></td>"
                                                + " <td>";
                                        }else{
                                            taskStr=taskStr  + "<td><input type='text' value='挂起'  class='layui-input' style='border:none;'></td>"
                                                + " <td>";
                                        }
                                        if(item.status==2){
                                            taskStr=taskStr+ "<button value='" + item.procdefId + "' class='layui-btn' id='updateStatus'>激活</button>";
                                        }
                                        if(item.status==1){
                                            taskStr=taskStr  + "<button value='" + item.procdefId + "' class='layui-btn' id='updateStatus1'>挂起</button>";
                                        }
                                        + "</td>"
                                        + "</tr>";
                                    arr.push(taskStr);
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
layui.use('upload', function(){
    var upload = layui.upload;

    //执行实例
    var uploadInst = upload.render({
        elem: '#test1' //绑定元素
        ,url: '/upload/' //上传接口
        ,done: function(res){
            //上传完毕回调
        }
        ,error: function(){
            //请求异常回调
        }
    });
});

/**
 * 打开上传弹窗
 */
function upload() {
    document.getElementById("uploadFile").value=null;
    layui.use('form', function() {
        var form = layui.form; //只有执行了这一步，部分表单元素才会自动修饰成功

        layui.use('layer', function () {
            var layer = layui.layer;
            layer.open({
                type: 1,
                title: "上传文件",   //标题
                area: ['400px', '210px'],    //弹窗大小
                shadeClose: false,      //禁止点击空白关闭
                scrollbar: false,      //禁用滚动条
                move: false,       //禁用移动
                scrolling: 'no',
                resize: false,
                closeBtn: 1,
                content: $('#procdefUpload'),
                end: function () {
                    $('#procdefUpload').hide();
                }
            });
        });
    });
}


