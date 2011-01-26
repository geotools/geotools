/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2003-2008, Open Source Geospatial Foundation (OSGeo)
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
import java.util.ArrayList;
import java.util.List;

import org.opengis.filter.Filter;
import org.opengis.filter.expression.PropertyName;


/**
 * A Query class allowing content to be requested from a Datastore or FeatureSource.
 * @deprecated This class is now synonymous with the {@linkplain Query} class.
 * @see Query
 * @author Chris Holmes
 * @source $URL$
 */
public class DefaultQuery extends Query {
    
    /**
     * No argument constructor.
     */
    public DefaultQuery() {
        // no arg
    }

    /**
     * Query with typeName.
     * <p>
     * </p>
     * @param typeName the name of the featureType to retrieve
     */
    public DefaultQuery( String typeName ){
        this( typeName, Filter.INCLUDE );
    }

    /**
     * Constructor with typeName and filter.  Note that current datasource
     * implementations only have one type per datasource, so the typeName
     * field will likely be ignored.
     *
     * @param typeName the name of the featureType to retrieve.
     * @param filter the OGC filter to constrain the request.
     */
    public DefaultQuery(String typeName, Filter filter) {
        this( typeName, filter, Query.ALL_NAMES );        
    }
    
    /**
     * Constructor that sets the filter and properties
     * @param typeName 
     *
     * @param filter the OGC filter to constrain the request.
     * @param properties an array of the properties to fetch.
     */
    public DefaultQuery(String typeName, Filter filter, String[] properties) {
        this( typeName, null, filter, Query.DEFAULT_MAX, properties, null );        
    }    
    /**
     * Constructor that sets all fields.
     *
     * @param typeName the name of the featureType to retrieve.
     * @param filter the OGC filter to constrain the request.
     * @param maxFeatures the maximum number of features to be returned.
     * @param propNames an array of the properties to fetch.
     * @param handle the name to associate with the query.
     */
    public DefaultQuery(String typeName, Filter filter, int maxFeatures,
        String[] propNames, String handle) {
        this(typeName, null, filter, maxFeatures, propNames, handle );
    }
    
    /**
     * Constructor that sets all fields.
     *
     * @param typeName the name of the featureType to retrieve.
     * @param namespace Namespace for provided typeName, or null if unspecified
     * @param filter the OGC filter to constrain the request.
     * @param maxFeatures the maximum number of features to be returned.
     * @param propNames an array of the properties to fetch.
     * @param handle the name to associate with the query.
     */
    public DefaultQuery(String typeName, URI namespace, Filter filter, int maxFeatures,
        String[] propNames, String handle) {
        this.typeName = typeName;
        this.filter = filter;
        this.namespace = namespace;
        this.maxFeatures = maxFeatures;
        this.handle = handle;
        setPropertyNames(propNames);
    }
    
    /**
     * Constructor that sets all fields.
     *
     * @param typeName the name of the featureType to retrieve.
     * @param namespace Namespace for provided typeName, or null if unspecified
     * @param filter the OGC filter to constrain the request.
     * @param maxFeatures the maximum number of features to be returned.
     * @param propNames a list of the properties to fetch.
     * @param handle the name to associate with the query.
     */
    public DefaultQuery(String typeName, URI namespace, Filter filter, int maxFeatures,
        List<PropertyName> propNames, String handle) {
        this.typeName = typeName;
        this.filter = filter;
        this.namespace = namespace;
        this.properties = propNames==null? null : new ArrayList<PropertyName>(propNames);
        this.maxFeatures = maxFeatures;
        this.handle = handle;
    }
    
    /**
     * Copy contructor, clones the state of a generic Query into a DefaultQuery
     * @param query
     */
    public DefaultQuery(Query query) {
      this(query.getTypeName(), query.getNamespace(), query.getFilter(), query.getMaxFeatures(),
          query.getProperties(), query.getHandle());
      this.sortBy = query.getSortBy();
      this.coordinateSystem = query.getCoordinateSystem();
      this.coordinateSystemReproject = query.getCoordinateSystemReproject();
      this.version = query.getVersion();
      this.hints = query.getHints();
      this.startIndex = query.getStartIndex();
    }
    
}
