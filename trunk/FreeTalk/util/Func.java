package util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Func {
	/**
	 * @return - Current date and time in dd/MM/yyyy HH:mm:ss format
	 * @author Arthur Kiyanovsky
	 * Nov 30, 2007
	 */
	public static String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }
}
