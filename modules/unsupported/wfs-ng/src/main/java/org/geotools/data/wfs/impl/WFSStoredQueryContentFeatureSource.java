/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */

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
