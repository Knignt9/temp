package cn.tedu.aop;

public class Before {
	private String pointcut_ref;
	private String method;
	public String getPointcut_ref() {
		return pointcut_ref;
	}
	public void setPointcut_ref(String pointcut_ref) {
		this.pointcut_ref = pointcut_ref;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
}
