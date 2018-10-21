/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2004-2007 Open Geospatial Consortium Inc.
 *
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.feature.type;

import java.util.Map;
import java.util.Set;

/**
 * A collection of AttributeType.
 *
 * <p>A schema is organized as a map of {@link Name} to {@link AttributeType}. In each name,type
 * tuple, the name matches the name of the type.
 *
 * <pre>
 *   //create some attribute types
 *   AttributeType pointType =
 *     new AttributeTypeImpl( new NameImpl( "http://www.opengis.net/gml", "PointType" ), ... );
 *   AttributeType lineStringType =
 *     new AttributeTypeImpl( new NameImpl( "http://www.opengis.net/gml", "LineStringType" ), ... );
 *   AttributeType polygonType =
 *     new AttributeTypeImpl( new NameImpl( "http://www.opengis.net/gml", "PolygonType" ), ... );
 *
 *   //create a schema
 *   Schema schema = new SchemaImpl( "http://www.opengis.net/gml" );
 *
 *   //add types to the schema
 *   schema.add( pointType );
 *   schema.add( lineStringType );
 *   schema.add( polygonType );
 * </pre>
 *
 * <p>The intention of a schema is to provide a resuable set of attribute types. These types are
 * used when building attribute instances.
 *
 * @author Jody Garnett, Refractions Research, Inc.
 * @author Justin Deoliveira, The Open Planning Project
 */
public interface Schema extends Map<Name, AttributeType> {

    /**
     * The uri of the schema.
     *
     * <p>This method is a convenience for <code>keySet().getURI()</code>.
     *
     * @return The uri of the schema.
     */
    String getURI();

    /**
     * Adds a type to the schema.
     *
     * <p>This method is a convenience for <code>put(type.getName(),type)</code>.
     *
     * @param type The type to add.
     */
    void add(AttributeType type);

    /**
     * Profiles the schema, creating a new schema in the process.
     *
     * <p>A profile of a schema is a subset of the schema, and it also a schema itself.
     *
     * <p>Used to select a subset of types for a specific application. Profiles often are used to
     * express limitiations of a source of data.
     *
     * @param profile The set of names which corresond to entries that will make up the profile.
     * @return The profile of the original schema.
     */
    Schema profile(Set<Name> profile);
}
