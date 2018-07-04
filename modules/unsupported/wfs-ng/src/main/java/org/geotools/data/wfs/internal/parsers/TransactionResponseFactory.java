/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008-2014, Open Source Geospatial Foundation (OSGeo)
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
import org.geotools.data.ows.HTTPResponse;
import org.geotools.data.wfs.internal.TransactionRequest;
import org.geotools.data.wfs.internal.Versions;
import org.geotools.data.wfs.internal.WFSOperationType;
import org.geotools.data.wfs.internal.WFSRequest;
import org.geotools.data.wfs.internal.WFSResponse;
import org.geotools.ows.ServiceException;

public class TransactionResponseFactory extends AbstractWFSResponseFactory {

    private static final List<String> SUPPORTED_OUTPUT_FORMATS =
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
                            "text/gml; subtype=gml/3.1.1", // the incorrectly advertised GeoServer
                            // format
                            "text/xml", // oddly, GeoServer returns plain 'text/xml' instead of the
                            // propper
                            // subtype when resultType=hits. Guess we should make this something
                            // the specific strategy can hanlde?
                            "text/xml; charset=UTF-8",
                            "GML2", //
                            "text/xml; subtype=gml/2.1.2", //
                            "application/xml", //
                            "text/xml; subtype=gml/3.2", //
                            "application/gml+xml; version=3.2", //
                            "gml32" //
                            ));

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public boolean canProcess(WFSRequest originatingRequest, String contentType) {
        if (!(originatingRequest instanceof TransactionRequest)) {
            return false;
        }
        List<String> supportedOutputFormats = getSupportedOutputFormats();
        return supportedOutputFormats.contains(originatingRequest.getOutputFormat());
    }

    @Override
    public boolean canProcess(WFSOperationType operation) {
        return WFSOperationType.TRANSACTION.equals(operation);
    }

    @Override
    public List<String> getSupportedOutputFormats() {
        return SUPPORTED_OUTPUT_FORMATS;
    }

    @Override
    protected WFSResponse createResponseImpl(
            WFSRequest request, HTTPResponse response, InputStream in) throws IOException {
        try {
            if (Versions.v2_0_0.toString().equals(request.getStrategy().getVersion())) {
                return new org.geotools.data.wfs.internal.v2_0.TransactionResponseImpl(
                        request, response, in);
            } else if (Versions.v1_0_0.toString().equals(request.getStrategy().getVersion())
                    || Versions.v1_1_0.toString().equals(request.getStrategy().getVersion())) {
                return new org.geotools.data.wfs.internal.v1_x.TransactionResponseImpl(
                        request, response, in);
            }
            return null;
        } catch (ServiceException e) {
            throw new IOException(e);
        }
    }

    @Override
    protected boolean isValidResponseHead(String head) {
        return head.indexOf("TransactionResponse") > 0;
    }
}
