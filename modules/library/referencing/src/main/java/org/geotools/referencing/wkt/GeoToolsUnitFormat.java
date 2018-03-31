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

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.text.ParsePosition;
import java.util.HashMap;
import java.util.Set;

import javax.measure.Quantity;
import javax.measure.Unit;
import javax.measure.format.ParserException;
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
        // We will delegate on DEFAULT instead of trying to clone its definition
        private static final DefaultFormat DEFAULT = (DefaultFormat) SimpleUnitFormat.getInstance();

        /**
         * Holds the name to unit mapping.
         */
        final HashMap<String, Unit<?>> _nameToUnit = new HashMap<>();

        /**
         * Holds the unit to name mapping.
         */
        final HashMap<Unit<?>, String> _unitToName = new HashMap<>();

        public BaseGT2Format() {
            //FIXME: this constructor is apparently not useful. Should we remove it?
            
            // make sure Units registers the extar units in the default format
            Unit<?> forceInit = Units.SEXAGESIMAL_DMS;

            DefaultFormat base = (DefaultFormat) SimpleUnitFormat.getInstance();

            // clone non si units
            Set<Unit<?>> nonSiUnits = NonSI.getInstance().getUnits();
            for (Unit<?> unit : nonSiUnits) {
                String name = unit.getName();
                if (name != null) {
                    label(unit, name);
                }
            }
            // clone si units
            Set<Unit<?>> siUnits = SI.getInstance().getUnits();
            for (Unit<?> unit : siUnits) {
                String name = unit.getName();
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
                        String name = unit.getName();
                        if (name != null) {
                            label(unit, name);
                        }
                    } catch (Throwable t) {
                        // we tried...
                    }
                }
            }

        }

        @Override
        public Appendable format(Unit<?> unit, Appendable appendable) throws IOException {
            // use our name if defined, otherwise delegate on DEFAULT
            String name = nameFor(unit);
            if (name != null) {
                return super.format(unit, appendable);
            } else {
                return DEFAULT.format(unit, appendable);
            }
        }
        
        @Override
        public void label(Unit<?> unit, String label) {
            if (!isValidIdentifier(label))
                throw new IllegalArgumentException(
                        "Label: " + label + " is not a valid identifier.");
            synchronized (this) {
                _nameToUnit.put(label, unit);
                _unitToName.put(unit, label);
            }
        }

        // //////////////////////////
        // Parsing.
        @SuppressWarnings({ "rawtypes", "unchecked" })
        public Unit<? extends Quantity> parseSingleUnit(CharSequence csq, ParsePosition pos) throws ParserException {
            Unit<? extends Quantity> unit = super.parseSingleUnit(csq, pos);
            // if the unit is defined by us, we should be able to get it by name. Otherwise delegate on DEFAULT
            if (unit == unitFor(unit.getName())) {
                return unit;
            }
            return DEFAULT.parseSingleUnit(csq, pos);
        }

        protected String nameFor(Unit<?> unit) {
            // First search if specific ASCII name should be used.
            return _unitToName.get(unit);
        }

        @Override
        protected Unit<?> unitFor(String name) {
            return _nameToUnit.get(name);
        }
    }

    /**
     * Subclass adding overrides for the EPSG dialect
     * @author Andrea Aime - GeoSolutions
     */
    static class EPSGFormat extends BaseGT2Format {
        private static final long serialVersionUID = -1207705344688824557L;

        public EPSGFormat() {
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
            label(NonSI.DEGREE_ANGLE, "Degree");
            label(SI.METRE, "Meter");
            label(SI.METRE.multiply(0.3047997101815088), "Foot_Gold_Coast");
            label(USCustomary.FOOT, "Foot");
            label(USCustomary.FOOT_SURVEY, "Foot_US");
        }
    }
}
