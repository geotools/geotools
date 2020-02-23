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

import java.io.IOException;
import org.locationtech.jts.geom.Geometry;
import org.opengis.feature.simple.SimpleFeature;

/**
 * A {@link MultiLevelROIProvider} implementation used for returning {@link MultiLevelROIGeometry}s
 */
public class MultiLevelROIGeometryProvider implements MultiLevelROIProvider {

    private final FootprintGeometryProvider geometryProvider;

    private final double inset;

    /** The FootprintInsetPolicy if any for inset management */
    private final FootprintInsetPolicy insetPolicy;

    /**
     * The optional granuleBounds to be used as a default value when there is no need to do ROI
     * lookup against specific features. Specify that when dealing with single granules store
     */
    private final Geometry defaultGranuleBounds;

    /** Geometry provider constructor based on a fixed granuleBounds */
    public MultiLevelROIGeometryProvider(
            FootprintGeometryProvider geometryProvider,
            double inset,
            FootprintInsetPolicy insetPolicy,
            Geometry defaultGranuleBounds) {
        this.geometryProvider = geometryProvider;
        this.inset = inset;
        this.insetPolicy = insetPolicy;
        this.defaultGranuleBounds = defaultGranuleBounds;
    }

    /** Geometry provider constructor based on per feature granuleBounds */
    public MultiLevelROIGeometryProvider(
            FootprintGeometryProvider geometryProvider,
            double inset,
            FootprintInsetPolicy insetPolicy) {
        this(geometryProvider, inset, insetPolicy, null);
    }

    @Override
    public MultiLevelROI getMultiScaleROI(SimpleFeature sf) throws IOException {
        Geometry footprint = geometryProvider.getFootprint(sf);
        if (footprint == null) {
            return null;
        } else {
            Geometry granuleBounds =
                    defaultGranuleBounds != null
                            ? defaultGranuleBounds
                            : (Geometry) sf.getDefaultGeometry();
            return new MultiLevelROIGeometry(footprint, granuleBounds, inset, insetPolicy);
        }
    }

    public void dispose() {
        geometryProvider.dispose();
    }
}
