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
