package cn.tedu.domain;

public class Environment {
	private String id;
	private TransactionManager trans;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public TransactionManager getTrans() {
		return trans;
	}
	public void setTrans(TransactionManager trans) {
		this.trans = trans;
	}
	
	
}
