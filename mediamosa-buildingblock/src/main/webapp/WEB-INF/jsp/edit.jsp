<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="/bbNG" prefix="bbNG"%>
<bbNG:learningSystemPage authentication="Y" ctxId="ctx">
	<bbNG:pageHeader>
		<bbNG:breadcrumbBar environment="CTRL_PANEL">
			<bbNG:breadcrumb>MediaMosa Building Block</bbNG:breadcrumb>
		</bbNG:breadcrumbBar>
		<bbNG:pageTitleBar>MediaMosa: edit media</bbNG:pageTitleBar>
	</bbNG:pageHeader>
	<form:form method="POST" commandName="asset">
	<bbNG:dataCollection>
		<bbNG:step title="Details" hideNumber="false">
			<bbNG:dataElement label="Title" isRequired="true">
				<input type="text" name="title" id="title" size="50" value="${asset.title}"/>
			</bbNG:dataElement><br/>
    		<bbNG:dataElement label="Description" isRequired="false">
				<textarea name="description" id="description" rows="4" cols="48"><c:out value="${asset.description}"/></textarea>
			</bbNG:dataElement><br/>
		</bbNG:step>
		<input type="hidden" name="owner" id="owner" value="<c:out value="${asset.owner}"/>" />
		<bbNG:stepSubmit hideNumber="false"/>
	</bbNG:dataCollection>
	</form:form>

</bbNG:learningSystemPage>
