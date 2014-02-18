package com.szamuraj.omega;

public class ModelPlace {
	 Integer id;
	 String name;
	 String url;
	 long lat;
	 long lon;
	 String addrr;
	 String email;
	 String tel;
	 
	 public ModelPlace() {}

	public Integer getId() {
		return id;
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

	public void setLat(long lat) {
		this.lat = lat;
	}

	public void setLon(long lon) {
		this.lon = lon;
	}

	public void setAddrr(String addrr) {
		this.addrr = addrr;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getName() {
		return name;
	}

	public String getUrl() {
		return url;
	}

	public long getLat() {
		return lat;
	}

	public long getLon() {
		return lon;
	}

	public String getAddrr() {
		return addrr;
	}

	public String getEmail() {
		return email;
	}

	public String getTel() {
		return tel;
	}


	 
}
