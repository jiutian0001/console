package com.bxsbot.console.mapper;

import java.util.List;

import com.bxsbot.console.bean.Button;
import com.bxsbot.console.bean.Cmds;
import com.bxsbot.console.bean.Component;
import com.bxsbot.console.bean.CssJsFile;
import com.bxsbot.console.bean.Page;
import com.bxsbot.console.bean.SysConfig;

public interface InitMapper {

	List<Page> initPage();

	List<Cmds> initCmd();

	List<CssJsFile> initCssJs();

	List<Button> initButtn();

	List<Component> initComponent();

	List<SysConfig> initConfig();

	List<SysConfig> initExchange();

}
