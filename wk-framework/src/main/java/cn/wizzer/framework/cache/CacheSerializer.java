package cn.wizzer.framework.cache;

/**
 * 缓存序列化器, 实现类必须是线程安全的
 * Created by wendal,wizzer on 2017/1/21.
 *
 */
public interface CacheSerializer {
    
    /**
     * 如果对象无法序列化,返回null
     */
    Object fromObject(Object obj);
    
    /**
     * 要求: 如果对象无法还原,返回null
     */
    Object toObject(Object obj);

}
