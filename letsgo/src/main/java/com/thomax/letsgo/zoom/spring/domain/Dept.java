package com.thomax.letsgo.zoom.spring.domain;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Component("dept")
@Scope(scopeName="prototype") 
public class Dept {

	private int deptno;
	private String dname;
	private String loc;
	
	public Dept() {
		super();
	}
	public Dept(int deptno, String dname, String loc) {
		super();
		this.deptno = deptno;
		this.dname = dname;
		this.loc = loc;
	}
	
	@PostConstruct
	public void init02() {
		System.out.println("Dept.init开始！");
	}
	@PreDestroy
	public void destroy02() {
		System.out.println("Dept.destroy结束！");
	}
	
	public int getDeptno() {
		return deptno;
	}
	public void setDeptno(int deptno) {
		this.deptno = deptno;
	}
	public String getDname() {
		return dname;
	}
	public void setDname(String dname) {
		this.dname = dname;
	}
	public String getLoc() {
		return loc;
	}
	public void setLoc(String loc) {
		this.loc = loc;
	}
	
	@Override
	public String toString() {
		return "Dept [deptno=" + deptno + ", dname=" + dname + ", loc=" + loc + "]";
	}
	
}
