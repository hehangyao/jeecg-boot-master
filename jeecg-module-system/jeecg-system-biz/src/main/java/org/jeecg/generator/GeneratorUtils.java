package org.jeecg.generator;

import cn.hutool.core.util.StrUtil;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class GeneratorUtils {
    static Logger logger = Logger.getLogger("GeneratorUtils");
    private static final JdbcTemplate jdbcTemplate;
    private static final Configuration cfg;

    private final static String PACKAGE_PATH = "D:\\workspces\\smart\\scrm\\scrm-admin\\src\\main\\java\\com\\neksmart\\";
    // q:这里可以用相对路径吗？
    // a:可以，但是要注意，相对路径是相对于当前项目的根目录，而不是相对于当前类的根目录
    private final static String FTL_PATH = "D:\\workspces\\smart\\scrm\\scrm-admin\\src\\main\\resources\\templates";
    // mapper.xml文件存放路径
    private final static String XML_PATH = "D:\\workspces\\smart\\scrm\\scrm-admin\\src\\main\\resources\\mapper";
    private final static String SCHEMA = "netsmart";

    static {
        try {
            // TODO 从配置文件中读取
            String driver = "com.mysql.cj.jdbc.Driver";//mysql驱动
            String url = "jdbc:mysql://127.0.0.1:3306/netsmart?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=GMT%2B8";//连接地址
            String user = "root";//用户
            String password = "root";//密码


            DriverManagerDataSource dataSource = new DriverManagerDataSource();
            dataSource.setUrl(url);
            dataSource.setDriverClassName(driver);
            dataSource.setUsername(user);
            dataSource.setPassword(password);
            jdbcTemplate = new JdbcTemplate(dataSource);

            cfg = new Configuration(Configuration.VERSION_2_3_27);
            cfg.setDirectoryForTemplateLoading(new File(FTL_PATH));
            cfg.setDefaultEncoding("UTF-8");
            cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
            cfg.setLogTemplateExceptions(false);
            cfg.setWrapUncheckedExceptions(true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws IOException, TemplateException {
        String sql2 = "select TABLE_NAME,TABLE_COMMENT from information_schema.tables where TABLE_SCHEMA = '" + SCHEMA + "'";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql2);
        for (Map<String, Object> map : list) {
            String tableName = map.get("table_name").toString();
            String tableComment = map.get("table_comment").toString();
            // 将表名字sys_user转换为实体类SysUser
            String className = StrUtil.upperFirst(StrUtil.toCamelCase(tableName));
//            String className = StrUtil.upperFirst(StrUtil.toCamelCase(tableName.substring(2)));
            logger.info("tableName:" + tableName + ",className:" + className);

            List<Map<String, Object>> fields = new ArrayList<>();
            String sql = "select column_name as columnName, data_type as columnType, column_comment as columnComment from information_schema.columns where table_schema = '" + SCHEMA + "'" + " and table_name ='" + map.get("table_name") + "'";
            List<Map<String, Object>> list2 = jdbcTemplate.queryForList(sql);
            boolean hasDate = false;
            boolean hasBigDecimal = false;
            for (Map<String, Object> map2 : list2) {
                String columnName = String.valueOf(map2.get("columnName"));
                String columnType = String.valueOf(map2.get("columnType"));
                String columnComment=new String(map2.get("columnComment").toString().getBytes("UTF-8"),"UTF-8");
                Map<String, Object> field = new HashMap<>(map2);
                field.put("fieldComment", new String(columnComment.getBytes("UTF-8"), "UTF-8"));
                field.put("fieldName", StrUtil.toCamelCase(columnName));
                if ("tinyint".equals(columnType) || "smallint".equals(columnType) || "mediumint".equals(columnType) || "int".equals(columnType) || "integer".equals(columnType)) {
                    field.put("fieldType", "Integer");
                }
                if ("bigint".equals(columnType)) {
                    field.put("fieldType", "Long");
                }
                if ("varchar".equals(columnType) || "text".equals(columnType)) {
                    field.put("fieldType", "String");
                }
                if ("datetime".equals(columnType)) {
                    field.put("fieldType", "LocalDateTime");
                    hasDate = true;
                }
                if ("decimal".equals(columnType)) {
                    field.put("fieldType", "BigDecimal");
                    hasBigDecimal = true;
                }
                fields.add(field);
            }
            Map<String, Object> renderMap = new HashMap<>();
            // renderMap中的中文会乱码，所以要转换为utf-8
            renderMap.put("className", className);
            renderMap.put("tableName",tableName);
            renderMap.put("hasDate", hasDate);
            renderMap.put("hasBigDecimal", hasBigDecimal);
            renderMap.put("fields", fields);
            renderMap.put("package", "com.neksmart");
            renderMap.put("tableComment", new String(tableComment.getBytes("UTF-8"), "UTF-8"));
            // 将SysUser转换为sysUser
            String lowerClassName = StrUtil.lowerFirst(className);
            renderMap.put("lowerClassName", lowerClassName);
            // 生成前先删除
            String[] options = {"entity", "dto", "mapper", "service", "impl", "mapper.xml", "controller"};
            for (String option : options) {
                if ("impl".equals(option)) {
                    File file = new File(PACKAGE_PATH + "service\\impl\\" + className + "ServiceImpl.java");
                    if (file.exists()) {
                        while (!file.delete()) {
                            logger.info("删除失败，再次尝试");
                        }
                    }
                    continue;
                }
                if ("mapper.xml".equals(option)) {
                    File file = new File(XML_PATH + "\\" + className + "Mapper.xml");
                    if (file.exists()) {
                        while (!file.delete()) {
                            logger.info("删除失败，再次尝试");
                        }
                    }
                    continue;
                }
                if ("controller".equals(option)) {
                    File file = new File(PACKAGE_PATH + "controller\\" + className + "\\" + className + "Controller.java");
                    if (file.exists()) {
                        while (!file.delete()) {
                            logger.info("删除失败，再次尝试");
                        }
                    }
                    continue;
                }
                File file = new File(PACKAGE_PATH + option + "\\" + className + StrUtil.upperFirst(option) + ".java");
                if (file.exists()) {
                    while (!file.delete()) {
                        logger.info("删除失败，再次尝试");
                    }
                }
            }
            // 生成
            for (String option : options) {
                if ("entity".equals(option)) {
                    generatorEntity(renderMap);
                }
                if ("dto".equals(option)) {
                    generatorDto(renderMap);
                }
                if ("mapper".equals(option)) {
                    generatorMapper(renderMap);
                }
                if ("service".equals(option)) {
                    generatorService(renderMap);
                }
                if ("impl".equals(option)) {
                    generatorServiceImpl(renderMap);
                }
                if ("mapper.xml".equals(option)) {
                    generatorMapperXml(renderMap);
                }
                if ("controller".equals(option)) {
                    generatorController(renderMap);
                }
            }
        }

    }

    private static void generatorController(Map<String, Object> renderMap) {
        try {
            Template temp = cfg.getTemplate("controller.ftl");
            // 如果Controller不存在，则创建
            File file = new File(PACKAGE_PATH + "controller\\" + renderMap.get("className"));
            if (!file.exists()) {
                boolean mkdir = file.mkdirs();
                if (!mkdir) {
                    logger.info("创建controller文件夹失败");
                }
            }
            Writer out = new OutputStreamWriter(Files.newOutputStream(Paths.get(PACKAGE_PATH + "controller\\" + renderMap.get("className") + "\\" + renderMap.get("className") + "Controller.java")));
            temp.process(renderMap, out);
            logger.info("生成Controller成功");
        } catch (IOException | TemplateException e) {
            e.printStackTrace();
            logger.info("生成Controller失败,原因：" + e.getMessage());
        }
    }

    private static void generatorMapperXml(Map<String, Object> renderMap) {
        try {
            Template temp = cfg.getTemplate("mapperXml.ftl");
            Writer out = new OutputStreamWriter(Files.newOutputStream(Paths.get(XML_PATH + "\\" + renderMap.get("className") + "Mapper.xml")));
            temp.process(renderMap, out);
            logger.info("生成MapperXml成功");
        } catch (IOException | TemplateException e) {
            e.printStackTrace();
            logger.info("生成MapperXml失败,原因：" + e.getMessage());
        }
    }

    private static void generatorServiceImpl(Map<String, Object> renderMap) {
        try {
            Template temp = cfg.getTemplate("serviceImpl.ftl");
            Writer out = new OutputStreamWriter(Files.newOutputStream(Paths.get(PACKAGE_PATH + "service\\impl\\" + renderMap.get("className") + "ServiceImpl.java")));
            temp.process(renderMap, out);
            logger.info("生成ServiceImpl成功");
        } catch (TemplateException | IOException e) {
            logger.info("生成ServiceImpl失败,原因：" + e.getMessage());
        }
    }

    private static void generatorDto(Map<String, Object> renderMap) {
        try {
            Template temp = cfg.getTemplate("dto.ftl");
            Writer out = new OutputStreamWriter(Files.newOutputStream(Paths.get(PACKAGE_PATH + "dto\\" + renderMap.get("className") + "Dto.java")));
            temp.process(renderMap, out);
            logger.info("生成Dto成功");
        } catch (IOException | TemplateException e) {
            e.printStackTrace();
            logger.info("生成Dto失败,原因：" + e.getMessage());
        }
    }

    public static void generatorService(Map<String, Object> renderMap) throws IOException, TemplateException {
        Template temp = cfg.getTemplate("service.ftl");
        Writer out = new OutputStreamWriter(Files.newOutputStream(Paths.get(PACKAGE_PATH + "service\\" + renderMap.get("className") + "Service.java")));
        temp.process(renderMap, out);
        logger.info("生成Service成功");
    }

    public static void generatorMapper(Map<String, Object> renderMap) throws IOException, TemplateException {
        Template temp = cfg.getTemplate("mapper.ftl");
        Writer out = new OutputStreamWriter(Files.newOutputStream(Paths.get(PACKAGE_PATH + "mapper\\" + renderMap.get("className") + "Mapper" + ".java")));
        temp.process(renderMap, out);
        logger.info("生成Mapper成功");
    }

    public static void generatorEntity(Map<String, Object> renderMap) throws IOException, TemplateException {
        Template temp = cfg.getTemplate("entity.ftl");
        // q:如何解决中文乱码问题？
        // a:使用OutputStreamWriter，而不是FileWriter
        Writer out = new OutputStreamWriter(Files.newOutputStream(Paths.get(PACKAGE_PATH + "entity\\" + renderMap.get("className") + ".java")));
        temp.process(renderMap, out);
        logger.info("生成Entity成功");
    }
}
