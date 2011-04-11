package nl.uva.mediamosa.bb.web.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.uva.ic.vpcore.bb.ServiceManagerSingleton;
import nl.uva.mediamosa.MediaMosaService;
import nl.uva.mediamosa.SearchParameters;
import nl.uva.mediamosa.model.AssetType;
import nl.uva.mediamosa.util.ServiceException;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import blackboard.data.user.User;
import blackboard.platform.context.Context;
import blackboard.platform.context.ContextManager;
import blackboard.platform.context.ContextManagerFactory;
import blackboard.platform.log.LogService;
import blackboard.platform.log.LogServiceFactory;

public class BrowseController extends SimpleFormController {
	private static final LogService logger = LogServiceFactory.getInstance();
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		
		// get context
		ContextManager ctxMgr = ContextManagerFactory.getInstance();
		Context ctx = ctxMgr.setContext(request);
		User user = ctx.getUser();
		String userName = user.getUserName();
		// release context		 
		ctxMgr.releaseContext();
		
		ServiceManagerSingleton singleton = ServiceManagerSingleton.getInstance();
		MediaMosaService service = null;
		try {
			service = singleton.getMediaMosaService();
		} catch (ServiceException e) {
			logger.logError(e.getMessage(), e);
		}
		
		Map <String, String> searchParams = new HashMap <String, String>();
		searchParams.put(SearchParameters.OWNERID, userName);
		List<AssetType> assetList = null;
		try {
			assetList = service.getAssets(searchParams);
		} catch (ServiceException e) {
			logger.logError(e.getMessage(), e);
		} catch (IOException e) {
			logger.logError(e.getMessage(), e);
		}
		
		return new ModelAndView("browse", "assets", assetList);
	}

}
