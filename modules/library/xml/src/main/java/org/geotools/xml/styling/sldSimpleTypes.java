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
 */
import org.geotools.xml.schema.Facet;
import org.geotools.xml.schema.SimpleType;
import org.geotools.xml.schema.impl.FacetGT;
import org.geotools.xml.schema.impl.SimpleTypeGT;

public class sldSimpleTypes {

    protected static class _Service extends SimpleTypeGT {
        private static SimpleType instance = new _Service();

        public static SimpleType getInstance() {
            return instance;
        }

        private static SimpleType[] parents = {
            org.geotools.xml.xsi.XSISimpleTypes.String
                    .getInstance() /* simpleType name is string */,
            org.geotools.xml.xsi.XSISimpleTypes.String.getInstance() /* simpleType name is string */
        };
        private static Facet[] facets = {new FacetGT(1, "WFS"), new FacetGT(1, "WCS")};

        private _Service() {
            super(null, "Service", sldSchema.NAMESPACE, SimpleType.RESTRICTION, parents, facets, 0);
        }
    }
}
