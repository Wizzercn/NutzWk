package cn.wizzer.app.web.commons.activiti.parser;

import org.activiti.engine.impl.bpmn.parser.BpmnParser;
import org.activiti.engine.impl.bpmn.parser.factory.ActivityBehaviorFactory;
import org.activiti.engine.impl.bpmn.parser.factory.DefaultActivityBehaviorFactory;

/**
 * Created by wizzer on 2015/5/18.
 */
public class CustomBpmnParser extends BpmnParser {
    // 当使用自定义activityBehaviorFactory 时，需要重写该方法
    public void setActivityBehaviorFactory(
            ActivityBehaviorFactory activityBehaviorFactory) {
        ((DefaultActivityBehaviorFactory) activityBehaviorFactory)
                .setExpressionManager(expressionManager);
        super.setActivityBehaviorFactory(activityBehaviorFactory);
    }
}
