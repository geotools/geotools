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
package org.geotools.data.wfs.internal.parsers;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import javax.xml.namespace.QName;
import org.geotools.data.wfs.internal.GetFeatureParser;
import org.geotools.wfs.v1_1.WFSConfiguration;
import org.opengis.feature.simple.SimpleFeatureType;

public class PullParserTest extends AbstractGetFeatureParserTest {

    public PullParserTest() {
        setSupportsCount(false);
    }

    @Override
    protected GetFeatureParser getParser(
            final QName featureName,
            final URL schemaLocation,
            final SimpleFeatureType featureType,
            final URL getFeaturesRequest,
            String axisOrder)
            throws IOException {

        InputStream inputStream = new BufferedInputStream(getFeaturesRequest.openStream());
        GetFeatureParser parser =
                new PullParserFeatureReader(
                        new WFSConfiguration(), inputStream, featureType, axisOrder);
        return parser;
    }

    public void testParseGeoServer_States_100() {
        // TODO: support custom number format parsing in coordinates, such as
        // <gml:coordinates xmlns:gml="http://www.opengis.net/gml" decimal="#" cs="$" ts="_">
        // 0#0$0#0_0#0$10#0_10#0$10#0_10#0$0#0_0#0$0#0
        // </gml:coordinates>
    }
}
