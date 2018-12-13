<html lang="zh-CN">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="icon" href="/favicon.ico">

    <title>登陆</title>


    <!-- Bootstrap core CSS -->
    <link href="${request.contextPath}/bootstrap3.3.5/css/bootstrap.min.css" rel="stylesheet">

    <!-- Custom styles for this template -->
    <link href="${request.contextPath}/css/signin.css" rel="stylesheet">

    <!-- Just for debugging purposes. Don't actually copy these 2 lines! -->
    <!--[if lt IE 9]><script src="${request.contextPath}/js/ie8-responsive-file-warning.js"></script><![endif]-->
    <script src="${request.contextPath}/js/ie-emulation-modes-warning.js"></script>

    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
    <script src="${request.contextPath}/js/html5shiv.min.js"></script>
    <script src="${request.contextPath}/js/respond.min.js"></script>

    <![endif]-->
    <script src="/js/jquery-1.9.1.min.js"></script>

    <script language="JavaScript">
        $(function (){
            $(".btn").click(function(){
                $.ajax({
                    async : false,
                    cache: false,
                    type: 'POST',
                    data: $(".form-signin").serializeArray(),
                    url: '${request.contextPath}/sys/admin/login',//请求的action路径
                    success:function(result){ //请求成功后处理函数。
                        console.info(result);
                        if(result.rect) {
                            window.location.href="${request.contextPath}/sys/admin/index";
                        }else{
                            alert(result.msg);
                        }
                    }
                });
            });
        });
    </script>
</head>

<body>

<div class="container">
    <form class="form-signin" action="/sys/admin/login" method="post">
        <h2 class="form-signin-heading">请登陆</h2>
        <label for="inputEmail" class="sr-only">邮箱/电话</label>
        <input type="text" id="inputEmail" class="form-control" placeholder="Email/Telephone" name="username" required autofocus>
        <label for="inputPassword" class="sr-only">密码</label>
        <input type="password" id="inputPassword" class="form-control" placeholder="Password" name="password" required >
        <div class="checkbox" style="color:red"></div>
        <button class="btn btn-lg btn-primary btn-block" type="button">登 陆</button>
    </form>
</div>

<!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
<script src="${request.contextPath}/js/ie10-viewport-bug-workaround.js"></script>
</body>
</html>
