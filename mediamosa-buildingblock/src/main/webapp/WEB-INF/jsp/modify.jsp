<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="/bbNG" prefix="bbNG" %>
<bbNG:learningSystemPage authentication="Y">
	<bbNG:pageHeader>
		<bbNG:pageTitleBar>MediaMosa: modify content</bbNG:pageTitleBar>
		<bbNG:breadcrumbBar environment="COURSE"> <%-- CTRL_PANEL --%>
			<bbNG:breadcrumb>Modify MediaMosa Content</bbNG:breadcrumb>
		</bbNG:breadcrumbBar>
	</bbNG:pageHeader>
	<form:form method="POST" commandName="content">
	<bbNG:dataCollection>
		<bbNG:step title="Content information" hideNumber="false">
			<bbNG:dataElement label="Title" isRequired="true">
				<input type="text" name="title" size="50" value="${content.title}"/>
			</bbNG:dataElement>
		</bbNG:step>
		<bbNG:stepSubmit title="Submit" hideNumber="false"/>
	</bbNG:dataCollection>
	</form:form>
</bbNG:learningSystemPage>
