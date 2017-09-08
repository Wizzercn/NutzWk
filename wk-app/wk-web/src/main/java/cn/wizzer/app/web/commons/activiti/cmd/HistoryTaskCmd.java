package cn.wizzer.app.web.commons.activiti.cmd;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.pvm.PvmActivity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Sql;
import org.nutz.dao.sql.SqlCallback;
import org.nutz.lang.Strings;
import org.nutz.lang.util.NutMap;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.Mvcs;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 获取已办历史节点
 * Created by wizzer on 2015/5/13.
 */

public class HistoryTaskCmd implements Command<NutMap> {

    private RepositoryService repositoryService = Mvcs.ctx().getDefaultIoc().get(RepositoryService.class);
    private Dao dao = Mvcs.ctx().getDefaultIoc().get(Dao.class);
    private final Log log = Logs.get();
    private String taskId;
    private static int i = 0;

    public HistoryTaskCmd(String taskId) {
        this.taskId = taskId;
    }

    public NutMap execute(CommandContext commandContext) {
        NutMap map = new NutMap();
        TaskEntity taskEntity = Context.getCommandContext()
                .getTaskEntityManager().findTaskById(taskId);
        if (taskEntity == null) {
            log.debug("cannot find task : " + taskId);
            map.setv("errcode", 1);
            map.setv("errmsg", "cannot find task : " + taskId);
            return map;
        }
        map.setv("errcode", 0);
        map.setv("errmsg", "");
        //查找已经历过的环节

        Sql sql = Sqls.create("select ACT_ID_,ACT_NAME_ from ACT_HI_ACTINST where  ACT_TYPE_='userTask' AND END_TIME_ is not null AND PROC_INST_ID_=@pid");
        sql.params().set("pid", taskEntity.getProcessInstanceId());
        Map map1 = getHashMap(dao, sql);

        //查找定义的上环节
        NutMap map2 = new NutMap();
        ProcessDefinitionEntity def = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService).getDeployedProcessDefinition(taskEntity.getProcessDefinitionId());
        List<ActivityImpl> activitiList = def.getActivities();
        getTaskActivitys(taskEntity.getTaskDefinitionKey(), activitiList, map2);
        Set<String> key = map2.keySet();
        List<NutMap> list = new ArrayList<>();
        for (Iterator it = key.iterator(); it.hasNext(); ) {
            NutMap m = new NutMap();
            String s = (String) it.next();
            if (map1.get(s) != null) {
                m.setv("id", s);
                m.setv("name", map2.get(s));
                list.add(m);
            }
        }
        map.setv("list", list);
        return map;
    }

    //获取流程定义的上级所有节点
    public static List<PvmActivity> getTaskActivitys(String activityId, List<ActivityImpl> activityList, NutMap map) {
        if (activityId.startsWith("join")) {
            //如果是前加签、后加签，替换ActivityId
            Pattern p = Pattern.compile("join:\\d+:([a-zA-Z_]+):\\d+\\-\\d+");
            Matcher m = p.matcher(activityId);
            if (m.find()) {
                activityId = m.group(1);
            }
        }
        List<PvmActivity> activitiyIds = new ArrayList<>();
        for (ActivityImpl activityImpl : activityList) {
            String id = activityImpl.getId();
            if (activityId.equals(id)) {
                List<PvmTransition> incomingTransitions = activityImpl.getIncomingTransitions();//获取某节点所有线路
                for (PvmTransition tr : incomingTransitions) {
                    PvmActivity ac = tr.getDestination();//获取线路的终点节点
                    if (!ac.getProperty("type").equals("userTask")) {
                        activitiyIds.addAll(getTaskActivitys(ac.getId(), activityList, map));
                    } else {
                        activitiyIds.add(ac);
                    }
                }
                break;
            }
            map.setv(activityImpl.getId(), activityImpl.getProperty("name"));
        }
        return activitiyIds;
    }

    private <T> HashMap<String, String> getHashMap(Dao dao, Sql sql) {
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        sql.setCallback(new SqlCallback() {
            public Object invoke(Connection conn, ResultSet rs, Sql sql)
                    throws SQLException {
                String key = "", value = "";
                while (rs.next()) {
                    key = Strings.sNull(rs.getString(1));
                    value = Strings.sNull(rs.getString(2));
                    hashMap.put(key, value);
                }
                return null;
            }
        });
        dao.execute(sql);
        return hashMap;
    }

}
