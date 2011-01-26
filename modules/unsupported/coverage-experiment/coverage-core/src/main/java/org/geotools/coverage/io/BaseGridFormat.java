/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.io;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.spi.ImageReaderSpi;

import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.imageio.GeoToolsWriteParams;
import org.geotools.factory.Hints;
import org.geotools.parameter.DefaultParameterDescriptor;
import org.opengis.coverage.grid.Format;
import org.opengis.coverage.grid.GridCoverageReader;
import org.opengis.coverage.grid.GridCoverageWriter;
import org.opengis.parameter.GeneralParameterValue;

/**
 * A Base abstract class implementing {@link Format}
 * 
 * @author Daniele Romagnoli, GeoSolutions
 * @author Simone Giannecchini, GeoSolutions
 * 
 */
@SuppressWarnings("deprecation")
public abstract class BaseGridFormat extends AbstractGridFormat implements
        Format {
    private final static Logger LOGGER = org.geotools.util.logging.Logging
            .getLogger("org.geotools.coverage.io");

    /** The inner {@code ImageReaderSpi} */
    protected final ImageReaderSpi spi;

    /**
     * Constructor for the {@code BaseGridFormat}. It is invoked by the
     * underlying implementations.
     * 
     * @param spi
     *                the format specific {@code ImageReaderSpi} instance
     */
    protected BaseGridFormat(ImageReaderSpi spi) {
        this.spi = spi;
        setInfo();
    }

    /**
     * The {@code String} representing the parameter to customize multithreading
     * use
     */
    private static final String USE_MT = "USE_MULTITHREADING";

    /**
     * This {@link GeneralParameterValue} can be provided to the
     * {@link GridCoverageReader}s through the
     * {@link GridCoverageReader#read(GeneralParameterValue[])} method in order
     * to specify to use multithreading when leveraging on a JAI ImageRead
     * operation. This will be achieved with the use of the ImageReadMT
     * operation of the ImageIO-Ext.
     */
    public static final DefaultParameterDescriptor<Boolean> USE_MULTITHREADING = new DefaultParameterDescriptor<Boolean>(
            USE_MT, Boolean.class,
            new Boolean[] { Boolean.TRUE, Boolean.FALSE }, Boolean.FALSE);

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
            SUGGESTED_TILESIZE, String.class, null, "");

    public static final String TILE_SIZE_SEPARATOR = ",";

    /**
     * Each plugin needs to implement this method defining format specific
     * properties
     */
    protected abstract void setInfo();

    /**
     * @see org.geotools.data.coverage.grid.AbstractGridFormat#getReader(Object
     *      source)
     */
    public AbstractGridCoverage2DReader getReader(Object source) {
        return getReader(source, null);
    }

    /**
     * @see org.geotools.data.coverage.grid.AbstractGridFormat#createWriter(java.lang.Object
     *      destination)
     * 
     * Actually, the plugin does not support write capabilities. The method
     * throws an {@code UnsupportedOperationException}.
     */
    public GridCoverageWriter getWriter(Object destination) {
        throw new UnsupportedOperationException(
                "This plugin does not support writing at this time.");
    }

    /**
     * @see org.geotools.data.coverage.grid.AbstractGridFormat#getDefaultImageIOWriteParameters
     * 
     * Actually, the plugin does not support write capabilities. The method
     * throws an {@code UnsupportedOperationException}.
     */
    public GeoToolsWriteParams getDefaultImageIOWriteParameters() {
        throw new UnsupportedOperationException(
                "This plugin does not support writing parameters");
    }

    /**
     * @see org.geotools.data.coverage.grid.AbstractGridFormat#createWriter(java.lang.Object
     *      destination,Hints hints)
     * 
     * Actually, the plugin does not support write capabilities. The method
     * throws an {@code UnsupportedOperationException}.
     */
    public GridCoverageWriter getWriter(Object destination, Hints hints) {
        throw new UnsupportedOperationException(
                "This plugin does not support writing at this time.");
    }

    /**
     * @see org.geotools.data.coverage.grid.AbstractGridFormat#accepts(java.lang.Object input)
     */
    public boolean accepts(Object input) {
        try {
            return spi.canDecodeInput(input);
        } catch (IOException e) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE, e.getLocalizedMessage(), e);
            }
            return false;
        }
    }
}
