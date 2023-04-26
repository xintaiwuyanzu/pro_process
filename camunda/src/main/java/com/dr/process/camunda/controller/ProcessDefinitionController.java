package com.dr.process.camunda.controller;

import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.core.process.bo.ProcessDefinition;
import com.dr.framework.core.process.controller.AbstractProcessDefinitionController;
import com.dr.process.camunda.service.ProcessDeployService;
import org.camunda.bpm.engine.RepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 流程控制器
 *
 * @author dr
 */
@RestController
@RequestMapping("${common.api-path:/api}/processDefinition")
public class ProcessDefinitionController extends AbstractProcessDefinitionController {
    @Autowired
    RepositoryService repositoryService;

    private ProcessDeployService processDeployService;

    public ProcessDefinitionController(ProcessDeployService processDeployService) {
        this.processDeployService = processDeployService;
    }

    /**
     * 部署流程定义
     *
     * @param type 流程类型
     * @param id   流程Id
     * @param xml  bpmn xml内容
     * @return
     */
    @PostMapping("/deploy")
    public ResultEntity<ProcessDefinition> deploy(String type, String id, String xml) {
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8))) {
            List<ProcessDefinition> processDefinitions = getProcessDeployService().deploy(type, inputStream);
            ProcessDefinition result;
            if (processDefinitions.isEmpty()) {
                //重复发布的可能返回为空
                if (StringUtils.hasText(id)) {
                    //尝试根据id查询发布信息
                    result = getProcessDefinitionService().getProcessDefinitionById(id);
                } else {
                    return ResultEntity.error("添加流程失败，请检查流程定义重试");
                }
            } else {
                //TODO 发布多个流程的情况未处理
                result = processDefinitions.get(0);
            }
            return ResultEntity.success(result);
        } catch (IOException e) {
            e.printStackTrace();
            return ResultEntity.error("添加流程失败，请检查流程定义重试");
        }
    }

    /**
     * 删除流程定义
     *
     * @param id         流程定义Id
     * @param allVersion 是否删除所有版本
     *                   这里没有添加级联删除参数，不级联删除数据
     * @return
     */
    @PostMapping("/delete")
    public ResultEntity<String> delete(String id, boolean allVersion) {
        if (allVersion) {
            ProcessDefinition processDefinition = getProcessDefinitionService().getProcessDefinitionById(id);
            getProcessDeployService().deleteProcessByDefinitionKey(processDefinition.getKey());
        } else {
            getProcessDeployService().deleteProcessByDefinitionId(id);
        }
        return ResultEntity.success("删除成功！");
    }

    /**
     * 根据流程定义Id查询流程xml文件
     *
     * @param processDefinitionId
     * @return
     */
    @PostMapping("/xml")
    public ResultEntity<String> bpmnXml(String processDefinitionId) throws IOException {
        InputStream stream = getProcessDeployService().getDeployResourceById(processDefinitionId);
        return ResultEntity.success(StreamUtils.copyToString(stream, StandardCharsets.UTF_8));
    }

    /**
     * 根据流程id获取流程基本信息
     *
     * @param processDefinitionId
     * @return
     */
    @PostMapping("/detail")
    public ResultEntity<ProcessDefinition> detail(String processDefinitionId) {
        return ResultEntity.success(getProcessDefinitionService().getProcessDefinitionById(processDefinitionId));
    }

    /**
     * 根据环节定义id获取对应角色人员
     *
     * @param processDefinitionId 流程定义id
     * @param taskDefinitionId    环节定义id
     * @return
     */
    @RequestMapping("/getPersonByTaskDefinitionId")
    public ResultEntity getPersonByTaskDefinitionId(String processDefinitionId, String taskDefinitionId) {
        return ResultEntity.success(getProcessDeployService().getPersonByTaskDefinitionId(processDefinitionId, taskDefinitionId));
    }

    /**
     * 根据流程定义id取启动环节角色下的人员列表
     *
     * @param processDefinitionId 流程定义id
     * @return
     */
    @RequestMapping("/getPersonByProcessDefinitionId")
    public ResultEntity getPersonByProcessDefinitionId(String processDefinitionId) {
        return ResultEntity.success(getProcessDeployService().getPersonByProcessDefinitionId(processDefinitionId));
    }

    public ProcessDeployService getProcessDeployService() {
        return processDeployService;
    }
}
