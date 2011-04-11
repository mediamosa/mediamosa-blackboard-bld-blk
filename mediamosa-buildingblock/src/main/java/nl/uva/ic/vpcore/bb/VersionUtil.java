package nl.uva.ic.vpcore.bb;

import blackboard.persist.PersistenceException;
import blackboard.platform.LicenseUtil;
import blackboard.platform.log.LogService;
import blackboard.platform.log.LogServiceFactory;

public class VersionUtil {
	private static final Version version;
	private static final LogService logger = LogServiceFactory.getInstance();

	static {
		Version tmp = null;
		try {
			String bbBuildNr = LicenseUtil.getBuildNumber();
			tmp = new Version(LicenseUtil.getMajorReleaseNumber(bbBuildNr)
					,LicenseUtil.getAppPackNumber(bbBuildNr)); 
			logger.logInfo("MediaMosa building block on Blackboard version " + tmp);
		} catch (PersistenceException e) {
			logger.logError("Could not determine Blackboard version number");
			tmp = new Version();
		}
		version = tmp;
	}

	/**
	 * @return true if we're running Blackboard version 7 (any application pack)
	 * @throws IllegalStateException when Blackboard version was not detected
	 */
	public static boolean isVersion7x() {
		return version.isVersion7x();
	}

	/**
	 * @return true if we're running Blackboard version 8 (any application pack)
	 * @throws IllegalStateException when Blackboard version was not detected
	 */
	public static boolean isVersion8x() {
		return version.isVersion8x();
	}

	
	/**
	 * @return true if we're running Blackboard version 9 (any application pack)
	 * @throws IllegalStateException when Blackboard version was not detected
	 */
	public static boolean isVersion9x() {
		return version.isVersion9x();
	}
	private static class Version {
		private final int major;
		private final int minor;
		
		public Version(String majorReleaseNumber, String appPackNumber) {
			major = Integer.valueOf(majorReleaseNumber);
			minor= Integer.valueOf(appPackNumber);
		}
		public Version() {
			major = -1;
			minor = -1;
		}
		private void valid() {
			if (major  < 0) {
				throw new IllegalStateException("Blackboard version not detected");
			}
		}
		public boolean isVersion7x () {
			valid();
			return major == 7;
		}
		public boolean isVersion8x () {
			valid();
			return major == 8;
		}
		public boolean isVersion9x () {
			valid();
			return major == 9;
		}
		@Override
		public String toString() {
			return major + "." + minor;
		}
	}
}
