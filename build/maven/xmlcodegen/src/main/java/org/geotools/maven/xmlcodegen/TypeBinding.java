/*
 *    GeoTools - The Open Source Java GIS Tookit
 *    http://geotools.org
 * 
 *    (C) 2002-2010, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.maven.xmlcodegen;

/**
 * Data transfer object for explicit bindings of XSD types to classes. Properties will be set by Maven using reflection.
 * 
 * @author Ben Caradoc-Davies, CSIRO Earth Science and Resource Engineering 
 *
 */
public class TypeBinding {

    private String namespace;
    
    private String name;
    
    private String binding;

    /**
     * @return XSD type namespace
     */
    public String getNamespace() {
        return namespace;
    }

    /**
     * @return XSD type local name
     */
    public String getName() {
        return name;
    }

    /**
     * @return fully-qualified binding class name, for example "com.vividsolutions.jts.geom.Point"
     */
    public String getBinding() {
        return binding;
    }
    
    
}
