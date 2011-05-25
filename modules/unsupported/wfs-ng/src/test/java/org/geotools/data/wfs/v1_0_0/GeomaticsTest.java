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
package org.geotools.data.wfs.v1_0_0;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.NoSuchElementException;

import junit.framework.TestCase;

import org.geotools.feature.IllegalAttributeException;
import org.xml.sax.SAXException;

/**
 *  summary sentence.
 * <p>
 * Paragraph ...
 * </p><p>
 * Responsibilities:
 * <ul>
 * <li>
 * <li>
 * </ul>
 * </p><p>
 * Example:<pre><code>
 * GeoServer x = new GeoServer( ... );
 * TODO code example
 * </code></pre>
 * </p>
 * @author dzwiers
 * @since 0.6.0
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/unsupported/wfs-ng/src/test/java/org/geotools/data/wfs/v1_0_0/GeomaticsTest.java $
 */
public class GeomaticsTest extends TestCase {

    private URL url = null;
    
    public GeomaticsTest() throws MalformedURLException{
        url = new URL("http://gws2.pcigeomatics.com/wfs1.0.0/wfs?service=WFS&request=getcapabilities");
    }
    
    public void testFeatureType() throws NoSuchElementException, IOException, SAXException{
//        WFSDataStoreReadTest.doFeatureType(url,true,false,2);
    }
    public void testFeatureReader() throws NoSuchElementException, IOException, IllegalAttributeException, SAXException{
        // FAILS due to Choice !!!
//        WFSDataStoreReadTest.doFeatureReader(url,true,false,2);
    }
    public void testFeatureReaderWithFilter() throws NoSuchElementException, IllegalAttributeException, IOException, SAXException{
//        WFSDataStoreReadTest.doFeatureReaderWithFilter(url,true,false,2);
    }
}
