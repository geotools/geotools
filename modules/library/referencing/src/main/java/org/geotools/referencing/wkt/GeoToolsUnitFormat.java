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
 *
 *    This package contains documentation from OpenGIS specifications.
 *    OpenGIS consortium's work is fully acknowledged here.
 */
package org.geotools.referencing.wkt;

import java.util.HashMap;
import java.util.Map;

import javax.measure.Unit;
import javax.measure.format.UnitFormat;

import org.geotools.measure.Units;
import org.geotools.metadata.iso.citation.Citations;
import org.geotools.referencing.CRS;
import org.opengis.metadata.citation.Citation;

import si.uom.NonSI;
import si.uom.SI;
import systems.uom.common.USCustomary;
import tec.uom.se.format.SimpleUnitFormat;

/**
 * Provides unit formatting for EPSG and ESRI WKT dialects
 * @author Andrea Aime - GeoSolutions
 */
abstract class GeoToolsUnitFormat extends SimpleUnitFormat {

    public static UnitFormat getInstance(Citation citation) {
        // FIXME: creating the format is an expensive operation, we might consider storing the formats as static final variables
        if (CRS.equalsIgnoreMetadata(Citations.ESRI, citation)) {
            return new ESRIFormat();
        } else {
            return new EPSGFormat();
        }
    }

    /**
     * Base class that just copies {@link UnitFormat} default instance contents
     * 
     * @author Andrea Aime - GeoSolutions
     */
    static abstract class BaseGT2Format extends DefaultFormat {
        public BaseGT2Format() {
            super();

            /**
             * Labels and alias are only defined on the DEFAULT format instance, so these definitions are not inherited by subclassing DefaultFormat.
             * Therefore, we need to clone these definitions in our GT formats
             */
            DefaultFormat base = (DefaultFormat) SimpleUnitFormat.getInstance();
            try {

                java.lang.reflect.Field nameToUnitField = DefaultFormat.class
                        .getDeclaredField("_nameToUnit");
                nameToUnitField.setAccessible(true);
                HashMap<String, Unit<?>> nameToUnitMap = (HashMap<String, Unit<?>>) nameToUnitField
                        .get(base);

                java.lang.reflect.Field unitToNameField = DefaultFormat.class
                        .getDeclaredField("_unitToName");
                unitToNameField.setAccessible(true);
                HashMap<String, Unit<?>> unitToNameMap = (HashMap<String, Unit<?>>) unitToNameField
                        .get(base);
                for (Map.Entry<String, Unit<?>> entry : nameToUnitMap.entrySet()) {
                    String name = entry.getKey();
                    Unit<?> unit = entry.getValue();
                    if (unitToNameMap.containsKey(unit)
                            && name.equals(unitToNameMap.get(unit))) {
                        label(unit, name);
                    } else {
                        alias(unit, name);
                    }
                }
            } catch (Throwable t) {
                // we tried...
            }

            // labels and aliases for custom GT units
            Units.registerCustomUnits(this);
        }
    }

    /**
     * Subclass adding overrides for the EPSG dialect
     * @author Andrea Aime - GeoSolutions
     */
    static class EPSGFormat extends BaseGT2Format {
        private static final long serialVersionUID = -1207705344688824557L;

        public EPSGFormat() {
            super();
            label(NonSI.DEGREE_ANGLE, "degree");
        }
    }

    /**
     * Subclass adding overrides for the ESRI dialect
     * @author Andrea Aime - GeoSolutions
     */
    static class ESRIFormat extends BaseGT2Format {
        private static final long serialVersionUID = 5769662824845469523L;

        public ESRIFormat() {
            super();
            label(NonSI.DEGREE_ANGLE, "Degree");
            label(SI.METRE, "Meter");
            label(SI.METRE.multiply(0.3047997101815088), "Foot_Gold_Coast");
            label(USCustomary.FOOT, "Foot");
            label(USCustomary.FOOT_SURVEY, "Foot_US");
        }
    }
}
