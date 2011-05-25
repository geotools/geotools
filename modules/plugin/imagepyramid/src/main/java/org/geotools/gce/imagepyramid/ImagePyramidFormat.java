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
 */
package org.geotools.gce.imagepyramid;

import java.awt.Color;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.imageio.GeoToolsWriteParams;
import org.geotools.data.DataUtilities;
import org.geotools.data.PrjFileReader;
import org.geotools.factory.Hints;
import org.geotools.parameter.DefaultParameterDescriptor;
import org.geotools.parameter.DefaultParameterDescriptorGroup;
import org.geotools.parameter.ParameterGroup;
import org.opengis.coverage.grid.Format;
import org.opengis.coverage.grid.GridCoverageReader;
import org.opengis.coverage.grid.GridCoverageWriter;
import org.opengis.parameter.GeneralParameterDescriptor;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterDescriptor;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * This class implements the basic format capabilities for a coverage format.
 * 
 * @author Simone Giannecchini (simboss)
 * @author Stefan Alfons Krueger (alfonx), Wikisquare.de : Support for jar:file:foo.jar/bar.properties like URLs
 *
 * @source $URL$
 */
public final class ImagePyramidFormat extends AbstractGridFormat implements Format {

	/** Logger. */
	private final static Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.geotools.gce.imagepyramid");
	
    /** The {@code String} representing the parameter to customize tile sizes */
    private static final String SUGGESTED_TILESIZE = "SUGGESTED_TILE_SIZE";

    /**
     * This {@link GeneralParameterValue} can be provided to the
     * {@link GridCoverageReader}s through the
     * {@link GridCoverageReader#read(GeneralParameterValue[])} method in order
     * to specify the suggested size of tiles to avoid long time reading
     * occurring with JAI ImageRead on striped images. (Images with tiles Nx1)
     * Value should be a String in the form of "W,H" (without quotes) where W is
     * a number representing the suggested tileWidth and H is a number
     * representing the suggested tileHeight.
     */
    public static final DefaultParameterDescriptor<String> SUGGESTED_TILE_SIZE = new DefaultParameterDescriptor<String>(
    		SUGGESTED_TILESIZE, String.class, null, "512,512");

    public static final String TILE_SIZE_SEPARATOR = ",";
    
    /** Control the type of the final mosaic. */
    public static final ParameterDescriptor<Boolean> FADING = new DefaultParameterDescriptor<Boolean>(
            "Fading", Boolean.class, new Boolean[]{Boolean.TRUE,Boolean.FALSE}, Boolean.FALSE);

    /** Control the transparency of the input coverages. */
    public static final ParameterDescriptor<Color> INPUT_TRANSPARENT_COLOR = new DefaultParameterDescriptor<Color>(
            "InputTransparentColor", Color.class, null, null);

    /** Control the transparency of the output coverage. */
    public static final ParameterDescriptor<Color> OUTPUT_TRANSPARENT_COLOR = new DefaultParameterDescriptor<Color>(
            "OutputTransparentColor", Color.class, null, null);

    /** Control the thresholding on the input coverage */
    public static final ParameterDescriptor<Integer> MAX_ALLOWED_TILES = new DefaultParameterDescriptor<Integer>(
            "MaxAllowedTiles", Integer.class, null, Integer.MAX_VALUE);
    
    /** Control the threading behavior for this plugin. This parameter contains the number of thread that we should use to load the granules. Default value is 0 which means not additional thread, max value is 8.*/
    public static final ParameterDescriptor<Boolean> ALLOW_MULTITHREADING = new DefaultParameterDescriptor<Boolean>(
            "AllowMultithreading", Boolean.class, new Boolean[]{Boolean.TRUE,Boolean.FALSE}, Boolean.FALSE);
    
    /** Control the background values for the output coverage */
    public static final ParameterDescriptor<double[]> BACKGROUND_VALUES = new DefaultParameterDescriptor<double[]>(
            "BackgroundValues", double[].class, null, null);
    
	/**
	 * Creates an instance and sets the metadata.
	 */
	public ImagePyramidFormat() {
		setInfo();
	}

	/**
	 * Sets the metadata information for this format
	 */
	private void setInfo() {
		HashMap<String, String> info = new HashMap<String, String>();
		info.put("name", "ImagePyramid");
		info.put("description", "Image pyramidal plugin");
		info.put("vendor", "Geotools");
		info.put("docURL", "");
		info.put("version", "1.0");
		mInfo = info;

        // reading parameters
        readParameters = new ParameterGroup(new DefaultParameterDescriptorGroup(mInfo,
                new GeneralParameterDescriptor[]{
        		READ_GRIDGEOMETRY2D,
        		INPUT_TRANSPARENT_COLOR,
                OUTPUT_TRANSPARENT_COLOR,
                USE_JAI_IMAGEREAD,
                BACKGROUND_VALUES,
                SUGGESTED_TILE_SIZE,
                ALLOW_MULTITHREADING,
                MAX_ALLOWED_TILES}));

		// reading parameters
		writeParameters = null;
	}

	/**
	 * Retrieves a reader for this source object in case the provided source can
	 * be read using this plugin.
	 * 
	 * @param source
	 *            Object
	 * @return An {@link ImagePyramidReader} if the provided object can be read
	 *         using this plugin or null.
	 */
	@Override
	public ImagePyramidReader getReader(Object source) {
		return getReader(source, null);
	}

	/**
	 * This methods throw an {@link UnsupportedOperationException} because this
	 * plugiin si read only.
	 */
	@Override
	public GridCoverageWriter getWriter(Object destination) {
		throw new UnsupportedOperationException(
				"This plugin is a read only plugin!");
	}

	
	/**
	 * @see org.geotools.data.coverage.grid.AbstractGridFormat#accepts(Object
	 *      input)
	 */
	@Override
	public boolean accepts(Object source, Hints hints) {
		try {

            // /////////////////////////////////////////////////////////////////////
            //
            // Check source
            //
            // /////////////////////////////////////////////////////////////////////
			URL sourceURL = Utils.checkSource(source, hints);
			if(sourceURL == null)
			    return false;
            try {
                sourceURL.openStream().close();
            } catch (Throwable e) {
                return false;
            }
			
			// ///////////////////////////////////////////////////////////////////
			//
			// Trying to load informations
			//
			//
			// ///////////////////////////////////////////////////////////////////
			// //
			//
			// get the crs if able to
			//
			// //
			final URL prjURL = DataUtilities.changeUrlExt(sourceURL, "prj"); 
			PrjFileReader crsReader;
			try {
				crsReader = new PrjFileReader(Channels.newChannel(prjURL.openStream()));
			} catch (FactoryException e) {
				return false;
			}
			CoordinateReferenceSystem tempcrs = crsReader.getCoordinateReferenceSystem();
			if (tempcrs == null) {
				// use the default crs
				tempcrs = AbstractGridFormat.getDefaultCRS();
				LOGGER.log(Level.FINE,new StringBuffer("Unable to find a CRS for this coverage, using a default one: ").append(tempcrs.toWKT()).toString());
			}
			//
			// ///////////////////////////////////////////////////////////////////
			//
			// Load properties file with information about levels and envelope
			//
			//
			// ///////////////////////////////////////////////////////////////////
			// property file
			final Properties properties = new Properties();
			BufferedInputStream propertyStream = null;
			if(!sourceURL.getPath().endsWith(".properties"))
				return false;
			final InputStream openStream = sourceURL.openStream();
			try {
				propertyStream = new BufferedInputStream(openStream);
				properties.load(propertyStream);
			} catch (Throwable e) {
				if(propertyStream!=null)
					propertyStream.close();
				return false;
			} finally {
				if (openStream != null) 
					openStream.close();
			}

			// load the envelope
			final String envelope = properties.getProperty("Envelope2D");
			if (envelope == null) return false;
			String[] pairs = envelope.split(" ");
			final double cornersV[][] = new double[2][2];
			String pair[];
			for (int i = 0; i < 2; i++) {
				pair = pairs[i].split(",");
				cornersV[i][0] = Double.parseDouble(pair[0]);
				cornersV[i][1] = Double.parseDouble(pair[1]);
			}

			// overviews dir
			int numOverviews = Integer.parseInt(properties.getProperty("LevelsNum")) - 1;

			// resolutions levels
			final String levels = properties.getProperty("Levels");
			pairs = levels.split(" ");
			double[][] overViewResolutions = numOverviews >= 1 ? new double[numOverviews][2]: null;
			pair = pairs[0].split(",");
			double[] highestRes = new double[2];
			highestRes[0] = Double.parseDouble(pair[0]);
			highestRes[1] = Double.parseDouble(pair[1]);
			for (int i = 1; i < numOverviews + 1; i++) {
				pair = pairs[i].split(",");
				overViewResolutions[i - 1][0] = Double.parseDouble(pair[0]);
				overViewResolutions[i - 1][1] = Double.parseDouble(pair[1]);
			}

			// name
			if (properties.getProperty("Name") == null)
				return false;

			return true;
		} catch (IOException e) {
			if (LOGGER.isLoggable(Level.FINE))
				LOGGER.log(Level.FINE, e.getLocalizedMessage(), e);
			return false;

		} catch (NumberFormatException e) {
			if (LOGGER.isLoggable(Level.FINE))
				LOGGER.log(Level.FINE, e.getLocalizedMessage(), e);
			return false;
		}

	}

	/**
	 * Retrieves a reader for this source object in case the provided source can
	 * be read using this plugin.
	 * 
	 * @param source
	 *            Object
	 * @param hints
	 *            {@link Hints} to control the reader behaviour.
	 * @return An {@link ImagePyramidReader} if the provided object can be read
	 *         using this plugin or null.
	 */
	@Override
	public ImagePyramidReader getReader(Object source, Hints hints) {
		try {

			return new ImagePyramidReader(source, hints);
		} catch (MalformedURLException e) {
			if (LOGGER.isLoggable(Level.SEVERE))
				LOGGER
						.severe(new StringBuffer(
								"impossible to get a reader for the provided source. The error is ")
								.append(e.getLocalizedMessage()).toString());
			return null;
		} catch (IOException e) {
			if (LOGGER.isLoggable(Level.SEVERE))
				LOGGER
						.severe(new StringBuffer(
								"impossible to get a reader for the provided source. The error is ")
								.append(e.getLocalizedMessage()).toString());
			return null;
		}
	}

	/**
	 * Throw an exception since this plugin is readonly.
	 * 
	 * @return nothing.
	 */
	@Override
	public GeoToolsWriteParams getDefaultImageIOWriteParameters() {
		throw new UnsupportedOperationException("Unsupported method.");
	}

	@Override
	public GridCoverageWriter getWriter(Object destination, Hints hints) {
		throw new UnsupportedOperationException("Unsupported method.");
	}

}
