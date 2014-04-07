package org.geotools.data.wfs.internal;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import net.opengis.wfs20.StoredQueryDescriptionType;

import org.geotools.data.ows.HTTPResponse;
import org.geotools.ows.ServiceException;

public class DescribeStoredQueriesResponse extends WFSResponse {

	public DescribeStoredQueriesResponse(WFSRequest originatingRequest, HTTPResponse response) throws IOException, ServiceException {
		super(originatingRequest, response);
		
		System.err.println("TODO TODO TODO, DescribeStoredQueriesResponse()");
		// TODO Auto-generated constructor stub
	}
	
	public List<StoredQueryDescriptionType> getStoredQueryDescriptions() {
		System.err.println("DescribeStoredQueriesResponse.getStoredQueryDescriptions() NOT IMPLEMENTED");
		return Collections.emptyList();
	}

}
