/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2020, Open Source Geospatial Foundation (OSGeo)
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

import java.io.IOException;
import javax.imageio.ImageReader;
import javax.imageio.spi.ImageInputStreamSpi;
import javax.imageio.spi.ImageReaderSpi;
import javax.imageio.stream.ImageInputStream;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.imageio.MaskOverviewProvider;
import org.geotools.util.factory.Hints;

/**
 * An interface providing multiple types of entities involved when getting access to a Granule.
 *
 * <p>
 *
 * <ul>
 *   <li>An ImageReaderSpi to create an ImageReader to decode that granule
 *   <li>An ImageInputStreamSpi to create a stream for the granule
 *   <li>An AbstractGridFormat supporting the type of format of that granule
 *   <li>An AbstractGridCoverage2DReader able to read that granule
 *   <li>A MaskOverviewProvider with details on mask and overviews for that granule
 * </ul>
 */
public interface GranuleAccessProvider {

    Hints.Key SUGGESTED_READER_SPI = new Hints.Key(ImageReaderSpi.class);

    Hints.Key SUGGESTED_STREAM_SPI = new Hints.Key(ImageInputStreamSpi.class);

    Hints.Key SUGGESTED_FORMAT = new Hints.Key(AbstractGridFormat.class);

    Hints.Key GRANULE_ACCESS_PROVIDER = new Hints.Key(GranuleAccessProvider.class);

    /** Input to be set before invoking any method of the provider */
    void setGranuleInput(Object input) throws IOException;

    /** Return the AbstractGridFormat supporting the type of the input granule */
    AbstractGridFormat getFormat() throws IOException;

    /** Return a GridCoverageReader supporting the type of the input granule */
    AbstractGridCoverage2DReader getGridCoverageReader() throws IOException;

    /** Return an ImageInputStreamSpi to create streams on the input granule */
    ImageInputStreamSpi getInputStreamSpi() throws IOException;

    /** Return an ImageReaderSpi to create readers on the input granule */
    ImageReaderSpi getImageReaderSpi() throws IOException;

    MaskOverviewProvider getMaskOverviewsProvider() throws IOException;

    ImageInputStream getImageInputStream() throws IOException;

    ImageReader getImageReader() throws IOException;

    /** Get a copy of this GranuleAccessProviders with same SPIs, factories and config */
    GranuleAccessProvider copyProviders();
}
