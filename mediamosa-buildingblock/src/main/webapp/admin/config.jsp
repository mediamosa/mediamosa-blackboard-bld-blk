<%@ page language="java" import="nl.uva.mediamosa.*, nl.uva.ic.vpcore.bb.*, nl.uva.mediamosa.model.*, java.util.*, java.io.File, blackboard.data.*,blackboard.persist.*,blackboard.db.*,blackboard.base.*,blackboard.platform.*,blackboard.platform.log.*,blackboard.platform.session.*,blackboard.platform.persistence.*,blackboard.platform.plugin.*,blackboard.platform.db.*,blackboard.data.content.*,blackboard.persist.content.*,blackboard.data.course.*,blackboard.persist.course.*,blackboard.platform.filesystem.*" pageEncoding="UTF-8"%>
<%@ taglib uri="/bbNG" prefix="bbNG" %>
<bbNG:genericPage ctxId="ctx">
  <%
  	String iconUrl = "/images/ci/icons/tools_u.gif";
  	String page_title = "MediaMosa Configuration";

	ServiceManagerSingleton singleton = ServiceManagerSingleton.getInstance();

	if ("POST".equals(request.getMethod())) {
            singleton.save(request);
            MediaMosaService service = singleton.getMediaMosaService();
  %>
	<bbNG:pageHeader>
		<bbNG:pageTitleBar><%= page_title %></bbNG:pageTitleBar>
		<bbNG:breadcrumbBar environment="SYS_ADMIN"> <%-- CTRL_PANEL --%>
			<bbNG:breadcrumb>Configure Properties</bbNG:breadcrumb>
		</bbNG:breadcrumbBar>
	</bbNG:pageHeader>
	<bbNG:receipt type="SUCCESS" title="Properties Saved" recallUrl="/webapps/blackboard/admin/manage_plugins.jsp">
      Properties successfully saved.<br/>
      The remote webservice is running MediaMosa version: <%= service.getVersion() %>	
	</bbNG:receipt>
	<br/>
  <%
	} else {
        singleton.init(); // Quick hack to prevent NPE. fix in Singleton
		ConfigPropertiesBean bean = singleton.getCb();
  %>
  	<bbNG:pageHeader>
		<bbNG:pageTitleBar><%= page_title %></bbNG:pageTitleBar>
		<bbNG:breadcrumbBar environment="SYS_ADMIN"> <%-- CTRL_PANEL --%>
			<bbNG:breadcrumb>Configure Properties</bbNG:breadcrumb>
		</bbNG:breadcrumbBar>
	</bbNG:pageHeader>

	<form action="config.jsp" method="POST">
	<bbNG:dataCollection>
		<bbNG:step title="MediaMosa parameters">
			<bbNG:dataElement label="Server" isRequired="true"><input type="text" name="<%= Config.SERVER %>" value="<%= bean.getServer() %>" size="50"></bbNG:dataElement>
      		<bbNG:dataElement label="Username" isRequired="true"><input type="text" name="<%= Config.USERNAME %>" value="<%= bean.getUsername() %>" size="50"></bbNG:dataElement>
      		<bbNG:dataElement label="Password" isRequired="true"><input type="password" name="<%= Config.PASSWORD %>" value="<%= bean.getPassword() %>" size="50"></bbNG:dataElement>
		</bbNG:step>
		<bbNG:step title="Mediaplayer dimension">
      		<bbNG:dataElement label="Maximum width" isRequired="true"><input type="text" name="<%= Config.MAXWIDTH %>" value="<%= bean.getMaxwidth() %>" size="50"></bbNG:dataElement>
    	</bbNG:step>
    	<bbNG:step title="Allowed filetypes for uploading">
			<bbNG:dataElement label="Extensions" isRequired="true"><input type="text" name="<%= Config.FILETYPES %>" value="<%= bean.getFiletypes() %>" size="50"></bbNG:dataElement>
		</bbNG:step>
		 <bbNG:stepSubmit title="Submit"/>
	</bbNG:dataCollection>
	</form>
  <%
	}
  %>
</bbNG:genericPage>
