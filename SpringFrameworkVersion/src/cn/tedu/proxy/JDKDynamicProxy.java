package cn.tedu.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import cn.tedu.framework.ProceedingJoinPoint;

public class JDKDynamicProxy {
	
	public Object getProxyObject(final Object targetObject,final Object aspectObject){
		Object proxyObj=Proxy.newProxyInstance(targetObject.getClass().getClassLoader(), 
				targetObject.getClass().getInterfaces(),
				new InvocationHandler() {
					@Override
					public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
						ProceedingJoinPoint pjp=new ProceedingJoinPoint();
						pjp.setTargetObject(targetObject);
						pjp.setSignation(method);
						pjp.setArgs(args);
						Method[] methods = aspectObject.getClass().getDeclaredMethods();
						Object result=null;
						for (Method aspectMethod : methods) {
							if("around".equals(aspectMethod.getName())){
								
								result = aspectMethod.invoke(aspectObject, pjp);
							}
						}
						
						return result;
					}
				});
		return proxyObj;
	}
	
}
