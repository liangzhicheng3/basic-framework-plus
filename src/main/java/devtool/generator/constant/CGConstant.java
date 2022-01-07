package devtool.generator.constant;

/**
 * DB常量类
 * @author liangzhicheng
 */
public interface CGConstant {

    //作者
    String CG_AUTHOR = "liangzhicheng";

    //生成文件的输出目录
    String CG_PROJECT_PATH = System.getProperty("user.dir");

    //根路径
    String CG_ROOT_PATH = "/code-generator";

    //输出目录
    String CG_OUTPUT_DIR = CG_PROJECT_PATH + CG_ROOT_PATH;

    //包名
    String CG_PACKAGE_NAME = "com.liangzhicheng.modules";

    //数据库类型
    String CG_MYSQL = "MySQL";
    String CG_MYSQL_DRIVER = "com.mysql.cj.jdbc.Driver";
    String CG_ORACLE = "Oracle";
    String CG_ORACLE_DRIVER = "oracle.jdbc.OracleDriver";
    String CG_SQL_SERVER = "SQL_Server";
    String CG_SQL_SERVER_DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

    //数据库服务器
    String CG_HOST = "127.0.0.1:3306";

    //数据库用户名
    String CG_USERNAME = "root";

    //数据库密码
    String CG_PASSWORD = "admin";

    //包名：controller
    String CG_PACKAGE_NAME_CONTROLLER = "controller";

    //包名：mapper
    String CG_PACKAGE_NAME_DAO = "mapper";

    //包名：mapper.xml
    String CG_PACKAGE_NAME_XML = "mapper.xml";

    //包名：entity
    String CG_PACKAGE_NAME_ENTITY = "entity";

    //包名：service
    String CG_PACKAGE_NAME_SERVICE = "service";

    //包名：service.impl
    String CG_PACKAGE_NAME_SERVICE_IMPL = "service.impl";

    //文件名后缀：Controller
    String CG_FILE_NAME_CONTROLLER = "%sController";

    //文件名前后缀：I*Mapper
    String CG_FILE_NAME_DAO = "I%sMapper";

    //文件名后缀：Mapper
    String CG_FILE_NAME_XML = "%sMapper";

    //文件名后缀：Entity
    String CG_FILE_NAME_ENTITY = "%sEntity";

    //文件名前后缀：I*Service
    String CG_FILE_NAME_SERVICE = "I%sService";

    //文件名后缀：ServiceImpl
    String CG_FILE_NAME_SERVICE_IMPL = "%sServiceImpl";

    //逻辑删除字段
    String CG_DEL_USER = "del_user";

    //乐观锁字段名
    String CG_VERSION = "version";

}
