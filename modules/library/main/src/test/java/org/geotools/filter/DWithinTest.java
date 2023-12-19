package org.geotools.filter;

import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.expression.PropertyName;
import org.geotools.api.filter.spatial.DWithin;
import org.geotools.data.DataUtilities;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.SchemaException;
import org.geotools.util.logging.Logging;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static org.junit.Assert.assertEquals;

public class DWithinTest {

    // a point far away from all others (100+ km)

    static final Point REFERENCE_POINT = new GeometryFactory().createPoint(new Coordinate(3, 3));

    static final SimpleFeatureCollection FEATURES;

    static final Logger LOGGER = Logging.getLogger(DWithinTest.class);

    static {
        SimpleFeatureType ft1;
        try {
            ft1 = DataUtilities.createType("ft1", "*geometry:Point:srid=4326,intProperty:Integer,doubleProperty:Double,stringProperty:String");
        } catch (SchemaException e) {
            LOGGER.severe("Could not create feature type: " + e);
            throw new RuntimeException("Could not create feature type", e);
        }
        List<SimpleFeature> features = new ArrayList<>();
        features.add(DataUtilities.createFeature(ft1, "0=POINT(0 0)|0|0.0|zero"));
        features.add(DataUtilities.createFeature(ft1, "1=POINT(1 1)|1|1.1|one"));
        features.add(DataUtilities.createFeature(ft1, "2=POINT(2 2)|2|2.2|two"));
        FEATURES = new ListFeatureCollection(ft1, features);
    }

    @Test
    public void testDWithinGeographicKm() throws IOException {
        double pointDistance = 111d * Math.sqrt(2); // ft1 points are in diagonal
        assertDWithinFilter(0, pointDistance * 0.1, "km");
        assertDWithinFilter(0, pointDistance * 0.9, "km");
        assertDWithinFilter(1, pointDistance * 1.1, "km");
        assertDWithinFilter(2, pointDistance * 2.1, "km");
        assertDWithinFilter(3, pointDistance * 3.1, "km");
    }

    @Test
    public void testDWithinGeographicMeter() throws IOException {
        double pointDistance = 111000 * Math.sqrt(2); // ft1 points are in diagonal
        assertDWithinFilter(0, pointDistance * 0.1, "m");
        assertDWithinFilter(0, pointDistance * 0.9, "m");
        assertDWithinFilter(1, pointDistance * 1.1, "m");
        assertDWithinFilter(2, pointDistance * 2.1, "m");
        assertDWithinFilter(3, pointDistance * 3.1, "m");
    }

    @Test
    public void testDWithinGeographicMile() throws IOException {
        double pointDistance = 69 * Math.sqrt(2); // ft1 points are in diagonal
        assertDWithinFilter(0, pointDistance * 0.1, "mi");
        assertDWithinFilter(0, pointDistance * 0.9, "mi");
        assertDWithinFilter(1, pointDistance * 1.1, "mi");
        assertDWithinFilter(2, pointDistance * 2.1, "mi");
        assertDWithinFilter(3, pointDistance * 3.1, "mi");
    }

    @Test
    public void testDWithinGeographicFeet() throws IOException {
        double pointDistance = 5280 * 69 * Math.sqrt(2); // ft1 points are in diagonal
        assertDWithinFilter(0, pointDistance * 0.1, "ft");
        assertDWithinFilter(0, pointDistance * 0.9, "ft");
        assertDWithinFilter(1, pointDistance * 1.1, "ft");
        assertDWithinFilter(2, pointDistance * 2.1, "ft");
        assertDWithinFilter(3, pointDistance * 3.1, "ft");
    }

    private void assertDWithinFilter(int expectedMatches, double distance, String unit)
          throws IOException {
        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        final PropertyName geomProperty = ff.property("geometry");
        // too short distance
        DWithin filter = ff.dwithin(geomProperty, ff.literal(REFERENCE_POINT), distance, unit);
        SimpleFeatureCollection fc = FEATURES.subCollection(filter);
        assertEquals(expectedMatches, fc.size());
    }
}
