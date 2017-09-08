package cn.wizzer.app.web.commons.activiti.listener;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

import java.util.Collections;
import java.util.List;

/**
 * Created by wizzer on 2015/5/18.
 */
public class ProxyTaskListener implements TaskListener {
    private List<TaskListener> taskListeners = Collections.EMPTY_LIST;

    public void notify(DelegateTask delegateTask) {
        for (TaskListener taskListener : taskListeners) {
            taskListener.notify(delegateTask);
        }
    }

    public void setTaskListeners(List<TaskListener> taskListeners) {
        this.taskListeners = taskListeners;
    }
}
