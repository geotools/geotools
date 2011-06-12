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

import java.lang.reflect.Modifier;
import java.util.Set;

import javax.measure.unit.NonSI;
import javax.measure.unit.SI;
import javax.measure.unit.Unit;
import javax.measure.unit.UnitFormat;

import org.geotools.measure.Units;
import org.geotools.metadata.iso.citation.Citations;
import org.geotools.referencing.CRS;
import org.opengis.metadata.citation.Citation;

/**
 * Provides unit formatting for EPSG and ESRI WKT dialects
 * @author Andrea Aime - GeoSolutions
 */
abstract class GeoToolsUnitFormat extends UnitFormat {

    public static UnitFormat getInstance(Citation citation) {
        if (CRS.equalsIgnoreMetadata(Citations.ESRI, citation)) {
            return new ESRIFormat();
        } else {
            return new EPSGFormat();
        }
    }

    /**
     * Base class that just copies {@link UnitFormat} default instance contents
     * @author Andrea Aime - GeoSolutions
     */
    static abstract class BaseGT2Format extends DefaultFormat {

        public BaseGT2Format() {
            // make sure Units registers the extar units in the default format
            Unit<?> forceInit = Units.SEXAGESIMAL_DMS;

            DefaultFormat base = (DefaultFormat) UnitFormat.getInstance();

            // clone non si units
            Set<Unit<?>> nonSiUnits = NonSI.getInstance().getUnits();
            for (Unit<?> unit : nonSiUnits) {
                String name = base.nameFor(unit);
                if (name != null) {
                    label(unit, name);
                }
            }
            // clone si units
            Set<Unit<?>> siUnits = NonSI.getInstance().getUnits();
            for (Unit<?> unit : siUnits) {
                String name = base.nameFor(unit);
                if (name != null) {
                    label(unit, name);
                }
            }

            // clone extra gt units
            for (java.lang.reflect.Field field : Units.class.getFields()) {
                if (Modifier.isStatic(field.getModifiers())
                        && Unit.class.isAssignableFrom(field.getType())) {
                    try {
                        field.setAccessible(true);
                        Unit unit = (Unit) field.get(null);
                        String name = base.nameFor(unit);
                        if (name != null) {
                            label(unit, name);
                        }
                    } catch (Throwable t) {
                        // we tried...
                    }
                }
            }

        }
    }

    /**
     * Subclass adding overrides for the EPSG dialect
     * @author Andrea Aime - GeoSolutions
     */
    static class EPSGFormat extends GeoToolsUnitFormat.BaseGT2Format {
        private static final long serialVersionUID = -1207705344688824557L;

        public EPSGFormat() {
            label(NonSI.DEGREE_ANGLE, "degree");
        }
    }

    /**
     * Subclass adding overrides for the ESRI dialect
     * @author Andrea Aime - GeoSolutions
     */
    static class ESRIFormat extends DefaultFormat {
        private static final long serialVersionUID = 5769662824845469523L;

        public ESRIFormat() {
            label(NonSI.DEGREE_ANGLE, "Degree");
            label(SI.METER, "Meter");
            label(SI.METER.times(0.3047997101815088), "Foot_Gold_Coast");
            label(NonSI.FOOT, "Foot");
            label(NonSI.FOOT_SURVEY_US, "Foot_US");
        }
    }
}