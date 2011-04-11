package nl.uva.ic.vpcore.bb;

import java.util.Calendar;
import java.util.Comparator;

import nl.uva.mediamosa.model.AssetType;

public class AssetTypeComparator implements Comparator<Object>{

    public AssetTypeComparator() {
    }
	
	public int compare(Object o1, Object o2) {
		AssetType asset1 = (AssetType) o1;
		AssetType asset2 = (AssetType) o2;
		Calendar cal1 = asset1.getVideotimestamp();
		Calendar cal2 = asset2.getVideotimestamp();
		if (cal1 != null && cal2 != null) {
			if (cal1.before(cal2)) {
				return -1;
			}
			else if (cal1.equals(cal2)) {
				return 0;
			}
			else {
				return 1;
			}
		} else {
			return 1;
		}
	}

}
