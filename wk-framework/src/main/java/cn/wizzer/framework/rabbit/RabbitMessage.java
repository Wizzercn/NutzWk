package cn.wizzer.framework.rabbit;

import org.nutz.lang.Lang;
import org.nutz.lang.util.NutMap;

import java.io.Serializable;

/**
 * 消息实体类
 * Created by wizzer on 2016/12/29.
 */
public class RabbitMessage implements Serializable {
    private static final long serialVersionUID = -6778170718151494509L;

    private String exchange;//交换器

    private byte[] body;

    private String routeKey;//路由key

    public RabbitMessage() {
    }

    public RabbitMessage(String exchange, String routeKey, NutMap nutMap) {
        this.exchange = exchange;
        this.routeKey = routeKey;
        this.body = Lang.toBytes(nutMap);
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public String getRouteKey() {
        return routeKey;
    }

    public void setRouteKey(String routeKey) {
        this.routeKey = routeKey;
    }
}
