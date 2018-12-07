<html>
<head>
    <title>权限</title>
    <#include "../common/backend-common.ftl">
    <link rel="stylesheet" type="text/css" href="${request.contextPath}/ztree/zTreeStyle.css">
    <script type="text/javascript" src="${request.contextPath}/ztree/jquery.ztree.all.min.js"></script>
    <script type="text/javascript" src="${request.contextPath}/js/page.js"></script>
    <script language="JavaScript">

        var treeData;
        var currPage;
        var pageSize;
        var moduleId;


        /*新增模块*/
        function save(){
            $.ajax({
                url: "${request.contextPath}/sys/aclModule/save",
                data: $("#aclModuleForm").serializeArray(),
                type: 'POST',
                success: function(result) {
                    if (result.rect) {
                        $("#dialog-aclModule-form").dialog("close");
                        showMessage("新增权限组成功",result.msg,true);
                        loadModuleTree();
                    }else{
                        showMessage("新增权限组失败",result.msg,false);
                    }
                }
            })
        }

        /*加载模块树*/
        function loadModuleTree(){
            $.ajax({
                async : false,
                cache: false,
                type: 'POST',
                dataType : 'json',
                url: '${request.contextPath}/sys/aclModule/tree',//请求的action路径
                success:function(result){ //请求成功后处理函数。
                    if(result.rect) {
                        treeData = result.data;   //把后台封装好的Json格式赋给treeNodes
                        $.fn.zTree.init($("#moduleTree"), setting, treeData);
                    }else{
                        showMessage("加载模块列表失败", result.msg, false);
                    }
                }
            });
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

        //下拉树点击事件
        function selectTreeOnClick(event, treeId, treeNode, clickFlag){
            $("#moduleName").val(treeNode.name);

            //document.getElementById("deptName").value=treeId.name;
            $("#moduleId").val(treeNode.id);
            hideTreeMenu();
        };
        function showTreeMenu(){
            $.fn.zTree.init($("#treeModule"), selectTreesetting, treeData);
            $("#menuContent").show();

        }
        function hideTreeMenu(){
            $("#menuContent").hide();

        }
        /*点击模块树*/
        function zTreeOnClickRight(event, treeId, treeNode, clickFlag){

            moduleId=treeNode.id;
            setPageParam(1,pageSize,moduleId);
            loadAcls(currPage,pageSize,moduleId);
        }

        /*删除模块*/
        function zTreeOnRemove(event, treeId, treeNode, clickFlag) {
            $.ajax({
                url: "${request.contextPath}/sys/aclModule/delete",
                data: {
                    'id':$.trim(treeNode.id)
                },
                type: 'POST',
                success: function(result) {

                    if (result.rect) {
                        showMessage("删除成功",result.msg,true);
                    }else{
                        showMessage("删除失败",result.msg,false);
                    }
                    loadModuleTree();
                }
            })
        }

        /*修改模块*/
        function zTreeOnRename(event, treeId, treeNode, clickFlag) {
            $.ajax({
                url: "${request.contextPath}/sys/aclModule/update",
                data: {
                    'id':$.trim(treeNode.id),
                    'name':$.trim(treeNode.name)
                },
                type: 'POST',
                success: function(result) {
                    if (result.rect) {
                        showMessage("修改成功",result.msg,true);
                    }else{
                        showMessage("修改失败",result.msg,false);
                    }
                    loadModuleTree();
                }
            })
        }

        /*加载权限点*/
        function loadAcls(currPage,pageSize,moduleId){
            $.ajax({
                url: "${request.contextPath}/sys/acl/aclList",
                data: {
                    'currPage':currPage,
                    'pageSize':pageSize,
                    'moduleId':moduleId
                },
                type: 'POST',
                success: function(result) {
                    if (result.rect) {
                        var resultMap=result.data;

                        delRows();
                        addRow(resultMap.list);

                        showAcls(resultMap.totalCounts,resultMap.from,resultMap.to,resultMap.currPage,resultMap.pageSize,resultMap.totalPages,moduleId);
                        setPageParam(resultMap.currPage,resultMap.pageSize,moduleId);
                        setDisabled(resultMap.currPage,resultMap.totalPages);
                    }else{
                        showMessage("用户加载失败",result.msg,false);
                    }
                }
            })
        }
        //重置分页参数
        function setPageParam(page,size,sysModuleId){
            currPage=page;
            pageSize=size;
            moduleId=sysModuleId;
        }
        //通过下拉框获取页面尺寸
        function getPageSizeBySelect(){

            pageSize=$('#dynamic-table_length').val();
            loadAcls(currPage,pageSize,moduleId);
        }

        //删除行
        function delRows(){

            var table = document.getElementById('dynamic-table');
            var tbodies= $('#dynamic-table').find('tbody').find('tr');
            for(var i=tbodies.length;i>0;i--){
                table.deleteRow(i);
            }
        }

        /*显示权限点*/
        //新增行
        function addRow(aclList){
            for(var i=0;i<aclList.length;i++) {
                var sname="<td><span class='label label-sm label-success'>"+aclList[i].statusName+"</span></td>";
                if(aclList[i].status=="0"){
                    sname="<td><span class='label label-sm label-warning'>"+aclList[i].statusName+"</span></td>";
                }

                var str = "<tr role='row' class='user-name odd'>" +
                        "<td><a href='#' class='user-edit' onclick='showAclFrame("+aclList[i].id+")'>"+aclList[i].name+"</a></td>" +
                        "<td>"+aclList[i].moduleName+"</td>" +
                        "<td>"+aclList[i].typeName+"</td>" +
                        "<td>"+aclList[i].url+"</td>" +
                        sname+
                        "<td>"+aclList[i].sort+"</td>" +

                        "<td><div class='hidden-sm hidden-xs action-buttons'> <a class='green acl-edit' href='#' onclick='showAclFrame("+aclList[i].id+")'> <i class='ace-icon fa fa-pencil bigger-100'></i> </a> <a class='red acl-role' href='#'> <i class='ace-icon fa fa-flag bigger-100'></i> </a> </div> </td>"



                        /*"<td><a href='#' onclick='showUserFrame("+aclList[i].id+")'>修改</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href='#' onclick='deleteUser("+aclList[i].id+")'>删除</a></td>" +*/
                        "</tr>";
                $("#dynamic-table tbody").append(str);
            }


        }


        function showAclFrame(id){
            $.ajax({
                url: "${request.contextPath}/sys/acl/findById",
                data: {
                    'id':id
                },
                type: 'POST',
                success: function(result) {
                    if (result.rect) {
                        console.info(result.data);
                        console.info($("#sort"));
                        $("#dialog-acl-form").dialog({
                            model: true,
                            title: "权限点管理",
                            open: function(event, ui) {
                                $("#aclForm")[0].reset();
                                $("#moduleName").val(result.data.moduleName);
                                $("#moduleId").val(result.data.moduleId);
                                $("#aclId").val(result.data.id);
                                $("#aclName").val(result.data.name);
                                $("#aclType").val(result.data.type);
                                $("#url").val(result.data.url);
                                $("#aclStatus").val(result.data.status);
                                $("#aclSort").val(result.data.sort);
                                $("#aclRemark").val(result.data.remark);

                            },
                            buttons : {
                                "保存": function(e) {
                                    e.preventDefault();
                                    aclManager();
                                },
                                "取消": function () {
                                    $("#dialog-acl-form").dialog("close");
                                }
                            }
                        });

                    }else{
                        showMessage("权限点列表加载失败",result.msg,false);
                    }
                }
            });
        }


        /*权限点管理  */
        function aclManager(){

            $.ajax({
                url: "${request.contextPath}/sys/acl/save",
                data: $("#aclForm").serializeArray(),
                type: 'POST',
                success: function(result) {
                    if (result.rect) {
                        $("#dialog-acl-form").dialog("close");
                        showMessage("保存成功",result.msg,true);
                        loadAcls(currPage,pageSize,moduleId);
                    }else{
                        showMessage("保存失败",result.msg,false);
                    }
                }
            })

        }

        $(function(){

            /*加载模块树*/
            loadModuleTree();

            /*加载权限点列表*/
            loadAcls(1,10);

            /*显示新增模块dialog*/
            $(".aclModule-add").click(function(){
                var treeObj = $.fn.zTree.getZTreeObj("moduleTree");

                var parentZNode = treeObj.getSelectedNodes(); //获取父节点
                if(parentZNode.length==0){
                    parentZNode=treeObj.getNodes(0);
                }
                $("#dialog-aclModule-form").dialog({
                    model: true,
                    title: "新增模块",
                    open: function(event, ui) {
                        $("#aclModuleForm")[0].reset();
                        $("#parentName").val(parentZNode[0].name);
                        $("#parentId").val(parentZNode[0].id);
                        $("#moduleLevel").val(parentZNode[0].moduleLevel);
                    },
                    buttons : {
                        "添加": function(e) {
                            e.preventDefault();
                            save();
                        },
                        "取消": function () {
                            $("#dialog-aclModule-form").dialog("close");
                        }
                    }
                })
            });





            /*显示新增权限点框*/
            $(".acl-add").click(function() {
                var treeObj = $.fn.zTree.getZTreeObj("moduleTree");
                var parentZNode = treeObj.getSelectedNodes(); //获取父节点
                if(parentZNode.length==0){
                    parentZNode=treeObj.getNodes(0);
                }

                $("#dialog-acl-form").dialog({
                    model: true,
                    title: "用户管理",
                    open: function(event, ui) {
                        $("#aclForm")[0].reset();
                        $("#moduleName").val(parentZNode[0].name);
                        $("#moduleId").val(parentZNode[0].id);
                    },
                    buttons : {
                        "保存": function(e) {
                            e.preventDefault();
                            //添加用户
                            aclManager();
                        },
                        "取消": function () {
                            $("#dialog-acl-form").dialog("close");
                        }
                    }
                });
            });
        });
    </script>
</head>
<body class="no-skin" youdao="bind" style="background: white">
<input id="gritter-light" checked="" type="checkbox" class="ace ace-switch ace-switch-5"/>

<div class="page-header">
    <h1>
        权限模块管理
        <small>
            <i class="ace-icon fa fa-angle-double-right"></i>
            维护权限模块和权限点关系
        </small>
    </h1>
</div>
<div class="main-content-inner">
    <div class="col-sm-3">
        <div class="table-header">
            权限模块列表&nbsp;&nbsp;
            <a class="green" href="#">
                <i class="ace-icon fa fa-plus-circle orange bigger-130 aclModule-add"></i>
            </a>
        </div>
        <div>
            <ul id="moduleTree" class="ztree">
            </ul>
        </div>
    </div>
    <div class="col-sm-9">
        <div class="col-xs-12">
            <div class="table-header">
                权限点列表&nbsp;&nbsp;
                <a class="green" href="#">
                    <i class="ace-icon fa fa-plus-circle orange bigger-130 acl-add"></i>
                </a>
            </div>
            <div>
                <div id="dynamic-table_wrapper" class="dataTables_wrapper form-inline no-footer">
                    <div class="row">
                        <div class="col-xs-6">
                            <div class="dataTables_length" id="dynamic-table_length"><label>
                                展示
                                <select id="dynamic-table_length" name="dynamic-table_length" aria-controls="dynamic-table" class="form-control input-sm" onchange="getPageSizeBySelect();">
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
                                权限名称
                            </th>
                            <th tabindex="0" aria-controls="dynamic-table" rowspan="1" colspan="1">
                                权限模块
                            </th>
                            <th tabindex="0" aria-controls="dynamic-table" rowspan="1" colspan="1">
                                类型
                            </th>
                            <th tabindex="0" aria-controls="dynamic-table" rowspan="1" colspan="1">
                                URL
                            </th>
                            <th tabindex="0" aria-controls="dynamic-table" rowspan="1" colspan="1">
                                状态
                            </th>
                            <th tabindex="0" aria-controls="dynamic-table" rowspan="1" colspan="1">
                                顺序
                            </th>
                            <th class="sorting_disabled" rowspan="1" colspan="1" aria-label=""></th>
                        </tr>
                        </thead>
                        <tbody id="aclList"></tbody>
                    </table>

                    <div class="row" id="turnPage">
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<div id="dialog-aclModule-form" style="display: none;">
    <form id="aclModuleForm">
        <table class="table table-striped table-bordered table-hover dataTable no-footer" role="grid">
            <tr>
                <td style="width: 80px;"><label for="parentName">上级模块</label></td>
                <td>
                    <input type="text" id="parentName" name="parentName" style="width: 200px;"/>
                    <input type="hidden" id="parentId" name="parentId" style="width: 200px;"/>
                    <input type="hidden" name="id" id="id"/>
                    <input type="hidden" name="moduleLevel" id="moduleLevel"/>
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
                <td><label for="status">状态</label></td>
                <td>
                    <select id="status" name="status" data-placeholder="选择状态" style="width: 150px;">
                        <option value="0">无效</option>
                        <option value="1" selected>有效</option>
                        <option value="2">删除</option>
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
<div id="dialog-acl-form" style="display: none;">
    <form id="aclForm">
        <table class="table table-striped table-bordered table-hover dataTable no-footer" role="grid">
            <tr>
                <td style="width: 80px;"><label>所属权限模块</label></td>
                <td>
                    <input  type="text" id="moduleName" name="moduleName" onclick="showTreeMenu()" style="width: 170px;" readonly/>
                    <input type="button" value="∨" onclick="showTreeMenu()">
                    <div id="menuContent"  style="display:none; position: absolute;">
                        <ul id="treeModule" class="ztree" style="margin-top: 10px;border: 1px solid #617775;background: #fefefe;width:190px;height:270px;overflow-y:scroll;overflow-x:auto;">
                        </ul>
                    </div>

                    <input  type="hidden" id="moduleId" name="moduleId" style="width: 200px;"/>

                </td>
            </tr>
            <tr>
                <td><label for="aclName">名称</label></td>
                <input type="hidden" name="id" id="aclId"/>
                <td><input type="text" name="name" id="aclName" value="" class="text ui-widget-content ui-corner-all"></td>
            </tr>
            <tr>
                <td><label for="aclType">类型</label></td>
                <td>
                    <select id="aclType" name="type" data-placeholder="类型" style="width: 150px;">
                        <option value="1">菜单</option>
                        <option value="2">按钮</option>
                        <option value="3">其他</option>
                    </select>
                </td>
            </tr>
            <tr>
                <td><label for="url">URL</label></td>
                <td><input type="text" name="url" id="url" value="1" class="text ui-widget-content ui-corner-all"></td>
            </tr>
            <tr>
                <td><label for="aclStatus">状态</label></td>
                <td>
                    <select id="aclStatus" name="status" data-placeholder="选择状态" style="width: 150px;">
                        <option value="0">冻结</option>
                        <option value="1" selected>正常</option>
                    </select>
                </td>
            </tr>
            <tr>
                <td><label for="aclSort">顺序</label></td>
                <td><input type="text" name="sort" id="aclSort" value="" class="text ui-widget-content ui-corner-all"></td>
            </tr>
            <tr>
                <td><label for="aclRemark">备注</label></td>
                <td><textarea name="remark" id="aclRemark" class="text ui-widget-content ui-corner-all" rows="3" cols="25"></textarea></td>
            </tr>
        </table>
    </form>
</div>


</body>
</html>
