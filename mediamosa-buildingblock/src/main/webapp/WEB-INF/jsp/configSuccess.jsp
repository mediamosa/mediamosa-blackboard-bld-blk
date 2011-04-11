<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/bbNG" prefix="bbNG"%>
<bbNG:genericPage>

	<bbNG:receipt type="SUCCESS" recallUrl="/webapps/blackboard/admin/manage_plugins.jsp">
	Properties successfully saved.<br/>
	[<c:out value="${config.server}"/>]
	</bbNG:receipt>


</bbNG:genericPage>
