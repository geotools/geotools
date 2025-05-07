/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2014, Open Source Geospatial Foundation (OSGeo)
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

import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.opengis.wfs20.StoredQueryDescriptionType;
import org.geotools.api.feature.type.FeatureType;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.sort.SortBy;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.gml2.SrsSyntax;
import org.geotools.gml2.bindings.GML2EncodingUtils;
import org.geotools.http.HTTPClient;
import org.geotools.util.factory.Hints;
import org.geotools.util.logging.Logging;

/** */
public class GetFeatureRequest extends WFSRequest {

    private static Logger LOGGER = Logging.getLogger(GetFeatureRequest.class);

    public enum ResultType {
        RESULTS,
        HITS
    }

    private String[] propertyNames;

    private String srsName;

    private Filter filter;

    private Integer maxFeatures;

    private Integer startIndex;

    private ResultType resultType;

    private SortBy[] sortBy;

    private FeatureType fullType;

    private FeatureType queryType;

    private Filter unsupportedFilter;

    private boolean storedQuery;

    private StoredQueryDescriptionType storedQueryDescriptionType;

    private Hints hints;

    private final HTTPClient httpClient;

    GetFeatureRequest(WFSConfig config, WFSStrategy strategy) {
        this(config, strategy, null);
    }

    /** Pass along the http client to use when parsing the response. */
    GetFeatureRequest(WFSConfig config, WFSStrategy strategy, HTTPClient httpClient) {
        super(GET_FEATURE, config, strategy);
        resultType = ResultType.RESULTS;
        this.httpClient = httpClient;
    }

    public HTTPClient getHTTPClient() {
        return httpClient;
    }

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

    public Integer getStartIndex() {
        return startIndex;
    }

    public ResultType getResultType() {
        return resultType;
    }

    public SortBy[] getSortBy() {
        return sortBy;
    }

    /** @param propertyNames the propertyNames to set */
    public void setPropertyNames(String[] propertyNames) {
        this.propertyNames = propertyNames;
    }

    /** Looks for a srs specified in the configuration that matches the coordinate reference system */
    public void findSupportedSrsName(CoordinateReferenceSystem crs) {
        String identifier = GML2EncodingUtils.toURI(crs, SrsSyntax.AUTH_CODE, false);
        if (identifier != null) {
            int idx = identifier.lastIndexOf(':');
            String authority = identifier.substring(0, idx);
            String code = identifier.substring(idx + 1);
            Set<String> supported = getStrategy().getSupportedCRSIdentifiers(getTypeName());
            for (String supportedSrs : supported) {
                if (supportedSrs.contains(authority) && supportedSrs.endsWith(":" + code)) {
                    LOGGER.log(Level.FINE, "Found supportedSRS: " + supportedSrs);
                    srsName = supportedSrs;
                    break;
                }
            }
        } else {
            LOGGER.log(Level.FINE, "GML2EncodingUtils couldn't handle the coordinate system: " + crs);
        }
    }

    /** @param srsName the srsName to set */
    public void setSrsName(String srsName) {
        this.srsName = srsName;
    }

    /** @param filter the filter to set */
    public void setFilter(Filter filter) {
        this.filter = filter;
    }

    /** @param maxFeatures the maxFeatures to set */
    public void setMaxFeatures(Integer maxFeatures) {
        this.maxFeatures = maxFeatures;
    }

    /** @param startIndex the startIndex to set */
    public void setStartIndex(Integer startIndex) {
        this.startIndex = startIndex;
    }

    /** @param resultType the resultType to set */
    public void setResultType(ResultType resultType) {
        this.resultType = resultType;
    }

    /** @param sortBy the sortBy to set */
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

    public boolean isStoredQuery() {
        return storedQuery;
    }

    public void setStoredQuery(boolean storedQuery) {
        this.storedQuery = storedQuery;
    }

    public void setHints(Hints hints) {
        this.hints = hints;
    }

    public Hints getHints() {
        return hints;
    }

    public StoredQueryDescriptionType getStoredQueryDescriptionType() {
        return storedQueryDescriptionType;
    }

    public void setStoredQueryDescriptionType(StoredQueryDescriptionType desc) {
        this.storedQueryDescriptionType = desc;
    }
}
