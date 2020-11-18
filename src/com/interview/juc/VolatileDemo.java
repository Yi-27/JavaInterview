package com.interview.juc;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class VolatileDemo {

    /**
     * 1. 验证volatile的可见性
     *  1.1 假如int number = 0; number变量之前根本没有添加volatile关键字修饰 没有可见性
     *  1.2 添加了volatile，可以解决可见性问题
     * 2. 验证volatile不保证原子性
     *  2.1 原子性指的是什么意思？
     *       不可分割，完整性，也即某个线程正在做某个具体业务时，中间不可以被加塞或者被分割。需要整体完整
     *       要么同时成功，要么同时失败
     *  2.2 volatile是否可以保证原子性？不可以
     *  2.3 为什么不能保证？
     *       速度太快了，有线程丢失写的值了
     *       其他线程还没得到已经修改主内存的消息就导致这种情况，写丢失写覆盖
     *  2.4 怎么解决？
     *       在方法前加synchronized肯定可以保证
     *       使用juc中的AtomicInteger和其getAndIncrement()
     * @param args
     */
    public static void main(String[] args) {
//        seeOKByVolatile();
        MyData myData = new MyData();

        // 开20个线程
        for (int i = 0; i < 20; i++) {

            new Thread(() -> {
                for (int j = 0; j <= 1000; j++) {
                    myData.addPlusPlus();
                    myData.addAtomic();
                }
            }, String.valueOf(i)).start();

        }

        // 等待上面20个线程都全部计算完成后，再用main线程取得最终结果值
        while(Thread.activeCount() > 2){ // 后台默认有两个线程 main和gc
            Thread.yield(); // yield表示退出去让出资源
        }

        // 最终的结果不一定是20000，但有可能正好是，这就是不保证原子性
        // 有线程丢失写的值了，写丢失写覆盖，速度太快了，
        // 其他线程还没得到已经修改主内存的消息就导致这种情况
        System.out.println(Thread.currentThread().getName() + "\t finally number value: " + myData.number);

        // 为啥是20020啊
        System.out.println(Thread.currentThread().getName() + "\t finally AtomicInteger value: " + myData.atomicInteger);
    }

    // volatile可以保证可见性，及时通知其他线程，著物理内存的值已经被修改
    private static void seeOKByVolatile() {
        MyData myData = new MyData(); // 资源类

        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "\t come in");
            // 休眠一会
            try {
                TimeUnit.SECONDS.sleep(3); // 休眠一下让其他线程读取到number的值
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            myData.addT060();
            System.out.println(Thread.currentThread().getName() + "\t update number value: " + myData.number);
        }, "AA").start();

        // 线程2就是main线程
        while(myData.number == 0){
            // main线程一直循环，知道number不等于0
        }
        // 执行下面这行说明 可见性 验证成功
        System.out.println(Thread.currentThread().getName() + "\t mission is over number value: " + myData.number);
    }

}
class MyData{
    volatile int number = 0;
    public void addT060(){
        this.number = 60;
    }

    // 这时是加了volatile关键字的
    public void addPlusPlus(){
        number++;
    }

    // 线程同步的，但是这对于++这样的代码有点杀鸡用牛刀了
    public synchronized void addPlusPlusSync(){
        number++;
    }

    // 可以用原子类
    AtomicInteger atomicInteger = new AtomicInteger(); // 默认为0

    public void addAtomic(){
        atomicInteger.getAndIncrement(); // 一次+1，类似于++
    }
}