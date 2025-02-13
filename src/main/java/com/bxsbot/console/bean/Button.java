package com.bxsbot.console.bean;

public class Button {
    private int id;
    private String label,jsFunc;
    private String action;
    private Integer identifier;
    private Integer pageId;
    
    
    
	public String getJsFunc() {
		return jsFunc;
	}
	public void setJsFunc(String jsFunc) {
		this.jsFunc = jsFunc;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public Integer getIdentifier() {
		return identifier;
	}
	public void setIdentifier(Integer identifier) {
		this.identifier = identifier;
	}
	public Integer getPageId() {
		return pageId;
	}
	public void setPageId(Integer pageId) {
		this.pageId = pageId;
	}

  
}

