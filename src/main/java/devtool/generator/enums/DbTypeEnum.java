package devtool.generator.enums;

import lombok.Getter;
import org.springframework.util.StringUtils;

/**
 * 数据库类型枚举类
 * @author liangzhicheng
 */
@Getter
public enum DbTypeEnum {

    MYSQL("mysql", "MySQL"),
    ORACLE("oracle", "Oracle"),
    SQL_SERVER("sql_server", "SQL_Server");

    /**
     * 类型代码
     */
    private final String code;

    /**
     * 描述
     */
    private final String desc;

    DbTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static DbTypeEnum getDbType(String dbName) {
        if (!StringUtils.hasText(dbName)) {
            return null;
        }
        for (DbTypeEnum dbTypeEnum : DbTypeEnum.values()) {
            if (dbTypeEnum.code.equals(dbName.toLowerCase())) {
                return dbTypeEnum;
            }
        }
        return null;
    }

}
