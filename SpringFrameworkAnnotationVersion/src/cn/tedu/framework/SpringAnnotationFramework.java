package cn.tedu.framework;

import java.io.File;
import java.io.FileFilter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.XPath;
import org.dom4j.io.SAXReader;

import cn.tedu.annotation.Compnent;
import cn.tedu.annotation.Prototype;
import cn.tedu.annotation.Resource;
import cn.tedu.annotation.Value;
import cn.tedu.aop.Around;
import cn.tedu.aop.Aspect;
import cn.tedu.aop.Config;
import cn.tedu.aop.Pointcut;

public class SpringAnnotationFramework {
	private static List<Packages> packages = new ArrayList<>();
	private static Map<String, PropertyDefinition> propertyDefinitions = new HashMap<String, PropertyDefinition>();
	private static List<Class> classes = new ArrayList<>();
	private static List<Config> configs = new ArrayList<>();
	private static Map<String, Object> singleton = new HashMap<>();

	public SpringAnnotationFramework(String fileName) {
		readXml(fileName);
		parsePackage();
	}

	/**
	 * 读取xml文件
	 * 
	 * @param fileName
	 *            xml文件的路劲
	 */
	private void readXml(String fileName) {
		SAXReader saxReader = new SAXReader();
		try {
			URL xmlUrl = this.getClass().getClassLoader().getResource(fileName);
			Document document = saxReader.read(xmlUrl);
			Map<String, String> xMap = new HashMap<>();

			xMap.put("ns", "http://www.springframework.org/schema/beans");
			XPath xPath = document.createXPath("//ns:beans/ns:context_component-scan");
			xPath.setNamespaceURIs(xMap);
			List<Element> elements = xPath.selectNodes(document);
			for (Element element : elements) {
				String packgeName = element.attributeValue("base-package");
				Packages pack = new Packages();
				pack.setPackageName(packgeName);
				packages.add(pack);
			}

			// 获取properties文件的xml
			XPath uXPath = document.createXPath("//ns:beans/ns:util_properties");
			uXPath.setNamespaceURIs(xMap);
			List<Element> propElements = uXPath.selectNodes(document);
			for (Element element : propElements) {
				String pid = element.attributeValue("id");
				String location = element.attributeValue("location");
				PropertyDefinition pd = new PropertyDefinition();
				String fpath = location.split(":")[1];
				pd.initProp(fpath);
				propertyDefinitions.put(pid, pd);
			}

			// 解析aop中的xml
			XPath configXPath = document.createXPath("//ns:beans/ns:config");
			configXPath.setNamespaceURIs(xMap);
			List<Element> configElements = configXPath.selectNodes(document);
			for (Element element : configElements) {
				Config config = new Config();
				String flag = element.attributeValue("proxy-target-class");
				config.setProxy_target_class(flag);

				XPath aspectXPath = document.createXPath("ns:aspect");
				aspectXPath.setNamespaceURIs(xMap);
				List<Element> aspectElemnets = aspectXPath.selectNodes(element);
				for (Element aspectElement : aspectElemnets) {
					Aspect aspect = new Aspect();
					String id = aspectElement.attributeValue("id");
					String ref = aspectElement.attributeValue("ref");
					aspect.setId(id);
					aspect.setRef(ref);
					Object obj = singleton.get(ref);// 切面对象

					XPath pointcutXPath = document.createXPath("ns:pointcut");
					pointcutXPath.setNamespaceURIs(xMap);
					List<Element> pointcutElements = pointcutXPath.selectNodes(aspectElement);
					for (Element pointcutElement : pointcutElements) {
						Pointcut pointcut = new Pointcut();
						String pid = pointcutElement.attributeValue("id");
						String expression = pointcutElement.attributeValue("expression");
						pointcut.setId(pid);
						pointcut.setExpression(expression);
						aspect.getPonitCutMap().put(pid, pointcut);
					}

					XPath aroundXPath = document.createXPath("ns:around");
					aroundXPath.setNamespaceURIs(xMap);
					List<Element> aroundElements = aroundXPath.selectNodes(aspectElement);
					for (Element aroundElement : aroundElements) {
						Around around = new Around();
						String pointcut_ref = aroundElement.attributeValue("pointcut-ref");
						String method = aroundElement.attributeValue("method");
						around.setPointcut_ref(pointcut_ref);
						around.setMethod(method);
						aspect.getAroundMap().put(pointcut_ref, around);
					}
					config.getAspectMap().put(id, aspect);
					configs.add(config);
				}

			}

		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 根据xml得到包路径解析所有得类
	 */
	private void parsePackage() {
		for (Packages p : packages) {
			String packageName = p.getPackageName();
			for (String name : packageName.split(",")) {
				String packagePath = name.replace(".", "/");
				packagePath = this.getClass().getClassLoader().getResource(packagePath).getFile();
				recurseFile(name, packagePath);
			}
		}
	}
	
	/**
	 * 根据传过来的类路径，解析得到所有类储存在classes中
	 * 
	 * @param fname
	 *            包名
	 * @param packagePath
	 *            包路径
	 */
	private void recurseFile(String fname, String packagePath) {
		File dir = new File(packagePath);
		if (!dir.exists() || !dir.isDirectory()) {
			return;
		}
		File[] files = dir.listFiles(new FileFilter() {

			@Override
			public boolean accept(File pathname) {
				return pathname.getName().endsWith(".class");
			}
		});
		for (File file : files) {
			if (file.isDirectory()) {
				recurseFile(fname + file.getName(), file.getAbsolutePath());
			}
			try {
				String filePath = file.getName().substring(0, file.getName().length() - 6);
				Class clazz = Class.forName(fname + "." + filePath);
				classes.add(clazz);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 根据注解初始化所有类
	 * 
	 * @param clz
	 */
	public Object InstanceObject(String key) {
		for (Class clz : classes) {
			Annotation[] annotations = clz.getAnnotations();
			boolean flag = clz.isAnnotationPresent(Compnent.class);
			if (flag) {
				Compnent annotation = (Compnent) clz.getAnnotation(Compnent.class);
				String id = annotation.value();
				Object obj;
				try {
					obj = clz.newInstance();
					singleton.put(id, obj);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		// 实例化对象后，给对象中注入内容
		InjectionObject();
		//查看aop中是否有该类的切面，若有这将次返回值改成代理对象
		Object obj=decorateObject1(singleton.get(key));
		// 返回对象
		return obj;//返回值是obj
	}
	/**
	 * 
	 */
	private Object decorateObject1(Object targetObject){
		String id = isAspectObject(targetObject);
		Object aspectObject = singleton.get(id);
		Object proxyObject=null;
		if(id!=null){
			JDKDynamicProxy proxy=new JDKDynamicProxy();
			proxyObject = proxy.getProxyObject(targetObject, aspectObject);
		}else{
			proxyObject=targetObject;
		}
		return proxyObject;
	}
	/**
	 * 
	 * @param obj 判断该对象是否是要切入的目标对象
	 * @return 
	 */
	private String isAspectObject(Object obj){
		String id=null;
		for(Config config:configs){
			for(Aspect aspect:config.getAspectMap().values()){
				for(Pointcut pointcut:aspect.getPonitCutMap().values()){
					
					String expression = pointcut.getExpression();
					String method=expression.split(" ")[0];
					String packageName=expression.split(" ")[1].split("[.][.]")[0];
					String name = obj.getClass().getPackage().getName();
					if(packageName.equals(name)){
						id = pointcut.getId();
					}
				}
			}
		}
		return id;
		
	}

	/**
	 * 获取多利对象
	 */
	private Object PrototypeInstanceObject(String key, Class clz) {
		Object obj = null;
		Annotation[] annotations = clz.getAnnotations();
		boolean flag = clz.isAnnotationPresent(Compnent.class);
		if (flag) {
			Compnent annotation = (Compnent) clz.getAnnotation(Compnent.class);
			String id = annotation.value();
			try {
				obj = clz.newInstance();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return obj;
	}

	/**
	 * 初始化所有所有类后，将属性注入类中
	 * 
	 * @param clz
	 *            所有实例化得类
	 */
	private void InjectionObject() {
		for (Object obj : singleton.values()) {
			Class clz = obj.getClass();
			Field[] fields = clz.getDeclaredFields();
			for (Field field : fields) {
				boolean flag = field.isAnnotationPresent(Resource.class);
				boolean valueFlag = field.isAnnotationPresent(Value.class);
				if (flag) {
					Resource annotation = field.getAnnotation(Resource.class);
					String name = annotation.name();
					Object value = singleton.get(name);
					String setMethodName = "set" + name.substring(0, 1).toUpperCase() + name.substring(1);
					try {
						Method method = clz.getMethod(setMethodName, field.getType());
						method.invoke(obj, value);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				if (valueFlag) {
					Value annotation = field.getAnnotation(Value.class);
					String value = annotation.value();
					if (value.contains("#")) {
						String result = value.split("[{]")[1].split("[}]")[0];
						String id = result.split("[.]")[0];
						String key = result.split("[.]")[1];
						PropertyDefinition propertyDefinition = propertyDefinitions.get(id);
						String propValue = propertyDefinition.getProp(key);
						try {
							field.setAccessible(true);
							field.set(obj, propValue);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}

	public Object doSingleton(String id) {
		return InstanceObject(id);
	}

	public Object doPrototype(String id, Class clz) {
		return PrototypeInstanceObject(id, clz);
	}

	public boolean isPrototype(Class clazz) {
		boolean flag = clazz.isAnnotationPresent(Prototype.class);
		return flag;
	}

	public Object getBean(String id) {
		return singleton.get(id);
	}

	public <T> T getBean(String id, Class<T> clazz) {
		Object obj;
		if (!isPrototype(clazz)) {
			obj = doSingleton(id);
		} else {
			obj = doPrototype(id, clazz);
		}
		return (T) obj;
	}
}
