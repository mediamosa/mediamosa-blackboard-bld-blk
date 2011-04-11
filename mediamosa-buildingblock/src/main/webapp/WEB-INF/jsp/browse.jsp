<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/bbNG" prefix="bbNG"%>
<bbNG:learningSystemPage authentication="Y" ctxId="ctx">
	<bbNG:pageHeader>
		<bbNG:breadcrumbBar environment="CTRL_PANEL">
			<bbNG:breadcrumb>MediaMosa Building Block</bbNG:breadcrumb>
		</bbNG:breadcrumbBar>
		<bbNG:pageTitleBar>MediaMosa: <%= String.format("%s %s", ctx.getUser().getGivenName(), ctx.getUser().getFamilyName()) %> media</bbNG:pageTitleBar>
		<bbNG:cssBlock>
			<style type="text/css">
			  	img.still {
  					background:url("images/nopreview.png");
  					width:176px;
  					height:142px;
  				}
			</style>		
		</bbNG:cssBlock>
	</bbNG:pageHeader>
	
	<bbNG:actionControlBar>
		<bbNG:actionButton url="upload.htm?content_id=${param.content_id}&course_id=${param.course_id}" title="Upload media" primary="true"/>
		<bbNG:actionButton url="search.htm?content_id=${param.content_id}&course_id=${param.course_id}" title="Search media" primary="true"/>
	</bbNG:actionControlBar>
	
	<bbNG:inventoryList className="nl.uva.mediamosa.model.AssetType" collection="${assets}" objectVar="asset" displayPagingControls="false" initialSortCol="Date" initialSortBy="DESCENDING" url="browse.htm" >
		<bbNG:listElement isRowHeader="true" name="Title" label="Title">
		<c:out value="${asset.dublinCore.title}"/>
			<bbNG:listContextMenu dynamic="true" menuGeneratorURL="browseMenuItems.htm" 
			contextParameters="assetId=${asset.assetId}&assetOwner=${asset.ownerId}&contentId=${param.content_id}&courseId=${param.course_id}" />
		</bbNG:listElement>
		<bbNG:listElement name="Still" label="Still">
		<a href="play.htm?id=${asset.assetId}" >
    	<img src='${asset.mediafileContainerType == "mp3" ? "images/audio.png" : asset.vpxStillUrl}' width="176" class="still"/>
    	</a>
		</bbNG:listElement>
		<bbNG:listElement name="Date" label="Date" comparator="<%= new nl.uva.ic.vpcore.bb.AssetTypeComparator() %>" >
		<fmt:formatDate value="${asset.videotimestamp.time}" pattern="dd MMM yyyy @ HH:mm" />
		</bbNG:listElement>
		<bbNG:listElement name="Id" label="Id">
		<c:out value="${asset.assetId}" />
		</bbNG:listElement>				
	</bbNG:inventoryList>
	
</bbNG:learningSystemPage>