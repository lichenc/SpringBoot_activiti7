
layui.use('laydate', function() {
    var laydate = layui.laydate;
    laydate.render({
        elem: '#times',
        type: 'datetime',
        range: true
    });
})

/*
* 帐号表单联动属性
* */
layui.use(['layer', 'jquery', 'form'], function () {
    var layer = layui.layer,
        $ = layui.jquery;
        form = layui.form;

    form.on('select(taskty)', function(data){
        var ty=data.value;
        $('.div').remove();
       // $('#div').empty();
            $.ajax({
                url:"/actApply",
                type:'POST',
                data:{aName:ty},
                dataType: "json",
                success:function(res) {
                    if(res.length==0&&ty=='帐号修改'){
                        layer.alert("您当前还没有启用的账号，请先新增或者启用账号")
                    }else if(res.length==0&&ty=='帐号启用'){
                        layer.alert("您当前没有禁用的账号，请先新增账号")
                    }else {
                        var selectName = "<div id='div' class='layui-form-item delForm div'><label class='layui-form-label' style='font-size:12px;'>系统资源： </label>"
                            + "<div class='layui-input-inline layui-form' lay-filter='test'>"
                            + "<select id='appName'  lay-filter='app'   lay-verify='required'   required   class='select'>"
                            + "  <option></option>"
                        for (var i = 0; i < res.length; i++) {
                            selectName = selectName + "<option value='" + res[i].name + "'>" + res[i].name + "</option>"
                        }
                        selectName = selectName + "</select></div></div>"
                        $('#types').append(selectName);
                        form.render('select', 'test');
                    }
                }
            })

    });



    form.on('select(app)', function(data){
        $('.actDiv').remove();
        var ActType=document.getElementById("taskType").value;
        var app=data.value;
        if(ActType=='帐号新增'){
        $.ajax({
            url:"/act",
            type:'POST',
            data:{app:app},
            dataType: "json",
            success:function(sea) {
                for (var j=0;j<sea.length;j++){
                    //获取的数据
                    var account=sea[j];
                    //字段名
                    var accountName=account.names;
                    //字段名称
                    var accountRemark=account.remarks;
                    //字段默认值
                    var accountValue=account.defaultValues;
                    //字段类型
                    var accountType=account.inputTypes;
                    //字段是否可以为空，1可以为空 2不允许为空
                    var accountRequrieds=account.isRequrieds;
                    //字段是否可以新增，1可以 2不可以
                    var accountInserts=account.isInserts;
                    //字段是否可以编辑，1可以编辑 2不可以编辑
                    var accountisEdits=account.isEdits;

                    //基本字段由于iam后台改变类型等状态不会变化，所以这里也不做动态改变
                    if(accountName=='LOGIN_PWD'||accountName=='USER_SN'||accountName=='STATUS'){

                    } else if(accountName=='LOGIN_NAME'){
                        var selectName  = "<div id='div actDiv' class='layui-form-item delForm actDiv div'><label class='layui-form-label' style='font-size:12px;'>登陆名： </label>"
                            + "<div class='layui-input-inline layui-form' lay-filter='test'>"
                            +" <input type='text' id='accountName'  lay-vertype='accountField' required class='layui-input' value='"+accountValue+"'  ></div></div>"

                        $('#field').append(selectName);
                    }else if(accountName=='ACCT_TYPE'){
                        var select= "<div id='div actDiv' class='layui-form-item delForm actDiv div'><label class='layui-form-label' style='font-size:12px;'>"+accountRemark +"： </label>"
                            + "<div class='layui-input-inline layui-form' lay-filter='test'>"
                            +"<select id='actType'  lay-filter='accountType'  lay-verify='required'   required  class='select'>"
                        if(accountValue==1){
                         select=select+"<option value='"+accountValue+"'>一般账号</option>" +
                             "<option value='2'>公共账号</option>" +
                            "<option value='3'>接口账号</option>"
                        }else if(accountValue==2){
                            select=select+ "<option value='"+accountValue+"'>公共账号</option>" +
                                "<option value='1'>一般账号</option>" +
                                "<option value='3'>接口账号</option>"
                        }else if(accountValue==3){
                            select=select+"<option value='"+accountValue+"'>接口账号</option>"
                            +"<option value='1'>一般账号</option>" +
                                "<option value='2'>公共账号</option>"
                        }
                        select=select+"</select></div></div>"
                        $('#field').append(select);
                    }else if(accountName=='ACCOUNT_ORG'){
                        var selectO= "<div id='div actDiv' class='layui-form-item delForm actDiv div'><label class='layui-form-label' style='font-size:12px;'>"+accountRemark +"：</label>"
                            + "<div class='layui-input-inline layui-form' lay-filter='test'>"
                            +"<input type='text' id='accountOrgId' style='display:none' class='' />"
                            +" <input onfocus='org();' type='text' id='accountOrg'  lay-vertype='accountField' required class='layui-input'  value='"+accountValue+"'   ></div></div>"
                        $('#field').append(selectO);
                    }else {

                        //扩展字段动态显示
                        if(accountType=='text'){

                            var selectT= "<div id='div actDiv' class='layui-form-item delForm actDiv div'><label class='layui-form-label labelText'  style='font-size:12px;'>"+accountRemark +"：</label>"
                                + "<div class='layui-input-inline layui-form' lay-filter='test'>"
                                +"<input type='text' style='display:none' class='textKey' value='"+accountName+"' />"
                                +" <input type='text' class='text layui-input'  lay-vertype='accountField' required   value='"+accountValue+"'  name='account' ></div></div>"
                            $('#field').append(selectT);

                        }else if(accountType=='select'){
                            //下拉框内要选择的值
                            var selectS= "<div id='div actDiv' class='layui-form-item delForm actDiv div'><label class='layui-form-label labelSelectss' style='font-size:12px;'>"+accountRemark +"： </label>"
                                + "<div class='layui-input-inline layui-form' lay-filter='test'>"
                                +"<input type='text' style='display:none' class='selectKey' value='"+accountName+"' />"
                                +"<select type='text' class='selectss'  lay-filter='accountField'  lay-verify='required'   required  >"
                                     +"<option value='0'></option>"
                            if(account.listCompants.toString()!=null||account.listCompants.toString()!=""){
                                for(var cp=0;cp<account.listCompants.length;cp++){
                                    for ( var key in account.listCompants[cp] ) {
                                        selectS = selectS + "<option value='" + key + "'>"+account.listCompants[cp][key]+"</option>"
                                    }
                                }
                            }
                            selectS=selectS+"</select></div></div>"
                            $('#field').append(selectS);
                        }else if(accountType=='password'){

                            var selectP= "<div id='div actDiv' class='layui-form-item delForm actDiv div'><label class='layui-form-label labelPassword' style='font-size:12px;'>"+accountRemark +"：</label>"
                                + "<div class='layui-input-inline layui-form' lay-filter='test'>"
                                +"<input type='text' style='display:none' class='passwordKey' value='"+accountName+"' />"
                                +" <input type='password'  class='password layui-input'   lay-vertype='accountField' required   value='"+accountValue+"'  ></div></div>"
                            $('#field').append(selectP);

                        }else if(accountType=='date'){
                            var selectD= "<div id='div actDiv' class='layui-form-item delForm actDiv div'><label class='layui-form-label labelDate' style='font-size:12px;'>"+accountRemark +"：</label>"
                                + "<div class='layui-input-inline layui-form' lay-filter='test'>"
                                +"<input type='text' style='display:none' class='dateKey' value='"+accountName+"' />"
                                +" <input type='date'  class='date layui-input' placeholder='yyyy-MM-dd'  lay-vertype='accountField' required   value='"+accountValue+"'   ></div></div>"
                            $('#field').append(selectD);
                        }
                    }
                }
                form.render('select', 'test');
            }
        })
        }else if(ActType=='帐号启用'){
            $.ajax({
                url:"/enable",
                type:'POST',
                data:{app:app},
                dataType: "json",
                success:function(en) {
                        var select = "<div id='div actDiv' class='layui-form-item delForm actDiv div'><label class='layui-form-label' style='font-size:12px;'>登录名： </label>"
                            + "<div class='layui-input-inline layui-form' lay-filter='test'>"
                            + "<select id='account'      required  class='select'>"
                            + "  <option></option>"
                            for (var e = 0; e < en.length; e++) {
                                select = select + "<option value='" + en[e].loName + "'>" + en[e].loName + "</option>"
                            }
                        select = select + "</select></div></div>"
                        $('#field').append(select);
                        form.render('select', 'test');
                }
            });
        } else if( ActType=='帐号修改'){
            $.ajax({
                url: "/disableAcct",
                type: 'POST',
                data: {app: app},
                dataType: "json",
                success: function (disable) {
                    var select = "<div id='div actDiv' class='layui-form-item delForm actDiv div'><label class='layui-form-label' style='font-size:12px;'>登录名： </label>"
                        + "<div class='layui-input-inline layui-form' lay-filter='test'>"
                        + "<select id='accountName'   lay-filter='appAccount'   required  class='select'>"
                        + "  <option></option>"
                    for (var z = 0; z < disable.length; z++) {
                        select = select + "<option value='" + disable[z].loginName + "'>" + disable[z].loginName + "</option>"
                    }
                    select = select + "</select></div></div>"

                    $('#field').append(select);
                    form.render('select', 'test');
                }
            })


        }

    });




    form.on('select(appAccount)', function(data) {
        $('.actDivs').remove();
        var taskType=  document.getElementById("taskType").value;
        //应用系统
        var appName = document.getElementById("appName").value;
        if(taskType=='帐号修改'){
            var app = appName;
            var act = data.value;
            $.ajax({
                url:"/actField",
                type:'POST',
                data:{app:app,act:act},
                dataType: "json",
                success:function(sea) {
                    for (var j=0;j<sea.length;j++){
                        //获取的数据
                        var account=sea[j];
                        //字段名
                        var accountName=account.names;
                        //字段名称
                        var accountRemark=account.remarks;
                        //字段默认值
                        var accountValue=account.defaultValues;
                        //字段类型
                        var accountType=account.inputTypes;
                        //字段是否可以为空，1可以为空 2不允许为空
                        var accountRequrieds=account.isRequrieds;
                        //字段是否可以新增，1可以 2不可以
                        var accountInserts=account.isInserts;
                        //字段是否可以编辑，1可以编辑 2不可以编辑
                        var accountisEdits=account.isEdits;

                        //基本字段由于iam后台改变类型等状态不会变化，所以这里也不做动态改变
                        if(accountName=='LOGIN_PWD'||accountName=='USER_SN'||accountName=='STATUS'){

                        }else if(accountName=='LOGIN_NAME'){

                        } else if(accountName=='ACCT_TYPE'){
                            var select= "<div id='div' class='layui-form-item delForm div actDiv actDivs'><label class='layui-form-label' style='font-size:12px;'>"+accountRemark +"： </label>"
                                + "<div class='layui-input-inline layui-form' lay-filter='test'>"
                                +"<select id='actType'  lay-filter='accountType'  lay-verify='required'   required  class='select'>"
                            if(accountValue==1){
                                select=select+"<option value='"+accountValue+"'>一般账号</option>" +
                                    "<option value='2'>公共账号</option>" +
                                    "<option value='3'>接口账号</option>"
                            }else if(accountValue==2){
                                select=select+ "<option value='"+accountValue+"'>公共账号</option>" +
                                    "<option value='1'>一般账号</option>" +
                                    "<option value='3'>接口账号</option>"
                            }else if(accountValue==3){
                                select=select+"<option value='"+accountValue+"'>接口账号</option>"
                                    +"<option value='1'>一般账号</option>" +
                                    "<option value='2'>公共账号</option>"
                            }
                            select=select+"</select></div></div>"
                            $('#field').append(select);
                        }else if(accountName=='ACCOUNT_ORG'){
                            var selectO= "<div id='div' class='layui-form-item delForm div actDiv actDivs'><label class='layui-form-label' style='font-size:12px;'>"+accountRemark +"：</label>"
                                + "<div class='layui-input-inline layui-form' lay-filter='test'>"
                                +"<input type='text' id='accountOrgId' style='display:none' class='' />"
                                +" <input type='text' onfocus='org();' id='accountOrg'  lay-vertype='accountField' required class='layui-input'  value='"+accountValue+"'   ></div></div>"
                            $('#field').append(selectO);
                        }else {

                            //扩展字段动态显示
                            if(accountType=='text'){

                                var selectT= "<div id='div' class='layui-form-item div delForm actDiv actDivs'><label class='layui-form-label labelText' style='font-size:12px;'>"+accountRemark +"：</label>"
                                    + "<div class='layui-input-inline layui-form' lay-filter='test'>"
                                    +"<input type='text' style='display:none' class='textKey' value='"+accountName+"' />"
                                    +" <input type='text' class='text layui-input'  lay-vertype='accountField' required   value='"+accountValue+"'  name='account' ></div></div>"
                                $('#field').append(selectT);

                            }else if(accountType=='select'){
                                //下拉框内要选择的值
                                var selectS= "<div id='div' class='layui-form-item div delForm actDiv actDivs'><label class='layui-form-label labelSelectss' style='font-size:12px;'>"+accountRemark +"： </label>"
                                    + "<div class='layui-input-inline layui-form' lay-filter='test'>"
                                    +"<input type='text' style='display:none' class='selectKey' value='"+accountName+"' />"
                                    +"<select type='text' class='selectss'  lay-filter='accountField'  lay-verify='required'   required  >"
                                    for (var cp = 0; cp < account.listCompants.length; cp++) {
                                        for (var key in account.listCompants[cp]) {
                                            if(key==accountValue) {
                                            selectS = selectS + "<option value='" + accountValue + "'>" + account.listCompants[cp][key] + "</option>"
                                        }
                                    }
                                }
                                if(account.listCompants.toString()!=null||account.listCompants.toString()!=""){
                                    for(var cp=0;cp<account.listCompants.length;cp++){
                                        for ( var key in account.listCompants[cp] ) {
                                                selectS = selectS + "<option value='" + key + "'>"+account.listCompants[cp][key]+"</option>"
                                        }
                                    }
                                }
                                selectS=selectS+"</select></div></div>"
                                $('#field').append(selectS);
                            }else if(accountType=='password'){

                                var selectP= "<div id='div' class='layui-form-item div delForm actDiv actDivs'><label class='layui-form-label labelPassword' style='font-size:12px;'>"+accountRemark +"：</label>"
                                    + "<div class='layui-input-inline layui-form' lay-filter='test'>"
                                    +"<input type='text' style='display:none' class='passwordKey' value='"+accountName+"' />"
                                    +" <input type='password'  class='password layui-input'   lay-vertype='accountField' required   value='"+accountValue+"'  ></div></div>"
                                $('#field').append(selectP);

                            }else if(accountType=='date'){
                                var selectD= "<div id='div' class='layui-form-item div delForm actDiv actDivs'><label class='layui-form-label labelDate' style='font-size:12px;'>"+accountRemark +"：</label>"
                                    + "<div class='layui-input-inline layui-form' lay-filter='test'>"
                                    +"<input type='text' style='display:none' class='dateKey' value='"+accountName+"' />"
                                    +" <input type='date'  class='date layui-input' placeholder='yyyy-MM-dd'  lay-vertype='accountField' required   value='"+accountValue+"'   ></div></div>"
                                $('#field').append(selectD);
                            }
                        }
                    }
                    form.render('select', 'test');

                }
            });
        }

    });

    form.render('select', 'test');
});



/*展示任务数据*/

function getPositionLists(){
//渲染分页
layui.use(['laypage','table', 'layer'], function () {
    var layer = layui.layer, table = layui.table;
    //清空之前的列表记录
    var startTime=document.getElementById("startTime").value;
    var endTime=document.getElementById("endTime").value;
    $.ajax({
        url: "/selectApplyTast",
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
                    elem: 'positionPages',
                    count: data.length, //数据总数，从服务端得到
                    limit:2,
                    limits:[1,2,3,4,5,10,20,30,50,100],
                    layout: ['count', 'prev', 'page', 'next','skip','limit'],
                    jump: function(obj){
                        //模拟渲染
                        document.getElementById('positionLists').innerHTML = function(){
                            var arr =[]
                            thisData =data.concat().splice(obj.curr*obj.limit - obj.limit, obj.limit);
                            layui.each(thisData, function(index, item){

                                var positionStr= "<tr>"
                                    +"<td>"+item.taskId+"</td>"
                                    +"<td>"+item.taskApplyPerson+"</td>"
                                    +"<td>"+item.taskCreateTime+"</td>"
                                    +"<td>"+item.taskApp+"</td>"
                                positionStr=positionStr+"<td>"+item.taskAccount+"</td>"
                                    +"<td>"+item.taskTypes+"</td>"
                                    +"<td>"+item.taskApprovedPerson+"</td>"+"<td>"+item.taskName+"</td><td style='width: 250px'>"
                                if(item.taskName=='审批账号'){
                                    positionStr=positionStr+ "<button value='" + item.taskProcessInstanceId + "'  class='layui-btn' id='backProce'>撤回</button>"
                                        +"<button value='" + item.taskProcessInstanceId + "'  class='layui-btn' id='selectTa'>详细信息</button>"
                                        + "<button value='" + item.taskProcessInstanceId + "' class='layui-btn'   id='processView'>任务进度</button>"
                                }else {
                                    positionStr=positionStr+ "<button value='" + item.taskProcessInstanceId + "'  class='layui-btn' id='actSubmit'>重新申请</button>"
                                        +"<button value='" + item.taskProcessInstanceId + "'  class='layui-btn' id='selectTa'>详细信息</button>"
                                        + "<button value='" + item.taskProcessInstanceId + "' class='layui-btn layui-btn-sm layui-btn-normal' id='delete'><i class='layui-icon'></i>删除</button>"
                                }
                                positionStr=positionStr+"</td></tr>";
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





/*
* 帐号申请表单
* */
function accountApply() {
    $('.delForm').remove();
    layui.use('form', function () {
        var form = layui.form; //只有执行了这一步，部分表单元素才会自动修饰成功
        layui.use('layer', function () {
            var layer = layui.layer;
            layer.open({
                type: 1,
                title: "账号申请",   //标题
                area: ['400px', '400px'],    //弹窗大小
                shadeClose: false,      //禁止点击空白关闭
                scrollbar: false,      //禁用滚动条
                move: false,       //禁用移动
                scrolling: 'no',
                resize: false,
                closeBtn: 1,
                content: $('#accountApply'),
                end: function () {
                    $('#accountApply').hide();
                }
            });
        });

        $.ajax({
            url: "/selectForm",
            type: "POST",
            dataType: "json",
            success: function (data) {
                var selectS= "<div class='layui-form-item delForm'><label class='layui-form-label' style='font-size:12px;'>审批人： </label>"
                    + "<div class='layui-input-inline layui-form' lay-filter='test'>"
                    +"<select id='audit'  class='select'      >"
                    + "  <option></option>"
                    +"<option >"+data[0].taskApprovedPerson+"</option>"
                    +"</select></div></div>"
                $('#aud').append(selectS);
                var selectT= "<div class='layui-form-item delForm'><label class='layui-form-label' style='font-size:12px;'>任务类型： </label>"
                    + "<div class='layui-input-inline layui-form' lay-filter='test'>"
                    +"<select id='taskType'   lay-filter='taskty'  >"
                    + "  <option></option>"
                    for(var t=0;t<data.length;t++){
                        selectT=selectT+"<option >"+data[t].taskTypes+"</option>"
                    }
                selectT=selectT+"</select></div></div>"
                $('#taskTapys').append(selectT);
                form.render('select', 'test');

            }
        })
    })
}

/*
* 提交申请任务
* */
function addAccount() {
    //申请人
    var userName = document.getElementById("userName").value;
    //用户所属组织
    /*var orgName = document.getElementById("orgName").value;*/
    //用户所属组织ID
   /* var orgId = document.getElementById("orgId").value;*/
    //任务类型
    var taskTypes = document.getElementsByClassName("layui-this");
    var taskType=  document.getElementById("taskType").value;
    //应用系统
    var appName = document.getElementById("appName").value;
    //审批人audit
    var audit = document.getElementById("audit").value;
    //审批角色
    var role = taskTypes[taskTypes.length - 1].innerText;
    //申请理由
    var applyReason = document.getElementById("applyReason").value;
    if (userName == null || userName == "") {
        layer.alert("申请人为空!")
        return false;
    }
   /* if (orgName == null || orgName == "") {
        layer.alert("请选择用户所属组织!")
        return false;
    }*/
    if (taskType == null || taskType == "") {
        layer.alert("请选择任务类型!")
        return false;
    }
    if (appName == null || appName == "") {
        layer.alert("请选择应用系统!")
        return false;
    }
    if (audit == null || audit == "") {
        layer.alert("请选择审批人!")
        return false;
    }
    if (role == null || role == "") {
        layer.alert("请选择审批角色!")
        return false;
    }
    if (applyReason == null || applyReason == "") {
        layer.alert("请填写申请理由!")
        return false;
    }


    //系统账号扩展字段
    if (taskType == '帐号新增'||taskType == '帐号修改') {
        var accountOrg = document.getElementById("accountOrg").value;
        var accountOrgId = document.getElementById("accountOrgId").value;
        if (accountOrg == null || accountOrg == "") {
            layer.alert("请选择帐号所属组织!")
            return false;
        }
        var actType = document.getElementById("actType").value;
        if (actType == null || actType == "") {
            layer.alert("请选择帐号类型!")
            return false;
        }
        var accountName = document.getElementById("accountName").value;
        if(taskType == '帐号新增'){

            if (accountName == null || accountName == "") {
                layer.alert("请填写登陆名!")
                return false;
            }
        }else if (taskType == '帐号修改'){

            if (accountName == null || accountName == "") {
                layer.alert("请选择要修改的帐号!")
                return false;
            }
        }

        var text = document.getElementsByClassName("text");
        var labelText = document.getElementsByClassName("labelText");
        var tkey = document.getElementsByClassName("textKey");
        var textList=new Array();
        for (var tex = 0; tex < $(".text").length; tex++) {
            //文本框的内容
           /* var textMap=new Map();*/
            var textField = text[tex].value;
            var textKey=tkey[tex].value;
            if (textField == null || textField == "") {
                layer.alert("请填写" + labelText[tex].innerHTML + "!");
                return false;
            }
            var textMap='"'+textKey+'":"'+textField+'"';
            textList.push(textMap);
        }
        var password = document.getElementsByClassName("password");
        var labelPassword = document.getElementsByClassName("labelPassword");
        var padKey = document.getElementsByClassName("passwordKey");
        var passwordList=new Array();
        for (var pwd = 0; pwd < $(".password").length; pwd++) {
            //密码框的内容
           /* var passwordMap=new Map();*/
            var passwordField = password[pwd].value;
            var passwordKey=padKey[pwd].value;
            if (passwordField == null || passwordField == "") {
                layer.alert("请填写" + labelPassword[pwd].innerHTML + "!");
                return false;
            }
            var passwordMap='"'+passwordKey+'":"'+passwordField+'"';
            passwordList.push(passwordMap);
        }
        var labelDate = document.getElementsByClassName("labelDate");
        var date = document.getElementsByClassName("date");
        var dakey = document.getElementsByClassName("dateKey");
        var dateList=new Array();
        for (var da = 0; da < $(".date").length; da++) {
            //日期框的内容
            /*var dateMap=new Map();*/
            var dateField = date[da].value;
            var dateKey=dakey[da].value;
            if (dateField == null || dateField == "") {
                layer.alert("请填写" + labelDate[da].innerHTML + "!")
                return false;
            }
            var dateMap='"'+dateKey+'":"'+dateField+'"';
            dateList.push(dateMap);
        }
        var labelSelectss = document.getElementsByClassName("labelSelectss");
        var selectss = document.getElementsByClassName("selectss");
        var selkey = document.getElementsByClassName("selectKey");
        var selectList=new Array();
        for (var selt = 0; selt < $(".selectss").length; selt++) {
            //下拉框的内容
           /* var selectMap=new Map();*/
            var selectField = selectss[selt].value;
            var selectKey=selkey[selt].value;
            if (selectField == null || selectField == "") {
                layer.alert("请选择" + labelSelectss[selt].innerHTML + "!")
                return false;
            }
            var selectMap='"'+selectKey+'":"'+selectField+'"';
            selectList.push(selectMap);
        }


        if ((appName != null || appName != "") && (accountName != null || accountName != "")) {
            $.ajax({
                url: "/verifyActTask",
                type: "POST",
                dataType: "json",
                data: {
                    taskType:taskType,
                    appName: appName,
                    accountName: accountName,
                   /* orgName: orgName,*/
                  /*  orgId:orgId,*/
                    audit:audit,
                    role:role,
                    applyReason:applyReason,
                    accountOrg:accountOrg,
                    accountOrgId:accountOrgId,
                    actType:actType,
                    applyName: appName,
                    textList:"{"+textList.toString()+"}",
                    passwordList:"{"+passwordList.toString()+"}",
                    selectList:"{"+selectList.toString()+"}",
                    dateList:"{"+dateList.toString()+"}"
                },
                success: function (data) {
                    if(taskType == '帐号新增'){
                        if (data[0].taskId == '1') {
                            layer.alert("您在" + appName + "中已经拥有" + accountName + "账号，并帐号处于启用状态，您可以直接使用！");
                            return false;
                        } else if (data[0].taskId== '2') {
                            layer.alert("您在" + appName + "中已经拥有" + accountName + "账号，但是处于禁用状态，您可以直接启用" + accountName + "帐号，或者可以修改登陆名申请其他帐号！");
                            return false;
                        } else {
                            layer.close(layer.index);
                            layer.alert("申请成功！", {icon: 6,time:3000});
                            if(data!=null){
                                var laypage = layui.laypage;
                                //账号Tab分页
                                laypage.render({
                                    elem: 'positionPages',
                                    count: data.length, //数据总数，从服务端得到
                                    limit:2,
                                    limits:[1,2,3,4,5,10,20,30,50,100],
                                    layout: ['count', 'prev', 'page', 'next','skip','limit'],
                                    jump: function(obj){
                                        //模拟渲染
                                        document.getElementById('positionLists').innerHTML = function(){
                                            var arr =[]
                                            thisData =data.concat().splice(obj.curr*obj.limit - obj.limit, obj.limit);
                                            layui.each(thisData, function(index, item){
                                                var positionStr= "<tr>"
                                                    +"<td>"+item.taskId+"</td>"
                                                    +"<td>"+item.taskApplyPerson+"</td>"
                                                    +"<td>"+item.taskCreateTime+"</td>"
                                                    +"<td>"+item.taskApp+"</td>"
                                                positionStr=positionStr+"<td>"+item.taskAccount+"</td>"
                                                    +"<td>"+item.taskTypes+"</td>"
                                                    +"<td>"+item.taskApprovedPerson+"</td>"+"<td>"+item.taskName+"</td><td style='width: 250px'>"
                                                    if(item.taskName=='审批账号'){
                                                        positionStr=positionStr+ "<button value='" + item.taskProcessInstanceId + "' class='layui-btn' id='backProce'>撤回</button>"
                                                        +"<button value='" + item.taskProcessInstanceId + "' class='layui-btn' id='selectTa'>详细信息</button>"
                                                            + "<button value='" + item.taskProcessInstanceId + "' class='layui-btn'   id='processView'>任务进度</button>"
                                                    }else {
                                                        positionStr = positionStr + "<button value='" + item.taskProcessInstanceId + "' class='layui-btn' id='actSubmit'>重新申请</button>"
                                                            + "<button value='" + item.taskProcessInstanceId + "' class='layui-btn' id='selectTa'>详细信息</button>"
                                                            + "<button value='" + item.taskProcessInstanceId + "' class='layui-btn layui-btn-sm layui-btn-normal' id='delete'><i class='layui-icon'></i>删除</button>"
                                                    }
                                                        positionStr=positionStr+"</td></tr>";
                                                arr.push(positionStr);
                                            });
                                            return arr.join('');

                                        }();

                                    }
                                });
                            }
                        }
                    }else if(taskType == '帐号修改'){
                        layer.alert("申请成功！", {icon: 6,time:3000});
                        layer.close(layer.index - 1);
                        if(data!=null){
                            var laypage = layui.laypage;
                            //账号Tab分页
                            laypage.render({
                                elem: 'positionPages',
                                count: data.length, //数据总数，从服务端得到
                                limit:2,
                                limits:[1,2,3,4,5,10,20,30,50,100],
                                layout: ['count', 'prev', 'page', 'next','skip','limit'],
                                jump: function(obj){
                                    //模拟渲染
                                    document.getElementById('positionLists').innerHTML = function(){
                                        var arr =[]
                                        thisData =data.concat().splice(obj.curr*obj.limit - obj.limit, obj.limit);
                                        layui.each(thisData, function(index, item){
                                            var positionStr= "<tr>"
                                                +"<td>"+item.taskId+"</td>"
                                                +"<td>"+item.taskApplyPerson+"</td>"
                                                +"<td>"+item.taskCreateTime+"</td>"
                                                +"<td>"+item.taskApp+"</td>"
                                            positionStr=positionStr+"<td>"+item.taskAccount+"</td>"
                                                +"<td>"+item.taskTypes+"</td>"
                                                +"<td>"+item.taskApprovedPerson+"</td>"+"<td>"+item.taskName+"</td><td style='width: 250px'>"
                                            if(item.taskName=='审批账号'){
                                                positionStr=positionStr+ "<button value='" + item.taskProcessInstanceId + "' class='layui-btn' id='backProce'>撤回</button>"
                                                    +"<button value='" + item.taskProcessInstanceId + "' class='layui-btn' id='selectTa'>详细信息</button>"
                                                    + "<button value='" + item.taskProcessInstanceId + "' class='layui-btn'   id='processView'>任务进度</button>"
                                            }else {
                                                positionStr=positionStr+ "<button value='" + item.taskProcessInstanceId + "' class='layui-btn' id='actSubmit'>重新申请</button>"
                                                    +"<button value='" + item.taskProcessInstanceId + "' class='layui-btn' id='selectTa'>详细信息</button>"
                                                    + "<button value='" + item.taskProcessInstanceId + "' class='layui-btn layui-btn-sm layui-btn-normal' id='delete'><i class='layui-icon'></i>删除</button>"
                                            }
                                            positionStr=positionStr+"</td></tr>";
                                            arr.push(positionStr);
                                        });
                                        return arr.join('');

                                    }();

                                }
                            });
                        }

                    }
                }
            })
        }


    } else if (taskType == '帐号启用') {

        var accountEnable = document.getElementById("account").value;
        if (accountEnable == null || accountEnable == "") {
            layer.alert("请选择要禁用的帐号!")
            return false;
        }
        if ((accountEnable != null || accountEnable != "")&&(taskType != "" || taskType != null) && (appName != null || appName != "") &&
            (audit != null || audit != "") && (role != null || role != "") && (applyReason != null || applyReason != "") ) {
            $.ajax({
                url: "/verifyActTask",
                type: "POST",
                dataType: "json",
                data: {
                    taskType:taskType,
                    appName: appName,
                    accountName: accountEnable,
                   /* orgName: orgName,*/
                    audit:audit,
                    applyReason:applyReason,
                    role:role
                },
                success: function (data) {
                    layer.alert("申请成功！", {icon: 6,time:3000});
                    layer.close(layer.index - 1);
                    getPositionLists();

                }
            });
        }
    }
}


$(document).ready(function() {

    $("body").on("click", "#backProce", function (e) {
        var layer = layui.layer;
        var r = confirm("确定要撤回吗？");
        if (r == true) {
            //获取taskId
            var value = $(this).val();
            $.ajax({
                url: "/withdraw",
                type: "POST",
                data: {
                    id: value
                },
                success: function (data) {
                    //撤回成功
                    if (data == 3) {
                        layer.alert("撤回成功!", {icon: 6,time:3000});

                    } else if (data == 1) {
                        layer.alert("流程未启动或已执行完成，无法撤回!");
                    } else if (data == 2) {
                        layer.alert("该任务非当前用户提交，无法撤回!");
                    } else {
                        layer.alert("撤回失败!", {icon: 5,time:3000})
                        document.getElementById("selectTask").click();
                    }
                    getPositionLists();
                }
            });
        }
    });
})


    $(document).ready(function() {

        $("body").on("click", "#delete", function (e) {
            var layer = layui.layer;
            var r = confirm("确定要删除吗？");
            if (r == true) {
                //获取taskId
                var value = $(this).val();
                $.ajax({
                    url: "/deleteTask",
                    type: "POST",
                    data: {
                        id: value
                    },
                    success: function (data) {
                        //撤回成功
                        if (data == 1) {
                            layer.alert("删除成功!", {icon: 6,time:3000});
                            getPositionLists();

                        } else {
                            layer.alert("删除失败!", {icon: 5,time:3000})
                            getPositionLists();
                        }
                    }
                });
            }
        });
    })

$(document).ready(function() {
    $("body").on("click", "#actSubmit", function (e) {
            var value = $(this).val();
        $('.actDivs').remove();
        $('.actDiv').remove();
        $('.div').remove();
        $('#div').remove();
            document.getElementById("applyReason").value = null;
            layui.use('form', function () {
                var form = layui.form; //只有执行了这一步，部分表单元素才会自动修饰成功
                layui.use('layer', function () {
                    var layer = layui.layer;
                    layer.open({
                        type: 1,
                        title: "重新申请",   //标题
                        area: ['400px', '400px'],    //弹窗大小
                        shadeClose: false,      //禁止点击空白关闭
                        scrollbar: false,      //禁用滚动条
                        move: false,       //禁用移动
                        scrolling: 'no',
                        resize: false,
                        closeBtn: 1,
                        content: $('#accountAgainApply'),
                        end: function () {
                            $('#accountAgainApply').hide();
                        }
                    });
                });
            })

            $.ajax({
                url: "/againSelect",
                type: "POST",
                data: {
                    id: value
                },
                success: function (data) {

                    var userName  = "<div id='div' class='layui-form-item delForm div'><label class='layui-form-label' style='font-size:12px;'>申请者： </label>"
                        + "<div class='layui-input-inline layui-form' lay-filter='test'>"
                        +"<input id='pid' type='text' disabled  style='display:none'   required class='layui-input' value='"+data[0].id+"'  >"
                        +" <input id='userName' type='text' disabled   lay-vertype='tips' required class='layui-input' value='"+data[0].applyPerson+"'  ></div></div>"

                    $('#userNames').append(userName);
                    /*var orgName  = "<div id='div' class='layui-form-item delForm div'><label class='layui-form-label' style='font-size:12px;'>用户所属组织： </label>"
                        + "<div class='layui-input-inline layui-form' lay-filter='test'>"
                        +"<input type='text' id='orgId' style='display:none' class='' value='' />"
                        +" <input id='orgName' onfocus='org();' type='text'   lay-vertype='tips' required class='layui-input' value='"+data[0].orgName+"'  ></div></div>"

                    $('#orgNames').append(orgName);*/

                    var selectTs= "<div id='div' class='layui-form-item div'><label class='layui-form-label' style='font-size:12px;'>任务类型： </label>"
                        + "<div class='layui-input-inline layui-form' lay-filter='test2'>"
                        +"<select id='taskType'   lay-filter='taskty'  lay-verify='required'   required  >"
                    for(var t=0;t<data[0].taskTypeAll.length;t++){
                        if(data[0].taskTypeAll[t].taskTypes==data[0].taskType){
                            selectTs=selectTs+"<option selected='selected'>"+data[0].taskTypeAll[t].taskTypes+"</option>"
                        }else {
                            selectTs=selectTs+"<option >"+data[0].taskTypeAll[t].taskTypes+"</option>"
                        }

                    }
                    selectTs=selectTs+"</select></div></div>"
                    $('#taskTapys2').append(selectTs);

                    var selectA = "<div id='div' class='layui-form-item delForm actDiv div'><label class='layui-form-label' style='font-size:12px;'>系统资源： </label>"
                        + "<div class='layui-input-inline layui-form' lay-filter='test2'>"
                        + "<select id='appName'  lay-filter='app'   lay-verify='required'   required  class='select'>"

                    for(var t=0;t<data[0].imApp.length;t++){
                        if(data[0].imApp[t].name==data[0].app){
                            selectA=selectA+"<option selected='selected'>"+data[0].imApp[t].name+"</option>"
                        }else {
                            selectA=selectA+"  <option>" + data[0].imApp[t].name+ "</option>"
                        }

                    }
                    selectA = selectA + "</select></div></div>"
                    $('#types2').append(selectA);
                    var selectSa= "<div id='div' class='layui-form-item delForm div'><label class='layui-form-label' style='font-size:12px;'>审批人： </label>"
                        + "<div class='layui-input-inline layui-form' lay-filter='test2'>"
                        +"<select id='audit'  class='select'   lay-verify='required'   required  >"
                       /* + "  <option></option>"*/
                        +"<option >"+data[0].approvedPerson+"</option>"
                        +"</select></div></div>"
                    $('#aud2').append(selectSa);
                    form.render('select', 'test2');
                    if(data[0].taskType=='帐号新增'){
                        for (var j=0;j<data.length;j++){
                            //获取的数据
                            var account=data[j];
                            //字段名
                            var accountName=account.names;
                            //字段名称
                            var accountRemark=account.remarks;
                            //字段默认值
                            var accountValue=account.defaultValues;
                            //修改前值
                            var accountBasic=account.basic;
                            //字段类型
                            var accountType=account.inputTypes;
                            //字段是否可以为空，1可以为空 2不允许为空
                            var accountRequrieds=account.isRequrieds;
                            //字段是否可以新增，1可以 2不可以
                            var accountInserts=account.isInserts;
                            //字段是否可以编辑，1可以编辑 2不可以编辑
                            var accountisEdits=account.isEdits;

                            //基本字段由于iam后台改变类型等状态不会变化，所以这里也不做动态改变
                            if(accountName=='LOGIN_PWD'||accountName=='USER_SN'||accountName=='STATUS'){

                            }else if(accountName=='LOGIN_NAME'){
                                var selectName2  = "<div id='div actDiv' class='layui-form-item delForm actDiv div'><label class='layui-form-label' style='font-size:12px;'>登陆名： </label>"
                                    + "<div class='layui-input-inline layui-form' lay-filter='test'>"
                                    +" <input type='text' id='accountName'  lay-vertype='accountField' required class='layui-input' value='"+data[0].account+"'  ></div></div>"

                                $('#field2').append(selectName2);
                            } else if(accountName=='ACCT_TYPE'){
                                var select= "<div id='div actDiv' class='layui-form-item delForm actDiv div'><label class='layui-form-label' style='font-size:12px;'>"+accountRemark +"： </label>"
                                    + "<div class='layui-input-inline layui-form' lay-filter='test'>"
                                    +"<select id='actType'  lay-filter='accountType'  lay-verify='required'   required  class='select'>"
                                if(data[0].actType==1){
                                    select=select+"<option value='"+data[0].actType+"'>一般账号</option>" +
                                        "<option value='2'>公共账号</option>" +
                                        "<option value='3'>接口账号</option>"
                                }else if(data[0].actType==2){
                                    select=select+ "<option value='"+data[0].actType+"'>公共账号</option>" +
                                        "<option value='1'>一般账号</option>" +
                                        "<option value='3'>接口账号</option>"
                                }else if(data[0].actType==3){
                                    select=select+"<option value='"+data[0].actType+"'>接口账号</option>"
                                        +"<option value='1'>一般账号</option>" +
                                        "<option value='2'>公共账号</option>"
                                }
                                select=select+"</select></div></div>"
                                $('#field2').append(select);
                            }else if(accountName=='ACCOUNT_ORG'){
                                var selectO= "<div id='div actDiv' class='layui-form-item delForm actDiv div'><label class='layui-form-label' style='font-size:12px;'>"+accountRemark +"：</label>"
                                    + "<div class='layui-input-inline layui-form' lay-filter='test'>"
                                    +"<input type='text' id='accountOrgId' style='display:none' class=''  />"
                                    +" <input type='text' onfocus='org();' id='accountOrg'  lay-vertype='accountField' required class='layui-input'  value='"+data[0].accountOrg+"'   ></div></div>"
                                $('#field2').append(selectO);
                            }else {
                                //扩展字段动态显示
                                if(accountType=='text'){

                                    var selectT= "<div id='div actDiv' class='layui-form-item delForm actDiv div'><label class='layui-form-label labelText' style='font-size:12px;'>"+accountRemark +"：</label>"
                                        + "<div class='layui-input-inline layui-form' lay-filter='test'>"
                                        +"<input type='text' style='display:none' class='textKey' value='"+accountName+"' />"
                                        +" <input type='text' class='text layui-input'  lay-vertype='accountField' required   value='"+accountBasic+"'  name='account' ></div></div>"
                                    $('#field2').append(selectT);

                                }else if(accountType=='select'){
                                    //下拉框内要选择的值
                                    var selectS= "<div id='div actDiv' class='layui-form-item delForm actDiv div'><label class='layui-form-label labelSelectss' style='font-size:12px;'>"+accountRemark +"： </label>"
                                        + "<div class='layui-input-inline layui-form' lay-filter='test'>"
                                        +"<input type='text' style='display:none' class='selectKey' value='"+accountName+"' />"
                                        +"<select type='text' class='selectss'  lay-filter='accountField'  lay-verify='required'   required  >"

                                    if(account.listCompants.toString()!=null||account.listCompants.toString()!=""){
                                        for(var cp=0;cp<account.listCompants.length;cp++){
                                            for ( var key in account.listCompants[cp] ) {

                                                if(accountBasic==key){
                                                    selectS = selectS + "<option selected='selected' value='" + key + "'>" + account.listCompants[cp][key] + "</option>"
                                                }else {
                                                    selectS = selectS + "<option value='" + key + "'>"+account.listCompants[cp][key]+"</option>"
                                                }

                                            }
                                        }
                                    }
                                    selectS=selectS+"</select></div></div>"
                                    $('#field2').append(selectS);
                                }else if(accountType=='password'){

                                    var selectP= "<div id='div actDiv' class='layui-form-item delForm actDiv div'><label class='layui-form-label labelPassword' style='font-size:12px;'>"+accountRemark +"：</label>"
                                        + "<div class='layui-input-inline layui-form' lay-filter='test'>"
                                        +"<input type='text' style='display:none' class='passwordKey' value='"+accountName+"' />"
                                        +" <input type='password'  class='layui-input password'   lay-vertype='accountField' required   value='"+accountBasic+"'  ></div></div>"
                                    $('#field2').append(selectP);

                                }else if(accountType=='date'){
                                    var selectD= "<div id='div actDiv' class='layui-form-item delForm actDiv div'><label class='layui-form-label labelDate' style='font-size:12px;'>"+accountRemark +"：</label>"
                                        + "<div class='layui-input-inline layui-form' lay-filter='test'>"
                                        +"<input type='text' style='display:none' class='dateKey' value='"+accountName+"' />"
                                        +" <input type='date'  class='layui-input date' placeholder='yyyy-MM-dd'  lay-vertype='accountField' required   value='"+accountBasic+"'   ></div></div>"
                                    $('#field2').append(selectD);
                                }
                            }
                        }
                    }else if(data[0].taskType=='帐号修改'){
                        var selectAcc = "<div id='div actDiv actDivs' class='layui-form-item delForm actDiv div'><label class='layui-form-label' style='font-size:12px;'>登录名： </label>"
                            + "<div class='layui-input-inline layui-form' lay-filter='disable'>"
                            + "<select id='accountName'   lay-filter='appAccount'   required  class='select'>"
                        for (var z = 0; z < data[0].actAll.length; z++) {
                            if(data[0].account==data[0].actAll[0].loginName){
                                selectAcc =   selectAcc + "  <option selected='selected'>"+data[0].actAll[0].loginName+"</option>"
                            }else {
                                selectAcc =   selectAcc + "  <option>"+data[0].actAll[0].loginName+"</option>"
                            }
                        }
                        selectAcc = selectAcc + "</select></div></div>"

                        $('#field2').append(selectAcc);
                        form.render('select', 'disable');

                        for (var j=0;j<data.length;j++){
                            //获取的数据
                            var account=data[j];
                            //字段名
                            var accountName=account.names;
                            //字段名称
                            var accountRemark=account.remarks;
                            //字段默认值
                            var accountValue=account.defaultValues;
                            //修改前值
                            var accountBasic=account.basic;
                            //字段类型
                            var accountType=account.inputTypes;
                            //字段是否可以为空，1可以为空 2不允许为空
                            var accountRequrieds=account.isRequrieds;
                            //字段是否可以新增，1可以 2不可以
                            var accountInserts=account.isInserts;
                            //字段是否可以编辑，1可以编辑 2不可以编辑
                            var accountisEdits=account.isEdits;

                            //基本字段由于iam后台改变类型等状态不会变化，所以这里也不做动态改变
                            if(accountName=='LOGIN_PWD'||accountName=='USER_SN'||accountName=='STATUS'){

                            }else if(accountName=='LOGIN_NAME'){

                            } else if(accountName=='ACCT_TYPE'){
                                var select= "<div id='div actDiv actDivs' class='layui-form-item delForm actDiv actDivs div'><label class='layui-form-label' style='font-size:12px;'>"+accountRemark +"： </label>"
                                    + "<div class='layui-input-inline layui-form' lay-filter='test'>"
                                    +"<select id='actType'  lay-filter='accountType'  lay-verify='required'   required  class='select'>"
                                if(data[0].actType==1){
                                    select=select+"<option value='"+data[0].actType+"'>一般账号</option>" +
                                        "<option value='2'>公共账号</option>" +
                                        "<option value='3'>接口账号</option>"
                                }else if(data[0].actType==2){
                                    select=select+ "<option value='"+data[0].actType+"'>公共账号</option>" +
                                        "<option value='1'>一般账号</option>" +
                                        "<option value='3'>接口账号</option>"
                                }else if(data[0].actType==3){
                                    select=select+"<option value='"+data[0].actType+"'>接口账号</option>"
                                        +"<option value='1'>一般账号</option>" +
                                        "<option value='2'>公共账号</option>"
                                }
                                select=select+"</select></div></div>"
                                $('#field2').append(select);
                            }else if(accountName=='ACCOUNT_ORG'){
                                var selectO= "<div id='div actDiv actDivs' class='layui-form-item delForm actDiv actDivs div'><label class='layui-form-label' style='font-size:12px;'>"+accountRemark +"：</label>"
                                    + "<div class='layui-input-inline layui-form' lay-filter='test'>"
                                    +"<input type='text' id='accountOrgId' style='display:none' />"
                                    +" <input type='text' onfocus='org();' id='accountOrg'  lay-vertype='accountField' required class='layui-input'  value='"+data[0].accountOrg+"'   ></div></div>"
                                $('#field2').append(selectO);
                            }else {

                                //扩展字段动态显示
                                if(accountType=='text'){

                                    var selectT= "<div id='div' class='layui-form-item delForm actDiv actDivs div'><label class='layui-form-label labelText' style='font-size:12px;'>"+accountRemark +"：</label>"
                                        + "<div class='layui-input-inline layui-form' lay-filter='test'>"
                                        +"<input type='text' style='display:none' class='textKey' value='"+accountName+"' />"
                                        +" <input type='text' class='text layui-input'  lay-vertype='accountField' required   value='"+accountBasic+"'  name='account' ></div></div>"
                                    $('#field2').append(selectT);

                                }else if(accountType=='select'){
                                    //下拉框内要选择的值
                                    var selectS= "<div id='div' class='layui-form-item delForm actDiv actDivs div'><label class='layui-form-label labelSelectss' style='font-size:12px;'>"+accountRemark +"： </label>"
                                        + "<div class='layui-input-inline layui-form' lay-filter='test'>"
                                        +"<input type='text' style='display:none' class='selectKey' value='"+accountName+"' />"
                                        +"<select type='text' class='selectss'  lay-filter='accountField'  lay-verify='required'   required  >"

                                    if(account.listCompants.toString()!=null||account.listCompants.toString()!=""){
                                        for(var cp=0;cp<account.listCompants.length;cp++){
                                            for ( var key in account.listCompants[cp] ) {
                                                if(accountBasic==key){
                                                    selectS = selectS + "<option selected='selected' value='" + key + "'>" + account.listCompants[cp][key] + "</option>"
                                                }else {
                                                    selectS = selectS + "<option value='" + key + "'>"+account.listCompants[cp][key]+"</option>"
                                                }

                                            }
                                        }
                                    }
                                    selectS=selectS+"</select></div></div>"
                                    $('#field2').append(selectS);
                                }else if(accountType=='password'){

                                    var selectP= "<div id='div' class='layui-form-item delForm actDiv actDivs div'><label class='layui-form-label labelPassword' style='font-size:12px;'>"+accountRemark +"：</label>"
                                        + "<div class='layui-input-inline layui-form' lay-filter='test'>"
                                        +"<input type='text' style='display:none' class='passwordKey' value='"+accountName+"' />"
                                        +" <input type='password'  class='password layui-input'   lay-vertype='accountField' required   value='"+accountBasic+"'  ></div></div>"
                                    $('#field2').append(selectP);

                                }else if(accountType=='date'){
                                    var selectD= "<div id='div' class='layui-form-item delForm actDiv actDivs div'><label class='layui-form-label labelDate' style='font-size:12px;'>"+accountRemark +"：</label>"
                                        + "<div class='layui-input-inline layui-form' lay-filter='test'>"
                                        +"<input type='text' style='display:none' class='dateKey' value='"+accountName+"' />"
                                        +" <input type='date'  class='date layui-input' placeholder='yyyy-MM-dd'  lay-vertype='accountField' required   value='"+accountBasic+"'   ></div></div>"
                                    $('#field2').append(selectD);
                                }
                            }
                        }
                    }else if(data[0].taskType=='帐号启用'){
                        var selectAcc = "<div id='div ' class='layui-form-item delForm actDiv div'><label class='layui-form-label' style='font-size:12px;'>登录名： </label>"
                            + "<div class='layui-input-inline layui-form' lay-filter='disable'>"
                            + "<select id='accountName'   lay-filter='appAccount'   required  class='select'>"
                        for (var z = 0; z < data[0].actAll.length; z++) {
                            if(data[0].account==data[0].actAll[0].loginName){
                                selectAcc =   selectAcc + "  <option selected='selected'>"+data[0].actAll[0].loginName+"</option>"
                            }else {
                                selectAcc =   selectAcc + "  <option>"+data[0].actAll[0].loginName+"</option>"
                            }
                        }
                        selectAcc = selectAcc + "</select></div></div>"

                        $('#field2').append(selectAcc);
                        form.render('select', 'disable');
                    }
                    if (data[0].comment.length == 0) {

                    } else {
                        var approvalOpinion = "<div  class='layui-form-item div' id='div'><label class='layui-form-label' style='font-size:12px;'>历史批注： </label>"
                            + "<div class='layui-input-inline layui-form' lay-filter='test2'>"
                            + " <p   disabled autocomplete='off' class='layui-text'  >"
                        for(var co=0;co<data[0].comment.length;co++){
                            var num=co+1;
                            approvalOpinion = approvalOpinion +"第"+num+"次批注</br>批注人："+data[0].comment[co].userId+"</br>批注时间："+data[0].comment[co].time+"</br>批注内容：" +data[0].comment[co].fullMessage+"</br></br>"
                        }
                        approvalOpinion =approvalOpinion + "</p></div></div>"
                        $('#approvalOpinions').append(approvalOpinion);
                    }
                    document.getElementById("applyReasons").value=data[0].applyReason ;
                    //渲染select，不然无法显示值
                    form.render('select', 'test');
                    form.render();
                }
            });
    });
})





/*
* 重新提交申请任务
* */
function againAddAccount() {
    //任务ID
    var id = document.getElementById("pid").value;
    //申请人
    var userName = document.getElementById("userName").value;
    //用户所属组织
   /* var orgName = document.getElementById("orgName").value;*/
    //用户所属组织
   /* var orgId = document.getElementById("orgId").value;*/
    //任务类型
    var taskTypes = document.getElementsByClassName("layui-this");
    var taskType=  document.getElementById("taskType").value;
    //应用系统
    var appName = document.getElementById("appName").value;
    //审批人audit
    var audit = document.getElementById("audit").value;
    //审批角色
    var role = taskTypes[taskTypes.length - 1].innerText;
    //申请理由
    var applyReason = document.getElementById("applyReasons").value;
    if (userName == null || userName == "") {
        layer.alert("申请人为空!")
        return false;
    }
   /* if (orgName == null || orgName == "") {
        layer.alert("请选择用户所属组织!")
        return false;
    }*/
    if (taskType == null || taskType == "") {
        layer.alert("请选择任务类型!")
        return false;
    }
    if (appName == null || appName == "") {
        layer.alert("请选择应用系统!")
        return false;
    }
    if (audit == null || audit == "") {
        layer.alert("请选择审批人!")
        return false;
    }
    if (role == null || role == "") {
        layer.alert("请选择审批角色!")
        return false;
    }
    if (applyReason == null || applyReason == "") {
        layer.alert("请填写申请理由!")
        return false;
    }


    //系统账号扩展字段
    if (taskType == '帐号新增'||taskType == '帐号修改') {
        var accountOrg = document.getElementById("accountOrg").value;
        var accountOrgId = document.getElementById("accountOrgId").value;
        if (accountOrg == null || accountOrg == "") {
            layer.alert("请选择帐号所属组织!")
            return false;
        }
        var actType = document.getElementById("actType").value;
        if (actType == null || actType == "") {
            layer.alert("请选择帐号类型!")
            return false;
        }
        var accountName = document.getElementById("accountName").value;
        if(taskType == '帐号新增'){

            if (accountName == null || accountName == "") {
                layer.alert("请填写登陆名!")
                return false;
            }
        }else if (taskType == '帐号修改'){

            if (accountName == null || accountName == "") {
                layer.alert("请选择要修改的帐号!")
                return false;
            }
        }
        
        var text = document.getElementsByClassName("text");
        var labelText = document.getElementsByClassName("labelText");
        var tkeys = document.getElementsByClassName("textKey");
        var textList=new Array();
        for (var tex = 0; tex < $(".text").length; tex++) {
            //文本框的内容
            /* var textMap=new Map();*/
            var textField = text[tex].value;
            var textKey=tkeys[tex].value;
            if (textField == null || textField == "") {
                layer.alert("请填写" + labelText[tex].innerHTML + "!");
                return false;
            }
            var textMap='"'+textKey+'":"'+textField+'"';
            textList.push(textMap);
        }
        var password = document.getElementsByClassName("password");
        var labelPassword = document.getElementsByClassName("labelPassword");
        var padKey = document.getElementsByClassName("passwordKey");
        var passwordList=new Array();
        for (var pwd = 0; pwd < $(".password").length; pwd++) {
            //密码框的内容
            /* var passwordMap=new Map();*/
            var passwordField = password[pwd].value;
            var passwordKey=padKey[pwd].value;
            if (passwordField == null || passwordField == "") {
                layer.alert("请填写" + labelPassword[pwd].innerHTML + "!");
                return false;
            }
            var passwordMap='"'+passwordKey+'":"'+passwordField+'"';
            passwordList.push(passwordMap);
        }
        var labelDate = document.getElementsByClassName("labelDate");
        var date = document.getElementsByClassName("date");
        var dakey = document.getElementsByClassName("dateKey");
        var dateList=new Array();
        for (var da = 0; da < $(".date").length; da++) {
            //日期框的内容
            /*var dateMap=new Map();*/
            var dateField = date[da].value;
            var dateKey=dakey[da].value;
            if (dateField == null || dateField == "") {
                layer.alert("请填写" + labelDate[da].innerHTML + "!")
                return false;
            }
            var dateMap='"'+dateKey+'":"'+dateField+'"';
            dateList.push(dateMap);
        }
        var labelSelectss = document.getElementsByClassName("labelSelectss");
        var selectss = document.getElementsByClassName("selectss");
        var selkey = document.getElementsByClassName("selectKey");
        var selectList=new Array();
        for (var selt = 0; selt < $(".selectss").length; selt++) {
            //下拉框的内容
            /* var selectMap=new Map();*/
            var selectField = selectss[selt].value;
            var selectKey=selkey[selt].value;
            if (selectField == null || selectField == "") {
                layer.alert("请选择" + labelSelectss[selt].innerHTML + "!")
                return false;
            }
            var selectMap='"'+selectKey+'":"'+selectField+'"';
            selectList.push(selectMap);
        }

        if ((appName != null || appName != "") && (accountName != null || accountName != "")) {
            $.ajax({
                url: "/verifyActTask",
                type: "POST",
                dataType: "json",
                data: {
                    id:id,
                    taskType:taskType,
                    appName: appName,
                    accountName: accountName,
                   /* orgName: orgName,*/
                   /* orgId: orgId,*/
                    audit:audit,
                    role:role,
                    applyReason:applyReason,
                    accountOrg:accountOrg,
                    accountOrgId:accountOrgId,
                    actType:actType,
                    applyName: appName,
                    textList:"{"+textList.toString()+"}",
                    passwordList:"{"+passwordList.toString()+"}",
                    selectList:"{"+selectList.toString()+"}",
                    dateList:"{"+dateList.toString()+"}"
                },
                success: function (data) {
                    if(taskType == '帐号新增'){
                        if (data[0].taskId == '1') {
                            layer.alert("您在" + appName + "中已经拥有" + accountName + "账号，并帐号处于启用状态，您可以直接使用！");
                            return false;
                        } else if (data[0].taskId== '2') {
                            layer.alert("您在" + appName + "中已经拥有" + accountName + "账号，但是处于禁用状态，您可以直接启用" + accountName + "帐号，或者可以修改登陆名申请其他帐号！");
                            return false;
                        } else {
                            layer.close(layer.index);
                            layer.alert("申请成功！", {icon: 6,time:2500});
                            if(data!=null){
                                var laypage = layui.laypage;
                                //账号Tab分页
                                laypage.render({
                                    elem: 'positionPages',
                                    count: data.length, //数据总数，从服务端得到
                                    limit:2,
                                    limits:[1,2,3,4,5,10,20,30,50,100],
                                    layout: ['count', 'prev', 'page', 'next','skip','limit'],
                                    jump: function(obj){
                                        //模拟渲染
                                        document.getElementById('positionLists').innerHTML = function(){
                                            var arr =[]
                                            thisData =data.concat().splice(obj.curr*obj.limit - obj.limit, obj.limit);
                                            layui.each(thisData, function(index, item){
                                                var positionStr= "<tr>"
                                                    +"<td>"+item.taskId+"</td>"
                                                    +"<td>"+item.taskApplyPerson+"</td>"
                                                    +"<td>"+item.taskCreateTime+"</td>"
                                                    +"<td>"+item.taskApp+"</td>"
                                                positionStr=positionStr+"<td>"+item.taskAccount+"</td>"
                                                    +"<td>"+item.taskTypes+"</td>"
                                                    +"<td>"+item.taskApprovedPerson+"</td>"+"<td>"+item.taskName+"</td><td style='width: 250px'>"
                                                if(item.taskName=='审批账号'){
                                                    positionStr=positionStr+ "<button value='" + item.taskProcessInstanceId + "' class='layui-btn' id='backProce'>撤回</button>"
                                                        +"<button value='" + item.taskProcessInstanceId + "' class='layui-btn' id='selectTa'>详细信息</button>"
                                                        + "<button value='" + item.taskProcessInstanceId + "' class='layui-btn'   id='processView'>任务进度</button>"
                                                }else {
                                                    positionStr = positionStr + "<button value='" + item.taskProcessInstanceId + "' class='layui-btn' id='actSubmit'>重新申请</button>"
                                                        + "<button value='" + item.taskProcessInstanceId + "' class='layui-btn' id='selectTa'>详细信息</button>"
                                                        + "<button value='" + item.taskProcessInstanceId + "' class='layui-btn layui-btn-sm layui-btn-normal' id='delete'><i class='layui-icon'></i>删除</button>"
                                                }
                                                positionStr=positionStr+"</td></tr>";
                                                arr.push(positionStr);
                                            });
                                            return arr.join('');

                                        }();

                                    }
                                });
                            }
                        }
                    }else if(taskType == '帐号修改'){
                        layer.alert("申请成功！", {icon: 6,time:3000});
                        layer.close(layer.index - 1);
                        if(data!=null){
                            var laypage = layui.laypage;
                            //账号Tab分页
                            laypage.render({
                                elem: 'positionPages',
                                count: data.length, //数据总数，从服务端得到
                                limit:2,
                                limits:[1,2,3,4,5,10,20,30,50,100],
                                layout: ['count', 'prev', 'page', 'next','skip','limit'],
                                jump: function(obj){
                                    //模拟渲染
                                    document.getElementById('positionLists').innerHTML = function(){
                                        var arr =[]
                                        thisData =data.concat().splice(obj.curr*obj.limit - obj.limit, obj.limit);
                                        layui.each(thisData, function(index, item){
                                            var positionStr= "<tr>"
                                                +"<td>"+item.taskId+"</td>"
                                                +"<td>"+item.taskApplyPerson+"</td>"
                                                +"<td>"+item.taskCreateTime+"</td>"
                                                +"<td>"+item.taskApp+"</td>"
                                            positionStr=positionStr+"<td>"+item.taskAccount+"</td>"
                                                +"<td>"+item.taskTypes+"</td>"
                                                +"<td>"+item.taskApprovedPerson+"</td>"+"<td>"+item.taskName+"</td><td style='width: 250px'>"
                                            if(item.taskName=='审批账号'){
                                                positionStr=positionStr+ "<button value='" + item.taskProcessInstanceId + "' class='layui-btn' id='backProce'>撤回</button>"
                                                    +"<button value='" + item.taskProcessInstanceId + "' class='layui-btn' id='selectTa'>详细信息</button>"
                                                    + "<button value='" + item.taskProcessInstanceId + "' class='layui-btn'   id='processView'>任务进度</button>"
                                            }else {
                                                positionStr=positionStr+ "<button value='" + item.taskProcessInstanceId + "' class='layui-btn' id='actSubmit'>重新申请</button>"
                                                    +"<button value='" + item.taskProcessInstanceId + "' class='layui-btn' id='selectTa'>详细信息</button>"
                                                    + "<button value='" + item.taskProcessInstanceId + "' class='layui-btn layui-btn-sm layui-btn-normal' id='delete'><i class='layui-icon'></i>删除</button>"
                                            }
                                            positionStr=positionStr+"</td></tr>";
                                            arr.push(positionStr);
                                        });
                                        return arr.join('');

                                    }();
                                }
                            });
                        }

                    }
                }
            })
        }


    } else if (taskType == '帐号启用') {

        var accountEnable = document.getElementById("account").value;
        if (accountEnable == null || accountEnable == "") {
            layer.alert("请选择要禁用的帐号!")
            return false;
        }
        if ((accountEnable != null || accountEnable != "")&&(taskType != "" || taskType != null) && (appName != null || appName != "") &&
            (audit != null || audit != "") && (role != null || role != "") && (applyReason != null || applyReason != "") ) {
            $.ajax({
                url: "/verifyActTask",
                type: "POST",
                dataType: "json",
                data: {
                    taskType:taskType,
                    appName: appName,
                    accountName: accountEnable,
                   /* orgName: orgName,*/
                    audit:audit,
                    applyReason:applyReason,
                    role:role
                },
                success: function (data) {
                    layer.close(layer.index - 1);
                    layer.alert("申请成功！", {icon: 6,time:3000});
                    getPositionLists();

                }
            });
        }
    }
}




$(document).ready(function() {
        $.ajax({
            url: "/selectApplyTast",
            type: "POST",
            success: function (data) {
                //渲染分页
                layui.use('laypage', function () {
                //账号Tab分
                var laypage = layui.laypage;
                laypage.render({
                    elem: 'positionPages',
                    count: data.length, //数据总数，从服务端得到
                    limit:2,
                    limits:[1,2,3,4,5,10,20,30,50,100],
                    layout: ['count', 'prev', 'page', 'next','skip','limit'],
                    jump: function(obj){
                        //模拟渲染
                        document.getElementById('positionLists').innerHTML = function(){
                            var arr =[];
                            thisData = data.concat().splice(obj.curr * obj.limit - obj.limit, obj.limit);
                            layui.each(thisData, function(index, item){
                                var positionStr= "<tr>"
                                    +"<td>"+item.taskId+"</td>"
                                    +"<td>"+item.taskApplyPerson+"</td>"
                                    +"<td>"+item.taskCreateTime+"</td>"
                                    +"<td>"+item.taskApp+"</td>"
                                positionStr=positionStr+"<td>"+item.taskAccount+"</td>"
                                    +"<td>"+item.taskTypes+"</td>"
                                    +"<td>"+item.taskApprovedPerson+"</td>"+"<td>"+item.taskName+"</td><td style='width: 250px'>"
                                if(item.taskName=='审批账号'){
                                    positionStr=positionStr+ "<button value='" + item.taskProcessInstanceId + "' class='layui-btn' id='backProce'>撤回</button>"
                                        +"<button value='" + item.taskProcessInstanceId + "' class='layui-btn' id='selectTa'>详细信息</button>"
                                        + "<button value='" + item.taskProcessInstanceId + "' class='layui-btn'   id='processView'>任务进度</button>"
                                }else {
                                    positionStr=positionStr+ "<button value='" + item.taskProcessInstanceId + "' class='layui-btn' id='actSubmit'>重新申请</button>"
                                        +"<button value='" + item.taskProcessInstanceId + "' class='layui-btn' id='selectTa'>详细信息</button>"
                                        + "<button value='" + item.taskProcessInstanceId + "' class='layui-btn layui-btn-sm layui-btn-normal' id='delete'><i class='layui-icon'></i>删除</button>"
                                }
                                positionStr=positionStr+"</td></tr>";
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




$(document).ready(function() {
    $("body").on("click", "#selectTa", function (e) {
        var value = $(this).val();
        $('.div').remove();
        layui.use('form', function () {
            var form = layui.form; //只有执行了这一步，部分表单元素才会自动修饰成功
            layui.use('layer', function () {
                var layer = layui.layer;
                layer.open({
                    type: 1,
                    title: "任务详细信息",   //标题
                    area: ['400px', '400px'],    //弹窗大小
                    shadeClose: false,      //禁止点击空白关闭
                    scrollbar: false,      //禁用滚动条
                    move: false,       //禁用移动
                    scrolling: 'no',
                    resize: false,
                    closeBtn: 1,
                    content: $('#accountAudit3'),
                    end: function () {
                        $('#accountAudit3').hide();
                    }
                });
            });

            $.ajax({
                url: "/SelAuditTask",
                type: "POST",
                dataType: "json",
                data: {
                    id: value
                },
                success: function (data) {
                    var userName = "<div class='layui-form-item div'><label class='layui-form-label' style='font-size:12px;'>申请者： </label>"
                        + "<div class='layui-input-inline layui-form' lay-filter='test'>"
                        + " <input id='userName' type='text' disabled   lay-vertype='tips' required class='layui-input' value='" + data[0].taskApplyPerson + "'  ></div></div>"

                    $('#userNames3').append(userName);
                   /* var orgName = "<div class='layui-form-item div'><label class='layui-form-label' style='font-size:12px;'>用户所属组织： </label>"
                        + "<div class='layui-input-inline layui-form' lay-filter='test'>"
                        + " <input id='orgName' type='text' disabled  lay-vertype='tips' required class='layui-input' value='" + data[0].taskOrgName + "'  ></div></div>"

                    $('#orgNames3').append(orgName);*/
                    var selectTs = "<div class='layui-form-item div' ><label class='layui-form-label' style='font-size:12px;'>任务类型： </label>"
                        + "<div class='layui-input-inline layui-form' lay-filter='test2'>"
                        + " <input id='taskType' class='layui-input' lay-filter='taskty' disabled lay-verify='required'   required value='" + data[0].taskTypes + "'  ></div></div>"
                    $('#taskTapys3').append(selectTs);

                    var selectA = "<div class='layui-form-item div'><label class='layui-form-label' style='font-size:12px;'>系统资源： </label>"
                        + "<div class='layui-input-inline layui-form' lay-filter='test2'>"
                        + " <input id='appName' class='layui-input' lay-filter='app'  disabled lay-verify='required'   required value='" + data[0].taskApp + "'  ></div></div>"
                    selectA = selectA + "</select></div></div>"
                    $('#types3').append(selectA);
                    var selectSa = "<div class='layui-form-item div'><label class='layui-form-label' style='font-size:12px;'>审批人： </label>"
                        + "<div class='layui-input-inline layui-form' lay-filter='test2'>"
                        + " <input id='audit'  class='select layui-input' disabled  lay-verify='required'   required value='" + data[0].taskApprovedPerson + "'  ></div></div>"

                    $('#aud3').append(selectSa);
                    var selectName2 = "<div class='layui-form-item div'><label class='layui-form-label' style='font-size:12px;'>登陆名： </label>"
                        + "<div class='layui-input-inline layui-form' lay-filter='test2'>"
                        + " <input type='text' id='accountName' disabled lay-vertype='accountField' required class='layui-input' value='" + data[0].taskAccount + "'  ></div></div>"

                    $('#field3').append(selectName2);
                    var select = "<div class='layui-form-item div'><label class='layui-form-label' style='font-size:12px;'>帐号类型： </label>"
                        + "<div class='layui-input-inline layui-form' lay-filter='test2'>"
                    if (data[0].taskActType == "1") {
                        select = select + "<input id='actType' class='layui-input' disabled lay-filter='accountType'  lay-verify='required'   required  value='一般账号'></div></div>"

                    } else if (data[0].taskActType == "2") {
                        select = select + "<input id='actType' class='layui-input' disabled lay-filter='accountType'  lay-verify='required'   required  value='公共账号'></div></div>"

                    } else if (data[0].taskActType == "3") {
                        select = select + "<input id='actType' class='layui-input' disabled lay-filter='accountType'  lay-verify='required'   required  value='接口账号'></div></div>"

                    }
                    $('#field3').append(select);
                    for(var textVl=0;textVl<data[0].textLists.length;textVl++){
                        var textVls= "<div id='div' class='layui-form-item delForm actDiv actDivs'><label class='layui-form-label labelText' style='font-size:12px;'>"+data[0].textLists[textVl].remarks +"：</label>"
                            + "<div class='layui-input-inline layui-form' lay-filter='test2'>"
                            +" <input type='text' class='text layui-input' disabled  required   value='"+data[0].textLists[textVl].defaultValues+"'  name='account' ></div></div>"
                        $('#field3').append(textVls);
                    }
                    for(var passwordVl=0;passwordVl<data[0].passwordLists.length;passwordVl++){
                        var passwordVls= "<div id='div' class='layui-form-item delForm actDiv actDivs'><label class='layui-form-label labelText' style='font-size:12px;'>"+data[0].passwordLists[passwordVl].remarks +"：</label>"
                            + "<div class='layui-input-inline layui-form' lay-filter='test2'>"
                            +" <input type='password' class='text layui-input' disabled  required   value='"+data[0].passwordLists[passwordVl].defaultValues+"'  name='account' ></div></div>"
                        $('#field3').append(passwordVls);
                    }
                    for(var selectVl=0;selectVl<data[0].selectLists.length;selectVl++){
                        var selectVls= "<div id='div' class='layui-form-item delForm actDiv actDivs'><label class='layui-form-label labelText' style='font-size:12px;'>"+data[0].selectLists[selectVl].remarks +"：</label>"
                            + "<div class='layui-input-inline layui-form' lay-filter='test2'>"
                            +" <input type='text' class='text layui-input'  disabled required   value='"+data[0].selectLists[selectVl].defaultValues+"'  name='account' ></div></div>"
                        $('#field3').append(selectVls);
                    }
                    for(var dateVl=0;dateVl<data[0].dateLists.length;dateVl++){
                        var dateVls= "<div id='div' class='layui-form-item delForm actDiv actDivs'><label class='layui-form-label labelText' style='font-size:12px;'>"+data[0].dateLists[dateVl].remarks +"：</label>"
                            + "<div class='layui-input-inline layui-form' lay-filter='test2'>"
                            +" <input type='text' class='text layui-input' disabled  required   value='"+data[0].dateLists[dateVl].defaultValues+"'  name='account' ></div></div>"
                        $('#field3').append(dateVls);
                    }

                    var applyReasont = "<div  class='layui-form-item div' id='div'><label class='layui-form-label' style='font-size:12px;'>申请理由： </label>"
                        + "<div class='layui-input-inline layui-form' lay-filter='test2'>"
                        + " <p  id='applyReason' required  disabled autocomplete='off' class='layui-text'  >" + data[0].taskApplyReason + "</p></div></div>"
                    $('#applyReasonss3').append(applyReasont);

                    var selectRole = "<div class='layui-form-item div'><label class='layui-form-label' style='font-size:12px;'>审批角色： </label>"
                        + "<div class='layui-input-inline layui-form' lay-filter='test2'>"
                        + " <input id='role'  class='select layui-input' disabled  lay-verify='required'   required value='" + data[0].taskRole + "'  ></div></div>"

                    $('#roles3').append(selectRole);
                    if (data[0].comment.length == 0) {

                    } else {
                        var approvalOpinion = "<div  class='layui-form-item div' id='div'><label class='layui-form-label' style='font-size:12px;'>历史批注： </label>"
                            + "<div class='layui-input-inline layui-form' lay-filter='test2'>"
                            + " <p   disabled autocomplete='off' class='layui-text'  >"
                        for(var co=0;co<data[0].comment.length;co++){
                            var num=co+1;
                            approvalOpinion = approvalOpinion +"第"+num+"次批注</br>批注人："+data[0].comment[co].userId+"</br>批注时间："+data[0].comment[co].time+"</br>批注内容：" +data[0].comment[co].fullMessage+"</br></br>"
                        }
                        approvalOpinion =approvalOpinion + "</p></div></div>"
                        $('#approvalOpinions3').append(approvalOpinion);
                    }
                    form.render('select', 'test2');
                }
            })

        })
    })

})


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
                content: $('#orgTree'),
                end: function () {
                    $('#orgTree').hide();
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
                        elem: '#test13',
                        data: data,
                        showLine: true,  //是否开启连接线,
                        click:function (obj) {

                            $("#accountOrg").val(obj.data.title);
                            $("#accountOrgId").val(obj.data.id);
                           layer.close(layer.index);
                        }
                    });
                })
            }
        })
    })
}



/*任务流程进度*/
$(document).ready(function() {
    $("body").on("click", "#processView", function (e) {
        var proId = $(this).val();
        layui.use('form', function () {
            var form = layui.form;
            layui.use('layer', function () {
                var layer = layui.layer;
                layer.open({
                    type: 1,
                    title: "任务流程进度图",   //标题
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






























