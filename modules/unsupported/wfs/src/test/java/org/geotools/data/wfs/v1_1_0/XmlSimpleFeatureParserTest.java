/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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
import org.geotools.data.wfs.v1_1_0.parsers.XmlSimpleFeatureParser;
import org.opengis.feature.simple.SimpleFeatureType;

public class XmlSimpleFeatureParserTest extends AbstractGetFeatureParserTest {

    @Override
    protected GetFeatureParser getParser(final QName featureName, final String schemaLocation,
            final SimpleFeatureType featureType, final URL getFeaturesRequest) throws IOException {

        InputStream inputStream = new BufferedInputStream(getFeaturesRequest.openStream());
        GetFeatureParser parser = new XmlSimpleFeatureParser(inputStream,  featureType, featureName);
        return parser;
    }

    /**
     * This is to be run as a normal java application in order to reproduce a
     * GetFeature request to the nsdi server and thus being able to
     * assess/profile the OutOfMemory errors I'm getting in uDig
     * 
     * @param argv
     */
    public static void main(String argv[]) {
        XmlSimpleFeatureParserTest test;
        test = new XmlSimpleFeatureParserTest();
        try {
            test.runGetFeaturesParsing();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
