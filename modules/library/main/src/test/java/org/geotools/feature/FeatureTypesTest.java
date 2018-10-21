package org.geotools.feature;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.junit.Assert;
import org.junit.Test;
import org.locationtech.jts.geom.Point;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.FeatureType;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Function;

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
}
