# basic-framework-plus

SpringBoot基础框架搭建

项目结构

src

-main

--java

---com

----liangzhicheng

-----common(公用函数定义、工具类定义)

-----config(应用相关类配置)

-----modules(逻辑业务相关类)

---devtool(应用开发中相关代码生成处理类)

--resources(数据库链接、代码生成模板相关)

启动

    修改appliction.yml配置，包括数据库链接，redis链接等...
    启动类：com/liangzhicheng/BasicFrameworkPlusApplication.java

swagger接口文档

    访问路径：http://localhost:8081/doc.html(swagger-boostrap-ui)
    根据自己项目所需的端口和命名方式配置appliction.yml配置中的port和context-path的值

代码生成

    找到devtool->generator->CodeGenerator类，修改数据库链接、路径等，直接运行generator()函数(详细注释说明类中有写)
    resources下提供了一些db的表
