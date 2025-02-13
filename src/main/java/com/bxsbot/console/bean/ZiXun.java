package com.bxsbot.console.bean;

public class ZiXun {
	private Integer id;
	private String text;
	private Integer sta;

	
	public  ZiXun() {}
	public  ZiXun(Integer id,String text) {
		this.id=id;
		this.text=text;
	}
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Integer getSta() {
		return sta;
	}

	public void setSta(Integer sta) {
		this.sta = sta;
	}

}
