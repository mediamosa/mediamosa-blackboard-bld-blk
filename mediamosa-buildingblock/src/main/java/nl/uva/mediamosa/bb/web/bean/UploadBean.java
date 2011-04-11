package nl.uva.mediamosa.bb.web.bean;

public class UploadBean {
	private String assetId; 
	private String description;
	private String mediafileId;
	private String owner;
	private String title;
	private String filetypes;
	private String uploadurl;
	private String imageurl;
	private String flashurl;

	
	public String getUploadurl() {
		return uploadurl;
	}
	public void setUploadurl(String uploadurl) {
		this.uploadurl = uploadurl;
	}
	public String getImageurl() {
		return imageurl;
	}
	public void setImageurl(String imageurl) {
		this.imageurl = imageurl;
	}
	public String getFlashurl() {
		return flashurl;
	}
	public void setFlashurl(String flashurl) {
		this.flashurl = flashurl;
	}
	public String getFiletypes() {
		return filetypes;
	}
	public void setFiletypes(String filetypes) {
		this.filetypes = filetypes;
	}
	public String getAssetId() {
		return assetId;
	}
	public String getDescription() {
		return description;
	}
	public String getMediafileId() {
		return mediafileId;
	}
	public String getOwner() {
		return owner;
	}
	public String getTitle() {
		return title;
	}
	public void setAssetId(String assetId) {
		this.assetId = assetId;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public void setMediafileId(String mediafileId) {
		this.mediafileId = mediafileId;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public void setTitle(String title) {
		this.title = title;
	}

}
