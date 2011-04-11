package nl.uva.mediamosa.bb.web.bean;

import nl.uva.mediamosa.model.AssetDetailsType;
import nl.uva.mediamosa.model.LinkType;
import nl.uva.mediamosa.model.MediafileDetailsType;

public class MediaBean {
	private AssetDetailsType assetDetails;
	private LinkType link;
	private MediafileDetailsType mediafileDetails;
	private String ownerFullname;
	private String player;
	
	public AssetDetailsType getAssetDetails() {
		return assetDetails;
	}
	public LinkType getLink() {
		return link;
	}
	public MediafileDetailsType getMediafileDetails() {
		return mediafileDetails;
	}
	public String getOwnerFullname() {
		return ownerFullname;
	}
	public String getPlayer() {
		return player;
	}
	public void setAssetDetails(AssetDetailsType assetDetails) {
		this.assetDetails = assetDetails;
	}
	public void setLink(LinkType link) {
		this.link = link;
	}
	public void setMediafileDetails(MediafileDetailsType mediafileDetails) {
		this.mediafileDetails = mediafileDetails;
	}
	public void setOwnerFullname(String ownerFullname) {
		this.ownerFullname = ownerFullname;
	}
	public void setPlayer(String player) {
		this.player = player;
	}
}
