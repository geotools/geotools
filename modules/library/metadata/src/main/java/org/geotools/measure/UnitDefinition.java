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

import static java.util.Collections.emptyList;

import java.util.List;
import javax.measure.Unit;

/**
 * This class holds a unit and associates it with its available prefixes and aliases.
 *
 * <p>It also allows overriding the symbol to use, in cases where the unit itself lacks this piece
 * of information.
 */
// class can be a `record` in future versions of Java
public final class UnitDefinition {

    private final Unit<?> unit;
    private final List<PrefixDefinition> prefixes;
    private final String symbolOverride;
    private final List<String> aliases;

    public static UnitDefinition of(
            Unit<?> unit,
            List<PrefixDefinition> prefixes,
            String symbolOverride,
            List<String> aliases) {
        return new UnitDefinition(unit, prefixes, symbolOverride, aliases);
    }

    public static UnitDefinition withStandardPrefixes(Unit<?> unit) {
        return new UnitDefinition(unit, PrefixDefinitions.STANDARD, null, emptyList());
    }

    private UnitDefinition(
            Unit<?> unit,
            List<PrefixDefinition> prefixes,
            String symbolOverride,
            List<String> aliases) {
        this.unit = unit;
        this.prefixes = prefixes;
        this.symbolOverride = symbolOverride;
        this.aliases = aliases;
    }

    public Unit<?> getUnit() {
        return unit;
    }

    public List<PrefixDefinition> getPrefixes() {
        return prefixes;
    }

    public String getSymbolOverride() {
        return symbolOverride;
    }

    public List<String> getAliases() {
        return aliases;
    }
}
