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
package org.geotools.xml.schema;

import java.net.URI;

/**
 * <p>
 * This represents an abstract collection of xml element definitions within a
 * Schema.
 * </p>
 * 
 * <p>
 * To avoid multiple type checks, a group mask was include, as described below.
 * </p>
 *
 * @author dzwiers www.refractions.net
 *
 * @source $URL$
 */
public interface ElementGrouping {
    /**
     * ElementGrouping mask to determine the type of ElementGrouping
     * represented. This is intended to  reduce the use of the instanceof
     * operand,  increasing performance.
     */
    public static final int ELEMENT = 1;

    /**
     * ElementGrouping mask to determine the type of ElementGrouping
     * represented. This is intended to  reduce the use of the instanceof
     * operand,  increasing performance.
     */
    public static final int GROUP = 2;

    /**
     * ElementGrouping mask to determine the type of ElementGrouping
     * represented. This is intended to  reduce the use of the instanceof
     * operand,  increasing performance.
     */
    public static final int ANY = 4;

    /**
     * ElementGrouping mask to determine the type of ElementGrouping
     * represented. This is intended to  reduce the use of the instanceof
     * operand,  increasing performance.
     */
    public static final int SEQUENCE = 8;

    /**
     * ElementGrouping mask to determine the type of ElementGrouping
     * represented. This is intended to  reduce the use of the instanceof
     * operand,  increasing performance.
     */
    public static final int CHOICE = 16;

    /**
     * ElementGrouping mask to determine the type of ElementGrouping
     * represented. This is intended to  reduce the use of the instanceof
     * operand,  increasing performance.
     */
    public static final int ALL = 32;
    
    public static final int UNBOUNDED = Integer.MAX_VALUE;

    /**
     * <p>
     * Returns the mask informing the caller as to the type of object they are
     * dealing with.
     * </p>
     *
     */
    public int getGrouping();

    /**
     * <p>
     * Convinience method which will search for the specified element within
     * it's children. This is typically implemented recursively, and as such
     * may be expensive to execute (so don't call me too much if you want to
     * be fast).
     * </p>
     *
     * @param name The Element LocalName (namespace and prefix should not be
     *        included)
     *
     * @return Element or null if not found.
     */
    public Element findChildElement(String name);

    /**
     * <p>
     * returns the max number of allowable occurences within the xml schema for
     * this construct.
     * </p>
     *
     */
    public int getMaxOccurs();

    /**
     * <p>
     * returns the min number of allowable occurences within the xml schema for
     * this construct.
     * </p>
     *
     */
    public int getMinOccurs();

	public Element findChildElement(String localName, URI namespaceURI);
}
