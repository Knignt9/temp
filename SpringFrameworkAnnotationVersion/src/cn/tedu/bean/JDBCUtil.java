package cn.tedu.bean;

import cn.tedu.annotation.Compnent;
import cn.tedu.annotation.Value;


@Compnent("jdbcUtil")
public class JDBCUtil {
		@Value("#{manyProperties.driverClass}")
		private String driverClass;
		@Value("#{manyProperties.url}")
		private String url;
		@Value("#{manyProperties.userName}")
		private String userName;
		@Value("#{manyProperties.userPassword}")
		private String userPassword;
		
		public String getDriverClass() {
			return driverClass;
		}
		public String getUrl() {
			return url;
		}
		public String getUserName() {
			return userName;
		}
		public String getUserPassword() {
			return userPassword;
		}
		@Override
		public String toString() {
			return "JDBCUtil [driverClass=" + driverClass + ", url=" + url + ", userName=" + userName + ", userPassword="
					+ userPassword + "]";
		}

}
