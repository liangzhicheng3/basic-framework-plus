package com.liangzhicheng.modules.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liangzhicheng.common.constant.ApiConstant;
import com.liangzhicheng.common.enums.StringEnum;
import com.liangzhicheng.common.response.ResponseResult;
import com.liangzhicheng.common.utils.JSONUtil;
import com.liangzhicheng.common.utils.PrintUtil;
import com.liangzhicheng.modules.controller.WebSocketManager;
import com.liangzhicheng.modules.entity.SysRoleUserEntity;
import com.liangzhicheng.modules.mapper.ISysRoleUserMapper;
import com.liangzhicheng.modules.service.ISysRoleUserService;
import org.springframework.stereotype.Service;

import javax.websocket.Session;
import java.util.List;

/**
 * <p>
 * 角色用户表 服务实现类
 * </p>
 *
 * @author liangzhicheng
 */
@Service
public class SysRoleUserServiceImpl extends ServiceImpl<ISysRoleUserMapper, SysRoleUserEntity> implements ISysRoleUserService {

    /**
     * 根据key，value获取角色用户列表
     * @param key
     * @param value
     * @return List<SysRoleUserEntity>
     */
    @Override
    public List<SysRoleUserEntity> list(String key, String value) {
        return baseMapper.selectList(
                Wrappers.<SysRoleUserEntity>query()
                        .eq(key, value)
                        .eq(StringEnum.DEL_FLAG.getValue(), StringEnum.ZERO.getValue()));
    }

    @Override
    public void testOnlineRoleUser(String connectId) {
        ResponseResult responseResult = new ResponseResult(ApiConstant.BASE_SUCCESS_CODE, "未读消息总数");
        String str = "accountId:";
        try {
            String key = str + connectId;
            Session session = WebSocketManager.clients.get(key);
            if (session != null) {
                responseResult.setData(selectCountByAccountId(connectId));
                session.getBasicRemote().sendText(JSONUtil.toJSONString(responseResult));
                PrintUtil.info("[websocket服务] 统计未读消息总数，通知用户id：{} 成功", connectId);
            } else {
                PrintUtil.info("[websocket服务] 统计未读消息总数，通知用户失败，无法获取到用户id：{} 的连接", connectId);
            }
        } catch (Exception e) {
            PrintUtil.error("[websocket服务] 统计未读消息总数，通知用户发生异常，异常信息为：{}", e.getMessage());
        }
    }

    private int selectCountByAccountId(String accountId) {
        return baseMapper.selectCount(
                Wrappers.<SysRoleUserEntity>lambdaQuery()
                        .eq(SysRoleUserEntity::getAccountId, accountId)).intValue();
    }

}
