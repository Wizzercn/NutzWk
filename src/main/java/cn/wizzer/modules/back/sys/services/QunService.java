package cn.wizzer.modules.back.sys.services;

import java.util.List;

import org.nutz.aop.interceptor.ioc.TransAop;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.entity.Entity;
import org.nutz.dao.entity.Record;
import org.nutz.dao.sql.Sql;
import org.nutz.ioc.aop.Aop;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.log.Log;
import org.nutz.log.Logs;

import cn.wizzer.common.base.Service;
import cn.wizzer.modules.back.sys.models.Sys_chat_log;
import cn.wizzer.modules.back.sys.models.Sys_qun_black_user;

/**
 * 群用户服务
 */
@IocBean(args = {"refer:dao"})
public class QunService extends Service<Sys_qun_black_user> {
	 private static final Log log = Logs.get();
	@Inject
	private Dao dao43;
    public QunService(Dao dao) {
        super(dao);
    }
    
    /**
     * 新增QQ
     *
     * @param unit
     * @param pid
     */
    @Aop(TransAop.READ_COMMITTED)
    public void save(Sys_qun_black_user qq) {
        dao().insert(qq);
    }
    /**
     * 新增QQ
     *
     * @param unit
     * @param pid
     */
    @Aop(TransAop.READ_COMMITTED)
    public void saveChatLog(Sys_chat_log chat) {
        dao().insert(chat);
    }

    /**
     * 查询用户按钮权限
     *
     * @param userId
     * @return
     */
    public List<Sys_qun_black_user> getDatas(List<String> contacts) {
    	log.info(contacts);
    	String[] strarr = new String[contacts.size()];
    	for (int i = 0; i < contacts.size(); i++) {
			 strarr[i] = contacts.get(i);
		}
        Sql sql = Sqls.create("select * from black_user_view where contact in (@contact)");
        sql.params().set("contact", strarr);
        Entity<Sys_qun_black_user> entity = dao().getEntity(Sys_qun_black_user.class);
        sql.setEntity(entity);
        sql.setCallback(Sqls.callback.entities());
        dao().execute(sql);
        return sql.getList(Sys_qun_black_user.class);
    }
 

    /**
     * 删除一个用户
     *
     * @param userId
     */
    @Aop(TransAop.READ_COMMITTED)
    public void deleteById(String userId) {
        dao().clear("sys_user_unit", Cnd.where("userId", "=", userId));
        dao().clear("sys_user_role", Cnd.where("userId", "=", userId));
        dao().clear("sys_user", Cnd.where("id", "=", userId));
    }

    /**
     * 批量删除用户
     *
     * @param userIds
     */
    @Aop(TransAop.READ_COMMITTED)
    public void deleteByIds(String[] userIds) {
        dao().clear("sys_user_unit", Cnd.where("userId", "in", userIds));
        dao().clear("sys_user_role", Cnd.where("userId", "in", userIds));
        dao().clear("sys_user", Cnd.where("id", "in", userIds));
    }
    //评论列表添加数据
    //ip做发送人使用了，publish_source 作为查询码
    @Aop(TransAop.READ_COMMITTED)
    public void insert2answer(Sys_chat_log log) {
    	 if(log == null || log.getSenderName()==null ||  !log.getSenderName().toLowerCase().contains("m")){
    		 return;
    	 }
    	 
    	 //是否已经存在联系方式的帖子
    	 StringBuffer links = new StringBuffer();
    	 List<Record> question = dao43.query("aws_question", Cnd.where("qq", "=", log.getSender()));
    	 if(question!=null && question.size()>0){
    		 for(int q=0;q<question.size();q++){
    			 links.append("帖子详情和照片<a href=\"/?/question/").append(question.get(q).getString("question_id")).append("\">").append(question.get(q).getString("question_content")).append("</a> </br>");
    			 dao43.update("aws_question", Chain.make("question_detail", question.get(q).getString("question_detail")+"</br>"+log.getMessage().replaceAll("[\\[a-zA-Z:0-9.\\]]", "")), Cnd.where("question_id", "=", question.get(q).getInt("question_id")));
    		 }
    		 
    	 }else{//插入到question 表
    		 dao43.insert("aws_question", Chain.make("question_content", log.getSenderName())
    				   .add("add_time", System.currentTimeMillis()/1000)
    				   .add("update_time", System.currentTimeMillis()/1000)
    				   .add("published_uid", 1)
    				   .add("category_id", 2)
    				   .add("integral", 30)
    				   .add("question_content", log.getSenderName())
    				   .add("qq", log.getSender())
    				   .add("question_detail", "来自VIP群:"+log.getSenderName()+","+log.getMessage()));
    	 }
    	 //是否有签到
    	 
    	 int delRows = dao43.clear("aws_answer", Cnd.where("ip", "=", log.getSender()));
    	 dao43.insert("aws_answer", Chain.make("question_id",31)
    			 .add("add_time", System.currentTimeMillis()/1000)
    			 .add("uid", 422)
    			 .add("answer_content","来自VIP群:"+log.getSenderName()+","+log.getMessage()+","+links.toString())
    			 .add("category_id", 1)
    			 .add("ip", log.getSender())
    			 .add("publish_source","mobile")
    			 );
    	  
    	 if(delRows == 0){
    		 dao43.update("aws_question", Chain.makeSpecial("answer_count", "+1"), Cnd.where("question_id", "=", 31));
    	 }
    	 
         System.out.println(dao43+"------------");
    }
   
}
