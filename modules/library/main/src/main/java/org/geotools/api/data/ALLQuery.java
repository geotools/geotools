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
package org.geotools.api.data;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.sort.SortBy;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.util.factory.Hints;

/**
 * Implementation of Query.ALL.
 *
 * <p>This query is used to retrive all Features. Query.ALL is the only instance of this class.
 *
 * <p>Example:
 *
 * <pre><code>
 * featureSource.getFeatures( Query.FIDS );
 * </code></pre>
 */
class ALLQuery extends Query {
    @Override
    public final String[] getPropertyNames() {
        return null;
    }

    @Override
    public final boolean retrieveAllProperties() {
        return true;
    }

    @Override
    public final int getMaxFeatures() {
        return DEFAULT_MAX; // consider Integer.MAX_VALUE
    }

    @Override
    public Integer getStartIndex() {
        return null;
    }

    @Override
    public final Filter getFilter() {
        return Filter.INCLUDE;
    }

    @Override
    public final String getTypeName() {
        return null;
    }

    @Override
    public URI getNamespace() {
        return NO_NAMESPACE;
    }

    @Override
    public final String getHandle() {
        return "Request All Features";
    }

    @Override
    public final String getVersion() {
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

        return ((n == null) ? -1 : ((n.length == 0) ? 0 : (n.length | n[0].hashCode())))
                | getMaxFeatures()
                | ((getFilter() == null) ? 0 : getFilter().hashCode())
                | ((getTypeName() == null) ? 0 : getTypeName().hashCode())
                | ((getVersion() == null) ? 0 : getVersion().hashCode())
                | ((getCoordinateSystem() == null) ? 0 : getCoordinateSystem().hashCode())
                | ((getCoordinateSystemReproject() == null)
                        ? 0
                        : getCoordinateSystemReproject().hashCode());
    }

    /**
     * Equality based on propertyNames, maxFeatures, filter, typeName and version.
     *
     * <p>Changing the handle does not change the meaning of the Query.
     *
     * @param obj Other object to compare against
     * @return <code>true</code> if <code>obj</code> matches this filter
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
                && ((getFilter() == null)
                        ? (other.getFilter() == null)
                        : getFilter().equals(other.getFilter()))
                && ((getTypeName() == null)
                        ? (other.getTypeName() == null)
                        : getTypeName().equals(other.getTypeName()))
                && ((getVersion() == null)
                        ? (other.getVersion() == null)
                        : getVersion().equals(other.getVersion()))
                && ((getCoordinateSystem() == null)
                        ? (other.getCoordinateSystem() == null)
                        : getCoordinateSystem().equals(other.getCoordinateSystem()))
                && ((getCoordinateSystemReproject() == null)
                        ? (other.getCoordinateSystemReproject() == null)
                        : getCoordinateSystemReproject().equals(other.getCoordinateSystemReproject()));
    }

    @Override
    public String toString() {
        return "Query.ALL";
    }

    /**
     * Return <code>null</code> as ALLQuery does not require a CS.
     *
     * @return <code>null</code> as override is not required.
     * @see Query#getCoordinateSystem()
     */
    @Override
    public CoordinateReferenceSystem getCoordinateSystem() {
        return null;
    }

    /**
     * Return <code>null</code> as ALLQuery does not require a CS.
     *
     * @return <code>null</code> as reprojection is not required.
     * @see Query#getCoordinateSystemReproject()
     */
    @Override
    public CoordinateReferenceSystem getCoordinateSystemReproject() {
        return null;
    }

    /** @return {@link SortBy#UNSORTED}. */
    @Override
    public SortBy[] getSortBy() {
        return SortBy.UNSORTED;
    }

    /** Returns an empty Hints set */
    @Override
    public Hints getHints() {
        return new Hints();
    }

    //
    // Not mutable; all values hard coded
    //
    @Override
    public void setCoordinateSystem(CoordinateReferenceSystem system) {
        throw new UnsupportedOperationException("Query.ALL cannot be changed, please just use as a default.");
    }

    @Override
    public void setCoordinateSystemReproject(CoordinateReferenceSystem system) {
        throw new UnsupportedOperationException("Query.ALL cannot be changed, please just use as a default.");
    }

    @Override
    public void setFilter(Filter filter) {
        throw new UnsupportedOperationException("Query.ALL cannot be changed, please just use as a default.");
    }

    @Override
    public void setHandle(String handle) {
        throw new UnsupportedOperationException("Query.ALL cannot be changed, please just use as a default.");
    }

    @Override
    public void setHints(Hints hints) {
        throw new UnsupportedOperationException("Query.ALL cannot be changed, please just use as a default.");
    }

    @Override
    public void setMaxFeatures(int maxFeatures) {
        throw new UnsupportedOperationException("Query.ALL cannot be changed, please just use as a default.");
    }

    @Override
    public void setNamespace(URI namespace) {
        throw new UnsupportedOperationException("Query.ALL cannot be changed, please just use as a default.");
    }

    @Override
    public void setPropertyNames(List<String> propNames) {
        throw new UnsupportedOperationException("Query.ALL cannot be changed, please just use as a default.");
    }

    @Override
    public void setPropertyNames(String... propNames) {
        throw new UnsupportedOperationException("Query.ALL cannot be changed, please just use as a default.");
    }

    @Override
    public void setSortBy(SortBy... sortBy) {
        throw new UnsupportedOperationException("Query.ALL cannot be changed, please just use as a default.");
    }

    @Override
    public void setStartIndex(Integer startIndex) {
        throw new UnsupportedOperationException("Query.ALL cannot be changed, please just use as a default.");
    }

    @Override
    public void setTypeName(String typeName) {
        throw new UnsupportedOperationException("Query.ALL cannot be changed, please just use as a default.");
    }

    @Override
    public void setVersion(String version) {
        throw new UnsupportedOperationException("Query.ALL cannot be changed, please just use as a default.");
    }
}
