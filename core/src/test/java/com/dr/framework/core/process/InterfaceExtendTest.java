package com.dr.framework.core.process;

import com.dr.framework.core.process.service.ProcessService;
import com.dr.framework.core.process.service.TaskDefinitionService;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class InterfaceExtendTest {
    public static void main(String[] args) {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();

        TaskDefinitionService definitionService = (TaskDefinitionService) Proxy.newProxyInstance(classLoader, new Class[]{TaskDefinitionService.class}, (proxy, method, args1) -> {
            System.out.println("invokeByTaskDefinitionService：" + method.getName());
            return null;
        });

        ProcessService processService = (ProcessService) Proxy.newProxyInstance(classLoader, new Class[]{ProcessService.class}, (proxy, method, args12) -> {
            if (method.getDeclaringClass().equals(TaskDefinitionService.class)) {
                method.invoke(definitionService, args12);
            } else {
                System.out.println("ProcessDefinitionService：" + method.getName());
            }
            return null;
        });

        Class processServiceClazz = ProcessService.class;
        for (Method declaredMethod : processServiceClazz.getMethods()) {
            System.out.println(declaredMethod.getDeclaringClass());
        }
        processService.processTaskDefinitions("");
        processService.send("", "");
    }
}
