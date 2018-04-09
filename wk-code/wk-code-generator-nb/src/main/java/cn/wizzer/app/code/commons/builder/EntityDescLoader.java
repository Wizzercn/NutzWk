package cn.wizzer.app.code.commons.builder;

import org.nutz.dao.entity.annotation.*;
import org.nutz.ioc.Ioc;
import org.nutz.lang.Files;
import org.nutz.lang.Mirror;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * 根据java model定义生成，service，controller
 * Created by wizzer on 2017/1/23.
 */
public class EntityDescLoader extends Loader {

    private static final Log log = Logs.get();

    @Override
    public Map<String, TableDescriptor> loadTables(Ioc ioc, String basePackageName, String basePath, String baseUri, String servPackageName, String modPackageName) throws Exception {
        String packageName = modPackageName;
        String filePath = packageName.replaceAll("\\.", "\\/");
        URL url = Loader.class.getClassLoader().getResource(filePath);
        String path;
        if (url != null)
            path = url.getPath();
        else {
            path = "target/classes/" + filePath;
        }
        File f = Files.createDirIfNoExists(path);
        log.debug("classes dir = " + f.getAbsolutePath());
        String abstractPath = URLDecoder.decode(path, "utf8");
        File[] files = Files.lsFile(abstractPath, null);
        Map<String, TableDescriptor> tables = new HashMap<String, TableDescriptor>();

        for (File file : files) {
            String fileName = file.getName().split("\\.")[0];
            String className = packageName + "." + fileName;
            if (className.contains(".Model") || className.contains(".em")) {
                continue;
            }
            if (className.contains("$")) {
                continue;
            }
            Class<?> modelClass = Class.forName(className);
            Mirror<?> mirror = Mirror.me(modelClass);
            Table tableAnno = mirror.getAnnotation(Table.class);
            if (tableAnno == null) {
                continue;
            }
            String tableName = tableAnno.value();
            String entityName = modelClass.getSimpleName();
            TableDescriptor table = new TableDescriptor(tableName, entityName, basePackageName, baseUri, servPackageName, modPackageName);

            Comment comment = mirror.getAnnotation(Comment.class);
            if (comment != null) {
                table.setLabel(comment.value());
            } else {
                table.setLabel(entityName);
            }

            tables.put(tableName, table);
            tables.put(entityName, table);
            Field[] fields = mirror.getFields();
            for (Field field : fields) {
                ColumnDescriptor column = new ColumnDescriptor();
                String fieldName = field.getName();
                if (fieldName.equals("opBy") || fieldName.equals("opAt") || fieldName.equals("delFlag")) {
                    continue;
                }
                column.setFieldName(fieldName);
                Annotation[] annotations = field.getAnnotations();
                for (Annotation annotation : annotations) {
                    if (annotation instanceof Comment) {
                        column.setLabel(((Comment) annotation).value());
                        column.setComment(((Comment) annotation).value());
                    }
                    if (annotation instanceof Id || annotation instanceof Name) {
                        column.primary = true;
                        table.setPkType(column.getSimpleJavaTypeName());
                        column.columnName = fieldName;
                    }
                    if (annotation instanceof ColDefine) {
                        ColType colType = ((ColDefine) annotation).type();
                        column.setColumnType(colType.name());
                        column.dataType = colType.name();
                    }
                    if (annotation instanceof Column) {
                        String columnName = ((Column) annotation).value();
                        if (Strings.isBlank(columnName)) {
                            column.columnName = fieldName;
                        } else {
                            column.columnName = columnName;
                        }
                    }


                }
                if (Strings.isEmpty(column.getLabel())) {
                    column.setLabel(fieldName);
                }
                table.addColumn(column);
            }


        }
        return tables;
    }
}
