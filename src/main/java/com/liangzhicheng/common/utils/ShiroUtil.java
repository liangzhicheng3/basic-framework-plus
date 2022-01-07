package com.liangzhicheng.common.utils;

import com.liangzhicheng.modules.entity.SysUserEntity;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

/**
 * Shiro权限工具类
 * @author liangzhicheng
 */
public class ShiroUtil {

    public static Subject getSubject() {
        return SecurityUtils.getSubject();
    }

    public static SysUserEntity getCurrentUser(){
        return (SysUserEntity) getSubject().getPrincipal();
    }

    public static String getUserId(){
        return getCurrentUser().getId();
    }

}
