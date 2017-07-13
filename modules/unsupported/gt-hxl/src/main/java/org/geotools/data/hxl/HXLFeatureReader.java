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

import java.io.IOException;

import org.geotools.data.Query;
import org.geotools.data.csv.CSVFeatureReader;
import org.geotools.data.csv.parse.CSVStrategy;

/**
 * @author ian
 *
 */
public class HXLFeatureReader extends CSVFeatureReader {

    /**
     * @param csvStrategy
     * 
     * @throws IOException
     */
    public HXLFeatureReader(CSVStrategy csvStrategy) throws IOException {
        super(csvStrategy);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param csvStrategy
     * @param query
     * @throws IOException
     */
    public HXLFeatureReader(CSVStrategy csvStrategy, Query query) throws IOException {
        super(csvStrategy, query);
        // TODO Auto-generated constructor stub
    }

}
