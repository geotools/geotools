package org.geotools.data.georest;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.geotools.data.DataStore;
import org.geotools.data.FeatureReader;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.geotools.data.georest.GeoRestDataStoreFactory;
import org.geotools.data.georest.GeoRestFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.spatial.BBOX;

public class DataStoreTest extends TestCase {

    private static final String URL = "http://localhost:5000/";

    private static final String FEATURESOURCE = "countries";

    private static final FilterFactory2 FF = CommonFactoryFinder.getFilterFactory2(null);

    public void testFactoryCanProcess() {
        GeoRestDataStoreFactory factory = new GeoRestDataStoreFactory();
        Assert.assertTrue(factory.canProcess(createParams()));
    }

    public void testCreateDataStore() throws IOException {
        GeoRestDataStoreFactory factory = new GeoRestDataStoreFactory();
        DataStore ds = factory.createDataStore(createParams());
        Assert.assertNotNull(ds);
    }

    public void testGetFeatureSource() throws IOException {
        GeoRestDataStoreFactory factory = new GeoRestDataStoreFactory();
        DataStore ds = factory.createDataStore(createParams());
        GeoRestFeatureSource source = (GeoRestFeatureSource) ds.getFeatureSource(FEATURESOURCE);
        Assert.assertNotNull(source);
    }

    public void testGetSchema() throws IOException {
        GeoRestDataStoreFactory factory = new GeoRestDataStoreFactory();
        DataStore ds = factory.createDataStore(createParams());
        GeoRestFeatureSource source = (GeoRestFeatureSource) ds.getFeatureSource(FEATURESOURCE);
        SimpleFeatureType type = source.getSchema();
        Assert.assertNotNull(type);
        Assert.assertTrue(type.getAttributeCount() > 0);
    }

    public void testCountFeatures() throws IOException {
        GeoRestDataStoreFactory factory = new GeoRestDataStoreFactory();
        DataStore ds = factory.createDataStore(createParams());
        GeoRestFeatureSource source = (GeoRestFeatureSource) ds.getFeatureSource(FEATURESOURCE);
        int count = source.getCount(new Query(FEATURESOURCE, Filter.INCLUDE));
        Assert.assertTrue(count > 0);
        BBOX bbox = FF.bbox("geometry", 0, 0, 10, 10, "EPSG:4326");
        count = source.getCount(new Query(FEATURESOURCE, bbox, 5, new String[] {}, ""));
        Assert.assertTrue(count == 5);
    }

    public void testGetFeatureReader() throws Exception {
        GeoRestDataStoreFactory factory = new GeoRestDataStoreFactory();
        DataStore ds = factory.createDataStore(createParams());
        FeatureReader<SimpleFeatureType, SimpleFeature> r = ds.getFeatureReader(new Query(
                FEATURESOURCE), Transaction.AUTO_COMMIT);
        assertNotNull(r);
        assertTrue(r.hasNext());
    }

    // Private methods:

    private Map<String, Serializable> createParams() {
        Map<String, Serializable> params = new HashMap<String, Serializable>();
        // URL url = DataStoreTest.class.getResource("/");
        params.put(GeoRestDataStoreFactory.PARAM_URL, URL);
        params.put(GeoRestDataStoreFactory.PARAM_LAYERS, "countries");
        return params;
    }
}
