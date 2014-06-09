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
package org.geotools.data.wfs.internal;

import static org.geotools.data.wfs.internal.WFSOperationType.GET_FEATURE;

import org.opengis.feature.type.FeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.sort.SortBy;

/**
 */
public class GetFeatureRequest extends WFSRequest {

    public enum ResultType {
        RESULTS, HITS;
    }

    private String[] propertyNames;

    private String srsName;

    private Filter filter;

    private Integer maxFeatures;

    private ResultType resultType;

    private SortBy[] sortBy;

    private FeatureType fullType;

    private FeatureType queryType;

    private Filter unsupportedFilter;;

    GetFeatureRequest(WFSConfig config, WFSStrategy strategy) {
        super(GET_FEATURE, config, strategy);
        resultType = ResultType.RESULTS;
    }

    //
    // public GetFeatureRequest(GetFeatureRequest query) {
    // setFilter(query.getFilter());
    // setMaxFeatures(query.getMaxFeatures());
    // setOutputFormat(query.getOutputFormat());
    // setPropertyNames(query.getPropertyNames());
    // setResultType(query.getResultType());
    // setSortBy(query.getSortBy());
    // setSrsName(query.getSrsName());
    // }

    public String[] getPropertyNames() {
        return propertyNames;
    }

    public String getSrsName() {
        return srsName;
    }

    public Filter getFilter() {
        return filter;
    }

    public Integer getMaxFeatures() {
        return maxFeatures;
    }

    public ResultType getResultType() {
        return resultType;
    }

    public SortBy[] getSortBy() {
        return sortBy;
    }

    /**
     * @param propertyNames
     *            the propertyNames to set
     */
    public void setPropertyNames(String[] propertyNames) {
        this.propertyNames = propertyNames;
    }

    /**
     * @param srsName
     *            the srsName to set
     */
    public void setSrsName(String srsName) {
        this.srsName = srsName;
    }

    /**
     * @param filter
     *            the filter to set
     */
    public void setFilter(Filter filter) {
        this.filter = filter;
    }

    /**
     * @param maxFeatures
     *            the maxFeatures to set
     */
    public void setMaxFeatures(Integer maxFeatures) {
        this.maxFeatures = maxFeatures;
    }

    /**
     * @param resultType
     *            the resultType to set
     */
    public void setResultType(ResultType resultType) {
        this.resultType = resultType;
    }

    /**
     * @param sortBy
     *            the sortBy to set
     */
    public void setSortBy(SortBy[] sortBy) {
        this.sortBy = sortBy;
    }

    public void setFullType(FeatureType fullType) {
        this.fullType = fullType;
    }

    public FeatureType getFullType() {
        return fullType;
    }

    public void setQueryType(FeatureType queryType) {
        this.queryType = queryType;
    }

    public FeatureType getQueryType() {
        return queryType;
    }

    public void setUnsupportedFilter(Filter unsupportedFilter) {
        this.unsupportedFilter = unsupportedFilter;
    }

    public Filter getUnsupportedFilter() {
        return unsupportedFilter == null ? Filter.INCLUDE : unsupportedFilter;
    }
}
