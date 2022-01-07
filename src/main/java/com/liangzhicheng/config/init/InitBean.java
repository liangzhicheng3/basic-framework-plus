package com.liangzhicheng.config.init;

import com.liangzhicheng.common.bean.RedisBean;
import com.liangzhicheng.common.utils.ListUtil;
import com.liangzhicheng.common.utils.PrintUtil;
import com.liangzhicheng.common.utils.ToolUtil;
import com.liangzhicheng.modules.entity.vo.SysMenuVO;
import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 初始化系统菜单、角色、权限 bean
 * @author liangzhicheng
 */
@Data
@Component
public class InitBean implements InitializingBean {

    @Resource
    private RedisBean redisBean;

    @Override
    public void afterPropertiesSet() throws Exception {
        initSys();
    }

    private void initSys(){
        PrintUtil.info("[InitializingBean初始化] 系统菜单、角色、权限初始化执行开始 >>>>>>");
        //系统权限菜单列表初始化处理
        List<SysMenuVO> permMenuVOList = redisBean.getPermMenuList();
        //系统角色权限初始化处理
        Map<String, Object> roleMap = redisBean.getRoleMap();
        Map<String, Object> permMap = redisBean.getPermMap();
        if(!ListUtil.sizeGT(permMenuVOList)){
            redisBean.refreshPermMenu();
        }
        if(ToolUtil.isNull(roleMap) || ToolUtil.isNull(permMap)){
            redisBean.refreshRolePerm();
        }
        PrintUtil.info("[InitializingBean初始化] 系统菜单、角色、权限初始化执行结束 >>>>>>");
    }

}
