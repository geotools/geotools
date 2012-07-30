package org.geotools.data.wfs.impl;

import static org.geotools.data.DataUtilities.createType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;

import javax.xml.namespace.QName;

import org.geotools.data.store.ContentFeatureSource;
import org.geotools.data.wfs.internal.DescribeFeatureTypeRequest;
import org.geotools.data.wfs.internal.DescribeFeatureTypeResponse;
import org.geotools.data.wfs.internal.WFSClient;
import org.geotools.feature.NameImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.Name;

public class WFSContentDataStoreTest {

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
        //
    }

    @Test
    public void testCreateTypeNames() throws Exception {
        List<Name> names = dataStore.createTypeNames();
        assertNotNull(names);
        assertEquals(2, names.size());
        assertTrue(names.contains(new NameImpl(simpleTypeName1)));
        assertTrue(names.contains(new NameImpl(simpleTypeName2)));
    }

    @Test
    public void testCreateTypeNamesNS() throws Exception {
        final String nsOverride = "http://geotools.org";
        dataStore.setNamespaceURI(nsOverride);

        List<Name> names = dataStore.createTypeNames();
        assertNotNull(names);
        assertEquals(2, names.size());
        assertTrue(names.contains(new NameImpl(nsOverride, simpleTypeName1)));
        assertTrue(names.contains(new NameImpl(nsOverride, simpleTypeName2)));
    }

    @Test
    public void testGetRemoteTypeName() throws Exception {
        final String nsOverride = "http://geotools.org";
        dataStore.setNamespaceURI(nsOverride);

        assertEquals(TYPE1, dataStore.getRemoteTypeName(new NameImpl(nsOverride, simpleTypeName1)));
        assertEquals(TYPE2, dataStore.getRemoteTypeName(new NameImpl(nsOverride, simpleTypeName2)));
        try {
            Name badName = new NameImpl(TYPE1.getNamespaceURI(), TYPE1.getLocalPart() + "2");
            dataStore.getRemoteTypeName(badName);
            fail("Expected NSEE");
        } catch (NoSuchElementException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testGetRemoteFeatureType() throws Exception {

        DescribeFeatureTypeRequest req = mock(DescribeFeatureTypeRequest.class);
        when(wfs.createDescribeFeatureTypeRequest()).thenReturn(req);

        DescribeFeatureTypeResponse resp = mock(DescribeFeatureTypeResponse.class);
        when(wfs.issueRequest(same(req))).thenReturn(resp);
        when(resp.getFeatureType()).thenReturn(featureType2);

        dataStore = spy(new WFSContentDataStore(wfs));

        assertSame(featureType2, dataStore.getRemoteSimpleFeatureType(TYPE2));

        verify(req).setTypeName(eq(TYPE2));

        when(wfs.issueRequest(same(req))).thenThrow(new IOException());
        try {
            QName badName = new QName(TYPE1.getNamespaceURI(), TYPE1.getLocalPart() + "2");
            dataStore.getRemoteSimpleFeatureType(badName);
            fail("Expected IOE");
        } catch (IOException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testGetFeatureSource() throws Exception {
        ContentFeatureSource source;

        source = dataStore.getFeatureSource(simpleTypeName1);
        assertNotNull(source);
        assertTrue(source instanceof WFSContentFeatureStore);

        source = dataStore.getFeatureSource(simpleTypeName2);
        assertNotNull(source);
        assertTrue(source instanceof WFSContentFeatureSource);
        assertFalse(source instanceof WFSContentFeatureStore);
    }
}
