package com.xueqiang.footmark.utils;

import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

/**
 * SpEL 表达式
 */
public class ExpressionUtils {

    public static void main(String[] args) {
        ExpressionUtils utils = new ExpressionUtils();
        utils.method1();
    }

    public void method1() {
        // 接口ExpressionParser负责解析表达式字符串
        ExpressionParser parser = new SpelExpressionParser();
        Expression exp = parser.parseExpression("'Hello world'");
        String message = (String) exp.getValue();
        System.out.println(message);

        //SpEL支持很多功能特性，如调用方法，访问属性，调用构造函数。
        exp = parser.parseExpression("'Hello World'.concat('!')");//调用字符串的 concat 方法
        message = (String) exp.getValue();
        System.out.println(message);

        // invokes getBytes()
        exp = parser.parseExpression("'Hello World'.bytes"); //调用对象属性
        byte[] bytes = (byte[]) exp.getValue();

        // SpEL还支持使用标准的“.”符号，即嵌套属性prop1.prop2.prop3和属性值的设置
        // invokes getBytes().length
        exp = parser.parseExpression("'Hello World'.bytes.length");
        int length = (Integer) exp.getValue();
        System.out.println(length);

        exp = parser.parseExpression("new String('hello world').toUpperCase()");
        //public <T> T getValue(@Nullable Class<T> expectedResultType)
        message = exp.getValue(String.class);
        System.out.println(message);
    }
}
