/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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

import java.util.Map;
import org.opengis.filter.expression.Expression;

/**
 * Custom symbolizer support.
 *
 * <p>This facility is used to allow you to work on your "vendor specific" symbolizer.
 */
public interface ExtensionSymbolizer extends org.opengis.style.ExtensionSymbolizer, Symbolizer {

    /**
     * Vendor specific name for your symbolizer.
     *
     * @return the symbolizer name
     */
    String getExtensionName();

    /** Name of vendor specific extensions */
    void setExtensionName(String name);

    /**
     * Live map symbolizer expressions.
     *
     * @return map of all expressions.
     */
    Map<String, Expression> getParameters();
}
