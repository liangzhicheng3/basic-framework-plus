package com.liangzhicheng.config.shiro;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.liangzhicheng.common.bean.RedisBean;
import com.liangzhicheng.common.enums.StringEnum;
import com.liangzhicheng.common.utils.ToolUtil;
import com.liangzhicheng.modules.entity.SysUserEntity;
import com.liangzhicheng.modules.service.ISysUserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Set;

/**
 * Shiro权限初始化类
 * @author liangzhicheng
 */
@Component
public class SysRealm extends AuthorizingRealm {

    @Resource
    private ISysUserService sysUserService;
    @Resource
    private RedisBean redisBean;

    /**
     * realm中使用指定的凭证匹配器完成密码匹配操作
     * @param credentialsMatcher
     */
    @Override
    public void setCredentialsMatcher(CredentialsMatcher credentialsMatcher) {
        super.setCredentialsMatcher(credentialsMatcher);
    }

    /**
     * 用户登录认证
     * @param token
     * @return AuthenticationInfo
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        //登录请求头参数携带，通过token获取用户名
        String accountName = token.getPrincipal().toString();
        String password = new String((char[]) token.getCredentials());
        SysUserEntity user = sysUserService.getOne(new QueryWrapper<SysUserEntity>()
                .eq("account_name", accountName)
                .eq(StringEnum.DEL_FLAG.getValue(), StringEnum.ZERO.getValue()));
        if(ToolUtil.isNull(user) || !user.getPassword().equals(password)){
            throw new UnknownAccountException("账号或密码错误");
        }
        if(!StringEnum.ONE.getValue().equals(user.getLoginStatus())){
            throw new DisabledAccountException("账号已被禁用");
        }
        return new SimpleAuthenticationInfo(user, user.getPassword(), getName());
    }

    /**
     * 角色，权限相关设置
     * @param pc
     * @return AuthorizationInfo
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection pc) {
        SysUserEntity user = (SysUserEntity) pc.getPrimaryPrincipal();
        if(ToolUtil.isNull(user)){
            return null;
        }
        Map<String, Object> roleMap = redisBean.getRoleMap();
        Map<String, Object> permMap = redisBean.getPermMap();
        Set<String> roles = (Set<String>) roleMap.get(user.getId());
        Set<String> perms = (Set<String>) permMap.get(user.getId());
        //创建SimpleAuthorizationInfo，将角色和权限存储
        SimpleAuthorizationInfo authInfo = new SimpleAuthorizationInfo();
        authInfo.setRoles(roles);
        authInfo.setStringPermissions(perms);
        return authInfo;
    }

}


