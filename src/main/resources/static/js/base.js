

var masktemp = [

    '<div  class="mask"  style=" display: none;  position: fixed ;width: 100%; height: 100%; z-index: 100">',
    '<div style="display: flex;justify-content: center;align-items: center;height: 100%; flex-direction: column">',
    '<img src="../img/nodata.png" style="max-width: 4rem">',
    '<h3>暂无数据</h3>',
    '</div>',
    '</div>'

];

//参数一为控件ID ，data为加载的数据，3为赋值的id
function  iospick(pickId,list,dataId) {
    var userDom = document.querySelector('#'+pickId);
    var  IdDom= pickId.substring(0,pickId.length-3);
     var iddom= document.querySelector('#'+IdDom);
    for (var i = 0; i < list.length; i++) {
        if (list[i].id == dataId) {
            userDom.value = list[i].id ;
            userDom.value = list[i].value ;
            userDom.dataset['id'] =  list[i].id ;
            userDom.dataset['value'] = list[i].value ;
            iddom.value = list[i].id ;
        }
    }
}


function showProgressing(){
	

}
function closeProgressing(){
	
}
function nodataOpen(){
    document.write(masktemp.join("\n"));
    $(".mask").css('display','block')

}


function nodataClose(){
    $(".mask").css('display','none')

}

function millsToStr(dateMills,hasTime,hasSec) {
	if(!dateMills) return "";
    var date = new Date(dateMills);
    var str = "";

    var mon = date.getMonth()+1;
    if(mon<10) {
        mon = "0" + mon;
	} else {
        mon = "" + mon;
	}
    var day = date.getDate();
    if(day<10) {
        day = "0" + day;
    } else {
        day = "" + day;
    }

    str += date.getFullYear();
    str += ("-" + mon);
    str += ("-" + day);

    if(hasTime) {
        var hour = date.getHours();
        if(hour<10) {
            hour = "0" + hour;
        } else {
            hour = "" + hour;
        }

        var min = date.getMinutes();
        if(min<10) {
            min = "0" + min;
        } else {
            min = "" + min;
        }
        str += (" " + hour);
        str += (":" + min);

        if(hasSec) {
            var sec = date.getSeconds();
            if(sec<10) {
                sec = "0" + sec;
            } else {
                sec = "" + sec;
            }
            str += (":" + sec);
		}

	}


    return str;
}

$.fn.serializeObject = function(){
	    var o = {};
	    var a = this.serializeArray();
	    $.each(a, function() {
	        if (o[this.name] !== undefined) {
	            if (!o[this.name].push) {
	                o[this.name] = [o[this.name]];
	            }
	            o[this.name].push(this.value || '');
	        } else {
	            o[this.name] = this.value || '';
	        }
	    });
	    return o;
	}

function getQueryString(name) { 
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i"); 
    var r = window.location.search.substr(1).match(reg); 
    if (r != null) return unescape(r[2]); 
    return null; 
}  

//获取根目录路径
function rootPath() {
    var result;
    var localObj = window.location;
    var host=localObj.protocol+"//"+localObj.host;
    // 针对IE8
    if (!Array.prototype.indexOf){
        Array.prototype.indexOf = function(elt /* , from */){
            var len = this.length >>> 0;
            var from = Number(arguments[1]) || 0;
            from = (from < 0)
                ? Math.ceil(from)
                : Math.floor(from);
            if (from < 0)
                from += len;
            for (; from < len; from++)
            {
                if (from in this &&
                    this[from] === elt)
                    return from;
            }
            return -1;
        };
    }
    /*if(host.indexOf(".com")>0){
        result=host;
    }else{*/
        var contextPath = localObj.pathname.split("/")[1];

        var basePath = localObj.protocol+"//"+localObj.host;

        result=basePath;
    /*}*/
    return result;
};
function getrootHost() {
	var result;
	var localObj = window.location;
	var pathName = window.document.location.pathname;
	var projectName = pathName.substring(0, pathName.substr(1).indexOf('/') + 1);
	var host = localObj.protocol + "//" + localObj.host+projectName;
	return host;
}

function getrootHost1() {
	var url = document.location.toString();
	return url;
}
function valiResponseResult(responseResult) {
	if(responseResult.system_result_key != 0){
		
		if(responseResult.system_result_key=='7'){
			
			if(self!=top){//
				closeProgressing();
//				parent.$.messager.confirm("操作提示", responseResult.system_result_message_key, function(data){
//					
//		            if(data){
//		            	top.location.href = rootPath();
//		            }
//		            else {
//		               
//		            }
//		        });
				location.href = getrootHost()+"/pages/login.html";
			}else{
				
				closeProgressing();
//				$.messager.confirm("操作提示", responseResult.system_result_message_key, function(data){
//					
//		            if(data){
//		            	top.location.href = rootPath();
//		            }
//		            else {
//		               
//		            }
//		        });
				location.href = getrootHost()+"/pages/login.html";
			}
			
			
		}else{
			
			if(self!=top){//
				
				closeProgressing();
				//parent.$.messager.alert('消息提示',responseResult.system_result_message_key,'error');
				
			}else{
				
				closeProgressing();
				//$.messager.alert('消息提示',responseResult.system_result_message_key,'error');
			}
			
		}
		
		return false;
		
	}

	if(responseResult.app_result_key != 0){
		
		handleAppError(responseResult);
		return false;
	}
	
	return true;
	
}

// 处理应用级错误!
function handleAppError(iframeResponseText) {

	var errMsg = iframeResponseText.system_result_message_key;
	if(!errMsg) {
		errMsg = iframeResponseText.app_result_message_key;
		if(!errMsg) {
            errMsg = "出现未知错误。";
		}

	}

	if (self != top) {//
        baseAlert(errMsg )
	} else {
        baseAlert(errMsg)
	}
}

function sendRequestAjax(loadUrl, callBackFun,customErrHandler) {
	$.ajax({
		cache : true,
		type : "POST",
		url : loadUrl + "&rfm=" + Math.random(),
		data : $('#baseForm').serialize(),//
		async : false,
		dataType : 'text', //
		error : function(XMLHttpRequest, textStatus, errorThrown) {

			if(customErrHandler) {
                customErrHandler(XMLHttpRequest, textStatus, errorThrown);
                return;
			}

			if (self != top) {//
				//closeProgressing();
//				$.messager.alert('请求错误', '服务器响应数据错误!或服务器已关闭。', 'error');
// 				alert(iframeResponseText.msg)
                baseAlert(textStatus);
			} else {
				//closeProgressing();
//				$.messager.alert('请求错误', '服务器响应数据错误!或服务器已关闭。', 'error');
// 				alert(iframeResponseText.msg)
                baseAlert(textStatus);
			}
		},
		success : function(responseResult) {
			var sArr = responseResult.match(/\[.*?\]/g);
			var da = $.parseJSON(responseResult);
			if (!valiResponseResult(da)) {
				return;
			}
			callBackFun(da);
		}
	});
}

function sendRequestAjax2(loadUrl,callBackFun){
	
	$.ajax({
	        cache: true,
	        type: "POST",
	        url:loadUrl+"&rfm="+Math.random(),
	        data:$('#baseForm').serialize(),//
	        async: false,
	        dataType: 'text', //
	        error: function(XMLHttpRequest, textStatus, errorThrown){
				
				if(self!=top){//
					parent.closeProgressing();
					parent.$.messager.alert('请求错误','服务器响应数据错误!或服务器已关闭。','error');
				}else{
					
					closeProgressing();
					$.messager.alert('请求错误','服务器响应数据错误!或服务器已关闭。','error');
					
				}	
				
			},
	        success: function(responseResult) {
				var sArr = responseResult.match(/\[.*?\]/g);
				var da = $.parseJSON(responseResult);
				if(!valiResponseResult(da)){
					return;
				}
				
	            callBackFun(da);
				
	        }
	    });
} 

function setFormDefault(responseResult) {

	if (responseResult != null && responseResult.entity != null) {

		var values = $("#baseForm").serializeArray();
		for (index = 0; index < values.length; ++index) {
			// $("#"+values[index].name).attr('value',
			// responseResult.entity[values[index].name]);
			if (responseResult.entity[values[index].name] != null
					&& typeof (responseResult.entity[values[index].name]) != 'undefined') {
				$("#" + values[index].name).val(
						responseResult.entity[values[index].name]);
			}
		}

	} else {

		document.getElementById('baseForm').reset();
	}
}

function gridOperate(id, loadUrl, callBackFun) {
	$.ajax({
		cache : true,
		type : "POST",
		url : loadUrl + "&rfm=" + Math.random(),
		data : {id:id},//
		async : true,
		dataType : 'text', //
		error : function(XMLHttpRequest, textStatus, errorThrown) {

			if (self != top) {//
				parent.$.messager.alert('请求错误', '服务器响应数据错误!或服务器已关闭。', 'error');
			} else {
				$.messager.alert('请求错误', '服务器响应数据错误!或服务器已关闭。', 'error');
			}
		},
		success : function(responseResult) {
			var sArr = responseResult.match(/\[.*?\]/g);
			var da = $.parseJSON(responseResult);
			if (!valiResponseResult(da)) {
				return;
			}
			callBackFun(da);
		}
	});
}

function gridOperateData(data, loadUrl, callBackFun) {
	$.ajax({
		cache : true,
		type : "POST",
		url : loadUrl + "&rfm=" + Math.random(),
		data : data,//
		async : true,
		dataType : 'text', //
		error : function(XMLHttpRequest, textStatus, errorThrown) {

			if (self != top) {//
				//closeProgressing();
				parent.$.messager.alert('请求错误', '服务器响应数据错误!或服务器已关闭。', 'error');
			} else {
				//closeProgressing();
				$.messager.alert('请求错误', '服务器响应数据错误!或服务器已关闭。', 'error');
			}
		},
		success : function(responseResult) {
			var sArr = responseResult.match(/\[.*?\]/g);
			var da = $.parseJSON(responseResult);
			if (!valiResponseResult(da)) {
				return;
			}
			callBackFun(da);
		}
	});
}

function baseAlert(__msg) {

    if(typeof(msg)!=="undefined" && msg){
    	notice(__msg);
    } else {
    	notice(__msg);
	}

}

//fileName file的id，url请求后台链接
function commonUploadFile(fileName, url,callBackFun){
        $.ajaxFileUpload ({
        	url: url, // 用于文件上传的服务器端请求地址
        	type: 'post',
            data : {},
            secureuri: false, // 是否需要安全协议，一般设置为false
            fileElementId: fileName, // 文件上传域的ID
            dataType: 'json', // 返回值类型 一般设置为json
            success: function (data) {
            	callBackFun(data)
            }
        })
}