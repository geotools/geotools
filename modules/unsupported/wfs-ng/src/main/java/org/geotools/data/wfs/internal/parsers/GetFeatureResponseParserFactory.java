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
import org.geotools.data.wfs.internal.GetFeatureParser;
import org.geotools.data.wfs.internal.GetFeatureRequest;
import org.geotools.data.wfs.internal.Versions;
import org.geotools.wfs.v1_0.WFSConfiguration_1_0;
import org.geotools.xsd.Configuration;
import org.opengis.feature.type.FeatureType;

/**
 * A WFS response parser factory for GetFeature requests in GML output formats.
 *
 * <p>Should eventually completely replace GmlGetFeatureResponseParserFactory as soon as it has been
 * proven to work as well. For now only used for wfs 2.0 and gml 3.2.
 *
 * <p>
 */
@SuppressWarnings("nls")
public class GetFeatureResponseParserFactory extends AbstractGetFeatureResponseParserFactory {

    private static final List<String> SUPPORTED_FORMATS =
            Collections.unmodifiableList(
                    Arrays.asList( //
                            "text/xml; subtype=gml/3.1.1", //
                            "text/xml;subtype=gml/3.1.1", //
                            "text/xml; subtype=gml/3.1.1; charset=UTF-8",
                            "text/xml; subtype=gml/3.1.1/profiles/gmlsf/0", //
                            "text/xml;subtype=gml/3.1.1/profiles/gmlsf/0", //
                            "text/xml; subType=gml/3.1.1/profiles/gmlsf/1.0.0/0", //
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
                            "application/xml", //
                            "text/xml; subtype=gml/3.2", //
                            "application/gml+xml; version=3.2", //
                            "gml32" //
                            ));

    private static final List<String> SUPPORTED_VERSIONS =
            Collections.unmodifiableList(
                    Arrays.asList(
                            Versions.v2_0_0.toString()
                            // , Versions.v1_0_0.toString() --disabled for now, so the old factory
                            // is definitely picked
                            // , Versions.v1_1_0.toString() --disabled for now, so the old factory
                            // is definitely picked
                            ));

    @Override
    protected GetFeatureParser parser(GetFeatureRequest request, InputStream in)
            throws IOException {

        FeatureType queryType = request.getQueryType();
        if (queryType == null) {
            queryType = request.getFullType();
        }

        Configuration config = null;
        if (request.getStrategy().getVersion().equals(Versions.v2_0_0.toString())) {
            config = new org.geotools.wfs.v2_0.WFSConfiguration();
        } else if (request.getStrategy().getVersion().equals(Versions.v1_1_0.toString())) {
            config = new org.geotools.wfs.v1_1.WFSConfiguration();
        } else if (request.getStrategy().getVersion().equals(Versions.v1_0_0.toString())) {
            config = new WFSConfiguration_1_0();
        }
        return new PullParserFeatureReader(
                config, in, queryType, request.getStrategy().getConfig().getAxisOrder());
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
