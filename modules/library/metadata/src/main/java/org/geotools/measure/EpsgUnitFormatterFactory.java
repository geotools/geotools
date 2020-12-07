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

import si.uom.NonSI;
import tech.units.indriya.format.SimpleUnitFormat;

/** A factory for unit formatters that support the EPSG dialect. */
public final class EpsgUnitFormatterFactory {

    public static UnitFormatter getUnitFormatterSingleton() {
        return INSTANCE;
    }

    public static SimpleUnitFormat create() {
        return new EpsgUnitFormatter();
    }

    private EpsgUnitFormatterFactory() {}

    private static final EpsgUnitFormatter INSTANCE = new EpsgUnitFormatter();

    /**
     * Subclass adding overrides for the EPSG dialect
     *
     * @author Andrea Aime - GeoSolutions
     */
    static class EpsgUnitFormatter extends SimpleUnitFormatForwarder.BaseUnitFormatter
            implements UnitFormatter {
        private static final long serialVersionUID = -1207705344688824557L;

        public EpsgUnitFormatter() {
            addEpsgLabelsAndAliases(this);
        }
    }

    static void addEpsgLabelsAndAliases(SimpleUnitFormatForwarder.BaseUnitFormatter format) {
        format.label(NonSI.DEGREE_ANGLE, "degree");
    }
}
