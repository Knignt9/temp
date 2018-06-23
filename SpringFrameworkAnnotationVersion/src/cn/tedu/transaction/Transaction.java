package cn.tedu.transaction;

import cn.tedu.annotation.Compnent;
import cn.tedu.framework.ProceedingJoinPoint;

@Compnent(value="trans")
public class Transaction {
	public Object around(ProceedingJoinPoint pjp){
		System.out.println("开启事务");
		pjp.proceed();
		System.out.println("关闭shiwu");
		return pjp.getTargetObject();
	}
}
