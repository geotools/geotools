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
 *
 *    This package contains documentation from OpenGIS specifications.
 *    OpenGIS consortium's work is fully acknowledged here.
 */
package org.geotools.measure;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.measure.Quantity;
import javax.measure.Unit;
import tech.units.indriya.unit.TransformedUnit;

/**
 * UnitFormat configured to parse units. Since usually we don't know the citation in use for a particular unit literal
 * definition, this format includes the aliases of EPSG and ESRI citations, in order to be able to parse the widest
 * possible range of units.
 */
// this is in `org.geotools.measure` instead of `org.geotools.referencing.wkt`, because
// `Units.autoCorrect` depends on it & we need `Units` for the unit definitions in gt-measure there
public final class WktUnitFormat extends BaseUnitFormatter {

    public static UnitFormatter getInstance() {
        return INSTANCE;
    }

    private static final List<UnitDefinition> UNIT_DEFINITIONS = Stream.of(
                    UnitDefinitions.DIMENSIONLESS,
                    UnitDefinitions.CONSTANTS,
                    UnitDefinitions.SI_BASE,
                    UnitDefinitions.SI_DERIVED,
                    UnitDefinitions.NON_SI,
                    UnitDefinitions.US_CUSTOMARY,
                    UnitDefinitions.WKT)
            .flatMap(Collection::stream)
            .collect(Collectors.toUnmodifiableList());

    private static final WktUnitFormat INSTANCE = new WktUnitFormat(UNIT_DEFINITIONS);

    private HashMap<UnitWrapper, Unit<?>> unitWrapperToUnitMap;

    WktUnitFormat(List<UnitDefinition> unitDefinitions) {
        super(unitDefinitions);
    }

    @Override
    public void addLabel(Unit<?> unit, String label) {
        super.addLabel(unit, label);
        if (unitWrapperToUnitMap == null) {
            unitWrapperToUnitMap = new HashMap<>();
        }
        unitWrapperToUnitMap.put(new UnitWrapper(unit), unit);
    }

    /**
     * Returns an equivalent unit instance based on the provided unit. First, it tries to get one of the reference units
     * defined in the JSR 385 implementation in use. If no equivalent reference unit is defined, it returns the provided
     * unit
     */
    @SuppressWarnings("unchecked")
    public <Q extends Quantity<Q>> Unit<Q> getEquivalentUnit(Unit<Q> unit) {
        return (Unit<Q>) INSTANCE.unitWrapperToUnitMap.getOrDefault(new UnitWrapper(unit), unit);
    }

    /**
     * This wrapper is used to compare equivalent units using the {@link Units#equals} method. It implements hashCode
     * and equals method in a coherent way, so it can be used in a HashMap to retrieve equivalent units.
     *
     * @author cesar
     */
    static class UnitWrapper {
        private final Unit<?> unit;

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
                } catch (Throwable e) {
                    // Fall back to standard hashCode if conversion fails
                }
            }
            return unit.hashCode();
        }

        @Override
        public String toString() {
            return unit.toString();
        }
    }
}
