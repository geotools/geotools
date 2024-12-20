/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015 - 2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.grid.io.footprint;

import com.sun.media.jai.operator.ImageReadDescriptor;
import it.geosolutions.imageio.maskband.DatasetLayout;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.RenderedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.media.jai.ROI;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.coverage.grid.io.imageio.MaskOverviewProvider;
import org.geotools.coverage.grid.io.imageio.MaskOverviewProvider.MaskInfo;
import org.geotools.coverage.grid.io.imageio.ReadType;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.util.logging.Logging;
import org.locationtech.jts.geom.Geometry;

/**
 * {@link MultiLevelROI} implementation supporting Raster masking. Notice that actually it does not support Inset
 * definition.
 *
 * @author Nicola Lagomarsini GeoSolutions
 */
public class MultiLevelROIRaster implements MultiLevelROI {

    /** {@link Logger} used for logging exceptions */
    private static final Logger LOGGER = Logging.getLogger(MultiLevelROIRaster.class);

    /** Bounding Box of the mask */
    private Geometry footprint;

    /** Envelope of the mask */
    private ReferencedEnvelope env;

    /** {@link MaskOverviewProvider} instance used for handling internal/External Masks */
    private MaskOverviewProvider maskOvrProvider;

    public MultiLevelROIRaster(DatasetLayout layout, MaskOverviewProvider maskOvrProvider, SimpleFeature sf)
            throws IOException {
        // Getting Feature Geometry
        Geometry geo = (Geometry) sf.getDefaultGeometry();
        // Getting as envelope
        env = JTS.toEnvelope(geo);
        // Save envelope as Geometry
        footprint = JTS.toGeometry(env);
        this.maskOvrProvider = maskOvrProvider;
    }

    @Override
    // if delayed read we should not close immediately
    @SuppressWarnings({"PMD.CloseResource", "PMD.UseTryWithResources"})
    public ROI getTransformedROI(
            AffineTransform at, int imageIndex, Rectangle imgBounds, ImageReadParam params, ReadType readType) {
        // Getting MaskInfo
        MaskInfo info = maskOvrProvider.getMaskInfo(imageIndex, imgBounds, params);
        // Defining imageIndex based on the imageIndex
        int index = info.index;

        // Getting input stream and reader from File
        ImageInputStream inStream = null;
        ImageReader reader = null;
        try {
            // Getting input Stream
            inStream = maskOvrProvider.getMaskStream(info);
            // Getting Reader
            reader = info.readerSpi.createReaderInstance();
            // Setting input
            reader.setInput(inStream, false, false);
            // Reading file
            RenderedImage raster = null;
            if (readType.equals(ReadType.DIRECT_READ)) {
                // read data directly
                raster = reader.read(index, info.readParameters);
            } else {
                // read data
                inStream.seek(0);
                raster = ImageReadDescriptor.create(
                        inStream, index, false, false, false, null, null, info.readParameters, reader, null);
            }
            return MaskOverviewProvider.scaleROI(raster, imgBounds);
        } catch (IOException e) {
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, e.getMessage(), e);
            }
        } finally {
            if (readType != ReadType.JAI_IMAGEREAD && reader != null) {
                try {
                    reader.dispose();
                } catch (Exception e) {
                    if (LOGGER.isLoggable(Level.SEVERE)) {
                        LOGGER.log(Level.SEVERE, e.getMessage(), e);
                    }
                }
            }
            if (readType != ReadType.JAI_IMAGEREAD && inStream != null) {
                try {
                    inStream.close();
                } catch (Exception e) {
                    if (LOGGER.isLoggable(Level.SEVERE)) {
                        LOGGER.log(Level.SEVERE, e.getMessage(), e);
                    }
                }
            }
        }
        return null;
    }

    @Override
    public boolean isEmpty() {
        return env.isEmpty();
    }

    @Override
    public Geometry getFootprint() {
        return footprint;
    }
}
