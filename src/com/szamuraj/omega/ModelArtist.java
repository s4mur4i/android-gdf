package com.szamuraj.omega;

public class ModelArtist {
	Integer id;
	String name;
	String url;
	
	public ModelArtist() {}

	public Integer getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getUrl() {
		return url;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
