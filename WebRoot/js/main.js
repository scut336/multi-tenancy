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
            applyRes();
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
            findTask("");
            taskpage = 1;
            break;
        case 15:
            clearInterval( intervalProcess );
            taskpage = 1;
            manageApply(1);
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
        "</div>"+
        "<div class=\"row\">"+
        	"<div class=\"col-lg-6 col-md-6\">"+
	        	"<div class=\"panel panel-primary\">"+
			        "<div class=\"panel-heading\"><i class=\"fa fa-th fa-fw\"></i> 集群信息</div>"+
			        "<div class=\"panel-body\">"+
				        "<div class=\"table-responsive\">"+
			            	"<table class=\"table table-bordered clusterField\">"+
			                "</table>"+
			            "</div>"+
			        "</div>"+
			    "</div>"+
        	"</div>"+
	        "<div class=\"col-lg-6 col-md-6\">"+
		        "<div class=\"col-lg-6 col-md-6\">"+
		            "<div class=\"panel panel-primary\">"+
		                "<div class=\"panel-heading\">"+
		                    "<div class=\"row\">"+
		                        "<div class=\"col-xs-3\">"+
		                            "<i class=\"fa fa-user fa-5x\"></i>"+
		                        "</div>"+
		                        "<div class=\"col-xs-9 text-right\">"+
		                            "<div class=\"huge\" id='userSum'></div>"+
		                            "<div>用户总数</div>"+
		                        "</div>"+
		                    "</div>"+
		                "</div>"+
		                "<a href=\"####\" onclick='page(3,this)'>"+
		                    "<div class=\"panel-footer\">"+
		                        "<span class=\"pull-left\">View Details</span>"+
		                        "<span class=\"pull-right\"><i class=\"fa fa-arrow-circle-right\"></i></span>"+
		                        "<div class=\"clearfix\"></div>"+
		                    "</div>"+
		                "</a>"+
		            "</div>"+
		        "</div>"+
		        "<div class=\"col-lg-6 col-md-6\">"+
		    	    "<div class=\"panel panel-green\">"+
		    	        "<div class=\"panel-heading\">"+
		    	            "<div class=\"row\">"+
		    	                "<div class=\"col-xs-3\">"+
		    	                    "<i class=\"fa fa-bar-chart-o fa-5x\"></i>"+
		    	                "</div>"+
		    	                "<div class=\"col-xs-9 text-right\">"+
		    	                    "<div class=\"huge\" id='taskSum'></div>"+
		    	                    "<div>任务总数</div>"+
		    	                "</div>"+
		    	            "</div>"+
		    	        "</div>"+
		    	        "<a href=\"####\" onclick='page(5,this)'>"+
		    	            "<div class=\"panel-footer\">"+
		    	                "<span class=\"pull-left\">View Details</span>"+
		    	                "<span class=\"pull-right\"><i class=\"fa fa-arrow-circle-right\"></i></span>"+
		    	                "<div class=\"clearfix\"></div>"+
		    	            "</div>"+
		    	        "</a>"+
		    	    "</div>"+
		    	"</div>"+
		    	"<div class=\"col-lg-6 col-md-6\">"+
		    	    "<div class=\"panel panel-yellow\">"+
		    	        "<div class=\"panel-heading\">"+
		    	            "<div class=\"row\">"+
		    	                "<div class=\"col-xs-3\">"+
		    	                    "<i class=\"fa fa-random fa-5x\"></i>"+
		    	                "</div>"+
		    	                "<div class=\"col-xs-9 text-right\">"+
		    	                    "<div class=\"huge\" id='queueSum'>26</div>"+
		    	                    "<div>队列总数</div>"+
		    	                "</div>"+
		    	            "</div>"+
		    	        "</div>"+
		    	        "<a href=\"####\" onclick='page(8,this)'>"+
		    	            "<div class=\"panel-footer\">"+
		    	                "<span class=\"pull-left\">View Details</span>"+
		    	                "<span class=\"pull-right\"><i class=\"fa fa-arrow-circle-right\"></i></span>"+
		    	                "<div class=\"clearfix\"></div>"+
		    	            "</div>"+
		    	        "</a>"+
		    	    "</div>"+
	    	    "</div>"+
	    	    "<div class=\"col-lg-6 col-md-6\">"+
		    	    "<div class=\"panel panel-red\">"+
		    	        "<div class=\"panel-heading\">"+
		    	            "<div class=\"row\">"+
		    	                "<div class=\"col-xs-3\">"+
		    	                    "<i class=\"fa fa-magnet fa-5x\"></i>"+
		    	                "</div>"+
		    	                "<div class=\"col-xs-9 text-right\">"+
		    	                    "<div class=\"huge\" id='manageSum'>26</div>"+
		    	                    "<div>待审核事项</div>"+
		    	                "</div>"+
		    	            "</div>"+
		    	        "</div>"+
		    	        "<a href=\"####\" onclick='page(15,this)'>"+
		    	            "<div class=\"panel-footer\">"+
		    	                "<span class=\"pull-left\">View Details</span>"+
		    	                "<span class=\"pull-right\"><i class=\"fa fa-arrow-circle-right\"></i></span>"+
		    	                "<div class=\"clearfix\"></div>"+
		    	            "</div>"+
		    	        "</a>"+
		    	    "</div>"+
		    	"</div>"+
	    	"</div>"+
    	"</div>"+
    	"<div class=\"row indexGroup\">"+
	        "<div class=\"col-lg-3\">"+
	            "<div class=\"panel panel-primary\">"+
	                "<div class=\"panel-heading\" id=\"hdfsHead\"><i class=\"fa fa-bullseye fa-fw\"></i> CPU使用情况</div>"+
	                "<div class=\"panel-body\">"+
		                "<a href='#cpuHealth'><span class=\"Homechart\" id='Homechart1' data-percent=\"12\">"+
			        		"<span class=\"Homepercent\"></span>"+
			        	"</span></a>"+
	                "</div>"+
	            "</div>"+
	        "</div>"+
	        "<div class=\"col-lg-3\">"+
	            "<div class=\"panel panel-green\">"+
	                "<div class=\"panel-heading\" id=\"hdfsHead\"><i class=\"fa fa-rocket fa-fw\"></i> Memory使用情况</div>"+
	                "<div class=\"panel-body\">"+
		                "<a href='#memoryHealth'><span class=\"Homechart\" id='Homechart2' data-percent=\"12\">"+
			        		"<span class=\"Homepercent\"></span>"+
			        	"</span></a>"+
	                "</div>"+
	            "</div>"+
	        "</div>"+
	        "<div class=\"col-lg-3\">"+
	            "<div class=\"panel panel-yellow\">"+
	                "<div class=\"panel-heading\" id=\"hdfsHead\"><i class=\"fa fa-sitemap fa-fw\"></i> 节点使用情况</div>"+
	                "<div class=\"panel-body\">"+
		                "<a href='#nodeHealth'><span class=\"Homechart\" id='Homechart3' data-percent=\"12\">"+
			        		"<span class=\"Homepercent\"></span>"+
			        	"</span></a>"+
	                "</div>"+
	            "</div>"+
	        "</div>"+
	        "<div class=\"col-lg-3\">"+
	            "<div class=\"panel panel-red\">"+
	                "<div class=\"panel-heading\" id=\"hdfsHead\"><i class=\"fa fa-square fa-fw\"></i> APP情况</div>"+
	                "<div class=\"panel-body\">"+
		                "<a href='#appHealth'><span class=\"Homechart\" id='Homechart4' data-percent=\"12\">"+
			        		"<span class=\"Homepercent\"></span>"+
			        	"</span></a>"+
	                "</div>"+
	            "</div>"+
	        "</div>"+
        "</div>"+
        "<div class=\"row\">" +
	    	"<div class=\"col-lg-6\">"+
	    		"<div class=\"panel panel-primary\" id='cpuHealth'>"+
			        "<div class=\"panel-heading\"><i class=\"fa fa-desktop fa-fw\"></i> CPU信息</div>"+
			        "<div class=\"panel-body\">"+
				        "<div class=\"table-responsive\">"+
			            	"<table class=\"table table-striped table-bordered table-hover\">"+
			                "<thead><tr class='info'><th>Available(个)</th><th>Allocated(个)</th><th>Reserved(个)</th></tr></thead>"+
			                "<tbody class='cpuHealth'></tbody>"+
			                "</table>"+
			            "</div>"+
			        "</div>"+
			    "</div>"+
		    "</div>"+
	    	"<div class=\"col-lg-6\">"+
	    		"<div class=\"panel panel-green\" id='memoryHealth'>"+
			        "<div class=\"panel-heading\"><i class=\"fa fa-desktop fa-fw\"></i> Memory信息</div>"+
			        "<div class=\"panel-body\">"+
				        "<div class=\"table-responsive\">"+
			            	"<table class=\"table table-striped table-bordered table-hover\">"+
			                "<thead><tr class='success'><th>Available(M)</th><th>Allocated(M)</th><th>Reserved(M)</th></thead>"+
			                "<tbody class='memoryHealth'></tbody>"+
			                "</table>"+
			            "</div>"+
			        "</div>"+
			    "</div>"+
		    "</div>"+
		"</div>"+
        "<div class=\"row\" id='nodeHealth'>" +
        	"<div class=\"col-lg-12\">"+
        		"<div class=\"panel panel-yellow\">"+
			        "<div class=\"panel-heading\"><i class=\"fa fa-desktop fa-fw\"></i> 节点信息</div>"+
			        "<div class=\"panel-body\">"+
				        "<div class=\"table-responsive\">"+
			            	"<table class=\"table table-striped table-bordered table-hover\">"+
			                "<thead><tr class='warning'><th>name</th><th>state</th><th>version</th><th>LastHealthUpdate</th><th>usedMemory(M)</th><th>availMemory(M)</th><th>usedVirtualCores(个)</th><th>availableVirtualCores(个)</th></tr></thead>"+
			                "<tbody class='nodeHealth'></tbody>"+
			                "</table>"+
			            "</div>"+
			        "</div>"+
			    "</div>"+
		    "</div>"+
		"</div>"+
		"<div class=\"row\" id='appHealth'>" +
	    	"<div class=\"col-lg-12\">"+
	    		"<div class=\"panel panel-red\">"+
			        "<div class=\"panel-heading\"><i class=\"fa fa-desktop fa-fw\"></i> APP信息</div>"+
			        "<div class=\"panel-body\">"+
				        "<div class=\"table-responsive\">"+
			            	"<table class=\"table table-striped table-bordered table-hover\">"+
			                "<thead><tr class='danger'><th>Submitted(个)</th><th>Completed(个)</th><th>Pending(个)</th><th>Running(个)</th><th>Failed(个)</th><th>Killed(个)</th></tr></thead>"+
			                "<tbody class='appHealth'></tbody>"+
			                "</table>"+
			            "</div>"+
			        "</div>"+
			    "</div>"+
		    "</div>"+
		"</div>";
	       
     $("#page-wrapper").append(string);
     $.ajax({
  		url:ADDRESS+'/cluster/Cluster_getSum.action',
  		type:'post',
  		data:{},
  		error:function(){
  			alert("服务器出错！");
  		},
  		success:function(data){
  			var res = data.split(",");
  			$('#userSum').html(res[0]);
  			$('#taskSum').html(res[1]);
  			$('#queueSum').html(res[2]);
  			$('#manageSum').html(res[3]);
  		}
      });
     $.ajax({
 		url:ADDRESS+'/cluster/Cluster_getCluster.action',
 		type:'post',
 		data:{},
 		error:function(){
 			alert("服务器出错！");
 		},
 		success:function(data){
 			var res = data.split(",");
 			var str = "<tbody><tr class='colorBlue'><td>开始时间</td><td>"+res[0]+"</td></tr><tr><td>状态</td><td>"+res[1]+"</td></tr><tr class='colorBlue'><td>ResourceManager</td><td>"+res[2]+"</td></tr><tr><td>Hadoop版本</td><td>"+res[3]+"</td></tr></tbody>";
 			$('.clusterField').append(str);
 		}
     });
     $.ajax({
  		url:ADDRESS+'/cluster/Cluster_getMetrics.action',
  		type:'post',
  		data:{},
  		error:function(){
  			alert("服务器出错！");
  		},
  		success:function(data){
  			var res = data.split(",");
  			var str1 = parseInt(res[9])+parseInt(res[10])+parseInt(res[11]);
  			var str2 = parseInt(res[6])+parseInt(res[7])+parseInt(res[8]);
  			var str3 = parseInt(res[12])+parseInt(res[13])+parseInt(res[14])+parseInt(res[15])+parseInt(res[16]);
  			$('#Homechart1').attr('data-percent',parseInt(res[9])+parseInt(res[11])/str1*100);
  			$('#Homechart1').children('span').html(str1+"个");
  			$('#Homechart2').attr('data-percent',parseInt(res[6])+parseInt(res[8])/str2*100);
  			$('#Homechart2').children('span').html(str2/1024+"G");
  			$('#Homechart3').attr('data-percent',parseInt(res[16])/str3*100);
  			$('#Homechart3').children('span').html(str3+"个");
  			$('#Homechart4').attr('data-percent',parseInt(res[1])/parseInt(res[0])*100);
  			$('#Homechart4').children('span').html(parseInt(res[0])+"个");
  			var str5 = "<tr><td>"+res[10]+"</td><td>"+res[11]+"</td><td>"+res[9]+"</td><tr>";
  			$('.cpuHealth').append(str5);
  			var str6 = "<tr><td>"+res[7]+"</td><td>"+res[8]+"</td><td>"+res[6]+"</td><tr>";
  			$('.memoryHealth').append(str6);
  			var str7 = "<tr><td>"+res[0]+"</td><td>"+res[1]+"</td><td>"+res[2]+"</td><td>"+res[3]+"</td><td>"+res[4]+"</td><td>"+res[5]+"</td><tr>";
  			$('.appHealth').append(str7);
  			
  			window.chart = new EasyPieChart(document.querySelector('#Homechart1'), {
  				easing: 'easeOutElastic',
  				delay: 3000,
  				barColor: '#5093CE',
  				trackColor: '#78ACD9',
  				scaleColor: false,
  				lineWidth: 20,
  				trackWidth: 16,
  				lineCap: 'butt',
  			});
  		     window.chart = new EasyPieChart(document.querySelector('#Homechart2'), {
  		 		easing: 'easeOutElastic',
  		 		delay: 3000,
  		 		barColor: '#6EBF6E',
  		 		trackColor: '#A3D7A3',
  		 		scaleColor: false,
  		 		lineWidth: 20,
  		 		trackWidth: 16,
  		 		lineCap: 'butt',
  		 	});
  		     window.chart = new EasyPieChart(document.querySelector('#Homechart3'), {
  		 		easing: 'easeOutElastic',
  		 		delay: 3000,
  		 		barColor: '#F4C27B',
  		 		trackColor: '#F8D8AA',
  		 		scaleColor: false,
  		 		lineWidth: 20,
  		 		trackWidth: 16,
  		 		lineCap: 'butt',
  		 	});
  		     window.chart = new EasyPieChart(document.querySelector('#Homechart4'), {
  		 		easing: 'easeOutElastic',
  		 		delay: 3000,
  		 		barColor: '#E27D79',
  		 		trackColor: '#EBA5A3',
  		 		scaleColor: false,
  		 		lineWidth: 20,
  		 		trackWidth: 16,
  		 		lineCap: 'butt',
  		 	});
  		}
      });
     $.ajax({
  		url:ADDRESS+'/cluster/Cluster_getNodes.action',
  		type:'post',
  		data:{},
  		error:function(){
  			alert("服务器出错！");
  		},
  		success:function(data){
  			var arr = data.split(";");
  			var str = "";
  			for(var i=0;i<arr.length-1;i++){
  				var res = arr[i].split(",");
  				str += "<tr><td>"+res[0]+"</td><td>"+res[1]+"</td><td>"+res[2]+"</td><td>"+res[3]+"</td><td>"+res[4]+"</td>"+
  				"<td>"+res[5]+"</td><td>"+res[6]+"</td><td>"+res[7]+"</td></tr>";
  			}
  			$('.nodeHealth').append(str);
  		}
      });
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
                    "<label>用户ID<span style='color:red'>*</span></label>"+
                    "<input class=\"form-control\" placeholder=\"userID\" id=\"addID\">"+
                    "<p class=\"help-block\">5个字符，可使用字母、数字，需以字母开头</p>"+
                "</div>"+
                "<div class=\"form-group\">"+
                    "<label>用户名<span style='color:red'>*</span></label>"+
                    "<input class=\"form-control\" placeholder=\"userName\" id=\"addName\">"+
                    "<p class=\"help-block\">6~18个字符，可使用字母、数字、下划线，需以字母开头</p>"+
                "</div>"+
                "<div class=\"form-group\">"+
                    "<label>密码<span style='color:red'>*</span></label>"+
                    "<input class=\"form-control\" type=\"password\" placeholder=\"password\" id=\"addPassword\">"+
                    "<p class=\"help-block\">6~16个字符，区分大小写</p>"+
                "</div>"+
                "<div class=\"form-group\">"+
                    "<label>部门<span style='color:red'>*</span></label>"+
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
                "<label>类型<span style='color:red'>*</span></label>"+
                "<div class=\"form-group\">"+
                    "<select class=\"form-control\" id=\"addRole\">"+
                        "<option>user</option>"+
                        "<option>vip1</option>"+
                        "<option>vip2</option>"+
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
	var boolName = true;
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
	        if(boolName&&$("#updatePassword").val()!=""&&$("#updateDepartment").val()!=""&&$("#updateRole").val()!=""){
	        $.ajax({
	    		url:ADDRESS+'/userInfo/UserInfo_save.action',
	    		type:'post',
	    		data:{	"id" : $("#updateID").val(),
	    				"name" : $("#updateName").val(),
						"department": $("#updateDepartment option:selected").val(),
						"role":$("#updateRole option:selected").val()},
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
                "<div class=\"form-group\">"+
                "<label>类型</label>"+
                "<div class=\"form-group\">"+
                    "<select class=\"form-control\" id=\"updateRole\">"+
                        "<option>user</option>"+
                        "<option>vip1</option>"+
                        "<option>vip2</option>"+
                    "</select>"+
                "</div>"+
                "<p class=\"help-block\">请选择其中一种类型</p>"+
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
    $('#updateRole').val(temp.eq(2).html());
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
                    "<label>任务ID<span style='color:red'>*</span></label>"+
                    "<input class=\"form-control\" placeholder=\"taskID\" id=\"addTaskID\">"+
                    "<p class=\"help-block\">5个字符，可使用字母、数字，需以字母开头</p>"+
                "</div>"+
                "<div class=\"form-group\">"+"<label>选择作业<span style='color:red'>*</span></label>"+
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
                    "<label>任务的运行输入路径<span style='color:red'>*</span></label>"+
                    "<input class=\"form-control\" placeholder=\"HDFS path\" id=\"addTasklog\">"+
                    "<p class=\"help-block\">请填写出完整的地址</p>"+
                "</div>"+
                "<div class=\"form-group\">"+
                    "<label>任务的运行结果路径</label>"+
                    "<input class=\"form-control\" placeholder=\"result path\" id=\"addTaskRes\" disabled value='/user/smoketest/output'>"+
                    "<p class=\"help-block\">请填写出完整的地址</p>"+
                "</div>"+
                "<div class=\"form-group\">"+
                    "<label>任务的出错信息路径</label>"+
                    "<input class=\"form-control\" placeholder=\"error path\" id=\"addTaskErr\" disabled value='/user/smoketest/output'>"+
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
				string += "<thead><tr><th>任务ID</th><th>任务的运行输入路径</th><th>任务的运行结果路径</th><th>任务的出错信息路径</th><th>任务创建时间</th><th>操作</th><th></th></tr></thead>"+
						"<tbody>"+		
							"<tr><td colspan='7'>空！</td></tr>"+
							"</tbody>"+
						"</table>";
			}
			else if(data=="00"){
				string += "<thead><tr><th>任务ID</th><th>用户名</th><th>任务的运行输入路径</th><th>任务的运行结果路径</th><th>任务的出错信息路径</th><th>任务创建时间</th><th>操作</th><th></th></tr></thead>"+
							"<tbody>"+			
							"<tr><td colspan='8'>空！</td></tr>"+
							"</tbody>"+
						"</table>";
			}
			else{
				var res = data.split(";");
				var pageSum = res[0];
				if(res[1]=="1"){
					string += "<thead><tr><th>任务ID</th><th>用户名</th><th>任务的运行输入路径</th><th>任务的运行结果路径</th><th>任务的出错信息路径</th><th>任务创建时间</th><th>操作</th><th></th></tr></thead>"+
					"<tbody>";
				}else{
					string += "<thead><tr><th>任务ID</th><th>任务的运行输入路径</th><th>任务的运行结果路径</th><th>任务的出错信息路径</th><th>任务创建时间</th><th>操作</th><th></th></tr></thead>"+
					"<tbody>";
				}
				for(var i=2;i<res.length-1;i++){
					var temp = res[i].split(",");
					string += "<tr>";
					for(var j=0;j<temp.length;j++){
						if((res[1]=="1"&&j==2)||(res[1]=="0"&&j==1)||(j==temp.length-1))
							continue;
						else if((res[1]=="1"&&j==3)||(res[1]=="0"&&j==2))
							string += "<td><a href='####' onclick='updateRoute(this)' id='"+temp[0]+"'>"+temp[j]+"</a></td>";
						else
							string += "<td>"+temp[j]+"</td>";
					}
                    string += "<td class=\"operate\">"+
                      "<button type=\"button\" class=\"btn btn-danger\" onclick=\"delTask(this)\">删除</button>"+
                    "</td><td></td>"+
                  "</tr>";
                    if(res[1]=="1"){
						string += "<tr><td>作业状态</td><td colspan='5' class=\"processid\"><div class=\"progress progress-striped active\" style=\"margin-bottom:0\">"+
                        	"<div class=\"progress-bar progress-bar-info "+temp[temp.length-1]+"\" role=\"progressbar\" aria-valuenow=\"20\" aria-valuemin=\"0\" aria-valuemax=\"100\" style=\"width: "+temp[2]+"%\">"+temp[2]+"%</div>"+
                        "</div></td>";
                    }else if(res[1]=="0"){
                    	string += "<tr><td>作业状态</td><td colspan='4' class=\"processid\"><div class=\"progress progress-striped active\" style=\"margin-bottom:0\">"+
	                    	"<div class=\"progress-bar progress-bar-info "+temp[temp.length-1]+"\" role=\"progressbar\" aria-valuenow=\"20\" aria-valuemin=\"0\" aria-valuemax=\"100\" style=\"width: "+temp[1]+"%\">"+temp[1]+"%</div>"+
	                    "</div></td>";
                    }
                    string += "<td class=\"operate\">"+
                    "<button onclick=\"start(this,true)\" class=\"btn btn-primary ladda-button\" data-style=\"contract-overlay\" style=\"z-index: 1000;\" data-size=\"xs\">运行</button>"+
                	"</td>"+
                	"<td class=\"operate\">"+
                	"<button onclick=\"download(this)\" class=\"btn btn-primary\">结果下载</button>"+
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
		        "</div>"+
		        "<div class=\"modal fade\" id=\"myModal7\" tabindex=\"-1\" role=\"dialog\" aria-labelledby=\"myModalLabel\" aria-hidden=\"true\">"+
		            "<div class=\"modal-dialog\">"+
		                "<div class=\"modal-content\">"+
		                    "<div class=\"modal-header\">"+
		                        "<button type=\"button\" class=\"close\" data-dismiss=\"modal\" aria-hidden=\"true\">&times;</button>"+
		                        "<h4 class=\"modal-title\" id=\"myModalLabel\">修改HDFS路径</h4>"+
		                    "</div>"+
		                    "<div class=\"modal-body\">"+
			                    "<div class=\"form-group\">"+
				                    "<label>HDFS路径</label>"+
				                    "<input class=\"form-control\" placeholder=\"hdfsroute\" id=\"updateHDFSRoute\">"+
				                    "<div id='updateHDFSname' style='display:none'></div>"+
			                    "</div>"+
		                    "</div>"+
		                    "<div class=\"modal-footer\">"+
		                        "<button type=\"button\" class=\"btn btn-default\" data-dismiss=\"modal\" onclick='updateHDFS()'>更改</button>"+
		                        "<button type=\"button\" class=\"btn btn-default\" data-dismiss=\"modal\">关闭</button>"+
		                    "</div>"+
		                "</div>"+
		            "</div>"+
		        "</div>"+
		        "<div class=\"modal fade\" id=\"myModal8\" tabindex=\"-1\" role=\"dialog\" aria-labelledby=\"myModalLabel\" aria-hidden=\"true\">"+
		            "<div class=\"modal-dialog\">"+
		                "<div class=\"modal-content\">"+
		                    "<div class=\"modal-header\">"+
		                        "<button type=\"button\" class=\"close\" data-dismiss=\"modal\" aria-hidden=\"true\">&times;</button>"+
		                        "<h4 class=\"modal-title\" id=\"myModalLabel\">结果下载</h4>"+
		                    "</div>"+
		                    "<div class=\"modal-body\" id='download'>"+
		                    "</div>"+
		                    "<div class=\"modal-footer\">"+
		                        "<button type=\"button\" class=\"btn btn-default\" data-dismiss=\"modal\">关闭</button>"+
		                    "</div>"+
		                "</div>"+
		            "</div>"+
		        "</div>";
			}
			$("#page-wrapper").append(string);
			Ladda.bind( '.operate .ladda-button', {
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

function download(that){
	var id = $(that).parent().parent().prev().children('td').eq(0).html();
	$.ajax({
  		url:ADDRESS+'/resourceInfo/ResourceInfo_ResultRoute.action',
  		type:'post',
  		data:{'id':id},
  		error:function(){
  			alert("Error：服务器出错！");
  		},
  		success:function(data){
  			var arr = data.split("|");
  			var res = "";
  			for(var i=0;i<arr.length-1;i++){
  				var temp = arr[i].split(",");
  				res += "<li><a href='"+temp[1]+"'>"+temp[0]+"</a></li>";
  			}
  			$('#download').empty();
  			$('#download').append("<ul>"+res+"</ul>");
  			$('#myModal8').modal('show');
  		}
  	});
	
}

function updateRoute(that){
	$('#updateHDFSRoute').val($(that).html());
	$('#updateHDFSname').html($(that).parent().parent().children('td').eq(0).html());
	$('#myModal7').modal('show');
}

function updateHDFS(){
	var tempid = $('#updateHDFSname').html();
	var temphdfs = $('#updateHDFSRoute').val();
	$.ajax({
		url:ADDRESS+"/taskInfo/TaskInfo_UpdateHDFS.action",
		type:'post',
		data:{'id':tempid,
			  'hdfs':temphdfs},
		error:function(){
			alert("Error：服务器出错！");
		},
		success:function(d){
			$('#'+tempid).html(temphdfs);
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
					if(parseInt(temp[1])<=100){
						$('.'+temp[0]).animate({'width': parseInt(temp[1]) + '%'},'slow');
						$('.'+temp[0]).html(parseInt(temp[1])+"%");
					}
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
	            "<div class=\"panel-heading\">HDFS路径</div>"+
	            "<div class=\"panel-body\">"+
		            "<h4>文件路径</h4>"+
		            "<ul class='hdfsroute'>"+
		            	"<li class='0' onclick='expand(this)'>Home</li>"+
		            "</ul>"+    
	            "</div>"+
	        "</div>"+
	    "</div>"+
    "</div>"+
    "<div class=\"row\">"+
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
    "<div class=\"col-lg-6\">"+
	    "<div class=\"panel panel-default\">"+
		    "<div class=\"panel-heading\">已上传应用</div>"+
		    "<div class=\"panel-body Allapp\"></div>"+
	    "</div>"+
	"</div>"+
    "</div>"+
    "<div class=\"row\">"+
	    "<div class=\"col-lg-12\">"+
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
			                        "<h4 id=\"createJobTime\"></h4>"+
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
			                        "<h4 id=\"usedQueue\"></h4>"+
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
			                        "<h4 id=\"usedTimes\"></h4>"+
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
			                        "<h4 id=\"lastUpdateTime\"></h4>"+
			                    "</div>"+
			                "</div>"+
		                "</li>"+
		            "</ul>"+
		        "</div>"+
		    "</div>"+
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
				$('#hdfsHead').html("HDFS使用情况[总共"+(val[2]/1048576).toFixed(2)+"M]");
				var outrange = "";
				if(val[1]<0){
					outrange = -val[1];
					val[1] = "";
				}
				var data = [{
			        label: "剩余容量",
			        data: val[1]
			    }, {
			        label: "已用容量",
			        data: val[0]
			    }, {
			        label: "超出容量",
			        data: outrange
			    }];
				$.plot($("#flot-pie-chart"), data, {
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
				$.plot($("#flot-pie-chart2"), data, {
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
				$('#usedQueue').html("队列名称:"+val[7]+"<br/>容量百分比:"+val[11]+"<br/>最大容量百分比:"+val[12]+"<br/>已经使用容量百分比:"+val[13]+"<br/>队列单元占用大小:"+val[14]+"<br/>队列单元CPU值:"+val[15]);
				$('#usedTimes').html(val[9]);
			}
		});
     $.ajax({
 		url:ADDRESS+'/resourceInfo/ResourceInfo_Job.action',
 		type:'post',
 		data:{},
 		error:function(){
 			alert("Error：服务器出错！");
 		},
 		success:function(data){
 			var arr = data.split("|");
 			var res = "";
 			for(var i=0;i<arr.length-1;i++){
 				var temp = arr[i].split(".");
 				res+="<div class=\"well col-lg-4\" onclick='jumptoTask(\""+temp[0]+"\",1)'><h4>"+temp[0]+"</h4></div>";
 			}
 			$(".Allapp").append(res);
 		}
 	});
}

function jumptoTask(that,page){
	var jar = that+".jar";
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
		url:ADDRESS+'/taskInfo/TaskInfo_findJar.action',
		type:'post',
		data:{'page':page,'jar':jar},
		error:function(){
			alert("Error：服务器出错！");
			$("#page-wrapper").append(string);
		},
		success:function(data){
			string += "<div class=\"row\">"+
            "<div class=\"col-lg-12\">"+
            "<table class=\"table table-striped\">";
			if(data=="0"){
				string += "<thead><tr><th>任务ID</th><th>任务的运行输入路径</th><th>任务的运行结果路径</th><th>任务的出错信息路径</th><th>任务创建时间</th><th>操作</th><th></th></tr></thead>"+
						"<tbody>"+		
							"<tr><td colspan='6'>空！</td></tr>"+
							"</tbody>"+
						"</table>";
			}
			else{
				var res = data.split(";");
				var pageSum = res[0];
				string += "<thead><tr><th>任务ID</th><th>任务的运行输入路径</th><th>任务的运行结果路径</th><th>任务的出错信息路径</th><th>任务创建时间</th><th>操作</th><th></th></tr></thead>"+
					"<tbody>";
				for(var i=1;i<res.length-1;i++){
					var temp = res[i].split(",");
					string += "<tr>";
					for(var j=0;j<temp.length;j++){
						if(j==1||j==temp.length-1)
							continue;
						else if(j==2)
							string += "<td><a href='####' onclick='updateRoute(this)' id='"+temp[0]+"'>"+temp[j]+"</a></td>";
						else
							string += "<td>"+temp[j]+"</td>";
					}
                    string += "<td class=\"operate\">"+
                      "<button type=\"button\" class=\"btn btn-danger\" onclick=\"delTask(this)\">删除</button>"+
                    "</td><td></td>"+
                  "</tr>";
                    string += "<tr><td>作业状态</td><td colspan='4' class=\"processid\"><div class=\"progress progress-striped active\" style=\"margin-bottom:0\">"+
	                    	"<div class=\"progress-bar progress-bar-info "+temp[temp.length-1]+"\" role=\"progressbar\" aria-valuenow=\"20\" aria-valuemin=\"0\" aria-valuemax=\"100\" style=\"width: "+temp[1]+"%\">"+temp[1]+"%</div>"+
	                    "</div></td>"+
	                    "<td class=\"operate\">"+
                    "<button onclick=\"start(this,true)\" class=\"btn btn-primary ladda-button\" data-style=\"contract-overlay\" style=\"z-index: 1000;\" data-size=\"xs\">运行</button>"+
                	"</td>"+
                	"<td class=\"operate\">"+
                	"<button onclick=\"download(this)\" class=\"btn btn-primary\">结果下载</button>"+
                	"</td><td style=\"display:none\" class=\"taskid\">"+temp[temp.length-1]+"</td></tr>";
				}
				string += "</tbody>"+
	              "</table>"+
	              "<div class=\"btn-toolbar pageBtn\" role=\"toolbar\" aria-label=\"page\">"+
	                "<div class=\"btn-group\" role=\"group\" aria-label=\"fpage\">";
				if(page==1)
					string += "<button type=\"button\" class=\"btn btn-default disabled\">上一页</button>";
				else
					string += "<button type=\"button\" class=\"btn btn-default\" onclick=\"jumptoTask('"+that+"',"+(page-1)+")\">上一页</button>";
				string += "</div>"+
                "<div class=\"btn-group\" role=\"group\" aria-label=\"spage\">";
	            if(pageSum>7){
					if(page<4){
						for(var i=1;i<=5;i++){
							if(i!=page)
								string += "<button type=\"button\" class=\"btn btn-default\" onclick=\"jumptoTask('"+that+"',"+i+")\">"+i+"</button>";
							else
								string += "<button type=\"button\" class=\"btn btn-default disabled\">"+i+"</button>";
						}
						string += "<button type=\"button\" class=\"btn btn-default\">……</button>";
						string += "<button type=\"button\" class=\"btn btn-default\" onclick=\"jumptoTask('"+that+"',"+pageSum+")\">"+pageSum+"</button>";
					}else if(page>=4&&page<(pageSum-4)){
						for(var i=page-2;i<=page+2;i++){
							if(i!=page)
								string += "<button type=\"button\" class=\"btn btn-default\" onclick=\"jumptoTask('"+that+"',"+i+")\">"+i+"</button>";
							else
								string += "<button type=\"button\" class=\"btn btn-default disabled\">"+i+"</button>";
						}
						string += "<button type=\"button\" class=\"btn btn-default\">……</button>";
						string += "<button type=\"button\" class=\"btn btn-default\" onclick=\"jumptoTask('"+that+"',"+pageSum+")\">"+pageSum+"</button>";
					}else{
						for(var i=pageSum-6;i<=pageSum;i++){
							if(i!=page)
								string += "<button type=\"button\" class=\"btn btn-default\" onclick=\"jumptoTask('"+that+"',"+i+")\">"+i+"</button>";
							else
								string += "<button type=\"button\" class=\"btn btn-default disabled\">"+i+"</button>";
						}
					}
	            }else{
					for(var i=1;i<=pageSum;i++){
						if(i!=page)
							string += "<button type=\"button\" class=\"btn btn-default\" onclick=\"jumptoTask('"+that+"',"+i+")\">"+i+"</button>";
						else
							string += "<button type=\"button\" class=\"btn btn-default disabled\">"+i+"</button>";
					}
				}
	            string += "</div>"+
                "<div class=\"btn-group\" role=\"group\" aria-label=\"tpage\">";
	            if(page==pageSum)
	            	string += "<button type=\"button\" class=\"btn btn-default disabled\">下一页</button>";
	            else
	            	string += "<button type=\"button\" class=\"btn btn-default\" onclick=\"jumptoTask('"+that+"',"+(page+1)+")\">下一页</button>";
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
		        "</div>"+
		        "<div class=\"modal fade\" id=\"myModal7\" tabindex=\"-1\" role=\"dialog\" aria-labelledby=\"myModalLabel\" aria-hidden=\"true\">"+
		            "<div class=\"modal-dialog\">"+
		                "<div class=\"modal-content\">"+
		                    "<div class=\"modal-header\">"+
		                        "<button type=\"button\" class=\"close\" data-dismiss=\"modal\" aria-hidden=\"true\">&times;</button>"+
		                        "<h4 class=\"modal-title\" id=\"myModalLabel\">修改HDFS路径</h4>"+
		                    "</div>"+
		                    "<div class=\"modal-body\">"+
			                    "<div class=\"form-group\">"+
				                    "<label>HDFS路径</label>"+
				                    "<input class=\"form-control\" placeholder=\"hdfsroute\" id=\"updateHDFSRoute\">"+
				                    "<div id='updateHDFSname' style='display:none'></div>"+
			                    "</div>"+
		                    "</div>"+
		                    "<div class=\"modal-footer\">"+
		                        "<button type=\"button\" class=\"btn btn-default\" data-dismiss=\"modal\" onclick='updateHDFS()'>更改</button>"+
		                        "<button type=\"button\" class=\"btn btn-default\" data-dismiss=\"modal\">关闭</button>"+
		                    "</div>"+
		                "</div>"+
		            "</div>"+
		        "</div>"+
		        "<div class=\"modal fade\" id=\"myModal8\" tabindex=\"-1\" role=\"dialog\" aria-labelledby=\"myModalLabel\" aria-hidden=\"true\">"+
		            "<div class=\"modal-dialog\">"+
		                "<div class=\"modal-content\">"+
		                    "<div class=\"modal-header\">"+
		                        "<button type=\"button\" class=\"close\" data-dismiss=\"modal\" aria-hidden=\"true\">&times;</button>"+
		                        "<h4 class=\"modal-title\" id=\"myModalLabel\">结果下载</h4>"+
		                    "</div>"+
		                    "<div class=\"modal-body\" id='download'>"+
		                    "</div>"+
		                    "<div class=\"modal-footer\">"+
		                        "<button type=\"button\" class=\"btn btn-default\" data-dismiss=\"modal\">关闭</button>"+
		                    "</div>"+
		                "</div>"+
		            "</div>"+
		        "</div>";
			}
			$("#page-wrapper").append(string);
			Ladda.bind( '.operate .ladda-button', {
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

function expand(that){
	var val = $(that).attr('class');
	var TempLastNum = val.split(" ");
	var lastNum = TempLastNum[TempLastNum.length-1];
	if($('.'+lastNum).length>1){
		var lengthLastNum = $('.'+lastNum).length;
		for(var i=1;i<lengthLastNum;i++)
			$('.'+lastNum).eq(1).remove();
		return;
	}
	var len = TempLastNum.length;
	var add = "";
	var sign = "";
	var bro = $(that);
	while(val!="0"){
		if(add!="")
			add = bro.html()+"/"+add;
		else
			add = bro.html();
		var temp = val.split(" ");
		sign = temp[temp.length-2];
		bro = $('.'+sign);
		val = bro.attr('class');
	}
	$.ajax({
  		url:ADDRESS+'/resourceInfo/ResourceInfo_HDFSRoute.action',
  		type:'post',
  		data:{'add':add},
  		error:function(){
  			alert("Error：服务器出错！");
  		},
  		success:function(data){
  			var arr = data.split("|");
  			var res = "";
  			for(var i=0;i<arr.length-1;i++){
  				var temp = arr[i].split(",");
  				if(temp[0]==1){
  					res += "<li style='margin-left:"+len*20+"px' class='"+$(that).attr('class')+" "+lastNum+"-"+(i+1)+"' onclick='expand(this)'>"+temp[1]+"</li>";
  				}else{
  					res += "<li style='margin-left:"+len*20+"px' class='"+$(that).attr('class')+" "+lastNum+"-"+(i+1)+"' ><a href='"+temp[2]+"'>"+temp[1]+"</a></li>";
  				}
  			}
  			$(that).after(res);
  		}
  	});
}

function resourceQueue(){
    $("#page-wrapper").empty();
    var string = 
        "<div class=\"row\">"+
            "<div class=\"col-lg-12\">"+
                "<h1 class=\"page-header\">集群资源管理</h1>"+
            "</div>"+
        "</div>"+
        "<div class=\"row\">"+
	        "<div class=\"panel panel-default\">"+
		        "<div class=\"panel-heading\">队列使用情况</div>"+
		        "<div class=\"panel-body\">"+
			        "<div class=\"table-responsive\">"+
	                	"<table class=\"table table-striped table-bordered table-hover\">"+
	                    "<thead><tr><th>name</th><th>capacity(%)</th><th>Maxcapacity(%)</th><th>Usedcapacity(%)</th><th>enable</th><th>memory(M)</th><th>cpu(个)</th><th>max_waiting_time(min)</th></tr></thead>"+
	                    "<tbody class='queueUsage'></tbody>"+
	                    "</table>"+
                    "</div>"+
                "</div>"+
		    "</div>"+
	    "</div>"+
	    "<div class=\"row\">"+
	        "<div class=\"panel panel-default\">"+
		        "<div class=\"panel-heading\">HDFS</div>"+
		        "<div class=\"panel-body\">"+
			        
	            "</div>"+
		    "</div>"+
	    "</div>"+
	    "<div class=\"row userUsage\">"+
	    "</div>";
                
	$("#page-wrapper").append(string);
	manageUserResource(1);   
     $.ajax({
			url:ADDRESS+'/queueInfo/QueueInfo_showQueue.action',
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
				var res = "";
				for(var i=0;i<val.length-1;i++){
					var temp = val[i].split(",");
					res += "<tr><td>"+temp[0]+"</td><td>"+(parseFloat(temp[1])).toFixed(2)+"</td><td>"+(parseFloat(temp[2])).toFixed(2)+"</td><td>"+
					(parseFloat(temp[3])).toFixed(2)+"</td><td>"+(temp[4]=='T'?"可用":"不可用")+"</td><td>"+temp[5]+"</td><td>"+temp[6]+"</td><td>"+temp[7]+"</td></tr>";
				}
				$('.queueUsage').append(res);
			}
		});
}

function manageUserResource(page){
	$(".userUsage").empty();
	var string = 
        "<div class=\"row\">"+
        	"<div class=\"panel panel-default\">"+
        		"<div class=\"panel-heading\">用户</div>"+
        		"<div class=\"panel-body\">";
    $.ajax({
		url:ADDRESS+'/resourceInfo/ResourceInfo_listResource.action',
		type:'post',
		data:{'page':page},
		error:function(){
			alert("服务器出错！");
		},
		success:function(data){
			string += "<table class=\"table table-striped\">"+
              "<thead><tr><th>用户名</th><th>队列名</th><th>hdfs</th><th>作业数</th><th>操作</th></tr></thead>"+
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
                    "<td class=\"operate\">"+
                      "<a href=\"####\" onclick=\"updateResource(this)\">修改</a>"+
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
					string += "<button type=\"button\" class=\"btn btn-default\" onclick=\"manageUserResource("+(page-1)+")\">上一页</button>";
				string += "</div>"+
                "<div class=\"btn-group\" role=\"group\" aria-label=\"spage\">";
	            if(pageSum>7){
					if(page<4){
						for(var i=1;i<=5;i++){
							if(i!=page)
								string += "<button type=\"button\" class=\"btn btn-default\" onclick=\"manageUserResource("+i+")\">"+i+"</button>";
							else
								string += "<button type=\"button\" class=\"btn btn-default disabled\">"+i+"</button>";
						}
						string += "<button type=\"button\" class=\"btn btn-default\">……</button>";
						string += "<button type=\"button\" class=\"btn btn-default\" onclick=\"manageUserResource("+pageSum+")\">"+pageSum+"</button>";
					}else if(page>=4&&page<(pageSum-4)){
						for(var i=page-2;i<=page+2;i++){
							if(i!=page)
								string += "<button type=\"button\" class=\"btn btn-default\" onclick=\"manageUserResource("+i+")\">"+i+"</button>";
							else
								string += "<button type=\"button\" class=\"btn btn-default disabled\">"+i+"</button>";
						}
						string += "<button type=\"button\" class=\"btn btn-default\">……</button>";
						string += "<button type=\"button\" class=\"btn btn-default\" onclick=\"manageUserResource("+pageSum+")\">"+pageSum+"</button>";
					}else{
						for(var i=pageSum-6;i<=pageSum;i++){
							if(i!=page)
								string += "<button type=\"button\" class=\"btn btn-default\" onclick=\"manageUserResource("+i+")\">"+i+"</button>";
							else
								string += "<button type=\"button\" class=\"btn btn-default disabled\">"+i+"</button>";
						}
					}
	            }else{
					for(var i=1;i<=pageSum;i++){
						if(i!=page)
							string += "<button type=\"button\" class=\"btn btn-default\" onclick=\"manageUserResource("+i+")\">"+i+"</button>";
						else
							string += "<button type=\"button\" class=\"btn btn-default disabled\">"+i+"</button>";
					}
				}
	            string += "</div>"+
                "<div class=\"btn-group\" role=\"group\" aria-label=\"tpage\">";
	            if(page==pageSum)
	            	string += "<button type=\"button\" class=\"btn btn-default disabled\">下一页</button>";
	            else
	            	string += "<button type=\"button\" class=\"btn btn-default\" onclick=\"manageUserResource("+(page+1)+")\">下一页</button>";
	            string += "</div>"+
	              "</div>"+
		            "</div>"+
		        "</div>"+
		        "</div>"+
		        "</div>";
			}
			$("#page-wrapper").append(string);
		}
	});
}

function updateResource(that){
	var temp = $(that).parent().parent().children('td');
	$("#page-wrapper").empty();
    var string = 
        "<div class=\"row\">"+
            "<div class=\"col-lg-12\">"+
                "<h1 class=\"page-header\">修改用户资源</h1>"+
            "</div>"+
        "</div>"+
        "<div class=\"row\">"+
            "<div class=\"col-lg-12\">"+
                "<div class=\"form-group\">"+
                    "<label>用户</label>"+
                    "<input class=\"form-control\" placeholder=\"此界面不支持用户名的更改\" id=\"updateName\" disabled>"+
                    "<p class=\"help-block\">此界面不支持用户名的更改</p>"+
                "</div>"+
                "<div class=\"form-group\">"+
                    "<label>队列</label>"+
                    "<div class=\"form-group\">"+
	                    "<select class=\"form-control\" id=\"updateQueue\">"+
	                    "</select>"+
                    "</div>"+
                    "<p class=\"help-block\">请选择其中一个队列</p>"+
                "</div>"+
                "<div class=\"form-group\">"+
	                "<label>HDFS(Byte)</label>"+
	                "<input class=\"form-control\" placeholder=\"HDFS\" id=\"updateHDFS\">"+
	                "<p class=\"help-block\">请输入整数</p>"+
	            "</div>"+
	            "<div class=\"form-group\">"+
	                "<label>作业数</label>"+
	                "<input class=\"form-control\" placeholder=\"jobNumber\" id=\"updateJob\">"+
	                "<p class=\"help-block\">请输入整数</p>"+
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
                    "<div class=\"modal-body\">您已成功修改了该用户资源</div>"+
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
    $.ajax({
		url:ADDRESS+'/queueInfo/QueueInfo_show.action',
		type:'post',
		data:{},
		error:function(){
			alert("Error:服务器错误!");
		},
		success:function(data){
			var res = data.split("|");
			var str = "";
			for(var i=0;i<res.length;i++){
				str+="<option>"+res[i]+"</option>";
			}
			$('#updateQueue').append(str);
			$("#updateQueue").val(temp.eq(1).html());
		}
	});
    
	$("#updateName").val(temp.eq(0).html());
    $("#updateHDFS").val(temp.eq(2).html());
    $("#updateJob").val(temp.eq(3).html());
    loadingR();
}

function loadingR(){
    Ladda.bind( '.submit button', {
      callback: function( instance ) {
	        var progress = 0;
	        var interval = setInterval( function() {
	          if( progress === 1 ) {
	            instance.stop();
	            clearInterval( interval );
	          }
	        }, 200 );
	        if($("#updateQueue").val()!=""&&$("#updateHDFS").val()!=""&&$("#updateJob").val()!=""){
	        $.ajax({
	    		url:ADDRESS+'/resourceInfo/ResourceInfo_updateResource.action',
	    		type:'post',
	    		data:{	"name" : $("#updateName").val(),
	    				"queue" : $("#updateQueue option:selected").val(),
						"hdfs": $("#updateHDFS").val(),
						"job": $("#updateJob").val()},
	    		error:function(){
	    			alert("Error:服务器错误!");
	    		},
	    		success:function(data){
	    			progress = 1;
	    			if(data=="1"){
	    				$('#notifyBtn3').click();
	    			}else
	    				alert("服务器错误！");
	    		}
			});
	      }else{
	    	  progress = 1;
	    	  $('#notifyBtn4').click();
	      }
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

function findTask(name){
	if(name=="")
		name = $('#findText').val();
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
				string += "<thead><tr><th>任务ID</th><th>任务的运行输入路径</th><th>任务的运行结果路径</th><th>任务的出错信息路径</th><th>任务创建时间</th><th>操作</th><th></th></tr></thead>"+
						"<tbody>"+		
							"<tr><td colspan='7'>空！</td></tr>"+
							"</tbody>"+
						"</table>";
			}
			else if(data=="00"){
				string += "<thead><tr><th>任务ID</th><th>用户名</th><th>任务的运行输入路径</th><th>任务的运行结果路径</th><th>任务的出错信息路径</th><th>任务创建时间</th><th>操作</th><th></th></tr></thead>"+
							"<tbody>"+			
							"<tr><td colspan='8'>空！</td></tr>"+
							"</tbody>"+
						"</table>";
			}
			else{
				var res = data.split(";");
				if(res[0]=="1"){
					string += "<thead><tr><th>任务ID</th><th>用户名</th><th>任务的运行输入路径</th><th>任务的运行结果路径</th><th>任务的出错信息路径</th><th>任务创建时间</th><th>操作</th><th></th></tr></thead>"+
					"<tbody>";
				}else{
					string += "<thead><tr><th>任务ID</th><th>任务的运行输入路径</th><th>任务的运行结果路径</th><th>任务的出错信息路径</th><th>任务创建时间</th><th>操作</th><th></th></tr></thead>"+
					"<tbody>";
				}

				var temp = res[1].split(",");
				string += "<tr>";
				for(var j=0;j<temp.length;j++){
					if((res[0]=="1"&&j==2)||(res[0]=="0"&&j==1)||(j==temp.length-1))
						continue;
					else if((res[0]=="1"&&j==3)||(res[0]=="0"&&j==2))
						string += "<td><a href='####' onclick='updateRoute(this)' id='"+temp[0]+"'>"+temp[j]+"</a></td>";
					else	
						string += "<td>"+temp[j]+"</td>";
				}
                string += "<td class=\"operate\">"+
                  "<button type=\"button\" class=\"btn btn-danger\" onclick=\"delTask(this)\">删除</button>"+
                "</td><td></td>"+
              "</tr>";
                if(res[0]=="1"){
					string += "<tr><td>作业状态</td><td colspan='5' class=\"processid\"><div class=\"progress progress-striped active\" style=\"margin-bottom:0\">"+
                    	"<div class=\"progress-bar progress-bar-info "+temp[temp.length-1]+"\" role=\"progressbar\" aria-valuenow=\"20\" aria-valuemin=\"0\" aria-valuemax=\"100\" style=\"width: "+temp[2]+"%\">"+temp[2]+"%</div>"+
                    "</div></td>";
                }else if(res[0]=="0"){
                	string += "<tr><td>作业状态</td><td colspan='4' class=\"processid\"><div class=\"progress progress-striped active\" style=\"margin-bottom:0\">"+
                    	"<div class=\"progress-bar progress-bar-info "+temp[temp.length-1]+"\" role=\"progressbar\" aria-valuenow=\"20\" aria-valuemin=\"0\" aria-valuemax=\"100\" style=\"width: "+temp[1]+"%\">"+temp[1]+"%</div>"+
                    "</div></td>";
                }
                string += "<td class=\"operate\">"+
                "<button onclick=\"start(this,false)\" class=\"btn btn-primary ladda-button\" data-style=\"contract-overlay\" style=\"z-index: 1000;\" data-size=\"xs\">运行</button>"+
            	"</td>"+
            	"<td class=\"operate\">"+
            	"<button onclick=\"download(this)\" class=\"btn btn-primary\">结果下载</button>"+
            	"</td><td style=\"display:none\" class=\"taskid\">"+temp[temp.length-1]+"</td></tr>";
				
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
		        "</div>"+
		        "<div class=\"modal fade\" id=\"myModal7\" tabindex=\"-1\" role=\"dialog\" aria-labelledby=\"myModalLabel\" aria-hidden=\"true\">"+
		            "<div class=\"modal-dialog\">"+
		                "<div class=\"modal-content\">"+
		                    "<div class=\"modal-header\">"+
		                        "<button type=\"button\" class=\"close\" data-dismiss=\"modal\" aria-hidden=\"true\">&times;</button>"+
		                        "<h4 class=\"modal-title\" id=\"myModalLabel\">修改HDFS路径</h4>"+
		                    "</div>"+
		                    "<div class=\"modal-body\">"+
			                    "<div class=\"form-group\">"+
				                    "<label>HDFS路径</label>"+
				                    "<input class=\"form-control\" placeholder=\"hdfsroute\" id=\"updateHDFSRoute\">"+
				                    "<div id='updateHDFSname' style='display:none'></div>"+
			                    "</div>"+
		                    "</div>"+
		                    "<div class=\"modal-footer\">"+
		                        "<button type=\"button\" class=\"btn btn-default\" data-dismiss=\"modal\" onclick='updateHDFS()'>更改</button>"+
		                        "<button type=\"button\" class=\"btn btn-default\" data-dismiss=\"modal\">关闭</button>"+
		                    "</div>"+
		                "</div>"+
		            "</div>"+
		        "</div>"+
		        "<div class=\"modal fade\" id=\"myModal8\" tabindex=\"-1\" role=\"dialog\" aria-labelledby=\"myModalLabel\" aria-hidden=\"true\">"+
		            "<div class=\"modal-dialog\">"+
		                "<div class=\"modal-content\">"+
		                    "<div class=\"modal-header\">"+
		                        "<button type=\"button\" class=\"close\" data-dismiss=\"modal\" aria-hidden=\"true\">&times;</button>"+
		                        "<h4 class=\"modal-title\" id=\"myModalLabel\">结果下载</h4>"+
		                    "</div>"+
		                    "<div class=\"modal-body\" id='download'>"+
		                    "</div>"+
		                    "<div class=\"modal-footer\">"+
		                        "<button type=\"button\" class=\"btn btn-default\" data-dismiss=\"modal\">关闭</button>"+
		                    "</div>"+
		                "</div>"+
		            "</div>"+
		        "</div>";
			}
			$("#page-wrapper").append(string);
			Ladda.bind( '.operate .ladda-button', {
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

function start(that,flag){
	var val = $(that).parent().parent().prev().children('td').eq(0).html();
	var input,name;
	if($(that).parent().parent().prev().children('td').length==7){
		input = $(that).parent().parent().prev().children('td').eq(1).children('a').html();
		name = "";
	}else{
		input = $(that).parent().parent().prev().children('td').eq(2).children('a').html();
		name = $(that).parent().parent().prev().children('td').eq(1).html();
	}
	var tempPro = $(that).parent().parent().children('td').eq(1).children('.progress').children('.progress-bar').html();
	if(tempPro!="100%"&&tempPro!="0%"){
		startprogress = 1;
		return;
	}
	$.ajax({
		url:ADDRESS+'/taskInfo/TaskInfo_start.action',
		type:'post',
		data:{'id':val,
			  'input':input,
			  'name':name},
		error:function(){
			alert("Error：服务器出错！");
			$("#page-wrapper").append(string);
		},
		success:function(data){
			startprogress = 1;
			if(data==-1){
				alert("输入路径有误！");
				return;
			}else if(data==0){
				alert("启动失败");
			}else{
				if(flag)
					showTask(taskpage);
				else
					findTask(val);
			}
		}
	});
}

function applyRes(){
	$("#page-wrapper").empty();
    var string = 
        "<div class=\"row\">"+
            "<div class=\"col-lg-12\">"+
                "<h1 class=\"page-header\">资源申请</h1>"+
            "</div>"+
        "</div>"+
        "<div class=\"row\">"+
            "<div class=\"col-lg-12\">"+
                "<div class=\"form-group\">"+
                    "<label>作业队列</label>"+
                    "<div class=\"form-group\">"+
	                    "<select class=\"form-control\" id=\"applyQueue\" onchange=\"queueChange(this.value);\">"+
	                        
	                    "</select>"+
                    "</div>"+
                    "<p class=\"help-block queueChange\"></p>"+
                "</div>"+
                "<div class=\"form-group\">"+
	                "<label>hdfs(M)</label>"+
	                "<div class=\"form-group\">"+
	                	"<input class=\"form-control\" placeholder=\"storage\" id=\"applyHDFS\">"+
	                "</div>"+
	                "<p class=\"help-block hdfsUse\"></p>"+
                "</div>"+
                "<div class=\"form-group\">"+
	                "<label>应用数目</label>"+
	                "<input class=\"form-control\" placeholder=\"appCount\" id=\"applyApp\">"+
	                "<p class=\"help-block\">请输入一个整数</p>"+
	            "</div>"+
                "<div class=\"submitApply\">"+
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
                    "<div class=\"modal-body\">您已成功提交申请</div>"+
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
                        "<h4 class=\"modal-title\" id=\"myModalLabel\">提交失败！</h4>"+
                    "</div>"+
                    "<div class=\"modal-body\">请正确填写！</div>"+
                    "<div class=\"modal-footer\">"+
                        "<button type=\"button\" class=\"btn btn-default\" data-dismiss=\"modal\">Close</button>"+
                    "</div>"+
                "</div>"+
            "</div>"+
        "</div>";
          
    $("#page-wrapper").append(string);
    loadingApply();
    showqueue();
    showHDFS();
}

function showqueue(){
	$.ajax({
		url:ADDRESS+'/queueInfo/QueueInfo_show.action',
		type:'post',
		data:{},
		error:function(){
			alert("Error:服务器错误!");
		},
		success:function(data){
			var arr = data.split("|");
			var res = "";
			for(var i=0;i<arr.length-1;i++){
				res+="<option>"+arr[i]+"</option>";
			}
			$("#applyQueue").empty();
			$("#applyQueue").html(res);
			$("#applyQueue").val("default");
			queueChange("default");
		}
	});
}

function showHDFS(){
	$.ajax({
		url:ADDRESS+'/resourceInfo/ResourceInfo_HDFS.action',
		type:'post',
		data:{},
		error:function(){
			alert("Error:服务器错误!");
		},
		success:function(data){
			var arr = data.split("|");
			var res = "当前已使用:"+parseInt(arr[1])/1048576+"M/"+parseInt(arr[0])/1048576+"M";
			$(".hdfsUse").empty();
			$(".hdfsUse").html(res);
		}
	});
}

function loadingApply(){
    Ladda.bind( '.submitApply button', {
      callback: function( instance ) {
	        var progress = 0;
	        var interval = setInterval( function() {
	          if( progress === 1 ) {
	            instance.stop();
	            clearInterval( interval );
	          }
	        }, 200 );
	        if($("#applyHDFS").val()!=""&&$("#applyApp").val()!=""){
		        $.ajax({
		    		url:ADDRESS+'/resourceInfo/ResourceInfo_apply.action',
		    		type:'post',
		    		data:{	"queue" : $("#applyQueue option:selected").val(),
							"hdfs":$("#applyHDFS").val()*1048576,
							"num": $("#applyApp").val()
						 },
		    		error:function(){
		    			alert("Error:服务器错误!");
		    		},
		    		success:function(data){
		    			$("#applyQueue").val("default");
		    			$("#applyHDFS").val("");
		    			$("#applyApp").val("");
		    			queueChange("default");
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
}

function queueChange(val){
	$.ajax({
		url:ADDRESS+'/queueInfo/QueueInfo_find.action',
		type:'post',
		data:{"queue" : val},
		error:function(){
			alert("Error:服务器错误!");
		},
		success:function(data){
			var arr = data.split("|");
			var res = "队列详情<br/>单容器占用的资源数:"+arr[0]+"<br/>"+"容量百分比:"+arr[1]+"<br/>"+"能否使用:"+arr[2];
			$(".queueChange").empty();
			$(".queueChange").html(res);
		}
	});
}

function manageApply(page){
	$("#page-wrapper").empty();
	var string = 
        "<div class=\"row\">"+
            "<div class=\"col-lg-12\">"+
                "<h1 class=\"page-header\">资源审核</h1>"+
            "</div>"+
        "</div>";
    $.ajax({
		url:ADDRESS+'/resourceInfo/ResourceInfo_confirm.action',
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
              "<thead><tr><th>用户名</th><th>队列名</th><th>hdfs</th><th>作业数</th><th>操作</th><th></th></tr></thead>"+
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
                    "<td class=\"operate\">"+
                      "<a href=\"####\" onclick=\"accept(this)\">接受</a>"+
                    "</td>"+
                    "<td class=\"operate\">"+
                      "<a href=\"####\" onclick=\"refuse(this)\">拒绝</a>"+
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
					string += "<button type=\"button\" class=\"btn btn-default\" onclick=\"manageApply("+(page-1)+")\">上一页</button>";
				string += "</div>"+
                "<div class=\"btn-group\" role=\"group\" aria-label=\"spage\">";
	            if(pageSum>7){
					if(page<4){
						for(var i=1;i<=5;i++){
							if(i!=page)
								string += "<button type=\"button\" class=\"btn btn-default\" onclick=\"manageApply("+i+")\">"+i+"</button>";
							else
								string += "<button type=\"button\" class=\"btn btn-default disabled\">"+i+"</button>";
						}
						string += "<button type=\"button\" class=\"btn btn-default\">……</button>";
						string += "<button type=\"button\" class=\"btn btn-default\" onclick=\"manageApply("+pageSum+")\">"+pageSum+"</button>";
					}else if(page>=4&&page<(pageSum-4)){
						for(var i=page-2;i<=page+2;i++){
							if(i!=page)
								string += "<button type=\"button\" class=\"btn btn-default\" onclick=\"manageApply("+i+")\">"+i+"</button>";
							else
								string += "<button type=\"button\" class=\"btn btn-default disabled\">"+i+"</button>";
						}
						string += "<button type=\"button\" class=\"btn btn-default\">……</button>";
						string += "<button type=\"button\" class=\"btn btn-default\" onclick=\"manageApply("+pageSum+")\">"+pageSum+"</button>";
					}else{
						for(var i=pageSum-6;i<=pageSum;i++){
							if(i!=page)
								string += "<button type=\"button\" class=\"btn btn-default\" onclick=\"manageApply("+i+")\">"+i+"</button>";
							else
								string += "<button type=\"button\" class=\"btn btn-default disabled\">"+i+"</button>";
						}
					}
	            }else{
					for(var i=1;i<=pageSum;i++){
						if(i!=page)
							string += "<button type=\"button\" class=\"btn btn-default\" onclick=\"manageApply("+i+")\">"+i+"</button>";
						else
							string += "<button type=\"button\" class=\"btn btn-default disabled\">"+i+"</button>";
					}
				}
	            string += "</div>"+
                "<div class=\"btn-group\" role=\"group\" aria-label=\"tpage\">";
	            if(page==pageSum)
	            	string += "<button type=\"button\" class=\"btn btn-default disabled\">下一页</button>";
	            else
	            	string += "<button type=\"button\" class=\"btn btn-default\" onclick=\"manageApply("+(page+1)+")\">下一页</button>";
	            string += "</div>"+
	              "</div>"+
		            "</div>"+
		        "</div>";
			}
			$("#page-wrapper").append(string);
		}
	});
}

function accept(that){
	var name = $(that).parent().parent().children('td').eq(0).html();
	$.ajax({
		url:ADDRESS+'/resourceInfo/ResourceInfo_accept.action',
		type:'post',
		data:{"name" : name},
		error:function(){
			alert("Error:服务器错误!");
		},
		success:function(data){
			$(that).parent().parent().remove();
		}
	});
}

function refuse(that){
	var name = $(that).parent().parent().children('td').eq(0).html();
	$.ajax({
		url:ADDRESS+'/resourceInfo/ResourceInfo_refuse.action',
		type:'post',
		data:{"name" : name},
		error:function(){
			alert("Error:服务器错误!");
		},
		success:function(data){
			$(that).parent().parent().remove();
		}
	});
}
