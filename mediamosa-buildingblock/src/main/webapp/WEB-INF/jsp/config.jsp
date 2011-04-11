<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/bbNG" prefix="bbNG"%>
<bbNG:genericPage>
	<bbNG:pageHeader>
		<bbNG:breadcrumbBar environment="SYS_ADMIN">
			<bbNG:breadcrumb>Configure Properties</bbNG:breadcrumb>
		</bbNG:breadcrumbBar>
		<bbNG:pageTitleBar>MediaMosa Configuration</bbNG:pageTitleBar>
	</bbNG:pageHeader>
	<form method="POST">
	<bbNG:dataCollection>
		<bbNG:step title="MediaMosa parameters" hideNumber="false">
			<bbNG:dataElement label="Server" isRequired="true"><input type="text" name="server" value="${config.server}" size="50" /></bbNG:dataElement>
			<bbNG:dataElement label="Username" isRequired="true"><input type="text" name="username" value="${config.username}" size="50" /></bbNG:dataElement>
			<bbNG:dataElement label="Password" isRequired="true"><input type="password" name="password" value="${config.password}" size="50" /></bbNG:dataElement>
		</bbNG:step>
		<bbNG:step title="Mediaplayer dimension" hideNumber="false">
			<bbNG:dataElement label="Maximum width"><input type="text" name="maxwidth" value="${config.maxwidth}" size="50" /></bbNG:dataElement>
		</bbNG:step>
		<bbNG:step title="Allowed filetypes for uploading" hideNumber="false">
			<bbNG:dataElement label="Extensions"><input type="text" name="filetypes" value="${config.filetypes}" size="50" /></bbNG:dataElement>
		</bbNG:step>
		<bbNG:stepSubmit hideNumber="false" />
	</bbNG:dataCollection>
	</form>
</bbNG:genericPage>
