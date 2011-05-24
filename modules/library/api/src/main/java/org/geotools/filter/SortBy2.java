/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.filter;


/**
 * GeoTools allows for more pervase use of Expression then is strictly
 * supported by the specification.
 * <p>
 * To make the distiniction clear, generic use of Expression for SortBy has
 * been issolated to this interface. You shoudl note that this is outside
 * of the bounds of the Expression specification, and you will not be able
 * to construct valid documents if you use any other expression then
 * AttributeExpression.
 * </p>
 * <p>
 * Code that is compatible with the expanded use will indicate
 * via acceptance of a SortBy2 parameter. If you are working with a strictly
 * standards complient service (like WFS 1.1) it will be limited
 * to the use of SortBy.
 * <p>
 * @author Jody Garnett, Refractions Research.
 *
 * @source $URL$
 */
public interface SortBy2 extends SortBy {
    /**
     * GeoTools allows for more pervase use of Expression then is strictly
     * supported by the specification.
     *
     * @return Will return the same Expression as getAttributeExpression.
     */
    public Expression getExpression();

    /**
     * GeoTools allows for more pervase use of Expression then is strictly
     * supported by the specification.
     *
     */
    public void setExpression(Expression expression);
}
