package no.hvl.dat100ptc.oppgave6;

import javax.swing.JOptionPane;

import easygraphics.*;
import no.hvl.dat100ptc.TODO;
import no.hvl.dat100ptc.oppgave1.GPSPoint;
import no.hvl.dat100ptc.oppgave2.GPSData;
import no.hvl.dat100ptc.oppgave2.GPSDataFileReader;
import no.hvl.dat100ptc.oppgave3.GPSUtils;
import no.hvl.dat100ptc.oppgave4.GPSComputer;
import no.hvl.dat100ptc.oppgave5.ShowProfile;
import no.hvl.dat100ptc.oppgave5.ShowRoute;

public class CycleComputer extends EasyGraphics {

	private GPSComputer gpscomp;
	private GPSPoint[] gpspoints;

	private static int MARGIN = 40;
	private static int ROUTEMAPXSIZE = 2; // defineres med 2*antall-GPS-målinger
	private static int ROUTEMAPYSIZE = 600;
	private static int TEXTWIDTH = 225; // hvor mye plass tabellen med statestikk tar i x-aksen
	private static int TEXTDISTANCE = 20; // avstand mellom hver linje med statestikk
	private static int HEIGHTSIZE = 200; // hvor mya plass høydediagramet tar i y-aksen

	private double minlon, minlat, maxlon, maxlat;

	private double xstep, ystep;

	public CycleComputer() {

		String filename = JOptionPane.showInputDialog("GPS data filnavn: ");

		gpscomp = new GPSComputer(filename);
		gpspoints = gpscomp.getGPSPoints();

	}

	public static void main(String[] args) {
		launch(args);
	}

	public void run() {

		int N = gpspoints.length; // number of gps points

		minlon = GPSUtils.findMin(GPSUtils.getLongitudes(gpspoints));
		minlat = GPSUtils.findMin(GPSUtils.getLatitudes(gpspoints));

		maxlon = GPSUtils.findMax(GPSUtils.getLongitudes(gpspoints));
		maxlat = GPSUtils.findMax(GPSUtils.getLatitudes(gpspoints));

		xstep = xstep();
		ystep = ystep();

		// lager et vindu på forskjellig måte, avhengig av hva som tar mest plass i
		// x-aksen av statestikktabellen og høydediagrammet
		if (ROUTEMAPXSIZE * N < TEXTWIDTH) {
			makeWindow("Cycle Computer", 2 * MARGIN + TEXTWIDTH, 2 * MARGIN + ROUTEMAPYSIZE);
		} else {
			makeWindow("Cycle Computer", 2 * MARGIN + ROUTEMAPXSIZE * N, 2 * MARGIN + ROUTEMAPYSIZE);
		}

		bikeRoute(ROUTEMAPYSIZE);

	}

	public void bikeRoute(int ybase) {

		int N = gpspoints.length; // number of gps points

		// startpubkt & stoppunkt
		double xstep = xstep();
		double ystep = ystep();

		double minlon = GPSUtils.findMin(GPSUtils.getLongitudes(gpspoints));
		double minlat = GPSUtils.findMin(GPSUtils.getLatitudes(gpspoints));

		setColor(0, 255, 0); // startpunkt
		fillCircle((int) ((gpspoints[0].getLongitude() - minlon) / xstep) + MARGIN,
				ybase - (int) ((gpspoints[0].getLatitude() - minlat) / ystep), 5);

		setColor(0, 0, 255); // sirkel som forflytter seg langs ruten
		int id = drawCircle((int) ((gpspoints[0].getLongitude() - minlon) / xstep) + MARGIN,
				ybase - (int) ((gpspoints[0].getLatitude() - minlat) / ystep), 4);

		
		for (int i = 1; i < N; i++) {

			showHeightProfile(ybase, i);
			showStatistics(i);

			double x = gpspoints[i].getLongitude();
			double y = gpspoints[i].getLatitude();

			if (i < N && i > 0) {

				double xPast = gpspoints[i - 1].getLongitude();
				double yPast = gpspoints[i - 1].getLatitude();

				// ruten visualisert i linjer som illustrerer om ruten stiger eller synker
				if (gpspoints[i - 1].getElevation() > gpspoints[i].getElevation()) {
					setColor(0, 255, 0);
				} else if (gpspoints[i - 1].getElevation() < gpspoints[i].getElevation()) {
					setColor(255, 0, 0);
				} else if (gpspoints[i - 1].getElevation() == gpspoints[i].getElevation()) {
					setColor(0, 0, 255);
				}
				drawLine((int) ((x - minlon) / xstep) + MARGIN, ybase - (int) ((y - minlat) / ystep),
						(int) ((xPast - minlon) / xstep) + MARGIN, ybase - (int) ((yPast - minlat) / ystep));
			}

			if (i == N - 1) { // sluttpunkt
				setColor(255, 0, 0);
				fillCircle((int) ((gpspoints[N - 1].getLongitude() - minlon) / xstep) + MARGIN,
						ybase - (int) ((gpspoints[N - 1].getLatitude() - minlat) / ystep), 5);
			}
			
			// bevegelse av den blå sirkelen som følger ruten
			moveCircle(id, (int) ((x - minlon) / xstep) + MARGIN, ybase - (int) ((y - minlat) / ystep));

			// aktiver for å merke hvert punkt for GPS-måling med en blå prikk
			// setColor(115, 115, 255);
			// fillCircle((int) ((x - minlon) / xstep) + MARGIN, ybase - (int) ((y - minlat)
			// / ystep), 2);

			/*
			 * Programmet går treigt så vet ikke om det er poeng å prøve å styre hastigheten
			 * if(i<gpspoints.length-1) {
			 * pause((int)((gpspoints[i+1].getTime()-gpspoints[i].getTime()))/10); }
			 */

		}
	}

	public double xstep() {

		int N = gpspoints.length; // number of gps points

		double maxlon = GPSUtils.findMax(GPSUtils.getLongitudes(gpspoints));
		double minlon = GPSUtils.findMin(GPSUtils.getLongitudes(gpspoints));

		double xstep = (Math.abs(maxlon - minlon)) / (ROUTEMAPXSIZE * N);

		return xstep;
	}

	public double ystep() {

		double maxlat = GPSUtils.findMax(GPSUtils.getLatitudes(gpspoints));
		double minlat = GPSUtils.findMin(GPSUtils.getLatitudes(gpspoints));

		double ystep = (Math.abs(maxlat - minlat)) / (ROUTEMAPYSIZE - (2 * HEIGHTSIZE + MARGIN));

		return ystep;
	}

	public void showStatistics(int j) {

		setFont("Courier", 12);

		// TODO - START

		String[] statestikk = gpscomp.displayStatisticsX(j);

		// setColor(255, 255, 255);
		// fillRectangle(MARGIN *4, MARGIN, 75, 190);

		for (int i = 0; i < statestikk.length; i++) {
			if (i > 0 && i < statestikk.length - 1) {
				setColor(255, 255, 255);
				fillRectangle(MARGIN, MARGIN - 9 + TEXTDISTANCE * i, 225, 10);
			}
			setColor(0, 0, 0);
			drawString(statestikk[i], MARGIN, MARGIN + TEXTDISTANCE * i);
		}

		// TODO - SLUTT;
	}

	public void showHeightProfile(int ybase, int i) {

		int N = gpspoints.length; // number of gps points

		int h = (int) Math.round(gpspoints[i].getElevation());

		double[] hoyde = new double[N];
		for (int k = 0; k < N; k++) {
			hoyde[k] = gpspoints[k].getElevation();
		}
		double maxHoyde = (GPSUtils.findMax(hoyde));

		setColor(0, 0, 255);
		if (h > 0) {
			drawLine(i * 2 + MARGIN, MARGIN
					+ (int) ((ROUTEMAPYSIZE - ((HEIGHTSIZE + MARGIN))) - ((HEIGHTSIZE - 2 * MARGIN)) * (h / maxHoyde)),
					i * 2 + MARGIN, MARGIN + (ROUTEMAPYSIZE - ((HEIGHTSIZE + MARGIN))));
		} else {
			h = 0;
			drawLine(i * 2 + MARGIN, MARGIN
					+ (int) ((ROUTEMAPYSIZE - ((HEIGHTSIZE + MARGIN))) - ((HEIGHTSIZE - 2 * MARGIN)) * (h / maxHoyde)),
					i * 2 + MARGIN, MARGIN + (ROUTEMAPYSIZE - ((HEIGHTSIZE + MARGIN))));
		}
	}
}
