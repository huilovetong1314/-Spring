package com.Test.copy;

import org.springframwork.annotion.Component;

@Component("jj")
public class TestJava3 {
	private String name;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public TestJava3(){
		
	}
	public String Test() {
		return "haha";
	}
}
