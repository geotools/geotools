package org.geotools.jdbc;

import java.io.IOException;
import java.util.logging.Logger;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.FeatureCollection;
import org.geotools.util.logging.Logging;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.spatial.Beyond;
import org.opengis.filter.spatial.DWithin;

public abstract class JDBCDistanceFiltersTest extends JDBCTestSupport {

    // a point far away from all others (100+ km)

    static final Point REFERENCE_POINT = new GeometryFactory().createPoint(new Coordinate(3, 3));

    static final Logger LOGGER = Logging.getLogger(JDBCFunctionOnlineTest.class);

    public void testDWithinGeographicKm() throws IOException {
        double pointDistance = 111d * Math.sqrt(2); // ft1 points are in diagonal
        assertDWithinFilter(0, pointDistance * 0.1, "km");
        assertDWithinFilter(0, pointDistance * 0.9, "km");
        assertDWithinFilter(1, pointDistance * 1.1, "km");
        assertDWithinFilter(2, pointDistance * 2.1, "km");
        assertDWithinFilter(3, pointDistance * 3.1, "km");
    }

    public void testDWithinGeographicMeter() throws IOException {
        double pointDistance = 111000 * Math.sqrt(2); // ft1 points are in diagonal
        assertDWithinFilter(0, pointDistance * 0.1, "m");
        assertDWithinFilter(0, pointDistance * 0.9, "m");
        assertDWithinFilter(1, pointDistance * 1.1, "m");
        assertDWithinFilter(2, pointDistance * 2.1, "m");
        assertDWithinFilter(3, pointDistance * 3.1, "m");
    }

    public void testDWithinGeographicMile() throws IOException {
        double pointDistance = 69 * Math.sqrt(2); // ft1 points are in diagonal
        assertDWithinFilter(0, pointDistance * 0.1, "mi");
        assertDWithinFilter(0, pointDistance * 0.9, "mi");
        assertDWithinFilter(1, pointDistance * 1.1, "mi");
        assertDWithinFilter(2, pointDistance * 2.1, "mi");
        assertDWithinFilter(3, pointDistance * 3.1, "mi");
    }

    public void testDWithinGeographicFeet() throws IOException {
        double pointDistance = 5280 * 69 * Math.sqrt(2); // ft1 points are in diagonal
        assertDWithinFilter(0, pointDistance * 0.1, "ft");
        assertDWithinFilter(0, pointDistance * 0.9, "ft");
        assertDWithinFilter(1, pointDistance * 1.1, "ft");
        assertDWithinFilter(2, pointDistance * 2.1, "ft");
        assertDWithinFilter(3, pointDistance * 3.1, "ft");
    }

    public void testBeyondGeographicKm() throws IOException {
        double pointDistance = 111d * Math.sqrt(2); // ft1 points are in diagonal
        assertBeyondFilter(3, pointDistance * 0.1, "km");
        assertBeyondFilter(3, pointDistance * 0.9, "km");
        assertBeyondFilter(2, pointDistance * 1.1, "km");
        assertBeyondFilter(1, pointDistance * 2.1, "km");
        assertBeyondFilter(0, pointDistance * 3.1, "km");
    }

    public void testBeyondGeographicMeter() throws IOException {
        double pointDistance = 111000 * Math.sqrt(2); // ft1 points are in diagonal
        assertBeyondFilter(3, pointDistance * 0.1, "m");
        assertBeyondFilter(3, pointDistance * 0.9, "m");
        assertBeyondFilter(2, pointDistance * 1.1, "m");
        assertBeyondFilter(1, pointDistance * 2.1, "m");
        assertBeyondFilter(0, pointDistance * 3.1, "m");
    }

    public void testBeyondGeographicMile() throws IOException {
        double pointDistance = 69 * Math.sqrt(2); // ft1 points are in diagonal
        assertBeyondFilter(3, pointDistance * 0.1, "mi");
        assertBeyondFilter(3, pointDistance * 0.9, "mi");
        assertBeyondFilter(2, pointDistance * 1.1, "mi");
        assertBeyondFilter(1, pointDistance * 2.1, "mi");
        assertBeyondFilter(0, pointDistance * 3.1, "mi");
    }

    public void testBeyondGeographicFeet() throws IOException {
        double pointDistance = 5280 * 69 * Math.sqrt(2); // ft1 points are in diagonal
        assertBeyondFilter(3, pointDistance * 0.1, "ft");
        assertBeyondFilter(3, pointDistance * 0.9, "ft");
        assertBeyondFilter(2, pointDistance * 1.1, "ft");
        assertBeyondFilter(1, pointDistance * 2.1, "ft");
        assertBeyondFilter(0, pointDistance * 3.1, "ft");
    }

    /** Subclasses testing for "proper" distance calculations should return true */
    protected boolean areDistanceUnitsSupported() {
        LOGGER.info("Skipping dWithin with unit of measure calculation tests");
        return false;
    }

    private void assertDWithinFilter(int expectedMatches, double distance, String unit)
            throws IOException {
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);
        final PropertyName geomProperty = ff.property(aname("geometry"));
        final ContentFeatureSource features = dataStore.getFeatureSource(tname("ft1"));

        // too short distance
        DWithin filter = ff.dwithin(geomProperty, ff.literal(REFERENCE_POINT), distance, unit);
        FeatureCollection fc = features.getFeatures(filter);
        assertEquals(expectedMatches, fc.size());
    }

    private void assertBeyondFilter(int expectedMatches, double distance, String unit)
            throws IOException {
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);
        final PropertyName geomProperty = ff.property(aname("geometry"));
        final ContentFeatureSource features = dataStore.getFeatureSource(tname("ft1"));

        // too short distance
        Beyond filter = ff.beyond(geomProperty, ff.literal(REFERENCE_POINT), distance, unit);
        FeatureCollection fc = features.getFeatures(filter);
        assertEquals(expectedMatches, fc.size());
    }
}
