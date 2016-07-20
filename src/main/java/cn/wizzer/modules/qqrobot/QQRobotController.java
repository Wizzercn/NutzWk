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
    	  //群消息start
    	  if(data!=null && "BJ2016888".equals(data.getString("Key")) && "ClusterIM".equals(data.getString("Event"))){
    		  Sys_chat_log  chatlog = new Sys_chat_log();
    		  String groupId = data.getString("GroupId");
    		  String message = data.getString("Message");
    		  String sender = data.getString("Sender");
    		  Integer sendTime = Integer.valueOf(data.getString("SendTime"));
    		         chatlog.setGroupId(groupId);
    		         chatlog.setGroupName(data.getString("GroupId"));
    		         chatlog.setMessage(message);
    		         chatlog.setSender(sender);
    		         chatlog.setSenderName(data.getString("SenderName"));
    		         chatlog.setCreatedAt(sendTime);
    		         qunService.saveChatLog(chatlog);
    		 //验证用户是否是黑名单，如果是黑名单提示：
    		 List<Sys_qun_black_user> sendSelf =  qunService.getDatas(sender);    
    		 if(sendSelf!=null && sendSelf.size()>0){
    			 return "群主，群成员["+sender+"]已经被圈内人士举报了["+sendSelf.size()+"]次,最近一次举报的原因是:"+sendSelf.get(0).getText()+",如有误报请联系小助手删除提示，谢谢！";
    		 }
    		 if(StringUtils.isNotBlank(message)){
    			 if (StringUtils.contains(message, bcmd)) {
    		            String[] qqInfo = message.split(bcmd);
    		            if(qqInfo!=null && qqInfo.length==2 && qqInfo[0].length()<=11 && !qqInfo[0].equals(sender)){
    	    		           Sys_qun_black_user  blackUser= new Sys_qun_black_user();
    	    		                  blackUser.setContact(qqInfo[0].trim());
    	    		                  blackUser.setText(qqInfo[1]);
    	    		                  blackUser.setCreatedAt(sendTime);
    	    		                  blackUser.setSender(sender);
    	    		                  qunService.save(blackUser);
    	    		            return "已经成功添加【"+qqInfo[0]+"】到圈子黑名单，告诉兄弟姐妹们，遇到这个渣渣绕道走，黑名单查看方式回复：#"+qqInfo[0]+"";
    		            }
    		            if(qqInfo[0].equals(sender)){
    		            	    return "自己举报自己，你是开玩笑的吧，正确格式为：别人的QQ微信电话###这后面是举报的原因";
    		            }
    		            return "黑名单提交的格式不正确，正确格式为：1234567###这后面是举报的原因";
    		     }
    			 if(Strings.startsWithChar(message, cmd)){
    				 List<Sys_qun_black_user> blackList =  qunService.getDatas(message.substring(1));
    				 if(blackList!=null && blackList.size()>0){
    					 return "【"+message.substring(1)+"】共被举报"+blackList.size()+"次,最近一次举报原因："+blackList.get(0).getText();
    				 }else{
    					 return "【"+message.substring(1)+"】是个好人,......,截止"+DateUtil.getDate()+"还未收到圈内大拿举报。举报格式：1234567###这后面是举报的原因";
    				 }
    			 }
    			 
    		 }
    	  }
    	  //群消息end  
    	  if(data!=null&& "BJ2016888".equals(data.getString("Key")) && "ClusterMemberJoin".equals(data.getString("Event"))){
    		  String sender = data.getString("Sender");
    		  String operator = data.getString("Operator");
    		  List<Sys_qun_black_user> joinQun =  qunService.getDatas(sender);  
    		  if(joinQun!=null && joinQun.size()>0){
     			 return "@"+operator+"群主，群成员["+sender+"]已经被圈内人士举报了["+joinQun.size()+"]次,最近一次举报的原因是:"+joinQun.get(0).getText()+",如有误报请联系小助手删除提示，谢谢！";
     		  }else{
     			 return "@"+sender+",欢迎["+data.getString("SenderName")+"],加入本群，进群看公告，服从管理员管理，谢谢合作，如遇坏人请提交到坏人信息到本群，同时只要有小助手的群，在被举报人加群或者发言时，都会警示群友。小助手提供的功能，1，提交黑名单(格式：qq或者微信或者电话###说明为嘛是个坏人,注意QQ或者微信或者电话只能一个，如果有多个联系方式，请提交多次),2,黑名单查询(格式:#qq或者微信或者电话),3,黑名单进QQ群，发言时危险提示。4，如需此功能邀请小助手入群即可，我是人民小助手，小助手";
     		  }
    	  }
          return "";
    }
    
}
