package org.geotools.data.wfs.impl;

import java.io.IOException;
import java.util.logging.Logger;

import net.opengis.wfs20.StoredQueryDescriptionType;

import org.geotools.data.Query;
import org.geotools.data.store.ContentEntry;
import org.geotools.data.wfs.internal.GetFeatureRequest;
import org.geotools.data.wfs.internal.WFSClient;
import org.geotools.data.wfs.internal.GetFeatureRequest.ResultType;
import org.geotools.util.logging.Logging;

public class WFSStoredQueryContentFeatureSource extends WFSContentFeatureSource {

	private static final Logger LOGGER = Logging.getLogger(WFSStoredQueryContentFeatureSource.class);

	private final StoredQueryDescriptionType desc;
	
    public WFSStoredQueryContentFeatureSource(final ContentEntry entry, final WFSClient client,
    		StoredQueryDescriptionType desc) {
        super(entry, client);
        this.desc = desc;
    }

    @Override
    protected GetFeatureRequest createGetFeature(Query query,
    		ResultType resultType) throws IOException
    {
    	GetFeatureRequest request = super.createGetFeature(query, resultType);
    	request.setStoredQuery(true);
    	request.setStoredQueryDescriptionType(desc);
    	return request;
    }
}
