package nl.uva.mediamosa.bb.web.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import nl.uva.ic.vpcore.bb.ServiceManagerSingleton;
import nl.uva.mediamosa.MediaMosaService;
import nl.uva.mediamosa.bb.web.bean.UploadBean;
import nl.uva.mediamosa.model.UploadTicketType;
import nl.uva.mediamosa.util.ServiceException;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import blackboard.platform.context.Context;
import blackboard.platform.context.ContextManager;
import blackboard.platform.context.ContextManagerFactory;
import blackboard.platform.log.LogService;
import blackboard.platform.log.LogServiceFactory;
import blackboard.platform.plugin.PlugInException;
import blackboard.platform.plugin.PlugInUtil;

public class UploadController extends SimpleFormController {
	private static final LogService logger = LogServiceFactory.getInstance();
	
	private final ServiceManagerSingleton singleton = ServiceManagerSingleton.getInstance();
	public String assetId;
	
	public UploadController() {
		setCommandClass(UploadBean.class);
		setCommandName("upload");
	}
	
	protected Map<String, String> referenceData(HttpServletRequest request) throws ServiceException, IOException  {
		// get context
		ContextManager ctxMgr = ContextManagerFactory.getInstance();
		Context ctx = ctxMgr.setContext(request);
		String userName = ctx.getUser().getUserName();
		// release context		 
		ctxMgr.releaseContext();
		
		MediaMosaService service = singleton.getMediaMosaService();
		String mediafileId = service.createMediafile(assetId, userName);
		UploadTicketType uploadTicket = service.createUploadTicket(mediafileId, userName);
		String uploadUrl = uploadTicket.getAction();
		
		Map<String, String> model = new HashMap<String, String>();
		model.put("imageUrl", PlugInUtil.getUri("UVA", "BB-MEDIAMOSA", "images/XPButtonUploadText_61x22.png"));
		model.put("flashUrl", PlugInUtil.getUri("UVA", "BB-MEDIAMOSA", "swf/swfupload.swf"));
		model.put("uploadUrl", uploadUrl);
		model.put("filetypes", ServiceManagerSingleton.getInstance().getFiletypes());
			
		return model;
	}
	
	protected Object formBackingObject(HttpServletRequest request) throws ServiceException, IOException {
		// get context
		ContextManager ctxMgr = ContextManagerFactory.getInstance();
		Context ctx = ctxMgr.setContext(request);
		String userName = ctx.getUser().getUserName();
		// release context		 
		ctxMgr.releaseContext();

		// only create assets in case of GET request
		if ("GET".equals(request.getMethod())) {
			MediaMosaService service = singleton.getMediaMosaService();
			assetId = service.createAsset(userName);	
		}
		UploadBean upload = new UploadBean();
		upload.setOwner(userName);		
		upload.setAssetId(assetId);
		
		return upload;
	}
	
	protected ModelAndView onSubmit(Object command, BindException errors) throws IOException, PlugInException {
		
		UploadBean upload = (UploadBean) command;
		
  		String assetId  = upload.getAssetId();
  		String title = upload.getTitle();
  		String description = upload.getDescription();
  		String userName = upload.getOwner();
  		
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
  				       
		return new ModelAndView(getSuccessView(), "upload", upload);
	}
    
	
}
