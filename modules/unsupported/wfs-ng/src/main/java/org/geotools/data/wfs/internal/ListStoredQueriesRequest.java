package org.geotools.data.wfs.internal;

import static org.geotools.data.wfs.internal.WFSOperationType.LIST_STORED_QUERIES;

import java.io.IOException;
import java.net.URL;

import org.geotools.data.ows.AbstractRequest;
import org.geotools.data.ows.HTTPResponse;
import org.geotools.data.ows.Request;
import org.geotools.data.ows.Response;
import org.geotools.ows.ServiceException;

public class ListStoredQueriesRequest extends WFSRequest {

	public ListStoredQueriesRequest(WFSConfig config, WFSStrategy strategy) {
		super(LIST_STORED_QUERIES, config, strategy);
	}
/*
	@Override
	public WFSResponse createResponse(HTTPResponse response) throws ServiceException, IOException {
		return new ListStoredQueriesResponse(response);
	}
*/
}
