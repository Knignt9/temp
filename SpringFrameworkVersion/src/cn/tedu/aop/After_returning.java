package cn.tedu.aop;

public class After_returning {
	private String pointcut_ref;
	private String method;
	private String returning;
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
	public String getReturning() {
		return returning;
	}
	public void setReturning(String returning) {
		this.returning = returning;
	}
}
