package org.geotools.data.wfs.internal.parsers;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.geotools.data.ows.HTTPResponse;
import org.geotools.data.wfs.internal.DescribeFeatureTypeRequest;
import org.geotools.data.wfs.internal.DescribeFeatureTypeResponse;
import org.geotools.data.wfs.internal.WFSOperationType;
import org.geotools.data.wfs.internal.WFSRequest;
import org.geotools.data.wfs.internal.WFSResponse;
import org.geotools.data.wfs.internal.WFSResponseFactory;
import org.geotools.ows.ServiceException;

public class DescribeFeatureTypeResponseFactory implements WFSResponseFactory {

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public boolean canProcess(WFSRequest originatingRequest, String contentType) {
        return originatingRequest instanceof DescribeFeatureTypeRequest
                && (contentType == null || contentType.startsWith("text/xml"));
    }

    @Override
    public boolean canProcess(WFSOperationType operation) {
        return WFSOperationType.DESCRIBE_FEATURETYPE.equals(operation);
    }

    @Override
    public List<String> getSupportedOutputFormats() {
        return Arrays.asList("text/xml", "text/xml; subtype=gml/3.1.1",
                "text/xml; subtype=gml/3.2", "XMLSCHEMA", "text/gml; subtype=gml/3.1.1");
    }

    @Override
    public WFSResponse createResponse(WFSRequest request, HTTPResponse response) throws IOException {
        try {
            return new DescribeFeatureTypeResponse((DescribeFeatureTypeRequest) request, response);
        } catch (ServiceException e) {
            throw new IOException(e);
        }
    }

}
