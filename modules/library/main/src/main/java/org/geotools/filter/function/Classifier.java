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
package org.geotools.filter.function;

/**
 * The data structure returned by classification functions. We can take this object, tweak it, and
 * then pass it to a ClassifyFunction.
 *
 * @author Cory Horner, Refractions Research
 */
public abstract class Classifier {

    // TODO: handle null, NaN, else
    // TODO: simply this by just adding labelled bins
    // TODO: add size

    String[] titles;

    public String[] getTitles() {
        return titles;
    }

    public void setTitles(String[] titles) {
        this.titles = titles;
    }

    public void setTitle(int slot, String title) {
        titles[slot] = title;
    }

    public String getTitle(int slot) {
        return titles[slot];
    }

    /** Returns the slot containing the passed expression's value. */
    public int classify(org.opengis.filter.expression.Expression expr, Object feature) {
        Object value = expr.evaluate(feature); // retrive value from context
        return classify(value);
    }

    /**
     * Returns the slot this value belongs in.
     *
     * @return index, starting from zero
     */
    public abstract int classify(Object value);

    /** @return the number of bins */
    public abstract int getSize();
}
