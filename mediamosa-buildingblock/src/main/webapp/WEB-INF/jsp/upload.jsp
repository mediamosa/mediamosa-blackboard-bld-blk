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
		<bbNG:pageTitleBar>MediaMosa: upload media</bbNG:pageTitleBar>
		<bbNG:cssFile href="css/swfupload.css"/>
		<bbNG:jsFile href="javascript/swfupload/swfupload.js"/>
		<bbNG:jsFile href="javascript/swfupload/swfupload.swfobject.js"/>
		<bbNG:jsFile href="javascript/fileprogress.js"/>
		<bbNG:jsFile href="javascript/handlers.js"/>
		<bbNG:jsBlock>
		<script type="text/javascript">
			var swfu;

			document.observe("dom:loaded", function() {

				swfu = new SWFUpload({
					// Backend settings
					upload_url: "<c:out value="${uploadUrl}"/>",
					file_post_name: "file",
					post_params: {
					  "create_still" : "true"
					},
					http_success : [200, 201, 202],

					// Flash file settings
					file_size_limit : "1000 MB",
					file_types : "<c:out value="${filetypes}"/>",
					file_types_description : "Video Files",
					file_upload_limit : "0",
					file_queue_limit : "1",

					// Event handler settings
					swfupload_loaded_handler : swfUploadLoaded,
					
					file_dialog_start_handler: fileDialogStart,
					file_queued_handler : fileQueued,
					file_queue_error_handler : fileQueueError,
					file_dialog_complete_handler : fileDialogComplete,
					
					upload_progress_handler : uploadProgress,
					upload_error_handler : uploadError,
					upload_success_handler : uploadSuccess,
					upload_complete_handler : uploadComplete,

					// Button Settings
					button_image_url: "<c:out value="${imageUrl}"/>",
					button_placeholder_id : "spanButtonPlaceholder",
					button_width: 61,
					button_height: 22,
					
					// Flash Settings
					flash_url : "<c:out value="${flashUrl}"/>",

					custom_settings : {
						progress_target : "fsUploadProgress",
						upload_successful : false
					},
					
					// Debug settings
					debug: false
				});
					
			});
		</script>
		</bbNG:jsBlock>
	</bbNG:pageHeader>
	<form:form method="POST" commandName="upload">
	<bbNG:dataCollection>
		<bbNG:step title="Select video" hideNumber="false">
     		<bbNG:dataElement label="Source" isRequired="true">
        		<input type="text" name="file" id="file" /><span id="spanButtonPlaceholder"></span>
        		<div class="flash" id="fsUploadProgress"></div>
        	</bbNG:dataElement>
		</bbNG:step>
		<bbNG:step title="Details" hideNumber="false">
			<bbNG:dataElement label="Title" isRequired="true">
				<input type="text" name="title" id="title" size="50" value="" />
			</bbNG:dataElement><br/>
    		<bbNG:dataElement label="Description" isRequired="false">
				<textarea name="description" id="description" rows="4" cols="48"></textarea>
			</bbNG:dataElement><br/>
      		<%-- <input type="hidden" name="mediafileid" id="mediafileid" value="<c:out value="${upload.mediafileId}"/>" /> --%>
		</bbNG:step>
		<input type="hidden" name="assetid" id="assetid" value="<c:out value="${upload.assetId}"/>" />
		<%-- <input type="hidden" name="owner" id="owner" value="<c:out value="${upload.owner}"/>" /> --%>
		<bbNG:stepSubmit title="Upload" hideNumber="false"/>
	</bbNG:dataCollection>
	</form:form>

</bbNG:learningSystemPage>

