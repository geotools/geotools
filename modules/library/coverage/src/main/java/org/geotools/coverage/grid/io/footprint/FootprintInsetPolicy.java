/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2013 - 2016, Open Source Geospatial Foundation (OSGeo)
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

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.geotools.util.Converters;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryComponentFilter;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.operation.union.CascadedPolygonUnion;

/**
 * Policy specifying how to apply an inset of the footprints
 *
 * @author Andrea Aime - GeoSolutions
 */
public enum FootprintInsetPolicy {

    /** Full inset from all direction. Works best with fully overlapping granules */
    full {
        @Override
        public Geometry applyInset(Geometry footprint, Geometry granuleBounds, double inset) {
            if (footprint == null) {
                return null;
            }
            return footprint.buffer(-inset);
        }
    },
    /**
     * Applies the inset only on the footprint sections that are not lying about the granule bounds
     * (assuming the granules are cut in a regular grid and meant to be displayed side by side (no
     * overlap)
     */
    border {
        @Override
        public Geometry applyInset(Geometry footprint, Geometry granuleBounds, double inset) {
            if (footprint != null) {
                // we buffer only the portions of the footprint that are not overlapping with
                // the granule bounds, and remove them from the footprint
                List<LinearRing> boundRings = getRings(granuleBounds);
                List<LinearRing> footprintRings = getRings(footprint);
                Geometry bufferedOuterRings = buffer(boundRings, Math.max(inset / 100, 1e-9));
                List<LineString> internalBorders = filterRings(footprintRings, bufferedOuterRings);
                if (!internalBorders.isEmpty()) {
                    Geometry bufferedInternalRings = buffer(internalBorders, inset);
                    Geometry difference = footprint.difference(bufferedInternalRings);
                    footprint = collectPolygons(difference);
                }
            }

            return footprint;
        }

        /**
         * Collects all sub-polygons into the specified geometry and returns them either as a single
         * polygon, or as a multipolygon, shaving off any other lower dimension geometry
         */
        private Geometry collectPolygons(Geometry geometry) {
            if (geometry.isEmpty()) {
                return geometry;
            }

            final List<Polygon> polygons = new ArrayList<Polygon>();
            geometry.apply(
                    new GeometryComponentFilter() {

                        @Override
                        public void filter(Geometry geom) {
                            if (geom instanceof Polygon && !geom.isEmpty()) {
                                polygons.add((Polygon) geom);
                            }
                        }
                    });

            if (polygons.isEmpty()) {
                return geometry.getFactory().createMultiPolygon(new Polygon[0]);
            } else if (polygons.size() == 1) {
                return polygons.get(0);
            } else {
                Polygon[] array = (Polygon[]) polygons.toArray(new Polygon[polygons.size()]);
                return array[0].getFactory().createMultiPolygon(array);
            }
        }

        private List<LineString> filterRings(
                List<LinearRing> footprintRings, Geometry bufferedOuterRings) {
            List<LineString> result = new ArrayList<LineString>();
            for (LinearRing ring : footprintRings) {
                Geometry difference = ring.difference(bufferedOuterRings);
                if (difference != null) {
                    collectLines(difference, result);
                }
            }

            return result;
        }

        private Geometry buffer(List<? extends Geometry> geometries, double distance) {
            List<Geometry> polygons = new ArrayList<Geometry>();
            for (Geometry g : geometries) {
                Geometry buffered = g.buffer(distance);
                polygons.add(buffered);
            }

            return CascadedPolygonUnion.union(polygons);
        }

        private List<LinearRing> getRings(Geometry bounds) {
            final ArrayList<LinearRing> rings = new ArrayList<LinearRing>();
            bounds.apply(
                    new GeometryComponentFilter() {

                        @Override
                        public void filter(Geometry geom) {
                            if (geom instanceof LinearRing && !geom.isEmpty()) {
                                rings.add((LinearRing) geom);
                            }
                        }
                    });
            return rings;
        }

        private void collectLines(Geometry geometry, final List<LineString> lines) {
            geometry.apply(
                    new GeometryComponentFilter() {

                        @Override
                        public void filter(Geometry geom) {
                            if (geom instanceof LineString && !geom.isEmpty()) {
                                lines.add((LineString) geom);
                            }
                        }
                    });
        }
    };

    public static final String INSET_PROPERTY = "footprint_inset";

    public static final String INSET_TYPE_PROPERTY = "footprint_inset_type";

    public abstract Geometry applyInset(Geometry footprint, Geometry granuleBounds, double inset);

    /** Returns the list of names for this enum */
    public static List<String> names() {
        FootprintInsetPolicy[] values = FootprintInsetPolicy.values();
        List<String> names = new ArrayList<String>(values.length);

        for (int i = 0; i < values.length; i++) {
            names.add(values[i].name());
        }

        return names;
    }

    public static FootprintInsetPolicy getInsetPolicy(Properties properties) {
        String insetTypeValue = (String) properties.get(INSET_TYPE_PROPERTY);
        if (insetTypeValue == null || insetTypeValue.trim().isEmpty()) {
            return FootprintInsetPolicy.border;
        } else {
            try {
                return FootprintInsetPolicy.valueOf(insetTypeValue.trim());
            } catch (Exception e) {
                throw new IllegalArgumentException(
                        "Invalid inset type '"
                                + insetTypeValue
                                + "', valid values are: "
                                + FootprintInsetPolicy.names());
            }
        }
    }

    public static double getInset(Properties properties) {
        String inset = (String) properties.get(INSET_PROPERTY);
        if (inset == null) {
            return 0;
        }
        Double converted = Converters.convert(inset, Double.class);
        if (converted == null) {
            throw new IllegalArgumentException(
                    "Invalid inset value, should be a "
                            + "floating point number, but instead it is: '"
                            + inset
                            + "'");
        }
        return converted;
    }
}
