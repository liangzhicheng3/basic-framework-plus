DROP TABLE IF EXISTS `sys_config`;
CREATE TABLE `sys_config` (
`id`        BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
`key_name`  VARCHAR(50) NOT NULL COMMENT 'key',
`value`     VARCHAR(2000) NOT NULL COMMENT '值',
`status`    MEDIUMINT(1) UNSIGNED DEFAULT '1' COMMENT '状态：0隐藏，1显示',
`remark`    VARCHAR(500) DEFAULT '' COMMENT '备注',
PRIMARY KEY (`id`),
UNIQUE KEY `key_name` (`key_name`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='配置信息表';

INSERT INTO `sys_config` VALUES (1, 'cloud_storage_config_key', '{\"qiniuAccessKey\":\"1\",\"qiniuSecretKey\":\"1\",\"qiniuDomain\":\"1\",\"qiniuPrefix\":\"\",\"qiniuBucketName\":\"1\",\"aliEndPoint\":\"1\",\"aliAccessKey\":\"1\",\"aliSecretKey\":\"1\",\"aliDomain\":\"http://xxx.com\",\"aliPrefix\":\"upload\",\"aliBucketName\":\"shop-mall\",\"tencentAccessKey\":\"1\",\"tencentSecretId\":\"1\",\"tencentSecretKey\":\"1\",\"tencentDomain\":\"http://xxx.com\",\"tencentPrefix\":\"upload\",\"tencentBucketName\":\"shop-mall\",\"tencentRegion\":\"\",\"type\":1}', '0', '云存储配置信息');
INSERT INTO `sys_config` VALUES (2, 'push_config_key', '{\"xingeIosAccessKey\":\"1\",\"xingeIosSecretKey\":\"1\",\"xingeAndroidAccessKey\":\"1\",\"xingeAndroidSecretKey\":\"\",\"aliAccessKey\":\"1\",\"aliSecretKey\":\"1\",\"aliIosAppKey\":\"1\",\"aliAndroidAppKey\":\"1\",\"pushTokenKeyMap\":\"push_token_key_map\",\"pushTypeKeyMap\":\"push_type_key_map\",\"type\":1}', '0', '推送配置信息');
