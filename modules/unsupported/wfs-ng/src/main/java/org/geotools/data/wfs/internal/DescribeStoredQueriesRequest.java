package org.geotools.data.wfs.internal;

import static org.geotools.data.wfs.internal.WFSOperationType.DESCRIBE_STORED_QUERIES;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class DescribeStoredQueriesRequest extends WFSRequest {

	private List<URI> storedQueryIds;
	
	public DescribeStoredQueriesRequest(WFSConfig config, WFSStrategy strategy) {
		super(DESCRIBE_STORED_QUERIES, config, strategy);
		
		storedQueryIds = new ArrayList<URI>();
	}
	
	public List<URI> getStoredQueryIds() {
		return storedQueryIds;
	}

}
