package test;

import java.io.InputStream;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.tarena.vo.UserMapper;

import cn.tedu.entity.User;
import cn.tedu.mybatis.Resource;
import cn.tedu.mybatis.SqlSession;
import cn.tedu.mybatis.SqlSessionFactory;
import cn.tedu.mybatis.SqlSessionFactoryBuilder;

public class TestMabatisMapper {
	SqlSessionFactory factory =null;
	@Before
	public void before() throws Exception{
		String resource="resources/framework.xml";
		InputStream inputStream = Resource.getResourceAsStream(resource);
		factory = new SqlSessionFactoryBuilder().built(inputStream);
	}
//	@Test
//	public void testFindUser_ForOneGroup(){
//		
//		SqlSession session=null;
//		try{
//			session=factory.openSession();
//			
//			UserMapper umapper=session.getMapper(UserMapper.class);
//			
//
//			List<User> users=umapper.findUser_ForOneGroup();
//			for(User user : users){
//				System.out.println(user);
//			}
//			
//			session.commit();
//		}catch(Exception e){
//			e.printStackTrace();
//			session.rollback();
//			
//		}finally{
//			session.close();
//		}
//	}
}
