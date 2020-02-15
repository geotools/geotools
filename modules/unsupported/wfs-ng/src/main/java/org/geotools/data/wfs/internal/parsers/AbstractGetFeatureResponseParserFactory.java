/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2016, Open Source Geospatial Foundation (OSGeo)
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
import java.util.List;
import net.opengis.wfs.GetFeatureType;
import org.geotools.data.ows.HTTPResponse;
import org.geotools.data.wfs.internal.GetFeatureRequest;
import org.geotools.data.wfs.internal.GetFeatureResponse;
import org.geotools.data.wfs.internal.GetParser;
import org.geotools.data.wfs.internal.WFSOperationType;
import org.geotools.data.wfs.internal.WFSRequest;
import org.geotools.data.wfs.internal.WFSResponse;
import org.geotools.data.wfs.internal.WFSResponseFactory;
import org.geotools.ows.ServiceException;
import org.opengis.feature.simple.SimpleFeature;

/** An abstract WFS response parser factory for GetFeature requests in GML output formats. */
@SuppressWarnings("nls")
public abstract class AbstractGetFeatureResponseParserFactory extends AbstractWFSResponseFactory {

    /** @see WFSResponseFactory#isAvailable() */
    public boolean isAvailable() {
        return true;
    }

    /**
     * Checks if this factory can create a parser for the potential responses of the given WFS
     * request.
     *
     * <p>For instance, this factory can create a parser as long as the request is a {@link
     * GetFeatureType GetFeature} request and the request output format matches {@code "text/xml;
     * subtype=gml/3.1.1"}.
     *
     * @see WFSResponseFactory#canProcess(WFSOperationType, String)
     */
    public boolean canProcess(final WFSRequest request, final String contentType) {
        if (!WFSOperationType.GET_FEATURE.equals(request.getOperation())) {
            return false;
        }
        if (!getSupportedVersions().contains(request.getStrategy().getVersion())) {
            return false;
        }
        // String outputFormat = ((GetFeatureRequest) request).getOutputFormat();
        boolean matches = getSupportedOutputFormats().contains(contentType);
        if (!matches) {
            // fuzy search, "
            for (String supported : getSupportedOutputFormats()) {
                if (supported.startsWith(contentType) || contentType.startsWith(supported)) {
                    matches = true;
                    break;
                }
            }
        }
        return matches;
    }

    protected WFSResponse createResponseImpl(
            WFSRequest request, HTTPResponse response, InputStream in) throws IOException {
        GetParser<SimpleFeature> parser = parser((GetFeatureRequest) request, in);
        try {
            return new GetFeatureResponse(request, response, parser);
        } catch (ServiceException e) {
            throw new IOException(e);
        }
    }

    /** @param head The first couple of characters from the response, typically the first 512 */
    protected boolean isValidResponseHead(String head) {
        return head.indexOf("FeatureCollection") > 0;
    }

    @Override
    public boolean canProcess(WFSOperationType operation) {
        return WFSOperationType.GET_FEATURE.equals(operation);
    }

    protected abstract GetParser<SimpleFeature> parser(GetFeatureRequest request, InputStream in)
            throws IOException;

    protected abstract List<String> getSupportedVersions();
}
