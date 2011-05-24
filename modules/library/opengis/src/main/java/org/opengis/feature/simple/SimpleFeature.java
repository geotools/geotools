/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2004-2007 Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.feature.simple;

import java.util.List;

import org.opengis.feature.ComplexAttribute;
import org.opengis.feature.Feature;
import org.opengis.feature.type.Name;

/**
 * An instance of {@link SimpleFeature} composed of fixed list values in a known order.
 * <p>
 * The definition of a "simple feature" can be summed up as the following:
 * <ul>
 *   <li>made up of only non-complex attributes, no associations
 *   <li>attributes are of multiplicity 1
 *   <li>attributes are ordered
 *   <li>attribute names are unqualified (namespaceURI == null)
 * </ul>
 * </p>
 * <p>
 *  <h3>Attribute Access</h3>
 *  The order and multiplicity restrictions on simple feature make attribute
 *  values accessible via an index. For example consider the following shapefile
 *  entry:
 *  <pre>
 *  | GEOMETRY | INT | STRING |
 *  |POINT(0 0)|  0  | "zero" |
 *  </pre>
 *  Accessing attributes via index would look like:
 *  <pre>
 *  SimpleFeature feature = ...;
 *
 *  Geometry g = (Geometry) feature.getAttribute( 0 );
 *  Integer i = (Integer) feature.getAttribute( 1 );
 *  String s = (String) feature.getAttribute( 2 );
 *  </pre>
 *  One could also access by name:
 *  <pre>
 *  SimpleFeature feature = ...;
 *
 *  Geometry g = (Geometry) feature.getAttribute( "GEOMETRY" );
 *  Integer i = (Integer) feature.getAttribute( "INT" );
 *  String s = (String) feature.getAttribute( "STRING" );
 *  </pre>
 * </p>
 * <p>
 * <b>Note:</b> Attribute access via getAttribute() methods returns attribute
 * values, and not the attributes themselves. For access to the actual attributes
 * {@link ComplexAttribute#getProperty(String)} can be used.
 * </p>
 *
 * @see SimpleFeatureType
 *
 * @author Jody Garnett (Refractions Research)
 * @author Justin Deoliveira (The Open Planning Project)
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/modules/library/opengis/src/main/java/org/opengis/feature/simple/SimpleFeature.java $
 */
public interface SimpleFeature extends Feature {
    /**
     * Unique Identifier for the SimpleFeature
     * <p>
     * This value is non-null and should be the same as getIdentifier().toString().
     * Please note that an ID may be provided  
     * </p>
     *
     * @return A unique identifier for the attribute, or <code>null</code> if
     *         the attribute is non-identifiable.
     */
	String getID();
	
    /**
     * Override and type narrow to SimpleFeatureType.
     */
    SimpleFeatureType getType();

    /**
     * The type of the feature.
     * <p>
     * This method is a synonym for {@link #getType()}.
     * </p>
     * @see #getType()
     */
    SimpleFeatureType getFeatureType();

    /**
     * Returns a list of the values of the attributes contained by the feature.
     * <br>
     * <p>
     * This method is a convenience for:
     * <pre>
     * List values = new ArrayList();
     * for ( Property p : getProperties(); ) {
     *   values.add( p.getValue() );
     * }
     *
     * return values;
     * </pre>
     * </p>
     *
     * @return List of attribute values for the feature.
     */
    List<Object> getAttributes();

    /**
     * Sets the values of the attributes contained by the feature.
     * <p>
     * The <tt>values</tt> must be in the order of the attributes defined by the
     * feature type.
     * </p>
     * <p>
     * This method is a convenience for:
     * <pre>
     * int i = 0;
     * for ( Property p : getProperties() ) {
     *   p.setValue( values.get( i++ ) );
     * }
     * </pre>
     * </p>
     * @param values The attribute values to set.
     */
    void setAttributes( List<Object> values );

    /**
     * Sets the values of the attributes contained by the feature.
     * <p>
     * The <tt>values</tt> must be in the order of the attributes defined by the
     * feature type.
     * </p>
     * <p>
     * This method is a convenience for:
     * <pre>
     * for ( Property p : getProperties() ) {
     *   p.setValue( values[i] );
     * }
     * </pre>
     * </p>
     * @param values The attribute values to set.
     */
    void setAttributes( Object[] values );

    /**
     * Gets an attribute value by name.
     * <p>
     * This method is a convenience for:
     * <pre>
     * Property p = getProperty( name );
     * return p.getValue();
     * </pre>
     * </p>
     * @param name The name of the attribute whose value to retrieve.
     *
     * @return The attribute value, or <code>null</code> if no such attribute
     * exists with the specified name.
     */
    Object getAttribute( String name );

    /**
     * Sets an attribute value by name.
     * <p>
     * This method is a convenience for:
     * <pre>
     * Property p = getProperty( name );
     * p.setValue(value);
     * </pre>
     * </p>
     * @param name The name of the attribute whose value to set.
     * @param value The new value of the attribute.
     */
    void setAttribute( String name, Object value );

    /**
     * Gets an attribute value by name.
     * <p>
     * This method is a convenience for:
     * <pre>
     * Property p = getProperty( name );
     * return p.getValue();
     * </pre>
     * </p>
     * <p>
     * Since attribute names in simple features do not have a namespace uri
     * this method is equivalent to calling <code>getAttribute(name.getLocalPart())</code>.
     * </p>
     * @param name The name of the attribute whose value to retrieve.
     *
     * @return The attribute value, or <code>null</code> if no such attribute
     * exists with the specified name.
     */
    Object getAttribute( Name name );

    /**
     * Sets an attribute value by name.
     * <p>
     * This method is a convenience for:
     * <pre>
     * Property p = getProperty( name );
     * p.setValue(value);
     * </pre>
     * </p>
     * <p>
     * Since attribute names in simple features do not have a namespace uri
     * this method is equivalent to calling <code>setAttribute(name.getLocalPart(), value)</code>.
     * </p>
     * @param name The name of the attribute whose value to set.
     * @param value The new value of the attribute.
     */
    void setAttribute( Name name, Object value );

    /**
     * Gets an attribute value by index.
     * <p>
     * This method is a convenience for:
     * <pre>
     * Property p = ((List)getProperties()).get( i ) ;
     * return p.getValue();
     * </pre>
     * </p>
     * @param index The index of the attribute whose value to get.
     *
     * @return The attribute value at the specified index.
     * @throws IndexOutOfBoundsException If the specified index is out of bounds.
     */
    Object getAttribute( int index ) throws IndexOutOfBoundsException;

    /**
     * Sets an attribute value by index.
     * <p>
     * This method is a convenience for:
     * <pre>
     * Property p = ((List)getProperties()).get( i ) ;
     * p.setValue(value);
     * </pre>
     * </p>
     * @param index The index of the attribute whose value to set.
     * @param value The new value of the attribute.
     *
     * @throws IndexOutOfBoundsException If the specified index is out of bounds.
     */
    void setAttribute( int index, Object value ) throws IndexOutOfBoundsException;

    /**
     * The number of attributes the feature is composed of.
     * <p>
     * This is a convenience for:
     * <pre>
     *   return getAttributes().size();
     * </pre>
     * </p>
     * @return Number of attributes of the feature.
     */
    int getAttributeCount();

    /**
     * Returns the value of the default geometry of the feature.
     * <p>
     * This method is convenience for:
     * <pre>
     * return getDefaultGeometry().getValue();
     * </pre>
     * </p>
     * @return The default geometry, or <code>null</code> if no default geometry
     * attribute exists.
     *
     */
    Object getDefaultGeometry();

    /**
     * Sets the value of the default geometry for the feature.
     * <p>
     * This method is convenience for:
     * <pre>
     * getDefaultGeometry().setValue(geometry);
     * </pre>
     * </p>
     * @param geometry The new default geometry value.
     */
    void setDefaultGeometry(Object geometry);
}
