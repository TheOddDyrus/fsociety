package com.thomax.letsgo.zoom.shiro.realm;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

public class CustomCredentialsMd5SaltRealm extends AuthorizingRealm {

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
		String hashSaltPassword = "5634628e805f1c24b77d3acbb167a74f";  //用户名去数据库查询密码（用盐"salt01"通过md5加密一次的密码）
		String credentialsSalt = "salt01";
		return new SimpleAuthenticationInfo(usercode, hashSaltPassword, ByteSource.Util.bytes(credentialsSalt), this.getName());
	}
	
	@Override
	public String getName() {
		String name = "xxxx";
		return name;
	}

}
