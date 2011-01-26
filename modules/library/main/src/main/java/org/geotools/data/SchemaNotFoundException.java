/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2003-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data;

import java.io.IOException;


/**
 * Indicates a FeatureType could not be found.
 *
 * @author Jody Garnett, Refractions Research
 * @source $URL$
 */
public class SchemaNotFoundException extends IOException {
    private static final long serialVersionUID = 1L;
    
    static final String NOT_FOUND = "Feature type could not be found for ";
    private String typeName;

    public SchemaNotFoundException(String typeName) {
        super(NOT_FOUND + typeName);
        typeName = null;
    }

    public SchemaNotFoundException(String typeName, Throwable t) {
        this(typeName);
        initCause(t);
    }

    /**
     * Query TypeName that a FeatureType could not be found for.
     *
     * @return Unfound type name
     */
    String getTypeName() {
        return typeName;
    }
}
