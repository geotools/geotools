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
package org.geotools.data;

import java.io.IOException;

import org.geotools.data.postgis.VersionedPostgisDataStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.opengis.filter.Filter;

/**
 * Extension to feature source to provide the read only methods needed to work
 * against a versioned data store.
 * <p>
 * Thought a read only versioned data store does not make sense, it may happen
 * that a VersioningFeatureSource gets returned, for instance, because of
 * limited authorizations from the caller code.
 * 
 * @author Andrea Aime, TOPP TODO: this shall be moved in a more generic module
 *         (api?) once the interfaces are good
 *
 * @source $URL$
 */
public interface VersioningFeatureSource extends SimpleFeatureSource {

    /**
     * Returns a log of changes performed between fromVersion and toVersion
     * against the features matched by the specified filter.
     * <p>
     * This is equivalent to gathering the ids of features changed between the
     * two versions and matching the filter, getting a list of revision
     * involving those feaures between fromVersion and toVersion, and then query
     * {@link VersionedPostgisDataStore#TBL_CHANGESETS} against these revision
     * numbers.
     * 
     * @param fromVersion
     *            the start revision
     * @param toVersion
     *            the end revision, may be null to imply the latest one
     * @param filter
     *            will match features whose log will be reported *
     * @param users
     *            limits the features whose log will be returned, by
     *            catching only those that have been modified by at least one of
     *            the specified users. May be null to avoid user filtering.
     * @param maxRows
     *            the maximum number of log rows returned from this call
     * @return a feature collection of the logs, sorted on revision, descending
     * @throws IOException
     */
    public SimpleFeatureCollection getLog(String fromVersion, String toVersion, Filter filter,
            String[] userIds, int maxRows) throws IOException;

    /**
     * Returns a feature difference reader providing the changes occurred on
     * filtered features between the two specified versions
     * 
     * @param fromVersion
     *            the start version
     * @param toVersion
     *            the end version, may be null to imply the latest one
     * @param filter
     *            matches features whose differences will be reported *
     * @param users
     *            limits the features whose diff will be returned, by
     *            catching only those that have been modified by at least one of
     *            the specified users. May be null to avoid user filtering.
     * @return a difference reader
     * @throws IOException
     */
    public FeatureDiffReader getDifferences(String fromVersion, String toVersion, Filter filter,
            String[] userIds) throws IOException;
    
    /**
     * Returns the same features as {@link FeatureSource#getFeatures(Query)} but providing more
     * attributes, namely, revision, author and date of the version.
     * @return
     */
    public SimpleFeatureCollection getVersionedFeatures(Query q) throws IOException;
    
    /**
     * Returns the same features as {@link FeatureSource#getFeatures(Filter)} but providing more
     * attributes, namely, revision, author and date of the version.
     * @return
     */
    public SimpleFeatureCollection getVersionedFeatures(Filter f) throws IOException;
    
    /**
     * Returns the same features as {@link FeatureSource#getFeatures()} but providing more
     * attributes, namely, revision, author and date of the version.
     * @return
     */
    public SimpleFeatureCollection getVersionedFeatures() throws IOException;
}
