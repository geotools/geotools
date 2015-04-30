/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.imagemosaic.catalog;

import java.io.IOException;

import org.opengis.feature.simple.SimpleFeature;

import com.vividsolutions.jts.geom.Geometry;

/**
 * A {@link MultiLevelROIProvider} implementation used for returning {@link MultiLevelROIGeometry}s
 */
public class MultiLevelROIGeometryProvider implements MultiLevelROIProvider {

    private final FootprintGeometryProvider geometryProvider;

    private final double inset;

    private final FootprintInsetPolicy insetPolicy;

    public MultiLevelROIGeometryProvider(FootprintGeometryProvider geometryProvider, double inset,
            FootprintInsetPolicy insetPolicy) {
        super();
        this.geometryProvider = geometryProvider;
        this.inset = inset;
        this.insetPolicy = insetPolicy;
    }

    public MultiLevelROI getMultiScaleROI(SimpleFeature sf) throws IOException {
        Geometry footprint = geometryProvider.getFootprint(sf);
        if (footprint == null) {
            return null;
        } else {
            Geometry granuleBounds = (Geometry) sf.getDefaultGeometry();
            return new MultiLevelROIGeometry(footprint, granuleBounds, inset, insetPolicy);
        }
    }

    public void dispose() {
        geometryProvider.dispose();
    }
}
