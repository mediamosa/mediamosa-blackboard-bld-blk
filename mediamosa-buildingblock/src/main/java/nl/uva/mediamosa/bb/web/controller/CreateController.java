package nl.uva.mediamosa.bb.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.uva.mediamosa.bb.web.bean.ContentBean;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import blackboard.base.FormattedText;
import blackboard.data.ValidationException;
import blackboard.data.content.Content;
import blackboard.data.course.Course;
import blackboard.persist.BbPersistenceManager;
import blackboard.persist.Id;
import blackboard.persist.PersistenceException;
import blackboard.persist.content.ContentDbPersister;
import blackboard.platform.log.LogService;
import blackboard.platform.log.LogServiceFactory;
import blackboard.platform.persistence.PersistenceServiceFactory;
import blackboard.platform.plugin.PlugInUtil;

public class CreateController extends SimpleFormController {
	private static final LogService logger = LogServiceFactory.getInstance();

	public String contentId;
	public String courseId;
	
	public CreateController() {
		setCommandClass(ContentBean.class);
		setCommandName("content");
	}
	
	protected Map<String, String> referenceData(HttpServletRequest request) {
		
		contentId = request.getParameter("content_id");
		courseId = request.getParameter("course_id");
		
		Map<String, String> model = new HashMap<String, String>();
		model.put("recallUrl", PlugInUtil.getEditableContentReturnURL(contentId, courseId));
		
		return model;
	}

	// override onSubmit() to access reference data from the success view
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws PersistenceException, ValidationException {

		contentId = request.getParameter("content_id");
		courseId = request.getParameter("course_id");
		
		ContentBean contentBean = (ContentBean) command;
		BbPersistenceManager bbPm = PersistenceServiceFactory.getInstance().getDbPersistenceManager();
		// create a course document and set all desired attributes
		Content content = new Content();
		content.setTitle(contentBean.getTitle());
		
		String assetId = request.getParameter("assetId");
		String url = PlugInUtil.getUri("UVA", "BB-MEDIAMOSA", "PlayProxy");
		
		// switch to mediafileId
		// link to swfobject not needed as it is include in html object response
		
		String javascript = "<br/>\n" + 
		"<div id=\"" + assetId + "\"><p style=\"background:#000;color:#fff;width:300px;height:200px;padding:5px;\">Loading media...</p></div>\n" + 
		"<script type=\"text/javascript\" src=\""+ PlugInUtil.getUri("UVA", "BB-MEDIAMOSA", "player/swfobject.js") + "\"></script>\n" +
		"<script type=\"text/javascript\">\n" + 
		"//<![CDATA[\n" +
		    "document.observe(\"dom:loaded\", function() {\n" +  
		    "\tnew Ajax.Updater(\"" + assetId + "\", \"" + url + "\", {\n" +
		    	"\t\tmethod: \"get\", \n" +
		    	"\t\trequestTimeout: 30, \n" +
		    	"\t\tevalScripts: true, \n" +
		    	"\t\tparameters: {id:\"" + assetId + "\"}\n" + // start and duration parameters in case of fragment  
		    	"\t});\n" +
			"});" +
		"//]]>\n" +
		"</script>";
		
		FormattedText text = new FormattedText(javascript, FormattedText.Type.HTML);
		content.setBody(text);
		content.setContentHandler("resource/x-bb-mediamosa");
		// ... set additional attributes ...
		// these attributes of content require valid Ids... create and set them
		Id bbCourseId = bbPm.generateId(Course.DATA_TYPE, courseId);
		Id bbContentId = bbPm.generateId(Content.DATA_TYPE, contentId);
		content.setCourseId(bbCourseId);
		content.setParentId(bbContentId);
		
		//validate the new content item
		content.validate();
		
		// retrieve the content persister and persist the content item
		ContentDbPersister persister = (ContentDbPersister) bbPm.getPersister(ContentDbPersister.TYPE);
		persister.persist(content);
		
		ModelAndView mav = new ModelAndView(getSuccessView(), "content", contentBean);
		mav.getModel().putAll(referenceData(request));
		return mav;
	}

}
