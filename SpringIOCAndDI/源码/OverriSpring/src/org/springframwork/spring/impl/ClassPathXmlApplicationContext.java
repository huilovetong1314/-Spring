package org.springframwork.spring.impl;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframwork.annotion.Resource;
import org.springframwork.spring.ApplicationContext;

public class ClassPathXmlApplicationContext implements ApplicationContext {

	public ClassPathXmlApplicationContext() {

	}

	public ClassPathXmlApplicationContext(String xml) {
		init(xml);
	}

	@Override
	public Object getBean(String str) {

		for (String key : map.keySet()) {

			if (str.equals(key)) {
				setProperty();
				setPropertyRef();
				return map.get(key);
			}
		}
		return null;
	}

	@Override
	public <T> T getBean(Class<T> cls) {
		for (Entry<String, Object> obj : map.entrySet()) {
			Class cla = obj.getValue().getClass();
			if (cls == cla) {
				return (T) obj.getValue();
			}
		}

		return null;
	}

	public Document docun = null;
	public List<Element> list = new ArrayList<>();

	public void init(String str) {
		SAXReader saxread = new SAXReader();
		try {
			docun = saxread.read(str);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Element ele = docun.getRootElement();
		List<Element> elements = ele.elements();
		for (Element el : elements) {
			list.add(el);
		}

		shuxing();
		annotion();
	}

	// 用于xml的ID和对象的存储
	Map<String, Object> map = new HashMap<String, Object>();

	private void shuxing() {
		// TODO Auto-generated method stub
		Object obj = null;
		for (Element e : list) {
			String clazz = e.attributeValue("class");
			String id = e.attributeValue("id");
			if (clazz != null && id != null) {
				obj = newInstans(clazz);
				map.put(id, obj);
			}
		}

	}

	private Object newInstans(String clazz) {
		// TODO Auto-generated method stub

		try {
			if (clazz != null) {
				return Class.forName(clazz).newInstance();
			}
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			System.err.println(e.getMessage() + "未找到该类");
		}

		return null;
	}

	private void setProperty() {
		Class cla = null;
		Object ob = null;
		for (Entry<String, Object> obj : map.entrySet()) {

			cla = obj.getValue().getClass();

			for (Element e : list) {
				String clazz = e.attributeValue("class");

				List<Element> el = e.elements();
				for (Element ele : el) {
					String setter = ele.attributeValue("name");
					String value = ele.attributeValue("value");
					if (value != null) {
						if (clazz.equals(cla.getName())) {
							ob = obj.getValue();
							// System.out.println("ob=" + ob);
							try {
								ob.getClass().getMethod("set" + setter.substring(0, 1).toUpperCase()
										+ setter.substring(1, setter.length()), String.class).invoke(ob, value);
							} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
									| NoSuchMethodException | SecurityException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
					}
				}
			}
		}
	}

	List<String> listId = new ArrayList<>();

	public void setPropertyRef() {
		Class cla = null;
		Object ob = null;
		for (Entry<String, Object> obj : map.entrySet()) {
			cla = obj.getValue().getClass();
			for (Element e : list) {
				String clazz = e.attributeValue("class");
				String ids = e.attributeValue("id");
				listId.add(ids);
				List<Element> el = e.elements();
				if (el.size() != 0) {
					for (Element ele : el) {
						String ref = ele.attributeValue("ref");
						if (ref != null) {
							for (String id : listId) {
								if (ref.equals(id)) {
									if (clazz.equals(cla.getName())) {
										ob = obj.getValue();
										String setter = ele.attributeValue("name");
										try {
											ob.getClass()
													.getMethod(
															"set" + setter.substring(0, 1).toUpperCase()
																	+ setter.substring(1, setter.length()),
															map.get(ref).getClass())
													.invoke(ob, map.get(ref));
										} catch (IllegalAccessException | IllegalArgumentException
												| InvocationTargetException | NoSuchMethodException
												| SecurityException e1) {
											// TODO Auto-generated catch block
											e1.printStackTrace();
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}

	public void annotion() {

		for (Element key : list) {
			String basepackage = key.attributeValue("base-package");
			if (basepackage != null) {
				toSearchPackage(basepackage);
			}
		}
	}

	StringBuffer strbuf = new StringBuffer();
	String strpath = null;

	private void toSearchPackage(String basepackage) {
		// TODO Auto-generated method stub
		// System.out.println(basepackage);
		String path = System.getProperty("user.dir");
		strpath = basepackage.replace(".", "\\");
		getFile(path);
		zhujiekaifa();
	}

	List<String> setclasspath = new ArrayList<>();

	private void getFile(String path) {
		// TODO Auto-generated method stub
		File fin = new File(path);
		File[] file = fin.listFiles();
		for (File fi : file) {
			if (fi.isDirectory()) {
				/*
				 * boolean flag=fi.getPath().contains(strpath); if(flag) { String
				 * s=fi.getPath().substring(fi.getPath().indexOf(strpath)); String
				 * newpage=s.replace("\\", "."); System.out.println(newpage); }
				 */
				getFile(fi.getAbsolutePath());
			} else {
				boolean flag = fi.getPath().contains("src\\" + strpath);
				if (flag) {
					String s = fi.getPath().substring(fi.getPath().indexOf(strpath));
					String newpage = s.replace("\\", ".").replace(".java", "");
					// System.out.println(newpage);
					setclasspath.add(newpage);
				}
			}
		}
		// System.out.println("我是递归方法");
		// zhujiekaifa();
	}

	List<Object> listobj = new ArrayList<>();

	public void zhujiekaifa() {
		for (String strClass : setclasspath) {
			try {
				Object object = Class.forName(strClass).newInstance();

				listobj.add(object);
			} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		jiexi();
	}

	String annoValue = null;

	private void jiexi() {
//		System.out.println(listobj.size());
		for (Object object : listobj) {
			// 获取注解。
			Annotation[] ano = object.getClass().getAnnotations();
			if (ano.length == 0)
				continue;
			// System.out.println(" g=====" + object.getClass().getSimpleName());
			String ClassName = null;
			for (Annotation anntation : ano) {
				if (anntation.toString().contains("Component")) {
					// System.out.println("obj==="+object.getClass().getName());
					ClassName = object.getClass().getSimpleName();
					annoValue = anntation.toString().substring(anntation.toString().indexOf("value") + 6,
							anntation.toString().length() - 1);
					if (annoValue == null || annoValue == "") {
						annoValue = ClassName.substring(0, 1).toLowerCase()
								+ ClassName.substring(1, ClassName.length());
					} else {
						annoValue = annoValue;
					}
					map.put(annoValue, object);
				}
			}
			Object annoObject = null;
			Field[] field = object.getClass().getDeclaredFields();
			for (Field fiel : field) {
				Resource resource = fiel.getAnnotation(Resource.class);
				if (resource != null) {
					String s = fiel.getGenericType().toString().replace("class ", "");
					// System.out.println(s);
					try {

						annoObject = Class.forName(s).newInstance();

					} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} finally {
						if (annoObject != null) {
							// System.out.println("annoObject==" + annoObject);
							for (Object objectResource : listobj) {
								// System.out.println("object==" + objectResource);
								if (objectResource.getClass() == annoObject.getClass()) {
									try {
										// System.out.println("object==="+object.getClass().getName());
										// System.out.println("objectResource=--"+objectResource.getClass().getName());
										object.getClass()
												.getMethod(
														"set" + fiel.getName().substring(0, 1).toUpperCase()
																+ fiel.getName().substring(1, fiel.getName().length()),
														objectResource.getClass())
												.invoke(object, objectResource);
									} catch (IllegalAccessException | IllegalArgumentException
											| InvocationTargetException | NoSuchMethodException | SecurityException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
							}
						}
					}
				}
			}
		}
	}

}
