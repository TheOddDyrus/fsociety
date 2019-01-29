package com.thomax.letsgo.mybatis.domain;

public class DeptCustom extends Dept {
	
	//private int deptno; //去掉与父类中重复的字段（一对一关系）
	private String info;

	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}

	@Override
	public String toString() {
		return super.toString() + "\t" + "DeptCustom [info=" + info + "]";
	}	
	
}
