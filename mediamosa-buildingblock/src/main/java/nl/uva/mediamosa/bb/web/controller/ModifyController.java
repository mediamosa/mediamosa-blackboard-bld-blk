package nl.uva.mediamosa.bb.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.uva.mediamosa.bb.web.bean.ContentBean;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import blackboard.data.ValidationException;
import blackboard.data.content.Content;
import blackboard.data.content.CourseDocument;
import blackboard.data.course.Course;
import blackboard.persist.BbPersistenceManager;
import blackboard.persist.Container;
import blackboard.persist.Id;
import blackboard.persist.KeyNotFoundException;
import blackboard.persist.PersistenceException;
import blackboard.persist.PkId;
import blackboard.persist.content.ContentDbLoader;
import blackboard.persist.content.ContentDbPersister;
import blackboard.platform.log.LogService;
import blackboard.platform.log.LogServiceFactory;
import blackboard.platform.persistence.PersistenceServiceFactory;
import blackboard.platform.plugin.PlugInUtil;

public class ModifyController extends SimpleFormController {
	private static final LogService logger = LogServiceFactory.getInstance();
	
	public String contentId;
	public String courseId;
	
	public ModifyController() {
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

	protected Object formBackingObject(HttpServletRequest request) throws PersistenceException {
	
		contentId = request.getParameter("content_id");
		courseId = request.getParameter("course_id");
		ContentBean cntnt = new ContentBean();
		
		// only retrieve stored content in case of GET request
		if ("GET".equals(request.getMethod())) {		
			BbPersistenceManager bbPm = PersistenceServiceFactory.getInstance().getDbPersistenceManager();
			Container bbContainer = bbPm.getContainer();
			
			// Id bbCourseId = bbPm.generateId(Course.DATA_TYPE, courseId);
			Id bbContentId = new PkId(bbContainer, CourseDocument.DATA_TYPE, contentId);
			
			ContentDbLoader courseDocumentLoader = (ContentDbLoader) bbPm.getLoader(ContentDbLoader.TYPE);
			Content content = courseDocumentLoader.loadById(bbContentId);
			
			cntnt.setTitle(content.getTitle());
		}
		cntnt.setContentId(contentId);
		cntnt.setCourseId(courseId);	
		
		return cntnt;
	}
	
	// override onSubmit() to access reference data from the success view
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) {
		
		ContentBean contentBean = (ContentBean) command;
		BbPersistenceManager bbPm = PersistenceServiceFactory.getInstance().getDbPersistenceManager();
		
		Id bbCourseId = null;
		Id bbContentId = null;
		ContentDbLoader courseDocumentLoader = null;
		Content content = null;
		ContentDbPersister persister = null;
		try {
			bbCourseId = bbPm.generateId(Course.DATA_TYPE, contentBean.getCourseId());
			bbContentId = bbPm.generateId(Content.DATA_TYPE, contentBean.getContentId());
			courseDocumentLoader = (ContentDbLoader) bbPm.getLoader(ContentDbLoader.TYPE);
			content = courseDocumentLoader.loadById(bbContentId);
		    content.setTitle(contentBean.getTitle());
			content.setCourseId(bbCourseId);
			// validate the new content item
			content.validate();
			// retrieve the content persister and persist the content item
			persister = (ContentDbPersister) bbPm.getPersister(ContentDbPersister.TYPE);
			persister.persist(content);
		} catch (KeyNotFoundException e) {
			logger.logError(e.getMessage(), e);
		} catch (PersistenceException e) {
			logger.logError(e.getMessage(), e);
		} catch (ValidationException e) {
			logger.logError(e.getMessage(), e);
		}
		
		ModelAndView mav = new ModelAndView(getSuccessView(), "content", contentBean);
		mav.getModel().putAll(referenceData(request));
		return mav;
	}
	
}
