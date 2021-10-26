package com.dr.process;

import com.dr.framework.core.process.bo.ProcessDefinition;
import com.dr.framework.core.process.service.ProcessService;
import com.dr.process.camunda.service.ProcessDeployService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;

@SpringBootTest(classes = CamundaApplication.class)
@RunWith(SpringRunner.class)
public class ProcessDefinitionTest {
    Logger logger = LoggerFactory.getLogger(ProcessDefinitionTest.class);

    @Autowired
    ProcessDeployService processDeployService;
    @Autowired
    ProcessService processService;

    @Test
    public void testDeploy() throws IOException {
        ClassPathResource resource = new ClassPathResource("/processes/deployTest.bpmn");
        List<ProcessDefinition> definitionList = processDeployService.deploy(resource.getFilename(), resource.getInputStream());
        Assert.assertTrue(definitionList.size() == 1);
    }

    @Test
    public void testSelect() {
        processService.processDefinitionList(null);
    }

    @Test
    public void testDelete() {
        List<ProcessDefinition> processDefinitions = processService.processDefinitionList(null);
        processDeployService.deleteProcessByDefinitionId(processDefinitions.get(0).getId());
    }

}
