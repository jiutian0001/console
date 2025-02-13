package com.bxsbot.console.bean;

import java.util.List;

public class ComponentForm {
	 private List<Component> components;
	 private List<CssJsFile> cssJsFile;

	    // Getters and Setters
	    public List<Component> getComponents() {
	        return components;
	    }

	    public void setComponents(List<Component> components) {
	        this.components = components;
	    }

		public List<CssJsFile> getCssJsFile() {
			return cssJsFile;
		}

		public void setCssJsFile(List<CssJsFile> cssJsFile) {
			this.cssJsFile = cssJsFile;
		}
	    
}
