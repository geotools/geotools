/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2004-2007 Open Geospatial Consortium Inc.
 *
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.geotools.api.feature.type;

/**
 * Describes an instance of an Association.
 *
 * @author Jody Garnett, Refractions Research
 * @author Justin Deoliveira, The Open Planning Project
 */
public interface AssociationDescriptor extends PropertyDescriptor {

    /**
     * Override of {@link PropertyDescriptor#getType()} which type narrows to {@link
     * AssociationType}.
     *
     * @see PropertyDescriptor#getType()
     */
    @Override
    AssociationType getType();
}
