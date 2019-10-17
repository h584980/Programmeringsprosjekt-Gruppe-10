package no.hvl.dat100ptc.oppgave3;

import static java.lang.Math.*;

import no.hvl.dat100ptc.TODO;
import no.hvl.dat100ptc.oppgave1.GPSPoint;
import no.hvl.dat100ptc.oppgave2.GPSDataConverter;

public class GPSUtils {

	public static double findMax(double[] da) {

		double max; 
		
		max = da[0];
		
		for (double d : da) {
			if (d > max) {
				max = d;
			}
		}
		
		return max;
	}

	public static double findMin(double[] da) {

		// TODO - START

		double min; 
		
		min = da[0];
		
		for (double d : da) {
			if (d < min) {
				min = d;
			}
		}
		
		return min;

		// TODO - SLUT

	}

	public static double[] getLatitudes(GPSPoint[] gpspoints) {

		// TODO - START
		
		double[] breddegrader = new double[gpspoints.length];
		
		for(int i = 0; i < gpspoints.length; i++) {
			
			breddegrader[i] = gpspoints[i].getLatitude();
		}
		return breddegrader;
		
		// TODO - SLUTT
	}

	public static double[] getLongitudes(GPSPoint[] gpspoints) {

		// TODO - START

		double[] lengdegrader = new double[gpspoints.length];
		
		for(int i = 0; i < gpspoints.length; i++) {
			
			lengdegrader[i] = gpspoints[i].getLongitude();
		}
		return lengdegrader;
		
		// TODO - SLUTT

	}

	private static int R = 6371000; // jordens radius

	public static double distance(GPSPoint gpspoint1, GPSPoint gpspoint2) {

		double d;
		double latitude1, longitude1, latitude2, longitude2;

		// TODO - START

		double radLat1 = toRadians(latitude1 = gpspoint1.getLatitude());	
		double radLong1 = toRadians(longitude1 = gpspoint1.getLongitude());
		
		double radLat2 = toRadians(latitude2 = gpspoint2.getLatitude());	
		double radLong2 = toRadians(longitude2 = gpspoint2.getLongitude());
		
		double deltaLat = radLat2 - radLat1;
		double deltaLong = radLong2 - radLong1;
		
		double a = pow((sin(deltaLat/2)),2) + cos(radLat1)*cos(radLat2)*(pow(sin(deltaLong/2), 2));
		double c = 2*atan2(sqrt(a), sqrt(1-a));
		d = R * c;
				
		return d;

		// TODO - SLUTT

	}

	public static double speed(GPSPoint gpspoint1, GPSPoint gpspoint2) {

		int secs;
		double speed;

		// TODO - START
		
		int time1 = gpspoint1.getTime();
		int time2 = gpspoint2.getTime();
		secs = time2 - time1;
		
		speed = (distance(gpspoint1, gpspoint2)/secs) * 3.6;

		return speed;

		// TODO - SLUTT

	}

	public static String formatTime(int secs) {

		String timestr;
		String TIMESEP = ":";

		// TODO - START

		int restSek = secs % 3600;
		int timer = (secs - restSek) / 3600;
		int sek = restSek % 60;
		int min = (restSek - sek) / 60;
		
		String timerString = String.format("%02d", timer);
		String sekStr = String.format("%02d", sek);
		String minStr = String.format("%02d", min);
		
		timestr = timerString + TIMESEP + minStr + TIMESEP + sekStr;
		
		String finalTimeStr = String.format("%10s", timestr);
		
		return finalTimeStr;
		
		// TODO - SLUTT

	}
	// private static int TEXTWIDTH = 10;

	public static String formatDouble(double d) {

		String str;

		// TODO - START

		double d2 = round(d * 100.0) / 100.0;

		String dString = Double.toString(d2);
	
		str = String.format("%10s", dString);
		
		return str;

		// TODO - SLUTT
		
	}
}
