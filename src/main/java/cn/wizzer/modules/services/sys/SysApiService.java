package cn.wizzer.modules.services.sys;

import cn.wizzer.common.base.Service;
import cn.wizzer.modules.models.sys.Sys_api;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.log.Log;
import org.nutz.log.Logs;

import java.security.Key;
import java.util.Date;

/**
 * Created by wizzer on 2016/8/11.
 */
@IocBean(args = {"refer:dao"})
public class SysApiService extends Service<Sys_api> {
    private static final Log log = Logs.get();

    public SysApiService(Dao dao) {
        super(dao);
    }

    public static Key key;

    /**
     * 生成token
     *
     * @param date  失效时间
     * @param appId AppId
     * @return
     */
    public String generateToken(Date date, String appId) {

        if (key == null)
            key = MacProvider.generateKey();
        return Jwts.builder()
                .setSubject(appId)
                .signWith(SignatureAlgorithm.HS512, key)
                .setExpiration(date)
                .compact();
    }

    /**
     * 验证token
     * @param appId AppId
     * @param token token
     * @return
     */
    public boolean verifyToken(String appId, String token) {
        try {
            Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody().getSubject().equals(appId);
            return true;
        } catch (Exception e) {
            log.debug(e.getMessage());
            return false;
        }
    }
}
