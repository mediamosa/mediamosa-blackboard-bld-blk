package nl.uva.mediamosa.bb.web.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//import nl.uva.ic.vpcore.bb.PlayerUtil;
import nl.uva.ic.vpcore.bb.ServiceManagerSingleton;
import nl.uva.mediamosa.MediaMosaService;
import nl.uva.mediamosa.bb.web.bean.MediaBean;
import nl.uva.mediamosa.model.AssetDetailsType;
import nl.uva.mediamosa.model.LinkType;
import nl.uva.mediamosa.model.MediafileDetailsType;
import nl.uva.mediamosa.util.ServiceException;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import blackboard.data.user.User;
import blackboard.persist.KeyNotFoundException;
import blackboard.persist.PersistenceException;
import blackboard.persist.user.UserDbLoader;
import blackboard.platform.context.Context;
import blackboard.platform.context.ContextManager;
import blackboard.platform.context.ContextManagerFactory;
import blackboard.platform.log.LogService;
import blackboard.platform.log.LogServiceFactory;

public class PlayController implements Controller {
	private static final LogService logger = LogServiceFactory.getInstance();
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// get context
		ContextManager ctxMgr = ContextManagerFactory.getInstance();
		Context ctx = ctxMgr.setContext(request);
		User user = ctx.getUser();
		String userName = user.getUserName();
		// release context		 
		ctxMgr.releaseContext();
		
		//String player = "";
		//String streamUrl = "";
		//String stillUrl = "";
		AssetDetailsType assetDetails = null;
		String ownerFullname = "";
		LinkType link = null;
		LinkType embedLink = null;
		MediafileDetailsType mediafileDetails = null;
		String embedcode = "";
		
		String assetId = request.getParameter("id");
		if (!StringUtils.isEmpty(assetId)) {
			ServiceManagerSingleton singleton = ServiceManagerSingleton.getInstance();
			MediaMosaService service = null;
			try {
				service = singleton.getMediaMosaService();
				assetDetails = service.getAssetDetails(assetId);
				mediafileDetails = assetDetails.getMediafiles().getMediafile().get(0);
				link = service.getPlayLink(assetId, mediafileDetails.getMediafileId(), userName);
				// request embedcode
				embedLink = service.getPlayLink(assetId, mediafileDetails.getMediafileId(), userName, "object");
			} catch (ServiceException e) {
				embedLink = new LinkType();
				embedLink.setOutput("Error media cannot be played.");
				logger.logError(e.getMessage(), e);
			}

			//int height = mediafileDetails.getMetadata().getHeight();
			//int width = mediafileDetails.getMetadata().getWidth();
			//streamUrl = link.getOutput();
			//stillUrl = assetDetails.getVpxStillUrl();
			
			//player = PlayerUtil.generateEmbedCode(assetId, streamUrl, stillUrl, width, height, PlayerUtil.StreamType.forContainerType(mediafileDetails.getMetadata().getContainerType()) );
			
			// request embedcode
			embedcode = embedLink.getOutput();
		}

		try {
			User owner = UserDbLoader.Default.getInstance().loadByUserName(assetDetails.getOwnerId());
			ownerFullname = owner.getGivenName() + " " + owner.getFamilyName();
		} catch (KeyNotFoundException e) {
			ownerFullname = assetDetails.getOwnerId();		
		} catch (PersistenceException e) {
			logger.logError(e.getMessage(), e);
		}

		logger.logInfo("Return View");
		
		MediaBean media = new MediaBean();
		media.setAssetDetails(assetDetails);
		media.setLink(link);
		media.setMediafileDetails(mediafileDetails);
		media.setOwnerFullname(ownerFullname);
		// media.setPlayer(player);
		media.setPlayer(embedcode);
		
		return new ModelAndView("play", "media", media);
	}
	
}
