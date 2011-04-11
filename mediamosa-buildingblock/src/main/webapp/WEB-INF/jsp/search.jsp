<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="/bbNG" prefix="bbNG"%>
<bbNG:learningSystemPage authentication="Y">
	<bbNG:pageHeader>
		<bbNG:breadcrumbBar environment="CTRL_PANEL">
			<bbNG:breadcrumb>MediaMosa Building Block</bbNG:breadcrumb>
		</bbNG:breadcrumbBar>
		<bbNG:pageTitleBar>MediaMosa: search media</bbNG:pageTitleBar>
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
	<form:form method="POST" commandName="search">
	<bbNG:dataCollection>
		<bbNG:step title="Keyword" hideNumber="false">
     		<bbNG:dataElement label="Title" isRequired="true">
        		<input type="text" name="searchTerm" size="50" />
        	</bbNG:dataElement>
		</bbNG:step>
		<bbNG:stepSubmit title="Search" hideNumber="false" cancelUrl="browse.htm"/>
	</bbNG:dataCollection>	
	</form:form>

	<bbNG:inventoryList className="nl.uva.mediamosa.model.AssetType" collection="${assets}" objectVar="asset" displayPagingControls="false">
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
		<bbNG:listElement name="Id" label="Id">
		<c:out value="${asset.assetId}" />
		</bbNG:listElement>
	</bbNG:inventoryList>
</bbNG:learningSystemPage>
