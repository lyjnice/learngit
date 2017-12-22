package com.model.springmsb;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

public class ClassPathXmlApplicationContext implements BeansFactory {
	private Map<String, Object> beans = new HashMap<>();

	public ClassPathXmlApplicationContext() throws Exception {
		SAXBuilder sb = new SAXBuilder();
		Document doc = sb.build(sb.getClass().getClassLoader().getResourceAsStream("bean.xml"));
		Element root = doc.getRootElement();
		List list = root.getChildren("bean");
		for (int i = 0; i < list.size(); i++) {
			Element element = (Element) list.get(i);
			String id = element.getAttributeValue("id");
			String clazz = element.getAttributeValue("class");
			Object o = Class.forName(clazz).newInstance();
			beans.put(id, o);

			List<Element> sonelement = element.getChildren("property");
			for (Element prop : sonelement) {
				String name = prop.getAttributeValue("name");
				String bean = prop.getAttributeValue("bean");
				Object beanObject = beans.get(bean); // get dao对象

				String methodName = "set" + name.substring(0, 1).toUpperCase() + name.substring(1);
				System.out.println("methodName:" + methodName);

				System.out.println("class:" + beanObject.getClass().getInterfaces()[0]);
				Method m = o.getClass().getMethod(methodName, beanObject.getClass().getInterfaces()[0]);
				m.invoke(o, beanObject);
			}
		}

	}

	@Override
	public Object getBean(String name) {
		return beans.get(name);
	}
}
