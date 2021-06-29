window.onload=function(){
   // var btn_1=document.getElementById("btn_1");
    //var btn_2=document.getElementById("btn_2");
    var close=document.getElementsByClassName("close");
    var close_1=document.getElementsByClassName("close_1");
    var dialog=document.getElementsByClassName("dialog");
    var form_1=document.getElementsByClassName("form_1");

  /*   var btn_xg=document.getElementById("btn_xg");
     var form_xg=document.getElementsByClassName("form_xg");
     var close_xg=document.getElementsByClassName("close_xg");
     btn_xg.addEventListener('click',function(){
         form_xg[0].className="form_xg open";
     })
     close_xg[0].addEventListener('click',function(){
         form_xg[0].className="form_xg";
     })
*/


   // btn_1.addEventListener('click',function(){
      //  form_1[0].className="form_1 open";
   // })
    close[0].addEventListener('click',function(){
        form_1[0].className="form_1";
    })
   // btn_2.addEventListener('click',function(){
       // dialog[0].style.visibility='visible';
    //})
    close_1[0].addEventListener('click',function(){
        dialog[0].style.visibility='hidden';
    })
}

/*document.getElementById('zhezhao').style.display="none";
function dianwo(){
    document.getElementById('zhezhao').style.display="";
}
function hidder(){
    document.getElementById('zhezhao').style.display="none";
}*/


layui.use(['laypage', 'layer'], function() {
    var laypage = layui.laypage,
        layer = layui.layer;

    //总页数大于页码总数
    laypage.render({
            elem: 'customPages',
            count: 100,//总数
            limit:10,//每页多少条数据
            //url:"/selectApplyTast",
            page:true,
            layout: ['count', 'prev', 'page', 'next', 'limit', 'skip']
            ,jump: function(obj){
               // if(!first){
                    current_page=obj.curr; //当前页
                    limit=obj.limit; //得到每页显示的条数
                //}
                //console.log(obj.curr+obj.limit);
            }
    });
});


layui.use('form', function() {
    var form = layui.form;
    //监听提交
    form.on('submit(submitBut)', function(data) {
        layer.msg(JSON.stringify(data.field));
        return false;
    });
});

layui.use(['form', 'laydate'], function() {
    var form = layui.form;
    var laydate = layui.laydate;

    //监听提交
    form.on('submit(submitBut)', function(data) {
        layer.msg(JSON.stringify(data.field));
        return false;
    });

    var newDate = new Date();
    nowTime = newDate.getFullYear() + "-" + (newDate.getMonth() + 1) + "-" + newDate.getDate();
    laydate.render({
        elem: '#time',
        range: '~'
        //					    	,type: 'datetime'	//开启时分秒
        ,
        format: 'yyyy-MM-dd',
        max: nowTime //min/max - 最小/大范围内的日期时间值
    });
});
/*
layui.use(['laypage', 'layer'], function() {
    var laypage = layui.laypage,
        layer = layui.layer;

    //总页数大于页码总数
    laypage.render({
        elem: 'customPages'
        ,count: 100
        ,layout: ['count', 'prev', 'page', 'next', 'limit', 'skip']
        ,jump: function(obj){
            console.log(obj)
        }
    });
});
*/
