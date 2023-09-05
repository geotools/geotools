package org.geotools.feature;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.AttributeDescriptor;
import org.geotools.api.feature.type.FeatureType;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.filter.expression.Function;
import org.geotools.api.filter.expression.PropertyName;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.referencing.CRS;
import org.geotools.referencing.ReferencingFactoryFinder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.junit.Assert;
import org.junit.Test;
import org.locationtech.jts.geom.Point;

/** Test FeatureTypes utility class abilities to inspect FeatureType data structure. */
public class FeatureTypesTest {

    @Test
    public void testNoLength() {
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.setName("NoLength");
        builder.setCRS(null);
        builder.add("name", String.class);
        builder.add("geom", Point.class);
        SimpleFeatureType ft = builder.buildFeatureType();

        assertEquals(
                FeatureTypes.ANY_LENGTH, FeatureTypes.getFieldLength(ft.getDescriptor("name")));
        assertEquals(
                FeatureTypes.ANY_LENGTH, FeatureTypes.getFieldLength(ft.getDescriptor("geom")));
    }

    @Test
    public void testStandardLength() {
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.setName("StdLength");
        builder.setCRS(null);
        builder.length(20);
        builder.add("name", String.class);
        builder.add("geom", Point.class);
        SimpleFeatureType ft = builder.buildFeatureType();

        assertEquals(20, FeatureTypes.getFieldLength(ft.getDescriptor("name")));
        assertEquals(
                FeatureTypes.ANY_LENGTH, FeatureTypes.getFieldLength(ft.getDescriptor("geom")));
    }

    @Test
    public void testCustomLengthExpressions() {
        AttributeTypeBuilder builder = new AttributeTypeBuilder();
        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        Function length = ff.function("LengthFunction", new Expression[] {ff.property(".")});

        // strict less
        builder.addRestriction(ff.less(length, ff.literal(20)));
        builder.setBinding(String.class);
        AttributeDescriptor attribute = builder.buildDescriptor("attribute");
        assertEquals(19, FeatureTypes.getFieldLength(attribute));

        // flip expression
        builder.addRestriction(ff.greater(ff.literal(20), length));
        builder.setBinding(String.class);
        attribute = builder.buildDescriptor("attribute");
        assertEquals(19, FeatureTypes.getFieldLength(attribute));
    }

    @Test
    public void testGetAncestors() {
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.setName("SomeFeature");
        builder.setCRS(null);
        builder.add("name", String.class);
        builder.add("geom", Point.class);
        SimpleFeatureType ft = builder.buildFeatureType();
        List<FeatureType> types = FeatureTypes.getAncestors(ft);
        Assert.assertEquals(1, types.size());
        Assert.assertEquals("Feature", types.get(0).getName().getLocalPart());
    }

    @Test
    public void testEquals() {
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.setName("SomeFeature");
        builder.setCRS(null);
        builder.add("name", String.class);
        builder.add("geom1", Point.class);
        builder.add("geom2", Point.class);
        builder.setDefaultGeometry("geom1");
        SimpleFeatureType ft1 = builder.buildFeatureType();
        builder.setName("SomeFeature");
        builder.setCRS(null);
        builder.add("name", String.class);
        builder.add("geom1", Point.class);
        builder.add("geom2", Point.class);
        builder.setDefaultGeometry("geom1");
        SimpleFeatureType ft2 = builder.buildFeatureType();
        assertTrue(FeatureTypes.equalsExact(ft1, ft2));

        builder.setName("SomeFeature");
        builder.setCRS(null);
        builder.add("name", String.class);
        builder.add("geom1", Point.class);
        builder.add("geom2", Point.class);
        builder.setDefaultGeometry("geom2");
        ft2 = builder.buildFeatureType();
        assertFalse(FeatureTypes.equalsExact(ft1, ft2));
    }

    @Test
    public void testCreateFieldOptionsMulti() {
        Filter restriction = FeatureTypes.createFieldOptions(Arrays.asList("a", "b"));
        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        PropertyName thisProperty = ff.property(".");
        Filter expected =
                ff.or(
                        ff.equal(thisProperty, ff.literal("a"), true),
                        ff.equal(thisProperty, ff.literal("b"), true));
        assertEquals(expected, restriction);
    }

    @Test
    public void testCreateFieldOptionsSingle() {
        Filter restriction = FeatureTypes.createFieldOptions(Arrays.asList("a"));
        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        PropertyName thisProperty = ff.property(".");
        Filter expected = ff.equal(thisProperty, ff.literal("a"), true);
        assertEquals(expected, restriction);
    }

    @Test
    public void testGetFieldOptionsMulti() {
        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        PropertyName thisProperty = ff.property(".");
        Filter restriction =
                ff.or(
                        ff.equal(thisProperty, ff.literal("a"), true),
                        ff.equal(thisProperty, ff.literal("b"), true));
        AttributeDescriptor descriptor = buildDescriptorWithRestriction(restriction);
        assertEquals(Arrays.asList("a", "b"), FeatureTypes.getFieldOptions(descriptor));
    }

    @Test
    public void testGetFieldOptionsInvalid() {
        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        PropertyName thisProperty = ff.property(".");
        Filter restriction =
                ff.or(
                        ff.equal(thisProperty, ff.literal("a"), true),
                        ff.greaterOrEqual(thisProperty, ff.literal(10), true));
        AttributeDescriptor descriptor = buildDescriptorWithRestriction(restriction);
        assertNull(FeatureTypes.getFieldOptions(descriptor));
    }

    @Test
    public void testGetFieldOptionsSingle() {
        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        PropertyName thisProperty = ff.property(".");
        Filter restriction = ff.equal(thisProperty, ff.literal("a"), true);
        AttributeDescriptor descriptor = buildDescriptorWithRestriction(restriction);
        assertEquals(Arrays.asList("a"), FeatureTypes.getFieldOptions(descriptor));
    }

    @Test
    public void testGetFieldOptionsFromSuper() {
        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        PropertyName thisProperty = ff.property(".");
        Filter restriction =
                ff.or(
                        ff.equal(thisProperty, ff.literal("a"), true),
                        ff.greaterOrEqual(thisProperty, ff.literal(10), true));
        AttributeDescriptor superDescriptor = buildDescriptorWithRestriction(restriction);
        AttributeTypeBuilder at = new AttributeTypeBuilder();
        at.name("test").binding(String.class).superType(superDescriptor.getType());
        AttributeDescriptor descriptor = at.buildDescriptor("test");
        assertNull(FeatureTypes.getFieldOptions(descriptor));
    }

    public AttributeDescriptor buildDescriptorWithRestriction(Filter restriction) {
        AttributeTypeBuilder builder = new AttributeTypeBuilder();
        builder.addRestriction(restriction);
        builder.setBinding(String.class);
        return builder.buildDescriptor("attribute");
    }

    @Test
    public void testShouldNotReproject() throws FactoryException {
        CoordinateReferenceSystem wgs84FromConstant = DefaultGeographicCRS.WGS84;
        String crsDefinition =
                "GEOGCS[\"WGS84(DD)\",DATUM[\"WGS84\",SPHEROID[\"WGS84\", 6378137.0, 298.257223563]],"
                        + "PRIMEM[\"Greenwich\", 0.0],UNIT[\"degree\", 0.017453292519943295],AXIS[\"Geodetic longitude\", "
                        + "EAST],AXIS[\"Geodetic latitude\",NORTH],AUTHORITY[\"EPSG\",\"4326\"]]";
        CoordinateReferenceSystem fromWKT =
                ReferencingFactoryFinder.getCRSFactory(null).createFromWKT(crsDefinition);
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.setName("NoLength");
        builder.setCRS(fromWKT);
        builder.add("name", String.class);
        builder.add("geom", Point.class, fromWKT);
        SimpleFeatureType ft = builder.buildFeatureType();
        // check the util method result
        boolean shouldReproject = FeatureTypes.shouldReproject(ft, wgs84FromConstant);
        Assert.assertFalse(shouldReproject);
    }

    @Test
    public void testShouldReproject() throws FactoryException {
        CoordinateReferenceSystem wgs84FromConstant = DefaultGeographicCRS.WGS84;
        CoordinateReferenceSystem mercatorCRS = CRS.decode("EPSG:3857");
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.setName("NoLength");
        builder.setCRS(mercatorCRS);
        builder.add("name", String.class);
        builder.add("geom", Point.class, mercatorCRS);
        SimpleFeatureType ft = builder.buildFeatureType();
        // check the util method result
        boolean shouldReproject = FeatureTypes.shouldReproject(ft, wgs84FromConstant);
        Assert.assertTrue(shouldReproject);
    }
}
