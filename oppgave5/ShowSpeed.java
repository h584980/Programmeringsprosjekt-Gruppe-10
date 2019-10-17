package no.hvl.dat100ptc.oppgave5;

import javax.swing.JOptionPane;

import easygraphics.EasyGraphics;
import no.hvl.dat100ptc.TODO;
import no.hvl.dat100ptc.oppgave1.GPSPoint;
import no.hvl.dat100ptc.oppgave2.GPSData;
import no.hvl.dat100ptc.oppgave2.GPSDataFileReader;
import no.hvl.dat100ptc.oppgave3.GPSUtils;
import no.hvl.dat100ptc.oppgave4.GPSComputer;

public class ShowSpeed extends EasyGraphics {
			
	private static int MARGIN = 50;

	private GPSComputer gpscomputer;
	private GPSPoint[] gpspoints;
	
	public ShowSpeed() {

		String filename = JOptionPane.showInputDialog("GPS data filnavn: ");
		gpscomputer = new GPSComputer(filename);

		gpspoints = gpscomputer.getGPSPoints();
		
	}
	
	// read in the files and draw into using EasyGraphics
	public static void main(String[] args) {
		launch(args);
	}

	public void run() {

		int N = gpspoints.length-1; // number of data points
		
		double[] fart = new double[gpspoints.length];
		
		for(int i = 0; i < gpspoints.length-1; i++) {
			fart[i] = GPSUtils.speed(gpspoints[i], gpspoints[i+1]);
		}
		
		int maxFart = (int)GPSUtils.findMax(fart);
		
		makeWindow("Speed profile", 2*MARGIN + 2 * N, 2*MARGIN + maxFart);
		
		showSpeedProfile(MARGIN + maxFart, N);
	}
	
	public void showSpeedProfile(int ybase, int N) {
		
		//System.out.println("Angi tidsskalering i tegnevinduet ...");
		//int timescaling = Integer.parseInt(getText("Tidsskalering"));
				
		// TODO - START
		
		for(int i = 0; i < gpspoints.length-1; i++ ) {
			setColor(0, 0, 255);
			int s = (int) Math.round(GPSUtils.speed(gpspoints[i], gpspoints[i+1]));     
			drawLine(i*2+MARGIN, ybase-s, i*2+MARGIN, ybase);
			
			/*
			if(i<(gpspoints.length-1)) {	
				pause(1000/(int)GPSUtils.speed(gpspoints[i], gpspoints[i+1]));
			}
			*/
			
		}

		int avg = (int) Math.round(gpscomputer.averageSpeedX(gpspoints.length-1));
		
		setColor(0, 255, 0);
		drawLine(0+MARGIN, ybase-avg, gpspoints.length*2+MARGIN, ybase-avg);
	
		// TODO - SLUTT
	}
}
