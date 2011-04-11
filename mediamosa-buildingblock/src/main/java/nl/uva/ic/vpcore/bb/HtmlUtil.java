package nl.uva.ic.vpcore.bb;

import java.util.List;

import nl.uva.mediamosa.model.ProfileType;

public class HtmlUtil {

	public static String getSelectBox(List<ProfileType> profiles, String elementId) {
		StringBuilder sb = new StringBuilder();
		String selectbox = "<select name=\"" + elementId + "\" id=\"" + elementId + "\">\n";
		sb.append(selectbox).append("<option value=\"\"/>\n");
		for (ProfileType p : profiles) {
		        sb.append("<option value=\"").append(p.getProfileId()).append("\">").append(p.getProfile()).append("</option>\n");
		}
		sb.append("</select>");
		selectbox = sb.toString();
		return selectbox;
	}
}
