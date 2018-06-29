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
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.media.jai.ROI;
import org.geotools.coverage.grid.io.imageio.MaskOverviewProvider;
import org.geotools.coverage.grid.io.imageio.MaskOverviewProvider.MaskInfo;
import org.geotools.coverage.grid.io.imageio.ReadType;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.util.URLs;
import org.geotools.util.logging.Logging;
import org.locationtech.jts.geom.Geometry;
import org.opengis.feature.simple.SimpleFeature;

/**
 * {@link MultiLevelROI} implementation supporting Raster masking. Notice that actually it does not
 * support Inset definition.
 *
 * @author Nicola Lagomarsini GeoSolutions
 */
public class MultiLevelROIRaster implements MultiLevelROI {

    /** {@link Logger} used for logging exceptions */
    private static final Logger LOGGER = Logging.getLogger(MultiLevelROIRaster.class);

    /** Input File from where we load internal Masks */
    private File file;

    /** Bounding Box of the mask */
    private Geometry footprint;

    /** Envelope of the mask */
    private ReferencedEnvelope env;

    /** {@link MaskOverviewProvider} instance used for handling internal/External Masks */
    private MaskOverviewProvider maskOvrProvider;

    public MultiLevelROIRaster(DatasetLayout layout, File file, SimpleFeature sf)
            throws IOException {
        // Initialization
        this.file = file;
        // Getting Feature Geometry
        Geometry geo = (Geometry) sf.getDefaultGeometry();
        // Getting as envelope
        env = JTS.toEnvelope(geo);
        // Save envelope as Geometry
        footprint = JTS.toGeometry(env);
        // Getting the Mask provider
        maskOvrProvider = new MaskOverviewProvider(layout, file);
    }

    public ROI getTransformedROI(
            AffineTransform at,
            int imageIndex,
            Rectangle imgBounds,
            ImageReadParam params,
            ReadType readType) {
        // Getting MaskInfo
        MaskInfo info = maskOvrProvider.getMaskInfo(imageIndex, imgBounds, params);
        // Define which File must be used for reading mask info
        File inFile = info.file;
        // Defining imageIndex based on the imageIndex
        int index = info.index;

        // No file found?
        if (inFile == null) {
            throw new IllegalArgumentException(
                    "Unable to load Raster Footprint for granule: " + file.getAbsolutePath());
        }
        URL granuleUrl = URLs.fileToUrl(inFile);
        // Getting input stream and reader from File
        ImageInputStream inStream = null;
        ImageReader reader = null;
        try {
            // Getting input Stream
            inStream =
                    info.streamSpi.createInputStreamInstance(
                            granuleUrl, ImageIO.getUseCache(), ImageIO.getCacheDirectory());
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
                raster =
                        ImageReadDescriptor.create(
                                inStream,
                                index,
                                false,
                                false,
                                false,
                                null,
                                null,
                                info.readParameters,
                                reader,
                                null);
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
