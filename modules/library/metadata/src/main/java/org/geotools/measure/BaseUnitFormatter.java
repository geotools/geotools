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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.measure.Unit;
import org.geotools.measure.SimpleUnitFormatForwarder.DefaultFormatForwarder;

/**
 * This class encapsulates the required machinations to initialize the unit formatter implementation
 * of a certain third-party unit library.
 *
 * <p>GeoTools' unit formatters should be implemented by calling the provided constructors.
 */
// This class should be final, currently only prevented by the Wkt unit formatter subclass.
public class BaseUnitFormatter extends DefaultFormatForwarder {

    /**
     * Create a new {@code BaseUnitFormatter} instance, initialized with provided the unit
     * definitions.
     *
     * @param unitDefinitions unit definitions used to initialize this new instance
     */
    public BaseUnitFormatter(UnitDefinition... unitDefinitions) {
        this(Arrays.asList(unitDefinitions));
    }

    /**
     * Create a new {@code BaseUnitFormatter} instance, initialized with provided the unit
     * definitions.
     *
     * @param unitDefinitions a list of unit definitions used to initialize this new instance
     */
    public BaseUnitFormatter(List<UnitDefinition> unitDefinitions) {
        for (UnitDefinition unitDefinition : unitDefinitions) {
            Unit<?> unit = unitDefinition.getUnit();
            String unitSymbol =
                    unitDefinition.getSymbolOverride() != null
                            ? unitDefinition.getSymbolOverride()
                            : unit.getSymbol();

            // add units
            this.label(unit, unitSymbol);
            for (PrefixDefinition prefix : unitDefinition.getPrefixes()) {
                addUnit(unit, unitSymbol, prefix);
            }

            // add labels
            for (String alias : unitDefinition.getAliases()) {
                this.alias(unit, alias);
            }
            for (PrefixDefinition prefix : unitDefinition.getPrefixes()) {
                for (String alias : unitDefinition.getAliases()) {
                    addAlias(unit, alias, prefix);
                }
            }
        }
    }

    /**
     * @return an immutable map that shows the units (associated with their symbols) that this unit
     *     formatter contains
     */
    public Map<Unit<?>, String> getUnitToSymbolMap() {
        return Collections.unmodifiableMap(this.unitToName);
    }

    /**
     * @return an immutable map that shows the symbols (associated with their units) that this unit
     *     formatter contains
     */
    public Map<String, Unit<?>> getSymbolToUnitMap() {
        return Collections.unmodifiableMap(this.nameToUnit);
    }

    @Override
    public void label(Unit<?> unit, String label) {
        super.label(unit, label);
        addUnit(unit);
    }

    /** Defaults to being a no-op, subclasses can override */
    protected void addUnit(Unit<?> unit) {}

    private void addUnit(Unit<?> unit, String unitSymbol, PrefixDefinition prefix) {
        Unit<?> prefixedUnit = unit.prefix(prefix.getPrefix());
        String prefixString = prefix.getPrefix().getSymbol();
        String prefixedSymbol = prefixString + unitSymbol;
        this.label(prefixedUnit, prefixedSymbol);
        for (String prefixAlias : prefix.getPrefixAliases()) {
            this.label(prefixedUnit, prefixAlias + unitSymbol);
        }
    }

    private void addAlias(Unit<?> unit, String unitSymbol, PrefixDefinition prefix) {
        Unit<?> prefixedUnit = unit.prefix(prefix.getPrefix());
        String prefixString = prefix.getPrefix().getSymbol();
        String prefixedSymbol = prefixString + unitSymbol;
        this.alias(prefixedUnit, prefixedSymbol);
        for (String prefixAlias : prefix.getPrefixAliases()) {
            this.alias(prefixedUnit, prefixAlias + unitSymbol);
        }
    }
}
