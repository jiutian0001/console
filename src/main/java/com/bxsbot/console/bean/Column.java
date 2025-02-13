package com.bxsbot.console.bean;

public class Column {

	private String dbName;
	private String pageName;
	private String beanName;
	
	public Column() {}
	public Column( String dbName,String pageName,String beanName) {
		this.dbName=dbName;
		this.pageName=pageName;
		this.beanName=pageName;
	}
	public String getDbName() {
		return dbName;
	}
	public void setDbName(String dbName) {
		this.dbName = dbName;
	}
	public String getPageName() {
		return pageName;
	}
	public void setPageName(String pageName) {
		this.pageName = pageName;
	}
	public String getBeanName() {
		return beanName;
	}
	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}
	
}
