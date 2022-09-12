package com.idb.hmis.test;

import java.util.UUID;

/**
 *
 * @author Yasir Araphat
 */
public class Test {
    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            System.out.println(UUID.randomUUID().toString());
        }
        
    }
    private static void fun(Object o){
        System.out.println("Object");
    }
    private static void fun(String s){
        System.out.println("String");
    }
    private static void fun(int i){
        System.out.println("Long");
    }
}
