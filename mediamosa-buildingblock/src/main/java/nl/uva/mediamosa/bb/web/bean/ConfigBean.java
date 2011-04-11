package nl.uva.mediamosa.bb.web.bean;

public class ConfigBean {	
	private String server;
	private String username;
	private String password;
	private String maxwidth;
	private String filetypes;
	
	public String getServer() {
		return server;
	}
	public void setServer(String server) {
		this.server = server;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getMaxwidth() {
		return maxwidth;
	}
	public void setMaxwidth(String maxwidth) {
		this.maxwidth = maxwidth;
	}
	public String getFiletypes() {
		return filetypes;
	}
	public void setFiletypes(String filetypes) {
		this.filetypes = filetypes;
	}
	
}
