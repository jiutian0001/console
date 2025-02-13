package com.bxsbot.console.bean;

public class SysConfig {

	private Integer  id,sysCode,sta;
	private String sysKey,sysName,sysVal;
	
	
	
	public SysConfig( Integer sysCode, String sysName) {
		super();
		this.sysCode = sysCode;
		this.sysName = sysName;
	}
	
	public SysConfig() {
		super();
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getSysCode() {
		return sysCode;
	}
	public void setSysCode(Integer sysCode) {
		this.sysCode = sysCode;
	}
	public Integer getSta() {
		return sta;
	}
	public void setSta(Integer sta) {
		this.sta = sta;
	}
	public String getSysKey() {
		return sysKey;
	}
	public void setSysKey(String sysKey) {
		this.sysKey = sysKey;
	}
	public String getSysName() {
		return sysName;
	}
	public void setSysName(String sysName) {
		this.sysName = sysName;
	}
	public String getSysVal() {
		return sysVal;
	}
	public void setSysVal(String sysVal) {
		this.sysVal = sysVal;
	}
	
	
}
