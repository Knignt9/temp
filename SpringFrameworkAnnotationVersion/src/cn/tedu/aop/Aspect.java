package cn.tedu.aop;

import java.util.HashMap;
import java.util.Map;

public class Aspect {
	private String id;
	private String ref;
	private Map<String,Pointcut> ponitCutMap=new HashMap<String,Pointcut>();
	private Map<String,Before> BeforeMap=new HashMap<String,Before>();
	private Map<String,After_returning> After_returningMap=new HashMap<String,After_returning>();
	private Map<String,After_Throwing> After_ThrowingMap=new HashMap<String,After_Throwing>();
	private Map<String,After> AfterMap=new HashMap<String,After>();
	private Map<String,Around> AroundMap=new HashMap<String,Around>();
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getRef() {
		return ref;
	}
	public void setRef(String ref) {
		this.ref = ref;
	}
	public Map<String, Pointcut> getPonitCutMap() {
		return ponitCutMap;
	}
	public void setPonitCutMap(Map<String, Pointcut> ponitCutMap) {
		this.ponitCutMap = ponitCutMap;
	}
	public Map<String, Before> getBeforeMap() {
		return BeforeMap;
	}
	public void setBeforeMap(Map<String, Before> beforeMap) {
		BeforeMap = beforeMap;
	}
	public Map<String, After_returning> getAfter_returningMap() {
		return After_returningMap;
	}
	public void setAfter_returningMap(Map<String, After_returning> after_returningMap) {
		After_returningMap = after_returningMap;
	}
	public Map<String, After_Throwing> getAfter_ThrowingMap() {
		return After_ThrowingMap;
	}
	public void setAfter_ThrowingMap(Map<String, After_Throwing> after_ThrowingMap) {
		After_ThrowingMap = after_ThrowingMap;
	}
	public Map<String, After> getAfterMap() {
		return AfterMap;
	}
	public void setAfterMap(Map<String, After> afterMap) {
		AfterMap = afterMap;
	}
	public Map<String, Around> getAroundMap() {
		return AroundMap;
	}
	public void setAroundMap(Map<String, Around> aroundMap) {
		AroundMap = aroundMap;
	}
	
}
