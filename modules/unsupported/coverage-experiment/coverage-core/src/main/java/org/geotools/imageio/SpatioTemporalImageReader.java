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
package org.geotools.imageio;

import java.io.IOException;

import javax.imageio.ImageReader;
import javax.imageio.spi.ImageReaderSpi;

import org.geotools.imageio.metadata.SpatioTemporalMetadata;

/**
 * Class leveraging on an underlying imageio flat reader to extract
 * spatio-temporal information and setup proper spatio-temporal metadata. A flat
 * reader simply allows to get data access as well as exposing basic metadata
 * available for the underlying source. Basic metadata are usually, dataset
 * width/height, name, tiling and similar things. Moreover a flat reader may
 * returns attributes and properties of a whole data source by means of a
 * StreamMetadata instance, as well as a set of "single dataset" specific
 * attributes on which the spatioTemporal reader may leverage to expose spatio-
 * temporal metadata.
 * 
 * @author Daniele Romagnoli, GeoSolutions
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/unsupported/coverage-experiment/coverage-core/src/main/java/org/geotools/imageio/SpatioTemporalImageReader.java $
 */
public abstract class SpatioTemporalImageReader extends ImageReader {

    protected SpatioTemporalImageReader(ImageReaderSpi originatingProvider) {
        super(originatingProvider);
    }

    /**
     * Returns the {@link SliceDescriptor} describing a specified 2D raster
     * 
     * @param imageIndex
     *                the index of the required 2D raster
     * @return a {@link SliceDescriptor}object.
     * @throws IOException
     */
    public abstract SliceDescriptor getSliceDescriptor(int imageIndex) throws IOException;

    /**
     * Returns a {@link SpatioTemporalMetadata} instance for a specified 2D
     * raster
     * 
     * @param imageIndex
     *                the index of the required 2D raster
     * @return a {@link SpatioTemporalMetadata} object.
     */
    public abstract SpatioTemporalMetadata getSpatioTemporalMetadata(int imageIndex);
}
