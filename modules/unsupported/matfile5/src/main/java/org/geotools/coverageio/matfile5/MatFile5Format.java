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
package org.geotools.coverageio.matfile5;

import it.geosolutions.imageio.matfile5.sas.SASTileImageReaderSpi;

import java.util.Collections;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.coverage.grid.io.imageio.GeoToolsWriteParams;
import org.geotools.data.DataSourceException;
import org.geotools.factory.Hints;
import org.geotools.parameter.DefaultParameterDescriptorGroup;
import org.geotools.parameter.ParameterGroup;
import org.opengis.coverage.grid.Format;
import org.opengis.parameter.GeneralParameterDescriptor;

/**
 * A class implementing {@link Format} to handle Matlab Files version 5.
 * 
 * @author Daniele Romagnoli, GeoSolutions
 * @author Simone Giannecchini, GeoSolutions
 * @since 2.7.x
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/unsupported/matfile5/src/main/java/org/geotools/coverageio/matfile5/MatFile5Format.java $
 */
public class MatFile5Format extends BaseMatFileGridFormat implements Format {
    private final static Logger LOGGER = org.geotools.util.logging.Logging.getLogger(MatFile5Format.class.toString());
    
    /**
     * Constructor for the {@code SASTileFormat}. It is invoked by the
     * underlying implementations.
     * 
     * @param spi
     *                the format specific {@code ImageReaderSpi} instance
     */
    public MatFile5Format() {
        super(new SASTileImageReaderSpi());

        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Creating a new MatFile5Format.");
        }

        setInfo();
    }




    /**
     * @see org.geotools.data.coverage.grid.AbstractGridFormat#getDefaultImageIOWriteParameters
     * 
     * Actually, the plugin does not support write capabilities. The method
     * throws an {@code UnsupportedOperationException}.
     */
    @Override
    public GeoToolsWriteParams getDefaultImageIOWriteParameters() {
        throw new UnsupportedOperationException(
                "This plugin does not support writing parameters");
    }


    @Override
    public AbstractGridCoverage2DReader getReader(Object source, Hints hints) {
        try {
            return new MatFile5Reader(source, hints);
        } catch (DataSourceException e) {
            final RuntimeException re = new RuntimeException();
            re.initCause(e);
            throw re;
        }
    }
    
    /**
     * Sets the metadata information.
     */
    protected void setInfo() {
        final HashMap<String, String> info = new HashMap<String, String>();
        info.put("name", "MatFile5");
        info.put("description", "MatFile5 generated Format");
        info.put("vendor", "Geotools");
        info.put("docURL", ""); // TODO: set something
        info.put("version", "1.0");
        mInfo = Collections.unmodifiableMap(info);

        // writing parameters
        writeParameters = null;

        // reading parameters
        readParameters = new ParameterGroup(
                new DefaultParameterDescriptorGroup(mInfo,
                        new GeneralParameterDescriptor[] { READ_GRIDGEOMETRY2D}));
    }

}
