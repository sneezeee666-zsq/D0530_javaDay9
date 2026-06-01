package com.nuist.test4;


import lombok.Data;

import java.io.Serializable;


//java中一个标准的序列化类需要实现Serializable接口 并且定义静态常量标记序列的版本号
@Data
public class Student implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private int age;
}
