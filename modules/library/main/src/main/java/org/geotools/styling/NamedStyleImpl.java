/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2003-2008, Open Source Geospatial Foundation (OSGeo)
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

import org.opengis.style.StyleVisitor;

/**
 * A NamedStyle is used to refer to a style that has a name in a WMS.
 *
 * <p>A NamedStyle is a Style that has only Name, so all setters other than setName will throw an
 * <code>UnsupportedOperationException</code>
 *
 * @author jamesm
 */
public class NamedStyleImpl extends StyleImpl implements NamedStyle {
    /** Style name */
    private String name;

    /**
     * Style name
     *
     * @return style name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Set name.
     *
     * @param name style name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Human readable title.
     *
     * @return Human readable title, or null
     */
    public String getTitle() {
        return "";
    }

    /**
     * Human readable title.
     *
     * @param title Human readable title.
     * @throws UnsupportedOperationException Cannot be changed
     */
    public void setTitle(String title) {
        throw new UnsupportedOperationException();
    }

    /** */
    public String getAbstract() {
        return "";
    }

    /** */
    public void setAbstract(String abstractStr) {
        throw new UnsupportedOperationException();
    }

    /** */
    public boolean isDefault() {
        return false;
    }

    /** */
    public void setDefault(boolean isDefault) {
        throw new UnsupportedOperationException();
    }

    /** */
    public org.geotools.styling.FeatureTypeStyle[] getFeatureTypeStyles() {
        return new org.geotools.styling.FeatureTypeStyle[0];
    }

    /** */
    public void setFeatureTypeStyles(org.geotools.styling.FeatureTypeStyle[] types) {
        throw new UnsupportedOperationException();
    }

    /** */
    public void addFeatureTypeStyle(org.geotools.styling.FeatureTypeStyle type) {
        throw new UnsupportedOperationException();
    }

    /** */
    public Object accept(StyleVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }
}
