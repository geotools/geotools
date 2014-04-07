package org.geotools.data.wfs.internal;

import static org.geotools.data.wfs.internal.WFSOperationType.LIST_STORED_QUERIES;

public class ListStoredQueriesRequest extends WFSRequest {

	public ListStoredQueriesRequest(WFSConfig config, WFSStrategy strategy) {
		super(LIST_STORED_QUERIES, config, strategy);
	}

}
