/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.xml.styling;

/**
 * This code generated using Refractions SchemaCodeGenerator For more information, view the attached
 * licensing information. CopyRight 105
 *
 * @source $URL$
 */

import org.geotools.xml.schema.Element;
import org.geotools.xml.schema.Type;
import org.geotools.xml.schema.impl.ElementGT;

public class sldElement extends ElementGT {
    public sldElement(String name, Type type, Element substitution) {
        super(null, name, sldSchema.NAMESPACE, type, 1, 1, false, substitution, false);
    }

    public sldElement(String name, Type type, Element substitution, int min, int max) {
        super(null, name, sldSchema.NAMESPACE, type, min, max, false, substitution, false);
    }
}
