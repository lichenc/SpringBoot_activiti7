
/**
 * 加载岗位申请列表
 * */
$(document).ready(function(){
    getPositionList();
});

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
                                    +"<td><input type='text' value='"+formatDate(item.applyCreateTime)+"' class='layui-input' style='border:none;'></td>"
                                    +"<td><input type='text' value='"+item.position+"' class='layui-input' style='border:none;'></td>"
                                    +"<td><input type='text' value='"+item.approvedPerson+"' class='layui-input' style='border:none;'></td>"
                                    +"<td><input type='text' value='"+item.taskType+"' class='layui-input' style='border:none;'></td>"
                                    +"<td><input type='text' value='"+item.applyReason+"' class='layui-input' style='border:none;'></td>"
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
                area: ['400px', '400px'],    //弹窗大小
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
                if(data!=null){
                    var positionList=data.positionEntityList;
                    for(var i=0;i<positionList.length;i++){
                        var positionStro=  "<option value='0'>请选择岗位</option><option  value='"+positionList[i].name+"'>"+positionList[i].name+"</option>";
                    }
                    $('#position').append(positionStro);
                    document.getElementById("applyPerson").value=data.userSn;
                    form.render();
                }
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
    if (position==0){
        layer.alert("请选择岗位!")
        return false;
    }
    if (applyReason==null||applyReason==""){
        layer.alert("输入申请理由!")
        return false;
    }
    if(position!=0 &&position!=null&& applyReason!=null && applyReason!="") {
        $.ajax({
            url: "/addPositionProcess",
            type: "POST",
            dataType: "json",
            data: {
                position: position,
                applyReason: applyReason
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
function formatDate(date) {
    console.log(date);
// date = new Date();
    date = new Date(Date.parse(date.replace(/-/g, "/"))); //转换成Data();
    console.log(date);
    var y = date.getFullYear();
    console.log(y);
    var m = date.getMonth() + 1;
    m = m < 10 ? '0' + m : m;
    var d = date.getDate();
    d = d < 10 ? ('0' + d) : d;
    return y + '-' + m + '-' + d;
}