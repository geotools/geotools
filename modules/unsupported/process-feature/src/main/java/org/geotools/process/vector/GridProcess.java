/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2008-2011 TOPP - www.openplans.org.
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
package org.geotools.process.vector;

import java.io.IOException;
import java.util.Map;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.grid.GridElement;
import org.geotools.grid.GridFeatureBuilder;
import org.geotools.grid.PolygonElement;
import org.geotools.grid.hexagon.HexagonOrientation;
import org.geotools.grid.hexagon.Hexagons;
import org.geotools.grid.oblong.Oblongs;
import org.geotools.process.ProcessException;
import org.geotools.process.factory.DescribeParameter;
import org.geotools.process.factory.DescribeProcess;
import org.geotools.process.factory.DescribeResult;
import org.locationtech.jts.geom.Polygon;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * A process that builds a regular grid as a feature collection
 *
 * @author Andrea Aime - GeoSolutions
 */
@DescribeProcess(
        title = "Grid",
        description =
                "Generates a georeferenced regular grid of cells.  Output contains the attributes: cell - the cell polygon; id - a unique identifier; centerX and centerY - the ordinates of the cell center.")
public class GridProcess implements VectorProcess {

    public enum GridMode {
        Rectangular,
        HexagonFlat,
        HexagonAngled
    };

    @DescribeResult(name = "result", description = "Generated grid cells as features")
    public SimpleFeatureCollection execute(
            @DescribeParameter(name = "bounds", description = "Bounds of the grid")
                    ReferencedEnvelope bounds,
            @DescribeParameter(
                            name = "width",
                            description = "Width of a cell (in units of the grid CRS)")
                    double width,
            @DescribeParameter(
                            name = "height",
                            description =
                                    "Height of a cell (in units of the grid CRS).  Only for rectangular grid, defaults to equal width.",
                            min = 0)
                    Double height,
            @DescribeParameter(
                            name = "vertexSpacing",
                            description =
                                    "Distance between vertices along cell sides (in units of the grid CRS)",
                            min = 0)
                    Double vertexSpacing,
            @DescribeParameter(
                            name = "mode",
                            description =
                                    "Type of grid to be generated.  Specifies shape of cells in grid.",
                            defaultValue = "Rectangular")
                    GridMode mode)
            throws ProcessException {
        final GridFeatureBuilder builder =
                new GridFeatureBuilderImpl(bounds.getCoordinateReferenceSystem());
        double h = height != null ? height : width;

        SimpleFeatureSource source;
        if (mode == null || mode == GridMode.Rectangular) {
            source = Oblongs.createGrid(bounds, width, h, builder);
        } else if (mode == GridMode.HexagonFlat) {
            source = Hexagons.createGrid(bounds, width, HexagonOrientation.FLAT, builder);
        } else {
            source = Hexagons.createGrid(bounds, width, HexagonOrientation.ANGLED, builder);
        }

        try {
            return source.getFeatures();
        } catch (IOException e) {
            throw new ProcessException("Unexpected exception while grabbing features", e);
        }
    }

    /**
     * Builds the feature attributes providing the cell center and a stable id
     *
     * @author Andrea Aime - GeoSolutions
     */
    static final class GridFeatureBuilderImpl extends GridFeatureBuilder {
        private int id;

        /**
         * Creates the feature TYPE
         *
         * @param crs coordinate reference system (may be {@code null})
         * @return the feature TYPE
         */
        protected static SimpleFeatureType createType(CoordinateReferenceSystem crs) {
            SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
            tb.setName("grid");
            tb.add("cell", Polygon.class, crs);
            tb.add("id", Integer.class);
            tb.add("centerX", Double.class);
            tb.add("centerY", Double.class);
            return tb.buildFeatureType();
        }

        /**
         * Creates a new instance.
         *
         * @param crs coordinate reference system (may be {@code null})
         */
        public GridFeatureBuilderImpl(CoordinateReferenceSystem crs) {
            super(createType(crs));
        }

        @Override
        public String getFeatureID(GridElement ge) {
            return String.valueOf("grid." + (id++));
        }

        /**
         * Overrides {@linkplain GridFeatureBuilder#setAttributes(GridElement, Map)} to assign a
         * sequential integer id value to each grid element feature as it is constructed.
         *
         * @param ge the element from which the new feature is being constructed
         * @param attributes a {@code Map} with the single key "id"
         */
        @Override
        public void setAttributes(GridElement ge, Map<String, Object> attributes) {
            PolygonElement pe = (PolygonElement) ge;
            attributes.put("id", id);
            attributes.put("centerX", pe.getCenter().x);
            attributes.put("centerY", pe.getCenter().y);
        }
    }
}
