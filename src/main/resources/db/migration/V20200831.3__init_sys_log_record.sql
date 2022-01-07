DROP TABLE IF EXISTS `sys_log_record`;
CREATE TABLE `sys_log_record` (
`id`           BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
`user_id`      VARCHAR(50) DEFAULT '' COMMENT '用户id',
`operate`      VARCHAR(50) DEFAULT '' COMMENT '操作记录',
`method`       VARCHAR(200) DEFAULT '' COMMENT '请求方法',
`params`       VARCHAR(5000) DEFAULT '' COMMENT '请求参数',
`ip`           VARCHAR(64) DEFAULT '' COMMENT 'ip地址',
`create_time`  DATETIME DEFAULT NULL COMMENT '创建时间',
PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='日志记录信息表';
