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

import org.opengis.feature.Association;

/**
 * The type of an association; used to describe kind of relationship between two entities.
 * <p>
 * The notion of an "association" is similar to that of an association in UML
 * and is used to model a relationship among two attributes. See the javadoc for
 * {@link Association} for more info on the semantics of associations.
 * </p>
 * <p>
 * An association is used to relate one attribute to another. The type of the
 * association specifies the type of the related attribute with the
 * {@link #getRelatedType()} method.
 * </p>
 *
 * @author Jody Garnett, Refractions Research, Inc.
 * @author Justin Deoliveira, The Open Planning Project
 */
public interface AssociationType extends PropertyType {

    /**
     * Override of {@link PropertyType#getSuper()} which type narrows to
     * {@link AssociationType}.
     *
     * @see PropertyType#getSuper()
     */
    AssociationType getSuper();

    /**
     * The attribute type of the related attribute in the association.
     *
     * @return The type of the related attribute.
     */
    AttributeType getRelatedType();

    /**
     * Override of {@link PropertyType#getBinding()} which specifies that this
     * method should return <code>getRelatedType().getBinding()</code>, that is
     * it returns the binding of the related type.
     */
    Class<?> getBinding();
}
