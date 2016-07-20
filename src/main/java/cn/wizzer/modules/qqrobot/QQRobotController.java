package cn.wizzer.modules.qqrobot;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
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
import cn.wizzer.modules.back.sys.models.Sys_chat_log;
import cn.wizzer.modules.back.sys.models.Sys_qun_black_user;
import cn.wizzer.modules.back.sys.services.QunService;
 
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

    @At("/msg")
    @Ok("raw")
    @Fail("void")
    @Filters
    public synchronized String msg(@Param("..") NutMap data)
            throws IOException {
    	  log.info("消息信息："+Json.toJson(data));
    	  //Json.to
    	  
    	  
    	  if(data!=null && "BJ2016888".equals(data.getString("Key")) && "ClusterIM".equals(data.getString("Event"))){
    		  Sys_chat_log  chatlog = new Sys_chat_log();
    		  String groupId = data.getString("GroupId");
    		  String message = data.getString("Message");
    		  Integer sendTime = Integer.valueOf(data.getString("SendTime"));
    		         chatlog.setGroupId(groupId);
    		         chatlog.setGroupName(data.getString("GroupName"));
    		         chatlog.setMessage(message);
    		         chatlog.setSender(data.getString("Sender"));
    		         chatlog.setSenderName(data.getString("SenderName"));
    		         chatlog.setCreatedAt(sendTime);
    		         qunService.saveChatLog(chatlog);
    		 if("469615022".equals(groupId) && StringUtils.isNotBlank(message)){
    			 if (StringUtils.contains(message, bcmd)) {
    		            String[] qqInfo = message.split(bcmd);
    		            if(qqInfo!=null && qqInfo.length==2 && qqInfo[0].length()<=11 ){
    	    		           Sys_qun_black_user  blackUser= new Sys_qun_black_user();
    	    		                  blackUser.setContact(qqInfo[0]);
    	    		                  blackUser.setText(qqInfo[1]);
    	    		                  blackUser.setCreatedAt(sendTime);
    	    		                  qunService.save(blackUser);
    	    		            return "已经成功添加【"+qqInfo[0]+"】到圈子黑名单，告诉兄弟姐妹们，遇到这个渣渣绕道走，黑名单查看方式回复：#"+qqInfo[0]+"";
    		            }
    		            return "黑名单提交的格式不正确，正确格式为：1234567###这后面是举报的原因";
    		     }
    			 if(Strings.startsWithChar(message, cmd)){
    				 List<Sys_qun_black_user> blackList =  qunService.getDatas(message.substring(1));
    				 if(blackList!=null && blackList.size()>0){
    					 return "【"+message.substring(1)+"】共被举报【"+blackList.size()+"】次,最近一次举报原因："+blackList.get(0).getText();
    				 }else{
    					 return "【"+message.substring(1)+"】是个好人,......,截止"+DateUtil.getDate()+"还未收到圈内大拿举报。举报格式：1234567###这后面是举报的原因";
    				 }
    			 }
    		 }
    	  }
          return "";
    }
    
}
