package nl.uva.mediamosa.bb.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import blackboard.platform.context.Context;
import blackboard.platform.context.ContextManager;
import blackboard.platform.context.ContextManagerFactory;
import blackboard.platform.log.LogService;
import blackboard.platform.log.LogServiceFactory;
import blackboard.platform.ui.struts.dynamiccontextmenu.BaseContextMenuGenerator;
import blackboard.servlet.data.ngui.ContextMenuItem;
import blackboard.servlet.util.BaseContextMenuUtil;


public class BrowseMenuItemsController extends BaseContextMenuGenerator implements Controller {
	private static final LogService logger = LogServiceFactory.getInstance();

	@Override
	protected List<List<ContextMenuItem>> generateContextMenu( HttpServletRequest request ) {
		List<List<ContextMenuItem>> result = new ArrayList<List<ContextMenuItem>>();
		List<ContextMenuItem> group = new ArrayList<ContextMenuItem>();
		result.add(group);
		
		String assetId = request.getParameter("assetId");
		String assetOwner = request.getParameter("assetOwner");
		String contentId = request.getParameter("contentId");
		String courseId = request.getParameter("courseId");
		
		ContextManager ctxMgr = ContextManagerFactory.getInstance();
		Context ctx = ctxMgr.setContext(request);
		String userName = ctx.getUser().getUserName();
		// use BB api to retrieve contentId and courseId, is this better than using request params?
		// ctx.getContentId().toString();
		// ctx.getCourseId().toString();

		ContextMenuItem itemPlay = new ContextMenuItem();
		itemPlay.setTitle("Play");
		itemPlay.setToolTip("Play mediafile");
		itemPlay.setUrl("play.htm?id=" + assetId);
		group.add(itemPlay);
		
		// only owner is allowed to edit and delete media
		if(userName.equals(assetOwner)) {
			ContextMenuItem itemEdit = new ContextMenuItem();
			itemEdit.setTitle("Edit");
			itemEdit.setToolTip("Edit title and description");
			itemEdit.setUrl("edit.htm?assetId=" + assetId);
			group.add(itemEdit);

			ContextMenuItem itemDelete = new ContextMenuItem();
			itemDelete.setTitle("Delete");
			itemDelete.setToolTip("Delete mediafile");
			itemDelete.setUrl("manageAssets.htm?action=deleteAsset&assetId=" + assetId);
			group.add(itemDelete);
		}
		
		if(!StringUtils.isEmpty(contentId) && !StringUtils.isEmpty(courseId) && !StringUtils.isEmpty(assetId)) {
			ContextMenuItem itemAddToCourse = new ContextMenuItem();
			itemAddToCourse.setTitle("Add To Course");
			itemAddToCourse.setToolTip("Add mediafile to course");
			itemAddToCourse.setUrl("create.htm?assetId=" + assetId + "&content_id=" + contentId +  "&course_id=" + courseId);
			group.add(itemAddToCourse);
		}
		return result;
	}

	public ModelAndView handleRequest(HttpServletRequest req, HttpServletResponse resp) {
	
		Map<String, String> resultMap = new HashMap<String,String>();
        List<List<ContextMenuItem>> menuLists = generateContextMenu(req);
	    try {
			BaseContextMenuUtil.buildHtmlCode(menuLists, resultMap, resp, null);
		} catch (Exception e) {
			logger.logError(e.getMessage(), e);
		}
		return null;
	}

}
