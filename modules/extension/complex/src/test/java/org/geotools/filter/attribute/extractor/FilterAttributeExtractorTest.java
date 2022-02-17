package org.geotools.filter.attribute.extractor;

import static org.geotools.feature.FakeTypes.ANYSIMPLETYPE_TYPE;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.geotools.feature.NameImpl;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeImpl;
import org.geotools.feature.type.AttributeDescriptorImpl;
import org.geotools.feature.type.AttributeTypeImpl;
import org.geotools.feature.type.Types;
import org.geotools.filter.AttributeExpressionImpl;
import org.geotools.filter.FilterAttributeExtractor;
import org.junit.Assert;
import org.junit.Test;
import org.opengis.feature.Feature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.AttributeType;
import org.xml.sax.helpers.NamespaceSupport;

public class FilterAttributeExtractorTest {

    @Test
    public void testNonExistingAttributeEvaluatesToAttributeDescriptor() throws Exception {
        List<AttributeDescriptor> schema = new ArrayList<>();
        AttributeType doubleType =
                new AttributeTypeImpl(
                        new NameImpl("http://www.w3.org/2001/XMLSchema", "double"),
                        Double.class,
                        false,
                        false,
                        Collections.emptyList(),
                        ANYSIMPLETYPE_TYPE,
                        null);
        schema.add(
                new AttributeDescriptorImpl(
                        doubleType, Types.typeName("pointOne"), 0, 1, false, null));
        schema.add(
                new AttributeDescriptorImpl(
                        doubleType, Types.typeName("pointTwo"), 0, 1, false, null));

        SimpleFeatureType type =
                new SimpleFeatureTypeImpl(
                        Types.typeName("GeometryContainer"), schema, null, false, null, null, null);
        Feature feature = SimpleFeatureBuilder.build(type, new Object[] {5.0, 2.5}, null);
        FilterAttributeExtractor fae = new FilterAttributeExtractor(type);
        NamespaceSupport nss = new NamespaceSupport();
        nss.declarePrefix("ogc", "http://www.opengis.net/ogc");
        nss.declarePrefix("gml", "http://www.opengis.net/gml");
        nss.declarePrefix("sld", "http://www.opengis.net/sld");
        Assert.assertNotNull(fae.visit(new AttributeExpressionImpl("type", nss), feature));
    }
}
