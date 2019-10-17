package no.hvl.dat100ptc.oppgave5;

import javax.swing.JOptionPane;

import easygraphics.EasyGraphics;
import no.hvl.dat100ptc.TODO;
import no.hvl.dat100ptc.oppgave1.GPSPoint;
import no.hvl.dat100ptc.oppgave3.GPSUtils;
import no.hvl.dat100ptc.oppgave4.GPSComputer;

public class ShowRoute extends EasyGraphics {

	private static int MAPXSIZE = 600;
	private static int MAPYSIZE = 600;
	private static int MARGIN = MAPXSIZE / 16;
	private static int TEXTDISTANCE = 20; // distanse mellom hver linje med statestikk

	private GPSPoint[] gpspoints;
	private GPSComputer gpscomputer;

	public ShowRoute() {

		String filename = JOptionPane.showInputDialog("GPS data filnavn: ");
		gpscomputer = new GPSComputer(filename);

		gpspoints = gpscomputer.getGPSPoints();

	}

	public static void main(String[] args) {
		launch(args);
	}

	public void run() {

		makeWindow("Route", MAPXSIZE + 2 * MARGIN, MAPYSIZE + 2 * MARGIN);

		showRouteMap(MARGIN + MAPYSIZE);

		playRoute(MARGIN + MAPYSIZE);

	}

	// antall x-pixels per lengdegrad
	public double xstep() {

		double maxlon = GPSUtils.findMax(GPSUtils.getLongitudes(gpspoints));
		double minlon = GPSUtils.findMin(GPSUtils.getLongitudes(gpspoints));

		double xstep = (Math.abs(maxlon - minlon)) / MAPXSIZE;

		return xstep;
	}

	// antall y-pixels per breddegrad
	public double ystep() {

		// TODO - START

		double maxlat = GPSUtils.findMax(GPSUtils.getLatitudes(gpspoints));
		double minlat = GPSUtils.findMin(GPSUtils.getLatitudes(gpspoints));

		double ystep = (Math.abs(maxlat - minlat)) / (MAPYSIZE - (TEXTDISTANCE * 10));

		return ystep;

		// TODO - SLUTT

	}

	public void showRouteMap(int ybase) {

		// startpubkt & stoppunkt
		double xstep = xstep();
		double ystep = ystep();

		double minlon = GPSUtils.findMin(GPSUtils.getLongitudes(gpspoints));
		double minlat = GPSUtils.findMin(GPSUtils.getLatitudes(gpspoints));

		// startpunkt markert
		setColor(0, 255, 0);
		fillCircle((int) ((gpspoints[0].getLongitude() - minlon) / xstep) + MARGIN,
				ybase - (int) ((gpspoints[0].getLatitude() - minlat) / ystep), 5);

		// ruten satt opp i blå punkter
		for (int i = 1; i < gpspoints.length - 1; i++) {

			double x = gpspoints[i].getLongitude();
			double y = gpspoints[i].getLatitude();

			setColor(115, 115, 255);
			fillCircle((int) ((x - minlon) / xstep) + MARGIN, ybase - (int) ((y - minlat) / ystep), 2);

		}

		// sluttpunkt markert
		setColor(255, 0, 0);
		fillCircle((int) ((gpspoints[gpspoints.length - 1].getLongitude() - minlon) / xstep) + MARGIN,
				ybase - (int) ((gpspoints[gpspoints.length - 1].getLatitude() - minlat) / ystep), 5);
	}

	public void showStatistics(int j) {

		setColor(0, 0, 0);
		setFont("Courier", 12);

		// TODO - START

		String[] statestikk = gpscomputer.displayStatisticsX(j);

		for (int i = 0; i < statestikk.length; i++) {
			drawString(statestikk[i], TEXTDISTANCE, TEXTDISTANCE + TEXTDISTANCE * i);
		}

		// TODO - SLUTT;
	}

	public void playRoute(int ybase) {

		// TODO - START

		double xstep = xstep();
		double ystep = ystep();

		double minlon = GPSUtils.findMin(GPSUtils.getLongitudes(gpspoints));
		double minlat = GPSUtils.findMin(GPSUtils.getLatitudes(gpspoints));

		// blå sirkel som følger ruten
		setColor(115, 115, 255);
		int id = drawCircle((int) ((gpspoints[0].getLongitude() - minlon) / xstep) + MARGIN,
				ybase - (int) ((gpspoints[0].getLatitude() - minlat) / ystep), 4);

		// løkke for å forflytte sirkelen, og for å tegne linje bak den som markerer
		// hvis løypen går opp- eller nedover
		for (int i = 0; i < gpspoints.length; i++) {

			double x = gpspoints[i].getLongitude();
			double y = gpspoints[i].getLatitude();

			moveCircle(id, (int) ((x - minlon) / xstep) + MARGIN, ybase - (int) ((y - minlat) / ystep));

			if (i < gpspoints.length - 1 && i > 0) {

				double xNext = gpspoints[i + 1].getLongitude();
				double yNext = gpspoints[i + 1].getLatitude();

				if (gpspoints[i - 1].getElevation() > gpspoints[i].getElevation()) {
					setColor(0, 255, 0);
				} else if (gpspoints[i - 1].getElevation() < gpspoints[i].getElevation()) {
					setColor(255, 0, 0);
				} else if (gpspoints[i - 1].getElevation() == gpspoints[i].getElevation()) {
					setColor(0, 0, 255);
				}
				drawLine((int) ((x - minlon) / xstep) + MARGIN, ybase - (int) ((y - minlat) / ystep),
						(int) ((xNext - minlon) / xstep) + MARGIN, ybase - (int) ((yNext - minlat) / ystep));
			}

			if (i < gpspoints.length - 1) {
				pause((int) ((gpspoints[i + 1].getTime() - gpspoints[i].getTime())));
			}
		}

		showStatistics(gpspoints.length - 2);

		// TODO - SLUTT

	}
}
