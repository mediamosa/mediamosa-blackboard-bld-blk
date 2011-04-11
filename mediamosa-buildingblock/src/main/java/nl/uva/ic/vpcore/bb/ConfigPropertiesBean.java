package nl.uva.ic.vpcore.bb;

import org.apache.commons.configuration.Configuration;

public class ConfigPropertiesBean {
	private final Configuration configuration;

	public ConfigPropertiesBean(Configuration configuration) {
		this.configuration = configuration;
	}

	public String getPassword() {
		return configuration.getString(Config.PASSWORD, "mediamosa.password");
	}

	public void setPassword(String password) {
		configuration.setProperty(Config.PASSWORD, password);
	}

	public String getServer() {
		return configuration.getString(Config.SERVER, "http://your.mediamosa.server");
	}

	public void setServer(String server) {
		configuration.setProperty(Config.SERVER, server);
	}

	public String getUsername() {
		return configuration.getString(Config.USERNAME, "mediamosa.username");
	}

	public void setUsername(String username) {
		configuration.setProperty(Config.USERNAME, username);
	}
	
	public String getMaxwidth() {
		return configuration.getString(Config.MAXWIDTH, "600");
	}
	
	public void setMaxwidth(String maxwidth) {
		configuration.setProperty(Config.MAXWIDTH, maxwidth);
	}
	
	public String getFiletypes() {
		return configuration.getString(Config.FILETYPES, "*.flv;*.f4v;*.mp4;*.mov;*.wmv;*.asf;*.mp3;");
	}

	public void setFiletypes(String filetypes) {
		configuration.setProperty(Config.FILETYPES, filetypes);
	}

}
