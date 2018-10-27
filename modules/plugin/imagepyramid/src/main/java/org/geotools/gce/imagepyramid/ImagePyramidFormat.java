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

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.imageio.GeoToolsWriteParams;
import org.geotools.data.DataUtilities;
import org.geotools.data.PrjFileReader;
import org.geotools.gce.imagemosaic.ImageMosaicFormat;
import org.geotools.parameter.DefaultParameterDescriptorGroup;
import org.geotools.parameter.ParameterGroup;
import org.geotools.util.factory.Hints;
import org.opengis.coverage.grid.Format;
import org.opengis.coverage.grid.GridCoverageWriter;
import org.opengis.parameter.GeneralParameterDescriptor;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * This class implements the basic format capabilities for a coverage format.
 *
 * @author Simone Giannecchini (simboss)
 * @author Stefan Alfons Krueger (alfonx), Wikisquare.de : Support for
 *     jar:file:foo.jar/bar.properties like URLs
 */
public final class ImagePyramidFormat extends AbstractGridFormat implements Format {

    /** Logger. */
    private static final Logger LOGGER =
            org.geotools.util.logging.Logging.getLogger(ImagePyramidFormat.class);

    /** Creates an instance and sets the metadata. */
    public ImagePyramidFormat() {
        setInfo();
    }

    /** Sets the metadata information for this format */
    private void setInfo() {
        HashMap<String, String> info = new HashMap<String, String>();
        info.put("name", "ImagePyramid");
        info.put("description", "Image pyramidal plugin");
        info.put("vendor", "Geotools");
        info.put("docURL", "");
        info.put("version", "1.0");
        mInfo = info;

        // reading parameters. Inheriting them from the ImageMosaic
        final ImageMosaicFormat formatForParameters = new ImageMosaicFormat();
        final ParameterValueGroup readParams = formatForParameters.getReadParameters();
        final DefaultParameterDescriptorGroup descriptor =
                (DefaultParameterDescriptorGroup) readParams.getDescriptor();
        List<GeneralParameterDescriptor> descriptors = descriptor.descriptors();
        GeneralParameterDescriptor[] descriptorArray =
                new GeneralParameterDescriptor[descriptors.size()];
        descriptorArray = descriptors.toArray(descriptorArray);
        readParameters =
                new ParameterGroup(new DefaultParameterDescriptorGroup(mInfo, descriptorArray));

        // writing parameters
        writeParameters = null;
    }

    /**
     * Retrieves a reader for this source object in case the provided source can be read using this
     * plugin.
     *
     * @param source Object
     * @return An {@link ImagePyramidReader} if the provided object can be read using this plugin or
     *     null.
     */
    @Override
    public ImagePyramidReader getReader(Object source) {
        return getReader(source, null);
    }

    /**
     * This methods throw an {@link UnsupportedOperationException} because this plugiin si read
     * only.
     */
    @Override
    public GridCoverageWriter getWriter(Object destination) {
        throw new UnsupportedOperationException("This plugin is a read only plugin!");
    }

    /** @see org.geotools.data.coverage.grid.AbstractGridFormat#accepts(Object input) */
    @Override
    public boolean accepts(Object source, Hints hints) {

        if (source == null) {
            throw new NullPointerException(
                    "Null parameter provided to the accepts method of this ImagePyramidFormat");
        }

        try {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine("Accepts method of ImagePyramid with source" + source);
            }

            // Check source
            URL sourceURL = Utils.checkSource(source, hints);
            if (sourceURL == null) {
                return false;
            } else {
                LOGGER.fine("accepts: " + sourceURL);
            }

            try {
                sourceURL.openStream().close();
            } catch (Throwable e) {
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.log(Level.FINE, e.getLocalizedMessage(), e);
                }

                return false;
            }

            // Trying to load information

            // get the crs if able to
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
                LOGGER.log(
                        Level.FINE,
                        new StringBuilder(
                                        "Unable to find a CRS for this coverage, using a default one: ")
                                .append(tempcrs.toWKT())
                                .toString());
            }

            // Load properties file with information about levels and envelope

            // property file
            final Properties properties = new Properties();
            BufferedInputStream propertyStream = null;
            if (!sourceURL.getPath().endsWith(".properties")) {
                return false;
            }
            LOGGER.fine("loading properties from: " + sourceURL);
            final InputStream openStream = sourceURL.openStream();
            try {
                propertyStream = new BufferedInputStream(openStream);
                properties.load(propertyStream);
            } catch (Throwable e) {
                if (propertyStream != null) {
                    propertyStream.close();
                }

                return false;
            } finally {
                if (openStream != null) {
                    openStream.close();
                }
            }

            // load the envelope
            final String envelope = properties.getProperty("Envelope2D");
            if (envelope == null) {
                return false;
            }
            String[] pairs = envelope.split(" ");
            final double cornersV[][] = new double[2][2];
            String pair[];
            for (int i = 0; i < 2; i++) {
                pair = pairs[i].split(",");
                cornersV[i][0] = Double.parseDouble(pair[0]);
                cornersV[i][1] = Double.parseDouble(pair[1]);
            }

            // overviews dir
            final String levelsNumProp = properties.getProperty("LevelsNum");
            if (levelsNumProp == null) {
                return false;
            }

            // resolutions levels
            final String levels = properties.getProperty("Levels");
            if (levels == null) {
                return false;
            }

            // name
            if (properties.getProperty("Name") == null) {
                return false;
            }

            return true;
        } catch (Exception e) {
            if (LOGGER.isLoggable(Level.FINE)) LOGGER.log(Level.FINE, e.getLocalizedMessage(), e);
            return false;
        }
    }

    /**
     * Retrieves a reader for this source object in case the provided source can be read using this
     * plugin.
     *
     * @param source Object
     * @param hints {@link Hints} to control the reader behaviour.
     * @return An {@link ImagePyramidReader} if the provided object can be read using this plugin or
     *     null.
     */
    @Override
    public ImagePyramidReader getReader(Object source, Hints hints) {
        try {

            return new ImagePyramidReader(source, hints);
        } catch (MalformedURLException e) {
            if (LOGGER.isLoggable(Level.SEVERE))
                LOGGER.severe(
                        new StringBuffer(
                                        "impossible to get a reader for the provided source. The error is ")
                                .append(e.getLocalizedMessage())
                                .toString());
            return null;
        } catch (IOException e) {
            if (LOGGER.isLoggable(Level.SEVERE))
                LOGGER.severe(
                        new StringBuffer(
                                        "impossible to get a reader for the provided source. The error is ")
                                .append(e.getLocalizedMessage())
                                .toString());
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
