package cn.wizzer.modules.qqrobot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.servlet.http.HttpServletRequest;

import org.nutz.ioc.impl.PropertiesProxy;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.lang.util.NutMap;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Fail;
import org.nutz.mvc.annotation.Filters;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

/**
 * Created by wizzer on 2016/7/3.
 */
@IocBean
@At("/qqrobot")
public class QQRobotController  {
    private static final Log log = Logs.get();
    @Inject
    protected PropertiesProxy conf;
    /**
     * 命令开始符号
     */
    public static final char cmd = '#';
    /**
     * AT模板
     */
    public static final String AT_TPL = "@%s(%s)";

    public QQRobotController() {
       
    }

    // TODO 加上KEY认证
    @At("/msg")
    @Ok("raw")
    @Fail("void")
    @Filters
    public synchronized String msg(@Param("..") NutMap data, HttpServletRequest req)
            throws IOException {
         //String groupId = data.getString("GroupId");
    	if(req!= null){
    		InputStream is  = null;
        	BufferedReader br = null;
        	try {
    			StringBuffer sb = new StringBuffer();
    			 is = req.getInputStream();
    			InputStreamReader isr = new InputStreamReader(is);
    			 br = new BufferedReader(isr);
    			String s = "";
    			while ((s = br.readLine()) != null) {
    				sb.append(s);
    			}
    		    
    			log.info("=================sb========="+sb.toString());
    			 
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}finally{
    			if(is!=null){
    				is.close();
    			}
    			if(br!=null){
    				br.close();
    			}
    			
    		}
    	}else{
    		
    		log.info("=====================================req == null =========" );
    	}
    	
    	
        log.info("消息信息："+Json.toJson(data));
        return "好呀好";
    }
 
}
