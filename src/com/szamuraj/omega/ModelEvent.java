package com.szamuraj.omega;

public class ModelEvent {
	Integer id;
	String name;
	Integer place_id;
	String pic;
	String url;
	String date;
	Integer category_id;
	Integer besorolas;
	
	public ModelEvent() {}
	
	public ModelEvent(int id) {
		this.id = id;
	}

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

	public void setPlace_id(Integer place_id) {
		this.place_id = place_id;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public void setCategory_id(Integer category_id) {
		this.category_id = category_id;
	}

	public void setBesorolas(Integer besorolas) {
		this.besorolas = besorolas;
	}

	public Integer getPlace_id() {
		return place_id;
	}

	public String getPic() {
		return pic;
	}

	public String getUrl() {
		return url;
	}

	public String getDate() {
		return date;
	}

	public Integer getCategory_id() {
		return category_id;
	}

	public Integer getBesorolas() {
		return besorolas;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
}
