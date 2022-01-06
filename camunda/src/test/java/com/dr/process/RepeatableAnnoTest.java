package com.dr.process;

import com.dr.process.camunda.annotations.SqlProxy;
import com.dr.process.camunda.command.task.instance.AbstractGetTaskQueryCmd;

import java.lang.annotation.Annotation;

public class RepeatableAnnoTest {
    public static void main(String[] args) {
        Class clazz = AbstractGetTaskQueryCmd.TaskQueryImplWithExtend.class;
        SqlProxy.SqlProxies sqlProxies = (SqlProxy.SqlProxies) clazz.getAnnotation(SqlProxy.SqlProxies.class);

        for (SqlProxy sqlProxy : sqlProxies.value()) {
            System.out.println(sqlProxy.methodName());
        }

        for (Annotation sqlProxy : clazz.getAnnotationsByType(SqlProxy.class)) {
            System.out.println(sqlProxy.toString());
        }


    }
}
