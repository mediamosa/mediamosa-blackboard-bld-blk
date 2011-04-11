<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/bbNG" prefix="bbNG"%>
<bbNG:learningSystemPage authentication="Y">

	<bbNG:receipt type="SUCCESS" recallUrl="browse.htm">
	Asset successfully edited.<br/>
	title: <c:out value="${asset.title}"/><br/>
	description: <c:out value="${asset.description}"/><br/>
	assetid: <c:out value="${asset.assetId}"/><br/>
	owner: <c:out value="${asset.owner}"/>
	</bbNG:receipt>

</bbNG:learningSystemPage>