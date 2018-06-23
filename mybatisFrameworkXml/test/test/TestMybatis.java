package test;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.tarena.vo.Page;

import cn.tedu.entity.User;
import cn.tedu.mybatis.Resource;
import cn.tedu.mybatis.SqlSession;
import cn.tedu.mybatis.SqlSessionFactory;
import cn.tedu.mybatis.SqlSessionFactoryBuilder;

public class TestMybatis {
	SqlSessionFactory factory =null;
	@Before
	public void before() throws Exception{
		String resource="resources/framework.xml";
		InputStream inputStream = Resource.getResourceAsStream(resource);
		factory = new SqlSessionFactoryBuilder().built(inputStream);
	}
	@Test
	public void test(){
		SqlSession session = factory.openSession();
		try{
			System.out.println("aaa-->"+session.getConnection());
			List<User> users=session.selectList("findAllUsers");
			for(User user : users){
				System.out.println(user);
			}
//			session.commit();
		}catch(Exception e){
			e.printStackTrace();
			session.rollback();
			
		}finally{
			session.close();
		}
	}
	@Test
	public void testFindUserById(){
		
		SqlSession session=null;
		try{
			session=factory.openSession();
			User user=session.selectOne("findUserById",2);
			System.out.println(user);
//			session.commit();
		}catch(Exception e){
			e.printStackTrace();
			session.rollback();
			
		}finally{
			session.close();
		}
	}
	@Test
	public void testFindUserByPage1(){
		
		SqlSession session=null;
		try{
			session=factory.openSession();
			Page page=new Page();
			page.setCurrentPage(0);
			page.setPageSize(3);
			page.setKeyword("%a%");
			List<User> users=session.selectList("findUserByPage1",page);
			
			for(User user : users){
				System.out.println(user);
			}
//			session.commit();
		}catch(Exception e){
			e.printStackTrace();
			session.rollback();
			
		}finally{
			session.close();
		}
	}
	@Test
	public void testFindUserByPage2_1(){
		
		SqlSession session=null;
		try{
			session=factory.openSession();
			Page page=new Page();
			page.setCurrentPage(0);
			page.setPageSize(3);
			page.setKeyword("%1%");
			List<Map<String,Object>> users=session.selectList("findUserByPage2",page);
			for(Map<String,Object> user : users){
				System.out.print("id="+user.get("id"));
				System.out.print("   username="+user.get("uname"));
				System.out.println("   userpassword="+user.get("userpassword"));
			}
			session.commit();
		}catch(Exception e){
			e.printStackTrace();
			session.rollback();
			
		}finally{
			session.close();
		}
	}
	@Test
	public void testDeleteUser(){
		
		SqlSession session=null;
		try{
			session=factory.openSession();
			
			
			int rowAffects=session.delete("deleteUser", 1);
			System.out.println(rowAffects);
			
			session.commit();
		}catch(Exception e){
			e.printStackTrace();
			session.rollback();
			
		}finally{
			session.close();
		}
	}
	@Test
	public void testAddUser(){
		
		SqlSession session=null;
		try{
			session=factory.openSession();
			User user=new User();
			user.setUsername("tom");
			user.setUserpassword("dbj1");
			
			int rowAffects=session.insert("addUser", user);
			System.out.println(rowAffects+"  "+user.getId());
			
			session.commit();
		}catch(Exception e){
			e.printStackTrace();
			session.rollback();
			
		}finally{
			session.close();
		}
	}
	@Test
	public void testUpdateUser(){
		
		SqlSession session=null;
		try{
			session=factory.openSession();
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("id", 10);
			map.put("uname", "no");
			map.put("upwd", "hehe");
			
			int rowAffects=session.update("updateUser", map);
			System.out.println(rowAffects);
			
			session.commit();
		}catch(Exception e){
			e.printStackTrace();
			session.rollback();
			
		}finally{
			session.close();
		}
	}
}
