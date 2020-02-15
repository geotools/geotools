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

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.ParameterBlock;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.spi.ImageReaderSpi;
import javax.imageio.stream.ImageInputStream;
import javax.media.jai.ImageLayout;
import javax.media.jai.JAI;
import javax.media.jai.RenderedOp;
import org.geotools.coverage.util.CoverageUtilities;
import org.geotools.metadata.i18n.ErrorKeys;
import org.geotools.metadata.i18n.Errors;

/**
 * This enum can be used to distinguish between differet read methods, namely, JAI ImageRead based
 * and Java2D direct read via ImageReader.
 *
 * @author Simone Giannecchini, GeoSolutions SAS
 */
enum ReadType {
    DIRECT_READ {
        RenderedImage read(
                final ImageReadParam readP,
                final int imageIndex,
                final File rasterFile,
                final Rectangle readDimension,
                final Dimension tileDimension, // we just ignore in this case
                final ImageReaderSpi spi)
                throws IOException {
            //
            // Using ImageReader to load the data directly
            //
            ImageInputStream inStream = null;
            ImageReader reader = null;
            try {
                inStream = Utils.getInputStream(rasterFile);
                if (inStream == null) return null;

                reader = spi.createReaderInstance();
                if (reader == null) {
                    if (LOGGER.isLoggable(Level.WARNING))
                        LOGGER.warning(
                                "Unable to get reader for file " + rasterFile.getAbsolutePath());
                    return null;
                }

                inStream.reset();
                reader.setInput(inStream);

                // check source regione
                if (CoverageUtilities.checkEmptySourceRegion(readP, readDimension)) return null;

                if (LOGGER.isLoggable(Level.FINE))
                    LOGGER.log(Level.FINE, "reading file: " + rasterFile.getAbsolutePath());

                // read data
                return reader.read(imageIndex, readP);
            } catch (IOException e) {
                if (LOGGER.isLoggable(Level.WARNING))
                    LOGGER.log(
                            Level.WARNING,
                            "Unable to compute source area for file "
                                    + rasterFile.getAbsolutePath(),
                            e);
                return null;
            } finally {
                // close everything
                try {
                    // reader
                    reader.dispose();
                } catch (Throwable t) {
                    // swallow the exception, we are just trying to close as much stuff as possible
                }

                try {
                    // instream
                    inStream.close();
                } catch (Throwable t) {
                    // swallow the exception, we are just trying to close as much stuff as possible
                }
            }
        }
    },

    JAI_IMAGEREAD {
        @Override
        RenderedImage read(
                final ImageReadParam readP,
                final int imageIndex,
                final File rasterFile,
                final Rectangle readDimension,
                final Dimension tileDimension,
                final ImageReaderSpi spi)
                throws IOException {

            ///
            // Using ImageReader to load the data directly
            //
            ImageInputStream inStream = null;
            ImageReader reader = null;
            try {
                // get stream
                inStream = Utils.getInputStream(rasterFile);
                if (inStream == null) return null;
                // get a reader
                reader = spi.createReaderInstance();
                if (reader == null) {
                    if (LOGGER.isLoggable(Level.WARNING))
                        LOGGER.warning(
                                "Unable to get reader for file " + rasterFile.getAbsolutePath());
                    return null;
                }

                inStream.reset();
                reader.setInput(inStream);

                // check source region
                if (CoverageUtilities.checkEmptySourceRegion(readP, readDimension)) return null;

            } catch (IOException e) {
                if (LOGGER.isLoggable(Level.WARNING))
                    LOGGER.log(
                            Level.WARNING,
                            "Unable to compute source area for file "
                                    + rasterFile.getAbsolutePath(),
                            e);
                return null;
            } finally {
                // close everything
                try {
                    // reader
                    if (reader != null) reader.dispose();
                } catch (Throwable t) {
                    // swallow the exception, we are just trying to close as much stuff as possible
                }

                try {
                    // instream
                    if (inStream != null) inStream.close();
                } catch (Throwable t) {
                    // swallow the exception, we are just trying to close as much stuff as possible
                }
            }

            // read data
            final ParameterBlock pbjImageRead = new ParameterBlock();
            pbjImageRead.add(rasterFile);
            pbjImageRead.add(imageIndex);
            pbjImageRead.add(false);
            pbjImageRead.add(false);
            pbjImageRead.add(false);
            pbjImageRead.add(null);
            pbjImageRead.add(null);
            pbjImageRead.add(readP);
            pbjImageRead.add(spi.createReaderInstance());
            final RenderedOp raster;
            if (tileDimension != null) {
                // build a proper layout
                final ImageLayout layout = new ImageLayout();
                layout.setTileWidth(tileDimension.width).setTileHeight(tileDimension.height);
                raster =
                        JAI.create(
                                "ImageRead",
                                pbjImageRead,
                                new RenderingHints(JAI.KEY_IMAGE_LAYOUT, layout));
            } else raster = JAI.create("ImageRead", pbjImageRead);
            // force rendering (a-la JAI)
            if (raster != null) raster.getWidth();
            return raster;
        }
    },

    JAI_IMAGEREAD_MT {
        @Override
        RenderedImage read(
                final ImageReadParam readP,
                final int imageIndex,
                final File rasterFile,
                final Rectangle readDimension,
                final Dimension tileDimension,
                final ImageReaderSpi spi)
                throws IOException {

            ///
            // Using ImageReader to load the data directly
            //
            ImageInputStream inStream = null;
            ImageReader reader = null;
            try {
                // get stream
                inStream = Utils.getInputStream(rasterFile);
                if (inStream == null) return null;
                // get a reader
                reader = spi.createReaderInstance();
                if (reader == null) {
                    if (LOGGER.isLoggable(Level.WARNING))
                        LOGGER.warning(
                                "Unable to get reader for file " + rasterFile.getAbsolutePath());
                    return null;
                }

                inStream.reset();
                reader.setInput(inStream);

                // check source region
                if (CoverageUtilities.checkEmptySourceRegion(readP, readDimension)) return null;

            } catch (IOException e) {
                if (LOGGER.isLoggable(Level.WARNING))
                    LOGGER.log(
                            Level.WARNING,
                            "Unable to compute source area for file "
                                    + rasterFile.getAbsolutePath(),
                            e);
                return null;
            } finally {
                // close everything
                try {
                    // reader
                    if (reader != null) reader.dispose();
                } catch (Throwable t) {
                    // swallow the exception, we are just trying to close as much stuff as possible
                }

                try {
                    // instream
                    if (inStream != null) inStream.close();
                } catch (Throwable t) {
                    // swallow the exception, we are just trying to close as much stuff as possible
                }
            }

            // read data
            final ParameterBlock pbjImageRead = new ParameterBlock();
            pbjImageRead.add(rasterFile);
            pbjImageRead.add(imageIndex);
            pbjImageRead.add(false);
            pbjImageRead.add(false);
            pbjImageRead.add(false);
            pbjImageRead.add(null);
            pbjImageRead.add(null);
            pbjImageRead.add(readP);
            pbjImageRead.add(spi.createReaderInstance());
            final RenderedOp raster;
            if (tileDimension != null) {
                // build a proper layout
                final ImageLayout layout = new ImageLayout();
                layout.setTileWidth(tileDimension.width).setTileHeight(tileDimension.height);
                raster =
                        JAI.create(
                                "ImageReadMT",
                                pbjImageRead,
                                new RenderingHints(JAI.KEY_IMAGE_LAYOUT, layout));
            } else raster = JAI.create("ImageReadMT", pbjImageRead);
            // force rendering (a-la JAI)
            if (raster != null) raster.getWidth();
            return raster;
        }
    },

    UNSPECIFIED {

        @Override
        RenderedImage read(
                final ImageReadParam readP,
                final int imageIndex,
                final File rasterFile,
                final Rectangle readDimension,
                final Dimension tileDimension,
                final ImageReaderSpi spi)
                throws IOException {
            throw new UnsupportedOperationException(
                    Errors.format(ErrorKeys.UNSUPPORTED_OPERATION_$1, "read"));
        }
    };

    /** Logger. */
    private static final Logger LOGGER =
            org.geotools.util.logging.Logging.getLogger(ReadType.class);

    /**
     * Default {@link ReadType} enumeration.
     *
     * <p>We use the JAI ImageRead as the default type so that we can be sure that we can read very
     * large images with deferred loading.
     *
     * @return the default {@link ReadType}.
     */
    static ReadType getDefault() {
        return JAI_IMAGEREAD;
    }

    /**
     * Load the raster data from the underlying source with the specified read type.
     *
     * @param tileDimension a {@link Dimension} object that can be used to suggest specific tile
     *     dimension for the raster to load. It can be <code>null</code>.
     * @return a {@link RenderedImage} instance that matches the provided request parameters as
     *     close as possible.
     * @throws IOException in case something bad occurs during the decoding process.
     */
    abstract RenderedImage read(
            final ImageReadParam readParameters,
            final int imageIndex,
            final File rasterFile,
            final Rectangle readDimension,
            final Dimension tileDimension,
            final ImageReaderSpi spi)
            throws IOException;
}
