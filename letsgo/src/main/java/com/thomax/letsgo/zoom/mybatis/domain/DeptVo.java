package com.thomax.letsgo.zoom.mybatis.domain;

import java.util.List;

public class DeptVo {

	private int deptno;
	private String dname;
	private String loc;
	private List<Integer> deptnoList; //作为条件where deptno in(x, x, x)
	
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
	public List<Integer> getDeptnoList() {
		return deptnoList;
	}
	public void setDeptnoList(List<Integer> deptnoList) {
		this.deptnoList = deptnoList;
	}
	
}
