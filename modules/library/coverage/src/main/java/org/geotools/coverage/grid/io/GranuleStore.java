/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2013, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.grid.io;

import java.io.IOException;
import org.geotools.data.Transaction;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.util.factory.Hints;
import org.opengis.filter.Filter;

/**
 * API extending {@link GranuleSource} providing capabilities to add, delete and modify granules.
 *
 * @author Simone Giannecchini, GeoSolutions SAS
 * @author Andrea Aime, GeoSolutions SAS
 * @author Daniele Romagnoli, GeoSolutions SAS
 */
public interface GranuleStore extends GranuleSource {

    /**
     * Add all the granules from the specified collection to this {@link GranuleStore}.
     *
     * @param granules the granules to add
     */
    void addGranules(SimpleFeatureCollection granules);

    /**
     * Removes granules selected by the given filter.
     *
     * @param filter an OpenGIS filter
     * @throws IOException if an error occurs modifying the data source
     */
    int removeGranules(Filter filter);

    /**
     * Removes granules selected by the given filter, controlled by a set of hints (might be
     * implementation dependent and eventually ignored).
     *
     * @param filter an OpenGIS filter
     * @param hints a set of hints to control how the removal is performed
     * @throws IOException if an error occurs modifying the data source
     */
    default int removeGranules(Filter filter, Hints hints) {
        // the default implementation just delegates to the hint-less version of the method
        return removeGranules(filter);
    }

    /**
     * Modifies the attributes with the supplied values in all granules selected by the given
     * filter.
     *
     * @param attributeNames the attributes to modify
     * @param attributeValues the new values for the attributes
     * @param filter an OpenGIS filter
     * @throws IOException if the attribute and object arrays are not equal in length; if the value
     *     types do not match the attribute types; if modification is not supported; or if there
     *     errors accessing the data source
     */
    void updateGranules(String[] attributeNames, Object[] attributeValues, Filter filter);

    /**
     * Gets the {@code Transaction} that this {@code GranuleStore} is currently operating against.
     *
     * <pre>
     * <code>
     * Transaction t = GranuleStore.getTransaction();
     * try {
     *     GranuleStore.addGranules (granules);
     *     t.commit();
     * } catch( IOException erp ){
     *     // something went wrong;
     *     t.rollback();
     * }
     * </code>
     * </pre>
     *
     * @return Transaction in use, or {@linkplain Transaction#AUTO_COMMIT}
     */
    Transaction getTransaction();

    /**
     * Provide a transaction for commit/rollback control of a modifying operation on this {@code
     * GranuleStore}.
     *
     * <pre>
     * <code>
     * Transation t = new DefaultTransaction();
     * GranuleStore.setTransaction(t);
     * try {
     *     GranuleStore.addGranules (granules);
     *     t.commit();
     * } catch ( IOException ex ) {
     *     // something went wrong;
     *     t.rollback();
     * } finally {
     *     t.close();
     * }
     * </code>
     * </pre>
     *
     * @param transaction the transaction
     */
    void setTransaction(Transaction transaction);
}
