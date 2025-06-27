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

import org.geotools.api.style.FeatureTypeStyle;
import org.geotools.api.style.NamedStyle;
import org.geotools.api.style.TraversingStyleVisitor;

/**
 * A NamedStyle is used to refer to a style that has a name in a WMS.
 *
 * <p>A NamedStyle is a Style that has only Name, so all setters other than setName will throw an <code>
 * UnsupportedOperationException</code>
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
    @Override
    public String getName() {
        return this.name;
    }

    /**
     * Set name.
     *
     * @param name style name
     */
    @Override
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
    @Override
    public boolean isDefault() {
        return false;
    }

    /** */
    @Override
    public void setDefault(boolean isDefault) {
        throw new UnsupportedOperationException();
    }

    /** */
    @Override
    public FeatureTypeStyle[] getFeatureTypeStyles() {
        return new FeatureTypeStyle[0];
    }

    /** */
    @Override
    public void setFeatureTypeStyles(FeatureTypeStyle... types) {
        throw new UnsupportedOperationException();
    }

    /** */
    @Override
    public void addFeatureTypeStyle(FeatureTypeStyle type) {
        throw new UnsupportedOperationException();
    }

    /** */
    @Override
    public Object accept(TraversingStyleVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }
}
