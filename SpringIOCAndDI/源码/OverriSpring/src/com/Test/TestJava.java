package com.Test;

import org.springframwork.annotion.Component;
import org.springframwork.annotion.Resource;

import com.Test.copy.TestJava3;

@Component("kkk")
public class TestJava {
	private String name;
	
	@Resource()
	private TestJava1 test;
	@Resource()
	private TestJava2 test1;
	@Resource()
	private TestJava3 test3;
	public TestJava3 getTest3() {
		return test3;
	}
	public void setTest3(TestJava3 test3) {
		this.test3 = test3;
	}
	public TestJava2 getTest1() {
		return test1;
	}
	public void setTest1(TestJava2 test1) {
		this.test1 = test1;
	}
	public TestJava1 getTest() {
		return test;
	}
	public void setTest(TestJava1 test) {
		this.test = test;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public TestJava(){
		
	}
	public String Test() {
		return "haha";
	}
}
