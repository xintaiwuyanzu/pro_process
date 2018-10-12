package com.dr;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.lang.reflect.Method;
import java.lang.reflect.TypeVariable;

public class ReflectTest {
    @org.springframework.transaction.annotation.Transactional("")
    public <T> void aa(T qrg) {
    }

    public static void main(String[] args) {
        Class c = ReflectTest.class;
        for (Method method : c.getMethods()) {
            if (method.getName().equalsIgnoreCase("aa")) {
                System.out.println(ToStringBuilder.reflectionToString(method));
                for (TypeVariable type : method.getTypeParameters()) {
                    TypeVariable[] typeVariables = type.getGenericDeclaration().getTypeParameters();
                    System.out.println(type.getGenericDeclaration());
                    for (TypeVariable typeVariable : typeVariables) {
                        System.out.println(typeVariable);
                    }
                }

                System.out.println(method.getGenericReturnType());
            }
        }
    }
}
