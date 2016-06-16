var intervalProcess ;
var taskpage;
var startprogress;
$(function() {
    $(window).bind("load resize", function() {
        topOffset = 50;
        width = (this.window.innerWidth > 0) ? this.window.innerWidth : this.screen.width;
        if (width < 768) {
            $('div.navbar-collapse').addClass('collapse');
            topOffset = 100; // 2-row-menu
        } else {
            $('div.navbar-collapse').removeClass('collapse');
        }

        height = ((this.window.innerHeight > 0) ? this.window.innerHeight : this.screen.height) - 1;
        height = height - topOffset;
        if (height < 1) height = 1;
        if (height > topOffset) {
            $("#page-wrapper").css("min-height", (height) + "px");
        }
    });
});

function page(pageNum,that) {
    switch(pageNum){
        case 1:
            clearActive();
            $(that).parent().addClass("active");
            showGroup();
            taskpage = 1;
            clearInterval( intervalProcess );
            break;
        case 2:
            clearActive();
            $(that).parent().addClass("active");
            addUser();
            taskpage = 1;
            clearInterval( intervalProcess );
            break;
        case 3:
            clearActive();
            $(that).parent().addClass("active");
            showUser(1);
            taskpage = 1;
            clearInterval( intervalProcess );
            break;
        case 4:
            clearActive();
            $(that).parent().addClass("active");
            addTask();
            taskpage = 1;
            clearInterval( intervalProcess );
            break;
        case 5:
            clearActive();
            $(that).parent().addClass("active");
            showTask(1);
            break;
        case 6:
            clearActive();
            $(that).parent().addClass("active");
            clearInterval( intervalProcess );
            taskpage = 1;
            break;
        case 7:
            clearActive();
            $(that).parent().addClass("active");
            resourceStatus();
            clearInterval( intervalProcess );
            taskpage = 1;
            break;
        case 8:
            clearActive();
            $(that).parent().addClass("active");
            resourceQueue();
            clearInterval( intervalProcess );
            taskpage = 1;
            break;
        case 9:
            clearActive();
            $(that).parent().addClass("active");
            clearInterval( intervalProcess );
            taskpage = 1;
            break;
        case 10:
            clearActive();
            $(that).parent().addClass("active");
            clearInterval( intervalProcess );
            taskpage = 1;
            break;
        case 11:
            updateUser(that);
            clearInterval( intervalProcess );
            taskpage = 1;
            break;
        case 12:
            updateTask(that);
            clearInterval( intervalProcess );
            taskpage = 1;
            break;
        case 13:
            clearInterval( intervalProcess );
            findUser();
            taskpage = 1;
            break;
        case 14:
            clearInterval( intervalProcess );
            findTask();
            taskpage = 1;
            break;
    }
}

function clearActive(){
    $("li.active").removeClass("active");
}

function loading(){
	var boolID = false;
	var boolName = false;
    Ladda.bind( '.submit button', {
      callback: function( instance ) {
	        var progress = 0;
	        var interval = setInterval( function() {
	          if( progress === 1 ) {
	            instance.stop();
	            clearInterval( interval );
	          }
	        }, 200 );
	        if(boolID&&boolName&&$("#addPassword").val()!=""&&$("#addDepartment").val()!=""&&$("#addRole").val()!=""){
	        $.ajax({
	    		url:ADDRESS+'/userInfo/UserInfo_add.action',
	    		type:'post',
	    		data:{	"id" : $("#addID").val(),
						"name" : $("#addName").val(),
						"password":$("#addPassword").val(),
						"department": $("#addDepartment option:selected").val(),
						"role":$("#addRole option:selected").val()},
	    		error:function(){
	    			alert("Error:服务器错误!");
	    		},
	    		success:function(data){
	    			$("#addID").val("");
	    			$("#addName").val("");
	    			$("#addPassword").val("");
	    			$("#addDepartment").val("运营部");
	    			$("#addRole").val("user");
	    			$("#addID").nextAll("div.alert").remove();
	    			$("#addName").nextAll("div.alert").remove();
	    			progress = 1;
	    			$('#notifyBtn').click();
	    		}
			});
	      }else{
	    	  progress = 1;
	    	  $('#notifyBtn2').click();
	      }
      }
    });
    $("#addID").blur(function() {
		var val = $(this).val();
		val = $.trim(val);
		var $this = $(this);
		if (val != "") {
			$this.nextAll("div.alert").remove();
			var url = ADDRESS+"/user/User_check.action";
			var args = {
				"id" : val
			};
			$.post(url, args, function(data) {
				//表示可用
				if (data == "1") {
					$this.after("<div class=\"alert alert-success\">该ID可以使用.</div>");
					boolID = true;
				}
				//不可用
				else if (data == "0") {
					$this.after("<div class=\"alert alert-danger\">该ID已被注册.</div>");
					boolID = false;
				}
				//服务器错误
				else {
					alert("Error:服务器错误!");
					boolID = false;
				}
			});
		} else {
			$(this).val("");
			boolID = false;
		}
	});
	$("#addName").blur(function() {
		var val = $(this).val();
		val = $.trim(val);
		var $this = $(this);
		if (val != "") {
			$this.nextAll("div.alert").remove();
			var url = ADDRESS+"/user/User_check.action";
			var args = {
				"name" : val
			};
			$.post(url, args, function(data) {
				//表示可用
				if (data == "1") {
					$this.after("<div class=\"alert alert-success\">该用户名可以使用.</div>");
					boolName = true;
				}
				//不可用
				else if (data == "0") {
					$this.after("<div class=\"alert alert-danger\">该用户名已被注册.</div>");
					boolName = false;
				}
				//服务器错误
				else {
					alert("Error:服务器错误!");
					boolName = false;
				}
			});
		} else {
			$(this).val("");
			boolName = false;
		}
	});
}

function showGroup(){
    $("#page-wrapper").empty();
    var string = 
        "<div class=\"row\">"+
            "<div class=\"col-lg-12\">"+
                "<h1 class=\"page-header\">集群管理</h1>"+
            "</div>"+
        "</div>";
     $("#page-wrapper").append(string);
}

function addUser(){
    $("#page-wrapper").empty();
    var string = 
        "<div class=\"row\">"+
            "<div class=\"col-lg-12\">"+
                "<h1 class=\"page-header\">添加用户</h1>"+
            "</div>"+
        "</div>"+
        "<div class=\"row\">"+
            "<div class=\"col-lg-12\">"+
                "<div class=\"form-group\">"+
                    "<label>用户ID</label>"+
                    "<input class=\"form-control\" placeholder=\"userID\" id=\"addID\">"+
                    "<p class=\"help-block\">5个字符，可使用字母、数字，需以字母开头</p>"+
                "</div>"+
                "<div class=\"form-group\">"+
                    "<label>用户名</label>"+
                    "<input class=\"form-control\" placeholder=\"userName\" id=\"addName\">"+
                    "<p class=\"help-block\">6~18个字符，可使用字母、数字、下划线，需以字母开头</p>"+
                "</div>"+
                "<div class=\"form-group\">"+
                    "<label>密码</label>"+
                    "<input class=\"form-control\" type=\"password\" placeholder=\"password\" id=\"addPassword\">"+
                    "<p class=\"help-block\">6~16个字符，区分大小写</p>"+
                "</div>"+
                "<div class=\"form-group\">"+
                    "<label>部门</label>"+
                    "<div class=\"form-group\">"+
	                    "<select class=\"form-control\" id=\"addDepartment\">"+
	                        "<option>运营部</option>"+
	                        "<option>宣传部</option>"+
	                        "<option>技术部</option>"+
	                        "<option>其他</option>"+
	                    "</select>"+
                    "</div>"+
                    "<p class=\"help-block\">请选择其中一个部门或其他</p>"+
                "</div>"+
                "<div class=\"form-group\">"+
                "<label>类型</label>"+
                "<div class=\"form-group\">"+
                    "<select class=\"form-control\" id=\"addRole\">"+
                        "<option>user</option>"+
                        "<option>vip</option>"+
                    "</select>"+
                "</div>"+
                "<p class=\"help-block\">请选择其中一个部门或其他</p>"+
            "</div>"+
                "<div class=\"submit\">"+
                    "<button class=\"btn btn-primary ladda-button\" data-style=\"expand-right\" style=\"min-width:100px\"><span class=\"ladda-label\" style=\"padding:10px 12px;line-height:24px;font-size:18px;\">提交</span></button>"+
                "</div>"+
            "</div>"+
        "</div>"+
        "<button style=\"display:none\" data-toggle=\"modal\" data-target=\"#myModal\" id=\"notifyBtn\"></button>"+
        "<div class=\"modal fade\" id=\"myModal\" tabindex=\"-1\" role=\"dialog\" aria-labelledby=\"myModalLabel\" aria-hidden=\"true\">"+
            "<div class=\"modal-dialog\">"+
                "<div class=\"modal-content\">"+
                    "<div class=\"modal-header\">"+
                        "<button type=\"button\" class=\"close\" data-dismiss=\"modal\" aria-hidden=\"true\">&times;</button>"+
                        "<h4 class=\"modal-title\" id=\"myModalLabel\">添加成功</h4>"+
                    "</div>"+
                    "<div class=\"modal-body\">您已成功添加了该用户</div>"+
                    "<div class=\"modal-footer\">"+
                        "<button type=\"button\" class=\"btn btn-default\" data-dismiss=\"modal\">Close</button>"+
                    "</div>"+
                "</div>"+
            "</div>"+
        "</div>"+
        "<button style=\"display:none\" data-toggle=\"modal\" data-target=\"#myModal2\" id=\"notifyBtn2\"></button>"+
        "<div class=\"modal fade\" id=\"myModal2\" tabindex=\"-1\" role=\"dialog\" aria-labelledby=\"myModalLabel\" aria-hidden=\"true\">"+
            "<div class=\"modal-dialog\">"+
                "<div class=\"modal-content\">"+
                    "<div class=\"modal-header\">"+
                        "<button type=\"button\" class=\"close\" data-dismiss=\"modal\" aria-hidden=\"true\">&times;</button>"+
                        "<h4 class=\"modal-title\" id=\"myModalLabel\">添加失败！</h4>"+
                    "</div>"+
                    "<div class=\"modal-body\">请正确填写！</div>"+
                    "<div class=\"modal-footer\">"+
                        "<button type=\"button\" class=\"btn btn-default\" data-dismiss=\"modal\">Close</button>"+
                    "</div>"+
                "</div>"+
            "</div>"+
        "</div>";
          
    $("#page-wrapper").append(string);
    loading();
}

function showUser(page){
    $("#page-wrapper").empty();
    var string = 
        "<div class=\"row\">"+
            "<div class=\"col-lg-12\">"+
                "<h1 class=\"page-header\">用户列表</h1>"+
            "</div>"+
            "<div class=\"input-group sidebar-search\">"+
                "<input type=\"text\" class=\"form-control\" placeholder=\"Search...\" id=\"findText\">"+
                "<span class=\"input-group-btn\">"+
	                "<button class=\"btn btn-default\" type=\"button\" onclick=\"page(13,this)\">"+
	                    "<i class=\"fa fa-search\"></i>"+
	                "</button>"+
                "</span>"+
            "</div>"+
        "</div>";
    $.ajax({
		url:ADDRESS+'/userInfo/UserInfo_query.action',
		type:'post',
		data:{'page':page},
		error:function(){
			string+="<div>出错！</div>";
			$("#page-wrapper").append(string);
		},
		success:function(data){
			string += "<div class=\"row\">"+
            "<div class=\"col-lg-12\">"+
            "<table class=\"table table-striped\">"+
              "<thead><tr><th>ID</th><th>用户名</th><th>角色</th><th>部门</th><th>创建时间</th><th>操作</th><th></th></tr></thead>"+
              "<tbody>";
			if(data=="0")
				string += "<tr><td>空！</td></tr>"+
					"</tbody>"+
				"</table>";
			else{
				var res = data.split(";");
				var pageSum = res[0];
				for(var i=1;i<res.length-1;i++){
					var temp = res[i].split(",");
					string += "<tr>"+
                    "<td>"+temp[0]+"</td>"+
                    "<td>"+temp[1]+"</td>"+
                    "<td>"+temp[2]+"</td>"+
                    "<td>"+temp[3]+"</td>"+
                    "<td>"+temp[4]+"</td>"+
                    "<td class=\"operate\">"+
                      "<a href=\"####\" onclick=\"page(11,this)\">修改</a>"+
                    "</td>"+
                    "<td class=\"operate\">"+
                      "<a href=\"####\" onclick=\"del(this)\">删除</a>"+
                    "</td>"+
                  "</tr>";
				}
				string += "</tbody>"+
	              "</table>"+
	              "<div class=\"btn-toolbar pageBtn\" role=\"toolbar\" aria-label=\"page\">"+
	                "<div class=\"btn-group\" role=\"group\" aria-label=\"fpage\">";
				if(page==1)
					string += "<button type=\"button\" class=\"btn btn-default disabled\">上一页</button>";
				else
					string += "<button type=\"button\" class=\"btn btn-default\" onclick=\"showUser("+(page-1)+")\">上一页</button>";
				string += "</div>"+
                "<div class=\"btn-group\" role=\"group\" aria-label=\"spage\">";
	            if(pageSum>7){
					if(page<4){
						for(var i=1;i<=5;i++){
							if(i!=page)
								string += "<button type=\"button\" class=\"btn btn-default\" onclick=\"showUser("+i+")\">"+i+"</button>";
							else
								string += "<button type=\"button\" class=\"btn btn-default disabled\">"+i+"</button>";
						}
						string += "<button type=\"button\" class=\"btn btn-default\">……</button>";
						string += "<button type=\"button\" class=\"btn btn-default\" onclick=\"showUser("+pageSum+")\">"+pageSum+"</button>";
					}else if(page>=4&&page<(pageSum-4)){
						for(var i=page-2;i<=page+2;i++){
							if(i!=page)
								string += "<button type=\"button\" class=\"btn btn-default\" onclick=\"showUser("+i+")\">"+i+"</button>";
							else
								string += "<button type=\"button\" class=\"btn btn-default disabled\">"+i+"</button>";
						}
						string += "<button type=\"button\" class=\"btn btn-default\">……</button>";
						string += "<button type=\"button\" class=\"btn btn-default\" onclick=\"showUser("+pageSum+")\">"+pageSum+"</button>";
					}else{
						for(var i=pageSum-6;i<=pageSum;i++){
							if(i!=page)
								string += "<button type=\"button\" class=\"btn btn-default\" onclick=\"showUser("+i+")\">"+i+"</button>";
							else
								string += "<button type=\"button\" class=\"btn btn-default disabled\">"+i+"</button>";
						}
					}
	            }else{
					for(var i=1;i<=pageSum;i++){
						if(i!=page)
							string += "<button type=\"button\" class=\"btn btn-default\" onclick=\"showUser("+i+")\">"+i+"</button>";
						else
							string += "<button type=\"button\" class=\"btn btn-default disabled\">"+i+"</button>";
					}
				}
	            string += "</div>"+
                "<div class=\"btn-group\" role=\"group\" aria-label=\"tpage\">";
	            if(page==pageSum)
	            	string += "<button type=\"button\" class=\"btn btn-default disabled\">下一页</button>";
	            else
	            	string += "<button type=\"button\" class=\"btn btn-default\" onclick=\"showUser("+(page+1)+")\">下一页</button>";
	            string += "</div>"+
	              "</div>"+
		            "</div>"+
		        "</div>"+
		        "<button style=\"display:none\" data-toggle=\"modal\" data-target=\"#myModal5\" id=\"notifyBtn5\"></button>"+
		        "<div class=\"modal fade\" id=\"myModal5\" tabindex=\"-1\" role=\"dialog\" aria-labelledby=\"myModalLabel\" aria-hidden=\"true\">"+
		            "<div class=\"modal-dialog\">"+
		                "<div class=\"modal-content\">"+
		                    "<div class=\"modal-header\">"+
		                        "<button type=\"button\" class=\"close\" data-dismiss=\"modal\" aria-hidden=\"true\">&times;</button>"+
		                        "<h4 class=\"modal-title\" id=\"myModalLabel\">删除成功！</h4>"+
		                    "</div>"+
		                    "<div class=\"modal-footer\">"+
		                        "<button type=\"button\" class=\"btn btn-default\" data-dismiss=\"modal\">Close</button>"+
		                    "</div>"+
		                "</div>"+
		            "</div>"+
		        "</div>"+
		        "<button style=\"display:none\" data-toggle=\"modal\" data-target=\"#myModal6\" id=\"notifyBtn6\"></button>"+
		        "<div class=\"modal fade\" id=\"myModal6\" tabindex=\"-1\" role=\"dialog\" aria-labelledby=\"myModalLabel\" aria-hidden=\"true\">"+
		            "<div class=\"modal-dialog\">"+
		                "<div class=\"modal-content\">"+
		                    "<div class=\"modal-header\">"+
		                        "<button type=\"button\" class=\"close\" data-dismiss=\"modal\" aria-hidden=\"true\">&times;</button>"+
		                        "<h4 class=\"modal-title\" id=\"myModalLabel\">删除失败！</h4>"+
		                    "</div>"+
		                    "<div class=\"modal-footer\">"+
		                        "<button type=\"button\" class=\"btn btn-default\" data-dismiss=\"modal\">Close</button>"+
		                    "</div>"+
		                "</div>"+
		            "</div>"+
		        "</div>";
			}
			$("#page-wrapper").append(string);
		}
	});
}

function loadingU(){
	var boolName = false;
	var oldName = $("#updateName").val();
    Ladda.bind( '.submit button', {
      callback: function( instance ) {
	        var progress = 0;
	        var interval = setInterval( function() {
	          if( progress === 1 ) {
	            instance.stop();
	            clearInterval( interval );
	          }
	        }, 200 );
	        if(boolName&&$("#updatePassword").val()!=""&&$("#updateDepartment").val()!=""){
	        $.ajax({
	    		url:ADDRESS+'/userInfo/UserInfo_save.action',
	    		type:'post',
	    		data:{	"id" : $("#updateID").val(),
	    				"name" : $("#updateName").val(),
						"department": $("#updateDepartment option:selected").val()},
	    		error:function(){
	    			alert("Error:服务器错误!");
	    		},
	    		success:function(data){
	    			$("#updateName").nextAll("div.alert").remove();
	    			progress = 1;
	    			$('#notifyBtn3').click();
	    		}
			});
	      }else{
	    	  progress = 1;
	    	  $('#notifyBtn4').click();
	      }
      }
    });
    
	$("#updateName").blur(function() {
		if($(this).val()==oldName){
			boolName = true;
			return;
		}
		var val = $(this).val();
		val = $.trim(val);
		var $this = $(this);
		if (val != "") {
			$this.nextAll("div.alert").remove();
			var url = ADDRESS+"/user/User_check.action";
			var args = {
				"name" : val
			};
			$.post(url, args, function(data) {
				//表示可用
				if (data == "1") {
					$this.after("<div class=\"alert alert-success\">该用户名可以使用.</div>");
					boolName = true;
				}
				//不可用
				else if (data == "0") {
					$this.after("<div class=\"alert alert-danger\">该用户名已被注册.</div>");
					boolName = false;
				}
				//服务器错误
				else {
					alert("Error:服务器错误!");
					boolName = false;
				}
			});
		} else {
			boolName = false;
		}
	});
}

function updateUser(that){
	var temp = $(that).parent().parent().children('td');
	$("#page-wrapper").empty();
    var string = 
        "<div class=\"row\">"+
            "<div class=\"col-lg-12\">"+
                "<h1 class=\"page-header\">修改用户</h1>"+
            "</div>"+
        "</div>"+
        "<div class=\"row\">"+
            "<div class=\"col-lg-12\">"+
                "<div class=\"form-group\">"+
                    "<label>用户ID</label>"+
                    "<input class=\"form-control\" placeholder=\"暂时不支持用户ID的更改\" id=\"updateID\" disabled>"+
                    "<p class=\"help-block\">暂时不支持用户ID的更改</p>"+
                "</div>"+
                "<div class=\"form-group\">"+
                    "<label>用户名</label>"+
                    "<input class=\"form-control\" placeholder=\"userName\" id=\"updateName\">"+
                    "<p class=\"help-block\">6~18个字符，可使用字母、数字、下划线，需以字母开头</p>"+
                "</div>"+
                "<div class=\"form-group\">"+
                    "<label>密码</label>"+
                    "<input class=\"form-control\" type=\"password\" placeholder=\"暂时不支持密码更改\" id=\"updatePassword\" disabled>"+
                    "<p class=\"help-block\">暂时不支持密码更改</p>"+
                "</div>"+
                "<div class=\"form-group\">"+
                    "<label>部门</label>"+
                    "<div class=\"form-group\">"+
	                    "<select class=\"form-control\" id=\"updateDepartment\">"+
	                        "<option>运营部</option>"+
	                        "<option>宣传部</option>"+
	                        "<option>技术部</option>"+
	                        "<option>其他</option>"+
	                    "</select>"+
                    "</div>"+
                    "<p class=\"help-block\">请选择其中一个部门或其他</p>"+
                "</div>"+
                "<div class=\"submit\">"+
                    "<button class=\"btn btn-primary ladda-button\" data-style=\"expand-right\" style=\"min-width:100px\"><span class=\"ladda-label\" style=\"padding:10px 12px;line-height:24px;font-size:18px;\">提交</span></button>"+
                "</div>"+
            "</div>"+
        "</div>"+
        "<button style=\"display:none\" data-toggle=\"modal\" data-target=\"#myModal3\" id=\"notifyBtn3\"></button>"+
        "<div class=\"modal fade\" id=\"myModal3\" tabindex=\"-1\" role=\"dialog\" aria-labelledby=\"myModalLabel\" aria-hidden=\"true\">"+
            "<div class=\"modal-dialog\">"+
                "<div class=\"modal-content\">"+
                    "<div class=\"modal-header\">"+
                        "<button type=\"button\" class=\"close\" data-dismiss=\"modal\" aria-hidden=\"true\">&times;</button>"+
                        "<h4 class=\"modal-title\" id=\"myModalLabel\">修改成功</h4>"+
                    "</div>"+
                    "<div class=\"modal-body\">您已成功修改了该用户</div>"+
                    "<div class=\"modal-footer\">"+
                        "<button type=\"button\" class=\"btn btn-default\" data-dismiss=\"modal\">Close</button>"+
                    "</div>"+
                "</div>"+
            "</div>"+
        "</div>"+
        "<button style=\"display:none\" data-toggle=\"modal\" data-target=\"#myModal4\" id=\"notifyBtn4\"></button>"+
        "<div class=\"modal fade\" id=\"myModal4\" tabindex=\"-1\" role=\"dialog\" aria-labelledby=\"myModalLabel\" aria-hidden=\"true\">"+
            "<div class=\"modal-dialog\">"+
                "<div class=\"modal-content\">"+
                    "<div class=\"modal-header\">"+
                        "<button type=\"button\" class=\"close\" data-dismiss=\"modal\" aria-hidden=\"true\">&times;</button>"+
                        "<h4 class=\"modal-title\" id=\"myModalLabel\">修改失败！</h4>"+
                    "</div>"+
                    "<div class=\"modal-body\">请正确填写！</div>"+
                    "<div class=\"modal-footer\">"+
                        "<button type=\"button\" class=\"btn btn-default\" data-dismiss=\"modal\">Close</button>"+
                    "</div>"+
                "</div>"+
            "</div>"+
        "</div>";
          
    $("#page-wrapper").append(string);
    $("#updateID").val(temp.eq(0).html());
	$("#updateName").val(temp.eq(1).html());
	$("#updatePassword").val("111111111111");
    $("#updateDepartment").val(temp.eq(3).html());
    loadingU();
}

function del(obj){
	var val = $(obj).parent().parent().children('td');
	if(confirm("确定删除?")){
		var url = ADDRESS+"/userInfo/UserInfo_delete.action";
		var args = {
			"id" : val.eq(0).html()
		};
		$.get(url, args, function(data) {
			if (data == "1") {
				$(obj).parent().parent().remove();
				$('#notifyBtn5').click();
			}else{
				$('#notifyBtn6').click();
			}
		});
	}
}

function loadingT(){
	var boolID = false;
    Ladda.bind( '.submitTask button', {
      callback: function( instance ) {
	        var progress = 0;
	        var interval = setInterval( function() {
	          if( progress === 1 ) {
	            instance.stop();
	            clearInterval( interval );
	          }
	        }, 200 );
	        if(boolID&&$("#addTaskName").val()!=null&&$("#addTasklog").val()!=""&&$("#addTaskRes").val()!=""&&$("#addTaskRes").val()!=""&&$("#addTaskErr").val()!=""){
	        $.ajax({
	    		url:ADDRESS+'/taskInfo/TaskInfo_add.action',
	    		type:'post',
	    		data:{	"id" : $("#addTaskID").val(),
					"TaskLog" : $("#addTasklog").val(),
					"TaskResult":$("#addTaskRes").val(),
					"TaskError": $("#addTaskErr").val(),
					"TaskName":$("#addTaskName option:selected").val()},
	    		error:function(){
	    			progress = 1;
	    			alert("Error:服务器错误!");
	    		},
	    		success:function(data){
	    			$("#addTaskID").val("");
	    			$("#addTasklog").val("");
	    			$("#addTaskRes").val("");
	    			$("#addTaskErr").val("");
	    			$("#addTaskName").val("");
	    			$("#addTaskID").nextAll("div.alert").remove();
	    			progress = 1;
	    			showTask(1);
	    			$('#notifyBtn').click();
	    		}
			});
	      }else{
	    	  progress = 1;
	    	  $('#notifyBtn2').click();
	      }
      }
    });
    Ladda.bind( '.submitJar button', {
        callback: function( instance ) {
	  	        var progress = 0;
	  	        var interval = setInterval( function() {
	  	          if( progress === 1 ) {
	  	            instance.stop();
	  	            clearInterval( interval );
	  	          }
	  	        }, 200 );
	  	      if($('#photoCover').val()==""){
	        		alert("请选择要上传的文件！");
	        		progress = 1;
	  	      }else{
		  	      $.ajaxFileUpload(
		  		        {
		  		            url:ADDRESS+"/jarFileUpload/JarFileUploadAction.action", //用于文件上传的服务器端请求地址
		  		            secureuri: false, //是否需要安全协议，一般设置为false
		  		            fileElementId: "file", //文件上传域的ID
		  		            dataType: 'json', //返回值类型 一般设置为json
		  		            success: function (data, status)  //服务器成功响应处理函数
		  		            {
		  		            	if(data['message']=="ResourceErr"){
		  		            		alert("资源不够！");
		  		            		$('#photoCover').val("");
		  		            		progress = 1;
		  		            		return;
		  		            	}
		  		            	progress = 1;
		  		    			$('#notifyBtn').click();
		  		    			$('#photoCover').val("");
		  		    			$('#profileA').click();
		  		            },
		  		            error: function (data, status, e)//服务器响应失败处理函数
		  		            {
		  		            	progress = 1;
		  		            	alert("Error:服务器错误!");
		  		            }
		  		        }
		  		    );
	  	      }
    	}
    });
    $("#addTaskID").blur(function() {
		var val = $(this).val();
		val = $.trim(val);
		var $this = $(this);
		if (val != "") {
			$this.nextAll("div.alert").remove();
			var url = ADDRESS+"/taskInfo/TaskInfo_check.action";
			var args = {
				"id" : val
			};
			$.post(url, args, function(data) {
				//表示可用
				if (data == "1") {
					$this.after("<div class=\"alert alert-success\">该ID可以使用.</div>");
					boolID = true;
				}
				//不可用
				else if (data == "0") {
					$this.after("<div class=\"alert alert-danger\">该ID已被注册.</div>");
					boolID = false;
				}
				//服务器错误
				else {
					alert("Error:服务器错误!");
					boolID = false;
				}
			});
		} else {
			$(this).val("");
			boolID = false;
		}
	});
}

function addTask(){
    $("#page-wrapper").empty();
    var string = 
        "<div class=\"row\">"+
            "<div class=\"col-lg-12\">"+
                "<h1 class=\"page-header\">新建任务</h1>"+
            "</div>"+
        "</div>"+
        "<div class=\"row\">"+
            "<div class=\"col-lg-12\">"+
                "<div class=\"form-group\">"+
                    "<label>任务ID</label>"+
                    "<input class=\"form-control\" placeholder=\"taskID\" id=\"addTaskID\">"+
                    "<p class=\"help-block\">5个字符，可使用字母、数字，需以字母开头</p>"+
                "</div>"+
                "<div class=\"form-group\">"+"<label>选择作业</label>"+
                "<div class=\"panel panel-default\">"+
                        
                        "<div class=\"panel-body\">"+
                            "<ul class=\"nav nav-tabs\">"+
                                "<li class=\"active\"><a href=\"#home\" data-toggle=\"tab\">上传作业</a>"+
                                "</li>"+
                                "<li><a id=\"profileA\" href=\"#profile\" data-toggle=\"tab\" onclick=\"showJob()\">已有作业</a>"+
                                "</li>"+
                            "</ul>"+
                            "<div class=\"tab-content\">"+
                                "<div class=\"tab-pane fade in active\" id=\"home\">"+
                                    "<h4>上传作业</h4>"+
                                    "<input id=\"file\" name=\"file\" type=\"file\" style=\"display:none\">"+
                                    "<div class=\"input-append\">"+
                                        "<input id=\"photoCover\" class=\"input-large\" type=\"text\" style=\"height:30px;\" disabled>"+
                                        "<a class=\"btn\" onclick=\"$(\'input[id=file]\').click();\">Browse</a>"+
                                        "<div class=\"submitJar\">"+
                                        	"<button class=\"btn btn-primary ladda-button\" data-style=\"expand-right\" style=\"min-width:100px\"><span class=\"ladda-label\" style=\"padding:10px 12px;line-height:24px;font-size:18px;\">上传</span></button>"+
                                        "</div>"+
                                    "</div>"+  
                                "</div>"+
                                "<div class=\"tab-pane fade\" id=\"profile\">"+
                                    "<h4>已有作业</h4>"+
                                    "<div class=\"form-group\">"+
                                        "<select class=\"form-control\" id=\"addTaskName\">"+
                                        "</select>"+
                                        "<p class=\"help-block\">请选择作业，若没有请先上传作业后再选择</p>"+
                                    "</div>"+
                                "</div>"+
                            "</div>"+
                        "</div>"+
                    "</div>"+
                    
                    
                "</div>"+
                "<div class=\"form-group\">"+
                    "<label>任务的运行日志HDFS路径</label>"+
                    "<input class=\"form-control\" placeholder=\"HDFS path\" id=\"addTasklog\">"+
                    "<p class=\"help-block\">请填写出完整的地址</p>"+
                "</div>"+
                "<div class=\"form-group\">"+
                    "<label>任务的运行结果路径</label>"+
                    "<input class=\"form-control\" placeholder=\"result path\" id=\"addTaskRes\">"+
                    "<p class=\"help-block\">请填写出完整的地址</p>"+
                "</div>"+
                "<div class=\"form-group\">"+
                    "<label>任务的出错信息路径</label>"+
                    "<input class=\"form-control\" placeholder=\"error path\" id=\"addTaskErr\">"+
                    "<p class=\"help-block\">请填写出完整的地址</p>"+
                "</div>"+
                "<div class=\"submitTask\">"+
                    "<button class=\"btn btn-primary ladda-button\" data-style=\"expand-right\" style=\"min-width:100px\"><span class=\"ladda-label\" style=\"padding:10px 12px;line-height:24px;font-size:18px;\">提交</span></button>"+
                "</div>"+
            "</div>"+
        "</div>"+
        
        "<button style=\"display:none\" data-toggle=\"modal\" data-target=\"#myModal\" id=\"notifyBtn\"></button>"+
        "<div class=\"modal fade\" id=\"myModal\" tabindex=\"-1\" role=\"dialog\" aria-labelledby=\"myModalLabel\" aria-hidden=\"true\">"+
            "<div class=\"modal-dialog\">"+
                "<div class=\"modal-content\">"+
                    "<div class=\"modal-header\">"+
                        "<button type=\"button\" class=\"close\" data-dismiss=\"modal\" aria-hidden=\"true\">&times;</button>"+
                        "<h4 class=\"modal-title\" id=\"myModalLabel\">上传成功</h4>"+
                    "</div>"+
                    "<div class=\"modal-body\">您已成功上传该作业</div>"+
                    "<div class=\"modal-footer\">"+
                        "<button type=\"button\" class=\"btn btn-default\" data-dismiss=\"modal\">Close</button>"+
                    "</div>"+
                "</div>"+
            "</div>"+
        "</div>"+
        "<button style=\"display:none\" data-toggle=\"modal\" data-target=\"#myModal2\" id=\"notifyBtn2\"></button>"+
        "<div class=\"modal fade\" id=\"myModal2\" tabindex=\"-1\" role=\"dialog\" aria-labelledby=\"myModalLabel\" aria-hidden=\"true\">"+
            "<div class=\"modal-dialog\">"+
                "<div class=\"modal-content\">"+
                    "<div class=\"modal-header\">"+
                        "<button type=\"button\" class=\"close\" data-dismiss=\"modal\" aria-hidden=\"true\">&times;</button>"+
                        "<h4 class=\"modal-title\" id=\"myModalLabel\">上传失败！</h4>"+
                    "</div>"+
                    "<div class=\"modal-body\">上传作业失败</div>"+
                    "<div class=\"modal-footer\">"+
                        "<button type=\"button\" class=\"btn btn-default\" data-dismiss=\"modal\">Close</button>"+
                    "</div>"+
                "</div>"+
            "</div>"+
        "</div>";
          
    $("#page-wrapper").append(string);
    $('input[id=file]').change(function() {
        $('#photoCover').val($(this).val());
    });
    loadingT();
}

function showTask(page){
	taskpage = page;
	$("#page-wrapper").empty();
    var string = 
        "<div class=\"row\">"+
            "<div class=\"col-lg-12\">"+
                "<h1 class=\"page-header\">任务列表</h1>"+
            "</div>"+
            "<div class=\"input-group sidebar-search\">"+
	            "<input type=\"text\" class=\"form-control\" placeholder=\"Search...\" id=\"findText\">"+
	            "<span class=\"input-group-btn\">"+
	                "<button class=\"btn btn-default\" type=\"button\" onclick=\"page(14,this)\">"+
	                    "<i class=\"fa fa-search\"></i>"+
	                "</button>"+
	            "</span>"+
	        "</div>"+
        "</div>";
    $.ajax({
		url:ADDRESS+'/taskInfo/TaskInfo_query.action',
		type:'post',
		data:{'page':page},
		error:function(){
			alert("Error：服务器出错！");
			$("#page-wrapper").append(string);
		},
		success:function(data){
			string += "<div class=\"row\">"+
            "<div class=\"col-lg-12\">"+
            "<table class=\"table table-striped\">";
			if(data=="0"){
				string += "<thead><tr><th>任务ID</th><th>任务的运行日志HDFS路径</th><th>任务的运行结果路径</th><th>任务的出错信息路径</th><th>任务创建时间</th><th>操作</th></tr></thead>"+
						"<tbody>"+		
							"<tr><td colspan='6'>空！</td></tr>"+
							"</tbody>"+
						"</table>";
			}
			else if(data=="00"){
				string += "<thead><tr><th>任务ID</th><th>用户名</th><th>任务的运行日志HDFS路径</th><th>任务的运行结果路径</th><th>任务的出错信息路径</th><th>任务创建时间</th><th>操作</th></tr></thead>"+
							"<tbody>"+			
							"<tr><td colspan='7'>空！</td></tr>"+
							"</tbody>"+
						"</table>";
			}
			else{
				var res = data.split(";");
				var pageSum = res[0];
				if(res[1]=="1"){
					string += "<thead><tr><th>任务ID</th><th>用户名</th><th>任务的运行日志HDFS路径</th><th>任务的运行结果路径</th><th>任务的出错信息路径</th><th>任务创建时间</th><th>操作</th></tr></thead>"+
					"<tbody>";
				}else{
					string += "<thead><tr><th>任务ID</th><th>任务的运行日志HDFS路径</th><th>任务的运行结果路径</th><th>任务的出错信息路径</th><th>任务创建时间</th><th>操作</th></tr></thead>"+
					"<tbody>";
				}
				for(var i=2;i<res.length-1;i++){
					var temp = res[i].split(",");
					string += "<tr>";
					for(var j=0;j<temp.length;j++){
						if((res[1]=="1"&&j==2)||(res[1]=="0"&&j==1)||(j==temp.length-1))
							continue;
						else	
							string += "<td>"+temp[j]+"</td>";
					}
                    string += "<td class=\"operate\">"+
                      "<button type=\"button\" class=\"btn btn-danger\" onclick=\"delTask(this)\">删除</button>"+
                    "</td>"+
                  "</tr>";
                    if(res[1]=="1"){
						string += "<tr><td style=\"display:none\">"+temp[0]+"</td><td>作业状态</td><td colspan='5' class=\"processid\"><div class=\"progress progress-striped active\" style=\"margin-bottom:0\">"+
                        	"<div class=\"progress-bar progress-bar-info "+temp[temp.length-1]+"\" role=\"progressbar\" aria-valuenow=\"20\" aria-valuemin=\"0\" aria-valuemax=\"100\" style=\"width: "+temp[2]+"%\">"+temp[2]+"%</div>"+
                        "</div></td>";
                    }else if(res[1]=="0"){
                    	string += "<tr><td style=\"display:none\">"+temp[0]+"</td><td>作业状态</td><td colspan='4' class=\"processid\"><div class=\"progress progress-striped active\" style=\"margin-bottom:0\">"+
	                    	"<div class=\"progress-bar progress-bar-info "+temp[temp.length-1]+"\" role=\"progressbar\" aria-valuenow=\"20\" aria-valuemin=\"0\" aria-valuemax=\"100\" style=\"width: "+temp[1]+"%\">"+temp[1]+"%</div>"+
	                    "</div></td>";
                    }
                    string += "<td class=\"operate\">"+
                    "<button onclick=\"start(this)\" class=\"btn btn-primary ladda-button\" data-style=\"contract-overlay\" style=\"z-index: 1000;\" data-size=\"xs\">运行</button>"+
                	"</td><td style=\"display:none\" class=\"taskid\">"+temp[temp.length-1]+"</td></tr>";
				}
				string += "</tbody>"+
	              "</table>"+
	              "<div class=\"btn-toolbar pageBtn\" role=\"toolbar\" aria-label=\"page\">"+
	                "<div class=\"btn-group\" role=\"group\" aria-label=\"fpage\">";
				if(page==1)
					string += "<button type=\"button\" class=\"btn btn-default disabled\">上一页</button>";
				else
					string += "<button type=\"button\" class=\"btn btn-default\" onclick=\"showTask("+(page-1)+")\">上一页</button>";
				string += "</div>"+
                "<div class=\"btn-group\" role=\"group\" aria-label=\"spage\">";
	            if(pageSum>7){
					if(page<4){
						for(var i=1;i<=5;i++){
							if(i!=page)
								string += "<button type=\"button\" class=\"btn btn-default\" onclick=\"showTask("+i+")\">"+i+"</button>";
							else
								string += "<button type=\"button\" class=\"btn btn-default disabled\">"+i+"</button>";
						}
						string += "<button type=\"button\" class=\"btn btn-default\">……</button>";
						string += "<button type=\"button\" class=\"btn btn-default\" onclick=\"showTask("+pageSum+")\">"+pageSum+"</button>";
					}else if(page>=4&&page<(pageSum-4)){
						for(var i=page-2;i<=page+2;i++){
							if(i!=page)
								string += "<button type=\"button\" class=\"btn btn-default\" onclick=\"showTask("+i+")\">"+i+"</button>";
							else
								string += "<button type=\"button\" class=\"btn btn-default disabled\">"+i+"</button>";
						}
						string += "<button type=\"button\" class=\"btn btn-default\">……</button>";
						string += "<button type=\"button\" class=\"btn btn-default\" onclick=\"showTask("+pageSum+")\">"+pageSum+"</button>";
					}else{
						for(var i=pageSum-6;i<=pageSum;i++){
							if(i!=page)
								string += "<button type=\"button\" class=\"btn btn-default\" onclick=\"showTask("+i+")\">"+i+"</button>";
							else
								string += "<button type=\"button\" class=\"btn btn-default disabled\">"+i+"</button>";
						}
					}
	            }else{
					for(var i=1;i<=pageSum;i++){
						if(i!=page)
							string += "<button type=\"button\" class=\"btn btn-default\" onclick=\"showTask("+i+")\">"+i+"</button>";
						else
							string += "<button type=\"button\" class=\"btn btn-default disabled\">"+i+"</button>";
					}
				}
	            string += "</div>"+
                "<div class=\"btn-group\" role=\"group\" aria-label=\"tpage\">";
	            if(page==pageSum)
	            	string += "<button type=\"button\" class=\"btn btn-default disabled\">下一页</button>";
	            else
	            	string += "<button type=\"button\" class=\"btn btn-default\" onclick=\"showTask("+(page+1)+")\">下一页</button>";
	            string += "</div>"+
	              "</div>"+
		            "</div>"+
		        "</div>"+
		        "<button style=\"display:none\" data-toggle=\"modal\" data-target=\"#myModal5\" id=\"notifyBtn5\"></button>"+
		        "<div class=\"modal fade\" id=\"myModal5\" tabindex=\"-1\" role=\"dialog\" aria-labelledby=\"myModalLabel\" aria-hidden=\"true\">"+
		            "<div class=\"modal-dialog\">"+
		                "<div class=\"modal-content\">"+
		                    "<div class=\"modal-header\">"+
		                        "<button type=\"button\" class=\"close\" data-dismiss=\"modal\" aria-hidden=\"true\">&times;</button>"+
		                        "<h4 class=\"modal-title\" id=\"myModalLabel\">删除成功！</h4>"+
		                    "</div>"+
		                    "<div class=\"modal-footer\">"+
		                        "<button type=\"button\" class=\"btn btn-default\" data-dismiss=\"modal\">Close</button>"+
		                    "</div>"+
		                "</div>"+
		            "</div>"+
		        "</div>"+
		        "<button style=\"display:none\" data-toggle=\"modal\" data-target=\"#myModal6\" id=\"notifyBtn6\"></button>"+
		        "<div class=\"modal fade\" id=\"myModal6\" tabindex=\"-1\" role=\"dialog\" aria-labelledby=\"myModalLabel\" aria-hidden=\"true\">"+
		            "<div class=\"modal-dialog\">"+
		                "<div class=\"modal-content\">"+
		                    "<div class=\"modal-header\">"+
		                        "<button type=\"button\" class=\"close\" data-dismiss=\"modal\" aria-hidden=\"true\">&times;</button>"+
		                        "<h4 class=\"modal-title\" id=\"myModalLabel\">删除失败！</h4>"+
		                    "</div>"+
		                    "<div class=\"modal-footer\">"+
		                        "<button type=\"button\" class=\"btn btn-default\" data-dismiss=\"modal\">Close</button>"+
		                    "</div>"+
		                "</div>"+
		            "</div>"+
		        "</div>";
			}
			$("#page-wrapper").append(string);
			Ladda.bind( '.operate button', {
		        callback: function( instance ) {
		        		startprogress = 0;
			  	        var interval = setInterval( function() {
			  	          if(startprogress === 1){
			  	            instance.stop();
			  	            clearInterval(interval);
			  	          }
			  	        }, 200);
		    	}
		    });
			queryProcess();
		}
	});
}

function delTask(obj){
	var val = $(obj).parent().parent().children('td');
	if(confirm("确定删除?")){
		var url = ADDRESS+"/taskInfo/TaskInfo_delete.action";
		var args = {
			"id" : val.eq(0).html()
		};
		$.get(url, args, function(data) {
			if (data == "1") {
				$(obj).parent().parent().next().remove();
				$(obj).parent().parent().remove();
				$('#notifyBtn5').click();
			}else{
				$('#notifyBtn6').click();
			}
		});
	}
}

function showJob(){
	$.post(ADDRESS+"/taskInfo/TaskInfo_showJobs.action", "", function(data){
		var array = JSON.parse(data);
		$("#addTaskName").empty();
		for(var i in array){
			$("#addTaskName").append("<option value='"+array[i]+"'>"+array[i]+"</option>");
		}
	});
}

function queryProcess(){
    intervalProcess = setInterval(function() {
		var index = "";
		var allProcess = $('.taskid').parent().children('td.taskid');
		for(var i=0;i<allProcess.length;i++){
			if(allProcess.eq(i).html()!=0&&$('.'+allProcess.eq(i).html()).html()!="100%")
				index += allProcess.eq(i).html() + ",";
		}
		if(index==""){
			clearInterval( intervalProcess );
			return;
		}
		$.ajax({
			url:ADDRESS+'/taskInfo/TaskInfo_queryProcess.action',
			type:'post',
			data:{'index':index},
			error:function(){
				alert("Error：服务器出错！");
			},
			success:function(data){
				var bool = true;
				var resProcessData = data.split(',');
				var resProcess = $('.taskid').parent().children('td.processid');
				for(var i=0;i<resProcess.length;i++){
					var temp = resProcessData[i].split(":");
					$('.'+temp[0]).animate({'width': temp[1] + '%'},'slow');
					$('.'+temp[0]).html(temp[1]+"%");
				}
				
			}
		});
    }, 2000);
}

function resourceStatus(){
    $("#page-wrapper").empty();
    var string = 
        "<div class=\"row\">"+
            "<div class=\"col-lg-12\">"+
                "<h1 class=\"page-header\">资源使用情况</h1>"+
            "</div>"+
        "</div>"+
        "<div class=\"row\">"+
        "<div class=\"col-lg-6\">"+
            "<div class=\"panel panel-default\">"+
                "<div class=\"panel-heading\" id=\"hdfsHead\">HDFS使用情况</div>"+
                "<div class=\"panel-body\">"+
                    "<div class=\"flot-chart\">"+
                        "<div class=\"flot-chart-content\" id=\"flot-pie-chart\"></div>"+
                    "</div>"+
                "</div>"+
            "</div>"+
        "</div>"+
        "<div class=\"col-lg-6\">"+
        "<div class=\"panel panel-default\">"+
            "<div class=\"panel-heading\" id=\"appHead\">应用使用情况</div>"+
            "<div class=\"panel-body\">"+
                "<div class=\"flot-chart\">"+
                    "<div class=\"flot-chart-content\" id=\"flot-pie-chart2\"></div>"+
                "</div>"+
            "</div>"+
        "</div>"+
    "</div>"+
    "</div>"+
    "<div class=\"panel panel-default\">"+
    	"<div class=\"panel-heading\">作业使用情况</div>"+
    	"<div class=\"panel-body\">"+
        	"<ul class=\"timeline\">"+
            	"<li>"+
                	"<div class=\"timeline-badge\"><i class=\"fa fa-check\"></i></div>"+
                	"<div class=\"timeline-panel\">"+
                    	"<div class=\"timeline-heading\">"+
                        	"<h4 class=\"timeline-title\">创建作业时间</h4>"+
                    	"</div>"+
	                    "<div class=\"timeline-body\">"+
	                        "<h3 id=\"createJobTime\"></h3>"+
	                    "</div>"+
                    "</div>"+
                "</li>"+
                "<li class=\"timeline-inverted\">"+
	            	"<div class=\"timeline-badge info\"><i class=\"fa fa-rocket\"></i></div>"+
	            	"<div class=\"timeline-panel\">"+
	                	"<div class=\"timeline-heading\">"+
	                    	"<h4 class=\"timeline-title\">使用队列</h4>"+
	                    "</div>"+
	                    "<div class=\"timeline-body\">"+
	                        "<h3 id=\"usedQueue\"></h3>"+
	                    "</div>"+
	                "</div>"+
	            "</li>"+
                "<li>"+
                	"<div class=\"timeline-badge warning\"><i class=\"fa fa-times\"></i></div>"+
                	"<div class=\"timeline-panel\">"+
                    	"<div class=\"timeline-heading\">"+
                        	"<h4 class=\"timeline-title\">启动作业次数</h4>"+
                        "</div>"+
	                    "<div class=\"timeline-body\">"+
	                        "<h3 id=\"usedTimes\"></h3>"+
	                    "</div>"+
                    "</div>"+
                "</li>"+
                "<li class=\"timeline-inverted\">"+
	            	"<div class=\"timeline-badge danger\"><i class=\"fa fa-tags\"></i></div>"+
	            	"<div class=\"timeline-panel\">"+
	                	"<div class=\"timeline-heading\">"+
	                    	"<h4 class=\"timeline-title\">最后一次启动作业的时间</h4>"+
	                    "</div>"+
	                    "<div class=\"timeline-body\">"+
	                        "<h3 id=\"lastUpdateTime\"></h3>"+
	                    "</div>"+
	                "</div>"+
                "</li>"+
            "</ul>"+
        "</div>"+
    "</div>";
	$("#page-wrapper").append(string);
        
     $.ajax({
			url:ADDRESS+'/resourceInfo/ResourceInfo_get.action',
			type:'post',
			data:{},
			error:function(){
				alert("Error：服务器出错！");
			},
			success:function(d){
				if(d=='0'){
					alert("Error：服务器出错！");
					return;
				}
				var val = d.split(",");
				$('#hdfsHead').html("HDFS使用情况[总共"+val[2]+"]");
				var data = [{
			        label: "剩余容量",
			        data: val[1]
			    }, {
			        label: "已用容量",
			        data: val[0]
			    }];
				var plotObj = $.plot($("#flot-pie-chart"), data, {
			        series: {
			            pie: {
			                show: true
			            }
			        },
			        grid: {
			            hoverable: true
			        },
			        tooltip: true,
			        tooltipOpts: {
			            content: "%p.0%, %s", // show percentages, rounding to 2 decimal places
			            shifts: {
			                x: 20,
			                y: 0
			            },
			            defaultTheme: false
			        }
			    });
				$('#appHead').html("应用使用情况[总共"+val[3]+"]");
				var data = [{
			        label: "剩余数量",
			        data: val[5]
			    }, {
			        label: "已用数量",
			        data: val[4]
			    }];
				var plotObj = $.plot($("#flot-pie-chart2"), data, {
			        series: {
			            pie: {
			                show: true
			            }
			        },
			        grid: {
			            hoverable: true
			        },
			        tooltip: true,
			        tooltipOpts: {
			            content: "%p.0%, %s", // show percentages, rounding to 2 decimal places
			            shifts: {
			                x: 20,
			                y: 0
			            },
			            defaultTheme: false
			        }
			    });
				$('#createJobTime').html(val[8]);
				$('#lastUpdateTime').html(val[10]);
				$('#usedQueue').html(val[7]);
				$('#usedTimes').html(val[9]);
			}
		});
}

function resourceQueue(){
    $("#page-wrapper").empty();
    var string = 
        "<div class=\"row\">"+
            "<div class=\"col-lg-12\">"+
                "<h1 class=\"page-header\">资源队列管理</h1>"+
            "</div>"+
        "</div>"+
        "<div class=\"row\">"+
	        "<div class=\"col-lg-6\">"+
	            "<div class=\"panel panel-default\">"+
	                "<div class=\"panel-heading\" id=\"queueCapaticy\">队列容量</div>"+
	                "<div class=\"panel-body\">"+
	                    "<div class=\"flot-chart\">"+
	                        "<div class=\"flot-chart-content\" id=\"flot-pie-chart3\"></div>"+
	                    "</div>"+
	                "</div>"+
	            "</div>"+
	        "</div>"+
	        "<div class=\"col-lg-6\">"+
		        "<div class=\"panel panel-default\">"+
		    	"<div class=\"panel-heading\">队列详情</div>"+
		    	"<div class=\"panel-body\">"+
		        	"<ul class=\"timeline\">"+
		            	"<li>"+
		                	"<div class=\"timeline-badge\"><i class=\"fa fa-check\"></i></div>"+
		                	"<div class=\"timeline-panel\">"+
		                    	"<div class=\"timeline-heading\">"+
		                        	"<h4 class=\"timeline-title\">队列名称</h4>"+
		                    	"</div>"+
			                    "<div class=\"timeline-body\">"+
			                        "<h3 id=\"queueName\"></h3>"+
			                    "</div>"+
		                    "</div>"+
		                "</li>"+
		                "<li class=\"timeline-inverted\">"+
			            	"<div class=\"timeline-badge info\"><i class=\"fa fa-times\"></i></div>"+
			            	"<div class=\"timeline-panel\">"+
			                	"<div class=\"timeline-heading\">"+
			                    	"<h4 class=\"timeline-title\">单容器占用的资源数</h4>"+
			                    "</div>"+
			                    "<div class=\"timeline-body\">"+
			                        "<h3 id=\"queueLimitResource\"></h3>"+
			                    "</div>"+
			                "</div>"+
			            "</li>"+
		            "</ul>"+
	            "</div>"+
            "</div>"+
          "</div>"
	    "</div>";
                
	$("#page-wrapper").append(string);
        
     $.ajax({
			url:ADDRESS+'/queueInfo/QueueInfo_get.action',
			type:'post',
			data:{},
			error:function(){
				alert("Error：服务器出错！");
			},
			success:function(d){
				if(d=='0'){
					alert("Error：服务器出错！");
					return;
				}
				var val = d.split("|");
				$('#queueCapaticy').html("队列容量[总容量"+val[2]+"]");
				$('#queueName').html(val[3]);
				$('#queueLimitResource').html("memory:"+val[4]+"<br/>vcore:"+val[5]);
				var data = [{
			        label: "剩余容量",
			        data: val[0]
			    }, {
			        label: "已用容量",
			        data: val[1]
			    }];
				var plotObj = $.plot($("#flot-pie-chart3"), data, {
			        series: {
			            pie: {
			                show: true
			            }
			        },
			        grid: {
			            hoverable: true
			        },
			        tooltip: true,
			        tooltipOpts: {
			            content: "%p.0%, %s", // show percentages, rounding to 2 decimal places
			            shifts: {
			                x: 20,
			                y: 0
			            },
			            defaultTheme: false
			        }
			    });
			}
		});
}

function findUser(){
	var name = $("#findText").val();
    $("#page-wrapper").empty();
    var string = 
        "<div class=\"row\">"+
            "<div class=\"col-lg-12\">"+
                "<h1 class=\"page-header\">用户列表</h1>"+
            "</div>"+
            "<div class=\"input-group sidebar-search\">"+
            "<input type=\"text\" class=\"form-control\" placeholder=\"Search...\" id=\"findText\">"+
            "<span class=\"input-group-btn\">"+
                "<button class=\"btn btn-default\" type=\"button\" onclick=\"page(13,this)\">"+
                    "<i class=\"fa fa-search\"></i>"+
                "</button>"+
            "</span>"+
            "</div>"+
        "</div>";
    $.ajax({
		url:ADDRESS+'/userInfo/UserInfo_find.action',
		type:'post',
		data:{"name":name},
		error:function(){
			string+="<div>出错！</div>";
			$("#page-wrapper").append(string);
		},
		success:function(data){
			string += "<div class=\"row\">"+
            "<div class=\"col-lg-12\">"+
            "<table class=\"table table-striped\">"+
              "<thead><tr><th>ID</th><th>用户名</th><th>角色</th><th>部门</th><th>创建时间</th><th>操作</th><th></th></tr></thead>"+
              "<tbody>";
			if(data=="0")
				string += "<tr><td>空！</td></tr>"+
					"</tbody>"+
				"</table>";
			else{
				var temp = data.split(",");
				string += "<tr>"+
                "<td>"+temp[0]+"</td>"+
                "<td>"+temp[1]+"</td>"+
                "<td>"+temp[2]+"</td>"+
                "<td>"+temp[3]+"</td>"+
                "<td>"+temp[4]+"</td>"+
                "<td class=\"operate\">"+
                  "<a href=\"####\" onclick=\"page(11,this)\">修改</a>"+
                "</td>"+
                "<td class=\"operate\">"+
                  "<a href=\"####\" onclick=\"del(this)\">删除</a>"+
                "</td>"+
              "</tr>"+
			"</tbody>"+
	              "</table>"+
	              "</div>"+
	              "</div>"+
		        "</div>"+
		        "<button style=\"display:none\" data-toggle=\"modal\" data-target=\"#myModal5\" id=\"notifyBtn5\"></button>"+
		        "<div class=\"modal fade\" id=\"myModal5\" tabindex=\"-1\" role=\"dialog\" aria-labelledby=\"myModalLabel\" aria-hidden=\"true\">"+
		            "<div class=\"modal-dialog\">"+
		                "<div class=\"modal-content\">"+
		                    "<div class=\"modal-header\">"+
		                        "<button type=\"button\" class=\"close\" data-dismiss=\"modal\" aria-hidden=\"true\">&times;</button>"+
		                        "<h4 class=\"modal-title\" id=\"myModalLabel\">删除成功！</h4>"+
		                    "</div>"+
		                    "<div class=\"modal-footer\">"+
		                        "<button type=\"button\" class=\"btn btn-default\" data-dismiss=\"modal\">Close</button>"+
		                    "</div>"+
		                "</div>"+
		            "</div>"+
		        "</div>"+
		        "<button style=\"display:none\" data-toggle=\"modal\" data-target=\"#myModal6\" id=\"notifyBtn6\"></button>"+
		        "<div class=\"modal fade\" id=\"myModal6\" tabindex=\"-1\" role=\"dialog\" aria-labelledby=\"myModalLabel\" aria-hidden=\"true\">"+
		            "<div class=\"modal-dialog\">"+
		                "<div class=\"modal-content\">"+
		                    "<div class=\"modal-header\">"+
		                        "<button type=\"button\" class=\"close\" data-dismiss=\"modal\" aria-hidden=\"true\">&times;</button>"+
		                        "<h4 class=\"modal-title\" id=\"myModalLabel\">删除失败！</h4>"+
		                    "</div>"+
		                    "<div class=\"modal-footer\">"+
		                        "<button type=\"button\" class=\"btn btn-default\" data-dismiss=\"modal\">Close</button>"+
		                    "</div>"+
		                "</div>"+
		            "</div>"+
		        "</div>";
			}
			$("#page-wrapper").append(string);
		}
	});

}

function findTask(){
	var name = $('#findText').val();
	$("#page-wrapper").empty();
    var string = 
        "<div class=\"row\">"+
            "<div class=\"col-lg-12\">"+
                "<h1 class=\"page-header\">任务列表</h1>"+
            "</div>"+
            "<div class=\"input-group sidebar-search\">"+
	            "<input type=\"text\" class=\"form-control\" placeholder=\"Search...\" id=\"findText\">"+
	            "<span class=\"input-group-btn\">"+
	                "<button class=\"btn btn-default\" type=\"button\" onclick=\"page(14,this)\">"+
	                    "<i class=\"fa fa-search\"></i>"+
	                "</button>"+
	            "</span>"+
            "</div>"+
        "</div>";
    $.ajax({
		url:ADDRESS+'/taskInfo/TaskInfo_find.action',
		type:'post',
		data:{'id':name},
		error:function(){
			alert("Error：服务器出错！");
			$("#page-wrapper").append(string);
		},
		success:function(data){
			string += "<div class=\"row\">"+
            "<div class=\"col-lg-12\">"+
            "<table class=\"table table-striped\">";
			if(data=="0"){
				string += "<thead><tr><th>任务ID</th><th>任务状态</th><th>任务的运行日志HDFS路径</th><th>任务的运行结果路径</th><th>任务的出错信息路径</th><th>任务创建时间</th><th>操作</th></tr></thead>"+
						"<tbody>"+		
							"<tr><td>空！</td></tr>"+
							"</tbody>"+
						"</table>";
			}
			else if(data=="00"){
				string += "<thead><tr><th>任务ID</th><th>用户名</th><th>任务状态</th><th>任务的运行日志HDFS路径</th><th>任务的运行结果路径</th><th>任务的出错信息路径</th><th>任务创建时间</th><th>操作</th><th></thead>"+
							"<tbody>"+			
							"<tr><td>空！</td></tr>"+
							"</tbody>"+
						"</table>";
			}
			else{
				var res = data.split(";");
				if(res[0]=="1"){
					string += "<thead><tr><th>任务ID</th><th>用户名</th><th>任务状态</th><th>任务的运行日志HDFS路径</th><th>任务的运行结果路径</th><th>任务的出错信息路径</th><th>任务创建时间</th><th>操作</th></tr></thead>"+
					"<tbody>";
				}else{
					string += "<thead><tr><th>任务ID</th><th>任务状态</th><th>任务的运行日志HDFS路径</th><th>任务的运行结果路径</th><th>任务的出错信息路径</th><th>任务创建时间</th><th>操作</th></tr></thead>"+
					"<tbody>";
				}
				
				var temp = res[1].split(",");
				string += "<tr>";
				for(var j=0;j<temp.length;j++){
					if((res[0]=="1"&&j==2)||(res[0]=="0"&&j==1))
						string += "<td class=\"processid\"><div class=\"progress progress-striped active\" style=\"margin-bottom:0\">"+
		                        	"<div class=\"progress-bar progress-bar-info "+temp[temp.length-1]+"\" role=\"progressbar\" aria-valuenow=\"20\" aria-valuemin=\"0\" aria-valuemax=\"100\" style=\"width: "+temp[j]+"%\">"+temp[j]+"%</div>"+
		                        "</div></td>";
					else if(j==temp.length-1)
						string += "<td style=\"display:none\" class=\"taskid\">"+temp[j]+"</td>";
					else	
						string += "<td>"+temp[j]+"</td>";
				}
                string += "<td class=\"operate\">"+
                  "<a href=\"####\" onclick=\"delTask(this)\">删除</a>"+
                "</td>"+
              "</tr>";
				
				string += "</tbody>"+
	              "</table>"+
	            "</div>"+
	              "</div>"+
		        "<button style=\"display:none\" data-toggle=\"modal\" data-target=\"#myModal5\" id=\"notifyBtn5\"></button>"+
		        "<div class=\"modal fade\" id=\"myModal5\" tabindex=\"-1\" role=\"dialog\" aria-labelledby=\"myModalLabel\" aria-hidden=\"true\">"+
		            "<div class=\"modal-dialog\">"+
		                "<div class=\"modal-content\">"+
		                    "<div class=\"modal-header\">"+
		                        "<button type=\"button\" class=\"close\" data-dismiss=\"modal\" aria-hidden=\"true\">&times;</button>"+
		                        "<h4 class=\"modal-title\" id=\"myModalLabel\">删除成功！</h4>"+
		                    "</div>"+
		                    "<div class=\"modal-footer\">"+
		                        "<button type=\"button\" class=\"btn btn-default\" data-dismiss=\"modal\">Close</button>"+
		                    "</div>"+
		                "</div>"+
		            "</div>"+
		        "</div>"+
		        "<button style=\"display:none\" data-toggle=\"modal\" data-target=\"#myModal6\" id=\"notifyBtn6\"></button>"+
		        "<div class=\"modal fade\" id=\"myModal6\" tabindex=\"-1\" role=\"dialog\" aria-labelledby=\"myModalLabel\" aria-hidden=\"true\">"+
		            "<div class=\"modal-dialog\">"+
		                "<div class=\"modal-content\">"+
		                    "<div class=\"modal-header\">"+
		                        "<button type=\"button\" class=\"close\" data-dismiss=\"modal\" aria-hidden=\"true\">&times;</button>"+
		                        "<h4 class=\"modal-title\" id=\"myModalLabel\">删除失败！</h4>"+
		                    "</div>"+
		                    "<div class=\"modal-footer\">"+
		                        "<button type=\"button\" class=\"btn btn-default\" data-dismiss=\"modal\">Close</button>"+
		                    "</div>"+
		                "</div>"+
		            "</div>"+
		        "</div>";
			}
			$("#page-wrapper").append(string);
			queryProcess();
		}
	});
}

function start(that){
	var val = $(that).parent().parent().children('td').eq(0).html();
	var tempPro = $(that).parent().parent().children('td').eq(2).children('.progress').children('.progress-bar').html();
	console.log(tempPro);
	if(tempPro!="100%"&&tempPro!="0%"){
		startprogress = 1;
		return;
	}
	$.ajax({
		url:ADDRESS+'/taskInfo/TaskInfo_start.action',
		type:'post',
		data:{'id':val},
		error:function(){
			alert("Error：服务器出错！");
			$("#page-wrapper").append(string);
		},
		success:function(data){
			if(data==1){
				startprogress = 1;
				showTask(taskpage);
			}else{
				alert("启动失败");
			}
		}
	});
}
