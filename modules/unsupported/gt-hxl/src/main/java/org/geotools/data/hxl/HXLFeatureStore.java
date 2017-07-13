/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.hxl;

import org.geotools.data.Query;
import org.geotools.data.csv.CSVFeatureStore;
import org.geotools.data.csv.CSVFileState;
import org.geotools.data.csv.parse.CSVStrategy;
import org.geotools.data.store.ContentEntry;

/**
 * @author ian
 *
 */
public class HXLFeatureStore extends CSVFeatureStore {

    /**
     * @param csvStrategy
     * @param csvFileState
     * @param entry
     * @param query
     */
    public HXLFeatureStore(CSVStrategy csvStrategy, CSVFileState csvFileState, ContentEntry entry,
            Query query) {
        super(csvStrategy, csvFileState, entry, query);
        // TODO Auto-generated constructor stub
    }

}
