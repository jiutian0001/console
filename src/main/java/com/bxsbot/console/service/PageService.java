package com.bxsbot.console.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bxsbot.console.bean.Cmds;
import com.bxsbot.console.bean.Component;
import com.bxsbot.console.bean.ComponentForm;
import com.bxsbot.console.bean.CssJsFile;
import com.bxsbot.console.bean.Page;
import com.bxsbot.console.mapper.PageMapper;
import com.bxsbot.console.utils.Stringutils;

@Service
public class PageService {

	@Autowired
	private PageMapper mapper;

	@Transactional
	public void save(ComponentForm componentForm, Page page,Cmds cmd) {
		mapper.savePage(page);
		Integer id=page.getId();
		if(null!=id) {
			if(null!=componentForm.getComponents() && componentForm.getComponents().size()>0) {
				List<Component> dbs=new ArrayList<Component>();
				List<Component> components = componentForm.getComponents();
				for (Component c : components) {
					if(Stringutils.isNotNull(c.getDatabaseName())) {
						c.setPageId(id);
						dbs.add(c);
					}
					
					
				}
				if(dbs.size()>0) {
					mapper.batchInsertComponents(dbs);	
				}
				
			}
			List<CssJsFile> cssJsFile = componentForm.getCssJsFile();
			if(null!=cssJsFile && cssJsFile.size()>0) {
				for (CssJsFile c : cssJsFile) {
					c.setPageId(id);
				}
				mapper.batchInsertCssJsFiles(cssJsFile);
			}
			//保存cmd
			if(null!=cmd && Stringutils.isNotNull(cmd.getCmdName())) {
				cmd.setPageId(id);
				mapper.saveCmd(cmd);
			}
			
		}
	}
}
