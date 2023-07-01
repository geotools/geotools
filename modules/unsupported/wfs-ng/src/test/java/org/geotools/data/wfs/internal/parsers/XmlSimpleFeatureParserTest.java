/*
 * GeoTools - The Open Source Java GIS Toolkit http://geotools.org
 *
 * (C) 2008, Open Source Geospatial Foundation (OSGeo)
 *
 * This library is free software; you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free Software Foundation; version 2.1 of
 * the License.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 */
package org.geotools.data.wfs.internal.parsers;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import javax.xml.namespace.QName;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.data.wfs.internal.GetParser;
import org.geotools.xsd.Configuration;
import org.junit.Ignore;
import org.junit.Test;

public class XmlSimpleFeatureParserTest extends AbstractGetFeatureParserTest {

    @Override
    @SuppressWarnings("PMD.CloseResource") // wrapped and returned
    protected GetParser<SimpleFeature> getParser(
            final QName featureName,
            final URL schemaLocation,
            final SimpleFeatureType featureType,
            final URL getFeaturesRequest,
            String axisOrder,
            Configuration wfsConfiguration)
            throws IOException {

        InputStream inputStream = new BufferedInputStream(getFeaturesRequest.openStream());
        GetParser<SimpleFeature> parser =
                new XmlSimpleFeatureParser(inputStream, featureType, featureName, axisOrder);
        return parser;
    }

    @Override
    @Test
    @Ignore
    public void testParseWfs200Gml32() throws Exception {
        // parser is not supporting wfs 2.0.0/gml 3.2 so this impl is empty
    }
}
