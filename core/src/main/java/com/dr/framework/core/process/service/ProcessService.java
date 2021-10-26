package com.dr.framework.core.process.service;

/**
 * 流程相关的接口汇总
 *
 * @author dr
 */
public interface ProcessService extends ProcessDefinitionService, TaskDefinitionService, ProcessInstanceService, TaskInstanceService {
    /**
     * 默认是否查询最新版本流程定义
     */
    boolean DEFAULT_LATEST_VERSION = true;
    /**
     * 默认是否查询流程定义属性
     */
    boolean DEFAULT_WITH_PROPERTIES = false;

    String CREATE_KEY = "CREATE_PERSON";
    String CREATE_NAME_KEY = "CREATE_PERSON_NAME";
    String CREATE_DATE_KEY = "CREATE_DATE";


    String OWNER_KEY = "OWNER_PERSON";
    String OWNER_NAME_KEY = "OWNER_PERSON_NAME";


    String ASSIGNEE_KEY = "assignee";
    String ASSIGNEE_NAME_KEY = "ASSIGNEE_NAME";

    String TITLE_KEY = "title";
    String FORM_URL_KEY = "formUrl";
}


