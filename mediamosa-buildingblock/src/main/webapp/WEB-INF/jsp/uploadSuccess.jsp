<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/bbNG" prefix="bbNG"%>
<bbNG:learningSystemPage authentication="Y">

	<bbNG:receipt type="SUCCESS" recallUrl="browse.htm?content_id=${param.content_id}&course_id=${param.course_id}">
	File successfully uploaded.<br/>
	Title: <c:out value="${upload.title}"/><br/>
	Assetid: <c:out value="${upload.assetId}"/><br/>
	Owner: <c:out value="${upload.owner}"/>
	</bbNG:receipt>

</bbNG:learningSystemPage>
