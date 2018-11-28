<html>
<head>
    <title>部门管理</title>
    <#include "../common/backend-common.ftl">
    <link rel="stylesheet" type="text/css" href="${request.contextPath}/ztree/zTreeStyle.css">
    <script type="text/javascript" src="${request.contextPath}/ztree/jquery.ztree.all.min.js"></script>

    <script language="JavaScript">
        var zTree;
        var treeData;
        $(function() {

            /*显示部门树*/
            zTree=loadDeptTree();

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

            /*新增部门*/
            function save(){
                $.ajax({
                    url: "${request.contextPath}/sys/dept/save",
                    data: $("#deptForm").serializeArray(),
                    type: 'POST',
                    success: function(result) {
                        if (result.rect) {
                            $("#dialog-dept-form").dialog("close");
                            loadDeptTree();
                        }else{
                            showMessage("部门管理",rect.msg,false);
                        }
                    }
                })
            }





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

        });

        function zTreeOnRename(event, treeId, treeNode, isCancel) {
            console.info(treeId);
            console.info(treeNode);
            console.info(isCancel);
            $.ajax({
                url: "${request.contextPath}/sys/dept/update",
                data: {
                    'deptId':$.trim(treeNode.id),
                    'deptName':$.trim(treeNode.name)
                },
                type: 'POST',
                success: function(result) {
                    if (result.rect) {
                        $("#dialog-dept-form").dialog("close");
                        loadDeptTree();
                    }else{
                        showMessage("部门管理",rect.msg,false);
                    }
                }
            })



        }

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
                removeTitle : "remove",
                renameTitle : "rename"
            },
            callback : {
//                onClick : zTreeOnClickRight,
//                beforeRemove: beforeRemove,
                onRename: zTreeOnRename,
//                onRemove: zTreeOnRemove,
            }
        };



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

</div>
<div id="dialog-dept-form" style="display: none;">
    <form id="deptForm">
        <table class="table table-striped table-bordered table-hover dataTable no-footer" role="grid">
            <tr>
                <td style="width: 80px;"><label for="parentId">上级部门</label></td>
                <td>
                    <input id="parentName" name="parentName" style="width: 200px;" readonly/>
                    <input type="hidden" name="parentId" id="parentId"/>
                    <input type="hidden" name="deptLevel" id="deptLevel"/>
                    <input type="hidden" name="deptId" id="deptId"/>

                </td>
            </tr>
            <tr>
                <td><label for="deptName">名称</label></td>
                <td><input type="text" name="deptName" id="deptName" value="" class="text ui-widget-content ui-corner-all"></td>
            </tr>
            <tr>
                <td><label for="deptSeq">顺序</label></td>
                <td><input type="text" name="sort" id="sort" value="1" class="text ui-widget-content ui-corner-all"></td>
            </tr>
            <tr>
                <td><label for="deptRemark">备注</label></td>
                <td><textarea name="deptRemark" id="deptRemark" class="text ui-widget-content ui-corner-all" rows="3" cols="25"></textarea></td>
            </tr>
        </table>
    </form>
</div>
<div id="dialog-user-form" style="display: none;">
    <form id="userForm">
        <table class="table table-striped table-bordered table-hover dataTable no-footer" role="grid">
            <tr>
                <td style="width: 80px;"><label for="parentId">所在部门</label></td>
                <td>
                    <select id="deptSelectId" name="deptId" data-placeholder="选择部门" style="width: 200px;"></select>
                </td>
            </tr>
            <tr>
                <td><label for="userName">名称</label></td>
                <input type="hidden" name="id" id="userId"/>
                <td><input type="text" name="username" id="userName" value="" class="text ui-widget-content ui-corner-all"></td>
            </tr>
            <tr>
                <td><label for="userMail">邮箱</label></td>
                <td><input type="text" name="mail" id="userMail" value="" class="text ui-widget-content ui-corner-all"></td>
            </tr>
            <tr>
                <td><label for="userTelephone">电话</label></td>
                <td><input type="text" name="telephone" id="userTelephone" value="" class="text ui-widget-content ui-corner-all"></td>
            </tr>
            <tr>
                <td><label for="userStatus">状态</label></td>
                <td>
                    <select id="userStatus" name="status" data-placeholder="选择状态" style="width: 150px;">
                        <option value="1">有效</option>
                        <option value="0">无效</option>
                        <option value="2">删除</option>
                    </select>
                </td>
            </tr>
            <tr>
                <td><label for="userRemark">备注</label></td>
                <td><textarea name="remark" id="userRemark" class="text ui-widget-content ui-corner-all" rows="3" cols="25"></textarea></td>
            </tr>
        </table>
    </form>
</div>
</body>
</html>
