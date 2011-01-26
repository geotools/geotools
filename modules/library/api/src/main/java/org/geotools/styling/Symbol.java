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

/*
 * Symbol.java
 *
 * Created on 01 August 2002, 10:46
 */
package org.geotools.styling;



/**
 * This an empty interface for styling symbol objects to implement
 *
 * @author iant
 * @source $URL$
 */
public interface Symbol extends org.opengis.style.GraphicalSymbol{
    public static final Symbol[] SYMBOLS_EMPTY = new Symbol[0];

    void accept(org.geotools.styling.StyleVisitor visitor);
}
