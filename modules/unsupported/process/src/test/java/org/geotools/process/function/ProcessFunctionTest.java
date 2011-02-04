package org.geotools.process.function;

import static org.junit.Assert.*;

import java.net.URL;

import org.geotools.TestData;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.process.feature.BufferFeatureCollectionFactory;
import org.junit.Test;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Function;

import com.vividsolutions.jts.geom.MultiPolygon;

public class ProcessFunctionTest {

    FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);

    @Test
    public void testBuffer() throws Exception {
        URL url = TestData.getResource(TestData.class, "shapes/archsites.shp");
        ShapefileDataStore store = new ShapefileDataStore(url);
        SimpleFeatureCollection features = store.getFeatureSource().getFeatures();

        // first param, the context feature collection
        Function featuresParam = ff.function("parameter", ff
                .literal(BufferFeatureCollectionFactory.FEATURES.key));
        // second param, the buffer size
        Function bufferParam = ff.function("parameter", ff
                .literal(BufferFeatureCollectionFactory.BUFFER.key), ff.literal(1000));
        // build the function and call it
        Function buffer = ff.function("gt:BufferFeatureCollection", featuresParam, bufferParam);
        SimpleFeatureCollection buffered = (SimpleFeatureCollection) buffer.evaluate(features);
        
        // check the results
        assertEquals(features.size(), buffered.size());
        GeometryDescriptor gd = buffered.getSchema().getGeometryDescriptor();
        // is it actually a buffer?
        assertEquals(MultiPolygon.class, gd.getType().getBinding());
    }
}
