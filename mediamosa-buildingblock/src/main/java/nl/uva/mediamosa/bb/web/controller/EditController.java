package nl.uva.mediamosa.bb.web.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import nl.uva.ic.vpcore.bb.ServiceManagerSingleton;
import nl.uva.mediamosa.MediaMosaService;
import nl.uva.mediamosa.bb.web.bean.AssetBean;
import nl.uva.mediamosa.model.AssetDetailsType;
import nl.uva.mediamosa.util.ServiceException;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import blackboard.platform.log.LogService;
import blackboard.platform.log.LogServiceFactory;
import blackboard.platform.plugin.PlugInException;

public class EditController extends SimpleFormController {
	private static final LogService logger = LogServiceFactory.getInstance();
	
	private final ServiceManagerSingleton singleton = ServiceManagerSingleton.getInstance();
	public String assetId;
	
	public EditController() {
		setCommandClass(AssetBean.class);
		setCommandName("asset");
	}
	
	protected Object formBackingObject(HttpServletRequest request) throws ServiceException, IOException {
		
		assetId = request.getParameter("assetId");	
		AssetBean asset = new AssetBean();
		
		// only retrieve asset metadata in case of GET request
		if ("GET".equals(request.getMethod())) {
			MediaMosaService service = singleton.getMediaMosaService();
			AssetDetailsType assetDetails = service.getAssetDetails(assetId);
			String title = assetDetails.getDublinCore().getTitle();
			asset.setTitle(title);
			String description = assetDetails.getDublinCore().getDescription();
			asset.setDescription(description);
			String owner = assetDetails.getOwnerId();
			asset.setOwner(owner);
		}
		asset.setAssetId(assetId);
		
		return asset;
	}

	protected ModelAndView onSubmit(Object command, BindException errors) throws IOException, PlugInException {
		
		AssetBean asset = (AssetBean) command;
		// save metadata for asset
  		String assetId  = asset.getAssetId();
  		String title = asset.getTitle();
  		String description = asset.getDescription();
  		String userName = asset.getOwner();
  		
  		Map <String, String> metadata = new HashMap <String, String>();
  		metadata.put("title", title);
  		metadata.put("description", description);
  		  		
		try {
			MediaMosaService service = singleton.getMediaMosaService();
			service.setMetadata(assetId, userName, metadata);
		} catch (ServiceException e) {
			// Failed to update metadata
			logger.logError(e.getMessage(), e);
		}
		
		return new ModelAndView(getSuccessView(), "asset", asset);
	}
	
}
