<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/bbNG" prefix="bbNG"%>
<bbNG:learningSystemPage authentication="Y">
	<bbNG:receipt type="SUCCESS" recallUrl="${recallUrl}" >
	Course Item successfully created.<br/>
	Title: <c:out value="${content.title}"/><br/>
	</bbNG:receipt>
</bbNG:learningSystemPage>