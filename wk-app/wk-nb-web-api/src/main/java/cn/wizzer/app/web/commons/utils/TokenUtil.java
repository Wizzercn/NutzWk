package cn.wizzer.app.web.commons.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;
import org.nutz.integration.jedis.RedisService;
import org.nutz.ioc.aop.Aop;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;

import java.io.*;
import java.security.Key;
import java.util.Date;


/**
 * Created by wizzer on 2018/4/4.
 */
@IocBean
public class TokenUtil {
    @Inject
    private RedisService redisService;

    /**
     * 获取KEY
     *
     * @param appid appid
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private Key getKey(String appid) throws IOException, ClassNotFoundException {
        Key key;
        byte[] obj = redisService.get(("api_token:" + appid).getBytes());
        if (obj != null) {
            ObjectInputStream keyIn = new ObjectInputStream(new ByteArrayInputStream(obj));
            key = (Key) keyIn.readObject();
            keyIn.close();
        } else {
            key = MacProvider.generateKey();
            ByteArrayOutputStream bao = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bao);
            oos.writeObject(key);
            obj = bao.toByteArray();
            redisService.set(("api_token:" + appid).getBytes(), obj);
            redisService.expire(("api_token:" + appid).getBytes(), 7202);//2小时零2秒后自动删除
        }
        return key;
    }

    /**
     * 生成token
     *
     * @param date  失效时间
     * @param appid appid
     * @return
     */
    public String generateToken(Date date, String appid) throws IOException, ClassNotFoundException {
        return Jwts.builder()
                .setSubject(appid)
                .signWith(SignatureAlgorithm.HS512, getKey(appid))
                .setExpiration(date)
                .compact();
    }

    /**
     * 验证token
     *
     * @param appid appid
     * @param token token
     * @return
     */
    public boolean verifyToken(String appid, String token) {
        try {
            return Jwts.parser().setSigningKey(getKey(appid)).parseClaimsJws(token).getBody().getSubject().equals(appid);
        } catch (Exception e) {
            return false;
        }
    }
}
