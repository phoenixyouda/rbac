<html>
<head>
    <title>部门管理</title>
    <#include "../common/backend-common.ftl">
    <link rel="stylesheet" type="text/css" href="${request.contextPath}/ztree/zTreeStyle.css">
    <script type="text/javascript" src="${request.contextPath}/ztree/jquery.ztree.all.min.js"></script>
    <script type="text/javascript" src="${request.contextPath}/js/page.js"></script>
    <script language="JavaScript">
        var zTree;
        var treeData;

        var currPage;
        var pageSize;
        var departmentId;

        //重置分页参数
        function setPageParam(page,size,deptId){
            currPage=page;
            pageSize=size;
            departmentId=deptId;
        }
        //通过下拉框获取页面尺寸
        function getPageSizeBySelect(){

            pageSize=$('#dynamic-table_length').val();
            loadUsers(currPage,pageSize,departmentId);
        }


        /*========================部门管理========================*/
        $(function() {

            /*显示部门树*/
            zTree=loadDeptTree();

            /*加载用户列表*/
            loadUsers(1,10);


            /*点击新增部门按钮*/
            $(".dept-add").click(function() {
                var treeObj = $.fn.zTree.getZTreeObj("deptTree");
                var parentZNode = treeObj.getSelectedNodes(); //获取父节点
                if(parentZNode.length==0){
                    parentZNode=treeObj.getNodes(0);
                }
                $("#dialog-dept-form").dialog({
                    model: true,
                    title: "新增部门",
                    open: function(event, ui) {
                        $("#deptForm")[0].reset();
                        $("#parentName").val(parentZNode[0].name);
                        $("#parentId").val(parentZNode[0].id);
                        $("#deptLevel").val(parentZNode[0].deptLevel);
                    },
                    buttons : {
                        "添加": function(e) {
                            e.preventDefault();
                            save();
                        },
                        "取消": function () {
                            $("#dialog-dept-form").dialog("close");
                        }
                    }
                });
            });


            /*显示新增用户框*/
            $(".user-add").click(function() {
                var treeObj = $.fn.zTree.getZTreeObj("deptTree");
                var parentZNode = treeObj.getSelectedNodes(); //获取父节点
                if(parentZNode.length==0){
                    parentZNode=treeObj.getNodes(0);
                }

                $("#dialog-user-form").dialog({
                    model: true,
                    title: "用户管理",
                    open: function(event, ui) {
                        $("#userForm")[0].reset();
                        $("#deptName").val(parentZNode[0].name);
                        $("#deptId").val(parentZNode[0].id);
                    },
                    buttons : {
                        "保存": function(e) {
                            e.preventDefault();
                            //添加用户
                            userManager();
                        },
                        "取消": function () {
                            $("#dialog-user-form").dialog("close");
                        }
                    }
                });
            });


        });



        /*加载部门树*/
        function loadDeptTree() {
            $.ajax({
                async : false,
                cache: false,
                type: 'POST',
                dataType : 'json',
                url: '${request.contextPath}/sys/dept/tree',//请求的action路径
                success:function(result){ //请求成功后处理函数。
                    if(result.rect) {
                        treeData = result.data;   //把后台封装好的Json格式赋给treeNodes
                        $.fn.zTree.init($("#deptTree"), setting, treeData);

                    }else{
                        showMessage("加载部门列表", result.msg, false);
                    }
                }
            });
        }
        /*修改部门*/
        function zTreeOnRename(event, treeId, treeNode, clickFlag) {
            $.ajax({
                url: "${request.contextPath}/sys/dept/update",
                data: {
                    'id':$.trim(treeNode.id),
                    'name':$.trim(treeNode.name)
                },
                type: 'POST',
                success: function(result) {
                    if (result.rect) {
                        showMessage("部门管理",result.msg,true);
                    }else{
                        showMessage("部门管理",result.msg,false);
                    }
                    loadDeptTree();
                }
            })
        }

        /*删除部门*/
        function zTreeOnRemove(event, treeId, treeNode, clickFlag) {
            $.ajax({
                url: "${request.contextPath}/sys/dept/del",
                data: {
                    'id':$.trim(treeNode.id)
                },
                type: 'POST',
                success: function(result) {

                    if (result.rect) {
                        showMessage("部门管理",result.msg,true);
                    }else{
                        showMessage("部门管理",result.msg,false);
                    }
                    loadDeptTree();
                }
            })
        }

        /*点击部门*/
        /*eventjs event 对象
        标准的 js event 对象

        treeId         String
        对应 zTree 的 treeId，便于用户操控

        treeNode         JSON
        被点击的节点 JSON 数据对象*/
        function zTreeOnClickRight(event, treeId, treeNode, clickFlag){
            departmentId=treeNode.id;
            setPageParam(1,pageSize,departmentId);
            loadUsers(currPage,pageSize,departmentId);
        }

        /*设置部门树*/
        var setting = {
            view: {
                dblClickExpand: true,//双击节点时，是否自动展开父节点的标识
                showLine: true,//是否显示节点之间的连线
                fontCss:{'color':'black','font-weight':'bold'},//字体样式函数
                selectedMulti: false //设置是否允许同时选中多个节点
            },
            edit:{
                enable: true,
                editNameSelectAll: true,
                showRemoveBtn : true,
                showRenameBtn : true,
                removeTitle : "删除",
                renameTitle : "修改"
            },
            callback : {
                onClick : zTreeOnClickRight,
//                beforeRemove: beforeRemove,
                onRename: zTreeOnRename,
                onRemove: zTreeOnRemove,
            }

        };

        //设置下拉部门tree
        var selectTreesetting = {
            view: {
                dblClickExpand: true,//双击节点时，是否自动展开父节点的标识
                showLine: true,//是否显示节点之间的连线
                fontCss:{'color':'black','font-weight':'bold'},//字体样式函数
                selectedMulti: false //设置是否允许同时选中多个节点
            },
            edit:{
                enable: true,
                editNameSelectAll: true,
                showRemoveBtn : false,
                showRenameBtn : false
            },
            callback : {
                onClick : selectTreeOnClick,

            }

        };




        /*新增部门*/
        function save(){
            $.ajax({
                url: "${request.contextPath}/sys/dept/save",
                data: $("#deptForm").serializeArray(),
                type: 'POST',
                success: function(result) {
                    if (result.rect) {
                        $("#dialog-dept-form").dialog("close");
                        showMessage("部门管理",result.msg,true);
                        loadDeptTree();
                    }else{
                        showMessage("部门管理",result.msg,false);
                    }
                }
            })
        }



        /*========================用户管理========================*/

        /*用户管理  */
        function userManager(){

            $.ajax({
                url: "${request.contextPath}/sys/user/save",
                data: $("#userForm").serializeArray(),
                type: 'POST',
                success: function(result) {
                    if (result.rect) {
                        $("#dialog-user-form").dialog("close");
                        showMessage("员工管理",result.msg,true);
                        loadUsers(currPage,pageSize,departmentId);
                    }else{
                        showMessage("员工管理",result.msg,false);
                    }
                }
            })

        }

        /*加载用户*/
        function loadUsers(currPage,pageSize,departmentId){
            $.ajax({
                url: "${request.contextPath}/sys/user/userList",
                data: {
                    'currPage':currPage,
                    'pageSize':pageSize,
                    'departmentId':departmentId
                },
                type: 'POST',
                success: function(result) {
                    if (result.rect) {
                        var resultMap=result.data;

                        delRows();
                        addRow(resultMap.list);

                        show(resultMap.totalCounts,resultMap.from,resultMap.to,resultMap.currPage,resultMap.pageSize,resultMap.totalPages,departmentId);
                        setPageParam(resultMap.currPage,resultMap.pageSize,departmentId);
                        setDisabled(resultMap.currPage,resultMap.totalPages);
                    }else{
                        showMessage("用户管理",result.msg,false);
                    }
                }
            })
        }


        //删除行
        function delRows(){

            var table = document.getElementById('dynamic-table');
            var tbodies= $('#dynamic-table').find('tbody').find('tr');
            for(var i=tbodies.length;i>0;i--){
                table.deleteRow(i);
            }
        }


        /*显示用户*/
        //新增行
        function addRow(userList){
            for(var i=0;i<userList.length;i++) {

                var str = "<tr role='row' class='user-name odd'>" +
                        "<td><a href='#' class='user-edit' onclick='showUserFrame("+userList[i].id+")'>"+userList[i].username+"</a></td>" +
                        "<td>"+userList[i].deptName+"</td>" +
                        "<td>"+userList[i].telephone+"</td>" +
                        "<td>"+userList[i].mail+"</td>" +
                        "<td>"+userList[i].statusName+"</td>" +
                        "<td><a href='#' onclick='showUserFrame("+userList[i].id+")'>修改</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href='#' onclick='deleteUser("+userList[i].id+")'>删除</a></td>" +
                        "</tr>";
                $("#dynamic-table tbody").append(str);
            }


        }

        function showTreeMenu(){
            $.fn.zTree.init($("#treeDepartment"), selectTreesetting, treeData);
            $("#menuContent").show();

        }

        function hideTreeMenu(){
            $("#menuContent").hide();

        }

        //下拉树点击事件
        function selectTreeOnClick(event, treeId, treeNode, clickFlag){
            $("#deptName").val(treeNode.name);

            //document.getElementById("deptName").value=treeId.name;
            $("#deptId").val(treeNode.id);
            hideTreeMenu();
        };






        function showUserFrame(id){
            $.ajax({
                url: "${request.contextPath}/sys/user/queryById",
                data: {
                    'id':id
                },
                type: 'POST',
                success: function(result) {
                    if (result.rect) {
                        console.info(result.data);

                        $("#dialog-user-form").dialog({
                            model: true,
                            title: "用户管理",
                            open: function(event, ui) {
                                $("#userForm")[0].reset();
                                $("#deptName").val(result.data.deptName);
                                $("#deptId").val(result.data.deptId);
                                $("#userId").val(result.data.id);
                                $("#username").val(result.data.username);
                                $("#mail").val(result.data.mail);
                                $("#telephone").val(result.data.telephone);
                                $("#status").val(result.data.status);
                                $("#remark").val(result.data.remark);

                            },
                            buttons : {
                                "保存": function(e) {
                                    e.preventDefault();
                                    //添加用户
                                    userManager();
                                },
                                "取消": function () {
                                    $("#dialog-user-form").dialog("close");
                                }
                            }
                        });

                    }else{
                        showMessage("用户管理",result.msg,false);
                    }
                }
            });



        }


        /*删除用户*/
        function deleteUser(id){
            $.ajax({
                url: "${request.contextPath}/sys/user/delete",
                data: {
                    'id':id
                },
                type: 'POST',
                success: function(result) {
                    if (result.rect) {
                        show("用户管理",result.msg,true);
                        loadDeptTree();
                        loadUsers(currPage,pageSize,departmentId);
                    }else{
                        showMessage("用户管理",result.msg,false);
                    }
                }
            })
        }










    </script>

</head>
<body class="no-skin" youdao="bind" style="background: white">
<input id="gritter-light" checked="" type="checkbox" class="ace ace-switch ace-switch-5"/>

<div class="page-header">
    <h1>
        用户管理
        <small>
            <i class="ace-icon fa fa-angle-double-right"></i>
            维护部门与用户关系
        </small>
    </h1>
</div>
<div class="main-content-inner">
    <div class="col-sm-3">
        <div class="table-header">
            部门列表&nbsp;&nbsp;
            <a class="green" href="#">
                <i class="ace-icon fa fa-plus-circle orange bigger-130 dept-add"></i>
            </a>
        </div>
        <#--部门树列表-->
        <div>
            <ul id="deptTree" class="ztree">
            </ul>
        </div>


    </div>
    <div class="col-sm-9">
        <div class="col-xs-12">
            <div class="table-header">
                用户列表&nbsp;&nbsp;
                <a class="green" href="#">
                    <i class="ace-icon fa fa-plus-circle orange bigger-130 user-add"></i>
                </a>
            </div>
            <div>
                <div id="dynamic-table_wrapper" class="dataTables_wrapper form-inline no-footer">
                    <div class="row">
                        <div class="col-xs-6">
                            <div class="dataTables_length"><label>
                                展示
                                <select id="dynamic-table_length" name="dynamic-table_length"  class="form-control input-sm" onchange="getPageSizeBySelect();">
                                    <option value="10">10</option>
                                    <option value="25">25</option>
                                    <option value="50">50</option>
                                    <option value="100">100</option>
                                </select> 条记录 </label>
                            </div>
                        </div>
                    </div>
                    <table id="dynamic-table" class="table table-striped table-bordered table-hover dataTable no-footer" role="grid"
                           aria-describedby="dynamic-table_info" style="font-size:14px">
                        <thead>
                        <tr role="row">
                            <th tabindex="0" aria-controls="dynamic-table" rowspan="1" colspan="1">
                                姓名
                            </th>
                            <th tabindex="0" aria-controls="dynamic-table" rowspan="1" colspan="1">
                                所属部门
                            </th>
                            <th tabindex="0" aria-controls="dynamic-table" rowspan="1" colspan="1">
                                电话
                            </th>
                            <th tabindex="0" aria-controls="dynamic-table" rowspan="1" colspan="1">
                                邮箱
                            </th>
                            <th tabindex="0" aria-controls="dynamic-table" rowspan="1" colspan="1">
                                状态
                            </th>
                            <th class="sorting_disabled" rowspan="1" colspan="1" aria-label="">操作</th>
                        </tr>
                        </thead>
                        <tbody id="userList">

                        </tbody>
                    </table>
                    <div class="row" id="turnPage">



                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<div id="dialog-dept-form" style="display: none;">
    <form id="deptForm" name="deptForm">
        <table class="table table-striped table-bordered table-hover dataTable no-footer" role="grid">
            <tr>
                <td style="width: 80px;"><label>上级部门</label></td>
                <td>
                    <input id="parentName" name="parentName" style="width: 200px;" readonly/>
                    <input type="hidden" name="id" id="id"/>
                    <input type="hidden" name="deptLevel" id="deptLevel"/>
                    <input type="hidden" name="parentId" id="parentId"/>

                </td>
            </tr>
            <tr>
                <td><label for="name">名称</label></td>
                <td><input type="text" name="name" id="name" value="" class="text ui-widget-content ui-corner-all"></td>
            </tr>
            <tr>
                <td><label for="sort">顺序</label></td>
                <td><input type="text" name="sort" id="sort" value="1" class="text ui-widget-content ui-corner-all"></td>
            </tr>
            <tr>
                <td><label for="remark">备注</label></td>
                <td><textarea name="remark" id="remark" class="text ui-widget-content ui-corner-all" rows="3" cols="25"></textarea></td>
            </tr>
        </table>
    </form>
</div>
<div id="dialog-user-form" style="display: none;">
    <form id="userForm">
        <table class="table table-striped table-bordered table-hover dataTable no-footer" role="grid">
            <tr>
                <td style="width: 80px;"><label for="deptName">所在部门</label></td>


                <td>
                    <input  type="text" id="deptName" name="deptName" onclick="showTreeMenu()" style="width: 170px;" readonly/>
                    <input type="button" value="∨" onclick="showTreeMenu()">
                    <div id="menuContent"  style="display:none; position: absolute;">
                        <ul id="treeDepartment" class="ztree" style="margin-top: 10px;border: 1px solid #617775;background: #fefefe;width:190px;height:270px;overflow-y:scroll;overflow-x:auto;">
                        </ul>
                    </div>

                    <input  type="hidden" id="deptId" name="deptId" style="width: 200px;"/>
                </td>
            </tr>
            <tr>
                <td><label for="username">名称</label></td>
                <input type="hidden" name="userId" id="userId"/>
                <td><input type="text" name="username" id="username" value="" class="text ui-widget-content ui-corner-all"></td>
            </tr>
            <tr>
                <td><label for="mail">邮箱</label></td>
                <td><input type="text" name="mail" id="mail" value="" class="text ui-widget-content ui-corner-all"></td>
            </tr>
            <tr>
                <td><label for="telephone">电话</label></td>
                <td><input type="text" name="telephone" id="telephone" value="" class="text ui-widget-content ui-corner-all"></td>
            </tr>
            <tr>
                <td><label for="status">状态</label></td>
                <td>
                    <select id="status" name="status" data-placeholder="选择状态" style="width: 150px;">
                        <option value="0">冻结</option>
                        <option value="1" selected>正常</option>

                    </select>
                </td>
            </tr>
            <tr>
                <td><label for="remark">备注</label></td>
                <td><textarea name="remark" id="remark" class="text ui-widget-content ui-corner-all" rows="3" cols="25"></textarea></td>
            </tr>
        </table>
    </form>
</div>
</body>
</html>
