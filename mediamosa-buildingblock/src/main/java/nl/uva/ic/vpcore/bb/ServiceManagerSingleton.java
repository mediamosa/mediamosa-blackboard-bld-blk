package nl.uva.ic.vpcore.bb;

import java.io.File;

import java.io.IOException;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;

import blackboard.platform.log.LogService;
import blackboard.platform.log.LogServiceFactory;
import blackboard.platform.plugin.PlugInException;
import blackboard.platform.plugin.PlugInUtil;
import javax.servlet.http.HttpServletRequest;
import nl.uva.mediamosa.MediaMosaService;
import nl.uva.mediamosa.util.ServiceException;

/**
 *
 * @author Gilgamesh Nootebos <G.Nootebos@uva.nl>
 * @author Tom Kuipers <T.F.Kuipers@uva.nl>
 * @todo redesign together with ConfigProperties and ConfigPropertiesBean
 */
public class ServiceManagerSingleton {

    private static final class ServiceManagerSingletonHolder {

        @SuppressWarnings("PMD.AccessorClassGeneration")
        private static final ServiceManagerSingleton INSTANCE = new ServiceManagerSingleton();
    }

    private ServiceManagerSingleton() {
    }

    public static ServiceManagerSingleton getInstance() {
        return ServiceManagerSingletonHolder.INSTANCE;
    }

    private final class Reloader extends FileChangedReloadingStrategy {

        @Override
        public void reloadingPerformed() {
                try {
                    service = new MediaMosaService(getCb().getServer());
                    service.setCredentials(getCb().getUsername(), getCb().getPassword());
                } catch (ServiceException e1) {
                    logger.logError(e1.getMessage(), e1);
                }

                try {
                    maxwidth = Integer.valueOf(getCb().getMaxwidth());
                } catch (NumberFormatException e) {
                    // logging, empty catch!!!
                    logger.logError(e.getMessage(), e);
                }
        }
    }
    //private static final LogService logger = BbServiceManager.getLogService();
    private static final LogService logger = LogServiceFactory.getInstance();
    private MediaMosaService service;
    private int maxwidth = 600;
    private ConfigPropertiesBean cb;


    public ConfigPropertiesBean getCb() {
            return cb;
    }

    public MediaMosaService getMediaMosaService() throws ServiceException {
        init();
            getCb().getServer();
            return service;
    }

    public int getMaxwidth() throws ServiceException {
        init();
            getCb().getMaxwidth();
            return maxwidth;
    }

    public String getFiletypes() throws ServiceException {
        init();
            return getCb().getFiletypes();
    }

    public void init() throws ServiceException {
        if (getCb() != null) {
            return;
        }
        try {
            File _configFile = new File(PlugInUtil.getConfigDirectory("UVA", "BB-MEDIAMOSA"), "config.properties");
            _configFile.createNewFile();
            ConfigProperties cp = new ConfigProperties(_configFile);
            if (cp.isConfigured()) {
                try {
                    final Reloader reloader = new Reloader();
                    cb = cp.load(reloader);
                    reloader.reloadingPerformed();
                } catch (ConfigurationException e) {
                    throw new ServiceException("ConfigPropertiesBean is not loaded, did administrator configure the plugin from manage building blocks?", e);
                }
            }
            if (cb == null) {
                throw new ServiceException("ConfigPropertiesBean is not loaded, did administrator configure the plugin from manage building blocks?");
            }
        } catch (PlugInException e) {
            throw new ServiceException(e);
        } catch (IOException e) {
            throw new ServiceException(e);
		}
    }

    public void save(HttpServletRequest request) throws IOException, PlugInException {
        File configFile = new File(PlugInUtil.getConfigDirectory("UVA", "BB-MEDIAMOSA"), "config.properties");
        // create file if it does not exist
        boolean success = configFile.createNewFile();
        if (success) {
        	logger.logInfo("MediaMosa config file did not exist and was created.");
        } else {
        	logger.logInfo("MediaMosa config file already exists.");
        }
        ConfigProperties cp = new ConfigProperties(configFile);
        cp.saveProps(request);
    }
}
