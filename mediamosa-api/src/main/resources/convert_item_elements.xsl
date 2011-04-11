<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output indent="yes" method="xml"/>
	<xsl:template match="@*|node()">
	  <xsl:copy>
		<xsl:apply-templates select="@*|node()"/>
	  </xsl:copy>
	</xsl:template>
	
	<!-- rename 'item' elements  -->
	<xsl:template match="item">
		<!-- convert to link -->
		<xsl:if test="output and content_type">
			<xsl:element name="link">
				<xsl:apply-templates select="@*|node()"/>
			</xsl:element>		
		</xsl:if>	
		<!-- convert to asset -->
		<xsl:if test="asset_id and dublin_core and not(mediafiles)">
			<xsl:element name="asset">
				<xsl:apply-templates select="@*|node()"/>
			</xsl:element>
		</xsl:if>
		<!-- convert to asset_details -->
		<xsl:if test="asset_id and mediafiles">
			<xsl:element name="asset_details">
				<xsl:apply-templates select="@*|node()"/>
			</xsl:element>
		</xsl:if>
		<!-- convert to assetId -->
		<xsl:if test="asset_id and not(dublin_core) and not(filename)">
			<xsl:element name="asset_id">
				<xsl:apply-templates select="@*|node()"/>
			</xsl:element>
		</xsl:if>
		<!-- convert to mediafile -->
		<xsl:if test="mediafile_id and not(filename)">
			<xsl:element name="mediafile">
				<xsl:apply-templates select="@*|node()"/>
			</xsl:element>
		</xsl:if>
		<!-- convert to mediafile_details -->
		<xsl:if test="mediafile_id and filename">
			<xsl:element name="mediafile_details">
				<xsl:apply-templates select="@*|node()"/>
			</xsl:element>
		</xsl:if>
		<!-- convert to profile -->
		<xsl:if test="profile_id and profile">
			<xsl:element name="profile">
				<xsl:apply-templates select="@*|node()"/>
			</xsl:element>
		</xsl:if>
		<!-- convert to preview_profile -->
		<xsl:if test="preview_profile_id">
			<xsl:element name="preview_profile">
				<xsl:apply-templates select="@*|node()"/>
			</xsl:element>
		</xsl:if>
		<!-- convert to job -->
		<xsl:if test="job_id">
			<xsl:element name="job">
				<xsl:apply-templates select="@*|node()"/>
			</xsl:element>			
		</xsl:if>
		<!-- convert to job_details -->
		<xsl:if test="job_type and status">
			<xsl:element name="job_details">
				<xsl:apply-templates select="@*|node()"/>
			</xsl:element>			
		</xsl:if>
		<!-- convert to supplement -->
		<xsl:if test="supplement_id and supplement_base64">
			<xsl:element name="supplement">
				<xsl:apply-templates select="@*|node()"/>
			</xsl:element>			
		</xsl:if>
		<!-- convert to metadata_tag -->
		<xsl:if test="prop_id and prop_group">
			<xsl:element name="metadata_tag">
				<xsl:apply-templates select="@*|node()"/>
			</xsl:element>			
		</xsl:if>
		<!-- convert to collection -->
		<xsl:if test="coll_id and title">
			<xsl:element name="collection">
				<xsl:apply-templates select="@*|node()"/>
			</xsl:element>
		</xsl:if>		
		<!-- convert to user -->
		<xsl:if test="user_id and group_id">
			<xsl:element name="user">
				<xsl:apply-templates select="@*|node()"/>
			</xsl:element>
		</xsl:if>
		<!-- convert to user_details -->
		<xsl:if test="user_quota_mb and user_diskspace_used_mb">
			<xsl:element name="user_details">
				<xsl:apply-templates select="@*|node()"/>
			</xsl:element>		
		</xsl:if>
		<!-- convert to uploadticket -->
		<!-- <xsl:if test="upload_ticket and action"> In MediaMosa 1.7.3.2 element 'upload_ticket' is removed -->
		<xsl:if test="uploadprogress_url and action">
			<xsl:element name="upload_ticket">
				<xsl:apply-templates select="@*|node()"/>
			</xsl:element>
		</xsl:if>
		<!-- convert to group -->
		<xsl:if test="group_id and quotum">
			<xsl:element name="group">
				<xsl:apply-templates select="@*|node()"/>
			</xsl:element>
		</xsl:if>
		<!-- convert to group_details -->
		<xsl:if test="group_quota_mb and not(user_quota_mb)">
			<xsl:element name="group_details">
				<xsl:apply-templates select="@*|node()"/>
			</xsl:element>
		</xsl:if>		
		<!-- convert to errorcode -->
		<xsl:if test="code and name">
			<xsl:element name="errorcode">
				<xsl:apply-templates select="@*|node()"/>
			</xsl:element>
		</xsl:if>
		<!-- convert to statistics -->
		<xsl:if test="requested and mediafile_id">
			<xsl:element name="stats_popularstreams">
				<xsl:apply-templates select="@*|node()"/>
			</xsl:element>
		</xsl:if>
		<xsl:if test="file_size and timestamp">
			<xsl:element name="stats_dataupload">
				<xsl:apply-templates select="@*|node()"/>
			</xsl:element>
		</xsl:if>
		<xsl:if test="type and diskspace_mb">
			<xsl:element name="stats_datausagevideo">
				<xsl:apply-templates select="@*|node()"/>
			</xsl:element>
		</xsl:if>
		<xsl:if test="played and play_type">
			<xsl:element name="stats_playedstreams">
				<xsl:apply-templates select="@*|node()"/>
			</xsl:element>
		</xsl:if>
		<xsl:if test="count and coll_id">
			<xsl:element name="stats_popularcollections">
				<xsl:apply-templates select="@*|node()"/>
			</xsl:element>
		</xsl:if>
		<!-- ToDo /statistics/searchrequest -->
		<!-- ToDo /statistics/newestcollections -->
		<!-- ToDo /statistics/popularmediafiles -->
		<!-- ToDo /statistics/newestmediafiles -->
		<!-- ToDo /mediafile/count -->
		<!-- ToDo ../favorites/.. requests-->
		<!-- ToDo /ftp -->
		<!-- ACL en authorisaties -->
	</xsl:template>
	
	<!-- rename element <mediafile_X/> to <mediafile id="X"/> -->
	<xsl:template match="mediafiles/*">
		<xsl:variable name="mediafile_nr" select="substring(name(), 11)"/>
		<mediafile id="{$mediafile_nr}">
			<xsl:apply-templates select="@*|node()"/>
		</mediafile>
	</xsl:template>
	
</xsl:stylesheet>
