package cn.wizzer.framework.shiro;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;
import org.nutz.lang.random.R;

import java.io.Serializable;

/**
 * 使用UU32生成session id, 减少其长度
 * Created by wizzer on 2017/1/11.
 */
public class UU32SessionIdGenerator implements SessionIdGenerator {
    public Serializable generateId(Session session) {
        return R.UU32();
    }
}
