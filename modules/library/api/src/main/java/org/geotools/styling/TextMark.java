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
package org.geotools.styling;

import org.opengis.filter.expression.Expression;


/**
 * Note: this isn't in the SLD specification - it was added to work with characters symbol sets.
 * <p>
 * As of Symbology Encoding 1.1 specificationt here is a clean way to work with character
 * symbols - using an ExternalMark.getMarkIndex() to indicate which character to use.
 * 
 *
 * @source $URL$
 * @deprecated Please use a ExternalMark.getMarkIndex() to pick out a character from a true type font
 */
public interface TextMark extends Mark {
    public Expression getSymbol();

    public void setSymbol(String symbol);

    public Font[] getFonts();

    public void setWellKnownName(Expression wellKnownName);

    public Expression getWellKnownName();

    public void addFont(Font font);

    public void setSymbol(Expression symbol);
}
