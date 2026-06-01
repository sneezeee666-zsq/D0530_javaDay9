/*
package com.nuist.test5;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.nuist.test4.Student;

import java.util.List;
import java.util.Map;

public class TestJSON {
    public static void main(String[] args) {
        Student student = new Student();
        student.setName("dyx");
        student.setAge(18);
        String jsonString = JSONUtil.toJsonStr(student);
        System.out.println(jsonString);
        Student student1 = JSONUtil.toBean(jsonString, Student.class);
        System.out.println(student1);
        List<String> l = List.of("1","2","3");
        //将集合转换为 JSON格式
        String jsonArray = JSONUtil.toJsonStr(l);
        System.out.println(jsonArray);
        Map<String,Object> m = Map.of("name","dyx","age",18);
        String jsonObject = JSONUtil.toJsonStr(m);
        //用jjwt转换

    }
}
*/
