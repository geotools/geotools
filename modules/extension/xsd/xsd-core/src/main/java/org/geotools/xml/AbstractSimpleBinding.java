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


/**
 * Base class for simple bindings.
 *
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 *
 *
 * @source $URL$
 */
public abstract class AbstractSimpleBinding implements SimpleBinding {
    /**
     * This implementation returns {@link Binding#AFTER}.
     * <p>
     * Subclasses should override to change this behaviour.
     * </p>
     */
    public int getExecutionMode() {
        return AFTER;
    }

    /**
     * Subclasses need to override this method, this implementation returns
     * <code>null</code>.
     */
    public Object parse(InstanceComponent instance, Object value)
        throws Exception {
        return null;
    }

    /**
     * Performs the encoding of the object as a String.
     *
     * @param object The object being encoded, never null.
     * @param value The string returned from another binding in the type
     * hierachy, which could be null.
     *
     * @return A String representing the object.
     */
    public String encode(Object object, String value) throws Exception {
        //just return the value passed in, subclasses should override to provide new value
        return value;
    }
}
