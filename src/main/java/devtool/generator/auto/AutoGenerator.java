package devtool.generator.auto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.SimpleAutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import devtool.generator.bo.CodeGeneratorBO;
import devtool.generator.constant.CGConstant;
import org.springframework.util.StringUtils;

import java.util.Objects;

/**
 * 生成配置类
 * @author liangzhicheng
 */
public class AutoGenerator extends SimpleAutoGenerator {

    private final CodeGeneratorBO generatorBO;

    public AutoGenerator(CodeGeneratorBO generatorBO) {
        this.generatorBO = generatorBO;
    }

    /**
     * 数据源配置
     * @return IConfigBuilder<DataSourceConfig>
     */
    @Override
    public IConfigBuilder<DataSourceConfig> dataSourceConfigBuilder() {
        return new DataSourceConfig.Builder(
                generatorBO.getDbUrl(),
                generatorBO.getDbUsername(),
                generatorBO.getDbPassword());
    }

    /**
     * 全局配置
     * @return IConfigBuilder<GlobalConfig>
     */
    @Override
    public IConfigBuilder<GlobalConfig> globalConfigBuilder() {
        GlobalConfig.Builder builder = new GlobalConfig.Builder();
        builder.fileOverride().author(generatorBO.getAuthor());
        String outDir = generatorBO.getOutDir();
        if (!StringUtils.hasText(outDir)) {
            outDir = CGConstant.CG_OUTPUT_DIR;
        }
        builder.outputDir(outDir);
//        DateType dateType = DateType.TIME_PACK;
//        if (!"8".equalsIgnoreCase(generatorBO.getJdkVersion())) {
//            dateType = DateType.ONLY_DATE;
//        }
//        builder.dateType(dateType);
//        if (isBoolean(generatorBO.getSwaggerSupport())) {
//            builder.enableSwagger();
//        }
        return builder;
    }

    /**
     * 包配置
     * @return IConfigBuilder<PackageConfig>
     */
    @Override
    public IConfigBuilder<PackageConfig> packageConfigBuilder() {
        return new PackageConfig.Builder()
                .parent(generatorBO.getPackageName())
//                 builder.moduleName("");
                .controller(generatorBO.getPackageController())
                .mapper(generatorBO.getPackageMapper())
                .xml(generatorBO.getPackageMapperXml())
                .entity(generatorBO.getPackageEntity())
                .service(generatorBO.getPackageService())
                .serviceImpl(generatorBO.getPackageServiceImpl());
    }

    /**
     * 策略配置
     * @return IConfigBuilder<StrategyConfig>
     */
    @Override
    public IConfigBuilder<StrategyConfig> strategyConfigBuilder() {
        StrategyConfig.Builder builder = new StrategyConfig.Builder();
        builder.addInclude(generatorBO.getTableNames())
//                .addFieldPrefix(generatorBO.getFieldPrefixes())
                .addTablePrefix(generatorBO.getTablePrefixes())
//                .addExclude(generatorBO.getExcludeTableNames())
                .controllerBuilder()
                .enableRestStyle()
                .enableHyphenStyle()
                .mapperBuilder()
                .formatMapperFileName(generatorBO.getFileNamePatternMapper())
                .formatXmlFileName(generatorBO.getFileNamePatternMapperXml())
                .entityBuilder()
                .naming(NamingStrategy.underline_to_camel)
//                .enableChainModel()
//                .enableLombok()
//                .enableActiveRecord()
                .formatFileName(generatorBO.getFileNamePatternEntity())
                .idType(IdType.AUTO)
                .logicDeleteColumnName(generatorBO.getFieldLogicDelete())
                .versionColumnName(generatorBO.getFieldVersion())
//                .superClass(generatorBO.getSuperClassName())
//                .addIgnoreColumns(generatorBO.getIgnoreColumns())
                .serviceBuilder()
                .formatServiceFileName(generatorBO.getFileNamePatternService())
                .formatServiceImplFileName(generatorBO.getFileNamePatternServiceImpl());
//        Entity.Builder entityBuilder = builder.entityBuilder();
//        if (isBoolean(generatorBO.getLombokChainModel())) {
//            entityBuilder.enableChainModel();
//        }
//        if (isBoolean(generatorBO.getLombokModel())) {
//            entityBuilder.enableLombok();
//        }
//        if (isBoolean(generatorBO.getColumnConstant())) {
//            entityBuilder.enableColumnConstant();
//        }
//
//        Mapper.Builder mapperBuilder = builder.mapperBuilder();
//        if (isBoolean(generatorBO.getBaseResultMap())) {
//            mapperBuilder.enableBaseResultMap();
//        }
//        if (isBoolean(generatorBO.getBaseColumnList())) {
//            mapperBuilder.enableBaseColumnList();
//        }
        return builder;
    }

    /**
     * 自定义配置
     * @return IConfigBuilder<InjectionConfig>
     */
//    public IConfigBuilder<InjectionConfig> injectionConfigBuilder(){
//        InjectionConfig.Builder builder = new InjectionConfig.Builder();
//        //自定义处理
//
//        return builder;
//    }

    /**
     * 处理Boolean，为空时返回false
     * @param bool
     * @return boolean
     */
    private boolean isBoolean(Boolean bool) {
        return Objects.nonNull(bool) && bool;
    }

}
