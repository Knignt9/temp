package cn.tedu.framework;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.XPath;
import org.dom4j.io.SAXReader;

import cn.tedu.aop.Around;
import cn.tedu.aop.Aspect;
import cn.tedu.aop.Config;
import cn.tedu.aop.Pointcut;
import cn.tedu.proxy.JDKDynamicProxy;
import sun.awt.image.ImageWatched.Link;

public class SpringModule {
	private static Map<String,Class> classes=new HashMap<String,Class>();
	private static Map<String,ContextDefinition> contextDefinition=new HashMap<String,ContextDefinition>(); 
	private static Map<String,BeanDefinition> beanDefinitions=new HashMap<String,BeanDefinition>();
	private static Map<String,Object> singleton=new HashMap<String,Object>();
	private static Config config=new Config();
	public SpringModule(){
		readXml("resources/framework.xml");
	}
	public SpringModule(String fileName){
		readXml(fileName);
	}
	private void instanceObject(){
		Object obj=null;
		for(BeanDefinition beanDefinition:beanDefinitions.values()){
			String className = beanDefinition.getClassName();
			try {
				Class clazz = Class.forName(className);
				obj=clazz.newInstance();
				singleton.put(beanDefinition.getId(), obj);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	private void instanceObject(String id,String className){
		Object obj=null;
		try {
			Class clazz = Class.forName(className);
			obj=clazz.newInstance();
			singleton.put(id, obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void pototypeInjectionObject(String id,String className){
		BeanDefinition bd = beanDefinitions.get(id);
		Injectionobject(bd);
	}
	private void singletonInjectionObject(){
		for(BeanDefinition bd:beanDefinitions.values()){
			Injectionobject(bd);
		}
	}
	private void Injectionobject(BeanDefinition bd){
		Object obj = singleton.get(bd.getId());
		if(bd.getConsts().values().size()>0){
			contructInjection(bd,obj);
		}else if(bd.getBeanMap().values().size()>0){
			setMethodInjection(bd,obj);
		}
	}
	private void contructInjection(BeanDefinition bd,Object obj){
		for(ConstructorArgDefinition cad:bd.getConsts().values()){
			int index = Integer.parseInt(cad.getIndex());
			String value = cad.getValue();
//			String ref = cad.getRef();
//			String name = cad.getName();
			Class clz=obj.getClass();
			Field[] fields = clz.getDeclaredFields();
			Type type=fields[index].getGenericType();
			if(type.toString().contains("int")){
				int value1=Integer.parseInt(value);
				try {
					fields[index].setAccessible(true);
					fields[index].set(obj,value1);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else if(type.toString().contains("String")){
				try {
					fields[index].setAccessible(true);
					fields[index].set(obj,value);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		
		}
	}
	private void setMethodInjection(BeanDefinition bd,Object obj){
		for(PropertyDefiniton prop:bd.getBeanMap().values()){
			String name = prop.getName();
			String ref= prop.getRef();
			String val=prop.getValue();
			Object value =null;
			if(val!=null){
				if(val.contains("$")){
					ContextDefinition context = contextDefinition.get("property");
					Property.initProp(context.getLocation());
					Properties property = Property.getProp();
					String param=val.split("\\{")[1].split("\\}")[0];
					String propValue = property.getProperty(param);
					setMethodInjection(obj,name,propValue);
				}
			}
			if(ref!=null){
				value = singleton.get(ref);
				setMethodInjection(obj,name,value);
			}
			
		}
	}
	private void setMethodInjection(Object obj,String name,Object...value){
		String setMethodName="set"+name.substring(0, 1).toUpperCase()+name.substring(1);
		Class clazz = obj.getClass();
		Method[] methods = clazz.getDeclaredMethods();
		for(Method method:methods){
			if(setMethodName.equals(method.getName())){
				try {
					method.invoke(obj, value);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

	}
	private void contextReadXml(List<Element> contextElements){
		for (Element element : contextElements) {
			String location = element.attributeValue("location");
			String classpath = location.split(":")[1];
			ContextDefinition context=new ContextDefinition();
			context.setLocation(classpath);
			contextDefinition.put("property", context);
		}
	}
	private void readBeanXml(Document document,Map<String,String> nsMap,List<Element> elements){
		for(Element element:elements){
			String scope = element.attributeValue("scope");
			String id = element.attributeValue("id");
			String className = element.attributeValue("class");
			
			BeanDefinition bean=new BeanDefinition();
			bean.setId(id);
			bean.setClassName(className);
		
			
			XPath subProperty = document.createXPath("ns:property");
			subProperty.setNamespaceURIs(nsMap);
			List<Element> properties = subProperty.selectNodes(element);
			Map<String,PropertyDefiniton> propMap=new HashMap<String,PropertyDefiniton>();
			for(Element property:properties){
				String name = property.attributeValue("name");
				String ref = property.attributeValue("ref");
				String value = property.attributeValue("value");
				if(value==null){
					value=property.getText();
				}
				PropertyDefiniton pd=new PropertyDefiniton();
				pd.setName(name);
				pd.setRef(ref);
				pd.setValue(value);
				propMap.put(name, pd);
				bean.setBeanMap(propMap);
			}
			
			
			XPath constructors = document.createXPath("ns:constructor-arg");
			constructors.setNamespaceURIs(nsMap);
			List<Element> cons = constructors.selectNodes(element);
			for(Element con:cons ){
				String con_name = con.attributeValue("name");
				String con_ref = con.attributeValue("ref");
				String value = con.attributeValue("value");
				String index = con.attributeValue("index");
				ConstructorArgDefinition cad=new ConstructorArgDefinition();
				cad.setName(con_name);
				cad.setRef(con_ref);
				cad.setValue(value);
				cad.setIndex(index);
				bean.getConsts().put(index,cad);
			}
			beanDefinitions.put(id, bean);
			if("prototype".equals(scope)){
				instanceObject(id,className);
				pototypeInjectionObject(id,className);
			}else{
				instanceObject();
				singletonInjectionObject();
			}
		}
	}
	
	private void readXml(String fileName){
		SAXReader xmlReader=new SAXReader();
		Document document=null;
		try {
			URL xmlPath = SpringModule.class.getClassLoader().getResource(fileName);
			document=xmlReader.read(xmlPath);
			Map<String,String> nsMap=new HashMap<String,String>();
			nsMap.put("ns", "http://www.springframework.org/schema/beans");
			
			//配置文件的读取
			XPath contextXPath = document.createXPath("//ns:beans/ns:context");
			contextXPath.setNamespaceURIs(nsMap);
			List<Element> contextElements = contextXPath.selectNodes(document);
			contextReadXml(contextElements);
			
			//生成对象节点
			XPath xPath = document.createXPath("//ns:beans/ns:bean");
			xPath.setNamespaceURIs(nsMap);
			List<Element> elements = xPath.selectNodes(document);
			readBeanXml(document,nsMap,elements);
			
			
			//处理代理节点
			XPath configXPath = document.createXPath("//ns:beans/ns:config");
			configXPath.setNamespaceURIs(nsMap);
			List<Element> configElements = configXPath.selectNodes(document);
			for (Element element : configElements) {
				
				String flag = element.attributeValue("proxy-target-class");
				config.setProxy_target_class(flag);
				
				XPath aspectXPath = document.createXPath("ns:aspect");
				aspectXPath.setNamespaceURIs(nsMap);
				List<Element> aspectElemnets = aspectXPath.selectNodes(element);
				for (Element aspectElement : aspectElemnets) {
					Aspect aspect=new Aspect();
					String id = aspectElement.attributeValue("id");
					String ref = aspectElement.attributeValue("ref");
					aspect.setId(id);
					aspect.setRef(ref);
					Object obj = singleton.get(ref);//切面对象
					
					XPath pointcutXPath = document.createXPath("ns:pointcut");
					pointcutXPath.setNamespaceURIs(nsMap);
					List<Element> pointcutElements = pointcutXPath.selectNodes(aspectElement);
					for (Element pointcutElement : pointcutElements) {
						Pointcut pointcut=new Pointcut();
						String pid = pointcutElement.attributeValue("id");
						String expression = pointcutElement.attributeValue("expression");
						pointcut.setId(pid);
						pointcut.setExpression(expression);
						aspect.getPonitCutMap().put(pid, pointcut);
					}
					
					XPath aroundXPath = document.createXPath("ns:around");
					aroundXPath.setNamespaceURIs(nsMap);
					List<Element> aroundElements = aroundXPath.selectNodes(aspectElement);
					for (Element aroundElement : aroundElements) {
						Around around =new Around();
						String pointcut_ref = aroundElement.attributeValue("pointcut-ref");
						String method = aroundElement.attributeValue("method");
						around.setPointcut_ref(pointcut_ref);
						around.setMethod(method);
						aspect.getAroundMap().put(pointcut_ref, around);
					}
					config.getAspectMap().put(id, aspect);
				}
				
				
				if(flag.contains("true")){
					
				}else if(flag.contains("false")||flag.contains("default")){
					
				}
			}
			
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void changeToProxyObject(Aspect aspect){
		String ref = aspect.getRef();
		Object aspectObject = singleton.get(ref);
		String id = aspect.getId();
		Class targetClass = classes.get(id);
		try {
			Object targetObj = targetClass.newInstance();
			JDKDynamicProxy proxy=new JDKDynamicProxy();
			Object proxyObject = proxy.getProxyObject(targetObj, aspectObject);
			for(Map.Entry<String,Object> entry:singleton.entrySet()){
				if(entry.getValue().getClass().equals(targetClass)){
					entry.setValue(proxyObject);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void getClasses(){
		for(Aspect aspect:config.getAspectMap().values()){
			
			String id = aspect.getId();
			for(Pointcut pointcut:aspect.getPonitCutMap().values()){
				String expression = pointcut.getExpression();
				String signation = expression.substring(8,expression.length()-1);
				String method = signation.split(" ")[0];
				String note = signation.split(" ")[1];
				String packageName = note.split("[.][.]")[0];
				String packagePath = packageName.replace(".", "/");
				try {
					Enumeration<URL> dirUrl = this.getClass().getClassLoader().getResources(packagePath);
					while(dirUrl.hasMoreElements()){
						URL url = dirUrl.nextElement();
						String protocol = url.getProtocol();
						if("file".equals(protocol)){
							packagePath=url.getFile();
							recursiveFile(packageName,packagePath,classes,id);
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
			changeToProxyObject(aspect);
		}
		
	}
	private void recursiveFile(String packageName,String packagePath,Map<String,Class> classes,String id){
		File dir=new File(packagePath); 
		if(!dir.exists()||!dir.isDirectory()){
			return ;
		}
		File[] files=dir.listFiles(new FileFilter() {
			
			@Override
			public boolean accept(File file) {
				return file.getName().endsWith(".class");
			}
		});
		for (File file : files) {
			if(file.isDirectory()){
				packageName=packageName+"."+file.getName();
				recursiveFile(packageName,file.getAbsolutePath(),classes,id);
			}else{
				String className = file.getName().substring(0, file.getName().length()-6);
				try {
					
					Class loadClass = this.getClass().getClassLoader().loadClass(packageName+"."+className);
					classes.put(id, loadClass);
//					System.err.println(loadClass.getName());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	public Object getBean(String key){
		getClasses();
		return singleton.get(key);
		
	}
	public <T>T getBean(String key,Class<T> clazz){
		getClasses();
		Object obj = singleton.get(key);
		return (T) obj;
	}
}
