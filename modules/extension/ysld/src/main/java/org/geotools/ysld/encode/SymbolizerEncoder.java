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

import java.util.Map;

import org.geotools.styling.Symbolizer;

/**
 * Encodes a {@link Symbolizer} as YSLD.
 */
public abstract class SymbolizerEncoder<S extends Symbolizer> extends YsldEncodeHandler<S> {

    SymbolizerEncoder(S sym) {
        super(sym);
    }

    @Override
    protected void encode(S sym) {
        put("geometry", sym.getGeometry());
        put("uom", sym.getUnitOfMeasure());
        vendorOptions(sym.getOptions());
    }
}
