package no.hvl.dat100ptc.oppgave5;

import easygraphics.EasyGraphics;
import no.hvl.dat100ptc.TODO;
import no.hvl.dat100ptc.oppgave1.GPSPoint;
import no.hvl.dat100ptc.oppgave2.GPSData;
import no.hvl.dat100ptc.oppgave2.GPSDataConverter;
import no.hvl.dat100ptc.oppgave2.GPSDataFileReader;
import no.hvl.dat100ptc.oppgave3.GPSUtils;
import no.hvl.dat100ptc.oppgave4.GPSComputer;

import javax.swing.JOptionPane;

public class ShowProfile extends EasyGraphics {

	private static int MARGIN = 50; // margin on the sides

	// FIXME: use highest point and scale accordingly
	private GPSPoint[] gpspoints;

	public ShowProfile() {

		String filename = JOptionPane.showInputDialog("GPS data filnavn: ");
		GPSComputer gpscomputer = new GPSComputer(filename);

		gpspoints = gpscomputer.getGPSPoints();

	}

	public static void main(String[] args) {
		launch(args);
	}

	public void run() {

		int N = gpspoints.length; // number of data points

		double[] hoyde = new double[gpspoints.length];

		for (int i = 0; i < gpspoints.length; i++) {
			hoyde[i] = gpspoints[i].getElevation();
		}

		int maxHoyde = (int) GPSUtils.findMax(hoyde);

		makeWindow("Height profile", 2 * MARGIN + 2 * N, 2 * MARGIN + maxHoyde);

		// top margin + height of drawing area
		showHeightProfile(MARGIN + maxHoyde);
	}

	public void showHeightProfile(int ybase) {

		// ybase indicates the position on the y-axis where the columns should start

		// TODO - START

		String scaleTidString = getText("Skaleringsfaktor (1/x) for tegning i skalert realtid");

		int scaleTid = Integer.parseInt(scaleTidString);

		for (int i = 0; i < gpspoints.length; i++) {
			setColor(0, 0, 255);
			int h = (int) Math.round(gpspoints[i].getElevation());
			if (h > 0) {
				drawLine(i * 2 + MARGIN, ybase - h, i * 2 + MARGIN, ybase);
			} else {
				h = 0;
				drawLine(i * 2 + MARGIN, ybase - h, i * 2 + MARGIN, ybase);
			}
			if (i < gpspoints.length - 1) {
				pause((1000 / scaleTid) * (gpspoints[i + 1].getTime() - gpspoints[i].getTime()));
			}

			/*
			 * if(i<(gpspoints.length-1)) { pause(1000/(int)GPSUtils.speed(gpspoints[i],
			 * gpspoints[i+1])); }
			 */
		}

		for (int i = 0; i * 10 < ybase - MARGIN; i++) {
			setColor(50, 50, 50);
			drawLine(MARGIN, MARGIN + ((ybase - MARGIN) % 10) + 10 * i, MARGIN + gpspoints.length * 2,
					MARGIN + ((ybase - MARGIN) % 10) + 10 * i);
		}
	}

	// TODO - SLUTT
}
