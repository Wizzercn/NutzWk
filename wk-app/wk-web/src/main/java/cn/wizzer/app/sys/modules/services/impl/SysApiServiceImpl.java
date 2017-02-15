package cn.wizzer.app.sys.modules.services.impl;

import cn.wizzer.app.sys.modules.models.Sys_api;
import cn.wizzer.app.sys.modules.services.SysApiService;
import cn.wizzer.app.web.commons.base.Globals;
import cn.wizzer.framework.base.service.BaseServiceImpl;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Files;

import java.io.*;
import java.security.Key;
import java.util.Date;

/**
 * Created by wizzer on 2016/12/23.
 */
@IocBean(args = {"refer:dao"})
public class SysApiServiceImpl extends BaseServiceImpl<Sys_api> implements SysApiService {
    public SysApiServiceImpl(Dao dao) {
        super(dao);
    }

    public static Key key;

    /**
     * 生成token
     *
     * @param date 失效时间
     * @param appId  appId
     * @return
     */
    public String generateToken(Date date, String appId) throws IOException, ClassNotFoundException {
        File f = new File(Globals.AppRoot + "/WEB-INF/apikey/" + appId + ".key");
        if (Files.isFile(f)) {
            ObjectInputStream keyIn = new ObjectInputStream(new FileInputStream(f));
            key = (Key) keyIn.readObject();
            keyIn.close();
        } else {
            key = MacProvider.generateKey();
            Files.createNewFile(f);
            ObjectOutputStream keyOut = new ObjectOutputStream(new FileOutputStream(f));
            keyOut.writeObject(key);
            keyOut.close();
        }

        return Jwts.builder()
                .setSubject(appId)
                .signWith(SignatureAlgorithm.HS512, key)
                .setExpiration(date)
                .compact();
    }

    /**
     * 验证token
     *
     * @param appId AppId
     * @param token token
     * @return
     */
    public boolean verifyToken(String appId, String token) {
        try {
            if (key == null) {
                ObjectInputStream keyIn = new ObjectInputStream(new FileInputStream(Globals.AppRoot + "/WEB-INF/apikey/" + appId + ".key"));
                key = (Key) keyIn.readObject();
                keyIn.close();
            }
            return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody().getSubject().equals(appId);
        } catch (Exception e) {
            return false;
        }
    }
}