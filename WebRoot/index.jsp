<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Hadoop</title>
    <link rel="stylesheet" type="text/css" href="<%=path%>/css/main.css">
    <link href="<%=path%>/css/bootstrap.min.css" rel="stylesheet">
    <!--[if lt IE 9]>
      <script src="<%=path%>/js/html5shiv.js"></script>
      <script src="<%=path%>/js/respond.min.js"></script>
    <![endif]-->
  </head>
  <body>

    <div class="container">
        <div class="row" style="margin-top:150px">
            <div class="col-md-4 col-md-offset-4">
                <div class="login-panel panel panel-default">
                    <div class="panel-heading">
                        <h3 class="panel-title">Please Sign In</h3>
                    </div>
                    <div class="panel-body">
                        <form role="form" action="<%=path%>/user/User_login.action" method="post">
                            <fieldset>
                                <div class="form-group">
                                    <input class="form-control" placeholder="Username" name="name" type="text" autofocus>
                                </div>
                                <div class="form-group">
                                    <input class="form-control" placeholder="Password" name="password" type="password" value="">
                                </div>
                                <div class="checkbox">
                                    <label>
                                        <input name="remember" type="checkbox" value="Remember Me">Remember Me
                                    </label>
                                </div>
                                <input type="submit" class="btn btn-lg btn-success btn-block" value="Login">
                            </fieldset>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script src="<%=path%>/js/jquery-1.12.0.js"></script>
    <script src="<%=path%>/js/bootstrap.min.js"></script>
    <script src="<%=path%>/js/main.js"></script>
    <script type="text/javascript">
	  var msg="${requestScope.login}";
	  if(msg=="failed"){
	     alert("用户名或密码错误！");
	  }
	</script>
  </body>
</html>