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
import si.uom.SI;
import systems.uom.common.USCustomary;
import tech.units.indriya.format.SimpleUnitFormat;

/** A factory for unit formatters that support the EPSG dialect. */
public final class EsriUnitFormatterFactory {

    public static UnitFormatter getUnitFormatterSingleton() {
        return INSTANCE;
    }

    public static SimpleUnitFormat create() {
        return new EsriUnitFormatter();
    }

    private EsriUnitFormatterFactory() {}

    private static final EsriUnitFormatter INSTANCE = new EsriUnitFormatter();

    /**
     * Subclass adding overrides for the ESRI dialect
     *
     * @author Andrea Aime - GeoSolutions
     */
    static class EsriUnitFormatter extends SimpleUnitFormatForwarder.BaseUnitFormatter
            implements UnitFormatter {
        private static final long serialVersionUID = 5769662824845469523L;

        public EsriUnitFormatter() {
            addEsriLabelsAndAliases(this);
        }
    }

    static void addEsriLabelsAndAliases(SimpleUnitFormatForwarder.BaseUnitFormatter format) {
        format.label(NonSI.DEGREE_ANGLE, "Degree");
        format.label(SI.METRE, "Meter");
        format.label(SI.METRE.multiply(0.3047997101815088), "Foot_Gold_Coast");
        format.alias(USCustomary.FOOT, "Foot");
        format.label(USCustomary.FOOT_SURVEY, "Foot_US");
    }
}
