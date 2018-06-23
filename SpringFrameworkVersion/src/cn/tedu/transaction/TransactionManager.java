package cn.tedu.transaction;

import cn.tedu.framework.ProceedingJoinPoint;

public class TransactionManager {
	public Object around(ProceedingJoinPoint pjp){
		System.out.println("事务开始");
		System.out.println(pjp.getTargetObject().getClass().getName());
		Object targetObject = pjp.getTargetObject();
		Object obj = pjp.proceed();
		System.out.println("事务结束");
		return targetObject;
		
	}
}
