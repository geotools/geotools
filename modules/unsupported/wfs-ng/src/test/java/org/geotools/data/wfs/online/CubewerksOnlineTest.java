/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2005, David Zwiers
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.data.wfs.online;

import static org.geotools.data.wfs.online.WFSOnlineTestSupport.doFeatureReader;
import static org.geotools.data.wfs.online.WFSOnlineTestSupport.getDataStore;
import static org.junit.Assert.assertEquals;

import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.geotools.data.wfs.impl.WFSContentDataStore;
import org.geotools.data.wfs.internal.Loggers;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 */
public class CubewerksOnlineTest {

    // old server -- do not test version 0.0.16
    // url = new
    // URL("http://ceoware2.ccrs.nrcan.gc.ca/cubewerx/cwwfs/cubeserv.cgi?datastore=CEOWARE2&version=1.0.0&service=WFS&request=GetCapabilities");
    // url = new
    // URL("http://cgns.nrcan.gc.ca/wfs/cubeserv.cgi?DATASTORE=cgns&REQUEST=GetCapabilities&VERSION=1.0.0&SERVICE=WFS");
    private static String SERVER_URL_100 = "http://demo.cubewerx.com/demo/cubeserv/cubeserv.cgi?SERVICE=wfs&DATASTORE=Foundation&version=1.0.0&request=GetCapabilities";

    private static String SERVER_URL_110 = "http://demo.cubewerx.com/demo/cubeserv/cubeserv.cgi?SERVICE=wfs&DATASTORE=Foundation&version=1.1.0&request=GetCapabilities";

    private static URL url_100;

    private static URL url_110;

    private static WFSContentDataStore wfs100;

    private static WFSContentDataStore wfs110;

    @BeforeClass
    public static void oneTimeSetUp() throws Exception {
        // Logging.GEOTOOLS.forceMonolineConsoleOutput();
        url_100 = new URL(SERVER_URL_100);
        url_110 = new URL(SERVER_URL_110);
        if (url_100 != null && url_100.toString().indexOf("localhost") != -1) {
            InputStream stream = null;
            try {
                stream = url_100.openStream();
            } catch (Throwable t) {
                System.err.println("Server is not available. test disabled ");
                url_100 = null;
                url_110 = null;
            } finally {
                IOUtils.closeQuietly(stream);
            }
        }
        if (url_100 != null) {
            Loggers.info("Creating 1.0.0 datastore from ", url_100);
            try {
                wfs100 = getDataStore(url_100, null);
            } catch (Throwable t) {
                url_100 = null;
                Loggers.info("Can't obtain 1.0.0 DataStore, 1.0.0 tests disabled: "
                        + t.getMessage());
            }
            assertEquals("1.0.0", wfs100.getInfo().getVersion());
        }
        if (url_110 != null) {
            Loggers.info("Creating 1.1.0 datastore from ", url_110);
            try {
                wfs110 = getDataStore(url_110, null);
            } catch (Exception t) {
                url_110 = null;
                Loggers.info("Can't obtain 1.1.0 DataStore, 1.1.0 tests disabled: "
                        + t.getMessage());
            }
            assertEquals("1.1.0", wfs110.getInfo().getVersion());
        }
    }

    @AfterClass
    public static void oneTimeTearDown() throws Exception {
        if (wfs100 != null) {
            wfs100.dispose();
        }
        if (wfs110 != null) {
            wfs110.dispose();
        }
    }

    @Test
    public void testFeatureType_1_0_0() throws Exception {
        if (wfs100 == null) {
            return;
        }
        List<String> typeNames = Arrays.asList(wfs100.getTypeNames()).subList(0, 5);
        for (String typeName : typeNames) {
            WFSOnlineTestSupport.doFeatureType(wfs100, typeName);
        }
    }

    @Test
    public void testFeatureType_1_1_0() throws Exception {
        if (wfs110 == null) {
            return;
        }
        List<String> typeNames = Arrays.asList(wfs110.getTypeNames()).subList(0, 5);
        for (String typeName : typeNames) {
            WFSOnlineTestSupport.doFeatureType(wfs110, typeName);
        }
    }

    @Test
    public void testFeatureReader_1_0_0() throws Exception {
        if (wfs100 == null) {
            return;
        }
        List<String> typeNames = Arrays.asList(wfs100.getTypeNames()).subList(0, 5);
        for (String typeName : typeNames) {
            doFeatureReader(wfs100, typeName);
        }
    }

    @Test
    public void testFeatureReader_1_1_0() throws Exception {
        if (wfs110 == null) {
            return;
        }
        List<String> typeNames = Arrays.asList(wfs110.getTypeNames()).subList(0, 5);
        for (String typeName : typeNames) {
            doFeatureReader(wfs110, typeName);
        }
    }

    //
    // public void testFeatureReaderWithFilter() throws NoSuchElementException,
    // IllegalAttributeException, IOException, SAXException {
    // WFSDataStoreTestSupport.doFeatureReaderWithQuery(url, true, true, 6);
    // }
}
