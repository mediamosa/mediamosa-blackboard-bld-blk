package nl.uva.mediamosa.bb.web.controller;

import java.io.File;
import java.io.IOException;

import nl.uva.ic.vpcore.bb.ConfigProperties;
import nl.uva.mediamosa.bb.web.bean.ConfigBean;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import blackboard.platform.log.LogService;
import blackboard.platform.log.LogServiceFactory;
import blackboard.platform.plugin.PlugInException;
import blackboard.platform.plugin.PlugInUtil;

public class ConfigController extends SimpleFormController {
	private static final LogService logger = LogServiceFactory.getInstance();
	
	public ConfigController() {
		setCommandClass(ConfigBean.class);
	}
	
	protected ModelAndView onSubmit(Object command, BindException errors) throws IOException, PlugInException {
		ConfigBean config = (ConfigBean) command;
		
        File configFile = new File(PlugInUtil.getConfigDirectory("UVA", "BB-MEDIAMOSA"), "config.properties");
        // create file if it does not exist
        boolean success = configFile.createNewFile();
        if (success) {
        	logger.logInfo("MediaMosa config file did not exist and was created.");
        } else {
        	logger.logInfo("MediaMosa config file already exists.");
        }
        ConfigProperties cp = new ConfigProperties(configFile);
        cp.saveProperties(config);
        
		return new ModelAndView(getSuccessView(), "config", config);
	}
	
}
