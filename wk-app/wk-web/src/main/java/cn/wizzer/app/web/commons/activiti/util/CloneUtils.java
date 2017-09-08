package cn.wizzer.app.web.commons.activiti.util;
import org.apache.commons.lang.reflect.FieldUtils;
import org.nutz.log.Logs;

import java.lang.reflect.Field;

/**
 * 实现对象的克隆功能
 * Created by wizzer on 2015/5/21.
 */

public abstract class CloneUtils
{

    public static void copyFields(Object source, Object target, String... fieldNames)
    {
        for (String fieldName : fieldNames)
        {
            try
            {
                Field field = FieldUtils.getField(source.getClass(), fieldName, true);
                field.setAccessible(true);
                field.set(target, field.get(source));
            }
            catch (Exception e)
            {
                Logs.get().debug("",e);
            }
        }
    }
}
