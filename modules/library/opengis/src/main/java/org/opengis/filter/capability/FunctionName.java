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
import java.util.Collection;
import java.util.List;

import org.opengis.annotation.UML;
import static org.opengis.annotation.Specification.*;

/**
 * Function provided in a filter capabilities.
 * <p>
 * <pre>
 * &lt;xsd:complexType name="FunctionNameType">
 *     &lt;xsd:simpleContent>
 *        &lt;xsd:extension base="xsd:string">
 *           &lt;xsd:attribute name="nArgs" type="xsd:string" use="required"/>
 *        &lt;/xsd:extension>
 *     &lt;/xsd:simpleContent>
 *  &lt;/xsd:complexType>
 * </pre>
 * </p>
 * 
 * @author <a href="mailto:tfr@users.sourceforge.net">Torsten Friebe </A>
 * @author Justin Deoliveira, The Open Planning Project
 */
public interface FunctionName extends Operator {
    /**
     * Number of arguments the function accepts.
     * <p>
     * <pre>
     * &lt;xsd:attribute name="nArgs" type="xsd:string" use="required"/>
     * </pre>
     * </p>
     */
    @UML(identifier="argumentCount", specification=UNSPECIFIED)
    int getArgumentCount();
    
    /**
     * Argument names for documentation purposes if known.
     * 
     * @return Argument names (for documentation purposes) if known
     */
    List<String> getArgumentNames();
}