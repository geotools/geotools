/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.wfs.v1_1_0;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.xml.namespace.QName;

import org.geotools.data.wfs.protocol.wfs.GetFeatureParser;
import org.geotools.test.TestData;
import org.geotools.wfs.v1_1.WFSConfiguration;
import org.geotools.xml.Configuration;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * Test suite for the {@link StreamingParserFeatureReader} strategy.
 * 
 * @author Gabriel Roldan
 * @version $Id: StreamingParserFeatureReaderTest.java 32043 2008-12-19 03:23:35Z jdeolive $
 * @since 2.5.x
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/unsupported/wfs-ng/src/test/java/org/geotools/data/wfs/v1_1_0/StreamingParserFeatureReaderTest.java $
 *         http://svn.geotools.org/geotools/trunk/gt/modules/plugin/wfs/src/test/java/org/geotools
 *         /wfs/v_1_1_0/data/StreamingParserFeatureReaderTest.java $
 */
public class StreamingParserFeatureReaderTest extends AbstractGetFeatureParserTest {

    @Override
    protected GetFeatureParser getParser( QName featureName, String schemaLocation,
            SimpleFeatureType featureType, URL getFeaturesRequest ) throws IOException {
        URL schemaLocationUrl = TestData.getResource(this, schemaLocation);

        Configuration configuration = new WFSConfiguration();
        final InputStream inputStream = new BufferedInputStream(getFeaturesRequest.openStream());
        final StreamingParserFeatureReader featureReader;
        featureReader = new StreamingParserFeatureReader(configuration, inputStream, featureName,
                schemaLocationUrl);
        return featureReader;
    }

    /**
     * This is to be run as a normal java application in order to reproduce a GetFeature request to
     * the nsdi server and thus being able to assess/profile the OutOfMemory errors I'm getting in
     * uDig
     * 
     * @param argv
     */
    public static void main( String argv[] ) {
        StreamingParserFeatureReaderTest test;
        test = new StreamingParserFeatureReaderTest();
        try {
            test.runGetFeaturesParsing();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
