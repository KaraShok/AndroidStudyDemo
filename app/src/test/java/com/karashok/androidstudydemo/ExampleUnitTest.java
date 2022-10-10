package com.karashok.androidstudydemo;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * @author zhangyaozhong @ Zhihu Inc.
 * @since 09-15-2022
 */
public class ExampleUnitTest {

    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
        //加载动态库
        System.load("/Users/karashokz/Library/Developer/Xcode/DerivedData/DemoCppLibrary-bprjibgwfsnwepebilykxkwsxpkt/Build/Products/Debug/libDemoCppLibrary.dylib");
        test(1, "java");
    }

    native void test(int i, String j);
}
