/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2021, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.measure;

import org.geotools.measure.SimpleUnitFormatForwarder.BaseUnitFormatter;
import tech.units.indriya.format.SimpleUnitFormat;

/** A factory for unit formatters that support the units required by GeoTools. */
public final class GeoToolsUnitFormatterFactory {

    public static UnitFormatter getUnitFormatterSingleton() {
        return INSTANCE;
    }

    public static SimpleUnitFormat create() {
        return new BaseUnitFormatter();
    }

    private GeoToolsUnitFormatterFactory() {}

    private static final BaseUnitFormatter INSTANCE = new BaseUnitFormatter();
}
