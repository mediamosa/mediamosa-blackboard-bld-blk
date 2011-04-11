<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/bbNG" prefix="bbNG"%>
<bbNG:learningSystemPage authentication="Y">
	<bbNG:pageHeader>
		<bbNG:breadcrumbBar environment="CTRL_PANEL"><%-- PORTAL, COURSE, SYS_ADMIN  --%>
			<bbNG:breadcrumb>MediaMosa Building Block</bbNG:breadcrumb>
		</bbNG:breadcrumbBar>
		<bbNG:pageTitleBar>MediaMosa: play media</bbNG:pageTitleBar>
		<bbNG:jsFile href="player/swfobject.js"/>
	</bbNG:pageHeader>
	
	<bbNG:actionControlBar>
		<bbNG:actionButton url="browse.htm?content_id=${param.content_id}&course_id=${param.course_id}" title="Browse media" primary="true"/>
	</bbNG:actionControlBar>
	
	<div style="float:left;">
    <br/>
    <c:out value="${media.player}" escapeXml="false" />
	</div>
	<div style="float:left; padding-left:2em;">
	<br/>
	<span class="contentTitle"><c:out value="${media.assetDetails.dublinCore.title}" /></span><br/>
	<c:out value="${media.assetDetails.dublinCore.description}" />
	<p>
	Length: <c:out value="${media.assetDetails.mediafileDuration}" /><br/>
	Added: <fmt:formatDate value="${media.assetDetails.videotimestamp.time}" pattern="dd MMM yyyy @ HH:mm" /><br/>
	Played: <c:out value="${media.assetDetails.played}" /><br/>
	Viewed: <c:out value="${media.assetDetails.viewed}" /><br/>
	Owner: <c:out value="${media.ownerFullname}" /> 
	<!-- Url: ${media.link.output}  -->
	</p>
	</div>
</bbNG:learningSystemPage>
