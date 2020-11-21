package com.interview.juc;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicStampedReference;

// ABA 问题的解决
public class ABADemo {

    static AtomicReference<Integer> atomicReference = new AtomicReference<>(100);
    static AtomicStampedReference<Integer> atomicStampedReference = new AtomicStampedReference<>(100, 1);
    public static void main(String[] args) {


        // 演示ABA问题
        new Thread(() -> {
            // 先改成101，再改回100
            atomicReference.compareAndSet(100, 101);
            atomicReference.compareAndSet(101, 100);
        }, "t1").start();

        new Thread(() -> {
            // 暂停1秒t2线程，保证上面的t1线程完成一次ABA操作
            try { TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }

            System.out.println(atomicReference.compareAndSet(100, 2019) + "\t" + atomicReference.get());
        }, "t2").start();

        
        // 睡眠2秒
        try { TimeUnit.SECONDS.sleep(2); } catch (InterruptedException e) { e.printStackTrace(); }
        System.out.println("========下面开始解决ABA问题=======");

        // 解决ABA问题
        new Thread(() -> {
            // 上来先获取版本号
            int stamp = atomicStampedReference.getStamp();
            System.out.println(Thread.currentThread().getName() + "\t第一次版本号：" + stamp); // 1

            // 暂停1秒钟，让线程4也获得相同的初始版本号
            try { TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }

            // 开始修改值做一次ABA操作                期待的引用值          新的引用值          期待的版本号       新的版本号
            atomicStampedReference.compareAndSet(100, 101, stamp, stamp+1);
            System.out.println(Thread.currentThread().getName() + "\t第二次版本号：" + atomicStampedReference.getStamp()); // 2
            // 从B改为A
            atomicStampedReference.compareAndSet(101, 100, atomicStampedReference.getStamp(), atomicStampedReference.getStamp()+1);
            System.out.println(Thread.currentThread().getName() + "\t第三次版本号：" + atomicStampedReference.getStamp()); // 3
        }, "t3").start();

        new Thread(() -> {
            int stamp = atomicStampedReference.getStamp();
            System.out.println(Thread.currentThread().getName() + "\t第一次版本号：" + stamp); // 1

            // 暂停3秒钟，让线程3完成一次ABA操作
            try { TimeUnit.SECONDS.sleep(3); } catch (InterruptedException e) { e.printStackTrace(); }

            // 尝试去该值 很乐观，认为当前的版本还是刚开始的版本，但其实已经不是了
            boolean result = atomicStampedReference.compareAndSet(100, 2020, stamp, stamp + 1); // 返回是个布尔值，表示修改是否成功
            System.out.println(Thread.currentThread().getName() + "\t修改值是否成功：" + result); // false
            System.out.println("\t\t当前版本号：" + atomicStampedReference.getStamp() + "\t当前值：" + atomicStampedReference.getReference()); // 3 100
        }, "t4").start();

    }

}
