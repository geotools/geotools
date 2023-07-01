/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2022, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.data.wfs.internal.parsers;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import org.geotools.data.wfs.internal.ComplexGetFeatureResponse;
import org.geotools.data.wfs.internal.GetFeatureRequest;
import org.geotools.data.wfs.internal.GetParser;
import org.geotools.data.wfs.internal.WFSRequest;
import org.geotools.data.wfs.internal.WFSResponse;
import org.geotools.http.HTTPResponse;
import org.geotools.ows.ServiceException;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.FeatureType;

/**
 * Creating GetFeatureResponse parsers that can treat featureType that don't extend
 * SimpleFeatureType.
 *
 * <p>Treats the same OutputFormats and Versions as GetFeatureResponseParserFactory.
 *
 * @author Roar Brænden
 */
public class ComplexGetFeatureResponseParserFactory
        extends AbstractGetFeatureResponseParserFactory {

    /**
     * Supporting GetFeature requests that also have request.getFullType() not being a
     * SimpleFeatureType.
     */
    @Override
    public boolean canProcess(WFSRequest request, String contentType) {
        if (!super.canProcess(request, contentType)) {
            return false;
        }
        if (getRequestedType((GetFeatureRequest) request) instanceof SimpleFeatureType) {
            return false;
        }
        return true;
    }

    @Override
    public List<String> getSupportedOutputFormats() {
        return GetFeatureResponseParserFactory.SUPPORTED_FORMATS;
    }

    @Override
    protected List<String> getSupportedVersions() {
        return GetFeatureResponseParserFactory.SUPPORTED_VERSIONS;
    }

    /**
     * Wrapping a XmlComplexFeatureParser over the input stream.
     *
     * <p>Using "in" instead of response.getResponseStream()
     */
    @Override
    protected WFSResponse createResponseImpl(
            WFSRequest request, HTTPResponse response, InputStream in) throws IOException {
        FeatureType schema =
                ((GetFeatureRequest) request).getQueryType() == null
                        ? ((GetFeatureRequest) request).getFullType()
                        : ((GetFeatureRequest) request).getQueryType();
        XmlComplexFeatureParser parser =
                new XmlComplexFeatureParser(
                        in, schema, request.getTypeName(), null, request.getStrategy());
        try {
            return new ComplexGetFeatureResponse(request, response, parser);
        } catch (ServiceException e) {
            throw new IOException("Server responded with error: " + e.getMessage(), e);
        }
    }

    @Override
    protected GetParser<SimpleFeature> parser(GetFeatureRequest request, InputStream in)
            throws IOException {
        throw new UnsupportedOperationException("We don't support parsing SimpleFeature's.");
    }
}
