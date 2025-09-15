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
package org.geotools.coverageio.jp2k;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.spi.ImageReaderSpi;
import javax.imageio.stream.ImageInputStream;
import org.geotools.api.coverage.grid.Format;
import org.geotools.api.coverage.grid.GridCoverageWriter;
import org.geotools.api.geometry.MismatchedDimensionException;
import org.geotools.api.parameter.GeneralParameterDescriptor;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.imageio.GeoToolsWriteParams;
import org.geotools.parameter.DefaultParameterDescriptorGroup;
import org.geotools.parameter.ParameterGroup;
import org.geotools.util.factory.Hints;

/**
 * An implementation of {@link Format} for the JP2K format.
 *
 * @author Daniele Romagnoli, GeoSolutions
 * @author Simone Giannecchini (simboss), GeoSolutions
 */
public final class JP2KFormat extends AbstractGridFormat implements Format {

    /** The inner {@code ImageReaderSpi} */
    private ImageReaderSpi spi = null;

    /** Logger. */
    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(JP2KFormat.class);

    /** Creates an instance and sets the metadata. */
    public JP2KFormat() {
        setInfo();
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Creating a new JP2KFormat.");
        }
    }

    /** Sets the metadata information. */
    protected void setInfo() {
        HashMap<String, String> info = new HashMap<>();
        info.put("name", "JP2K (Direct) ");
        info.put("description", "JP2K (Direct) Coverage Format");
        info.put("vendor", "Geotools");
        info.put("docURL", ""); // TODO: set something
        info.put("version", "1.0");
        mInfo = info;

        // writing parameters
        writeParameters = null;
        readParameters =
                new ParameterGroup(new DefaultParameterDescriptorGroup(mInfo, new GeneralParameterDescriptor[] {
                    READ_GRIDGEOMETRY2D, INPUT_TRANSPARENT_COLOR, USE_JAI_IMAGEREAD, SUGGESTED_TILE_SIZE,
                }));
    }

    /** @see org.geotools.data.coverage.grid.AbstractGridFormat#getReader(Object, Hints) */
    @Override
    public AbstractGridCoverage2DReader getReader(Object source, Hints hints) {
        try {
            return new JP2KReader(source, hints);
        } catch (MismatchedDimensionException | IOException e) {
            if (LOGGER.isLoggable(Level.WARNING)) LOGGER.log(Level.WARNING, e.getLocalizedMessage(), e);
            return null;
        }
    }

    /** @see org.geotools.data.coverage.grid.AbstractGridFormat#getReader(Object) */
    @Override
    public AbstractGridCoverage2DReader getReader(Object source) {
        return getReader(source, null);
    }

    /**
     * @see org.geotools.data.coverage.grid.AbstractGridFormat#createWriter(java.lang.Object destination)
     *     <p>Actually, the plugin does not support write capabilities. The method throws an
     *     {@code UnsupportedOperationException}.
     */
    @Override
    public GridCoverageWriter getWriter(Object destination) {
        throw new UnsupportedOperationException("This plugin does not support writing at this time.");
    }

    /**
     * @see org.geotools.data.coverage.grid.AbstractGridFormat#getDefaultImageIOWriteParameters
     *     <p>Actually, the plugin does not support write capabilities. The method throws an
     *     {@code UnsupportedOperationException}.
     */
    @Override
    public GeoToolsWriteParams getDefaultImageIOWriteParameters() {
        throw new UnsupportedOperationException("This plugin does not support writing parameters");
    }

    /**
     * @see org.geotools.data.coverage.grid.AbstractGridFormat#createWriter(java.lang.Object destination,Hints hints)
     *     <p>Actually, the plugin does not support write capabilities. The method throws an
     *     {@code UnsupportedOperationException}.
     */
    @Override
    public GridCoverageWriter getWriter(Object destination, Hints hints) {
        throw new UnsupportedOperationException("This plugin does not support writing at this time.");
    }

    /** @see org.geotools.data.coverage.grid.AbstractGridFormat#accepts(java.lang.Object input) */
    @Override
    public boolean accepts(Object input, Hints hints) {
        // Directories aren't accepted
        if (input != null && input instanceof File directory) {
            if (!directory.exists() || directory.isDirectory()) return false;
        }
        try (ImageInputStream stream = ImageIO.createImageInputStream(input)) {
            if (spi == null) {
                ImageReader reader = Utils.getReader(stream);
                if (reader != null) spi = reader.getOriginatingProvider();
                else return false;
            }
            return spi.canDecodeInput(stream);
        } catch (IOException e) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE, e.getLocalizedMessage(), e);
            }
            return false;
        }
    }
}
