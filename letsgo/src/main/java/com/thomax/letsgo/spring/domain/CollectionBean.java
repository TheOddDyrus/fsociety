package com.thomax.letsgo.spring.domain;

import java.util.List;
import java.util.Map;
import java.util.Properties;

public class CollectionBean {

	private String[] strArr;
	private List<Emp> list;
	private Map<Integer, Emp> map;
	private Properties properties;
	
	public String[] getStrArr() {
		return strArr;
	}
	public void setStrArr(String[] strArr) {
		this.strArr = strArr;
	}
	public List<Emp> getList() {
		return list;
	}
	public void setList(List<Emp> list) {
		this.list = list;
	}
	public Map<Integer, Emp> getMap() {
		return map;
	}
	public void setMap(Map<Integer, Emp> map) {
		this.map = map;
	}
	public Properties getProperties() {
		return properties;
	}
	public void setProperties(Properties properties) {
		this.properties = properties;
	}
	
}
