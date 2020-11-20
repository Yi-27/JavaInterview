package com.interview.juc;

public class SingletonDemo {

    private static SingletonDemo instance = null;

    private SingletonDemo(){
        System.out.println(Thread.currentThread().getName() + "\t 这是单例的构造方法");
    }


    // DCL（Double Check Lock双端检锁机制）
    // 但是这个地方还是有极小的几率出错，是因为 指令重排 的存在
    public static SingletonDemo getInstanceDCL(){
        if(instance == null){
            // 加锁前后都检测
            synchronized (SingletonDemo.class){
                if(instance == null){
                    instance = new SingletonDemo();
                }
            }
        }
        return instance;
    }


    public static SingletonDemo getInstance(){
        // 获取单例 这是懒汉式 要用的时候才创建单例
        if(instance == null){
            instance = new SingletonDemo();
        }
        return instance;
    }

    public static void main(String[] args) {
//        System.out.println(SingletonDemo.getInstance() == SingletonDemo.getInstance());
//        System.out.println(SingletonDemo.getInstance() == SingletonDemo.getInstance());
//        System.out.println(SingletonDemo.getInstance() == SingletonDemo.getInstance());


        for (int i = 0; i < 10; i++) {

            // 这样懒汉式就不安全了
            // 可以给方法加synchronized，但是小题大作了
            // 也可以改写成 饿汉式，直接上来就创建好
            new Thread(() -> {
//                SingletonDemo.getInstance(); // 多线程来获取单例
                SingletonDemo.getInstanceDCL(); // 多线程来获取单例
            }, String.valueOf(i)).start();

        }



    }
}
