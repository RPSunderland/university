package zadanie4.czas;

import java.util.Calendar;
import java.util.Date;

/**Przelicza java.util.Date i java.util.Calendar na sekundy dnia*/
public class PrzelicznikCzasu {
    public static int sekundaDnia(Date date){
	Calendar cal = Calendar.getInstance();
	cal.setTime(date);
	return sekundaDnia(cal);
    }
    
    public static int sekundaDnia(Calendar date){
	return 
		date.get(Calendar.HOUR_OF_DAY)*3600+
		date.get(Calendar.MINUTE)*60+
		date.get(Calendar.SECOND);
    }
}
