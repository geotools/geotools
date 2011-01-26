package org.geotools.data.georest;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.geotools.data.georest.GeoRestFilterVisitor;
import org.geotools.factory.CommonFactoryFinder;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.PropertyIsEqualTo;
import org.opengis.filter.PropertyIsGreaterThan;
import org.opengis.filter.PropertyIsGreaterThanOrEqualTo;
import org.opengis.filter.PropertyIsLessThan;
import org.opengis.filter.PropertyIsLessThanOrEqualTo;
import org.opengis.filter.PropertyIsLike;
import org.opengis.filter.PropertyIsNotEqualTo;
import org.opengis.filter.spatial.BBOX;

/**
 * <p>
 * Test-case that tests the filtering possibilities of the GeoRest service. Keep in mind that such a
 * service does not support the vast amount of filters that GeoTools supports.
 * </p>
 * 
 * @author Pieter De Graef, Geosparc
 */
public class GeoRestFilterVisitorTest extends TestCase {

    private static final String URL = "http://www.myurl.com/layer1/";

    private static final String PROPERTY_NAME = "aProperty";

    private static final String PROPERTY_VALUE = "342";

    private static final FilterFactory2 FF = CommonFactoryFinder.getFilterFactory2(null);

    public void testBBOX() throws Exception {
        StringBuilder builder = new StringBuilder(URL);
        GeoRestFilterVisitor visitor = new GeoRestFilterVisitor(true);
        BBOX bbox = FF.bbox("prop0", 0, 0, 10, 10, "EPSG:4326");
        visitor.visit(bbox, null);
        visitor.finish(builder, false);
        Assert.assertEquals(URL + "?bbox=0.0,0.0,10.0,10.0", builder.toString());
    }

    public void testPropertyIsEqualTo() throws Exception {
        StringBuilder builder = new StringBuilder(URL);
        GeoRestFilterVisitor visitor = new GeoRestFilterVisitor(true);
        PropertyIsEqualTo filter = FF
                .equals(FF.property(PROPERTY_NAME), FF.literal(PROPERTY_VALUE));
        visitor.visit(filter, null);
        visitor.finish(builder, false);
        Assert.assertEquals(URL + "?" + PROPERTY_NAME + "__eq=" + PROPERTY_VALUE + "&queryable="
                + PROPERTY_NAME, builder.toString());
    }

    public void testPropertyIsNotEqualTo() throws Exception {
        StringBuilder builder = new StringBuilder(URL);
        GeoRestFilterVisitor visitor = new GeoRestFilterVisitor(true);
        PropertyIsNotEqualTo filter = FF.notEqual(FF.property(PROPERTY_NAME),
                FF.literal(PROPERTY_VALUE));
        visitor.visit(filter, null);
        visitor.finish(builder, false);
        Assert.assertEquals(URL + "?" + PROPERTY_NAME + "__ne=" + PROPERTY_VALUE + "&queryable="
                + PROPERTY_NAME, builder.toString());
    }

    public void testPropertyIsGreaterThan() throws Exception {
        StringBuilder builder = new StringBuilder(URL);
        GeoRestFilterVisitor visitor = new GeoRestFilterVisitor(true);
        PropertyIsGreaterThan filter = FF.greater(FF.property(PROPERTY_NAME),
                FF.literal(PROPERTY_VALUE));
        visitor.visit(filter, null);
        visitor.finish(builder, false);
        Assert.assertEquals(URL + "?" + PROPERTY_NAME + "__gt=" + PROPERTY_VALUE + "&queryable="
                + PROPERTY_NAME, builder.toString());
    }

    public void testPropertyIsGreaterThanOrEqualTo() throws Exception {
        StringBuilder builder = new StringBuilder(URL);
        GeoRestFilterVisitor visitor = new GeoRestFilterVisitor(true);
        PropertyIsGreaterThanOrEqualTo filter = FF.greaterOrEqual(FF.property(PROPERTY_NAME),
                FF.literal(PROPERTY_VALUE));
        visitor.visit(filter, null);
        visitor.finish(builder, false);
        Assert.assertEquals(URL + "?" + PROPERTY_NAME + "__gte=" + PROPERTY_VALUE + "&queryable="
                + PROPERTY_NAME, builder.toString());
    }

    public void testPropertyIsLessThan() throws Exception {
        StringBuilder builder = new StringBuilder(URL);
        GeoRestFilterVisitor visitor = new GeoRestFilterVisitor(true);
        PropertyIsLessThan filter = FF.less(FF.property(PROPERTY_NAME), FF.literal(PROPERTY_VALUE));
        visitor.visit(filter, null);
        visitor.finish(builder, false);
        Assert.assertEquals(URL + "?" + PROPERTY_NAME + "__lt=" + PROPERTY_VALUE + "&queryable="
                + PROPERTY_NAME, builder.toString());
    }

    public void testPropertyIsLessThanOrEqualTo() throws Exception {
        StringBuilder builder = new StringBuilder(URL);
        GeoRestFilterVisitor visitor = new GeoRestFilterVisitor(true);
        PropertyIsLessThanOrEqualTo filter = FF.lessOrEqual(FF.property(PROPERTY_NAME),
                FF.literal(PROPERTY_VALUE));
        visitor.visit(filter, null);
        visitor.finish(builder, false);
        Assert.assertEquals(URL + "?" + PROPERTY_NAME + "__lte=" + PROPERTY_VALUE + "&queryable="
                + PROPERTY_NAME, builder.toString());
    }

    public void testPropertyIsLike() throws Exception {
        StringBuilder builder = new StringBuilder(URL);
        GeoRestFilterVisitor visitor = new GeoRestFilterVisitor(true);
        PropertyIsLike filter = FF.like(FF.property(PROPERTY_NAME), PROPERTY_VALUE);
        visitor.visit(filter, null);
        visitor.finish(builder, false);
        Assert.assertEquals(URL + "?" + PROPERTY_NAME + "__like=" + PROPERTY_VALUE + "&queryable="
                + PROPERTY_NAME, builder.toString());
    }
}