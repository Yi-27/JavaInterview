package com.interview.juc;

public class ReSortSeqDemo {

    int a = 0;
    boolean flag = false;

    public void method01(){
        a = 1; // 语句1
        flag = true; // 语句2
    }

    // 多线程情况下的编译器优化重排
    // 由于 语句1，2并没有数据依赖
    // 所以有可能语句2在语句1前面执行
    // 当线程1执行完语句2后 线程2执行进去后直接执行语句3
    // 接着有可能执行语句1，也有可能执行打印语句
    // 所以结果不一定
    // 所以要在变量前加volatile来修饰，来禁用指令重排
    public void method02(){
        if(flag){
            a = a + 5; // 语句3
            System.out.println("value: " + a);
        }
    }


}
