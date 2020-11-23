package com.interview.collection;


import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * 集合类不安全的问题
 *  ArrayList
 */
public class ContainerNotSafeDemo {

    public static void main(String[] args) {


//        Map<String, String> map = new HashMap<>();
        Map<String, String> map = new ConcurrentHashMap<>();
        for (int i = 0; i < 30; i++) {
            new Thread(() -> {
                map.put(Thread.currentThread().getName(), UUID.randomUUID().toString().substring(0, 8));
                System.out.println(map);
            }, String.valueOf(i)).start();
        }
    }

    private static void SetNotSafe() {
        //        Set<String> set = new HashSet<>();
//        Set<String> set = Collections.synchronizedSet(new HashSet<>());
        Set<String> set = new CopyOnWriteArraySet<>(); // 底层是CopyOnWriteArrayList
        for (int i = 0; i < 30; i++) {
            new Thread(() -> {
                set.add(UUID.randomUUID().toString().substring(0, 8));
                System.out.println(set);
            }, String.valueOf(i)).start();
        }
    }

    private static void ListNotSafe() {
        // new了一个空的列表（底层是数组），数组的大小默认值是10
        List<String> list = new ArrayList<>();
        // 返回安全的列表
        List<String> strings = Collections.synchronizedList(list);
        // juc包中的list
        List<String> list1 = new CopyOnWriteArrayList<>();

        for (int i = 0; i < 30; i++) {
            new Thread(() -> {
                list1.add(UUID.randomUUID().toString().substring(0, 8));
                System.out.println(list1);
            }, String.valueOf(i)).start();
        }

        //并发修改异常 java.util.ConcurrentModificationException
        /**
         * 1. 故障现象
         *      java.util.ConcurrentModificationException
         * 2. 导致原因
         *      并发争抢修改导致。
         *      一个线程正在写入，另外一个线程过来争抢，导致数据不一致异常，即并发修改异常
         * 3. 解决方案
         *   3.1 new Vector<>()
         *   3.2 Collections.synchronizedList(new ArrayList<>());
         *      Collections 是集合工具类
         *   3.3 new CopyOnWriteArrayList();
         *
         * 4. 优化建议（同样的错误不犯第二次）
         */}
}
