/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2016 Open Source Geospatial Foundation (OSGeo)
 *    (C) 2014-2016 Boundless Spatial
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
package org.geotools.ysld.encode;

import org.geotools.styling.PointSymbolizer;

/**
 * Encodes a {@link PointSymbolizer} as YSLD.
 */
public class PointSymblolizerEncoder extends SymbolizerEncoder<PointSymbolizer> {
    PointSymblolizerEncoder(PointSymbolizer sym) {
        super(sym);
    }

    @Override
    protected void encode(PointSymbolizer sym) {
        inline(new GraphicEncoder((((PointSymbolizer) sym).getGraphic())));
        super.encode(sym);
    }
}
