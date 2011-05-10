/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.geotools.grid;

import java.util.List;

import org.geotools.data.DataUtilities;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.grid.ortholine.OrthoLineControl;
import org.geotools.grid.ortholine.OrthoLineGridBuilder;
import org.geotools.referencing.CRS;

import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 *
 * @author michael
 */
public class Lines {

    public static SimpleFeatureSource createLines(ReferencedEnvelope bounds,
            List<OrthoLineControl> controls) {

        return createLines(bounds, 
                new DefaultLineFeatureBuilder(bounds.getCoordinateReferenceSystem()),
                0.0, controls);
    }

    public static SimpleFeatureSource createLines(ReferencedEnvelope bounds,
            double vertexSpacing, List<OrthoLineControl> controls) {

        return createLines(bounds, 
                new DefaultLineFeatureBuilder(bounds.getCoordinateReferenceSystem()),
                vertexSpacing, controls);
    }

    public static SimpleFeatureSource createLines(ReferencedEnvelope bounds, 
            GridFeatureBuilder lineFeatureBuilder,
            double vertexSpacing,
            List<OrthoLineControl> controls) {

        if (bounds == null || bounds.isEmpty() || bounds.isNull()) {
            throw new IllegalArgumentException("The bounds should not be null or empty");
        }

        if (controls == null || controls.isEmpty()) {
            throw new IllegalArgumentException("One or more line controls must be provided");
        }

        CoordinateReferenceSystem boundsCRS = bounds.getCoordinateReferenceSystem();
        CoordinateReferenceSystem builderCRS = 
                lineFeatureBuilder.getType().getCoordinateReferenceSystem();
        if (boundsCRS != null && builderCRS != null &&
                !CRS.equalsIgnoreMetadata(boundsCRS, builderCRS)) {
            throw new IllegalArgumentException("Different CRS set for bounds and the feature builder");
        }

        final SimpleFeatureCollection fc = new ListFeatureCollection(lineFeatureBuilder.getType());
        OrthoLineGridBuilder lineBuilder = new OrthoLineGridBuilder();
        lineBuilder.buildGrid(bounds, controls, lineFeatureBuilder, vertexSpacing, fc);
        return DataUtilities.source(fc);
    }

}
