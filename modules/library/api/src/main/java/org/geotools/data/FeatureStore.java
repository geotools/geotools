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

import java.io.IOException;
import java.util.List;

import org.opengis.feature.Feature;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;
import org.opengis.filter.identity.FeatureId;
import org.geotools.feature.FeatureCollection;


/**
 * This interface extends {@code FeatureSource}, adding methods to add and remove
 * features and to modify existing features.
 * <pre><code>
 * DataStore myDataStore = ...
 * FeatureSource featureSource = myDataStore.getFeatureSource("aname");
 * if (featureSource instanceof FeatureStore) {
 *     // we have write access to the feature data
 *     FeatureStore featureStore = (FeatureStore) featureSource;
 *
 *     // add some new features
 *     Transaction t = new DefaultTransaction("add");
 *     featureStore.setTransaction(t);
 *     try {
 *         featureStore.addFeatures( someFeatures );
 *         t.commit();
 *     } catch (Exception ex) {
 *         ex.printStackTrace();
 *         t.rollback();
 *     } finally {
 *         t.close();
 *     }
 * }
 * </code></pre>
 *
 * @author Jody Garnett
 * @author Ray Gallagher
 * @author Rob Hranac, TOPP
 * @author Chris Holmes, TOPP
 * @source $URL$
 * @version $Id$
 */
public interface FeatureStore<T extends FeatureType, F extends Feature> extends FeatureSource<T, F> {
    /**
     * Adds all features from the feature collection.
     * <p>
     * A list of {@code FeatureIds} is returned, one for each feature in the order created.
     * However, these might not be assigned until after a commit has been performed.
     * 
     * @param collection the collection of features to add
     *
     * @return the {@code FeatureIds} of the newly added features
     *
     * @throws IOException if an error occurs modifying the data source
     */
    List<FeatureId> addFeatures(FeatureCollection<T, F> collection) throws IOException;

    /**
     * Removes features selected by the given filter.
     *
     * @param filter an OpenGIS filter
     *
     * @throws IOException if an error occurs modifying the data source
     */
    void removeFeatures(Filter filter) throws IOException;

    /**
     * Modifies the attributes with the supplied values in all
     * features selected by the given filter.
     *
     * @param attributeNames the attributes to modify
     *
     * @param attributeValues the new values for the attributes
     *
     * @param filter an OpenGIS filter
     *
     * @throws IOException if the attribute and object arrays are not equal
     *         in length; if the value types do not match the attribute types;
     *         if modification is not supported; or if there errors accessing the
     *         data source
     */
    void modifyFeatures( Name[] attributeNames, Object[] attributeValues, Filter filter )  throws IOException;

    /**
     * For backwards compatibility; please be careful that your descriptor is
     * actually compatible with the one declared.
     * 
     * @param type the attributes to modify
     *
     * @param value the new values for the attributes
     *
     * @param filter an OpenGIS filter
     *
     * @throws IOException
     *
     * @deprecated Please use the safer method {@link #modifyFeatures(Name[], Object[], Filter)}
     */
    void modifyFeatures(AttributeDescriptor[] type, Object[] value, Filter filter) throws IOException;
    
    /**
     * Modifies an attribute with the supplied value in all features
     * selected by the given filter.
     *
     * @param attributeName the attribute to modify
     *
     * @param attributeValue the new value for the attribute
     *
     * @param filter an OpenGIS filter
     *
     * @throws IOException if modification is not supported; if the value type does
     *         not match the attribute type; or if there errors accessing the data source
     */
    void modifyFeatures( Name attributeName, Object attributeValue, Filter filter )  throws IOException;
    
    /**
     * For backwards compatibility; please be careful that your descriptor is actually compatible
     * with the one declared.
     *
     * @param type the attribute to modify
     *
     * @param value the new value for the attribute
     *
     * @param filter an OpenGIS filter
     *
     * @throws IOException
     *
     * @deprecated Please use the safer method {@link #modifyFeatures(Name, Object, Filter)}
     */
    void modifyFeatures(AttributeDescriptor type, Object value, Filter filter)
        throws IOException;

    /**
     * Deletes any existing features in the data source and then
     * inserts new features provided by the given reader. This is primarily used
     * as a convenience method for file-based data sources.
     *
     * @param reader - the collection to be written
     *
     * @throws IOException if there are any datasource errors.
     */
    void setFeatures(FeatureReader<T, F> reader) throws IOException;

    /**
     * Provide a transaction for commit/rollback control of a modifying
     * operation on this {@code FeatureStore}.
     * <pre><code>
     * Transation t = new DefaultTransaction();
     * featureStore.setTransaction(t);
     * try {
     *     featureStore.addFeatures( someFeatures );
     *     t.commit();
     * } catch ( IOException ex ) {
     *     // something went wrong;
     *     ex.printStackTrace();
     *     t.rollback();
     * } finally {
     *     t.close();
     * }
     * </code></pre>
     *
     * @param transaction the transaction
     */
    void setTransaction(Transaction transaction);

    /**
     * Gets the {@code Transaction} that this {@code FeatureStore} is
     * currently operating against.
     * <pre><code>
     * Transaction t = featureStore.getTransaction();
     * try {
     *     featureStore.addFeatures( features );
     *     t.commit();
     * } catch( IOException erp ){
     *     // something went wrong;
     *     ex.printStackTrace();
     *     t.rollback();
     * }
     * </code></pre>
     *
     * @return Transaction in use, or {@linkplain Transaction#AUTO_COMMIT}
     */
    Transaction getTransaction();
}
