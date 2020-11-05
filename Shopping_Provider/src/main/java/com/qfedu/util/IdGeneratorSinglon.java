package com.qfedu.util;

/**
 * projectName: Shopping
 * author: 崔
 * time: 2020/11/05  21:27
 * description:单例
 */

public class IdGeneratorSinglon {
    private IdGeneratorSinglon(){
        generator=new IdGenerator();
    }
    private static IdGeneratorSinglon singlon=new IdGeneratorSinglon();
    public IdGenerator generator;
    public static IdGeneratorSinglon getInstance(){
        return singlon;
    }
}
