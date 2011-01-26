package org.geotools.feature;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.junit.Assert;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.FeatureType;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Function;

import com.vividsolutions.jts.geom.Point;

public class FeatureTypesTest {

    @Test
    public void testNoLength() {
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.setName("NoLength");
        builder.add("name", String.class);
        builder.add("geom", Point.class);
        SimpleFeatureType ft = builder.buildFeatureType();

        assertEquals(FeatureTypes.ANY_LENGTH, FeatureTypes.getFieldLength(ft.getDescriptor("name")));
        assertEquals(FeatureTypes.ANY_LENGTH, FeatureTypes.getFieldLength(ft.getDescriptor("geom")));
    }

    @Test
    public void testStandardLength() {
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.setName("StdLength");
        builder.length(20);
        builder.add("name", String.class);
        builder.add("geom", Point.class);
        SimpleFeatureType ft = builder.buildFeatureType();

        assertEquals(20, FeatureTypes.getFieldLength(ft.getDescriptor("name")));
        assertEquals(FeatureTypes.ANY_LENGTH, FeatureTypes.getFieldLength(ft.getDescriptor("geom")));
    }

    @Test
    public void testCustomLengthExpressions() {
        AttributeTypeBuilder builder = new AttributeTypeBuilder();
        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        Function length = ff.function("LengthFunction", new Expression[]{ff.property(".")});
        
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
        builder.add("name", String.class);
        builder.add("geom", Point.class);
        SimpleFeatureType ft = builder.buildFeatureType();
        List<FeatureType> types = FeatureTypes.getAncestors(ft);
        Assert.assertEquals(1, types.size());
        Assert.assertEquals("Feature", types.get(0).getName().getLocalPart());
    }
    
}
