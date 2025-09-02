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
package org.geotools.gce.imagemosaic;

import it.geosolutions.imageio.utilities.ImageIOUtilities;
import it.geosolutions.imageioimpl.plugins.tiff.TIFFImageReaderSpi;
import java.awt.RenderingHints;
import java.util.Collections;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.spi.ImageReaderSpi;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridFormatFactorySpi;

/**
 * Implementation of the GridCoverageFormat service provider interface for mosaic of georeferenced images.
 *
 * @author Simone Giannecchini, GeoSolutions S.A.S.
 * @since 2.3
 */
public final class ImageMosaicFormatFactory implements GridFormatFactorySpi {

    private static final String GDAL_JP2ECW_SPI = "it.geosolutions.imageio.plugins.jp2ecw.JP2GDALEcwImageReaderSpi";

    private static final String GDAL_JP2KAKADU_SPI =
            "it.geosolutions.imageio.plugins.jp2kakadu.JP2GDALKakaduImageReaderSpi";

    private static final String GDAL_JP2MrSID_SPI =
            "it.geosolutions.imageio.plugins.jp2mrsid.JP2GDALMrSidImageReaderSpi";

    private static final String GDAL_SPI = "it.geosolutions.imageio.gdalframework.GDALImageReaderSpi";

    private static final String KAKADU_SPI = "it.geosolutions.imageio.plugins.jp2k.JP2KKakaduImageReaderSpi";

    /** Logger. */
    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(ImageMosaicFormatFactory.class);

    static {
        replaceTIFF();
    }

    private static void replaceTIFF() {
        try {
            // check if our tiff plugin is in the path
            final String customTiffName = it.geosolutions.imageioimpl.plugins.tiff.TIFFImageReaderSpi.class.getName();
            Class.forName(customTiffName);

            // imageio tiff reader
            final String imageioTiffName = TIFFImageReaderSpi.class.getName();

            final boolean succeeded =
                    ImageIOUtilities.replaceProvider(ImageReaderSpi.class, customTiffName, imageioTiffName, "tiff");
            if (!succeeded)
                if (LOGGER.isLoggable(Level.WARNING)) LOGGER.warning("Unable to set ordering between tiff readers spi");

        } catch (ClassNotFoundException e) {
            if (LOGGER.isLoggable(Level.WARNING))
                LOGGER.log(Level.WARNING, "Unable to load specific TIFF reader spi", e);
        }
    }

    /** @see GridFormatFactorySpi#createFormat(). */
    @Override
    public AbstractGridFormat createFormat() {
        return new ImageMosaicFormat();
    }

    /**
     * Returns the implementation hints. The default implementation returns an empty map.
     *
     * @return An empty map.
     */
    @Override
    public Map<RenderingHints.Key, ?> getImplementationHints() {
        return Collections.emptyMap();
    }

    /**
     * Tells me if this plugin will work on not given the actual installation.
     *
     * <p>Dependecies are mostly from JAI and ImageIO so if they are installed you should not have many problems.
     *
     * @return False if something's missing, true otherwise.
     */
    @Override
    public boolean isAvailable() {
        boolean available = true;

        // if these classes are here, then the runtine environment has
        // access to JAI and the JAI ImageI/O toolbox.
        try {
            Class.forName("org.eclipse.imagen.JAI");
            Class.forName("org.eclipse.imagen.media.imageread.ImageReadDescriptor");
        } catch (ClassNotFoundException cnf) {
            available = false;
        }

        return available;
    }
}
