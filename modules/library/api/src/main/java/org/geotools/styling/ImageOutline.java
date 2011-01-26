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



/**
 * ImageOutline specifies how individual source rasters in a multi-raster set
 * (such as a set of satellite-image scenes) should be outlined to make the
 * individual-image locations visible.
 *
 *  &lt;xsd:element name="ImageOutline"&gt;
 *      &lt;xsd:annotation&gt;
 *          &lt;xsd:documentation&gt;         &quot;ImageOutline&quot; specifies
 *              how individual source rasters in         a multi-raster set
 *              (such as a set of satellite-image scenes)         should be
 *              outlined to make the individual-image locations visible.       &lt;/xsd:documentation&gt;
 *      &lt;/xsd:annotation&gt;
 *      &lt;xsd:complexType&gt;
 *          &lt;xsd:choice&gt;
 *              &lt;xsd:element ref="sld:LineSymbolizer"/&gt;
 *              &lt;xsd:element ref="sld:PolygonSymbolizer"/&gt;
 *          &lt;/xsd:choice&gt;
 *      &lt;/xsd:complexType&gt;
 *  &lt;/xsd:element&gt;
 *
 * @author Justin Deoliveira, The Open Planning Project
 *
 * @source $URL$
 */
public interface ImageOutline {
    /**
     * Returns the symbolizer of the image outline.
     *
     * @return One of {@see PolygonSymbolizer},{@see LineSymbolizer}.
     */
    Symbolizer getSymbolizer();

    /**
     * Sets the symbolizer of the image outline.
     *
     * @param symbolizer The new symbolizer, one of {@see PolygonSymbolizer},{@see LineSymbolizer}.
     */
    void setSymbolizer(Symbolizer symbolizer);
    
    
    void accept(org.geotools.styling.StyleVisitor visitor);
}
