package no.hvl.dat100ptc.oppgave4;

import no.hvl.dat100ptc.TODO;
import no.hvl.dat100ptc.oppgave1.GPSPoint;
import no.hvl.dat100ptc.oppgave2.GPSData;
import no.hvl.dat100ptc.oppgave2.GPSDataConverter;
import no.hvl.dat100ptc.oppgave2.GPSDataFileReader;
import no.hvl.dat100ptc.oppgave3.GPSUtils;

public class GPSComputer {

	private GPSPoint[] gpspoints;

	public GPSComputer(String filename) {

		GPSData gpsdata = GPSDataFileReader.readGPSFile(filename);
		gpspoints = gpsdata.getGPSPoints();

	}

	public GPSComputer(GPSPoint[] gpspoints) {
		this.gpspoints = gpspoints;
	}

	public GPSPoint[] getGPSPoints() {
		return this.gpspoints;
	}

	// beregn total distances (i meter)
	public double totalDistanceX(int j) { 

		double distance = 0;

		for (int i = 1; i < (j); i++) { 
			distance = distance + GPSUtils.distance(gpspoints[i - 1], gpspoints[i]);
		}

		return distance;

	}
	
	public double totalDistance() { 

		double distance = 0;

		for (int i = 1; i < (gpspoints.length); i++) { 
			distance = distance + GPSUtils.distance(gpspoints[i - 1], gpspoints[i]);
		}

		return distance;

	}

	// beregn totale høydemeter (i meter)
	public double totalElevationX(int j) { 

		double elevation = 0;

		for (int i = 1; i < j; i++) { 
			if (gpspoints[i].getElevation() > gpspoints[i - 1].getElevation()) {
				elevation = elevation + (gpspoints[i].getElevation() - gpspoints[i - 1].getElevation());
			}
		}

		return elevation;

	}
	
	public double totalElevation() { 

		double elevation = 0;

		for (int i = 1; i < gpspoints.length; i++) { 
			if (gpspoints[i].getElevation() > gpspoints[i - 1].getElevation()) {
				elevation = elevation + (gpspoints[i].getElevation() - gpspoints[i - 1].getElevation());
			}
		}

		return elevation;

	}

	// beregn total tiden for hele turen (i sekunder)
	public int totalTimeX(int j) { 

		int totalTime = 0;

		int startTime = gpspoints[0].getTime();

		int endTime = gpspoints[j].getTime(); 

		totalTime = endTime - startTime;

		return totalTime;

	}
	
	public int totalTime() { 

		int totalTime = 0;

		int startTime = gpspoints[0].getTime();

		int endTime = gpspoints[gpspoints.length-1].getTime(); 

		totalTime = endTime - startTime;

		return totalTime;

	}

	// beregn gjennomsnitshastighets mellom hver av gps punktene

	public double[] speedsX(int j) { 

		double[] avgSpeeds = new double[j]; 

		for (int i = 1; i < avgSpeeds.length; i++) {
			avgSpeeds[i] = GPSUtils.speed(gpspoints[i - 1], gpspoints[i]);
		}

		return avgSpeeds;

	}
	
	public double[] speeds() { 

		double[] avgSpeeds = new double[gpspoints.length-1]; 

		for (int i = 1; i < gpspoints.length; i++) {
			avgSpeeds[i-1] = GPSUtils.speed(gpspoints[i - 1], gpspoints[i]);
		}

		return avgSpeeds;

	}

	public double maxSpeedX(int j) { 

		return GPSUtils.findMax(speedsX(j)); 

	}
	
	public double maxSpeed() { 

		return GPSUtils.findMax(speeds()); 

	}

	public double averageSpeedX(int j) { 

		double averageSpeed;

		averageSpeed = (totalDistanceX(j) / totalTimeX(j)) * 3.6; 

		return averageSpeed;

	}
	
	public double averageSpeed() { 

		double averageSpeed;

		averageSpeed = (totalDistance() / totalTime()) * 3.6; 

		return averageSpeed;

	}

	public double speedX(int j) {

		double speed = 0;

		if (j > 0) {
			speed = GPSUtils.speed(gpspoints[j - 1], gpspoints[j]);
			return speed;
		}

		return speed;
	}

	/*
	 * bicycling, <10 mph, leisure, to work or for pleasure 4.0 bicycling, general
	 * 8.0 bicycling, 10-11.9 mph, leisure, slow, light effort 6.0 bicycling,
	 * 12-13.9 mph, leisure, moderate effort 8.0 bicycling, 14-15.9 mph, racing or
	 * leisure, fast, vigorous effort 10.0 bicycling, 16-19 mph, racing/not drafting
	 * or >19 mph drafting, very fast, racing general 12.0 bicycling, >20 mph,
	 * racing, not drafting 16.0
	 */

	// conversion factor m/s to miles per hour
	public static double MS = 2.236936;

	// beregn kcal gitt weight og tid der kjøres med en gitt hastighet
	public double kcalX(double weight, int secs, double speed) {

		double kcal = 0;

		// MET: Metabolic equivalent of task angir (kcal x kg-1 x (tid i timer)-1)
		double met = 0;
		double speedmph = speed * MS;
		double h = 3600.0;

		if (speedmph < 10) {
			met = 4.0;
		} else if (speedmph > 10 && speedmph <= 12) {
			met = 6.0;
		} else if (speedmph > 12 && speedmph <= 14) {
			met = 8.0;
		} else if (speedmph > 14 && speedmph <= 16) {
			met = 10.0;
		} else if (speedmph > 16 && speedmph <= 20) {
			met = 12.0;
		} else if (speedmph > 20) {
			met = 16.0;
		}

		kcal = met * weight * (secs / h);

		return kcal;

	}
	
	public double kcal(double weight, int secs, double speed) {

		double kcal = 0;

		// MET: Metabolic equivalent of task angir (kcal x kg-1 x (tid i timer)-1)
		double met = 0;
		double speedmph = speed * MS;
		double h = 3600.0;

		if (speedmph < 10) {
			met = 4.0;
		} else if (speedmph > 10 && speedmph <= 12) {
			met = 6.0;
		} else if (speedmph > 12 && speedmph <= 14) {
			met = 8.0;
		} else if (speedmph > 14 && speedmph <= 16) {
			met = 10.0;
		} else if (speedmph > 16 && speedmph <= 20) {
			met = 12.0;
		} else if (speedmph > 20) {
			met = 16.0;
		}

		kcal = met * weight * (secs / h);

		return kcal;

	}

	public double totalKcalX(double weight, int j) {

		double totalkcal = 0;

		double[] totalSpeeds = speedsX(j);

		for (int i = 1; i < j; i++) { 
			totalkcal = totalkcal + kcalX(weight, gpspoints[i].getTime() - gpspoints[i - 1].getTime(), totalSpeeds[i]); //
		}

		return totalkcal;

	}
	
	public double totalKcal(double weight) {

		double totalkcal = 0;

		double[] totalSpeeds = speeds();

		for (int i = 1; i < gpspoints.length-1; i++) { 
			totalkcal = totalkcal + kcal(weight, gpspoints[i].getTime() - gpspoints[i - 1].getTime(), totalSpeeds[i]); //
		}

		return totalkcal;

	}

	public double[] climbsX(int j) { 

		double[] climbs = new double[j]; 

		for (int i = 1; i < climbs.length; i++) {
			climbs[i] = 100 * ((gpspoints[i].getElevation() - gpspoints[i - 1].getElevation())
					/ (GPSUtils.distance(gpspoints[i - 1], gpspoints[i])));
		}
		return climbs;
	}

	public double maxClimbX(int j) { 

		return GPSUtils.findMax(climbsX(j)); 

	}

	public double climbX(int j) {

		double climb = 0;

		if (j > 0) {
			climb = 100 * ((gpspoints[j].getElevation() - gpspoints[j - 1].getElevation())
					/ (GPSUtils.distance(gpspoints[j - 1], gpspoints[j])));
			return climb;
		}

		return climb;

	}

	private static double WEIGHT = 80.0;

	public String[] displayStatisticsX(int j) {

		String linje = String.format("%s %n", "================================");

		String totalTime = String.format("%s %5s %10s %n", "Total time", ":", GPSUtils.formatTime(totalTimeX(j)));

		String speed = String.format("%s %10s %7s %2s %n", "Speed", ":", GPSUtils.formatDouble(speedX(j)), "km/t");

		String climb = String.format("%s %10s %7s %1s %n", "Climb", ":", GPSUtils.formatDouble(climbX(j)), "%");

		String totalDistance = String.format("%s %1s %10s %2s %n", "Total distance", ":",
				GPSUtils.formatDouble(totalDistanceX(j) / 1000), "km");

		String totalElevation = String.format("%s%s %10s %1s %n", "Total elevation", ":",
				GPSUtils.formatDouble(totalElevationX(j)), "m");

		String maxSpeed = String.format("%s %6s %10s %4s %n", "Max speed", ":", GPSUtils.formatDouble(maxSpeedX(j)),
				"km/t");

		String averageSpeed = String.format("%s %2s %10s %4s %n", "Average speed", ":",
				GPSUtils.formatDouble(averageSpeedX(j)), "km/t");

		String energy = String.format("%s %9s %10s %4s %n", "Energy", ":", GPSUtils.formatDouble(totalKcalX(WEIGHT, j)),
				"kcal");

		String maxClimb = String.format("%s %6s %10s %1s %n", "Max climb", ":", GPSUtils.formatDouble(maxClimbX(j)),
				"%");

		String statestikk = linje + totalTime + totalDistance + climb + totalElevation + maxSpeed + averageSpeed
				+ energy + maxClimb + linje;

		String[] Statistics = new String[] { linje, totalTime, speed, climb, totalDistance, totalElevation, maxSpeed,
				averageSpeed, energy, maxClimb, linje };

		return Statistics;

	}
	
	public void displayStatistics() {

		String linje = String.format("%s %n", "================================");

		String totalTime = String.format("%s %5s %10s %n", "Total time", ":", GPSUtils.formatTime(totalTime()));

		String totalDistance = String.format("%s %1s %10s %2s %n", "Total distance", ":",
				GPSUtils.formatDouble(totalDistance() / 1000), "km");

		String totalElevation = String.format("%s%s %10s %1s %n", "Total elevation", ":",
				GPSUtils.formatDouble(totalElevation()), "m");

		String maxSpeed = String.format("%s %6s %10s %4s %n", "Max speed", ":", GPSUtils.formatDouble(maxSpeed()),
				"km/t");

		String averageSpeed = String.format("%s %2s %10s %4s %n", "Average speed", ":",
				GPSUtils.formatDouble(averageSpeed()), "km/t");

		String energy = String.format("%s %9s %10s %4s %n", "Energy", ":", GPSUtils.formatDouble(totalKcal(WEIGHT)),
				"kcal");

		String statestikk = linje + totalTime + totalDistance + totalElevation + maxSpeed + averageSpeed
				+ energy + linje;

		System.out.println(statestikk);

	}

}
