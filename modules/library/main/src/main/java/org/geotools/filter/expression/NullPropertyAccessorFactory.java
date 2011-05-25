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

package org.geotools.filter.expression;

import org.geotools.factory.Hints;

/**
 * This class supports the use of Expression/NIL for referring to a 'null' value.
 * 
 * @author Niels Charlier, Curtin University Of Technology
 * 
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/library/main/src/main/java/org/geotools/filter/expression/NullPropertyAccessorFactory.java $
 *         http://svn.osgeo.org/geotools/trunk/modules/library/main/src/main/java/org/geotools/
 *         filter/expression/NullPropertyAccessorFactory.java $
 */
public class NullPropertyAccessorFactory implements PropertyAccessorFactory {

    static private PropertyAccessor NULLPA = new NullPropertyAccessor();

    public PropertyAccessor createPropertyAccessor(Class type, String xpath, Class target,
            Hints hints) {
        return NULLPA;
    }

    /**
     * Return null for Expression/NIL
     * 
     * @author Niels Charlier, CSIRO
     */
    static class NullPropertyAccessor implements PropertyAccessor {

        /**
         * We can handle *one* case and one case only
         */
        public boolean canHandle(Object object, String xpath, Class target) {
            return "Expression/NIL".equals(xpath); // case sensitive

        }

        public Object get(Object object, String xpath, Class target)
                throws IllegalArgumentException {
            return null;
        }

        public void set(Object object, String xpath, Object value, Class target)
                throws IllegalArgumentException {
            throw new IllegalArgumentException("Cannot assign a value to Expression/NIL.");
        }
    }

}
