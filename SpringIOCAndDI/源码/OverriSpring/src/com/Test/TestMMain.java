package com.Test;

import org.springframwork.spring.ApplicationContext;
import org.springframwork.spring.impl.ClassPathXmlApplicationContext;

public class TestMMain {
	public static void main(String[] args) {
		ApplicationContext app=new ClassPathXmlApplicationContext("application.xml");
		TestJava te=(TestJava) app.getBean("kkk");
		System.out.println(te.Test());
		te.getTest().A();
		te.getTest1().B();
		te.getTest1();
		System.out.println(te.getTest3().Test());
	}
}
