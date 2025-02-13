package com.bxsbot.console.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.bxsbot.console.bean.Cmds;
import com.bxsbot.console.bean.Component;
import com.bxsbot.console.bean.CssJsFile;
import com.bxsbot.console.bean.Page;


/***
 * 通用page相关查询
 */

@Mapper
public interface PageMapper {
	
	void savePage(Page page);
	void batchInsertCssJsFiles(List<CssJsFile> cssJsFile);
	void batchInsertComponents( List<Component> list);
	void saveCmd(Cmds cmd);
}
