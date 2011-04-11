package nl.uva.mediamosa.bb.web.controller;

import java.io.IOException;
import java.util.List;

import nl.uva.ic.vpcore.bb.ServiceManagerSingleton;
import nl.uva.mediamosa.MediaMosaService;
import nl.uva.mediamosa.bb.web.bean.SearchBean;
import nl.uva.mediamosa.model.AssetType;
import nl.uva.mediamosa.util.ServiceException;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import blackboard.platform.log.LogService;
import blackboard.platform.log.LogServiceFactory;
import blackboard.platform.plugin.PlugInException;

public class SearchController extends SimpleFormController {
	private static final LogService logger = LogServiceFactory.getInstance();

	public SearchController() {
		setCommandClass(SearchBean.class);
		setCommandName("search");
	}
	
	protected ModelAndView onSubmit(Object command, BindException errors) throws IOException, PlugInException {
	
		SearchBean search = (SearchBean) command;
  		String searchterm  = search.getSearchTerm();
  		String cql = "((title all \"" + searchterm +  "\") OR (description all \"" + searchterm + "\") OR (subject all \"" + searchterm + "\") OR (owner_id all \"" + searchterm + "\") OR (group_id all \"" + searchterm + "\"))";
  		List<AssetType> assetList = null;

  		// only perform search when keyword is provided
  		if (!StringUtils.isEmpty(searchterm)) {	
  			ServiceManagerSingleton singleton = ServiceManagerSingleton.getInstance();
  			MediaMosaService service = null;
  			try {
  				service = singleton.getMediaMosaService();
  				assetList = service.getCqlAssets(cql);
  			} catch (ServiceException e) {
  				logger.logError(e.getMessage(), e);
  			}
  		}

  		return new ModelAndView("search", "assets", assetList);
	}
}
