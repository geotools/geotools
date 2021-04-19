package org.geotools.feature.visitor;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import org.geotools.data.DataUtilities;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.junit.BeforeClass;
import org.junit.Test;
import org.locationtech.jts.io.WKTReader;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;

public class UniqueCountVisitorTest {

    private static WKTReader wktParser = new WKTReader();

    private static SimpleFeatureType uniqueValuesCountTestType;
    private static FeatureCollection featureCollection;
    static FilterFactory ff = CommonFactoryFinder.getFilterFactory2();

    @BeforeClass
    public static void setup() throws Exception {
        // the building feature type that will be used during the tests
        uniqueValuesCountTestType =
                DataUtilities.createType(
                        "uniqueValuesCount",
                        "id:Integer,aStringValue:String," + "aDoubleValue:Double,geo:Geometry");
        // the building features that will be used during the tests
        SimpleFeature[] simpleFeatures = {
            SimpleFeatureBuilder.build(
                    uniqueValuesCountTestType,
                    new Object[] {1, "A", 50.0, wktParser.read("POINT(-5 -5)")},
                    null),
            SimpleFeatureBuilder.build(
                    uniqueValuesCountTestType,
                    new Object[] {2, "B", 10.0, wktParser.read("POINT(-5 -5)")},
                    null),
            SimpleFeatureBuilder.build(
                    uniqueValuesCountTestType,
                    new Object[] {3, "C", 20.0, wktParser.read("POINT(-5 -5)")},
                    null),
            SimpleFeatureBuilder.build(
                    uniqueValuesCountTestType,
                    new Object[] {4, "D", 30.0, wktParser.read("POINT(5 5)")},
                    null),
            SimpleFeatureBuilder.build(
                    uniqueValuesCountTestType,
                    new Object[] {5, "E", 60.0, wktParser.read("POINT(5 5)")},
                    null),
            SimpleFeatureBuilder.build(
                    uniqueValuesCountTestType,
                    new Object[] {6, "E", 10.0, wktParser.read("POINT(5 5)")},
                    null),
            SimpleFeatureBuilder.build(
                    uniqueValuesCountTestType,
                    new Object[] {7, "D", 500.0, wktParser.read("POINT(-5 5)")},
                    null),
            SimpleFeatureBuilder.build(
                    uniqueValuesCountTestType,
                    new Object[] {8, "C", 150.0, wktParser.read("POINT(-5 5)")},
                    null),
            SimpleFeatureBuilder.build(
                    uniqueValuesCountTestType,
                    new Object[] {9, "C", 20.0, wktParser.read("POINT(5 -5)")},
                    null),
            SimpleFeatureBuilder.build(
                    uniqueValuesCountTestType,
                    new Object[] {10, "C", 30.0, wktParser.read("POINT(5 -5)")},
                    null),
            SimpleFeatureBuilder.build(
                    uniqueValuesCountTestType,
                    new Object[] {11, "A", 500.0, wktParser.read("POINT(0 0)")},
                    null),
            SimpleFeatureBuilder.build(
                    uniqueValuesCountTestType,
                    new Object[] {12, "F", 500.0, wktParser.read("POINT(0 0)")},
                    null),
        };
        // creating the bulding featrue collection
        featureCollection = DataUtilities.collection(simpleFeatures);
    }

    @Test
    public void testUniqueCountVisitor() throws IOException {
        UniqueCountVisitor uniqueCountVisitor = new UniqueCountVisitor("aStringValue");
        featureCollection.accepts(uniqueCountVisitor, null);
        int count = uniqueCountVisitor.getResult().toInt();
        assertEquals(6, count);
    }

    @Test
    public void testUniqueCountVisitor2() throws IOException {
        UniqueCountVisitor uniqueCountVisitor = new UniqueCountVisitor("aDoubleValue");
        featureCollection.accepts(uniqueCountVisitor, null);
        int count = uniqueCountVisitor.getResult().toInt();
        assertEquals(7, count);
    }

    @Test
    public void testUniqueCountVisitorWithFilter() throws IOException {
        UniqueCountVisitor uniqueCountVisitor = new UniqueCountVisitor("aStringValue");
        Filter f =
                ff.or(
                        ff.equals(ff.property("aStringValue"), ff.literal("A")),
                        ff.equals(ff.property("aStringValue"), ff.literal("B")));
        featureCollection.subCollection(f).accepts(uniqueCountVisitor, null);
        int count = uniqueCountVisitor.getResult().toInt();
        assertEquals(2, count);
    }

    @Test
    public void testUniqueCountVisitorWithFilter2() throws IOException {
        UniqueCountVisitor uniqueCountVisitor = new UniqueCountVisitor("aDoubleValue");
        Filter f =
                ff.or(
                        ff.equals(ff.property("aDoubleValue"), ff.literal(500d)),
                        ff.equals(ff.property("aDoubleValue"), ff.literal(20d)));
        featureCollection.subCollection(f).accepts(uniqueCountVisitor, null);
        int count = uniqueCountVisitor.getResult().toInt();
        assertEquals(2, count);
    }
}
