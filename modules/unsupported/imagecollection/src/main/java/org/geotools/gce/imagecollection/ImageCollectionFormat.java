/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.imagecollection;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.imageio.GeoToolsWriteParams;
import org.geotools.data.DataSourceException;
import org.geotools.data.DataUtilities;
import org.geotools.factory.Hints;
import org.geotools.parameter.DefaultParameterDescriptor;
import org.geotools.parameter.DefaultParameterDescriptorGroup;
import org.geotools.parameter.ParameterGroup;
import org.opengis.coverage.grid.Format;
import org.opengis.coverage.grid.GridCoverageReader;
import org.opengis.coverage.grid.GridCoverageWriter;
import org.opengis.filter.Filter;
import org.opengis.parameter.GeneralParameterDescriptor;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterDescriptor;

/**
 *
 *
 *
 * @source $URL: http://svn.osgeo.org/geotools/trunk/modules/unsupported/imagecollection/src/main/java/org/geotools/gce/imagecollection/ImageCollectionFormat.java $
 */
public final class ImageCollectionFormat extends AbstractGridFormat implements Format {

    public static final String TILE_SIZE_SEPARATOR = ",";

    /** Logger. */
    private final static Logger LOGGER = org.geotools.util.logging.Logging
        .getLogger("org.geotools.gce.imagecollection");
    
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
            "SUGGESTED_TILESIZE", String.class, null, "512,512");
    
    /** Control the background values for the output coverage */
    public static final ParameterDescriptor<double[]> BACKGROUND_VALUES = new DefaultParameterDescriptor<double[]>(
            "BackgroundValues", double[].class, null, null);

    /** Filter tiles based on attributes from the input coverage */
    public static final ParameterDescriptor<Filter> FILTER = new DefaultParameterDescriptor<Filter>(
            "Filter", Filter.class, null, null);

    /**
     * ImageCollectionFormat
     */
    public ImageCollectionFormat() {
        setInfo();
    }

    private void setInfo() {
        // information for this format
        HashMap<String, String> info = new HashMap<String, String>();

        info.put("name", "ImageCollection");
        info.put("description", "A store setup on top of a folder containing several images");
        info.put("vendor", "Geotools");
        info.put("docURL", "");
        info.put("version", "1.0");
        mInfo = info;

        // reading parameters
        readParameters = new ParameterGroup(
                new DefaultParameterDescriptorGroup(mInfo,
                        new GeneralParameterDescriptor[] { READ_GRIDGEOMETRY2D,
                                FILTER, SUGGESTED_TILE_SIZE, USE_JAI_IMAGEREAD,
                                BACKGROUND_COLOR }));

        // writing parameters
        writeParameters = null;
    }

    /**
     * Retrieves a {@link ImageCollectionReader} in case the provided
     * <code>source</code> can be accepted as a valid source for an ImageCollection.
     * The method returns null otherwise.
     * 
     * @param source
     *            The source object to read a WorldImage from
     * 
     * @return a new WorldImageReader for the source
     */
    @Override
    public ImageCollectionReader getReader(Object source) {
        return getReader(source, null);
    }

    /**
     * 
     */
    @Override
    public GridCoverageWriter getWriter(Object destination) {
        throw new UnsupportedOperationException("This plugin does not support writing.");
    }

    /**
     *
     */
    @Override
    public GridCoverageWriter getWriter(Object destination, Hints hints) {
        throw new UnsupportedOperationException("This plugin does not support writing.");
    }

    /**
     * Takes the input and determines if it is a class that we can understand
     * and then futher checks the format of the class to make sure we can
     * read/write to it.
     * 
     * @param input
     *            The object to check for acceptance.
     * 
     * @return true if the input is acceptable, false otherwise
     */
    @Override
    public boolean accepts(Object input, Hints hints) {
        try {
            URL url = Utils.checkSource(input);
            if (url != null){
                File file = DataUtilities.urlToFile(url);
                if (file.isDirectory()) {
                    return true;
                }
            }
        } catch (MalformedURLException e) {
            if (LOGGER.isLoggable(Level.SEVERE)){
                LOGGER.log(Level.SEVERE, "Unable to accept the specified input", e);
            }
        } catch (DataSourceException e) {
            if (LOGGER.isLoggable(Level.SEVERE)){
                LOGGER.log(Level.SEVERE, "Unable to accept the specified input", e);
            }
        }
        return false;
    }

    /**
     * Retrieves a {@link ImageCollectionReader} in case the provided
     * <code>source</code> can be accepted as a valid source for an image collection coverage.
     * The method returns null otherwise.
     * 
     * @param source
     *            The source object to read an ImageCollection from
     * @param hints
     *            {@link Hints} to control the provided
     *            {@link ImageCollectionReader}.
     * @return a new WorldImageReader for the source
     */
    @Override
    public ImageCollectionReader getReader(Object source, Hints hints) {
        try {
            return new ImageCollectionReader(source, hints);
        } catch (DataSourceException e) {
            if (LOGGER.isLoggable(Level.WARNING))
                LOGGER.log(Level.WARNING, e.getLocalizedMessage(), e);
            return null;
        }
    }

    /**
     * Always returns null since for the moment there are no
     * {@link GeoToolsWriteParams} available for this format.
     * 
     * @return always null.
     */
    @Override
    public GeoToolsWriteParams getDefaultImageIOWriteParameters() {
        return null;
    }
}
