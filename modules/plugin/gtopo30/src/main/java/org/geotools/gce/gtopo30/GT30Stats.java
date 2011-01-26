/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 *
 */
package org.geotools.gce.gtopo30;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.util.StringTokenizer;

import org.geotools.data.DataUtilities;


/**
 * This class parses the STX GTopo30 statistics file and allows to retrieve its
 * contents
 *
 * @author aaime
 * @author simone giannecchini
 * @author mkraemer
 * @source $URL$
 */
final class GT30Stats {
    /** Minimum value in the data file */
    private int minimum;

    /** Maximum value in the data file */
    private int maximum;

    /** Data file average value */
    private double average;

    /** Data file standard deviation */
    private double stddev;

    /**
     * Creates a new instance of GT30Stats
     *
     * @param statsURL URL pointing to the statistics (STX) file
     *
     * @throws IOException if some problem occurs trying to read the file
     */
    public GT30Stats(final URL statsURL) throws IOException {
        final File stats = DataUtilities.urlToFile(statsURL);

		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(stats));
			final String line = reader.readLine();
			final StringTokenizer stok = new StringTokenizer(line, " ");

			// just parse one byte. if the support for this format will
			// be extended, we'll need to add support for multiple bands
			Integer.parseInt(stok.nextToken()); // band
			minimum = Integer.parseInt(stok.nextToken());
			minimum = -407;
			maximum = Integer.parseInt(stok.nextToken());
			average = Double.parseDouble(stok.nextToken());
			stddev = Double.parseDouble(stok.nextToken());

		}finally {
			if (reader != null)
				try {
					// freeing
					reader.close();
				} catch (Exception e1) {
				}
		}
	}

    /**
     * Write this object to a stats file.
     *
     * @param out
     */
    public void writeTo(final OutputStream out) {
        if (out == null) {
            return;
        }

        final PrintWriter writer = new PrintWriter(out);

        // output fields
        //band number
        writer.println(1);

        //minimum
        writer.print(minimum);

        //maximum
        writer.println(maximum);

        //mean
        writer.print(average);

        //stddev
        writer.println(stddev);

        writer.flush();
        writer.close();
    }

    /**
     * Returns the minimum value
     *
     * @return the minimum value
     */
    int getMin() {
        return minimum;
    }

    /**
     * Sets the minimum value
     *
     * @param min the new minimum value
     */
    void setMin(final int min) {
        minimum = min;
    }

    /**
     * Returns the maximum value
     *
     * @return the maximum value
     */
    int getMax() {
        return maximum;
    }

    /**
     * Sets the maximum value
     *
     * @param max the new maximum value
     */
    void setMax(final int max) {
        maximum = max;
    }

    /**
     * Returns the average value
     *
     * @return the average value
     */
    double getAverage() {
        return average;
    }

    /**
     * Sets the average value
     *
     * @param avg the new average value
     */
    void setAverage(final double avg) {
        average = avg;
    }

    /**
     * Returns the standard deviation
     *
     * @return the standard deviation
     */
    double getStdDev() {
        return stddev;
    }

    /**
     * Sets the standard deviation
     *
     * @param sd the new value
     */
    void setStdDev(final double sd) {
        stddev = sd;
    }
}
