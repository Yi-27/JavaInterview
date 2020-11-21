package com.interview.juc;

import java.util.concurrent.atomic.AtomicReference;

public class AtomicReferenceDemo {

    public static void main(String[] args) {
        // 原子引用的包装类
        User z3 = new User("z3", 22);
        User li4 = new User("li4", 25);
        AtomicReference<User> atomicReference = new AtomicReference<>();
        atomicReference.set(z3); // 原子引用指向 z3

        // 如果是内存中的对象是z3就替换成li4
        System.out.println(atomicReference.compareAndSet(z3, li4) + "\t" + atomicReference.get().toString());
        // 想再替换就不行了
        System.out.println(atomicReference.compareAndSet(z3, li4) + "\t" + atomicReference.get().toString());


    }


}

class User{
    String username;
    int age;

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", age=" + age +
                '}';
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public User() {
    }

    public User(String username, int age) {
        this.username = username;
        this.age = age;
    }
}