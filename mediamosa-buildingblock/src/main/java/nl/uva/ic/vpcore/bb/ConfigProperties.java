package nl.uva.ic.vpcore.bb;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import nl.uva.mediamosa.bb.web.bean.ConfigBean;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;

public class ConfigProperties {
	private File configFile;

	public ConfigProperties(File configFile) {
		if (configFile == null) {throw new IllegalArgumentException("configFile is a mandatory argument");}
		if (!configFile.exists()) {throw new IllegalArgumentException("configFile '"+configFile.getAbsolutePath()+"' doest not exist");}
		this.configFile = configFile;
	}

	public File getConfigFile() {
		return configFile;
	}
	
	public boolean isConfigured() {
		return configFile != null && configFile.exists();
	}

	public ConfigPropertiesBean load(FileChangedReloadingStrategy reloader) throws ConfigurationException {
		final PropertiesConfiguration propertiesConfiguration = new PropertiesConfiguration(configFile);
		propertiesConfiguration.setReloadingStrategy(reloader);
		return new ConfigPropertiesBean(propertiesConfiguration);
	}
	
	public Properties loadProperties() throws IOException {
		final Properties props = new Properties();
		final FileInputStream fis = new FileInputStream(configFile);
		try {
			props.load(fis);
		} finally {
			fis.close();
		}
		return props;
	}

	public void saveProperties(final Properties props) throws FileNotFoundException, IOException {
		final FileOutputStream fos = new FileOutputStream(configFile);
		try {
			props.store(fos, null);
		} finally {
			fos.close();
		}
	}

	public void saveProperties(ConfigBean config) throws IOException {
		final Properties props = new Properties();
		
		props.setProperty("mediamosa.server", config.getServer());
		props.setProperty("mediamosa.username", config.getUsername());
		props.setProperty("mediamosa.password", config.getPassword());
		props.setProperty("mediamosa.filetypes", config.getFiletypes());
		props.setProperty("mediamosa.maxwidth", config.getMaxwidth());
		
		saveProperties(props);
	}
	
	public void saveProps(HttpServletRequest request) throws IOException {
		final Properties props = new Properties();

		for (int i = 0; i < Config.ALL_PROPERTIES.length; i++) {
			String prop = Config.ALL_PROPERTIES[i];
			props.put(prop, request.getParameter(prop));
		}

		saveProperties(props);
	}

	public void setConfigFile(final File configFile) {
		this.configFile = configFile;
	}

}
