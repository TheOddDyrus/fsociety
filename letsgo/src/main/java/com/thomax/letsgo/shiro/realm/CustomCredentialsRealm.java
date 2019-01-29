package com.thomax.letsgo.shiro.realm;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

public class CustomCredentialsRealm extends AuthorizingRealm {

	@Override
	//授权方法
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principal) {
		return null;
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
