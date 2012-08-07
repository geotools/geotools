package org.geotools.data.wfs.impl;

import static org.geotools.data.DataUtilities.createType;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import javax.xml.namespace.QName;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.data.wfs.internal.WFSClient;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.identity.FeatureId;

public class WFSContentFeatureStoreTest {

    private static final QName TYPE1 = new QName("http://example.com/1", "points", "prefix1");

    private static final QName TYPE2 = new QName("http://example.com/2", "points", "prefix2");

    private static SimpleFeatureType featureType1;

    private static SimpleFeatureType featureType2;

    private static String simpleTypeName1;

    private static String simpleTypeName2;

    private WFSContentDataStore dataStore;

    private WFSClient wfs;

    @BeforeClass
    public static void oneTimeSetUp() throws Exception {
        simpleTypeName1 = TYPE1.getPrefix() + "_" + TYPE1.getLocalPart();
        simpleTypeName2 = TYPE2.getPrefix() + "_" + TYPE2.getLocalPart();

        featureType1 = createType("http://example.com/1", simpleTypeName1,
                "name:String,geom:Point:srid=4326");
        featureType2 = createType("http://example.com/2", "prefix2_roads",
                "name:String,geom:Point:srid=3857");

    }

    @Before
    public void setUp() throws Exception {
        wfs = mock(WFSClient.class);
        when(wfs.getRemoteTypeNames()).thenReturn(new HashSet<QName>(Arrays.asList(TYPE1, TYPE2)));
        when(wfs.supportsTransaction(TYPE1)).thenReturn(Boolean.TRUE);
        when(wfs.supportsTransaction(TYPE2)).thenReturn(Boolean.FALSE);

        dataStore = spy(new WFSContentDataStore(wfs));
        doReturn(featureType1).when(dataStore).getRemoteFeatureType(TYPE1);
        doReturn(featureType2).when(dataStore).getRemoteFeatureType(TYPE2);
        doReturn(featureType1).when(dataStore).getRemoteSimpleFeatureType(TYPE1);
        doReturn(featureType2).when(dataStore).getRemoteSimpleFeatureType(TYPE2);
    }

    @After
    public void tearDown() throws Exception {
    }

    //TODO: re-enable when transactions are worked out
    @Test
    @Ignore
    public void testAddFeaturesAutoCommit() throws Exception {
        ContentFeatureSource source;

        source = dataStore.getFeatureSource(simpleTypeName1);
        assertNotNull(source);
        assertTrue(source instanceof WFSContentFeatureStore);

        WFSContentFeatureStore store = (WFSContentFeatureStore) source;
        SimpleFeatureCollection collection = null;
        List<FeatureId> fids = store.addFeatures(collection);
    }
}
