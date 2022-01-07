SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
  `id`                   BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '菜单id(主键)',
  `level`                VARCHAR(1) NOT NULL COMMENT '菜单级别：1一级菜单，2二级菜单，3三级菜单，4四级菜单',
  `parent_id`            BIGINT(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '父级id',
  `name`                 VARCHAR(30) NOT NULL COMMENT '菜单名称',
  `component`            VARCHAR(100) DEFAULT '' COMMENT '父组件',
  `router_path`          VARCHAR(100) DEFAULT '' COMMENT '路由路径',
  `router_name`          VARCHAR(100) DEFAULT '' COMMENT '路由名称',
  `redirect`             VARCHAR(100) DEFAULT '' COMMENT '重定向',
  `is_hide`              VARCHAR(1) NOT NULL DEFAULT '0' COMMENT '是否隐藏：0显示，1隐藏',
  `rank`                 INT(11) UNSIGNED COMMENT '排序',
  `del_flag`             VARCHAR(1) NOT NULL DEFAULT '0' COMMENT '删除标记-平台：0否，1是',
  `create_time`          DATETIME NOT NULL COMMENT '创建时间',
  `update_time`          DATETIME NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=UTF8MB4 COLLATE=utf8mb4_general_ci COMMENT='菜单信息表';

-- ----------------------------
-- Table structure for sys_perm
-- ----------------------------
DROP TABLE IF EXISTS `sys_perm`;
CREATE TABLE `sys_perm` (
  `id`                   BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '权限id(主键)',
  `name`                 VARCHAR(100) NOT NULL COMMENT '权限名称',
  `expression`           VARCHAR(100) NOT NULL COMMENT '表达式',
  `del_flag`             VARCHAR(1) NOT NULL DEFAULT '0' COMMENT '删除标记-平台：0否，1是',
  `create_time`          DATETIME NOT NULL COMMENT '创建时间',
  `update_time`          DATETIME NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=UTF8MB4 COLLATE=utf8mb4_general_ci COMMENT='权限信息表';

-- ----------------------------
-- Table structure for sys_perm_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_perm_menu`;
CREATE TABLE `sys_perm_menu` (
  `id`                   BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '权限菜单id(主键)',
  `perm_id`              BIGINT(20) UNSIGNED NOT NULL COMMENT '权限id',
  `perm_name`            VARCHAR(30) NOT NULL COMMENT '权限名称',
  `menu_id`              BIGINT(20) UNSIGNED NOT NULL COMMENT '菜单id',
  `menu_name`            VARCHAR(30) NOT NULL COMMENT '菜单名称',
  `del_flag`             VARCHAR(1) NOT NULL DEFAULT '0' COMMENT '删除标记-平台：0否，1是',
  `create_time`          DATETIME NOT NULL COMMENT '创建时间',
  `update_time`          DATETIME NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=UTF8MB4 COLLATE=utf8mb4_general_ci COMMENT='权限菜单信息表';

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`(
  `id`                   BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '角色id(主键)',
  `name`                 VARCHAR(30) NOT NULL COMMENT '角色名称',
  `description`          VARCHAR(255) NOT NULL COMMENT '职务描述',
  `rank`                 INT(11) UNSIGNED COMMENT '排序',
  `del_flag`             VARCHAR(1) NOT NULL DEFAULT '0' COMMENT '删除标记-平台：0否，1是',
  `create_time`          DATETIME NOT NULL COMMENT '创建时间',
  `update_time`          DATETIME NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=2 DEFAULT CHARSET=UTF8MB4 COLLATE=utf8mb4_general_ci COMMENT='角色信息表';

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` (id, name, description, create_time, update_time)
  VALUE (1, '超级管理员', '管理公司业务、人员流动', sysdate(), sysdate());

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`(
 `id`                   BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '角色菜单id(主键)',
 `role_id`              BIGINT(20) UNSIGNED NOT NULL COMMENT '角色id',
 `role_name`            VARCHAR(30) NOT NULL COMMENT '角色名称',
 `menu_id`              BIGINT(20) UNSIGNED NOT NULL COMMENT '菜单id',
 `menu_name`            VARCHAR(30) NOT NULL COMMENT '菜单名称',
 `del_flag`             VARCHAR(1) NOT NULL DEFAULT '0' COMMENT '删除标记-平台：0否，1是',
 `create_time`          DATETIME NOT NULL COMMENT '创建时间',
 `update_time`          DATETIME NOT NULL COMMENT '更新时间',
 PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=UTF8MB4 COLLATE=utf8mb4_general_ci COMMENT='角色菜单信息表';

-- ----------------------------
-- Table structure for sys_role_perm
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_perm`;
CREATE TABLE `sys_role_perm` (
  `id`                   BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '角色权限id(主键)',
  `role_id`              BIGINT(20) UNSIGNED NOT NULL COMMENT '角色id',
  `perm_id`              BIGINT(20) UNSIGNED NOT NULL COMMENT '权限id',
  `perm_name`            VARCHAR(100) NOT NULL COMMENT '权限名称',
  `expression`           VARCHAR(100) NOT NULL COMMENT '表达式',
  `del_flag`             VARCHAR(1) NOT NULL DEFAULT '0' COMMENT '删除标记-平台：0否，1是',
  `create_time`          DATETIME NOT NULL COMMENT '创建时间',
  `update_time`          DATETIME NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=UTF8MB4 COLLATE=utf8mb4_general_ci COMMENT='角色权限信息表';

-- ----------------------------
-- Table structure for sys_role_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_user`;
CREATE TABLE `sys_role_user`(
  `id`                   BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '角色用户id(主键)',
  `role_id`              BIGINT(20) UNSIGNED NOT NULL COMMENT '角色id',
  `role_name`            VARCHAR(30) NOT NULL COMMENT '角色名称',
  `account_id`           BIGINT(20) UNSIGNED NOT NULL COMMENT '账号id',
  `account_name`         VARCHAR(30) NOT NULL COMMENT '账号名称',
  `rank`                 INT(11) UNSIGNED COMMENT '排序',
  `del_flag`             VARCHAR(1) NOT NULL DEFAULT '0' COMMENT '删除标记-平台：0否，1是',
  `create_time`          DATETIME NOT NULL COMMENT '创建时间',
  `update_time`          DATETIME NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=2 DEFAULT CHARSET=UTF8MB4 COLLATE=utf8mb4_general_ci COMMENT='角色用户表';

-- ----------------------------
-- Records of sys_role_user
-- ----------------------------
INSERT INTO `sys_role_user` (id, role_id, role_name, account_id, account_name, create_time, update_time)
  VALUE (1, 1, '超级管理员', 1, 'admin', sysdate(), sysdate());

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id`                   BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '账号id(主键)',
  `dept_id`              BIGINT(20) UNSIGNED NOT NULL COMMENT '部门id',
  `dept_name`            VARCHAR(30) NOT NULL COMMENT '部门名称',
  `account_name`         VARCHAR(30) NOT NULL COMMENT '账号名称',
  `truename`             VARCHAR(30) NOT NULL COMMENT '真实姓名',
  `password`             VARCHAR(255) NOT NULL COMMENT '密码',
  `avatar`               VARCHAR(255) DEFAULT '' COMMENT '头像',
  `is_admin`             VARCHAR(1) NOT NULL DEFAULT '0' COMMENT '超级管理员：0否，1是',
  `login_status`         VARCHAR(1) NOT NULL DEFAULT '1' COMMENT '登录状态：0冻结，1正常',
  `rank`                 INT(11) UNSIGNED COMMENT '排序',
  `del_flag`             VARCHAR(1) NOT NULL DEFAULT '0' COMMENT '删除标记-平台：0否，1是',
  `create_time`          DATETIME NOT NULL COMMENT '创建时间',
  `update_time`          DATETIME NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=2 DEFAULT CHARSET=UTF8MB4 COLLATE=utf8mb4_general_ci COMMENT='账号信息表';

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` (id, dept_id, dept_name, account_name, truename, password, is_admin, create_time, update_time)
  VALUE (1, 1, '总经办', 'admin', '超级管理员', '7c4a8d09ca3762af61e59520943dc26494f8941b', '1', sysdate(), sysdate());

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept` (
  `id`                   BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '部门id(主键)',
  `name`                 VARCHAR(30) NOT NULL COMMENT '部门名称',
  `description`          VARCHAR(100) NOT NULL COMMENT '部门描述',
  `rank`                 INT(11) UNSIGNED COMMENT '排序',
  `del_flag`             VARCHAR(1) NOT NULL DEFAULT '0' COMMENT '删除标记-平台：0否，1是',
  `create_time`          DATETIME NOT NULL COMMENT '创建时间',
  `update_time`          DATETIME NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=2 DEFAULT CHARSET=UTF8MB4 COLLATE=utf8mb4_general_ci COMMENT='部门信息表';

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
INSERT INTO `sys_dept` (id, name, description, create_time, update_time) VALUE (1, '总经办', '负责总公司运作', sysdate(), sysdate());
