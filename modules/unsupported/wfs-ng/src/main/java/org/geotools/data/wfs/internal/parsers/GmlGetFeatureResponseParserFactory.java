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
package org.geotools.data.wfs.internal.parsers;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.xml.namespace.QName;
import org.geotools.data.wfs.internal.GetFeatureParser;
import org.geotools.data.wfs.internal.GetFeatureRequest;
import org.geotools.data.wfs.internal.Versions;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.FeatureType;

/**
 * A WFS response parser factory for GetFeature requests in GML output formats.
 *
 * <p>Supports only GML 2 and GML 3.1
 *
 * <p>//@deprecated should be removed as long as {@link GetFeatureResponseParserFactory} works well
 */
@SuppressWarnings("nls")
public class GmlGetFeatureResponseParserFactory extends AbstractGetFeatureResponseParserFactory {

    private static final List<String> SUPPORTED_FORMATS =
            Collections.unmodifiableList(
                    Arrays.asList( //
                            "text/xml; subtype=gml/3.1.1", //
                            "text/xml;subtype=gml/3.1.1", //
                            "text/xml; subtype=gml/3.1.1; charset=UTF-8",
                            "text/xml; subtype=gml/3.1.1/profiles/gmlsf/0", //
                            "text/xml;subtype=gml/3.1.1/profiles/gmlsf/0", //
                            "application/gml+xml; subtype=gml/3.1.1", //
                            "application/gml+xml;subtype=gml/3.1.1", //
                            "application/gml+xml; subtype=gml/3.1.1/profiles/gmlsf/0", //
                            "application/gml+xml;subtype=gml/3.1.1/profiles/gmlsf/0", //
                            "GML3", //
                            "GML3L0", //
                            "text/xml", // oddly, GeoServer returns plain 'text/xml' instead of the
                            // propper
                            // subtype when resultType=hits. Guess we should make this something
                            // the specific strategy can hanlde?
                            "text/xml; charset=UTF-8",
                            "text/gml; subtype=gml/3.1.1", // the incorrectly advertised GeoServer
                            // format
                            "GML2", //
                            "text/xml; subtype=gml/2.1.2", //
                            "application/xml" //
                            ));

    private static final List<String> SUPPORTED_VERSIONS =
            Collections.unmodifiableList(
                    Arrays.asList(Versions.v1_0_0.toString(), Versions.v1_1_0.toString()));

    @Override
    protected GetFeatureParser parser(GetFeatureRequest request, InputStream in)
            throws IOException {

        final QName remoteFeatureName = request.getTypeName();

        FeatureType queryType = request.getQueryType();
        if (queryType == null) {
            queryType = request.getFullType();
        }
        if (!(queryType instanceof SimpleFeatureType)) {
            throw new UnsupportedOperationException();
        }

        SimpleFeatureType schema = (SimpleFeatureType) queryType;

        GetFeatureParser featureReader =
                new XmlSimpleFeatureParser(
                        in,
                        schema,
                        remoteFeatureName,
                        request.getStrategy().getConfig().getAxisOrder());
        return featureReader;
    }

    @Override
    public List<String> getSupportedOutputFormats() {
        return SUPPORTED_FORMATS;
    }

    @Override
    protected List<String> getSupportedVersions() {
        return SUPPORTED_VERSIONS;
    }
}
