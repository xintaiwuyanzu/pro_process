package com.dr.process;

import com.dr.framework.common.page.Page;
import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.organise.service.OrganisePersonService;
import com.dr.framework.core.process.bo.ProcessDefinition;
import com.dr.framework.core.process.bo.ProcessInstance;
import com.dr.framework.core.process.bo.TaskInstance;
import com.dr.framework.core.process.query.ProcessDefinitionQuery;
import com.dr.framework.core.process.query.ProcessQuery;
import com.dr.framework.core.process.query.TaskQuery;
import com.dr.framework.core.process.service.ProcessDefinitionService;
import com.dr.framework.core.process.service.ProcessInstanceService;
import com.dr.framework.core.process.service.TaskDefinitionService;
import com.dr.framework.core.process.service.TaskInstanceService;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest(classes = CamundaApplication.class)
@RunWith(SpringRunner.class)
public class ProcessServiceTest {
    @Autowired
    ProcessDefinitionService processService;
    @Autowired
    ProcessInstanceService processInstanceService;
    @Autowired
    TaskInstanceService taskInstanceService;
    @Autowired
    TaskDefinitionService taskDefinitionService;
    @Autowired
    OrganisePersonService organisePersonService;
    @Autowired
    RepositoryService repositoryService;
    @Autowired
    IdentityService identityService;
    ProcessDefinition processDefinition;
    Person person;

    @Autowired
    RuntimeService runtimeService;

    //@Before
    public void initDefine() {
        person = organisePersonService.getPersonById("admin");
        identityService.setAuthenticatedUserId(person.getId());
        processDefinition = processService.getProcessDefinitionByKey("sp_leave");
    }

    @Test
    public void testProcessDefinition() {
        List<ProcessDefinition> definitions = processService.processDefinitionList(new ProcessDefinitionQuery().withProperty());
        Page<ProcessDefinition> definitionPage = processService.processDefinitionPage(new ProcessDefinitionQuery().withProperty(), 0, 10);
    }


    @Test
    public void testStart() {

        Map map = new HashMap();
        map.put("assignee", "admin1");
        map.put("title", "hahha");
        map.put("formId", "aaaa");
        ProcessInstance processInstance = taskInstanceService.start(processDefinition.getId(), map, person);

        TaskQuery query = new TaskQuery().processDefinitionKeyLike("%aaa%").taskKeyNotLike("bbb").withProperty().withVariables();

        List<TaskInstance> taskObjects = taskInstanceService.taskList(query);


        map.put("title", "bbbbb");
        //  processService.complete(taskObjects.get(0).getId(), map);
        //  taskObjects = processService.taskList(query);

        //   processService.taskHistoryList(new TaskQuery().withVariables());
        //  List<ProcessObject> objects = processService.processObjectList(null);
    }


    @Test
    public void testStartUser() {

    }

    @Test
    public void testJump() {
        TaskInstance taskObject = taskInstanceService.taskList(new TaskQuery()).get(0);
        taskInstanceService.jump(taskObject.getId(), "Task_0oihs3n", "admin");
    }

    @Test
    public void testEnd() {
        ProcessDefinition processObject = processService.getProcessDefinitionByKey("sp_leave");
        Map<String, Object> map = new HashMap<>();
        map.put("title", "aaa");
        map.put("assignee", "aaa");
        map.put("formId", "aaa");
        ProcessInstance object = taskInstanceService.start(processObject.getId(), map, person);
        List<TaskInstance> objects = taskInstanceService.taskList(new TaskQuery().processInstanceIdEqual(object.getId()));

        taskInstanceService.endProcess(objects.get(0).getId(), null);

    }

    @Test
    public void testProcess() {
        List<ProcessInstance> pr = processInstanceService.processInstanceList(new ProcessQuery());
        List<ProcessInstance> processInstanceHistoryList = processInstanceService.processInstanceHistoryList(new ProcessQuery());
        repositoryService.getBpmnModelInstance(pr.get(0).getId());
    }

}
