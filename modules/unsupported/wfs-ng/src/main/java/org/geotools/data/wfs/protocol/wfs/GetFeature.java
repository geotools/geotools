/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.wfs.protocol.wfs;

import org.opengis.filter.Filter;
import org.opengis.filter.sort.SortBy;

public interface GetFeature {

    public enum ResultType {
        RESULTS, HITS;
    };

    String getTypeName();

    String[] getPropertyNames();

    String getOutputFormat();

    String getSrsName();

    Filter getFilter();

    Integer getMaxFeatures();

    ResultType getResultType();

    SortBy[] getSortBy();
}
