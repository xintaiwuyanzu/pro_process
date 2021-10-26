package com.dr.process.camunda.command.process.definition.extend;

import com.dr.framework.common.entity.BaseEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;
import com.dr.process.camunda.utils.Constants;
import org.camunda.bpm.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.camunda.bpm.engine.impl.persistence.entity.TableDataManager;

/**
 * 流程定义扩展表
 * <p>
 * 用来补全流程基本信息
 * 表名称参考
 *
 * @author dr
 * @see TableDataManager#persistentObjectToTableNameMap
 * @see ProcessDefinitionEntity
 */
@Table(name = "ACT_RE_PROCODEF" + Constants.EXTEND_TABLE_NAME_SUFFIX, module = Constants.MODULE_NAME, comment = "流程定义扩展表")
public
class ProcessDefinitionExtendEntity extends BaseEntity {
    //这个表的主键就是流程定义的主键
    @Column(name = "processType", comment = "流程类型")
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
