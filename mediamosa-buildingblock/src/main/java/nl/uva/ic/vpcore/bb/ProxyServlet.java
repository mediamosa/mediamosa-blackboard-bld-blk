package nl.uva.ic.vpcore.bb;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.whirlycott.cache.Cache;
import com.whirlycott.cache.CacheException;
import com.whirlycott.cache.CacheManager;

import nl.uva.mediamosa.model.AssetDetailsType;
import nl.uva.mediamosa.model.LinkType;
import nl.uva.mediamosa.model.MediafileDetailsType;
import nl.uva.mediamosa.MediaMosaService;
import nl.uva.mediamosa.util.ServiceException;

import blackboard.base.InitializationException;
import blackboard.platform.BbServiceException;
import blackboard.platform.BbServiceManager;
import blackboard.platform.context.Context;
import blackboard.platform.context.ContextManager;
import blackboard.platform.log.LogService;
import blackboard.platform.log.LogServiceFactory;

public class ProxyServlet extends HttpServlet {

    private static final long serialVersionUID = 3835345833351129162L;
    private static final LogService logger = LogServiceFactory.getInstance();

    /**
     * Initialize this servlet.
     * @throws CacheException 
     */
    public void init() throws ServletException {
//		super.init();
//        try {
//        	ServiceManagerSingleton singleton = ServiceManagerSingleton.getInstance();
//			service = singleton.getMediaMosaService();
//		} catch (ServiceException e) {
//			logger.logError("servlet init ServiceException: " + e.getMessage(), e);
//		}
    }

    /**
     * Process a GET request for the specified resource.
     *
     * @param request The servlet request we are processing
     * @param response The servlet response we are creating
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet-specified error occurs
     */
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws IOException, ServletException {
        logger.logInfo("get player");
        ContextManager ctxMgr = null;
        try {
            ctxMgr = (ContextManager) BbServiceManager.lookupService(ContextManager.class);
        } catch (InitializationException e) {
            logger.logError(e.getMessage(), e);
            throw new ServletException(e.getMessage(), e);
        } catch (BbServiceException e) {
            logger.logError(e.getMessage(), e);
            throw new ServletException(e.getMessage(), e);
        }

        Context ctx = null;
        if (ctxMgr != null) {
            ctx = ctxMgr.setContext(request);
            ctxMgr.releaseContext();
        } else {
            throw new ServletException("ContextManager is null");
        }
        String userName = ctx.getUser().getUserName();

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String assetId = request.getParameter("id");
        // String mediafileId = request.getParameter("id");
        if (assetId == null) {
            throw new ServletException("ID is Null");
        }

        try {
            ServiceManagerSingleton singleton = ServiceManagerSingleton.getInstance();
            MediaMosaService service = singleton.getMediaMosaService();

            String player = "Sorry, an error occured.";

            if(!StringUtils.isEmpty(assetId)) {
                try {
                	// check if assetId is already in cache 
                    if (cacheRetrieve(assetId) == null) {
                        AssetDetailsType assetDetails = null;
                        try {
                            assetDetails = service.getAssetDetails(assetId);
                            // MediafileDetails mediafileDetails = service.getMediafileDetails(mediafileId);
                        } catch (ServiceException e) {
                            logger.logError(e.getMessage(), e);
                            ServletException exception = new ServletException(e.getMessage(), e);
                            throw exception;
                        }
                        // does asset exist or is it deleted?  
                        if (assetDetails == null) {
                        	player = String.format("Sorry, the requested asset (%s) is unavailable. It was removed by the owner.", assetId);
                        // asset still exists
                        } else {
                            // MediafileDetailsType mediafileDetails = assetDetails.getMediafiles().getMediafile().get(0);
                        	MediafileDetailsType mediafileDetails = null;
                            if (assetDetails.getMediafiles().getMediafile().size() > 1) {
                            	// transcoded version, used for instance when orignal .3gp file cannot be streamed.
                            	mediafileDetails = assetDetails.getMediafiles().getMediafile().get(1);
                            } else {
                            	// original version, not transcoded
                            	mediafileDetails = assetDetails.getMediafiles().getMediafile().get(0);
                            }
                            LinkType link = null;
                            try {
                                // check if mediafile width is greater than maximum width specified in config settings.
                                int maxwidth = singleton.getMaxwidth();
                                int mediafileWidth = mediafileDetails.getMetadata().getWidth();
                                if (mediafileWidth > maxwidth) {
                                	link = service.getPlayLink(assetId, mediafileDetails.getMediafileId(), userName, maxwidth);
                                } else {
                                	link = service.getPlayLink(assetId, mediafileDetails.getMediafileId(), userName, "object");
                                }
                            } catch (ServiceException e) {
                                logger.logError(e.getMessage(), e);
                                ServletException exception = new ServletException(e.getMessage(), e);
                                throw exception;
                            }
                            if (link != null) {
                            	player = link.getOutput();	
                            }
                            cacheStore(assetId, player);
                        }
                    // retrieve from cache
                    } else {
                        player = cacheRetrieve(assetId);
                    }
                } catch (CacheException e) {
                    logger.logError(e.getMessage(), e);
                }
            }
            out.println(player);
        } catch (ServiceException e) {
            logger.logError(e.getMessage(), e);
        }
        out.close();
    }

    private String cacheRetrieve(String id) throws CacheException {
    	Cache c = CacheManager.getInstance().getCache();
        return (String) c.retrieve(id);
    }

    private void cacheStore(String id, String player) throws CacheException {
    	Cache c = CacheManager.getInstance().getCache();
        // expire after 9 minutes (in milliseconds)
        // long expire = 540000;
        // set caching timeout to 2 minutes = 120000 ms
        long expire = 120000;
        c.store(id, player, expire);
    }
}
