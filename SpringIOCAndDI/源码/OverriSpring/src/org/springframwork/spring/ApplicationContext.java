package org.springframwork.spring;

public interface ApplicationContext{
	Object getBean(String str);
	<T> T getBean(Class<T> cls);
}
