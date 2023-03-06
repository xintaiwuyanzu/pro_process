package com.dr.process.camunda.config;

import com.dr.process.camunda.parselistener.CustomerEventListenerParser;
import com.dr.process.camunda.parselistener.FixTransitionBpmnParseListener;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.camunda.bpm.engine.impl.bpmn.parser.BpmnParseListener;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.impl.event.EventHandler;
import org.camunda.bpm.engine.impl.interceptor.CommandContext;
import org.camunda.bpm.engine.impl.persistence.entity.EventSubscriptionEntity;
import org.camunda.bpm.spring.boot.starter.configuration.CamundaProcessEngineConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 用来拦截xml定义文件的解析监听
 * 然后修改内容
 *
 * @author dr
 */
public class ParserListenerPlugin implements CamundaProcessEngineConfiguration {
    Logger logger = LoggerFactory.getLogger(ParserListenerPlugin.class);

    @Override
    public void preInit(ProcessEngineConfigurationImpl processEngineConfiguration) {
        //流程相关统计
        processEngineConfiguration.setMetricsEnabled(false);
        parseEventListener(processEngineConfiguration);

        processEngineConfiguration.getCustomEventHandlers().add(new EventHandler() {
            @Override
            public String getEventHandlerType() {
                return ExecutionListener.EVENTNAME_START;
            }

            @Override
            public void handleEvent(EventSubscriptionEntity eventSubscription, Object payload, Object localPayload, String businessKey, CommandContext commandContext) {
                logger.info(payload.toString());
            }
        });
    }

    /**
     * 处理解析器，添加默认的监听器
     *
     * @param processEngineConfiguration
     */
    private void parseEventListener(ProcessEngineConfigurationImpl processEngineConfiguration) {
        List<BpmnParseListener> bpmnParseListeners = processEngineConfiguration.getCustomPostBPMNParseListeners();
        if (bpmnParseListeners == null) {
            bpmnParseListeners = new ArrayList<>();
            processEngineConfiguration.setCustomPostBPMNParseListeners(bpmnParseListeners);
        }
        bpmnParseListeners.add(new CustomerEventListenerParser(processEngineConfiguration.getExpressionManager()));
        bpmnParseListeners.add(new FixTransitionBpmnParseListener(processEngineConfiguration.getExpressionManager()));
    }
}
