/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.wfs.internal.v1_0.legacy;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.NoSuchElementException;

import junit.framework.TestCase;

import org.geotools.feature.IllegalAttributeException;
import org.xml.sax.SAXException;

/**
 */
public class DMSolutionOnlineTest extends TestCase {

    private URL url = null;

    public DMSolutionOnlineTest() throws MalformedURLException {
        url = new URL(
                "http://www2.dmsolutions.ca/cgi-bin/mswfs_gmap?version=1.0.0&request=getcapabilities&service=wfs");
    }

    public void testFeatureType() throws NoSuchElementException, IOException, SAXException {
        WFSDataStoreReadTest.doFeatureType(url, true, true, 0);
    }

    public void testFeatureReader() throws NoSuchElementException, IOException,
            IllegalAttributeException, SAXException {
        WFSDataStoreReadTest.doFeatureReader(url, true, true, 0);
    }

    public void testFeatureReaderWithFilter() throws NoSuchElementException,
            IllegalAttributeException, IOException, SAXException {
        WFSDataStoreReadTest.doFeatureReaderWithQuery(url, true, true, 0);
    }
}
