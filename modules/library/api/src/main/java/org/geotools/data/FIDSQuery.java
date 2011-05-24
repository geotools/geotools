/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import org.opengis.filter.Filter;
import org.opengis.filter.sort.SortBy;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.geotools.factory.GeoTools;
import org.geotools.factory.Hints;


/**
 * Implementation for Query.FIDS.
 *
 * <p>
 * This query is used to retrive FeatureIds. Query.FIDS is the only instance of
 * this class.
 * </p>
 *
 * <p>
 * Example:
 * <pre><code>
 * featureSource.getFeatures( Query.FIDS );
 * </code></pre>
 * There is nothing special about this implementation; you can achieve the same effect using:
 * <pre><code>
 * Query fids = new Query();
 * fids.setPropertyNames( new String[0] );
 * featureSource.getFeatures( fids );
 * </code></pre>
 * 
 *
 * @source $URL$
 */
class FIDSQuery extends Query {
    /** Empty String array */
    public static final String[] NO_PROPERTIES = new String[0];
    
    /** The string "Request Feature IDs" */
    public static final String FIDS_HANDLE = "Request Feature IDs";

    /** For the toString method: "Query.FIDS" */
    public static final String FIDS_NAME = "Query.FIDS";

    /**
     * Returns the value {@linkplain #NO_PROPERTIES} which is an empty String array.
     *
     * @return an empty array
     *
     * @see Query#getPropertyNames
     */
    @Override
    public String[] getPropertyNames() {
        return NO_PROPERTIES;
    }

    /**
     * Always returns false since FIDSQuery does not retrieve any properties.
     *
     * @return false
     *
     * @see Query#retrieveAllProperties
     */
    @Override
    public boolean retrieveAllProperties() {
        return false;
    }

    /**
     * Returns {@linkplain #DEFAULT_MAX} to indicate no max features limit is set.
     *
     * @return {@linkplain #DEFAULT_MAX}
     *
     * @see Query#getMaxFeatures
     */
    @Override
    public int getMaxFeatures() {
        return DEFAULT_MAX; // consider Integer.MAX_VALUE
    }

    /**
     * Returns {@code null} to indicate no start index applies.
     *
     * @return {@code null}
     *
     * @see Query#getStartIndex
     */
    @Override
    public Integer getStartIndex(){
        return null;
    }

    /**
     * Always returns {@linkplain Filter#INCLUDE} to indicate that all features
     * will be processed.
     *
     * @return {@linkplain Filter#INCLUDE}
     *
     * @see Query#getFilter
     */
    @Override
    public Filter getFilter() {
        return Filter.INCLUDE;
    }

    /**
     * Always returns {@code null} to indicate no feature type applies.
     *
     * @return {@code null}
     *
     * @see Query#getTypeName()
     */
    @Override
    public String getTypeName() {
        return null;
    }

    /**
     * Always returns {@linkplain #NO_NAMESPACE}.
     *
     * @return {@linkplain #NO_NAMESPACE}
     *
     * @see Query#getNamespace()
     */
    @Override
    public URI getNamespace() {
        return NO_NAMESPACE;
    }

    /**
     * Returns the constant {@linkplain #FIDS_HANDLE}.
     *
     * @return {@linkplain #FIDS_HANDLE}
     *
     * @see Query#getHandle()
     */
    @Override
    public String getHandle() {
        return FIDS_HANDLE;
    }

    /**
     * Always returns {@code null} since version don't apply to FIDSQuery.
     *
     * @return {@code null}
     *
     * @see Query#getVersion()
     */
    @Override
    public String getVersion() {
        return null;
    }

    /**
     * Hashcode based on propertyName, maxFeatures and filter.
     *
     * @return hascode for filter
     */
    @Override
    public int hashCode() {
        String[] n = getPropertyNames();

        return ((n == null) ? (-1) : ((n.length == 0) ? 0 : (n.length | n[0].hashCode())))
        | getMaxFeatures() | ((getFilter() == null) ? 0 : getFilter().hashCode())
        | ((getTypeName() == null) ? 0 : getTypeName().hashCode())
        | ((getVersion() == null) ? 0 : getVersion().hashCode())
        | ((getCoordinateSystem() == null) ? 0 : getCoordinateSystem().hashCode())
        | ((getCoordinateSystemReproject() == null) ? 0 : getCoordinateSystemReproject().hashCode());
    }

    /**
     * Equality based on propertyNames, maxFeatures, filter, typeName and
     * version.
     *
     * <p>
     * Changing the handle does not change the meaning of the Query.
     * </p>
     *
     * @param obj Other object to compare against
     *
     * @return <code>true</code> if <code>obj</code> retrieves only FIDS
     */
    @Override
    public boolean equals(Object obj) {
        if ((obj == null) || !(obj instanceof Query)) {
            return false;
        }

        if (this == obj) {
            return true;
        }

        Query other = (Query) obj;

        return Arrays.equals(getPropertyNames(), other.getPropertyNames())
        && (retrieveAllProperties() == other.retrieveAllProperties())
        && (getMaxFeatures() == other.getMaxFeatures())
        && ((getFilter() == null) ? (other.getFilter() == null)
                                  : getFilter().equals(other.getFilter()))
        && ((getTypeName() == null) ? (other.getTypeName() == null)
                                    : getTypeName().equals(other.getTypeName()))
        && ((getVersion() == null) ? (other.getVersion() == null)
                                   : getVersion().equals(other.getVersion()))
        && ((getCoordinateSystem() == null) ? (other.getCoordinateSystem() == null)
                                            : getCoordinateSystem()
                                                  .equals(other.getCoordinateSystem()))
        && ((getCoordinateSystemReproject() == null) ? (other.getCoordinateSystemReproject() == null)
                                                     : getCoordinateSystemReproject()
                                                           .equals(other
            .getCoordinateSystemReproject()));
    }

    /**
     * Returns the constant {@linkplain #FIDS_NAME}.
     *
     * @return {@linkplain #FIDS_NAME}
     */
    @Override
    public String toString() {
        return FIDS_NAME;
    }

    /**
     * Return <code>null</code> as FIDSQuery does not require a CS.
     *
     * @return <code>null</code> as override is not required.
     *
     * @see org.geotools.data.Query#getCoordinateSystem()
     */
    @Override
    public CoordinateReferenceSystem getCoordinateSystem() {
        return null;
    }

    /**
     * Return <code>null</code> as FIDSQuery does not require a CS.
     *
     * @return <code>null</code> as reprojection is not required.
     *
     * @see org.geotools.data.Query#getCoordinateSystemReproject()
     */
    @Override
    public CoordinateReferenceSystem getCoordinateSystemReproject() {
        return null;
    }

    /**
     * Always returns {@link SortBy#UNSORTED}.
     *
     * @return {@link SortBy#UNSORTED}
     *
     * @see Query#getSortBy()
     */
    @Override
    public SortBy[] getSortBy() {
        return SortBy.UNSORTED;
    }

    /**
     * Returns the GeoTools default hints {@link GeoTools#getDefaultHints()}
     *
     * @return {@link GeoTools#getDefaultHints()}
     *
     * @see Query#getHints()
     */
    public Hints getHints() {
        return GeoTools.getDefaultHints();
    }
    
    //
    // Not mutable; all values hard coded
    //
    /**
     * Not applicable to FIDSQuery.
     * @throws UnsupportedOperationException if called
     */
    @Override
    public void setCoordinateSystem(CoordinateReferenceSystem system) {
        new UnsupportedOperationException("Query.ALL cannot be changed, please just use as a default.");
    }
    /**
     * Not applicable to FIDSQuery.
     * @throws UnsupportedOperationException if called
     */
    @Override
    public void setCoordinateSystemReproject(CoordinateReferenceSystem system) {
        new UnsupportedOperationException("Query.ALL cannot be changed, please just use as a default.");
    }
    /**
     * Not applicable to FIDSQuery.
     * @throws UnsupportedOperationException if called
     */
    @Override
    public void setFilter(Filter filter) {
        new UnsupportedOperationException("Query.ALL cannot be changed, please just use as a default.");
    }
    /**
     * Not applicable to FIDSQuery.
     * @throws UnsupportedOperationException if called
     */
    @Override
    public void setHandle(String handle) {
        new UnsupportedOperationException("Query.ALL cannot be changed, please just use as a default.");
    }
    /**
     * Not applicable to FIDSQuery.
     * @throws UnsupportedOperationException if called
     */
    @Override
    public void setHints(Hints hints) {
        new UnsupportedOperationException("Query.ALL cannot be changed, please just use as a default.");
    }
    /**
     * Not applicable to FIDSQuery.
     * @throws UnsupportedOperationException if called
     */
    @Override
    public void setMaxFeatures(int maxFeatures) {
        new UnsupportedOperationException("Query.ALL cannot be changed, please just use as a default.");
    }
    /**
     * Not applicable to FIDSQuery.
     * @throws UnsupportedOperationException if called
     */
    @Override
    public void setNamespace(URI namespace) {
        new UnsupportedOperationException("Query.ALL cannot be changed, please just use as a default.");
    }
    /**
     * Not applicable to FIDSQuery.
     * @throws UnsupportedOperationException if called
     */
    @Override
    public void setPropertyNames(List<String> propNames) {
        new UnsupportedOperationException("Query.FIDS cannot be changed, please just use as a default.");
    }
    /**
     * Not applicable to FIDSQuery.
     * @throws UnsupportedOperationException if called
     */
    @Override
    public void setPropertyNames(String[] propNames) {
        new UnsupportedOperationException("Query.FIDS cannot be changed, please just use as a default.");
    }
    /**
     * Not applicable to FIDSQuery.
     * @throws UnsupportedOperationException if called
     */
    @Override
    public void setSortBy(SortBy[] sortBy) {
        new UnsupportedOperationException("Query.FIDS cannot be changed, please just use as a default.");
    }
    /**
     * Not applicable to FIDSQuery.
     * @throws UnsupportedOperationException if called
     */
    @Override
    public void setStartIndex(Integer startIndex) {
        new UnsupportedOperationException("Query.FIDS cannot be changed, please just use as a default.");
    }
    /**
     * Not applicable to FIDSQuery.
     * @throws UnsupportedOperationException if called
     */
    @Override
    public void setTypeName(String typeName) {
        new UnsupportedOperationException("Query.FIDS cannot be changed, please just use as a default.");
    }
    /**
     * Not applicable to FIDSQuery.
     * @throws UnsupportedOperationException if called
     */
    @Override
    public void setVersion(String version) {
        new UnsupportedOperationException("Query.FIDS cannot be changed, please just use as a default.");
    }
}
