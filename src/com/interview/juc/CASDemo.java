package com.interview.juc;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 1. CAS就是 比较并交换
 */
public class CASDemo {

    public static void main(String[] args) {
        AtomicInteger atomicInteger = new AtomicInteger(5);

        // 主线程读其他事

        // 参数expect是指主物理内存上的值  update的值是打算写进去的值
        System.out.println(atomicInteger.compareAndSet(5, 2019) + "\t current data: " + atomicInteger.get());
        // 比较发现不是5了，就不写入2014，返所以回false
        System.out.println(atomicInteger.compareAndSet(5, 2014) + "\t current data: " + atomicInteger.get());

        atomicInteger.getAndIncrement(); // 不加sync和volatile即可解决多线程下++的安全问题

    }
}
