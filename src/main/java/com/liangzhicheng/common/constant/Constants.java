package com.liangzhicheng.common.constant;

/**
 * 常量统一定义
 * @author liangzhicheng
 */
public interface Constants {

    /**
     * 系统账号、菜单、权限相关常量
     */
    String LOGIN_ACCOUNT_ID = "login_account_id";
    String MENU_LIST_KEY = "menu_list_key"; //菜单列表键值
    String ROLE_MAP_KEY = "role_map_key"; //角色Map键值
    String PERM_MAP_KEY = "perm_map_key"; //权限Map键值

    String USER_INFO = "user_info"; //用户信息缓存key
    
    String WECHAT_ACCESS_TOKEN_KEY = "wechat_access_token_key"; //微信accessToken key
    String WECHAT_TICKET_KEY = "wechat_access_token_key"; //微信ticket key

    /**
     * 配置相关常量
     */
    String CLOUD_STORAGE_CONFIG_KEY = "cloud_storage_config_key"; //云存储key
    String PUSH_CONFIG_KEY = "push_config_key"; //推送key

    /**
     * 文件存放相关常量
     */
    String CONTENT_WORDS_PATH = "D:\\content-words.txt"; //本地敏感词文件地址

    String PDF_UTIL_PATH = "D:\\data\\wkhtmltopdf.exe"; //本地pdf工具地址
    String PDF_FILE_PATH = "D:\\data\\pdf\\"; //本地pdf文件夹地址

    /**
     * 日期格式相关常量
     */
    String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss"; //默认格式
    String DATE_PATTERN = "yyyy-MM-dd"; //日期格式（年月日）
    String TIMESTAMP_PATTERN = "yyyyMMddHHmmssSSS"; //毫秒格式

}

