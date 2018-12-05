
-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(64) NOT NULL DEFAULT '' COMMENT '部门名称',
  `level` varchar(16) NOT NULL DEFAULT '' COMMENT '部门级别',
  `parent_id` int(11) NOT NULL DEFAULT '0' COMMENT '父部门',
  `sort` int(11) NOT NULL DEFAULT '0' COMMENT '排序',
  `remark` varchar(256) DEFAULT '' COMMENT '备注',
  `operator` varchar(16) DEFAULT '' COMMENT '操作者',
  `operator_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '操作时间',
  `operator_ip` varchar(32) NOT NULL DEFAULT '' COMMENT '操作IP',
  key `ids_dept_name` (`name`),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='部门表';


-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
create table sys_user(
	`id` int not null auto_increment comment 'id',
	`username` varchar(32) not null default '' comment '姓名',
	`password` varchar(32) not null default '' comment '密码',
	`telephone` varchar(32)  default '' comment '电话',
	`mail` varchar(64) default '' comment '邮箱',
	`dept_id` int not null comment '部门',
	`remark` varchar(256) DEFAULT '' COMMENT '备注',
	`operator` varchar(16) DEFAULT '' COMMENT '操作者',
	`operator_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '操作时间',
	`operator_ip` varchar(32) NOT NULL DEFAULT '' COMMENT '操作IP',
	key `ids_user_username` (`username`),
	 PRIMARY KEY (`id`)

)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';


-- ----------------------------
-- Table structure for sys_acl_module
-- ----------------------------
DROP TABLE IF EXISTS `sys_acl_module`;
create table `sys_acl_module`(
	`id` int not null auto_increment,
	`name` varchar(32) not null default '' comment '模块名称',
	`level` varchar(32) not null default '' comment '模块层级',
	`parent_id` int not null default '0' comment '父模块',
	`sort` int not null default '0' comment '排序',
	`remark` varchar(256) DEFAULT '' COMMENT '备注',
	`operator` varchar(16) DEFAULT '' COMMENT '操作者',
	`operator_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '操作时间',
	`operator_ip` varchar(32) NOT NULL DEFAULT '' COMMENT '操作IP',
	key `ids_module_name` (`name`),
	primary key (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='权限模块表';

-- ----------------------------
-- Table structure for sys_acl
-- ----------------------------
DROP TABLE IF EXISTS `sys_acl`;
create table `sys_acl`(
	`id` int not null auto_increment comment '权限id',
	`code` varchar(16) not null comment '权限码',
	`name` varchar(32) not null default '' comment '权限名称',
	`acl_module_id` int not null default '0' comment '权限模块id',
	`url` varchar(128) not null comment '请求的url，可以使用正则表达式',
	`type` int not null default '3' comment '权限类型: 1 菜单 2 按钮 3 其他',
	`status` int not null default '1' comment '状态: 0 冻结 1 正常',
	`sort` int not null default '0' comment '排序',
	`remark` varchar(256) DEFAULT '' COMMENT '备注',
	`operator` varchar(16) DEFAULT '' COMMENT '操作者',
	`operator_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '操作时间',
	`operator_ip` varchar(32) NOT NULL DEFAULT '' COMMENT '操作IP',
	key `ids_acl_name` (`name`),
	primary key (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='权限表';

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
create table `sys_role`(
	`id` int not null auto_increment comment '角色id',
	`name` varchar(32) not null default '' comment '角色名称',
	`type` int not null default '1' comment '角色类型 1: 管理员类型  2：其他',
	`status` int not null default '1' comment '角色状态:0 冻结 1 正常',
	`sort` int not null default '0' comment '排序',
	`remark` varchar(256) DEFAULT '' COMMENT '备注',
	`operator` varchar(16) DEFAULT '' COMMENT '操作者',
	`operator_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '操作时间',
	`operator_ip` varchar(32) NOT NULL DEFAULT '' COMMENT '操作IP',
	key `ids_role_name` (`name`),
	primary key (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表';


-- ----------------------------
-- Table structure for sys_role_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_user`;
create table `sys_role_user`(
	`id` int not null auto_increment,
	`role_id` int not null comment '角色id',
	`user_id` int not null comment '用户id',
	`operator` varchar(16) DEFAULT '' COMMENT '操作者',
	`operator_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '操作时间',
	`operator_ip` varchar(32) NOT NULL DEFAULT '' COMMENT '操作IP',
	key `ids_role_id` (`role_id`),
	key `ids_user_id` (`user_id`),
	primary key (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色用户表';

-- ----------------------------
-- Table structure for sys_role_acl
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_acl`;
create table `sys_role_acl`(
	`id` int not null auto_increment,
	`role_id` int not null comment '角色id',
	`acl_id` int not null comment '权限id',
	`operator` varchar(16) DEFAULT '' COMMENT '操作者',
	`operator_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '操作时间',
	`operator_ip` varchar(32) NOT NULL DEFAULT '' COMMENT '操作IP',
	key `ids_role_id` (`role_id`),
	key `ids_acl_id` (`acl_id`),
	primary key (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色权限表';

-- ----------------------------
-- Table structure for sys_log
-- ----------------------------
DROP TABLE IF EXISTS sys_log;
create table sys_log(
	`id` int not null auto_increment,
	`type` int not null default '0' comment '操作目标表: 0：未知表，不做处理 1：部门表 2：用户表 3：权限模块表 4：权限表 5：角色表 6：角色用户关系表 7 角色权限关系表',
	`target_id` int not null comment '基于type操作表的id',
	`old_value` varchar(512) not null default '' comment '原始值',
	`new_value` varchar(512) not null default '' comment '新值',
	`status` int not null default '0' comment '操作状态 0 :未还原 1：已还原',
	`updateType` int not null DEFAULT '0' comment '操作类型 0，增加，1，修改，2，删除，3，授予，4，撤销',
	`operator` varchar(16) DEFAULT '' COMMENT '操作者',
	`operator_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '操作时间',
	`operator_ip` varchar(32) NOT NULL DEFAULT '' COMMENT '操作IP',
	
	primary key (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='操作记录表';