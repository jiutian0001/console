package com.bxsbot.console.bean;

public class Component {
    private Integer id;
    private Integer pageId;
    private String type;
    private String config;
    private Integer position;
    private Integer inSearchDisplay;
    private Integer inInfoPage;
    private Integer inTableDisplay;
    private String displayName;
    private String javaBeanName;
    private String databaseName;
    private Integer cp;
    private Integer sta,mandatory ;
    
    
    
	public Integer getMandatory() {
		return mandatory;
	}
	public void setMandatory(Integer mandatory) {
		this.mandatory = mandatory;
	}
	public Integer getSta() {
		return sta;
	}
	public void setSta(Integer sta) {
		this.sta = sta;
	}
	public Component() {
		super();
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getPageId() {
		return pageId;
	}
	public void setPageId(Integer pageId) {
		this.pageId = pageId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getConfig() {
		return config;
	}
	public void setConfig(String config) {
		this.config = config;
	}
	public Integer getPosition() {
		return position;
	}
	public void setPosition(Integer position) {
		this.position = position;
	}
	public Integer getInSearchDisplay() {
		return inSearchDisplay;
	}
	public void setInSearchDisplay(Integer inSearchDisplay) {
		this.inSearchDisplay = inSearchDisplay;
	}
	public Integer getInInfoPage() {
		return inInfoPage;
	}
	public void setInInfoPage(Integer inInfoPage) {
		this.inInfoPage = inInfoPage;
	}
	public Integer getInTableDisplay() {
		return inTableDisplay;
	}
	public void setInTableDisplay(Integer inTableDisplay) {
		this.inTableDisplay = inTableDisplay;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getJavaBeanName() {
		return javaBeanName;
	}
	public void setJavaBeanName(String javaBeanName) {
		this.javaBeanName = javaBeanName;
	}
	public String getDatabaseName() {
		return databaseName;
	}
	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}
	public Integer getCp() {
		return cp;
	}
	public void setCp(Integer cp) {
		this.cp = cp;
	}
    
}

