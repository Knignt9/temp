package constructor;

public class User {
	private String username;
	private int age;
	private String password;
	public User(){
		System.out.println("user初始化完成");
		
	}
	
	public int getAge() {
		return age;
	}

	

	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}
	
	
}
