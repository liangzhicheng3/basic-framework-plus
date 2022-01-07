package com.liangzhicheng.common.push.properties;

import com.liangzhicheng.common.push.group.AliPushGroup;
import com.liangzhicheng.common.push.group.XingePushGroup;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 推送实体类
 * @author liangzhicheng
 */
@Data
public class Push implements Serializable {

    private static final long serialVersionUID = 1L;

    //类型：1信鸽，2阿里
    @Range(min = 1, max = 2, message = "类型有误")
    private Integer type;

    //信鸽Ios AccessKey
    @NotBlank(message = "信鸽Ios AccessKey不能为空", groups = XingePushGroup.class)
    private String xingeIosAccessKey;
    //信鸽Ios SecretKey
    @NotBlank(message = "信鸽Ios SecretKey", groups = XingePushGroup.class)
    private String xingeIosSecretKey;
    //信鸽Android AccessKey
    @NotBlank(message = "信鸽Android AccessKey", groups = XingePushGroup.class)
    private String xingeAndroidAccessKey;
    //信鸽Android SecretKey
    @NotBlank(message = "信鸽Android SecretKey", groups = XingePushGroup.class)
    private String xingeAndroidSecretKey;

    //阿里AccessKey
    @NotBlank(message = "阿里AccessKey不能为空", groups = AliPushGroup.class)
    private String aliAccessKey;
    //阿里Ios SecretKey
    @NotBlank(message = "阿里SecretKey不能为空", groups = AliPushGroup.class)
    private String aliSecretKey;
    //阿里appKey Ios
    @NotBlank(message = "阿里Ios appKey不能为空", groups = AliPushGroup.class)
    private Long aliIosAppKey;
    //阿里appKey Android
    @NotBlank(message = "阿里Android appKey不能为空", groups = AliPushGroup.class)
    private Long aliAndroidAppKey;


    //推送token key
    @NotBlank(message = "推送token key map不能为空", groups = {XingePushGroup.class, AliPushGroup.class,})
    private String pushTokenKeyMap;
    //推送类型 key
    @NotBlank(message = "信鸽推送类型key map不能为空", groups = XingePushGroup.class)
    private String pushTypeKeyMap;

}
