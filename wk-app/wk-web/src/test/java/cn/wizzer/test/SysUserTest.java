package cn.wizzer.test;

import cn.wizzer.app.TestBase;
import cn.wizzer.app.sys.modules.models.Sys_user;
import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.junit.Test;
import org.nutz.dao.Dao;

/**
 * Created by wizzer on 2017/5/19.
 */
public class SysUserTest extends TestBase {

    @Test
    public void testInsert() {
//        Sys_user user = new Sys_user();
//        user.setLoginname("test");
//        user.setUsername("测试帐号");
//        user.setOpAt((int) (System.currentTimeMillis() / 1000));
//        RandomNumberGenerator rng = new SecureRandomNumberGenerator();
//        String salt = rng.nextBytes().toBase64();
//        String hashedPasswordBase64 = new Sha256Hash("1", salt, 1024).toBase64();
//        user.setSalt(salt);
//        user.setPassword(hashedPasswordBase64);
//        user.setLoginIp("127.0.0.1");
//        user.setLoginAt(0);
//        user.setLoginCount(0);
//        user.setEmail("wizzer@qq.com");
//        user.setLoginTheme("palette.css");
//        user.setLoginBoxed(false);
//        user.setLoginScroll(false);
//        user.setLoginSidebar(false);
//        user.setLoginPjax(true);
//        user.setUnitid("");
//        Sys_user dbuser = ioc.get(Dao.class).insert(user);
//        assertTrue(dbuser != null);
//        assertTrue(!dbuser.isDisabled());

    }
}
