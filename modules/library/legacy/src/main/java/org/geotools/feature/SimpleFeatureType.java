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
package org.geotools.feature;

import java.net.URI;
import java.util.Collection;

/**
 * A basic implementation of FeatureType.
 *
 * @author Ian Schneider
 * @source $URL$
 * @version $Id$
 * @deprecated use {@link SimpleFeatureType}.
 */
public class SimpleFeatureType extends DefaultFeatureType{

    /**
     * @deprecated use SimpleFeatureType(String,URI,Collection,Collection,GeometryAttributeType)
     * @param typeName
     * @param namespace
     * @param types
     * @param superTypes
     * @param defaultGeom
     * @throws SchemaException
     * @throws NullPointerException
     */
    public SimpleFeatureType(String typeName, String namespace,
            Collection types, Collection superTypes, GeometryAttributeType defaultGeom)
            throws SchemaException, NullPointerException {
    	super(typeName,namespace,types,superTypes,defaultGeom);
    	// TODO check for simplicity here;
    }
    /**
     * Constructs a new SimpleFeatureType.
     *
     * <p>
     * Attributes from the superTypes will be copied to the list of attributes
     * for this feature type.  
     *
     * @param typeName The name to give this FeatureType.
     * @param namespace The namespace of the new FeatureType.
     * @param types The attributeTypes to use for validation.
     * @param superTypes The ancestors of this FeatureType.
     * @param defaultGeom The attributeType to set as the defaultGeometry.
     *
     * @throws SchemaException For problems making the FeatureType.
     * @throws NullPointerException If typeName is null.
     */
    public SimpleFeatureType(String typeName, URI namespace,
        Collection types, Collection superTypes, GeometryAttributeType defaultGeom)
        throws NullPointerException {
    	super(typeName,namespace,types,superTypes,defaultGeom);
    	// TODO check for simplicity here;
    }
}
