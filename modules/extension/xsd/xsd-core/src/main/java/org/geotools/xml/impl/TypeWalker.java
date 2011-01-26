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
package org.geotools.xml.impl;

import org.eclipse.xsd.XSDTypeDefinition;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class TypeWalker {
    /**
     * Cached type hieracty
     */
    HashMap /*<XSDTypeDefinition,List>*/ cache = new HashMap();

    /**
     * Walks from the bottom of the type hierachy to the top.
     */
    public void walk(XSDTypeDefinition base, Visitor visitor) {
        List types = types(base);

        for (int i = 0; i < types.size(); i++) {
            XSDTypeDefinition type = (XSDTypeDefinition) types.get(i);

            //do the visit, if visitor returns false, break out
            if (!visitor.visit(type)) {
                break;
            }
        }
    }

    /**
     * Walks from the top of the type hierachy to the bottom.
     *
     */
    public void rwalk(XSDTypeDefinition base, Visitor visitor) {
        List types = types(base);

        for (int i = types.size() - 1; i > -1; i--) {
            XSDTypeDefinition type = (XSDTypeDefinition) types.get(i);

            //do the visit, if visitor returns false, break out
            if (!visitor.visit(type)) {
                break;
            }
        }
    }

    private List types(XSDTypeDefinition base) {
        List types = (List) cache.get(base);

        if (types == null) {
            types = new ArrayList();

            XSDTypeDefinition type = base;

            while (type != null) {
                types.add(type);

                //get the next type
                if (type.equals(type.getBaseType())) {
                    break;
                }

                type = type.getBaseType();
            }

            cache.put(base, types);
        }

        return types;
    }

    public static interface Visitor {
        /**
         * Supplies the current type to the visitor.
         *
         * @param type The current type.
         *
         * @return True to signal that the walk should continue, false to
         * signal the walk should stop.
         */
        boolean visit(XSDTypeDefinition type);
    }
}
