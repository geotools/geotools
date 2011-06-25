package org.geotools.data.store;

import java.io.IOException;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.collection.FilteringSimpleFeatureCollection;
import org.opengis.feature.Feature;
import org.opengis.feature.FeatureVisitor;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;

public class FilteringSimpleFeatureCollectionTest extends FeatureCollectionWrapperTestSupport {
    FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);

    public void testCount() {
        Filter filter = ff.equal(ff.property("someAtt"), ff.literal("1"), false);
        SimpleFeatureCollection collection = new FilteringSimpleFeatureCollection(delegate, filter);
        assertEquals(1, collection.size());
    }
    
    public void testVisitor() throws IOException {
        Filter filter = ff.equal(ff.property("someAtt"), ff.literal("1"), false);
        SimpleFeatureCollection collection = new FilteringSimpleFeatureCollection(delegate, filter);
        collection.accepts(new FeatureVisitor() {
            
            public void visit(Feature feature) {
                assertEquals(1, feature.getProperty("someAtt").getValue());
                
            }
        }, null);
    }
}
