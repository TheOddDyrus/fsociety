package com.thomax.letsgo.shiro.test;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class TestShiro {
	
	@Test
	//认证方法测试
	public void test01() {
		Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro-credentials.ini");
		SecurityManager securityManager = factory.getInstance();
		SecurityUtils.setSecurityManager(securityManager);
		Subject subject = SecurityUtils.getSubject();
		AuthenticationToken token = new UsernamePasswordToken("thomas", "123");
		subject.login(token);
		System.out.println("验证是否通过：" + subject.isAuthenticated());
		/*1.如果iniRealm 返回的是null，则抛出：org.apache.shiro.authc.UnknownAccountException
		  2.如果iniRealm 返回的主体对象不为null，则ModularRealmAuthenticator 将此主体对象中的凭证信息（密码）与token 中的密码进行比对，如果一致，则认证通过。否则，由
			ModularRealmAuthenticator 抛出：org.apache.shiro.authc.IncorrectCredentialsException
		*/
	}
	
	@Test
	//自定义Realm - 认证方法测试
	public void test02() {
		Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro-credentials-customRealm.ini");
		SecurityManager securityManager = factory.getInstance();
		SecurityUtils.setSecurityManager(securityManager);
		Subject subject = SecurityUtils.getSubject();
		AuthenticationToken token = new UsernamePasswordToken("thomas", "123");
		subject.login(token);
		System.out.println("验证是否通过：" + subject.isAuthenticated());
	}

	@Test
	//自定义Realm散列算法：加盐  - 认证方法测试
	public void test03() {
		/**生成hd5hash*/
		String salt = "salt01";
		String oldPassword = "123";
		int hashInterations = 1; //加密次数
		
		//加密算法一：
		Md5Hash md5Hash = new Md5Hash(oldPassword, salt, hashInterations);
		System.out.println(md5Hash.toString());
		//加密算法二：
		SimpleHash simpleHash = new SimpleHash("md5", oldPassword, salt, hashInterations);
		System.out.println(simpleHash.toString());
		System.out.println();
		//加密一次的密码：5634628e805f1c24b77d3acbb167a74f
		//加密两次的密码：d2b93a4fab2a10112371b7336e810616
		
		/**测试hd5hash*/
		Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro-credentials-customRealmSalt.ini");
		SecurityManager securityManager = factory.getInstance();
		SecurityUtils.setSecurityManager(securityManager);
		Subject subject = SecurityUtils.getSubject();
		AuthenticationToken token = new UsernamePasswordToken("thomas", oldPassword);
		subject.login(token);
		System.out.println("验证是否通过：" + subject.isAuthenticated());
	}
	
	
	/**Shiro的三种授权方法：
	 * 1.编程式：通过写if/else 授权代码块完成：
			Subject subject = SecurityUtils.getSubject();
			if(subject.hasRole("admin")) {
				//有权限
			} else {
				//无权限
			}
		2.注解式：通过在执行的Java 方法上放置相应的注解完成：
			@RequiresRoles("admin")
			public void hello() {
				//有权限
			}
		3.JSP/GSP 标签：在JSP/GSP 页面通过相应的标签完成：
			<shiro:hasRole name="admin">
				<!— 有权限—>
			</shiro:hasRole>
	*/
	@Test
	//授权方法测试
	public void test04() {
		Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro-permissions.ini");
		SecurityManager securityManager = factory.getInstance();
		SecurityUtils.setSecurityManager(securityManager);
		Subject subject = SecurityUtils.getSubject();
		AuthenticationToken token = new UsernamePasswordToken("thomas", "123");
		subject.login(token);
		System.out.println("验证是否通过：" + subject.isAuthenticated());
		System.out.println();
		
		//验证角色
		System.out.println("单个是否存在" + subject.hasRole("role1"));
		List<String> roles = Arrays.asList("role1", "role2");
		List<String> roles2 = Arrays.asList("role1");
		System.out.println("所有是否存在" + subject.hasAllRoles(roles));
		System.out.println("是否存在(测试)" + subject.hasAllRoles(roles2)); //只要ini文件包含集合内容就成功
		System.out.println();
		
		//验证权限
		System.out.println("单个是否授权：" + subject.isPermitted("emp:create"));
		System.out.println("多个是否授权：" + subject.isPermittedAll("emp:create","emp:update"));
	}
	
	@Test
	//自定义Realm授权方法测试
	public void test05() {
		Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro-permissions_customRealm.ini");
		SecurityManager securityManager = factory.getInstance();
		SecurityUtils.setSecurityManager(securityManager);
		Subject subject = SecurityUtils.getSubject();
		AuthenticationToken token = new UsernamePasswordToken("thomas", "123");
		subject.login(token);
		
		//验证权限
		System.out.println("单个是否授权：" + subject.isPermitted("emp:create"));
		System.out.println("多个是否授权：" + subject.isPermittedAll("emp:create","emp:update"));
		
		//检查授权(如果不在授权范围，则抛出异常)
		subject.checkPermission("emp:create");
		subject.checkPermissions("emp:create","emp:update");
	}
	
}









