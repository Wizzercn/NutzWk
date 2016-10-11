package cn.wizzer.modules.services.sys;

import cn.wizzer.common.base.Globals;
import cn.wizzer.common.base.Result;
import cn.wizzer.common.base.Service;
import cn.wizzer.modules.models.sys.Sys_api;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.lang.Files;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;

import java.io.*;
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
     * @param date 失效时间
     * @param appId  appId
     * @return
     */
    public String generateToken(Date date, String appId) throws IOException, ClassNotFoundException {
        File f = new File(Globals.AppRoot + "/WEB-INF/apikey/" + appId + ".txt");
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
                ObjectInputStream keyIn = new ObjectInputStream(new FileInputStream(Globals.AppRoot + "/WEB-INF/apikey/" + appId + ".txt"));
                key = (Key) keyIn.readObject();
                keyIn.close();
            }
            Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody().getSubject().equals(appId);
            return true;
        } catch (Exception e) {
            log.debug(e.getMessage());
            return false;
        }
    }
}
