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

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.GeoTools;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;


/**
 * DOCUMENT ME!
 *
 * @author iant
 * @source $URL$
 */
public class TextMarkImpl extends MarkImpl implements TextMark {
    /** The logger for the default core module. */

    //private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.geotools.core");
    private FilterFactory filterFactory;
    private Expression wellKnownName = null;
    private java.util.List fonts = new java.util.ArrayList();
    private Expression symbol;

    public TextMarkImpl() {
        this( CommonFactoryFinder.getFilterFactory(GeoTools.getDefaultHints()));
    }

    public TextMarkImpl(FilterFactory factory) {
        filterFactory = factory;
    }
    /**
     * Creates a new instance of TextMark
     *
     * @param font DOCUMENT ME!
     * @param symbol DOCUMENT ME!
     */
    public TextMarkImpl(Font font, String symbol) {
        this( CommonFactoryFinder.getFilterFactory(GeoTools.getDefaultHints()));
        addFont(font);
        setSymbol(symbol);
        wellKnownName = filterFactory.literal("Symbol");
    }

    public TextMarkImpl(Font font, Expression symbol) {
        this( CommonFactoryFinder.getFilterFactory(GeoTools.getDefaultHints()));
        addFont(font);
        setSymbol(symbol);
        wellKnownName = filterFactory.literal("Symbol");
    }

    /**
     * This parameter gives the well-known name of the symbol of the mark.<br>
     *
     * @return The well-known name of this symbol
     */
    public Expression getWellKnownName() {
        return wellKnownName;
    }

    /**
     * Getter for property font.
     *
     * @return Value of property font.
     */
    public org.geotools.styling.Font[] getFonts() {
        return (Font[]) fonts.toArray(new Font[0]);
    }

    /**
     * Setter for property font.
     *
     * @param font New value of property font.
     */
    public void addFont(org.geotools.styling.Font font) {
        this.fonts.add(font);
    }

    /**
     * Getter for property symbol.
     *
     * @return Value of property symbol.
     */
    public Expression getSymbol() {
        return symbol;
    }

    /**
     * Setter for property symbol.
     *
     * @param symbol New value of property symbol.
     */
    public void setSymbol(java.lang.String symbol) {
        setSymbol(filterFactory.literal(symbol));
    }

    public void setSymbol(Expression symbol) {
        Expression old = this.symbol;
        this.symbol = symbol;
    }

    /**
     * Setter for property wellKnownName.
     *
     * @param wellKnownName New value of property wellKnownName.
     */
    public void setWellKnownName(Expression wellKnownName) {
        // this is really blank the name is always symbol
    }

    public void accept(StyleVisitor visitor) {
        visitor.visit(this);
    }
}
