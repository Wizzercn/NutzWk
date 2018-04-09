package cn.wizzer.app.code.commons.builder;

import org.nutz.ioc.Ioc;

import java.util.Map;

/**
 * 基础的数据结构加载器
 * Created by wizzer on 2017/1/23.
 */
public abstract class Loader {

    public abstract Map<String, TableDescriptor> loadTables(Ioc ioc,
                                                            String basePackageName, String basePath, String baseUri, String servPackageName, String modPackageName) throws Exception;


}
