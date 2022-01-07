package com.liangzhicheng.common.oss.properties;

import com.liangzhicheng.common.oss.group.AliCloudGroup;
import com.liangzhicheng.common.oss.group.QiniuCloudGroup;
import com.liangzhicheng.common.oss.group.ServerGroup;
import com.liangzhicheng.common.oss.group.TencentCloudGroup;
import lombok.Data;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 云存储实体类
 * @author liangzhicheng
 */
@Data
public class CloudStorage implements Serializable {

    private static final long serialVersionUID = 1L;

    //类型：1七牛云，2阿里云，3腾讯云，4服务器存储
    @Range(min = 1, max = 4, message = "类型有误")
    private Integer type;

    //七牛云ACCESS_KEY
    @NotBlank(message = "七牛云AccessKey不能为空", groups = QiniuCloudGroup.class)
    private String qiniuAccessKey;
    //七牛云SECRET_KEY
    @NotBlank(message = "七牛云SecretKey不能为空", groups = QiniuCloudGroup.class)
    private String qiniuSecretKey;
    //七牛云绑定的域名
    @NotBlank(message = "七牛云绑定的域名不能为空", groups = QiniuCloudGroup.class)
    @URL(message = "七牛云绑定的域名格式不正确", groups = QiniuCloudGroup.class)
    private String qiniuDomain;
    //七牛云路径前缀
    private String qiniuPrefix;
    //七牛云存储空间名
    @NotBlank(message = "七牛云空间名不能为空", groups = QiniuCloudGroup.class)
    private String qiniuBucketName;

    //阿里云EndPoint
    @NotBlank(message = "阿里云EndPoint不能为空", groups = AliCloudGroup.class)
    private String aliEndPoint;
    //阿里云AccessKeyId
    @NotBlank(message = "阿里云AccessKey不能为空", groups = AliCloudGroup.class)
    private String aliAccessKey;
    //阿里云AccessKeySecret
    @NotBlank(message = "阿里云SecretKey不能为空", groups = AliCloudGroup.class)
    private String aliSecretKey;
    //阿里云绑定的域名
    @NotBlank(message = "阿里云绑定的域名不能为空", groups = AliCloudGroup.class)
    @URL(message = "阿里云绑定的域名格式不正确", groups = AliCloudGroup.class)
    private String aliDomain;
    //阿里云路径前缀
    private String aliPrefix;
    //阿里云存储空间名
    @NotBlank(message = "阿里云空间名不能为空", groups = AliCloudGroup.class)
    private String aliBucketName;

    //腾讯云AppId
    @NotNull(message = "腾讯云AccessKey不能为空", groups = TencentCloudGroup.class)
    private Integer tencentAccessKey;
    //腾讯云SecretId
    @NotBlank(message = "腾讯云SecretId不能为空", groups = TencentCloudGroup.class)
    private String tencentSecretId;
    //腾讯云SecretKey
    @NotBlank(message = "腾讯云SecretKey不能为空", groups = TencentCloudGroup.class)
    private String tencentSecretKey;
    //腾讯云绑定的域名
    @NotBlank(message = "腾讯云绑定的域名不能为空", groups = TencentCloudGroup.class)
    @URL(message = "腾讯云绑定的域名格式不正确", groups = TencentCloudGroup.class)
    private String tencentDomain;
    //腾讯云路径前缀
    private String tencentPrefix;
    //腾讯云存储空间名
    @NotBlank(message = "腾讯云空间名不能为空", groups = TencentCloudGroup.class)
    private String tencentBucketName;
    //腾讯云COS所属地区
    @NotBlank(message = "所属地区不能为空", groups = TencentCloudGroup.class)
    private String tencentRegion;

    //服务器存储
    @NotBlank(message = "本地存储路径不能为空", groups = ServerGroup.class)
    private String serverPath;
    @NotBlank(message = "本地存储代理服务器不能为空", groups = ServerGroup.class)
    private String serverProxy;

}
