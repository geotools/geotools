package org.geotools.data.wfs.impl;

import java.io.IOException;
import java.util.logging.Logger;

import javax.xml.namespace.QName;

import net.opengis.wfs20.StoredQueryDescriptionType;

import org.geotools.data.FeatureReader;
import org.geotools.data.Query;
import org.geotools.data.store.ContentEntry;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.data.wfs.internal.FeatureTypeInfo;
import org.geotools.data.wfs.internal.WFSClient;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.util.logging.Logging;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

public class WFSStoredQueryContentFeatureSource extends WFSContentFeatureSource {

	private static final Logger LOGGER = Logging.getLogger(WFSStoredQueryContentFeatureSource.class);

	private final StoredQueryDescriptionType desc;
	
    public WFSStoredQueryContentFeatureSource(final ContentEntry entry, final WFSClient client,
    		StoredQueryDescriptionType desc) {
        super(entry, client);
        this.desc = desc;
    }

    @Override
    protected FeatureReader<SimpleFeatureType, SimpleFeature> getReaderInternal(
    		Query localQuery) throws IOException {
    	System.err.println("TODO: implement query with storedquery_id and parameters!");
    	return super.getReaderInternal(localQuery);
    }
}
