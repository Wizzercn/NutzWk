package cn.wizzer.app.code.commons.builder;

import org.nutz.lang.Lang;
import org.nutz.lang.Streams;
import org.nutz.lang.Strings;
import org.nutz.lang.util.ClassTools;
import org.nutz.resource.NutResource;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * 工具类
 * Created by wizzer on 2017/1/23.
 */
public class Utils {
    /**
     * 将以“_”分割的单词转换为首字母小写驼峰格式
     * @param src
     * @return
     */
    public static String  LOWER_CAMEL(String src){
        src = src.toLowerCase();
        StringBuilder result = new StringBuilder();
        for(String sitem:src.split("_")){
            if(result.toString().length()==0){
                result.append(sitem);
            }else{
                result.append(Strings.upperFirst(sitem));
            }
        }
        return result.toString();
    }

    /**
     * 以“_”分割的单词转换为首字母大写驼峰格式
     * @param src
     * @return
     */
    public static String  UPPER_CAMEL(String src){
        src = src.toLowerCase();
        StringBuilder result = new StringBuilder();
        for(String sitem:src.split("_")){
            if(result.toString().length()==0){
                result.append(Strings.upperFirst(sitem));
            }else{
                result.append(Strings.upperFirst(sitem));
            }
        }
        return result.toString();
    }

    public static String getBasePath(String path) {
        if (!Strings.isEmpty(path) && !path.endsWith("/")) {
            return path + "/";
        }
        return path;
    }
    /**
     * 将一组 NutResource 转换成 class 对象
     *
     * @param pkg  包前缀
     * @param list 列表
     * @return 类对象列表
     */
    public static List<Class<?>> rs2class(String pkg, List<NutResource> list) {
        Set<Class<?>> re = new LinkedHashSet<Class<?>>(list.size());
        if (!list.isEmpty()) {
            for (NutResource nr : list) {
                if (!nr.getName().endsWith(".class") || nr.getName().endsWith("package-info.class")) {
                    continue;
                }
                // Class快速载入
                String className = pkg + "." + nr.getName().substring(0, nr.getName().length() - 6).replaceAll("[/\\\\]", ".");
                try {
                    Class<?> klass = Lang.loadClass(className);
                    re.add(klass);
                    continue;
                } catch (Throwable e) {
                }
                // 失败了? 尝试终极方法,当然了,慢多了
                InputStream in = null;
                try {
                    in = nr.getInputStream();
                    className = ClassTools.getClassName(in);
                    if (className == null) {
                        continue;
                    }
                    Class<?> klass = Lang.loadClass(className);
                    re.add(klass);
                } catch (Throwable e) {
                } finally {
                    Streams.safeClose(in);
                }
            }
        }
        return new ArrayList<Class<?>>(re);
    }
    public static  void main(String[] args){
        System.out.println(UPPER_CAMEL("AAAA_BBBB"));
    }
}
