/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2018, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.referencing.wkt;

import java.util.HashMap;
import java.util.Objects;
import javax.measure.IncommensurableException;
import javax.measure.Quantity;
import javax.measure.UnconvertibleException;
import javax.measure.Unit;
import org.geotools.measure.Units;
import org.geotools.referencing.wkt.GeoToolsCRSUnitFormat.BaseGT2Format;
import tec.uom.se.unit.TransformedUnit;

/**
 * UnitFormat configured to parse units. Since usually we don't know the citation in use for a
 * particular unit literal definition, this format includes the aliases of EPSG and ESRI citations,
 * in order to be able to parse the widest possible range of units.
 */
public class DefaultUnitParser extends BaseGT2Format {

    private static final DefaultUnitParser UNITPARSER = new DefaultUnitParser();
    protected HashMap<UnitWrapper, Unit<?>> unitWrapperToUnitMap =
            new HashMap<UnitWrapper, Unit<?>>();

    //    /**
    //     * Gets a UnitFormat configured to parse units. Since usually we don't know the citation
    // in use
    //     * for a particular unit literal definition, this format includes the aliases of EPSG and
    // ESRI
    //     * citations, in order to be able to parse the widest possible range of units.
    //     *
    //     */
    //    public static DefaultUnitParser getInstance() {
    //        return UNITPARSER;
    //    }

    public static DefaultUnitParser getInstance(
            Flavor flavour) { // to avoid confusions with parent class
        return UNITPARSER;
    }

    DefaultUnitParser() {
        initUnits(Units.getDefaultFormat());
        esriLabelsAndAliases(this);
        // add epsg labels the latest, to override esri ones if they collide
        epsgLabelsAndAliases(this);
    }

    protected void addUnit(Unit<?> unit) {
        unitWrapperToUnitMap.put(new UnitWrapper(unit), unit);
    }

    /**
     * Returns an equivalent unit instance based on the provided unit. First, it tries to get one of
     * the reference units defined in the JSR363 implementation in use. If no equivalent reference
     * unit is defined, it returns the provided unit
     */
    public <Q extends Quantity<Q>> Unit<Q> getEquivalentUnit(Unit<Q> unit) {
        return (Unit<Q>) unitWrapperToUnitMap.getOrDefault(new UnitWrapper(unit), unit);
    }

    /**
     * This wrapper is used to compare equivalent units using the {@link Units.equals} method. It
     * implements hashCode and equals method in a coherent way, so it can be used in a HashMap to
     * retrieve equivalent units.
     *
     * @author cesar
     */
    static class UnitWrapper {
        private Unit<?> unit;

        public UnitWrapper(Unit<?> unit) {
            this.unit = unit;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof UnitWrapper) {
                return Units.equals(unit, ((UnitWrapper) obj).getUnit());
            }
            return false;
        }

        public Unit<?> getUnit() {
            return unit;
        }

        @Override
        public int hashCode() {
            if (unit instanceof TransformedUnit<?>) {
                Unit<?> systemUnit = unit.getSystemUnit();
                try {
                    float factor1 = (float) unit.getConverterToAny(systemUnit).convert(1.0);
                    return Objects.hash(systemUnit, Float.floatToIntBits(factor1));
                } catch (UnconvertibleException | IncommensurableException e) {
                } catch (Throwable e) {
                }
            }
            return unit.hashCode();
        }

        public String toString() {
            return unit.toString();
        }
    }
}
