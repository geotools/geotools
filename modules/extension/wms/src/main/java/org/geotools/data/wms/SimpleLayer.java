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
package org.geotools.data.wms;

import java.util.Set;


/**
 * A simple bean that represents a layer name paired with a style name for use
 * in requests.
 *
 * @author Richard Gould, Refractions Research Inc.
 *
 * @source $URL$
 */
public class SimpleLayer {
    /** Name of layer */
    private String name;

    /**
     * Name of style (limited to Set provided by validStyles).
     * 
     * <p>
     * null is used to indicate the "default" style.
     * </p>
     */
    private String style;

    /** Set of type <code>String</code> naming valid styles for this layer */
    private Set validStyles;

    /**
     * SimpleLayer creation.
     *
     * @param name Name of layer
     * @param style Name of style, null indicates default.
     */
    public SimpleLayer(String name, String style) {
        super();
        this.name = name;
        this.style = style;
    }

    /**
     * SimpleLayer creation.
     *
     * @param name
     * @param validStyles
     */
    public SimpleLayer(String name, Set validStyles) {
        super();
        this.name = name;
        this.validStyles = validStyles;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    /**
     * Returns a Set of type <code>String</code> containing the names of all
     * the styles that are valid for this layer.
     *
     */
    public Set getValidStyles() {
        return validStyles;
    }
}
