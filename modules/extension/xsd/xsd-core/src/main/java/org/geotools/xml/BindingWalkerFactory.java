/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.xml;

import org.eclipse.xsd.XSDFeature;
import org.geotools.xml.impl.BindingWalker;


/**
 * Factory made available to bindings to walk over a bindign execution chain.
 *
 * @author Justin Deoliveira, The Open Planning Project
 *
 * TODO: rename, this isnt really a factory!!
 *
 *
 * @source $URL$
 */
public interface BindingWalkerFactory {
    /**
     * Walks over the bindings for a particular xml component.
     *
     */
    void walk(XSDFeature component, BindingWalker.Visitor visitor);
}
