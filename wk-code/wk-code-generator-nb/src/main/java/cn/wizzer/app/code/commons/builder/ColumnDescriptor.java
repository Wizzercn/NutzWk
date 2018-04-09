package cn.wizzer.app.code.commons.builder;


import org.nutz.lang.Strings;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 属性（数据库列）基本信息描述
 * Created by wizzer on 2017/1/23.
 */
public class ColumnDescriptor {
    private static Map<String, Class<?>> typeMapping = new HashMap<String, Class<?>>();

    static {
        typeMapping.put("varchar", String.class);
        typeMapping.put("enum", String.class);
        typeMapping.put("bigint", Long.class);
        typeMapping.put("long", Long.class);
        typeMapping.put("integer", Integer.class);
        typeMapping.put("float", Float.class);
        typeMapping.put("double", Double.class);
        typeMapping.put("int", Integer.class);
        typeMapping.put("timestamp", Integer.class);
        typeMapping.put("datetime", Integer.class);
        typeMapping.put("boolean", boolean.class);
        typeMapping.put("tinyint", boolean.class);
        typeMapping.put("bool", boolean.class);
        typeMapping.put("decimal", BigDecimal.class);
    }
    //todo
//	private static Map<String, Class<?>> validationRules = new HashMap<String,Class<?>>();

    static {

    }

    private static Map<String, String> labelMapping = new HashMap<String, String>();

    static {

        labelMapping.put("id", "ID");
        labelMapping.put("opAt", "操作时间");
        labelMapping.put("opBy", "操作人");
        labelMapping.put("delFlag", "删除标记");
    }

    public static class Validation {
        public final Class<?> klass;
        private final String annotation;

        public Validation(Class<?> klass, String annotation) {
            this.klass = klass;
            this.annotation = annotation;
        }

        public String getAnnotation() {
            return annotation;
        }
    }

    private static Pattern COLUMN_TYPE_PATTERN = Pattern
            .compile("^(\\w+)(?:\\((\\d+)\\))?");
    private static Pattern ENUM_PATTERN = Pattern.compile("enum\\((.+)\\)");

    public String columnName;
    private String label;
    public boolean primary;
    public String dataType;

    public String columnType;
    public int size;

    public boolean nullable;
    private Object defaultValue;
    private String comment;
    private String fieldName;

    private List<String> enumValues = new ArrayList<String>();

    private List<Validation> validations = new ArrayList<Validation>();
    private boolean validationBuilt = false;

    private String queryOperator;


    private boolean containsValidation(Class<?> klass) {
        for (Validation v : validations) {
            if (v.klass == klass) {
                return true;
            }
        }
        return false;
    }

    public boolean hasLabel() {
        return label != null;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLabel() {
        if (label != null) {
            return label;
        }
        String defaultLabel = labelMapping.get(columnName);
        if (defaultLabel != null) {
            return defaultLabel;
        }
        return label;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
        if (comment == null) {
            return;
        }

        extractLabel(comment);
        extractSearchable(comment);
    }

    private void extractLabel(String comment) {
        Pattern labelPattern = Pattern.compile("label:\\s*([^,;,]+)");
        Matcher m = labelPattern.matcher(comment);
        if (m.find()) {
            this.label = m.group(1);
        }
    }

    public String getQueryOperator() {
        return queryOperator;
    }

    private void extractSearchable(String comment) {
        // searchable: eq
        Pattern queryPattern = Pattern.compile("searchable:\\s*(\\w+)");
        Matcher m = queryPattern.matcher(comment);
        if (m.find()) {
            queryOperator = m.group(1);
        }
    }

    public String getColumnName() {
        return columnName;
    }

    public String getFieldName() {
        if (Strings.isBlank(fieldName)) {
            return Utils.LOWER_CAMEL(columnName);
        }
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public List<String> getEnumValues() {
        return enumValues;
    }

    public void setColumnType(String columnType) {
        Matcher m = ENUM_PATTERN.matcher(columnType);
        if (m.find()) {
            this.columnType = "enum";

            String s = m.group(1);
            for (String v : s.split(",")) {
                v = v.trim().replaceAll("'", "");
                enumValues.add(v);
            }
            return;
        }

        m = COLUMN_TYPE_PATTERN.matcher(columnType);
        if (m.find()) {
            if (m.group(2) != null) {
                this.size = Integer.parseInt(m.group(2));
            }
            this.columnType = m.group(1);
        } else {
            throw new IllegalArgumentException();
        }
    }

    public String getJavaType() {
        if ("tinyint".equalsIgnoreCase(dataType) && size == 1) {
            return boolean.class.getName();
        }
        if ("enum".equalsIgnoreCase(dataType)) {
            return getUpperJavaFieldName();
        }
        if (dataType != null) {
            Class<?> type = typeMapping.get(dataType.toLowerCase());
            if (type != null) {
                return type.getName();
            }
        }
        return String.class.getName();
    }

    public String getSimpleJavaTypeName() {
        return getJavaType().replaceFirst("^.*\\.", "");
    }

    public boolean isEnum() {
        return "enum".equalsIgnoreCase(dataType);
    }

    public boolean isBoolean() {
        return boolean.class.getName().equals(getJavaType());
    }

    public boolean isTimestamp() {
        return "timestamp".equalsIgnoreCase(dataType);
    }

    public String getUpperJavaFieldName() {
        return Utils.LOWER_CAMEL(columnName);
//		return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL,
//				columnName);
    }

    public String getGetterMethodName() {
        if (isBoolean()) {
            return "is" + getUpperJavaFieldName();
        }
        return "get" + getUpperJavaFieldName();
    }

    public String getSetterMethodName() {
        return "set" + getUpperJavaFieldName();
    }

    public String getColumnAnnotation() {
        if (primary) {
//			return "@Id";
            return "@Name\r\n	@Prev(els = {@EL(\"uuid()\")})";
        }
        return "@Column";
    }

    public void setDefaultValue(Object v) {
        this.defaultValue = v;
    }

    public Object getDefaultValue() {
        return defaultValue;
    }

    public String getDefaultValueCode() {
        if (isEnum()) {
            return getSimpleJavaTypeName() + "." + defaultValue;
        }
        if (isBoolean()) {
            if ("1".equals(defaultValue.toString())) {
                return "true";
            } else {
                return "false";
            }
        }
        if (isTimestamp()) {
            if (("0000-00-00 00:00:00".equals(defaultValue) || "CURRENT_TIMESTAMP"
                    .equals(defaultValue))) {
                return "DateTime.now()";
            }
        }
        if (defaultValue != null && Long.class.getName().equals(getJavaType())) {
            return defaultValue + "L";
        }
        if (defaultValue != null && BigDecimal.class.getName().equals(getJavaType())) {
            return "new BigDecimal(\"" + defaultValue.toString() + "\")";
        }
        return "\"" + getDefaultValue().toString() + "\"";
    }

    //TODO
    public String getValidationFormClass() {
        return "";
    }

}