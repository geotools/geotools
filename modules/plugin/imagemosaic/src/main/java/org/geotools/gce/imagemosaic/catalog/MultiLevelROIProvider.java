package org.geotools.gce.imagemosaic.catalog;

import java.io.IOException;

import org.opengis.feature.simple.SimpleFeature;

import com.vividsolutions.jts.geom.Geometry;

public class MultiLevelROIProvider {

    private final FootprintGeometryProvider geometryProvider;

    private final double inset;

    private final FootprintInsetPolicy insetPolicy;

    public MultiLevelROIProvider(FootprintGeometryProvider geometryProvider, double inset,
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
            return new MultiLevelROI(footprint, granuleBounds, inset, insetPolicy);
        }
    }

    public void dispose() {
        geometryProvider.dispose();
    }
}
