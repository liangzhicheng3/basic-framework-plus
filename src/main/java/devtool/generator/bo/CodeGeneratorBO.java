package devtool.generator.bo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 代码生成数据传输对象
 * @author liangzhicheng
 */
@Data
@Accessors(chain = true)
public class CodeGeneratorBO {

    /**
     * 作者
     */
    private String author;

    /**
     * 输出目录
     */
    private String outDir;

    /**
     * 包名
     */
    private String packageName;

    /**
     * 数据库类型
     */
    private String dbType;

    /**
     * 数据库连接地址
     */
    private String dbUrl;

    /**
     * 数据库用户名
     */
    private String dbUsername;

    /**
     * 数据库密码
     */
    private String dbPassword;

    /**
     * 数据库驱动
     */
    private String dbDriver;

    /**
     * 表名
     */
    private String[] tableNames;

    /**
     * 表前缀
     */
    private String[] tablePrefixes;

    /**
     * controller包名
     */
    private String packageController;

    /**
     * mapper包名
     */
    private String packageMapper;

    /**
     * 包名：dao.mapper(xml)
     */
    private String packageMapperXml;

    /**
     * 实体类包名
     */
    private String packageEntity;
    /**
     * service包名
     */
    private String packageService;

    /**
     * serviceImpl包名
     */
    private String packageServiceImpl;

    /**
     * controller文件名格式
     */
    private String fileNamePatternController;

    /**
     * mapper文件名格式
     */
    private String fileNamePatternMapper;

    /**
     * mapper XML文件名格式
     */
    private String fileNamePatternMapperXml;

    /**
     * 实体类文件名格式
     */
    private String fileNamePatternEntity;

    /**
     * service文件名格式
     */
    private String fileNamePatternService;

    /**
     * serviceImpl文件名格式
     */
    private String fileNamePatternServiceImpl;

    /**
     * 逻辑删除字段
     */
    private String fieldLogicDelete;

    /**
     * 乐观锁字段
     */
    private String fieldVersion;

}
