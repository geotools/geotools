package org.geotools.data.wfs.internal;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.geotools.data.ows.HTTPResponse;
import org.geotools.ows.ServiceException;

public class ListStoredQueriesResponse extends WFSResponse {

	public ListStoredQueriesResponse(WFSRequest originatingRequest, HTTPResponse response) throws IOException, ServiceException {
		super(originatingRequest, response);
		
		System.err.println("TODO TODO TODO, ListStoredQueriesResponse()");
		// TODO Auto-generated constructor stub
	}
	
	public List<String> getStoredQueries() {
		return Collections.singletonList("mockito!");
	}

}
