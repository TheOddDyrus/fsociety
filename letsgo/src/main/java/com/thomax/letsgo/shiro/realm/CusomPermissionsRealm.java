package com.thomax.letsgo.shiro.realm;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.ArrayList;
import java.util.List;

public class CusomPermissionsRealm extends AuthorizingRealm {

	@Override
	//授权方法(调用授权方法一定要通过认证方法)
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principal) {
		String usercode = (String) principal.getPrimaryPrincipal();
		if (usercode == null) {
			return null;
		}
		List<String> permissions = new ArrayList<>();
		permissions.add("emp:create");
		permissions.add("emp:update");
		permissions.add("emp:delete");
		SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
		simpleAuthorizationInfo.addStringPermissions(permissions);
		
		return simpleAuthorizationInfo;
	}

	@Override
	//认证方法
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		String usercode = (String) token.getPrincipal(); //用户名
		if (usercode == null) {
			return null;
		}
		String password = "123";  //用户名去数据库查询密码
		return new SimpleAuthenticationInfo(usercode, password, this.getName());
	}

}
