/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data;

import java.awt.geom.AffineTransform;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.referencing.operation.matrix.GeneralMatrix;
import org.geotools.referencing.operation.transform.ProjectiveTransform;
import org.geotools.resources.i18n.ErrorKeys;
import org.geotools.resources.i18n.Errors;
import org.opengis.referencing.operation.MathTransform;

/**
 * This class is responsible for parsing a world file in order to build an
 * affine transform using the parameters provided in the file itself.
 * 
 * <p>
 * The parameters found in the file should be as follows:
 * <ol>
 * <li>size of pixel in x direction</li>
 * <li>rotation term for row</li>
 * <li>rotation term for column</li>
 * <li>size of pixel in y direction</li>
 * <li>x coordinate of centre of upper left pixel in map units</li>
 * <li>y coordinate of centre of upper left pixel in map units</li>
 * </ol>
 * <strong>Note that the last two coordinates refer to the centre of the pixel!</strong>
 * 
 * 
 * <p>
 * It is worth to point out that various data sources describe the parameters in
 * the world file as the mapping from the pixel centres' to the associated world
 * coords. Here we directly build the needed grid to world transform and we DO
 * NOT add any half a pixel translation given that, as stated above, the values
 * we receive should map to the centre of the pixel.
 * 
 * 
 * @author Simone Giannecchini, GeoSolutions
 * @since 2.3
 * 
 *
 * @source $URL$
 */
public class WorldFileReader {

	/**
	 * Default size for the underlying buffer,
	 */
	public final static int DEFAULT_BUFFER_SIZE = 4096;
	
	/**
	 * Logger for this class.
	 */
	private final static Logger LOGGER = org.geotools.util.logging.Logging
			.getLogger("org.geotools.data.data");

	/** Resolution on the first dimension. */
	private double xPixelSize = 0.0;

	/** Rotation on the first dimension. */
	private double rotationX = 0.0;

	/** Resolution on the second dimension. */
	private double rotationY = 0.0;

	/** Resolution on the second dimension. */
	private double yPixelSize = 0.0;

	/** Upper left centre coordinate of first dimension. */
	private double xULC = 0.0;

	/** Upper left centre coordinate of second dimension. */
	private double yULC = 0.0;

	/** Resulting linear transform. */
	private GeneralMatrix transform;

	/**
	 * Default constructor for a {@link WorldFileReader}.
	 * 
	 * @param inFile holds the location where to read from.
	 * @throws IOException in case something bad happens.
	 */
	public WorldFileReader(final File inFile) throws IOException {
		this(inFile, DEFAULT_BUFFER_SIZE);
	}

	/**
	 * Constructor for a {@link WorldFileReader}.
	 * 
	 * @param worldfile holds the location where to read from.
	 * @param bufferSize to buffer when reading.
	 * @throws IOException in case something bad happens.
	 */
	public WorldFileReader(final File worldfile, final int bufferSize)
	throws IOException {
		if(worldfile==null)
			throw new IllegalArgumentException(Errors.format(ErrorKeys.NULL_ARGUMENT_$1,"worldfile"));
		if(!worldfile.isFile()||!worldfile.canRead())
			throw new IllegalArgumentException(Errors.format(ErrorKeys.FILE_DOES_NOT_EXIST_$1,worldfile));
		if(bufferSize<=0)
			throw new IllegalArgumentException(Errors.format(ErrorKeys.ILLEGAL_ARGUMENT_$2,"bufferSize",bufferSize));
		parseWorldFile(new BufferedReader(
				new FileReader(worldfile)));
	}
	
	/**
	 * Constructor for a {@link WorldFileReader}.
	 * 
	 * @param worldfile {@link URL} where to read from.
	 * @param bufferSize to buffer when reading.
	 * @throws IOException in case something bad happens.
	 */
	public WorldFileReader(final URL worldfile, final int bufferSize)
	throws IOException {
		if(worldfile==null)
			throw new IllegalArgumentException(Errors.format(ErrorKeys.NULL_ARGUMENT_$1,"inFile"));
		if(bufferSize<=0)
			throw new IllegalArgumentException(Errors.format(ErrorKeys.ILLEGAL_ARGUMENT_$2,"bufferSize",bufferSize));		
		parseWorldFile(new BufferedReader(
				new InputStreamReader(worldfile.openStream())));
	}
	
	/**
	 * Constructor for a {@link WorldFileReader}.
	 * 
	 * @param worldfile {@link URL} where to read from.
	 * @param bufferSize to buffer when reading.
	 * @throws IOException in case something bad happens.
	 */
	public WorldFileReader(final URL worldfile)
	throws IOException {
		this(worldfile, WorldFileReader.DEFAULT_BUFFER_SIZE);
	}

	private void parseWorldFile(final BufferedReader bufferedreader)
			throws IOException, DataSourceException {
		int index = 0;
		String str;
		try {
			while ((str = bufferedreader.readLine()) != null) {

				double value = 0;

				try {
					value = Double.parseDouble(str.trim());
				} catch (Throwable t) {
					// A trick to bypass invalid lines ...
					if (LOGGER.isLoggable(Level.FINE))
						LOGGER.log(Level.FINE, t.getLocalizedMessage(), t);
					continue;
				}

				switch (index) {
				case 0:
					xPixelSize = value;

					break;

				case 1:
					rotationX = value;

					break;

				case 2:
					rotationY = value;

					break;

				case 3:
					yPixelSize = value;

					break;

				case 4:
					xULC = value;

					break;

				case 5:
					yULC = value;

					break;

				default:
					break;
				}

				index++;
			}
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			try {
				bufferedreader.close();
			} catch (Throwable t) {
				// A trick to bypass invalid lines ...
				if (LOGGER.isLoggable(Level.FINE))
					LOGGER.log(Level.FINE, t.getLocalizedMessage(), t);
			}

		}

		// did we find all we were looking for?
		if (index < 5)
			throw new DataSourceException(
					"Not all the values were found for this world file!");
	}

	public double getRotationX() {
		return rotationX;
	}

	public double getRotationY() {
		return rotationY;
	}

	public double getXPixelSize() {
		return xPixelSize;
	}

	public double getXULC() {
		return xULC;
	}

	public double getYPixelSize() {
		return yPixelSize;
	}

	public double getYULC() {
		return yULC;
	}

	public synchronized MathTransform getTransform() {
		initTransform();
		return ProjectiveTransform.create(transform);
	}

	private void initTransform() {
		if (transform == null) {
			// building the transform
			final GeneralMatrix gm = new GeneralMatrix(3);

			// compute an "offset and scale" matrix
			gm.setElement(0, 0, xPixelSize);
			gm.setElement(1, 1, yPixelSize);
			gm.setElement(0, 1, rotationX);
			gm.setElement(1, 0, rotationY);

			gm.setElement(0, 2, xULC);
			gm.setElement(1, 2, yULC);

			// make it a LinearTransform
			transform = gm;
		}

	}
	
	/**
	 * Creates an {@link AffineTransform} for interoperability with Java2d.
	 * @return an {@link AffineTransform} representing the transformation represented 
	 * 		   by the underlying world file.
	 */
	public synchronized AffineTransform getAffineTransform() {
		initTransform();
		return transform.toAffineTransform2D();
	}

}
