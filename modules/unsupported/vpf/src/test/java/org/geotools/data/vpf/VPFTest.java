package org.geotools.data.vpf;

import java.io.File;
import java.io.Serializable;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.geotools.api.data.DataStore;
import org.geotools.api.data.FeatureReader;
import org.geotools.api.data.Query;
import org.geotools.api.data.SimpleFeatureSource;
import org.geotools.api.data.Transaction;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.GeometryDescriptor;
import org.geotools.api.feature.type.Name;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.test.OnlineTestCase;
import org.geotools.util.URLs;
import org.junit.Test;

/** @author jody */
public class VPFTest extends OnlineTestCase {

    File vmap = null;

    @Override
    protected boolean isOnline() throws Exception {
        File file = new File(fixture.getProperty("vmap"));

        return file.exists();
    }

    @Override
    protected void connect() throws Exception {
        super.connect();

        String path = fixture.getProperty("vmap");
        vmap = new File(path);
    }

    @Override
    protected void disconnect() throws Exception {
        vmap = null;
        super.disconnect();
    }

    @Override
    protected String getFixtureId() {
        return "vpf.vmap";
    }

    /**
     * As the VMAP dataset is so large it requires a sepeate download. We are treating this as an
     * online test - asking you to supply the location of this dataset on your machine.
     *
     * @return fixture listing where vmap folder (that contains lht)
     */
    @Override
    protected Properties createExampleFixture() {
        Properties fixture = new Properties();
        fixture.put("vmap", "C:\\data\\v0noa_5\\vmaplv0\\noamer");
        return fixture;
    }

    @Test
    public void testFactory() throws Exception {
        assertNotNull("Check fixture is provided", vmap);
        assertTrue("vmap found", vmap.exists());
        assertTrue("directory check", vmap.isDirectory());
        File lht = new File(vmap, "lht");
        assertTrue("lht check", lht.exists());

        VPFDataStoreFactory factory = new VPFDataStoreFactory();

        URL url = URLs.fileToUrl(vmap);

        Map<String, Serializable> params = new HashMap<>();
        params.put("url", url);
        assertTrue("Can connect", factory.canProcess(params));

        DataStore vpf = factory.createDataStore(params);
        assertNotNull("connect", vpf);

        List<Name> names = vpf.getNames();
        assertFalse("content check", names.isEmpty());

        VPFLogger.log(names.toString());
    }

    @Test
    public void testSchema() throws Exception {
        assertNotNull("Check fixture is provided", vmap);
        assertTrue("vmap found", vmap.exists());
        assertTrue("directory check", vmap.isDirectory());
        File lht = new File(vmap, "lht");
        assertTrue("lht check", lht.exists());

        VPFDataStoreFactory factory = new VPFDataStoreFactory();
        URL url = URLs.fileToUrl(vmap);
        Map<String, Serializable> params = new HashMap<>();
        params.put("url", url);
        DataStore store = factory.createDataStore(params);
        List<Name> names = store.getNames();

        Name name = names.get(0);
        SimpleFeatureType schema = store.getSchema(name);
        assertNotNull("schema found", schema);
        GeometryDescriptor geom = schema.getGeometryDescriptor();
        assertNotNull("spatial", geom);
        // assertNotNull("crs", geom.getCoordinateReferenceSystem() );
    }

    @Test
    public void testFeatureReader() throws Exception {
        VPFDataStoreFactory factory = new VPFDataStoreFactory();
        URL url = URLs.fileToUrl(vmap);
        Map<String, Serializable> params = new HashMap<>();
        params.put("url", url);
        DataStore store = factory.createDataStore(params);
        List<Name> names = store.getNames();

        Name name = names.get(0);

        int count = 0;
        ReferencedEnvelope bounds = new ReferencedEnvelope(DefaultGeographicCRS.WGS84);
        try (FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                store.getFeatureReader(new Query(name.getLocalPart()), Transaction.AUTO_COMMIT)) {
            while (reader.hasNext()) {
                SimpleFeature feature = reader.next();
                count++;
                bounds.include(feature.getBounds());
            }
        }
        VPFLogger.log("count:" + count);
        VPFLogger.log("bounds:" + bounds);
    }

    @Test
    public void testFeatureSource() throws Exception {
        VPFDataStoreFactory factory = new VPFDataStoreFactory();
        URL url = URLs.fileToUrl(vmap);
        Map<String, Serializable> params = new HashMap<>();
        params.put("url", url);
        DataStore store = factory.createDataStore(params);
        List<Name> names = store.getNames();

        Name name = names.get(0);
        SimpleFeatureSource source = store.getFeatureSource(name);
        ReferencedEnvelope extent = source.getBounds();
        // assertNotNull( "extent", extent ); // should be from header
        VPFLogger.log("extent:" + extent);

        SimpleFeatureCollection features = source.getFeatures();

        int count = 0;
        ReferencedEnvelope bounds = new ReferencedEnvelope(DefaultGeographicCRS.WGS84);
        try (SimpleFeatureIterator iterator = features.features()) {
            while (iterator.hasNext()) {
                SimpleFeature feature = iterator.next();
                count++;
                bounds.include(feature.getBounds());
            }
        }
        VPFLogger.log("count:" + count);
        VPFLogger.log("bounds:" + bounds);
    }
}
