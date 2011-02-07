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
package org.geotools.coverageio.gdal;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.spi.ImageReaderSpi;

import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.imageio.GeoToolsWriteParams;
import org.geotools.factory.Hints;
import org.geotools.parameter.DefaultParameterDescriptor;
import org.geotools.parameter.DefaultParameterDescriptorGroup;
import org.geotools.parameter.ParameterGroup;
import org.opengis.coverage.grid.Format;
import org.opengis.coverage.grid.GridCoverageReader;
import org.opengis.coverage.grid.GridCoverageWriter;
import org.opengis.parameter.GeneralParameterDescriptor;
import org.opengis.parameter.GeneralParameterValue;

/**
 * A Base abstract class implementing {@link Format}, to be extended by Formats
 * leveraging on GDAL.
 * 
 * @author Daniele Romagnoli, GeoSolutions
 * @author Simone Giannecchini, GeoSolutions
 * 
 */
public abstract class BaseGDALGridFormat extends AbstractGridFormat implements
        Format {
    private final static Logger LOGGER = org.geotools.util.logging.Logging
            .getLogger("org.geotools.coverageio.gdal");

    /** The inner {@code ImageReaderSpi} */
    private final ImageReaderSpi spi;

    /**
     * Constructor for the {@code BaseGDALGridFormat}. It is invoked by the
     * underlying implementations.
     * 
     * @param spi
     *                the format specific {@code ImageReaderSpi} instance
     */
    protected BaseGDALGridFormat(ImageReaderSpi spi) {
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
            USE_MT, Boolean.class, new Boolean[] { Boolean.TRUE, Boolean.FALSE }, Boolean.FALSE);

    /**
     * Each plugin needs to implement this method defining format specific
     * properties
     */
    protected abstract void setInfo();

    /**
     * @see org.geotools.data.coverage.grid.AbstractGridFormat#createWriter(java.lang.Object
     *      destination)
     * 
     * Actually, the plugin does not support write capabilities. The method
     * throws an {@code UnsupportedOperationException}.
     */
    @Override
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
    @Override
    public GeoToolsWriteParams getDefaultImageIOWriteParameters() {
        throw new UnsupportedOperationException("This plugin does not support writing parameters");
    }

    /**
     * @see org.geotools.data.coverage.grid.AbstractGridFormat#createWriter(java.lang.Object
     *      destination,Hints hints)
     * 
     * Actually, the plugin does not support write capabilities. The method
     * throws an {@code UnsupportedOperationException}.
     */
    public GridCoverageWriter getWriter(Object destination, Hints hints) {
        throw new UnsupportedOperationException("This plugin does not support writing at this time.");
    }

    /**
     * @see org.geotools.data.coverage.grid.AbstractGridFormat#accepts(java.lang.Object input)
     */
    @Override
    public boolean accepts(Object input,Hints hints) {
        try {
            return spi.canDecodeInput(input);
        } catch (IOException e) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE, e.getLocalizedMessage(), e);
            }
            return false;
        }
    }
    
    /**
     * Return a ParameterGroup with default General Parameter Descriptors.
     * @param mInfo Set of properties info
     * @return a {@link ParameterGroup} with Default {@link GeneralParameterDescriptors}.
     */
    protected static ParameterGroup getDefaultParameterGroup(Map<String,String> mInfo){
        return new ParameterGroup(
                new DefaultParameterDescriptorGroup(mInfo,
                        new GeneralParameterDescriptor[] { 
                                READ_GRIDGEOMETRY2D,
                                USE_JAI_IMAGEREAD, 
                                USE_MULTITHREADING, 
                                SUGGESTED_TILE_SIZE }));
    }
    
    /**
     * @see org.geotools.data.coverage.grid.AbstractGridFormat#getReader(Object
     *      source)
     */
    @Override
    public AbstractGridCoverage2DReader getReader(Object source) {
        return getReader(source, null);
    }

}
