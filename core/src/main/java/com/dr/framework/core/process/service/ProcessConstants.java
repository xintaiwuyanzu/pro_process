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
     * 默认的流程类型
     */
    String DEFAULT_PROCESS_TYPE = Constants.DEFAULT + "_process";

    /*
     *============================================================================
     * 下面是流程相关常量
     * 所有流程扩展信息都存在常量表里面了，在流程启动和环节等信息中填充相关变量
     * 后面也可以关联表实现精确查询
     *============================================================================
     */

    /**
     * 流程实例类型
     */
    String PROCESS_TYPE_KEY = "PROCESS_TYPE";
    /**
     * 流程实例创建人ID
     */
    String PROCESS_CREATE_PERSON_KEY = "CREATE_PERSON";
    /**
     * 流程实例创建人名称
     */
    String PROCESS_CREATE_NAME_KEY = "CREATE_PERSON_NAME";
    /**
     * 流程办结人ID
     */
    String PROCESS_END_PERSON_KEY = "END_PERSON";
    /**
     * 流程办结人名称
     */
    String PROCESS_END_NAME_KEY = "END_PERSON";
    /**
     * 流程实例标题
     */
    String PROCESS_TITLE_KEY = "$title";
    /**
     * 描述key
     */
    String PROCESS_DESCRIPTION_KEY = "$description";

    /**
     * 详情url
     */
    String PROCESS_FORM_URL_KEY = "$formUrl";
    /**
     * 业务关联Id
     */
    String PROCESS_BUSINESS_KEY = "$businessId";

    String CREATE_DATE_KEY = "CREATE_DATE";


    String OWNER_KEY = "OWNER_PERSON";
    String OWNER_NAME_KEY = "OWNER_PERSON_NAME";


    String ASSIGNEE_KEY = "assignee";
    String ASSIGNEE_NAME_KEY = "ASSIGNEE_NAME";

    /**
     * 下一环节Id
     */
    String VAR_NEXT_TASK_ID = "$nextId";
    /**
     * 下一环节人接收人Id，可以是多个，多个以逗号隔开
     */
    String VAR_NEXT_TASK_PERSON = "$nextPerson";
    /**
     * 备注信息key
     */
    String VAR_COMMENT_KEY = "$comment";

}


