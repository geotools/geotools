package org.geotools.filter.visitor;

import org.geotools.data.DataUtilities;
import org.geotools.factory.CommonFactoryFinder;
import org.junit.Before;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.FilterFactory;

public class RangeCombinerTest {

    FilterFactory ff = CommonFactoryFinder.getFilterFactory();

    ExpressionTypeVisitor visitor;

    SimpleFeatureType ft;

    @Before
    public void setup() throws Exception {
        ft = DataUtilities
                .createType(
                        "test",
                        "theGeom:LineString,b:java.lang.Byte,s:java.lang.Short,i:java.lang.Integer,l:java.lang.Long,d:java.lang.Double,label:String");
        visitor = new ExpressionTypeVisitor(ft);
    }

    @Test
    public void testIntersection() {

    }
}
