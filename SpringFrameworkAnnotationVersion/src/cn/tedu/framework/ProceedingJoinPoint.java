package cn.tedu.framework;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ProceedingJoinPoint {
	private Method signation;
	private Object targetObject;
	private Object[] args;
	
	public Object[] getArgs() {
		return args;
	}
	public void setArgs(Object[] args) {
		this.args = args;
	}
	public ProceedingJoinPoint() {
		super();
	}
	public ProceedingJoinPoint(Object targetObject) {
		this.targetObject = targetObject;
	}
	public Method getSignation() {
		return signation;
	}
	public void setSignation(Method signation) {
		this.signation = signation;
	}
	public Object getTargetObject() {
		return targetObject;
	}
	public void setTargetObject(Object targetObject) {
		this.targetObject = targetObject;
	}
	
	public Object proceed(){
		try {
			Object returnValue = signation.invoke(targetObject, args);
			return returnValue;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
}
