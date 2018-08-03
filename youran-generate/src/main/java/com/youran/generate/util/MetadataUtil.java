package com.youran.generate.util;

import com.google.common.base.Joiner;
import com.youran.generate.constant.JFieldType;
import com.youran.generate.constant.MySqlType;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Title:
 * Description:
 * Author: cbb
 * Create Time:2017/5/21 22:51
 */
public class MetadataUtil {

    public static List<String> MYSQL_KEYWORD;

    static{
        try {
            InputStream stream = MetadataUtil.class.getClassLoader().getResourceAsStream("mysql_keyword.txt");
            MYSQL_KEYWORD = IOUtils.readLines(stream, "utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将mysql中的关键字加``包裹
     * @return
     */
    public static String wrapMysqlKeyword(String fieldName){
        if(MYSQL_KEYWORD.contains(fieldName.toUpperCase())){
            return "`"+fieldName+"`";
        }
        return fieldName;
    }


    /**
     * 映射swagger字段类型常量
     * @param jfieldType
     * @return
     */
    public static String getSwaggerType(String jfieldType){
        JFieldType jFieldType = JFieldType.find(jfieldType);
        if(jFieldType==JFieldType.INTEGER){
            return "int";
        }
        return jfieldType;
    }




    /**
     * 获取主键别名
     * @param className
     * @param forSql
     * @return
     */
    public static String getPkAlias(String className,boolean forSql){
        String alias = StringUtils.uncapitalize(className)+"Id";
        if(forSql){
            String[] split = StringUtils.splitByCharacterTypeCamelCase(alias);
            String join = Joiner.on("_").join(split);
            alias = join.toLowerCase();
        }
        return alias;
    }

    /**
     * 是否需要展示字段长度
     * @param fieldType
     * @return
     */
    public static boolean showFieldLength(String fieldType){
        return !MySqlType.DATETIME.equals(fieldType)
            && !MySqlType.TEXT.equals(fieldType);
    }
    /**
     * 是否需要展示字段精度
     * @param fieldType
     * @return
     */
    public static boolean showFieldScale(String fieldType){
        return MySqlType.DECIMAL.equals(fieldType)
            ||MySqlType.DOUBLE.equals(fieldType)
            ||MySqlType.FLOAT.equals(fieldType);
    }


    /**
     * 下划线转驼峰
     * @param name
     * @param capFirst
     * @return
     */
    public static String underlineToCamelCase(String name,boolean capFirst){
        String[] split = StringUtils.split(name,"_");
        String value = Arrays.stream(split)
            .map(s -> StringUtils.capitalize(s.toLowerCase()))
            .collect(Collectors.joining(""));
        if(!capFirst){
            return StringUtils.uncapitalize(value);
        }
        return value;
    }

    /**
     * 驼峰转下划线
     * @param name
     * @param upCase
     * @return
     */
    public static String camelCaseToUnderline(String name,boolean upCase){
        String[] split = StringUtils.splitByCharacterTypeCamelCase(name);
        Stream<String> stream = Arrays.stream(split);
        if(upCase){
            stream = stream.map(String::toUpperCase);
        }else{
            stream = stream.map(String::toLowerCase);
        }
        return stream.collect(Collectors.joining("_"));
    }


}
