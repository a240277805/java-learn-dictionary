package com.zmk.github.test;

import org.junit.Test;
import org.openjdk.jol.info.ClassLayout;

public class test {
    @Test
    public void test(){
        System.out.println(ClassLayout.parseInstance(new Object()).toPrintable());
    }
}
