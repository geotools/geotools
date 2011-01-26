/*
 *    GeoTools - The Open Source Java GIS Tookit
 *    http://geotools.org
 *
 *    (C) 2010, Open Source Geospatial Foundation (OSGeo)
 *
 *    This file is hereby placed into the Public Domain. This means anyone is
 *    free to do whatever they wish with this file. Use it well and enjoy!
 */
package org.geotools.demo.grid;

import java.io.IOException;
import java.net.URL;
import java.util.Map;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Polygon;
import java.awt.Color;

import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.grid.Envelopes;
import org.geotools.grid.GridElement;
import org.geotools.grid.GridFeatureBuilder;
import org.geotools.grid.Grids;
import org.geotools.map.DefaultMapContext;
import org.geotools.map.MapContext;
import org.geotools.styling.SLD;
import org.geotools.swing.JMapFrame;

import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;

/**
 * This example shows how to control the creation of vector grid elements
 * by overriding the GridFeatureBuilder.getCreateFeature() method. It reads
 * a shapefile containing a map of Australia and creates a grid of hexagons
 * whose centers lie within the country boundary.
 *
 * @author Michael Bedward
 */
public class IntersectionExample {

    public static void main(String[] args) throws Exception {
        URL url = IntersectionExample.class.getResource("/data/shapefiles/oz.shp");
        FileDataStore dataStore = FileDataStoreFinder.getDataStore(url);
        SimpleFeatureSource ozMapSource = dataStore.getFeatureSource();

        // set the grid size (1 degree) and create a bounding envelope
        // that is neatly aligned with the grid size
        double sideLen = 1.0;
        ReferencedEnvelope gridBounds =
                Envelopes.expandToInclude(ozMapSource.getBounds(), sideLen);

        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.setName("grid");
        tb.add(GridFeatureBuilder.DEFAULT_GEOMETRY_ATTRIBUTE_NAME,
                Polygon.class, gridBounds.getCoordinateReferenceSystem());
        tb.add("id", Integer.class);
        SimpleFeatureType TYPE = tb.buildFeatureType();

        GridFeatureBuilder builder = new IntersectionBuilder(TYPE, ozMapSource);
        SimpleFeatureSource grid = Grids.createHexagonalGrid(gridBounds, sideLen, -1, builder);

        MapContext map = new DefaultMapContext();
        map.addLayer(ozMapSource, SLD.createPolygonStyle(Color.BLUE, Color.CYAN, 1.0f));
        map.addLayer(grid, null);
        JMapFrame.showMap(map);
    }


    private static class IntersectionBuilder extends GridFeatureBuilder {
        final FilterFactory2 ff2 = CommonFactoryFinder.getFilterFactory2(null);
        final GeometryFactory gf = JTSFactoryFinder.getGeometryFactory(null);

        final SimpleFeatureSource source;
        int id = 0;

        public IntersectionBuilder(SimpleFeatureType type, SimpleFeatureSource source) {
            super(type);
            this.source = source;
        }

        @Override
        public void setAttributes(GridElement el, Map<String, Object> attributes) {
            attributes.put("id", ++id);
        }

        @Override
        public boolean getCreateFeature(GridElement el) {
            Coordinate c = el.getCenter();
            Geometry p = gf.createPoint(c);
            Filter filter = ff2.intersects(ff2.property("the_geom"), ff2.literal(p));
            boolean result = false;

            try {
                result = !source.getFeatures(filter).isEmpty();
            } catch (IOException ex) {
                throw new IllegalStateException(ex);
            }

            return result;
        }
    };
}
