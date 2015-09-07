package cn.wizzer.common.service;


import org.nutz.ioc.aop.Aop;
import org.nutz.ioc.loader.annotation.IocBean;
import static cn.wizzer.common.util.RedisInterceptor.*;

@IocBean
public class RedisService {
	
	@Aop("redis")
	public void set(String key, String val) {
		jedis().set(key, val);
	}

	@Aop("redis")
	public String get(String key) {
		return jedis().get(key);
	}
}
