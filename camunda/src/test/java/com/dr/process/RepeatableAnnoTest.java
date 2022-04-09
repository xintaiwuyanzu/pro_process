package com.dr.process;

import com.dr.process.camunda.annotations.SqlProxy;
import com.dr.process.camunda.command.process.definition.AbstractGetProcessQueryCmd;

import java.lang.annotation.Annotation;

public class RepeatableAnnoTest {
    public static void main(String[] args) {

        SqlProxy.SqlProxies sqlProxies = AbstractGetProcessQueryCmd.ProcessDefinitionQueryImplWithExtend.class.getAnnotation(SqlProxy.SqlProxies.class);

        for (SqlProxy sqlProxy : sqlProxies.value()) {
            System.out.println(sqlProxy.methodName());
        }

        for (Annotation sqlProxy : AbstractGetProcessQueryCmd.ProcessDefinitionQueryImplWithExtend.class.getAnnotationsByType(SqlProxy.class)) {
            System.out.println(sqlProxy.toString());
        }


    }
}
