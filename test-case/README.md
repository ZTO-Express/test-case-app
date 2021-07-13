# Test Case Management
* 测试用例管理平台，是一个前后端分离的项目。主要功能有系统设置、测试用例与执行计划的管理与维护。
-------------------------------------------------------------------------

# 表结构及基础数据
```
SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for tc_case_type
-- ----------------------------
DROP TABLE IF EXISTS `tc_case_type`;
CREATE TABLE `tc_case_type` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `status` tinyint(3) NOT NULL DEFAULT '1' COMMENT '批次状态：1-正常，0-删除，2-停用',
  `type` tinyint(3) DEFAULT NULL COMMENT '类型：1-用例类型，2-用例标签',
  `name` varchar(255) NOT NULL DEFAULT '' COMMENT '名称',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_user` varchar(50) NOT NULL DEFAULT '' COMMENT '创建人',
  `update_user` varchar(50) NOT NULL DEFAULT '' COMMENT '更新人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COMMENT='用例标签表';

-- ----------------------------
-- Records of tc_case_type
-- ----------------------------
INSERT INTO `tc_case_type` VALUES ('1', '1', '1', '功能测试', '2021-03-10 14:48:00', '2021-04-29 17:40:17', '', '');
INSERT INTO `tc_case_type` VALUES ('2', '1', '1', '冒烟测试', '2021-03-10 14:48:59', '2021-04-29 17:40:19', '', '');
INSERT INTO `tc_case_type` VALUES ('3', '2', '1', '性能测试', '2021-03-10 14:49:07', '2021-04-29 17:40:21', '', '');
INSERT INTO `tc_case_type` VALUES ('4', '2', '1', '自动化测试', '2021-03-10 14:49:12', '2021-04-29 17:40:21', '', '');
INSERT INTO `tc_case_type` VALUES ('5', '2', '1', '接口测试', '2021-03-10 14:49:33', '2021-04-29 17:40:22', '', '');
INSERT INTO `tc_case_type` VALUES ('6', '2', '1', '安装部署', '2021-03-10 14:49:39', '2021-04-29 17:40:23', '', '');
INSERT INTO `tc_case_type` VALUES ('7', '2', '1', '配置相关', '2021-03-10 14:49:46', '2021-04-29 17:40:24', '', '');
INSERT INTO `tc_case_type` VALUES ('8', '1', '1', '安全测试', '2021-03-10 14:49:51', '2021-04-29 17:40:25', '', '');
INSERT INTO `tc_case_type` VALUES ('9', '2', '1', '其它', '2021-03-10 14:49:56', '2021-04-29 17:40:27', '', '');
INSERT INTO `tc_case_type` VALUES ('10', '1', '1', '典型测试', '2021-04-29 17:39:30', '2021-04-29 17:39:30', '', '');
INSERT INTO `tc_case_type` VALUES ('11', '1', '2', 'web', '2021-04-29 17:39:37', '2021-04-29 17:39:37', '', '');
INSERT INTO `tc_case_type` VALUES ('12', '1', '2', '移动端', '2021-04-29 17:39:46', '2021-04-29 17:39:48', '', '');
INSERT INTO `tc_case_type` VALUES ('13', '1', '2', '接口', '2021-04-29 17:39:56', '2021-04-29 17:39:56', '', '');
```
```
SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for tc_file
-- ----------------------------
DROP TABLE IF EXISTS `tc_file`;
CREATE TABLE `tc_file` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `path` varchar(256) NOT NULL DEFAULT '' COMMENT '文件存储全路径',
  `name` varchar(256) NOT NULL DEFAULT '' COMMENT '文件名称',
  `relation_id` int(10) DEFAULT '0' COMMENT '关联id',
  `type` tinyint(4) DEFAULT '1' COMMENT '用例类型：1-用例, 2-计划',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_user` varchar(50) NOT NULL DEFAULT '' COMMENT '创建人',
  `update_user` varchar(50) NOT NULL DEFAULT '' COMMENT '更新人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用例文件表';
```

```
SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for tc_plan
-- ----------------------------
DROP TABLE IF EXISTS `tc_plan`;
CREATE TABLE `tc_plan` (
  `id` int(9) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `plan_name` varchar(100) NOT NULL DEFAULT '' COMMENT '执行计划名',
  `state` tinyint(3) NOT NULL DEFAULT '0' COMMENT '测试状态，0-未开始，1-进行中，2-已完成',
  `vid` int(9) NOT NULL DEFAULT '0' COMMENT '关联版本',
  `stage_id` int(9) NOT NULL DEFAULT '0' COMMENT '测试阶段id',
  `execute_user` int(9) NOT NULL DEFAULT '0' COMMENT '测试计划负责人',
  `total_num` int(9) NOT NULL DEFAULT '0' COMMENT '总用例数量',
  `execute_num` int(9) NOT NULL DEFAULT '0' COMMENT '用例执行数量',
  `pass_num` int(9) NOT NULL DEFAULT '0' COMMENT '通过数量',
  `fail_num` int(9) NOT NULL DEFAULT '0' COMMENT '失败数量',
  `block_num` int(9) NOT NULL DEFAULT '0' COMMENT '阻塞数量',
  `skip_num` int(9) NOT NULL DEFAULT '0' COMMENT '跳过数量',
  `pass_rate` decimal(10,4) NOT NULL DEFAULT '0.0000' COMMENT '通过率',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_user` varchar(50) NOT NULL DEFAULT '' COMMENT '创建人',
  `update_user` varchar(50) NOT NULL DEFAULT '' COMMENT '更新人',
  `status` tinyint(3) NOT NULL DEFAULT '1' COMMENT '状态，0-删除，1-正常',
  PRIMARY KEY (`id`),
  KEY `idx_vid` (`vid`) USING BTREE,
  KEY `idx_stage_id` (`stage_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=121 DEFAULT CHARSET=utf8mb4 COMMENT='执行计划表';
```
```
SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for tc_plan_case_result
-- ----------------------------
DROP TABLE IF EXISTS `tc_plan_case_result`;
CREATE TABLE `tc_plan_case_result` (
  `id` bigint(19) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `plan_id` int(9) NOT NULL DEFAULT '0' COMMENT '计划id',
  `case_id` int(9) NOT NULL DEFAULT '0' COMMENT '用例id',
  `result` tinyint(3) NOT NULL DEFAULT '0' COMMENT '执行结果，0-未执行，1-通过，2-失败，3-阻塞，4-跳过',
  `execute_user` int(9) NOT NULL DEFAULT '0' COMMENT '执行人（默认为测试计划负责人）',
  `result_comment` varchar(500) DEFAULT '' COMMENT '执行结果备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_user` varchar(50) NOT NULL DEFAULT '' COMMENT '创建人',
  `update_user` varchar(50) NOT NULL DEFAULT '' COMMENT '更新人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_plan_id` (`plan_id`,`case_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='执行计划-用例关联结果表';
```
```
SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for tc_plan_case_step_result
-- ----------------------------
DROP TABLE IF EXISTS `tc_plan_case_step_result`;
CREATE TABLE `tc_plan_case_step_result` (
  `id` bigint(19) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `plan_id` int(9) NOT NULL DEFAULT '0' COMMENT '执行计划id',
  `case_id` int(9) NOT NULL DEFAULT '0' COMMENT '用例id',
  `step_id` int(9) NOT NULL DEFAULT '0' COMMENT '步骤id',
  `result` tinyint(3) NOT NULL DEFAULT '0' COMMENT '执行结果',
  `actual_result` varchar(500) NOT NULL DEFAULT '' COMMENT '实际结果',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_user` varchar(50) NOT NULL DEFAULT '' COMMENT '创建人',
  `update_user` varchar(50) NOT NULL DEFAULT '' COMMENT '更新人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_plan_id_case_id_step_id` (`plan_id`,`case_id`,`step_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2549 DEFAULT CHARSET=utf8mb4 COMMENT='步骤结果表';
```
```
SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for tc_test_stage
-- ----------------------------
DROP TABLE IF EXISTS `tc_test_stage`;
CREATE TABLE `tc_test_stage` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `status` tinyint(3) NOT NULL DEFAULT '1' COMMENT '批次状态：1-正常，0-删除，2-停用',
  `name` varchar(255) NOT NULL DEFAULT '' COMMENT '名称',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_user` varchar(50) NOT NULL DEFAULT '' COMMENT '创建人',
  `update_user` varchar(50) NOT NULL DEFAULT '' COMMENT '更新人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COMMENT='执行阶段表';

-- ----------------------------
-- Records of tc_test_stage
-- ----------------------------
INSERT INTO `tc_test_stage` VALUES ('1', '1', '冒烟测试', '2021-03-10 14:50:47', '2021-04-09 17:59:21', 'system', 'system');
INSERT INTO `tc_test_stage` VALUES ('2', '1', '单元测试', '2021-03-10 14:50:53', '2021-04-09 17:59:21', 'system', 'system');
INSERT INTO `tc_test_stage` VALUES ('3', '1', '功能测试', '2021-03-10 14:51:01', '2021-04-09 17:59:21', 'system', 'system');
INSERT INTO `tc_test_stage` VALUES ('4', '1', '集成测试', '2021-03-10 14:51:06', '2021-04-09 17:59:21', 'system', 'system');
INSERT INTO `tc_test_stage` VALUES ('5', '1', '系统测试', '2021-03-10 14:51:10', '2021-04-09 17:59:21', 'system', 'system');
```
```
SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for tc_testcase
-- ----------------------------
DROP TABLE IF EXISTS `tc_testcase`;
CREATE TABLE `tc_testcase` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `name` varchar(256) NOT NULL DEFAULT '' COMMENT '用例名称',
  `priority` tinyint(4) NOT NULL DEFAULT '3' COMMENT '优先级:1 低 2 中 3 高',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '状态：1-正常，0-删除，2-停用',
  `precondition` longtext COMMENT '前置条件',
  `type` tinyint(4) NOT NULL DEFAULT '1' COMMENT '用例类型：1-功能测试, 2-性能测试, 3-接口测试, 4-安装部署,5-配置相关,6安全相关, 7-其它',
  `tag` varchar(50) DEFAULT NULL COMMENT '用例标签',
  `module_id` int(10) NOT NULL DEFAULT '0' COMMENT '所属模块id',
  `comment` varchar(1024) DEFAULT '' COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_user` varchar(50) NOT NULL DEFAULT '' COMMENT '创建人',
  `update_user` varchar(50) NOT NULL DEFAULT '' COMMENT '更新人',
  PRIMARY KEY (`id`),
  KEY `idx_module_id` (`module_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB AUTO_INCREMENT=635649 DEFAULT CHARSET=utf8mb4 COMMENT='用例表';
```
```
SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for tc_testcase_module
-- ----------------------------
DROP TABLE IF EXISTS `tc_testcase_module`;
CREATE TABLE `tc_testcase_module` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `parent_id` int(10) NOT NULL DEFAULT '0' COMMENT '父id',
  `name` varchar(256) NOT NULL COMMENT '用例库模块名',
  `comment` varchar(1024) DEFAULT '' COMMENT '备注',
  `status` tinyint(4) DEFAULT '1' COMMENT '状态：1-正常，0-删除',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_user` varchar(50) NOT NULL DEFAULT '' COMMENT '创建人',
  `update_user` varchar(50) NOT NULL DEFAULT '' COMMENT '更新人',
  `pkey` varchar(50) DEFAULT NULL COMMENT '项目简称',
  `level` int(9) NOT NULL DEFAULT '1' COMMENT '层级，科技与信息中心为1，以下每一层+1',
  `sort` double(10,2) DEFAULT '0.00' COMMENT '排序',
  PRIMARY KEY (`id`),
  KEY `idx_parent_id` (`parent_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COMMENT='模块表';

-- ----------------------------
-- Records of tc_testcase_module
-- ----------------------------
INSERT INTO `tc_testcase_module` VALUES ('1', '0', '根模块', '', '1', '2021-07-02 10:44:39', '2021-07-02 10:44:39', 'system', 'system', null, '1', '0.00');
```
```
SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for tc_testcase_step
-- ----------------------------
DROP TABLE IF EXISTS `tc_testcase_step`;
CREATE TABLE `tc_testcase_step` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `step_number` int(10) NOT NULL DEFAULT '1' COMMENT '步骤编号',
  `step_desc` longtext COMMENT '步骤描述',
  `expect_result` longtext COMMENT '期望结果',
  `case_id` int(10) NOT NULL COMMENT '所属用例id',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_user` varchar(50) NOT NULL DEFAULT '' COMMENT '创建人',
  `update_user` varchar(50) NOT NULL DEFAULT '' COMMENT '更新人',
  PRIMARY KEY (`id`),
  KEY `idx_case_id` (`case_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用例操作步骤表';
```
```
SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for sys_appinfo
-- ----------------------------
DROP TABLE IF EXISTS `sys_appinfo`;
CREATE TABLE `sys_appinfo` (
  `ID` int(10) NOT NULL AUTO_INCREMENT,
  `APP_ID` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '应用ID',
  `APP_NAME` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '应用名称',
  `DOMAIN` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '域名',
  `THEME` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '主题',
  `IS_DELETED` int(4) NOT NULL DEFAULT '0' COMMENT '1: deleted, 0: normal',
  `CREATED_DATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UPDATE_DATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `sms_ext` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT 'SMS短信参数',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `app_id_uk` (`APP_ID`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Records of sys_appinfo
-- ----------------------------
INSERT INTO `sys_appinfo` VALUES ('1', 'test-case', null, null, null, '0', '2018-11-16 10:19:57', '2018-11-16 10:19:57', '');
```
```
SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for sys_business_line
-- ----------------------------
DROP TABLE IF EXISTS `sys_business_line`;
CREATE TABLE `sys_business_line` (
  `bid` int(19) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `bname` varchar(255) NOT NULL DEFAULT '' COMMENT '部门名称',
  `id` bigint(19) NOT NULL DEFAULT '0' COMMENT '基础资料主键id',
  `type` int(9) NOT NULL DEFAULT '30' COMMENT 'GROUP(10,"集团"),COMPANY(11,"直营公司"),\r\nCENTER(13,"中心"),BRANCH(14,"网点"),SUBSECTION(15,"分部(承包区)"),\r\nFUNCTIONAL_DEPT(16,"职能部门"), DEPT(30,"部门"),OTHER(90,"其它")',
  `parent_id` int(19) NOT NULL DEFAULT '0' COMMENT '基础资料对应父部门主键id',
  `user_count` int(9) NOT NULL DEFAULT '0' COMMENT '部门人员',
  `level` int(9) NOT NULL DEFAULT '1' COMMENT '层级，科技与信息中心为1，以下每一层+1',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `visibility` tinyint(3) NOT NULL DEFAULT '1' COMMENT '是否可见，0-不可见，1-可见',
  `status` tinyint(2) NOT NULL DEFAULT '1' COMMENT '状态：0-删除，1-正常',
  `t_id` int(10) DEFAULT '0' COMMENT '小組id',
  `notify_email_to` mediumtext COMMENT '通知邮件收件人，多个邮件地址之间分号分割',
  `notify_email_cc` mediumtext COMMENT '通知邮件收件人，多个邮件地址之间分号分割',
  PRIMARY KEY (`bid`) USING BTREE,
  KEY `idx_id` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COMMENT='基础资料组织架构';

-- ----------------------------
-- Records of sys_business_line
-- ----------------------------
INSERT INTO `sys_business_line` VALUES ('1', '保障部', '817265', '30', '0', '0', '2', '2020-10-23 09:11:13', '2021-04-07 09:22:10', '1', '1', '0', null, null);
INSERT INTO `sys_business_line` VALUES ('2', '测试部', '817323', '30', '817265', '0', '3', '2020-10-23 09:11:13', '2021-04-07 09:22:10', '1', '1', '0', null, null);
INSERT INTO `sys_business_line` VALUES ('3', '测试一部', '817348', '30', '817323', '0', '4', '2020-10-23 09:11:13', '2021-04-07 09:22:10', '1', '1', '0', null, null);
INSERT INTO `sys_business_line` VALUES ('4', '测试二部', '817349', '30', '817323', '0', '4', '2020-10-23 09:11:13', '2021-04-07 09:22:10', '1', '1', '0', null, null);
INSERT INTO `sys_business_line` VALUES ('5', '测试三部', '817350', '30', '817323', '0', '4', '2020-10-23 09:11:13', '2021-04-07 09:22:10', '1', '1', '0', null, null);
INSERT INTO `sys_business_line` VALUES ('6', '测试四部', '817351', '30', '817323', '0', '4', '2020-10-23 09:11:13', '2021-04-07 09:22:10', '1', '1', '0', null, null);
```
```
SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for sys_menuinfo
-- ----------------------------
DROP TABLE IF EXISTS `sys_menuinfo`;
CREATE TABLE `sys_menuinfo` (
  `ID` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `APP_ID` varchar(20) NOT NULL DEFAULT '' COMMENT '系统ID',
  `MENU_CODE` varchar(20) NOT NULL DEFAULT '' COMMENT '菜单编号',
  `MENU_NAME` varchar(50) NOT NULL DEFAULT '' COMMENT '菜单名称',
  `ICON` varchar(50) NOT NULL DEFAULT '' COMMENT '菜单名称',
  `URL` varchar(50) NOT NULL DEFAULT '' COMMENT '菜单地址',
  `PARENT_CODE` varchar(20) NOT NULL DEFAULT '' COMMENT '父级菜单',
  `TREE_PATH` varchar(64) NOT NULL DEFAULT '' COMMENT '路径',
  `TREE_PATH_NAME` varchar(200) NOT NULL DEFAULT '' COMMENT '路径名称',
  `SRL` int(11) NOT NULL DEFAULT '0' COMMENT '显示顺序',
  `TITLE_DESC` varchar(20) NOT NULL DEFAULT '' COMMENT '显示标题',
  `MENU_HELP` varchar(50) NOT NULL DEFAULT '' COMMENT '菜单帮助',
  `ISDELETED` int(4) NOT NULL DEFAULT '0' COMMENT '1: deleted, 0: normal',
  `CREATED_DATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UPDATE_DATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `DEFAULT_SELECT_STATUS` int(4) NOT NULL DEFAULT '0' COMMENT '默认勾选状态',
  `CHANGEABLE` int(4) NOT NULL DEFAULT '1' COMMENT '是否可变，1:可更改，0:不可更改',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UK_MENU_CODE_APP_ID` (`MENU_CODE`,`APP_ID`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COMMENT='菜单信息';

-- ----------------------------
-- Records of sys_menuinfo
-- ----------------------------
INSERT INTO `sys_menuinfo` VALUES ('1', 'test-case', 'tc_100001', '设置', 'setting', '', 'root', '', '', '1', '', '', '0', '2021-05-06 14:35:48', '2021-05-10 16:41:12', '0', '1');
INSERT INTO `sys_menuinfo` VALUES ('2', 'test-case', 'tc_100002', '权限管理', 'nav-list-o', '', 'tc_100001', '', '', '1', '', '', '0', '2021-05-06 14:35:48', '2021-05-06 15:03:12', '0', '1');
INSERT INTO `sys_menuinfo` VALUES ('3', 'test-case', 'tc_menu', '菜单管理', '', 'menu/index', 'tc_100002', '', '', '0', '', '', '0', '2021-05-06 14:35:48', '2021-05-06 15:03:22', '0', '1');
INSERT INTO `sys_menuinfo` VALUES ('4', 'test-case', 'tc_100004', '角色管理', '', 'role/list', 'tc_100002', '', '', '4', '', '', '0', '2021-05-06 14:35:48', '2021-05-06 15:28:43', '0', '1');
INSERT INTO `sys_menuinfo` VALUES ('5', 'test-case', 'tc_100005', '操作员管理', '', 'user/list', 'tc_100002', '', '', '5', '', '', '0', '2021-05-06 14:35:48', '2021-05-06 15:28:38', '0', '1');
INSERT INTO `sys_menuinfo` VALUES ('6', 'test-case', 'tc_100006', '用例库', 'warehouse', 'testlibrary', 'root', '', '', '6', '', '', '0', '2021-05-06 14:57:24', '2021-05-10 16:42:08', '0', '1');
INSERT INTO `sys_menuinfo` VALUES ('7', 'test-case', 'tc_100007', '执行计划', 'task', 'testplan', 'root', '', '', '7', '', '', '0', '2021-05-06 15:00:02', '2021-05-07 18:00:10', '0', '1');
INSERT INTO `sys_menuinfo` VALUES ('8', 'test-case', 'tc_100008', '安全中心', 'safe', 'operate/securityCenter', 'tc_100002', '', '', '8', '', '', '0', '2021-05-06 15:37:26', '2021-05-06 15:39:21', '0', '1');
```
```
SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for sys_menurolerelation
-- ----------------------------
DROP TABLE IF EXISTS `sys_menurolerelation`;
CREATE TABLE `sys_menurolerelation` (
  `ID` int(10) NOT NULL AUTO_INCREMENT,
  `APP_ID` varchar(20) NOT NULL DEFAULT '' COMMENT '系统ID',
  `MENU_CODE` varchar(50) DEFAULT '' COMMENT '菜单编码',
  `ROLE_CODE` varchar(50) DEFAULT '' COMMENT '角色编码',
  `CREATED_DATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UPDATE_DATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`ID`),
  KEY `K_MENU_CODE` (`MENU_CODE`) USING BTREE,
  KEY `K_ROLE_CODE` (`ROLE_CODE`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=18097 DEFAULT CHARSET=utf8mb4 COMMENT='菜单与角色关系信息';

-- ----------------------------
-- Records of sys_menurolerelation
-- ----------------------------
INSERT INTO `sys_menurolerelation` VALUES ('18089', 'test-case', 'tc_100007', '100044', '2021-05-11 16:38:42', '2021-05-11 16:38:42');
INSERT INTO `sys_menurolerelation` VALUES ('18090', 'test-case', 'tc_100006', '100044', '2021-05-11 16:38:42', '2021-05-11 16:38:42');
INSERT INTO `sys_menurolerelation` VALUES ('18091', 'test-case', 'tc_100007', '100045', '2021-07-02 10:53:52', '2021-07-02 10:53:52');
INSERT INTO `sys_menurolerelation` VALUES ('18092', 'test-case', 'tc_100006', '100045', '2021-07-02 10:53:52', '2021-07-02 10:53:52');
INSERT INTO `sys_menurolerelation` VALUES ('18093', 'test-case', 'tc_100001', '100045', '2021-07-02 10:53:52', '2021-07-02 10:53:52');
INSERT INTO `sys_menurolerelation` VALUES ('18094', 'test-case', 'tc_100002', '100045', '2021-07-02 10:53:52', '2021-07-02 10:53:52');
INSERT INTO `sys_menurolerelation` VALUES ('18095', 'test-case', 'tc_100008', '100045', '2021-07-02 10:53:52', '2021-07-02 10:53:52');
INSERT INTO `sys_menurolerelation` VALUES ('18096', 'test-case', 'tc_100005', '100045', '2021-07-02 10:53:52', '2021-07-02 10:53:52');
```
```
SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for sys_project
-- ----------------------------
DROP TABLE IF EXISTS `sys_project`;
CREATE TABLE `sys_project` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `b_ids` int(10) DEFAULT NULL COMMENT '业务线id',
  `pname` varchar(50) NOT NULL COMMENT '项目名',
  `prd_lead` varchar(50) DEFAULT NULL COMMENT '产品负责人',
  `dev_lead` varchar(50) DEFAULT NULL COMMENT '开发负责人',
  `qa_lead` varchar(50) DEFAULT NULL COMMENT '测试负责人',
  `description` varchar(512) DEFAULT NULL COMMENT '描述',
  `pkey` varchar(50) DEFAULT NULL COMMENT '项目简称',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `status` tinyint(2) NOT NULL DEFAULT '1' COMMENT '状态：0-删除，1-正常',
  `type` tinyint(2) NOT NULL DEFAULT '0' COMMENT '项目类型：0-研发类，1-基础平台',
  `notify_email_to` varchar(1024) DEFAULT NULL COMMENT '通知邮件收件人，多个邮件地址之间分号分割',
  `notify_email_cc` varchar(1024) DEFAULT NULL COMMENT '通知邮件抄送人，多个邮件地址之间分号分割',
  `jp_id` decimal(18,0) NOT NULL,
  `category` tinyint(2) NOT NULL DEFAULT '5' COMMENT '项目类别：3-中心重点项目，4-中心级项目，5-其他项目',
  `total_man_day` decimal(10,4) NOT NULL DEFAULT '0.0000' COMMENT '项目总人天：手动在数据库中添加 （只有基础平台类的才会有值）',
  `tlp_id` int(10) unsigned DEFAULT NULL COMMENT 'testlink项目id',
  `user_exp_notify_email_to` varchar(512) DEFAULT NULL COMMENT '用户体验通知邮件收件人，多个邮件地址之间分号分割',
  `user_exp_notify_email_cc` varchar(512) DEFAULT NULL COMMENT '用户体验通知邮件抄送人，多个邮件地址之间分号分割',
  `f_p_id` int(10) DEFAULT NULL COMMENT '父项目id',
  `pms_project_id` int(10) DEFAULT NULL COMMENT 'pms中project主键id',
  `pms_project_name` varchar(255) DEFAULT NULL COMMENT 'pms中project名称',
  `pms_department_id` int(10) DEFAULT NULL COMMENT 'pms中所属部门(产品线)主键id',
  `enable` tinyint(2) DEFAULT '1' COMMENT '是否启用 1是0否',
  `forbidden` tinyint(2) DEFAULT '0' COMMENT '是否禁用（1:是，0:否）',
  `b_id` bigint(19) DEFAULT NULL COMMENT '组织架构id',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_pms_project_id` (`pms_project_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of sys_project
-- ----------------------------
INSERT INTO `sys_project` VALUES ('1', '11', '自动化测试', '产品负责人', '开发负责人', '测试负责人', '', 'ZDHCS', '2021-01-26 14:38:47', '2021-07-02 13:05:46', '1', '0', null, null, '10555', '5', '0.0000', '274524', null, null, null, null, null, null, null, null, '817323');
```
```
SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for sys_rolesinfo
-- ----------------------------
DROP TABLE IF EXISTS `sys_rolesinfo`;
CREATE TABLE `sys_rolesinfo` (
  `ID` int(10) NOT NULL AUTO_INCREMENT,
  `APP_ID` varchar(20) NOT NULL DEFAULT '' COMMENT '系统ID',
  `ROLE_CODE` varchar(50) DEFAULT '' COMMENT '角色编号',
  `ROLE_NAME` varchar(50) DEFAULT '' COMMENT '角色名称',
  `SYSTEM_ROLE` varchar(10) DEFAULT '' COMMENT '是否为系统角色',
  `STATUS` varchar(4) NOT NULL DEFAULT '0' COMMENT '标记状态0不可用1可用',
  `CREATE_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UPDATE_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `CREATOR_CHAIN` varchar(200) NOT NULL DEFAULT '' COMMENT '创建者层级链',
  `ROLE_CREATE_OPER` varchar(50) DEFAULT '',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UK_ROLE_CODE_APP_ID` (`ROLE_CODE`,`APP_ID`) USING BTREE,
  UNIQUE KEY `UK_ROLE_NAME_APP_ID` (`ROLE_NAME`,`APP_ID`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=46 DEFAULT CHARSET=utf8mb4 COMMENT='角色信息表';

-- ----------------------------
-- Records of sys_rolesinfo
-- ----------------------------
INSERT INTO `sys_rolesinfo` VALUES ('1', 'test-case', '00001', '超级管理员', '1', '1', '2018-11-07 14:20:01', '2021-05-06 14:38:14', 'TOP', null);
INSERT INTO `sys_rolesinfo` VALUES ('44', 'test-case', '100044', '测试专员', '0', '1', '2021-05-06 15:09:11', '2021-05-06 15:17:07', 'TOP_00001', '');
INSERT INTO `sys_rolesinfo` VALUES ('45', 'test-case', '100045', '测试经理', '0', '1', '2021-05-06 15:16:45', '2021-05-06 15:16:46', 'TOP_00001', '');
```
```
SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for sys_uimetadata
-- ----------------------------
DROP TABLE IF EXISTS `sys_uimetadata`;
CREATE TABLE `sys_uimetadata` (
  `ID` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `APP_ID` varchar(20) NOT NULL DEFAULT '' COMMENT '系统ID',
  `MENU_CODE` varchar(20) NOT NULL DEFAULT '' COMMENT '菜单编号',
  `CONTROL_ID` varchar(50) NOT NULL DEFAULT '' COMMENT '控件ID',
  `CONTROL_NAME` varchar(50) NOT NULL DEFAULT '' COMMENT '控件名称',
  `CONTROL_TYPE` varchar(20) NOT NULL DEFAULT '' COMMENT '控件类型: field 字段，oper 操作',
  `ISDELETED` int(4) NOT NULL DEFAULT '0' COMMENT '1: deleted, 0: normal',
  `CREATED_DATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UPDATE_DATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COMMENT='界面元素数据';

-- ----------------------------
-- Records of sys_uimetadata
-- ----------------------------
INSERT INTO `sys_uimetadata` VALUES ('1', 'test-case', 'tc_menu', 'menu-page-index/edit', '编辑', 'oper', '0', '2021-05-06 14:42:49', '2021-05-06 15:33:04');
INSERT INTO `sys_uimetadata` VALUES ('2', 'test-case', 'tc_menu', 'menu-page-index/add', '增加', 'oper', '0', '2021-05-06 14:42:49', '2021-05-06 15:33:05');
INSERT INTO `sys_uimetadata` VALUES ('3', 'test-case', 'tc_menu', 'menu-page-index/del', '删除', 'oper', '0', '2021-05-06 14:42:49', '2021-05-06 15:33:06');
INSERT INTO `sys_uimetadata` VALUES ('4', 'test-case', 'tc_100004', 'role-page-list/edit', '编辑', 'oper', '0', '2021-05-06 14:42:49', '2021-05-06 15:33:11');
INSERT INTO `sys_uimetadata` VALUES ('5', 'test-case', 'tc_100004', 'role-page-list/add', '增加', 'oper', '0', '2021-05-06 14:42:49', '2021-05-06 15:33:11');
INSERT INTO `sys_uimetadata` VALUES ('6', 'test-case', 'tc_100004', 'role-page-list/del', '删除', 'oper', '0', '2021-05-06 14:42:49', '2021-05-06 15:33:13');
INSERT INTO `sys_uimetadata` VALUES ('7', 'test-case', 'tc_100005', 'user-page-list/edit', '编辑', 'oper', '0', '2021-05-06 14:42:49', '2021-05-06 15:33:15');
INSERT INTO `sys_uimetadata` VALUES ('8', 'test-case', 'tc_100005', 'user-page-list/add', '增加', 'oper', '0', '2021-05-06 14:42:49', '2021-05-06 15:33:16');
INSERT INTO `sys_uimetadata` VALUES ('9', 'test-case', 'tc_100005', 'user-page-list/del', '删除', 'oper', '0', '2021-05-06 14:42:49', '2021-05-06 15:33:17');
INSERT INTO `sys_uimetadata` VALUES ('10', 'test-case', 'tc_menu', 'menu-page-index/url', '菜单URL', 'field', '0', '2021-05-06 14:42:49', '2021-05-06 15:33:18');
INSERT INTO `sys_uimetadata` VALUES ('11', 'test-case', 'tc_100006', 'testlibrary/upload', '导入用例', 'oper', '0', '2021-05-11 15:16:27', '2021-05-11 15:28:48');
INSERT INTO `sys_uimetadata` VALUES ('12', 'test-case', 'tc_100006', 'testlibrary/move', '移动用例', 'oper', '0', '2021-05-11 15:20:58', '2021-05-11 15:28:59');
INSERT INTO `sys_uimetadata` VALUES ('13', 'test-case', 'tc_100006', 'testlibrary/del', '删除用例', 'oper', '0', '2021-05-11 15:21:42', '2021-05-11 15:29:11');
INSERT INTO `sys_uimetadata` VALUES ('14', 'test-case', 'tc_100006', 'testlibrary/add', '新建用例', 'oper', '0', '2021-05-11 15:22:04', '2021-05-11 15:29:23');
```
```
SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for sys_userinfo
-- ----------------------------
DROP TABLE IF EXISTS `sys_userinfo`;
CREATE TABLE `sys_userinfo` (
  `ID` int(10) NOT NULL AUTO_INCREMENT,
  `USER_ID` varchar(50) DEFAULT NULL COMMENT '用户号',
  `USER_NAME` varchar(50) DEFAULT NULL COMMENT '用户名',
  `NICK_NAME` varchar(50) DEFAULT NULL COMMENT '登录用户名',
  `USER_PWD` varchar(50) DEFAULT NULL COMMENT '登录密码',
  `USER_STATUS` varchar(20) DEFAULT NULL COMMENT '用户状态 1 初始化  2 正常 3.锁定，4。禁用  9 注销',
  `MOBILE` varchar(20) DEFAULT NULL COMMENT '手机号',
  `EMAIL` varchar(50) DEFAULT NULL COMMENT '邮箱',
  `START_DATE` datetime DEFAULT NULL COMMENT '有效开始时间 diablo',
  `END_DATE` datetime DEFAULT NULL COMMENT '有效结束时间 diablo',
  `DATE_CREATED` datetime DEFAULT NULL COMMENT '创建时间',
  `DATE_UPDATED` datetime DEFAULT NULL COMMENT '更新时间',
  `APP_ID` varchar(32) DEFAULT NULL,
  `LOGIN_ERROR_TIMES` int(10) DEFAULT '0',
  `VALID_KEY` varchar(100) DEFAULT NULL COMMENT '动态校验码密钥',
  `org` varchar(64) DEFAULT NULL,
  `USER_CREATE_OPER` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `index_user_id` (`USER_ID`) USING BTREE,
  KEY `index_mobile` (`MOBILE`) USING BTREE,
  KEY `index_date_create` (`DATE_CREATED`) USING BTREE,
  KEY `index_user_name` (`USER_NAME`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=297 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of sys_userinfo
-- ----------------------------
INSERT INTO `sys_userinfo` VALUES ('1', 'tc1000000001', 'admin', '超级管理员', 'c6b12d4e92ee89684de2ce3ff61ca7d914c7694c', '2', '13888888888', 'admin@qq.com', '2018-11-07 14:20:01', '2018-11-07 14:20:01', '2018-11-07 14:20:01', '2021-05-12 09:54:57', 'test-case', '0', 'DEV', null, null);
```
```
SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for sys_userrolesrelation
-- ----------------------------
DROP TABLE IF EXISTS `sys_userrolesrelation`;
CREATE TABLE `sys_userrolesrelation` (
  `ID` int(10) NOT NULL AUTO_INCREMENT,
  `APP_ID` varchar(20) NOT NULL DEFAULT '' COMMENT '系统ID',
  `USER_ID` varchar(50) DEFAULT '' COMMENT '用户号',
  `ROLE_ID` varchar(50) DEFAULT '' COMMENT '角色ID',
  PRIMARY KEY (`ID`),
  KEY `index_menu_id` (`USER_ID`,`ROLE_ID`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=296 DEFAULT CHARSET=utf8mb4 COMMENT='用户角色关系';

-- ----------------------------
-- Records of sys_userrolesrelation
-- ----------------------------
INSERT INTO `sys_userrolesrelation` VALUES ('1', 'test-case', 'tc1000000001', '00001');
```
```
SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for sys_version
-- ----------------------------
DROP TABLE IF EXISTS `sys_version`;
CREATE TABLE `sys_version` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '序号',
  `tp_id` int(10) DEFAULT NULL COMMENT '测试计划id',
  `p_id` int(10) NOT NULL COMMENT '项目序号',
  `name` varchar(128) NOT NULL COMMENT '版本名',
  `status` tinyint(4) DEFAULT NULL COMMENT '状态：1-未发布，2-已发布，3-删除 4-归档，5-关闭',
  `jpv_id` decimal(18,0) DEFAULT NULL COMMENT 'jira projectversion表序号',
  `str_id` int(10) DEFAULT NULL COMMENT '简易项目测试报告序号',
  `type` tinyint(4) DEFAULT NULL COMMENT '版本类型，0-常规版本，1-非常规版本',
  `need_modify` tinyint(2) DEFAULT NULL COMMENT '是否发生了变更，0-否，1-是，发生变更时（1）只允许发送准入，没有发生变更时发送准入不会同步pms',
  `editable` tinyint(2) DEFAULT '0' COMMENT '0-已确认版本计划 1-未确认',
  `ext_id` int(10) DEFAULT NULL COMMENT '外采项目测试报告序号',
  `develop_start_date` date DEFAULT NULL COMMENT '开发开始时间',
  `develop_end_date` date DEFAULT NULL COMMENT '开发结束时间',
  `test_start_date` date DEFAULT NULL COMMENT '测试开始时间',
  `test_end_date` date DEFAULT NULL COMMENT '测试结束时间',
  `case_tag` varchar(50) DEFAULT NULL COMMENT '用例标签',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_tp_id` (`tp_id`),
  KEY `idx_str_id` (`str_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of sys_version
-- ----------------------------
INSERT INTO `sys_version` VALUES ('1', null, '1', 'ZDHCS-20210414-001', '1', null, null, '0', null, '0', null, null, null, '2021-04-16', '2021-04-16', null, '2021-04-14 14:27:21', '2021-07-02 13:11:28');
```

# 配置、启动
```bash
# BaseDruidConfig配置
username、password通过DESCodec.encode()加密

# 本地开发 启动项目
1. init sql
2. 所有IP、账户、密码相关请替换
3. 启动命令：
nohup java -jar -Dspring.profiles.active=test -Xmx1024m -Xms1024m -Xmn512m -Xss512k zto-test-case.jar &  

```
### swagger
* http://localhost:8081/testcase/swagger-ui.html#/login

# Note: 欢迎一起开发，如有问题请联系：中通科技-技术保障部-测试开发团队
## 联系方式：
### xujianfeng8105@zto.com
### wuxiaochen@zto.com
### gongqiang123@zto.com
