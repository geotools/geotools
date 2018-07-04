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

import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import javax.imageio.ImageReadParam;
import javax.media.jai.ROI;
import org.geotools.coverage.grid.io.imageio.ReadType;
import org.locationtech.jts.geom.Geometry;

/**
 * A ROI provider that handles multi-scale ROI
 *
 * @author Andrea Aime - GeoSolutions
 */
public interface MultiLevelROI {

    /**
     * Returns a transformed ROI based on the input parameters
     *
     * @param at AffineTransformation
     * @param imageIndex Overview level used for extracting the correct image overview
     * @param imgBounds ImageBounds to set for Raster ROIs
     * @param readType {@link ReadType} object indicating how the image file must be read. This may
     *     be useful for raster ROIs
     * @return a {@link ROI} object
     */
    public ROI getTransformedROI(
            AffineTransform at,
            int imageIndex,
            Rectangle imgBounds,
            ImageReadParam params,
            ReadType readType);

    /**
     * Checks if the provided {@link MultiLevelROI} object is empty or not
     *
     * @return a boolean indicating if this object is empty
     */
    public boolean isEmpty();

    /**
     * This method returns a {@link Geometry} object containing the ROI footprint or, at least, its
     * bounding box
     *
     * @return a {@link Geometry} object defining ROI bounds
     */
    public Geometry getFootprint();
}
