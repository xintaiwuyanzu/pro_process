package com.dr.framework.core.process.service;

import com.dr.framework.core.util.Constants;

/**
 * 流程相关常量
 *
 * @author dr
 */
public interface ProcessConstants {
    /**
     * 默认是否查询最新版本流程定义
     */
    boolean DEFAULT_LATEST_VERSION = true;
    /**
     * 默认是否查询流程定义属性
     */
    boolean DEFAULT_WITH_PROPERTIES = false;
    /**
     * 流程实例类型
     */
    String PROCESS_TYPE_KEY = "PROCESS_TYPE";
    String DEFAULT_PROCESS_TYPE = Constants.DEFAULT + "_process";
    String CREATE_KEY = "CREATE_PERSON";
    String CREATE_NAME_KEY = "CREATE_PERSON_NAME";
    String CREATE_DATE_KEY = "CREATE_DATE";


    String OWNER_KEY = "OWNER_PERSON";
    String OWNER_NAME_KEY = "OWNER_PERSON_NAME";


    String ASSIGNEE_KEY = "assignee";
    String ASSIGNEE_NAME_KEY = "ASSIGNEE_NAME";

    /**
     * 标题key
     */
    String TITLE_KEY = "$title";
    /**
     * 描述key
     */
    String DESCRIPTION_KEY = "$description";
    /**
     * 详情url
     */
    String FORM_URL_KEY = "$formUrl";
    /**
     * 业务关联Id
     */
    String BUSINESS_KEY = "$businessId";

    /**
     * 下一环节Id
     */
    String NEXT_TASK_ID = "$nextId";
    /**
     * 下一环节人接收人Id，可以是多个，多个以逗号隔开
     */
    String NEXT_TASK_PERSON = "$nextPerson";
    /**
     * 备注信息key
     */
    String COMMENT_KEY = "$comment";

}


