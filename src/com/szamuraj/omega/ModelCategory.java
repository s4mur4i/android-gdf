package com.szamuraj.omega;

public class ModelCategory {
	Integer id;
	String name;
	
	public ModelCategory() {}

	public Integer getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}
	@Override
	public String toString() {
		return name;
	}	
}
