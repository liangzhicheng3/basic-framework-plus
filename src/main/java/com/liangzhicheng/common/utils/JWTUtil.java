package com.liangzhicheng.common.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.liangzhicheng.common.bean.RedisBean;
import com.liangzhicheng.common.exception.TransactionException;
import com.liangzhicheng.config.context.SpringContextHolder;

import java.util.Date;

/**
 * JSON Web Token工具类
 * @author liangzhicheng
 */
public class JWTUtil {

    private static final RedisBean redisBean = SpringContextHolder.getBean(RedisBean.class);


    private static final String SUFFIX_MINI;
    private static final String TOKEN_KEY_MAP;
    private static final String SECRET;

    static {
        SUFFIX_MINI = ResourceUtil.getValue("jwt.suffix-mini");
        TOKEN_KEY_MAP = ResourceUtil.getValue("jwt.token-key-map");
        SECRET = ResourceUtil.getValue("jwt.secret");
    }

    /**
     * 生成用户JSON Web Token(MINI)
     * @param userId
     * @param expireTime
     * @return String
     */
    public static String createTokenMINI(String userId, Date expireTime){
        return createToken(userId + SUFFIX_MINI, expireTime);
    }

    /**
     * 更新用户Token(MINI)
     * @param userId
     * @param token
     */
    public static void updateTokenMINI(String userId, String token){
        updateToken(userId + SUFFIX_MINI, token);
    }

    /**
     * 清除用户缓存Token(MINI)
     * @param userId
     * @return String
     */
    public static String clearTokenMINI(String userId){
        return clearToken(userId + SUFFIX_MINI);
    }

    /**
     * 生成JSON Web Token
     * @param userId
     * @param expireTime
     * @return String
     */
    private static String createToken(String userId, Date expireTime){
        try{
            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            return JWT.create().withExpiresAt(expireTime).withIssuer(userId).sign(algorithm);
        }catch(JWTCreationException e){
            PrintUtil.info("[JWT] JWTCreationException：{}", e.getMessage());
            throw new TransactionException("生成JSON Web Token失败");
        }
    }

    /**
     * 更新用户Token
     * @param userId
     * @param token
     */
    private static void updateToken(String userId, String token){
        clearToken(userId);
        redisBean.hSet(TOKEN_KEY_MAP, userId, token);
    }

    /**
     * 清除用户缓存Token
     * @param userId
     * @return String
     */
    private static String clearToken(String userId){
        String oldKey = redisBean.hGet(TOKEN_KEY_MAP, userId);
        redisBean.hDelete(TOKEN_KEY_MAP, userId);
        return oldKey;
    }

    /**
     * 判断用户是否登录(MINI)
     * @param userId
     * @param token
     * @return boolean
     */
    public static boolean isLoginMINI(String userId, String token){
        return isLogin(userId + SUFFIX_MINI, token);
    }

    /**
     * 判断用户是否登录
     * @param userId
     * @param token
     * @return boolean
     */
    public static boolean isLogin(String userId, String token){
        if(ToolUtil.isNotBlank(userId)) {
            if(ToolUtil.isNotBlank(token)) {
                String existValue = redisBean.hGet(TOKEN_KEY_MAP, userId);
                if(ToolUtil.isNotBlank(existValue) && existValue.equals(token)) {
                    boolean isLogin = verifyToken(userId, token);
                    if(!isLogin){
                        PrintUtil.info("[JWT] 校验结果 userId：{},token：{},existValue：{},verifyToken：{}", userId, token, existValue, isLogin);
                        redisBean.hDelete(TOKEN_KEY_MAP, userId);
                    }
                    return isLogin;
                }
            }
        }
        return false;
    }

    /**
     * 判断用户是否登录
     * @param userId
     * @param token
     * @return boolean
     */
    public static boolean isNotLogin(String userId, String token){
        return !isLogin(userId, token);
    }

    /**
     * 校验Toekn是否有效
     * @param userId
     * @param token
     * @return boolean
     */
    public static boolean verifyToken(String userId, String token){
        boolean active = true;
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            JWTVerifier verifier = JWT.require(algorithm).withIssuer(userId).build();
            verifier.verify(token);
        } catch (TokenExpiredException exception){
            active = false;
        } catch (JWTDecodeException exception){
            active = false;
        } catch (JWTVerificationException exception){
            active = false;
        }
        return active;
    }

}
