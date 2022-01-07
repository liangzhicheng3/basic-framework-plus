package com.liangzhicheng.common.bean;

import com.liangzhicheng.common.constant.Constants;
import com.liangzhicheng.common.utils.JSONUtil;
import com.liangzhicheng.modules.entity.vo.SysMenuVO;
import com.liangzhicheng.modules.service.ISysMenuService;
import com.liangzhicheng.modules.service.ISysRolePermService;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Redis缓存 bean
 * @author liangzhicheng
 */
@Component
public class RedisBean {

    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private ISysMenuService menuService;
    @Resource
    private ISysRolePermService rolePermService;

    /**
     * 读操作
     * @param key
     * @return String
     */
    public String get(String key) {
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        return ops.get(key);
    }

    /**
     * 写操作，永久存活
     * @param key
     * @param value
     */
    public void set(String key, String value) {
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        ops.set(key, value);
    }

    /**
     * 写操作，可设置存活秒数
     * @param key
     * @param value
     * @param second
     * @param timeUnit
     */
    public void set(String key, String value, long second, TimeUnit timeUnit) {
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        ops.set(key, value, second, timeUnit);
    }

    /**
     * 删除操作
     * @param key
     */
    public void delete(String key) {
        stringRedisTemplate.delete(key);
    }

    /**
     * 判断key是否存在
     * @param key
     * @return boolean
     */
    public boolean hasKey(String key) {
        return stringRedisTemplate.hasKey(key);
    }

    /**
     * hash读操作，返回名称为key的hash中field对应的value
     * @param key
     * @param field
     * @return String
     */
    public String hGet(String key, String field) {
        HashOperations<String, String, String> ops = stringRedisTemplate.opsForHash();
        return ops.get(key, field);
    }

    /**
     * hash写操作，向名称为key的hash中添加元素field
     * @param key
     * @param field
     * @param value
     */
    public void hSet(String key, String field, String value) {
        HashOperations<String, String, String> ops = stringRedisTemplate.opsForHash();
        ops.put(key, field, value);
    }

    /**
     * hash删除操作，删除名称为key的hash中键为field的域
     * @param key
     * @param field
     */
    public void hDelete(String key, String field) {
        HashOperations<String, String, String> ops = stringRedisTemplate.opsForHash();
        ops.delete(key, field);
    }

    /**
     * 判断hash的某个map是否存在某个key
     * @param mapName
     * @param key
     * @return boolean
     */
    public boolean hHasKey(String mapName, String key) {
        HashOperations<String, String, String> ops = stringRedisTemplate.opsForHash();
        return ops.hasKey(mapName, key);
    }

    /**
     * 名称为key的value值自增指定数量
     * @param key
     * @param value
     * @param num
     * @return Long
     */
    public Long hIncrement(String key, String value, long num){
        HashOperations<String, String, String> ops = stringRedisTemplate.opsForHash();
        return ops.increment(key, value, num);
    }

    /**
     * 向名称为key的set中添加元素
     * @param key
     * @param value
     */
    public void sAdd(String key, String value) {
        SetOperations<String, String> ops = stringRedisTemplate.opsForSet();
        ops.add(key, value);
    }

    /**
     * 删除名称为key的set中的元素
     * @param key
     * @param value
     */
    public void sRemove(String key, String value){
        SetOperations<String, String> ops = stringRedisTemplate.opsForSet();
        ops.remove(key, value);
    }

    /**
     * 随机弹出名称为key的set集合指定数量的元素
     * @param key
     * @param num
     * @return List<String>
     */
    public List<String> sPop(String key, long num){
        SetOperations<String, String> ops = stringRedisTemplate.opsForSet();
        return ops.pop(key, num);
    }

    /**
     * 返回名称为key的set的一个数量元素
     * @param key
     * @return String
     */
    public String sRandomMember(String key){
        SetOperations<String, String> ops = stringRedisTemplate.opsForSet();
        return ops.randomMember(key);
    }

    /**
     * 返回名称为key的set的指定数量随机元素
     * @param key
     * @param num
     * @return List<String>
     */
    public List<String> sRandomMembers(String key, long num){
        SetOperations<String, String> ops = stringRedisTemplate.opsForSet();
        return ops.randomMembers(key, num);
    }

    /**
     * 在集合中随机获取数个不同的元素
     * @param key
     * @param num
     * @return Set<String>
     */
    public Set<String> sDistinctRandomMembers(String key, long num){
        SetOperations<String, String> ops = stringRedisTemplate.opsForSet();
        return ops.distinctRandomMembers(key, num);
    }

    /**
     * 返回名称为key的set的所有元素
     * @param key
     * @return Set<String>
     */
    public Set<String> sMembers(String key){
        SetOperations<String, String> ops = stringRedisTemplate.opsForSet();
        return ops.members(key);
    }

    /**
     * 得到v1集合和v2集合的差集
     * @param v1
     * @param v2
     * @return Set<String>
     */
    public Set<String> sDifference(String v1, String v2){
        SetOperations<String, String> ops = stringRedisTemplate.opsForSet();
        return ops.difference(v1, v2);
    }

    /**
     * 得到v1集合和v2集合的并集
     * @param v1
     * @param v2
     * @return Set<String>
     */
    public Set<String> sIntersect(String v1, String v2){
        SetOperations<String, String> ops = stringRedisTemplate.opsForSet();
        return ops.intersect(v1, v2);
    }

    /**
     * 得到v1集合和v2集合的差集，并保存到v3集合中
     * @param v1
     * @param v2
     * @param v3
     * @return Long
     */
    public Long sDifferenceAndStore(String v1, String v2, String v3){
        SetOperations<String, String> ops = stringRedisTemplate.opsForSet();
        return ops.differenceAndStore(v1, v2, v3);
    }

    /**
     * 得到v1集合和v2集合的交集，并保存到v3集合中
     * @param v1
     * @param v2
     * @param v3
     * @return Long
     */
    public Long sIntersectAndStore(String v1, String v2, String v3){
        SetOperations<String, String> ops = stringRedisTemplate.opsForSet();
        return ops.intersectAndStore(v1, v2, v3);
    }

    /**
     * 得到v1集合和v2集合的并集，并保存到v3集合中
     * @param v1
     * @param v2
     * @param v3
     * @return Long
     */
    public Long sUnionAndStore(String v1, String v2, String v3){
        SetOperations<String, String> ops = stringRedisTemplate.opsForSet();
        return ops.unionAndStore(v1, v2, v3);
    }

    /**
     * 获取集合长度
     * @param key
     * @return Long
     */
    public Long sSize(String key){
        SetOperations<String, String> ops = stringRedisTemplate.opsForSet();
        return ops.size(key);
    }


    /**
     * 刷新系统权限菜单列表
     */
    public void refreshPermMenu() {
        set(Constants.MENU_LIST_KEY, JSONUtil.toJSONString(menuService.listPermMenu()));
    }

    /**
     * 刷新系统权限Map
     */
    public void refreshRolePerm(){
        Map<String, Object> resultMap = rolePermService.mapRolePerm();
        set(Constants.ROLE_MAP_KEY, JSONUtil.toJSONString(resultMap.get("roleMap")));
        set(Constants.PERM_MAP_KEY, JSONUtil.toJSONString(resultMap.get("permMap")));
    }

    /**
     * 获取缓存中系统权限菜单列表
     * @return List<SysMenuVO>
     */
    public List<SysMenuVO> getPermMenuList() {
        return JSONUtil.parseObject(get(Constants.MENU_LIST_KEY), List.class);
    }

    /**
     * 获取缓存中角色Map
     * @return Map<String, Object>
     */
    public Map<String, Object> getRoleMap(){
        return JSONUtil.parseObject(get(Constants.ROLE_MAP_KEY), Map.class);
    }

    /**
     * 获取缓存中权限Map
     * @return Map<String, Object>
     */
    public Map<String, Object> getPermMap(){
        return JSONUtil.parseObject(get(Constants.PERM_MAP_KEY), Map.class);
    }

}
