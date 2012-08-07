package org.geotools.data.wfs.internal.parsers;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.geotools.data.ows.HTTPResponse;
import org.geotools.data.wfs.internal.TransactionRequest;
import org.geotools.data.wfs.internal.TransactionResponse;
import org.geotools.data.wfs.internal.WFSOperationType;
import org.geotools.data.wfs.internal.WFSRequest;
import org.geotools.data.wfs.internal.WFSResponse;
import org.geotools.data.wfs.internal.WFSResponseFactory;
import org.geotools.ows.ServiceException;

public class WFS_1_1_TransactionResponseFactory implements WFSResponseFactory {

    private static final List<String> SUPPORTED_OUTPUT_FORMATS = Collections
            .unmodifiableList(Arrays.asList(//
                    "text/xml; subtype=gml/3.1.1",//
                    "text/xml;subtype=gml/3.1.1",//
                    "text/xml; subtype=gml/3.1.1/profiles/gmlsf/0",//
                    "text/xml;subtype=gml/3.1.1/profiles/gmlsf/0",//
                    "application/gml+xml; subtype=gml/3.1.1",//
                    "application/gml+xml;subtype=gml/3.1.1",//
                    "application/gml+xml; subtype=gml/3.1.1/profiles/gmlsf/0",//
                    "application/gml+xml;subtype=gml/3.1.1/profiles/gmlsf/0",//
                    "GML3", //
                    "GML3L0", //
                    "text/gml; subtype=gml/3.1.1"// the incorrectly advertised GeoServer format
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
    public WFSResponse createResponse(WFSRequest request, HTTPResponse response) throws IOException {
        try {
            return new TransactionResponse(request, response);
        } catch (ServiceException e) {
            throw new IOException(e);
        }
    }

}
