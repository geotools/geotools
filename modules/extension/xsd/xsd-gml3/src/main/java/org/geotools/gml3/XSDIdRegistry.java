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
package org.geotools.gml3;

import java.util.HashSet;

/**
 * Holds the collection of encoded XSD ids to ensure that they're unique, therefore valid.
 * 
 * @author Rini Angreani, Curtin University of Technology (with Gabriel Roldan and Justin
 *         Deoliveira's help)
 * 
 *
 * @source $URL$
 *         http://svn.osgeo.org/geotools/trunk/modules/extension/xsd/xsd-gml3/src/main/java/org
 *         /geotools/gml3/XSDIdRegistry.java $
 */
public class XSDIdRegistry {
    HashSet<String> ids = new HashSet<String>();

    public boolean idExists(String id) {
        return ids.contains(id);
    }

    public void add(String id) {
        if (!ids.add(id)) {
            String msg = "Duplicate id '" + id + "' detected! XSD Ids must be unique per document.";
            throw new IllegalArgumentException(msg);
        }
    }
}
