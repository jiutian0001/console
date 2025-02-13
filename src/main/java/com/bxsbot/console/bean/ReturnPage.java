package com.bxsbot.console.bean;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ReturnPage{

	private List<CssJsFile> js;
	private List<CssJsFile> css;
	private List<Button> buttons;
	private List<Component> search;
	private List<Component> info;
	private List<Component> table;
	//private List<Component> components;
	//private List<Column> infoColumns;
	//private List<Column> tableColumns;
	private String returnPage;
	private String cmd;
	private String nextCmd;
	private Page page;
	private Map<String,Object> val=new ConcurrentHashMap<String, Object>();
	
	
	
	
	
	public String getCmd() {
		return cmd;
	}
	public void setCmd(String cmd) {
		this.cmd = cmd;
	}
	public Page getPage() {
		return page;
	}
	public void setPage(Page page) {
		this.page = page;
	}
	public String getNextCmd() {
		return nextCmd;
	}
	public void setNextCmd(String nextCmd) {
		this.nextCmd = nextCmd;
	}
	public String getReturnPage() {
		return returnPage;
	}
	public void setReturnPage(String returnPage) {
		this.returnPage = returnPage;
	}
	public Map<String, Object> getVal() {
		return val;
	}
	public void setVal(Map<String, Object> val) {
		this.val = val;
	}
	public List<Component> getSearch() {
		return search;
	}
	public void setSearch(List<Component> search) {
		this.search = search;
	}
	public List<Component> getInfo() {
		return info;
	}
	public void setInfo(List<Component> info) {
		this.info = info;
	}
	public List<Component> getTable() {
		return table;
	}
	public void setTable(List<Component> table) {
		this.table = table;
	}
	
	public List<CssJsFile> getJs() {
		return js;
	}
	public void setJs(List<CssJsFile> js) {
		this.js = js;
	}
	public List<CssJsFile> getCss() {
		return css;
	}
	public void setCss(List<CssJsFile> css) {
		this.css = css;
	}
	public List<Button> getButtons() {
		return buttons;
	}
	public void setButtons(List<Button> buttons) {
		this.buttons = buttons;
	}
	
}
