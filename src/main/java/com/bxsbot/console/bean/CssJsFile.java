package com.bxsbot.console.bean;

public class CssJsFile {
    private Integer id;
    private String filePath;
    private Integer loadOrder;
    private Integer identifier;
    private Integer pageId;
    private Integer typeFiles;
    private Integer sta;
	public CssJsFile() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	public Integer getSta() {
		return sta;
	}


	public void setSta(Integer sta) {
		this.sta = sta;
	}


	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public Integer getLoadOrder() {
		return loadOrder;
	}
	public void setLoadOrder(Integer loadOrder) {
		this.loadOrder = loadOrder;
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
	public Integer getTypeFiles() {
		return typeFiles;
	}
	public void setTypeFiles(Integer typeFiles) {
		this.typeFiles = typeFiles;
	}

   
    
}
