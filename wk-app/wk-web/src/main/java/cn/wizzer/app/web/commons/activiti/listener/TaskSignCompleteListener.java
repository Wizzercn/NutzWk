package cn.wizzer.app.web.commons.activiti.listener;

import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.apache.commons.lang.math.NumberUtils;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.Mvcs;

/**
 * 会签Demo
 * Created by wizzer on 2015/5/6.
 */
public class TaskSignCompleteListener implements TaskListener {
    private final Log log = Logs.get();

    TaskService taskService= Mvcs.ctx().getDefaultIoc().get(TaskService.class);
    @Override
    public void notify(DelegateTask delegateTask) {
        String approved = Strings.sNull(delegateTask.getVariable("ok"));
        String isOk = Strings.sNull(delegateTask.getVariable("isOk"));
        log.info("isOk::"+isOk);
        if (approved.equals("true")) {
            int okNum = NumberUtils.toInt(Strings.sNull(delegateTask.getVariable("okNum")));
            delegateTask.setVariable("okNum", okNum + 1);
        }
//        if(isOk.equals("false")){
//            List<Task> list=taskService.createTaskQuery().().processInstanceId(delegateTask.getProcessInstanceId())..list();
//            for (Task task:list){
//                if(!task.getId().equals(delegateTask.getId())) {
//                    log.info("id:::"+task.getId());
//                    taskService.deleteTask(task.getId());
//                }
//            }
//        }
    }
}

