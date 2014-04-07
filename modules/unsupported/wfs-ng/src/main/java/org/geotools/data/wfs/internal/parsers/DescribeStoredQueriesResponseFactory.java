package org.geotools.data.wfs.internal.parsers;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.geotools.data.ows.HTTPResponse;
import org.geotools.data.wfs.internal.DescribeStoredQueriesRequest;
import org.geotools.data.wfs.internal.DescribeStoredQueriesResponse;
import org.geotools.data.wfs.internal.ListStoredQueriesRequest;
import org.geotools.data.wfs.internal.WFSOperationType;
import org.geotools.data.wfs.internal.WFSRequest;
import org.geotools.data.wfs.internal.WFSResponse;
import org.geotools.data.wfs.internal.WFSResponseFactory;
import org.geotools.ows.ServiceException;

public class DescribeStoredQueriesResponseFactory implements WFSResponseFactory {

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public boolean canProcess(WFSRequest originatingRequest, String contentType) {
        return originatingRequest instanceof ListStoredQueriesRequest
                && (contentType == null || contentType.startsWith("text/xml"));
    }

    @Override
    public boolean canProcess(WFSOperationType operation) {
        return WFSOperationType.DESCRIBE_STORED_QUERIES.equals(operation);
    }

    @Override
    public List<String> getSupportedOutputFormats() {
        return Arrays.asList("", "text/xml", "text/xml; subtype=gml/3.1.1",
                "text/xml; subtype=gml/3.2", "XMLSCHEMA", "text/gml; subtype=gml/3.1.1");
    }

    @Override
    public WFSResponse createResponse(WFSRequest request, HTTPResponse response) throws IOException {
        try {
        	return new DescribeStoredQueriesResponse((DescribeStoredQueriesRequest)request, response);
        } catch (ServiceException e) {
            throw new IOException(e);
        }
    }

}
