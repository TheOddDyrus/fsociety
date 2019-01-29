package com.thomax.letsgo.mybatis.domain;

import java.util.Date;

public class EmpX {

	private int empnoX;
	private String enameX;
	private String jobX;
	private int mgrX;
	private Date hiredateX;
	private int salX;
	private int commX;
	private int deptnoX;
	
	public EmpX() {
		super();
	}
	public EmpX(int empno, String ename, String job, int mgr, Date hiredate, int sal, int comm, int deptno) {
		super();
		this.empnoX = empno;
		this.enameX = ename;
		this.jobX = job;
		this.mgrX = mgr;
		this.hiredateX = hiredate;
		this.salX = sal;
		this.commX = comm;
		this.deptnoX = deptno;
	}

	public int getEmpnoX() {
		return empnoX;
	}

	public void setEmpnoX(int empno) {
		this.empnoX = empno;
	}

	public String getEnameX() {
		return enameX;
	}

	public void setEnameX(String ename) {
		this.enameX = ename;
	}

	public String getJobX() {
		return jobX;
	}

	public void setJobX(String job) {
		this.jobX = job;
	}

	public int getMgrX() {
		return mgrX;
	}

	public void setMgrX(int mgr) {
		this.mgrX = mgr;
	}

	public Date getHiredateX() {
		return hiredateX;
	}

	public void setHiredateX(Date hiredate) {
		this.hiredateX = hiredate;
	}

	public int getSalX() {
		return salX;
	}

	public void setSalX(int sal) {
		this.salX = sal;
	}

	public int getCommX() {
		return commX;
	}

	public void setCommX(int comm) {
		this.commX = comm;
	}

	public int getDeptnoX() {
		return deptnoX;
	}

	public void setDeptnoX(int deptno) {
		this.deptnoX = deptno;
	}

	@Override
	public String toString() {
		return "EmpX [empnoX=" + empnoX + ", enameX=" + enameX + ", jobX=" + jobX + ", mgrX=" + mgrX + ", hiredateX=" + hiredateX
				+ ", salX=" + salX + ", commX=" + commX + ", deptnoX=" + deptnoX + "]";
	}
	
}
