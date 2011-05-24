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
package org.geotools.styling;

import org.opengis.filter.expression.Expression;


/**
 * Abstract base class for implementing style factories.
 *
 * @source $URL$
 */
public interface StyleFactory2 extends StyleFactory {
    /**
     * Label Shield hack, non SLD 1.1
     *
     * @param fill Fill
     * @param fonts Font information (CSS)
     * @param halo Describes Halo
     * @param label Expression for label
     * @param labelPlacement Captures label position
     * @param geometryPropertyName With respect to this geometry
     * @param graphic Used to draw a backdrop behind label
     *
     * @return TextSymbolizer2 allowing for a backdrop behind text label
     */
    public TextSymbolizer2 createTextSymbolizer(Fill fill, Font[] fonts, Halo halo,
        Expression label, LabelPlacement labelPlacement, String geometryPropertyName,
        Graphic graphic);    
}
