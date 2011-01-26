/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.store;

import java.io.IOException;

import org.geotools.data.FeatureSource;
import org.geotools.data.Transaction;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.util.InternationalString;

import com.vividsolutions.jts.geom.Envelope;

/**
 * Starting place for holding information about a FeatureType.
 * <p>
 * Like say for instance the FeatureType, its metadata and so on.
 * </p>
 * <p>
 * The default implemenation should contain enough information to wean
 * us off of AbstractDataStore. That is it should provide its own locking
 * and event notification.
 * </p>
 * <p>
 * There is a naming convention:
 * <ul>
 * <li> data access follows bean conventions: getTypeName(), getSchema()
 * <li> resource access methods follow Collections conventions reader(), 
 * writer(), etc...
 * <li> overrrides are all protected and follow factory conventions:
 *  createWriter(), createAppend(), createFeatureSource(),
 *  createFeatureStore(), etc...
 * </ul>
 * <li>
 * </p>
 * <p>
 * Feedback:
 * <ul>
 * <li>even notification yes
 * <li>locking not - locking needs to be rejuggled
 * <li>naming convention really helps when subclassing
 * </ul>
 * </p>
 * 
 * @author jgarnett
 * @source $URL$
 */
public interface TypeEntry {
    
    /**
     * @return user name for this feature collection
     */
    InternationalString getDisplayName();
    
    /**
     * @return Description of this feature collection
     */
    InternationalString getDescription();
    
    /**
     * @return Schema of this feature collection
     * @throws IOException If resoruce is unavailable
     */
    SimpleFeatureType getFeatureType() throws IOException;
        
    /**
     * Bounding box for associated Feature Collection, will be calcualted as needed.
     * <p>
     * Note bounding box is returned in lat/long - the coordinate system of the default geometry
     * is used to provide this reprojection.
     * </p>
     * @return Enveloper for this FeatureCollection/FeatureType
     */
    //Envelope getBounds();
    
    /** Number of features in associated Feature Collection, will be calcualted as needed 
     * @return number of features, may block while value is calculated, -1 indicates resource is unavailable
     */
    //public int getCount();
    
    /**
     * Create a new FeatueSource allowing interaction with content.
     * <p>
     * Subclass may optionally implement:
     * <ul>
     * <li>FeatureStore - to allow read/write access
     * <li>FeatureLocking - for locking support
     * </ul>
     * This choice may even be made a runtime (allowing the api
     * to represent a readonly file).
     * </p>
     * <p>
     * Several default implemenations are provided
     * 
     * @return FeatureLocking allowing access to content.
     * @throws IOException
     */
    //FeatureSource getFeatureSource() throws IOException;
    FeatureSource<SimpleFeatureType, SimpleFeature> createFeatureSource() throws IOException;
    
    /**
     * Change notifcation
     * 
     * @param newFeature
     * @param transaction
     */
    void fireAdded( SimpleFeature newFeature, Transaction transaction );
    /**
     * Change notifcation
     * 
     * @param removedFeature
     * @param transaction
     */
    void fireRemoved( SimpleFeature removedFeature, Transaction transaction );
    /**
     * Change notifcation
     * 
     * @param before
     * @param after
     * @param transaction
     */
    void fireChanged( SimpleFeature before, SimpleFeature after, Transaction transaction ); 
    
    /**
     * Equals based only on resource definition information (not connection information).
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     * @param obj
     * @return true if TypeEntry represents the same resource
     */
    boolean equals( Object obj );

    /**
     * This hashcode is *VERY* important!
     * <p>
     * The hascode must be dependent only on the parameters that "define"
     * the resource, not those that control opperation.
     * <ul>
     * <li>when representing a URL the hashcode must be: url.hashCode()
     * <li>when representing a File the hashcode must be: file.toURI().toURL().hashcode()
     * <li>when representing a database connection: hascode of jdbc url w/ out username, password
     * </ul> 
     * </p>
     * <p>
     * Implemetnation tip - URL.hashCode is a blocking operation, so you calculate and cache when the URL changes,
     * rather than block this method.
     * </p>
     * @see java.lang.Object#hashCode()
     * @return hashCode based on resource definition
     */
    int hashCode();

}
