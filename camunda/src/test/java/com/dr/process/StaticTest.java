package com.dr.process;

import java.util.HashMap;
import java.util.Map;

public class StaticTest {
    static class A {
        static Map<String, String> map;

        static {
            map = new HashMap<>();
            map.put("aaa", "bbb");
        }
    }

    static class B extends A {
        static {
            System.out.println("aaa");
            map.put("aaa", "ccc");
        }
    }

    public static void main(String[] args) {
        new B();
        System.out.println(
                B.map.get("aaa")
        );
    }
}
