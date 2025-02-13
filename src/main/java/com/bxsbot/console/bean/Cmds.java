package com.bxsbot.console.bean;

public class Cmds {
    private Integer id;
    private String cmdName;
    private String cmdClzz;
    private String cmdFunc;
    private String cmdVerify;
    private String tableName;
    private Integer sta;
    private Integer pageId;
    private String nextCmd;
    
    
    
	public String getNextCmd() {
		return nextCmd;
	}
	public void setNextCmd(String nextCmd) {
		this.nextCmd = nextCmd;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public Integer getPageId() {
		return pageId;
	}
	public void setPageId(Integer pageId) {
		this.pageId = pageId;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getCmdName() {
		return cmdName;
	}
	public void setCmdName(String cmdName) {
		this.cmdName = cmdName;
	}
	public String getCmdClzz() {
		return cmdClzz;
	}
	public void setCmdClzz(String cmdClzz) {
		this.cmdClzz = cmdClzz;
	}
	public String getCmdFunc() {
		return cmdFunc;
	}
	public void setCmdFunc(String cmdFunc) {
		this.cmdFunc = cmdFunc;
	}
	public String getCmdVerify() {
		return cmdVerify;
	}
	public void setCmdVerify(String cmdVerify) {
		this.cmdVerify = cmdVerify;
	}
	public Integer getSta() {
		return sta;
	}
	public void setSta(Integer sta) {
		this.sta = sta;
	}
    
}
