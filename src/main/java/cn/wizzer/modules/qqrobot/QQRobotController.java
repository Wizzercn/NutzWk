package cn.wizzer.modules.qqrobot;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.nutz.ioc.impl.PropertiesProxy;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.lang.Strings;
import org.nutz.lang.util.NutMap;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Fail;
import org.nutz.mvc.annotation.Filters;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import cn.wizzer.common.util.DateUtil;
import cn.wizzer.modules.back.sys.models.Sys_qun_black_user;
import cn.wizzer.modules.back.sys.services.QunService;

/**
 * Created by wizzer on 2016/7/3.
 */
@IocBean
@At("/qqrobot")
public class QQRobotController  {
    private static final Log log = Logs.get();
    @Inject
    protected PropertiesProxy conf;
	
	@Inject
    QunService qunService;
    
    /**
     * 命令开始符号
     */
    public static final char cmd = '#';
    public static final String bcmd = "###";
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
    public synchronized String msg(@Param("..") NutMap data)
            throws IOException {
     
    	  if(data!=null && "BJ2016888".equals(data.getString("key"))){
    		  String groupId = data.getString("GroupId");
    		  String message = data.getString("Message");
    		 if("469615022".equals(groupId) && StringUtils.isNotBlank(message)){
    			 log.info("消息信息："+Json.toJson(data));
    			 if (StringUtils.startsWith(message,bcmd)) {
    		            String[] qqInfo = message.split(bcmd);
    		            if(qqInfo==null || qqInfo.length<2){
    		            	return "黑名单提交的格式不正确，正确格式为：###QQ或者微信或者电话号码###这后面是原因";
    		            }
    		            Sys_qun_black_user  blackUser= new Sys_qun_black_user();
    		                  blackUser.setContact(qqInfo[0]);
    		                  blackUser.setContact(qqInfo[1]);
    		                  qunService.save(blackUser);
    		            return "已经成功添加【"+qqInfo[0]+"】到圈子黑名单，告诉兄弟姐妹们，遇到这个渣渣绕道走，查看方式回复：#"+qqInfo[0]+"";
    		                  
    		     }
    			 if(Strings.startsWithChar(message, cmd)){
    				 List<Sys_qun_black_user> blackList =  qunService.getDatas(message.substring(1));
    				 if(blackList!=null && blackList.size()>0){
    					 return "【"+message.substring(1)+"】被举报【"+blackList.size()+"】次\r\n最后一次举报的时间为："+DateUtil.getDate(blackList.get(0).getCreatedAt())+"\r\n举报原因："+blackList.get(0).getText();
    				 }
    			 }
    		 }
    	  }
         
          return "";
    }
 
}
