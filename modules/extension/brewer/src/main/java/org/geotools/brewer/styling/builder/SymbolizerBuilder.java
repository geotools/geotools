/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.brewer.styling.builder;

import java.util.LinkedHashMap;
import java.util.Map;
import org.geotools.styling.Symbolizer;

/** Base class handling options management */
public abstract class SymbolizerBuilder<T extends Symbolizer> extends AbstractStyleBuilder<T> {

    public SymbolizerBuilder(AbstractSLDBuilder parent) {
        super(parent);
    }

    protected Map<String, String> options = new LinkedHashMap<>();

    public SymbolizerBuilder option(String name, String value) {
        options.put(name, value);
        return this;
    }
}
