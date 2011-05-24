/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2005 Open Geospatial Consortium Inc.
 *    (C) 2001-2004 EXSE, Department of Geography, University of Bonn
 *                  lat/lon Fitzke/Fretter/Poth GbR
 *    
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation; either
 *    version 2.1 of the License, or (at your option) any later version.
 *   
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.opengis.filter.capability;

// Annotations
import static org.opengis.annotation.Specification.UNSPECIFIED;

import org.opengis.annotation.UML;

/**
 * Indicates a supported Operator.
 * <p>
 * The operator that is supported is indicated by the getName() field, these
 * names are formally defined to match:
 * <ul>
 * <li>A subclass of Filter. Examples include "BBOX" and "EqualsTo"
 * <li>A subclass of Expression or Function. Examples include "ADD" and "Length"
 * </ul>
 * Each filter subclass has an associated name (such as BBOX or EqualsTo), you
 * can use this name to determine if a matching Operator is defined as part of
 * FilterCapabilities.
 * 
 * @author <a href="mailto:tfr@users.sourceforge.net">Torsten Friebe</A>
 * @author Jody Garnett (Refractions Research)
 * @todo Which relationship with Filter and expressions?
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/modules/library/opengis/src/main/java/org/opengis/filter/capability/Operator.java $
 */
public interface Operator {	
    /**
     * Name of supported Operator.
     * <p>
     * Each filter subclass has an associated name (such as BBOX or EqualsTo), you
     * can use this name to determine if a matching Operator is defined as part of
     * FilterCapabilities. 
     */
    @UML(identifier="name", specification=UNSPECIFIED)
    String getName();
    
    /**
     * The supported interface enabled by this Operator.
     * <p>
     * The mapping from getName() to supported interface is formally defined; and
     * is must agree with the interfaces defined in org.opengis.filter. Because this
     * binding is formal we should replace Operator here with a CodeList and capture
     * it as part of the GeoAPI project.
     * </p>
     * @return Interface marked as supported by this Operator
     */
    //Class getSupportedType();
    
    /**
     * Equals should be implemented simply in terms of getName()
     */
    ///@Override
    boolean equals(Object obj);
    /**
     * HashCode should be implemented simply in terms of getName().
     */
    ///@Override
    int hashCode();
}
