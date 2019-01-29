package com.thomax.letsgo.jdbc.domain;

import java.sql.Date;
import java.sql.Time;

public class PlayerTable {

	private int id;
	private String name;
	private Date transfer;
	private Time endless;
	private String description;
	private int salary;
	
	public PlayerTable() {
		super();
	}
	
	public PlayerTable(int id, String name, Date transfer, Time endless, String description, int salary) {
		super();
		this.id = id;
		this.name = name;
		this.transfer = transfer;
		this.endless = endless;
		this.description = description;
		this.salary = salary;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getTransfer() {
		return transfer;
	}
	public void setTransfer(Date transfer) {
		this.transfer = transfer;
	}
	public Time getEndless() {
		return endless;
	}
	public void setEndless(Time endless) {
		this.endless = endless;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getSalary() {
		return salary;
	}
	public void setSalary(int salary) {
		this.salary = salary;
	}

	@Override
	public String toString() {
		return this.id + "  " + this.name + "\t" + this.transfer + "\t" + 
				this.endless + "\t" + this.description + "\t" + this.salary;
	}

}
