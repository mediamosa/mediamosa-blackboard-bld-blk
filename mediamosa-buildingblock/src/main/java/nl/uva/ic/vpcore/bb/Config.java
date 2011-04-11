package nl.uva.ic.vpcore.bb;

public abstract class Config {
	
	public static final String SERVER = "mediamosa.server";
	public static final String USERNAME = "mediamosa.username";
	public static final String PASSWORD = "mediamosa.password";
	public static final String ABOUT_LINK = "http://mediamosa.org";
	public static final String MAXWIDTH = "mediamosa.maxwidth";
	public static final String FILETYPES = "mediamosa.filetypes";

	static final String[] ALL_PROPERTIES = new String[] { SERVER,
		USERNAME, PASSWORD, MAXWIDTH };
}
