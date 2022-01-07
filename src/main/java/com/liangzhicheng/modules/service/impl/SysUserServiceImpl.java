package com.liangzhicheng.modules.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageInfo;
import com.liangzhicheng.common.bean.RedisBean;
import com.liangzhicheng.common.constant.ApiConstant;
import com.liangzhicheng.common.constant.Constants;
import com.liangzhicheng.common.enums.StringEnum;
import com.liangzhicheng.common.exception.TransactionException;
import com.liangzhicheng.common.response.PageHelper;
import com.liangzhicheng.common.response.WrapperHelper;
import com.liangzhicheng.common.utils.*;
import com.liangzhicheng.modules.entity.SysDeptEntity;
import com.liangzhicheng.modules.entity.SysRoleEntity;
import com.liangzhicheng.modules.entity.SysRoleUserEntity;
import com.liangzhicheng.modules.entity.SysUserEntity;
import com.liangzhicheng.modules.entity.dto.SysUserDTO;
import com.liangzhicheng.modules.entity.query.SysUserQueryCondition;
import com.liangzhicheng.modules.entity.vo.*;
import com.liangzhicheng.modules.mapper.ISysUserMapper;
import com.liangzhicheng.modules.service.*;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 账号信息表 服务实现类
 * </p>
 *
 * @author liangzhicheng
 */
@Service
public class SysUserServiceImpl extends BaseServiceImpl<ISysUserMapper, SysUserEntity> implements ISysUserService {

    @Resource
    private ISysDeptService deptService;
    @Resource
    private ISysRoleService roleService;
    @Resource
    private ISysRoleUserService roleUserService;
    @Resource
    private RedisBean redisBean;

    /**
     * 登录
     * @param userDTO
     * @param request
     * @return SysUserLoginVO
     */
    @Override
    public SysUserLoginVO login(SysUserDTO userDTO, HttpServletRequest request) {
        String accountName = userDTO.getAccountName();
        String password = userDTO.getPassword();
        String content = "账号或密码错误";
        AssertUtil.isFalse(ToolUtil.isBlank(accountName, password), content);
        SysUserLoginVO userVO = null;
        try{
            //封装令牌
            UsernamePasswordToken userToken = new UsernamePasswordToken(accountName, AESUtil.aesEncrypt(password));
            //调用Shiro的Api登录
            SecurityUtils.getSubject().login(userToken);
            PrintUtil.info("[PC登录] 校验结果：{}", SecurityUtils.getSubject().isAuthenticated());
            //用户信息
            SysUserEntity user = (SysUserEntity) SecurityUtils.getSubject().getPrincipal();
            userVO = BeansUtil.copyEntity(user, SysUserLoginVO.class);
            //角色，权限处理
            //若是超级管理员返回所有权限菜单，否则返回对应权限菜单
            if(StringEnum.ONE.getValue().equals(user.getIsAdmin())){
                userVO.setPermMenuList(redisBean.getPermMenuList());
            }else{
                userVO.setPermMenuList(this.getPermMenuList(user.getId()));
            }
            //生成JSON Web Token
            String token = JWTUtil.createTokenMINI(user.getId(), TimeUtil.dateAdd(new Date(), 7));
            JWTUtil.updateTokenMINI(user.getId(), token);
            userVO.setToken(token);
            request.getSession().setAttribute(Constants.LOGIN_ACCOUNT_ID, user.getId());
        }catch(UnknownAccountException e){
            throw new TransactionException(ApiConstant.BASE_FAIL_CODE, content);
        }catch(IncorrectCredentialsException e){
            throw new TransactionException(ApiConstant.BASE_FAIL_CODE, content);
        }catch(DisabledAccountException e){
            throw new TransactionException(ApiConstant.BASE_FAIL_CODE, e.getMessage());
        }
        return userVO;
    }

    /**
     * 退出登录
     * @param request
     */
    @Override
    public void logOut(HttpServletRequest request) {
//        SysUserEntity user = ShiroUtil.getCurrentUser(request);
        SysUserEntity user = baseMapper.selectById(request.getHeader("accountId"));
        AssertUtil.isFalse(ToolUtil.isNull(user), "账号不存在");
        JWTUtil.clearTokenMINI(user.getId());
        request.getSession().removeAttribute(Constants.LOGIN_ACCOUNT_ID);
    }

    /**
     * 更新当前登录用户头像
     * @param userDTO
     * @return SysPersonInfoVO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public SysPersonInfoVO updateAvatar(SysUserDTO userDTO, HttpServletRequest request) {
        String accountId = request.getHeader("accountId");
        String avatar = userDTO.getAvatar();
        AssertUtil.isFalse(ToolUtil.isBlank(accountId, avatar), ApiConstant.PARAM_IS_NULL);
//        SysUserEntity user = ShiroUtil.getCurrentUser();
        SysUserEntity user = baseMapper.selectById(accountId);
        AssertUtil.isFalse(ToolUtil.isNull(user), "账号不存在");
        user.setAvatar(avatar);
        baseMapper.updateById(user);
        return BeansUtil.copyEntity(user, SysPersonInfoVO.class);
    }

    /**
     *更新当前登录用户密码
     * @param userDTO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePassword(SysUserDTO userDTO, HttpServletRequest request) {
        String accountId = request.getHeader("accountId");
        String password = userDTO.getPassword();
        String newPassword = userDTO.getNewPassword();
        AssertUtil.isFalse(ToolUtil.isBlank(accountId, password, newPassword), ApiConstant.PARAM_IS_NULL);
//        SysUserEntity user = ShiroUtil.getCurrentUser();
        SysUserEntity user = baseMapper.selectById(accountId);
        AssertUtil.isFalse(ToolUtil.isNull(user), "账号不存在");
        AssertUtil.isFalse(!user.getPassword().equals(AESUtil.aesEncrypt(password)), ApiConstant.PASSWORD_ERROR);
        user.setPassword(AESUtil.aesEncrypt(newPassword));
        baseMapper.updateById(user);
    }

    /**
     * 账号管理列表
     * @param userDTO
     * @return IPage
     */
    @Override
    public IPage listAccount(SysUserDTO userDTO) {
        String keyword = userDTO.getKeyword();
        String deptId = userDTO.getDeptId();
        String roleId = userDTO.getRoleIds();
        String loginStatus = userDTO.getLoginStatus();
        //1.账号信息查询
        QueryWrapper<SysUserEntity> wrapperUser = new QueryWrapper<SysUserEntity>();
        if(ToolUtil.isNotBlank(keyword)){
            wrapperUser.and(Wrapper -> Wrapper.like("id", keyword)
                    .or().like("truename", keyword));
        }
        wrapperUser.eq(ToolUtil.isNotBlank(deptId), "dept_id", deptId);
        wrapperUser.eq(ToolUtil.isNotBlank(loginStatus)
                && ToolUtil.in(loginStatus, StringEnum.ZERO.getValue(), StringEnum.ZERO.getValue()),
                "login_status", loginStatus);
        List<SysUserEntity> userList = baseMapper.selectList(wrapperUser
                .eq(StringEnum.DEL_FLAG.getValue(), StringEnum.ZERO.getValue()));
        List<String> accountIdByUser = new ArrayList<>(userList.size());
        if(ListUtil.sizeGT(userList)){
            for(SysUserEntity user : userList){
                accountIdByUser.add(user.getId());
            }
        }
        //2.账号角色信息查询
        QueryWrapper<SysRoleUserEntity> wrapperRoleUser = new QueryWrapper<SysRoleUserEntity>();
        if(ToolUtil.inOneByNotBlank(roleId)){
            wrapperRoleUser.eq(ToolUtil.isNotBlank(roleId), "role_id", roleId);
            List<SysRoleUserEntity> roleUserList = roleUserService.list(
                    wrapperRoleUser.eq(StringEnum.DEL_FLAG.getValue(), StringEnum.ZERO.getValue()));
            //账号角色处理
            List<String> accountIdByRoleUser = new ArrayList<>(roleUserList.size());
            if(ListUtil.sizeGT(roleUserList)){
                for(SysRoleUserEntity roleUser : roleUserList){
                    String accountId = roleUser.getAccountId();
                    if(!accountIdByRoleUser.contains(accountId)){
                        accountIdByRoleUser.add(accountId);
                    }
                }
            }
            //获取两个List交集
            accountIdByUser = ListUtil.getListBoth(accountIdByUser, accountIdByRoleUser);
        }
        IPage resultList = new Page();
        if(ListUtil.sizeGT(accountIdByUser)){
            resultList = baseMapper.selectPage(PageHelper.getInstance().handle(userDTO),
                    new QueryWrapper<SysUserEntity>().in("id", accountIdByUser)
                            .orderByDesc("login_status").orderByAsc("id"));
            userList = resultList.getRecords();
            List<SysUserVO> userVOList = new ArrayList<>();
            if(ListUtil.sizeGT(userList)){
                SysUserVO userVO = null;
                List<SysRoleUserEntity> roleUserList = new ArrayList<>();
                List<String> roleNames = null;
                for(SysUserEntity user : userList){
                    userVO = BeansUtil.copyEntity(user, SysUserVO.class);
                    roleUserList = roleUserService.list("account_id", user.getId());
                    if(ListUtil.sizeGT(roleUserList)){
                        roleNames = new ArrayList<>();
                        for(SysRoleUserEntity roleUser : roleUserList){
                            roleNames.add(roleUser.getRoleName());
                        }
                        userVO.setRoleNames(roleNames);
                    }
                    userVOList.add(userVO);
                }
            }
            return resultList.setRecords(userVOList);
        }
        resultList.setRecords(new ArrayList<>(1));
        resultList.setTotal(0L);
        return resultList;
    }

    /**
     * 根据key，value获取用户列表
     * @param key
     * @param value
     * @return List<SysUserEntity>
     */
    @Override
    public List<SysUserEntity> list(String key, String value) {
        QueryWrapper<SysUserEntity> wrapperUser = Wrappers.<SysUserEntity>query();
        if(ToolUtil.isNotBlank(key, value)){
            wrapperUser.eq(key, value);
        }
        return baseMapper.selectList(
                wrapperUser.eq("login_status", StringEnum.ONE.getValue())
                        .eq(StringEnum.DEL_FLAG.getValue(), StringEnum.ZERO.getValue()));
    }

    /**
     * 获取账号
     * @param userDTO
     * @return SysUserDescVO
     */
    @Override
    public SysUserDescVO getAccount(SysUserDTO userDTO) {
        SysUserEntity user = baseMapper.selectById(userDTO.getId());
        AssertUtil.isFalse(ToolUtil.isNull(user), "账号不存在");
        SysUserDescVO userDescVO = BeansUtil.copyEntity(user, SysUserDescVO.class);
        //获取该账号角色
        List<SysRoleUserEntity> roleUserList = roleUserService.list("account_id", user.getId());
        if(ListUtil.sizeGT(roleUserList)){
            List<String> roleIds = new ArrayList<>(roleUserList.size());
            List<String> roleNames = new ArrayList<>(roleUserList.size());
            for(SysRoleUserEntity roleUser : roleUserList){
                roleIds.add(roleUser.getRoleId());
                roleNames.add(roleUser.getRoleName());
            }
            userDescVO.setRoleIds(roleIds);
            userDescVO.setRoleNames(roleNames);
        }
        return userDescVO;
    }

    /**
     * 保存账号
     * @param userDTO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveAccount(SysUserDTO userDTO) {
        String id = userDTO.getId();
        String deptId = userDTO.getDeptId();
        String roleId = userDTO.getRoleIds();
        String accountName = userDTO.getAccountName();
        String truename = userDTO.getTruename();
        String loginStatus = userDTO.getLoginStatus();
        SysUserEntity user = baseMapper.selectById(id);
        if(ToolUtil.isNull(user)){
            user = new SysUserEntity();
            user.setId(SnowFlakeUtil.get().nextId() + "");
            user.setPassword(AESUtil.aesEncrypt(""));
        }
        if(ToolUtil.isNotBlank(deptId)){
            SysDeptEntity dept = deptService.getById(deptId);
            AssertUtil.isFalse(ToolUtil.isNull(dept), "部门不存在");
            user.setDeptId(deptId);
            user.setDeptName(dept.getName());
        }
        if(ToolUtil.isNotBlank(accountName)){
            AssertUtil.isFalse(accountName.length() > 30, "账号名称字数过长");
            if(ToolUtil.isBlank(id) || !user.getAccountName().equals(accountName)){
                SysUserEntity existUser = baseMapper.selectOne(
                        Wrappers.<SysUserEntity>lambdaQuery()
                                .eq(SysUserEntity::getAccountName, accountName)
                                .eq(SysUserEntity::getDelFlag, StringEnum.ZERO.getValue())
                                .last("LIMIT 1"));
                AssertUtil.isFalse(ToolUtil.isNotNull(existUser), "账号已存在");
            }
            user.setAccountName(accountName);
        }
        if(ToolUtil.isNotBlank(truename)){
            AssertUtil.isFalse(accountName.length() > 30, "姓名字数过长");
            user.setTruename(truename);
        }
        if(ToolUtil.isNotBlank(loginStatus)){
            AssertUtil.isFalse(ToolUtil.notIn(loginStatus, StringEnum.ZERO.getValue(),
                    StringEnum.ONE.getValue()), ApiConstant.PARAM_ERROR);
            user.setLoginStatus(loginStatus);
        }
        //角色用户处理
        if(ToolUtil.isNotBlank(roleId)){
            //角色用户
            String[] arrayRoleId = roleId.split(",");
            if(arrayRoleId != null && arrayRoleId.length > 0){
                SysRoleEntity role = null;
                SysRoleUserEntity roleUser = null;
                String isAdmin = StringEnum.ZERO.getValue();
                List<SysRoleUserEntity> roleUserList = new ArrayList<>();
                for (String primaryId : arrayRoleId) {
                    role = roleService.getById(primaryId);
                    AssertUtil.isFalse(ToolUtil.isNull(role), "角色记录不存在");
                    if(StringEnum.ONE.getValue().equals(primaryId)){
                        isAdmin = StringEnum.ONE.getValue();
                    }
                    roleUser = new SysRoleUserEntity(SnowFlakeUtil.get().nextId() + "",
                            primaryId, role.getName(), user.getId(), user.getAccountName());
                    roleUserList.add(roleUser);
                }
                user.setIsAdmin(isAdmin);
                //保存前做删除角色用户
                roleUserService.lambdaUpdate()
                        .eq(SysRoleUserEntity::getAccountId, user.getId())
                        .eq(SysRoleUserEntity::getDelFlag, StringEnum.ZERO.getValue())
                        .set(SysRoleUserEntity::getDelFlag, StringEnum.ONE.getValue())
                        .update();
                roleUserService.saveBatch(roleUserList);
            }
        }
        saveOrUpdate(user);
        //开启线程刷新缓存中权限
        ThreadUtil.getInstance().refreshRolePerm();
    }

    /**
     * 重置密码
     * @param userDTO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resetPassword(SysUserDTO userDTO) {
        SysUserEntity user = baseMapper.selectById(userDTO.getId());
        AssertUtil.isFalse(ToolUtil.isNull(user), "账号不存在");
        user.setPassword(AESUtil.aesEncrypt(""));
        baseMapper.updateById(user);
    }

    /**
     * 删除账号
     * @param userDTO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAccount(SysUserDTO userDTO) {
        SysUserEntity user = baseMapper.selectById(userDTO.getId());
        AssertUtil.isFalse(ToolUtil.isNull(user), "账号不存在");
        //删除角色用户记录
        List<SysRoleUserEntity> roleUserList = roleUserService.list("account_id", user.getId());
        if(ListUtil.sizeGT(roleUserList)){
            for(SysRoleUserEntity roleUser : roleUserList){
                roleUser.setDelFlag(StringEnum.ONE.getValue());
            }
            roleUserService.updateBatchById(roleUserList);
        }
        user.setDelFlag(StringEnum.ONE.getValue());
        baseMapper.updateById(user);
    }

    @Override
    public SysUserEntity getByCache(String userId) {
        String userInfo = redisBean.hGet(Constants.USER_INFO, userId);
        return JSONUtil.parseObject(userInfo, SysUserEntity.class);
    }

    /**
     * 测试自定义排序查询列表
     * @param sysUserDTO
     * @param pageable
     * @return
     */
    @Override
    public Map<String, Object> testUserList(SysUserDTO sysUserDTO, Pageable pageable) {
        SysUserQueryCondition userQuery = new SysUserQueryCondition(sysUserDTO);
        //自定义排序
        List<Sort.Order> orders = new ArrayList<>();
        orders.add(new Sort.Order(Sort.Direction.DESC,"is_admin"));
        orders.add(new Sort.Order(Sort.Direction.ASC,"create_date"));
        super.pageHandle(super.customizeSort(userQuery, orders), userQuery.getPageNum(), userQuery.getPageSize());
        //只根据创建时间排序
//        super.pageHandle(pageable, userQuery.getPageNum(), userQuery.getPageSize());
        List<SysUserEntity> personList = baseMapper.selectList(
                WrapperHelper.getInstance().buildCondition(SysUserEntity.class, userQuery));
        PageInfo pageInfo = new PageInfo<>();
        List<?> records = null;
        if(ListUtil.sizeGT(personList)){
            pageInfo = new PageInfo<>(personList);
            records = BeansUtil.copyList(pageInfo.getList(), SysUserVO.class);
        }
        return super.pageResult(records, pageInfo);
    }

    /**
     * 根据账号获取权限菜单
     * @param accountId
     * @return List<SysMenuVO>
     */
    private List<SysMenuVO> getPermMenuList(String accountId){
        //获取用户所拥有的菜单
        List<String> userMenuIdList = baseMapper.selectListByUserMenu(accountId);
        //缓存中获取所有菜单
        List<SysMenuVO> menuList = redisBean.getPermMenuList();
        //用户所拥有的菜单与所有菜单匹配得到菜单对象
        List<SysMenuVO> userMenuList = new ArrayList<>(userMenuIdList.size());
        for(Iterator<String> userMenuIds = userMenuIdList.iterator(); userMenuIds.hasNext();){
            String userMenuId = userMenuIds.next();
            for(Iterator<SysMenuVO> menus = menuList.iterator(); menus.hasNext();){
                SysMenuVO menu = menus.next();
                if(userMenuId.equals(menu.getId())){
                    userMenuList.add(menu);
                }
            }
        }
        //过滤顶级菜单并递归获取子菜单
        List<SysMenuVO> resultList = userMenuList.stream()
                .filter(menu -> menu.getParentId().equals("0"))
                .map(menu -> this.convert(menu, menuList))
                .collect(Collectors.toList());
        return resultList;
    }

    /**
     * 将菜单转换为有子级的菜单对象，当找不到子级菜单时候map操作不会再递归调用covert
     * @param menu 菜单对象
     * @param menuList 菜单集合
     * @return 返回目录菜单按钮结果集
     */
    private SysMenuVO convert(SysMenuVO menu, List<SysMenuVO> menuList) {
        List<SysMenuVO> childrenList = menuList.stream()
                .filter(subMenu -> subMenu.getParentId().equals(menu.getId()))
                .map(subMenu -> this.convert(subMenu, menuList))
                .collect(Collectors.toList());
        menu.setChildren(childrenList);
        return menu;
    }

}
