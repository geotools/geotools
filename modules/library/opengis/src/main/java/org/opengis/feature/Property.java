/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2004-2007 Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.feature;

import java.util.Map;

import org.opengis.feature.type.Name;
import org.opengis.feature.type.PropertyDescriptor;
import org.opengis.feature.type.PropertyType;

/**
 * An instance of a {@link PropertyType} relised as a {@link Attribute} or {@link Association}.
 * <p>
 * A property is a wrapper around an arbitrary object or value. The value is
 * available via the {@link #getValue()} and {@link #setValue(Object)}.
 *
 * <pre>
 *  Property property = ...;
 *
 *  //set the value
 *  property.setValue( &quot;foo&quot; );
 *
 *  //get the value
 *  String value = (String) property.getValue();
 * </pre>
 *
 * </p>
 * <p>
 * Every property has a type. This {@link PropertyType} defines information
 * about the property. This includes which java class the value of the property
 * is an instance of, any restrictions on the value, etc... 
 * The type is available via the {@link #getType()} method.
 *
 * <pre>
 *   Property property = ...;
 *
 *   //get the type
 *   PropertyType type = property.getType();
 *
 *   //get the class of the value
 *   Class&lt;String&gt; valueClass = (Class&lt;String&gt;)type.getBinding();
 *
 * </pre>
 *
 * </p>
 * <p>
 * A property can often be part of another entity such as a {@link Feature} or {@link ComplexAttribute}.
 * When this is the case, the relationship between the property and its "container" is described by
 * a {@link PropertyDescriptor}.
 * The descriptor of a property defines things like nilablility, multiplicity,
 * etc... See the javadoc of {@link PropertyDescriptor} for more details. The
 * descriptor is available via the {@link #getDescriptor()} method.
 *
 * <pre>
 *   Property property = ...;
 *
 *   //get the descriptor
 *   PropertyDescriptor descriptor = property.getDescriptor()l
 *
 *   //is the value allowed to be null?
 *   descriptor.isNillable();
 *
 *   //how many instances of this property are allowed?
 *   descriptor.getMaxOccurs();
 * </pre>
 *
 * @author Jody Garnett (Refractions Research)
 * @author Justin Deoliveira (The Open Planning Project)
 */
public interface Property {

    /**
     * The value or content of the property.
     * <p>
     * The class of this object is defined by
     * <code>getType().getBinding()</code>.
     * </p>
     * <p>
     * This value may be <code>null</code>. In this case
     * <code>getDescriptor().isNillable()</code> would be <code>true</code>.
     * </p>
     *
     * @return The value of the property.
     */
    Object getValue();

    /**
     * Sets the value or content of the property.
     * <p>
     * The class of <tt>newValue</tt> should be the same as or a subclass of
     * <code>getType().getBinding()</code>.
     * </p>
     * <p>
     * <tt>newValue</tt> may be <code>null</code> if
     * <code>getDescriptor().isNillable()</code> is <code>true</code>.
     * </p>
     *
     * @param newValue
     *            The new value of the property.
     *
     */
    void setValue(Object newValue);

    /**
     * The type of the property.
     * <p>
     * The type contains information about the value or content of the property
     * such as its java class.
     * </p>
     * <p>
     * This value is also available via <code>getDescriptor().getType()</code>.
     * </p>
     *
     * @return The property type.
     */
    PropertyType getType();

    /**
     * The {@link PropertyDscriptor} of the property, null if this is a top-level value.
     * <p>
     * The descriptor provides information about the property with respect to
     * its containing entity (more often then not a {@link Feature} or {@link ComplexAttribute}.
     * </p>
     *
     * @return The property descriptor, null if this is a top-level value.
     * @see ComplexAttribute
     */
    PropertyDescriptor getDescriptor();

    /**
     * The name of the property with respect to its descriptor.
     * <p>
     * This method is convenience for <code>getDescriptor().getName()</code>.
     * </p>
     *
     * @return name of the property.
     */
    Name getName();

    /**
     * Flag indicating if <code>null</code> is an acceptable value for the
     * property.
     * <p>
     * This method is convenience for <code>getDescriptor().isNillable()</code>.
     * </p>
     *
     * @return <code>true</code> if the value of the property is allowed to be
     *         <code>null</code>, otherwise <code>false</code>.
     */
    boolean isNillable();

    /**
     * A map of "user data" which enables applications to store
     * "application-specific" information against a property.
     * <p>
     * An example of information that may wish to be stored along with an
     * attribute could be its srs information (in the case of a geometric
     * attribute ).
     *
     * <pre>
     * <code>
     *  GeometryAttribute attribute = ...;
     *
     *  //set the crs
     *  CoordinateReferenceSystem crs = CRS.decode(&quot;EPSG:4326&quot;);
     *  attribute.setCRS( crs );
     *
     *  //set the srs
     *  attribute.getUserData().put( &quot;srs&quot;, &quot;EPSG:4326&quot; );
     * </code>
     * </pre>
     *
     * </p>
     *
     * @return A map of user data.
     */
    Map<Object, Object> getUserData();

}
