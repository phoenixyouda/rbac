<html>
<head>
    <title>角色</title>
    <#include "../common/backend-common.ftl">
    <link rel="stylesheet" href="${request.contextPath}/ztree/zTreeStyle.css" type="text/css">
    <link rel="stylesheet" href="${request.contextPath}/assets/css/bootstrap-duallistbox.min.css" type="text/css">
    <script type="text/javascript" src="${request.contextPath}/ztree/jquery.ztree.all.min.js"></script>
    <script type="text/javascript" src="${request.contextPath}/assets/js/jquery.bootstrap-duallistbox.min.js"></script>

    <style type="text/css">
        .bootstrap-duallistbox-container .moveall, .bootstrap-duallistbox-container .removeall {
            width: 50%;
        }
        .bootstrap-duallistbox-container .move, .bootstrap-duallistbox-container .remove {
            width: 49%;
        }
    </style>
</head>
<body class="no-skin" youdao="bind" style="background: white">
<input id="gritter-light" checked="" type="checkbox" class="ace ace-switch ace-switch-5"/>
<div class="page-header">
    <h1>
        角色管理
        <small>
            <i class="ace-icon fa fa-angle-double-right"></i>
            维护角色与用户, 角色与权限关系
        </small>
    </h1>
</div>
<div class="main-content-inner">
    <div class="col-sm-3">
        <div class="table-header">
            角色列表&nbsp;&nbsp;
            <a class="green" href="#">
                <i class="ace-icon fa fa-plus-circle orange bigger-130 role-add"></i>
            </a>
        </div>
        <div id="roleList"></div>
    </div>
    <div class="col-sm-9">
        <div class="tabbable" id="roleTab">
            <ul class="nav nav-tabs">
                <li class="active">
                    <a data-toggle="tab" href="#roleAclTab">
                        角色与权限
                    </a>
                </li>
                <li>
                    <a data-toggle="tab" href="#roleUserTab">
                        角色与用户
                    </a>
                </li>
            </ul>
            <div class="tab-content">
                <div id="roleAclTab" class="tab-pane fade in active">
                    <ul id="roleAclTree" class="ztree"></ul>
                    <button class="btn btn-info saveRoleAcl" type="button">
                        <i class="ace-icon fa fa-check bigger-110"></i>
                        保存
                    </button>
                </div>

                <div id="roleUserTab" class="tab-pane fade" >
                    <div class="row">
                        <div class="box1 col-md-6">待选用户列表</div>
                        <div class="box1 col-md-6">已选用户列表</div>
                    </div>
                    <select multiple="multiple" size="10" name="roleUserList" id="roleUserList" >
                    </select>
                    <div class="hr hr-16 hr-dotted"></div>
                    <button class="btn btn-info saveRoleUser" type="button">
                        <i class="ace-icon fa fa-check bigger-110"></i>
                        保存
                    </button>
                </div>
            </div>
        </div>
    </div>
</div>
<div id="dialog-role-form" style="display: none;">
    <form id="roleForm">
        <table class="table table-striped table-bordered table-hover dataTable no-footer" role="grid">
            <tr>
                <td><label for="roleName">名称</label></td>
                <td>
                    <input type="text" name="roleName" id="roleName" value="" class="text ui-widget-content ui-corner-all">
                    <input type="hidden" name="roleId" id="roleId"/>
                </td>
            </tr>
            <tr>
                <td><label for="roleType">类型</label></td>
                <td>
                    <select id="roleType" name="roleType" data-placeholder="类型" style="width: 150px;">
                        <option value="1">管理员</option>
                        <option value="0">其他</option>
                    </select>
                </td>
            </tr>
            <tr>
                <td><label for="roleStatus">状态</label></td>
                <td>
                    <select id="roleStatus" name="status" data-placeholder="状态" style="width: 150px;">
                        <option value="1">正常</option>
                        <option value="0">冻结</option>
                    </select>
                </td>
            </tr>
            <tr>
                <td><label for="roleSort">顺序</label></td>
                <td>
                    <input type="text" name="roleSort" id="roleSort" value="1" class="text ui-widget-content ui-corner-all">
                </td>
            </tr>
            <tr>
            <td><label for="roleRemark">备注</label></td>
            <td><textarea name="remark" id="roleRemark" class="text ui-widget-content ui-corner-all" rows="3" cols="25"></textarea></td>
            </tr>
        </table>
    </form>
</div>
<script id="roleListTemplate" type="x-tmpl-mustache">
<ol class="dd-list ">
    {{#roleList}}
        <li class="dd-item dd2-item role-name" id="role_{{id}}" href="javascript:void(0)" data-id="{{id}}">
            <div class="dd2-content" style="cursor:pointer;">
            {{name}}
            <span style="float:right;">
                <a class="green role-edit" href="#" data-id="{{id}}" >
                    <i class="ace-icon fa fa-pencil bigger-100"></i>
                </a>
                &nbsp;
                <a class="red role-delete" href="#" data-id="{{id}}" data-name="{{name}}">
                    <i class="ace-icon fa fa-trash-o bigger-100"></i>
                </a>
            </span>
            </div>
        </li>
    {{/roleList}}
</ol>
</script>

<script id="selectedUsersTemplate" type="x-tmpl-mustache">
{{#userList}}
    <option value="{{id}}" selected="selected">{{username}}</option>
{{/userList}}
</script>

<script id="unSelectedUsersTemplate" type="x-tmpl-mustache">
{{#userList}}
    <option value="{{id}}">{{username}}</option>
{{/userList}}
</script>

<script type="text/javascript">
    $(function () {
        var roleMap = {};
        var lastRoleId = -1;
        var selectFirstTab = true;
        var hasMultiSelect = false;

        var roleListTemplate = $("#roleListTemplate").html();
        Mustache.parse(roleListTemplate);
        var selectedUsersTemplate = $("#selectedUsersTemplate").html();
        Mustache.parse(selectedUsersTemplate);
        var unSelectedUsersTemplate = $("#unSelectedUsersTemplate").html();
        Mustache.parse(unSelectedUsersTemplate);

        loadRoleList();

        // zTree
        <!-- 树结构相关 开始 -->
        var zTreeObj = [];
        var modulePrefix = 'm_';
        var aclPrefix = 'a_';
        var nodeMap = {};

        var setting = {
            check: {
                enable: true,
                chkDisabledInherit: true,
                chkboxType: {"Y": "ps", "N": "ps"}, //auto check 父节点 子节点
                autoCheckTrigger: true
            },
            data: {
                simpleData: {
                    enable: true,
                    rootPId: 0
                }
            },
            callback: {
                onClick: onClickTreeNode
            }
        };

        function onClickTreeNode(e, treeId, treeNode) { // 绑定单击事件
            var zTree = $.fn.zTree.getZTreeObj("roleAclTree");
            zTree.expandNode(treeNode);
        }

        function loadRoleList() {
            $.ajax({
                url: "/sys/role/roleList",
                success: function (result) {
                    if (result.rect) {
                        var rendered = Mustache.render(roleListTemplate, {roleList: result.data});
                        $("#roleList").html(rendered);
                        bindRoleClick();
                        $.each(result.data, function(i, role) {
                            roleMap[role.id] = role;

                        });
                    } else {
                        showMessage("加载角色列表", result.msg, false);
                    }
                }
            });
        }
        function bindRoleClick() {
            $(".role-edit").click(function (e) {
                e.preventDefault();
                e.stopPropagation();
                var roleId = $(this).attr("data-id");
                $("#dialog-role-form").dialog({
                    model: true,
                    title: "修改角色",
                    open: function(event, ui) {
                        $(".ui-dialog-titlebar-close", $(this).parent()).hide();
                        $("#roleForm")[0].reset();
                        var targetRole = roleMap[roleId];
                        if (targetRole) {
                            $("#roleId").val(roleId);
                            $("#roleName").val(targetRole.name);
                            $("#roleStatus").val(targetRole.status);
                            $("#roleRemark").val(targetRole.remark);
                            $("#roleSort").val(targetRole.sort);
                        }
                    },
                    buttons : {
                        "修改": function(e) {
                            e.preventDefault();
                            updateRole(function (data) {
                                $("#dialog-role-form").dialog("close");
                            }, function (data) {
                                showMessage("修改角色", data.msg, false);
                            })
                        },
                        "取消": function () {
                            $("#dialog-role-form").dialog("close");
                        }
                    }
                })
            });

            //删除按钮
            $(".role-delete").click(function (e) {
                e.preventDefault();
                e.stopPropagation();
                var roleId = $(this).attr("data-id");
                $.ajax({
                    url: "/sys/role/delete",
                    data: {
                        id:roleId,
                    },
                    success: function (result) {
                        if (result.rect) {
                            showMessage("删除角色", result.msg, true);
                        } else {
                            showMessage("删除角色", result.msg, false);
                        }
                    }
                });

            });
            $(".role-name").click(function (e) {
                e.preventDefault();
                e.stopPropagation();
                var roleId = $(this).attr("data-id");
                handleRoleSelected(roleId);
            });
        }

        function handleRoleSelected(roleId) {
            if (lastRoleId != -1) {
                var lastRole = $("#role_" + lastRoleId + " .dd2-content:first");
                lastRole.removeClass("btn-yellow");
                lastRole.removeClass("no-hover");
            }
            var currentRole = $("#role_" + roleId + " .dd2-content:first");
            currentRole.addClass("btn-yellow");
            currentRole.addClass("no-hover");
            lastRoleId = roleId;


            //角色与权限面板点击事件

            $('#roleTab a:first').trigger('click');
            if (selectFirstTab) {//selectFirtTab为true，加载第一个面板
                loadRoleAcl(roleId);
            }

        }
        //加载角色与权限
        function loadRoleAcl(selectedRoleId) {
            if (selectedRoleId == -1) {
                return;
            }
            $.ajax({
                url: "/sys/role/roleTree",
                data : {
                    roleId: selectedRoleId
                },
                type: 'POST',
                success: function (result) {
                    if (result.rect) {
                        renderRoleTree(result.data);
                    } else {
                        showMessage("加载角色权限数据", result.msg, false);
                    }
                }
            });
        }

        function getTreeSelectedId() {
            var treeObj = $.fn.zTree.getZTreeObj("roleAclTree");
            var nodes = treeObj.getCheckedNodes(true);

            var v = "";
            for(var i = 0; i < nodes.length; i++) {

                if(nodes[i].url) {
                    v +=  nodes[i].id+",";
                }
            }
            console.info(v);
            if(v.length>0){
                v= v.substring(0,v.length-1);
            }
            console.info(v);
            return v;
        }

        /*渲染角色-权限树*/
        function renderRoleTree(aclModuleList) {
            zTreeObj = [];
            recursivePrepareTreeData(aclModuleList);
            for(var key in nodeMap) {
                zTreeObj.push(nodeMap[key]);
            }
            $.fn.zTree.init($("#roleAclTree"), setting, zTreeObj);
        }


        function recursivePrepareTreeData(aclModuleList) {
            // prepare nodeMap
            if (aclModuleList && aclModuleList.length > 0) {
                $(aclModuleList).each(function(i, aclModule) {
                    if (aclModule.sysAclList && aclModule.sysAclList.length > 0) {
                        nodeMap[modulePrefix + aclModule.id] = {
                            id : modulePrefix + aclModule.id,
                            pId: modulePrefix + aclModule.parentId,
                            name: aclModule.name,
                            children: aclModule.sysAclList,
                            checked: aclModule.checked,
                            open: true
                        };
                    }else{
                        nodeMap[modulePrefix + aclModule.id] = {
                            id : modulePrefix + aclModule.id,
                            pId: modulePrefix + aclModule.parentId,
                            name: aclModule.name,
                            open: true,
                            checked:aclModule.checked
                        };
                    }
                    recursivePrepareTreeData(aclModule.children);
                });
            }
        }

        //新增角色按钮点击事件
        $(".role-add").click(function () {
            $("#dialog-role-form").dialog({
                model: true,
                title: "新增角色",
                open: function(event, ui) {
                    $(".ui-dialog-titlebar-close", $(this).parent()).hide();
                    $("#roleForm")[0].reset();
                },
                buttons : {
                    "添加": function(e) {
                        e.preventDefault();
                        updateRole(function (data) {
                            $("#dialog-role-form").dialog("close");
                        }, function (data) {
                            showMessage("新增角色", data.msg, false);
                        })
                    },
                    "取消": function () {
                        $("#dialog-role-form").dialog("close");
                    }
                }
            })
        });

        /*保存角色-权限树*/
        $(".saveRoleAcl").click(function (e) {
            e.preventDefault();
            if (lastRoleId == -1) {
                showMessage("保存角色与权限点的关系", "请现在左侧选择需要操作的角色", false);
                return;
            }
            $.ajax({
                url: "/sys/role/changeAcls",
                data: {
                    roleId: lastRoleId,
                    aclIds: getTreeSelectedId()
                },
                type: 'POST',
                success: function (result) {
                    if (result.rect) {
                        showMessage("保存角色与权限点的关系", result.msg, true);
                    } else {
                        showMessage("保存角色与权限点的关系", result.msg, false);
                    }
                }
            });
        });

        //修改角色
        function updateRole(successCallback, failCallback) {
            $.ajax({
                url:  "/sys/role/save",
                data: $("#roleForm").serializeArray(),
                type: 'POST',
                success: function(result) {
                    if (result.rect) {
                        loadRoleList();
                        if (successCallback) {
                            successCallback(result);
                        }
                    } else {
                        if (failCallback) {
                            failCallback(result);
                        }
                    }
                }
            })
        };


        $("#roleTab a[data-toggle='tab']").on('shown.bs.tab', function(e) {
            console.info(111);
            if(lastRoleId == -1) {
                showMessage("加载角色关系","请先在左侧选择操作的角色", false);
                return;
            }


            if (e.target.getAttribute("href") == '#roleAclTab') {
                selectFirstTab = true;
                loadRoleAcl(lastRoleId);
            } else {
                selectFirstTab = false;
                loadRoleUser(lastRoleId);
            }
        });


        function loadRoleUser(selectedRoleId) {
            $.ajax({
                url: "/sys/role/users",
                data: {
                    roleId: selectedRoleId
                },
                type: 'POST',
                success: function (result) {
                    if (result.rect) {
                        var renderedSelect = Mustache.render(selectedUsersTemplate, {userList: result.data.selected});
                        var renderedUnSelect = Mustache.render(unSelectedUsersTemplate, {userList: result.data.unselected});
                        $("#roleUserList").html(renderedSelect + renderedUnSelect);

                        if(!hasMultiSelect) {
                            $('select[name="roleUserList"]').bootstrapDualListbox({
                                showFilterInputs: false,
                                moveOnSelect: false,
                                infoText: false
                            });
                            hasMultiSelect = true;
                        } else {
                            $('select[name="roleUserList"]').bootstrapDualListbox('refresh', true);
                        }
                    } else {
                        showMessage("加载角色用户数据", result.msg, false);
                    }
                }
            });
        }

        $(".saveRoleUser").click(function (e) {
            e.preventDefault();
            if (lastRoleId == -1) {
                showMessage("保存角色与用户的关系", "请现在左侧选择需要操作的角色", false);
                return;
            }
            $.ajax({
                url: "/sys/role/changeUsers",
                data: {
                    roleId: lastRoleId,
                    userIds: $("#roleUserList").val() ? $("#roleUserList").val().join(",") : ''
                },
                type: 'POST',
                success: function (result) {
                    if (result.rect) {
                        showMessage("保存角色与用户的关系", result.msg, true);
                    } else {
                        showMessage("保存角色与用户的关系", result.msg, false);
                    }
                }
            });
        });
    });
</script>
</body>
</html>
