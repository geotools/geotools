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

import java.io.IOException;
import java.text.ParsePosition;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.measure.Quantity;
import javax.measure.Unit;
import javax.measure.format.MeasurementParseException;
import tech.units.indriya.format.SimpleUnitFormat;

/**
 * This class encapsulates the required machinations to initialize the unit formatter implementation of a certain
 * third-party unit library.
 *
 * <p>GeoTools' unit formatters should be implemented by calling the provided constructors.
 */
// This class should be final, currently only prevented by the Wkt unit formatter subclass.
public class BaseUnitFormatter extends SimpleUnitFormat implements UnitFormatter {

    private static final char MIDDLE_DOT = '\u00b7';
    private static final char EXPONENT_ONE = '\u00b9';
    private static final char EXPONENT_TWO = '\u00b2';
    private static final char EXPONENT_THREE = '\u00b3';

    private SimpleUnitFormat delegateFormatter = SimpleUnitFormat.getNewInstance();

    @Deprecated
    private Map<String, Unit<?>> nameToUnit = new HashMap<>();

    @Deprecated
    private Map<Unit<?>, String> unitToName = new HashMap<>();

    /**
     * Create a new {@code BaseUnitFormatter} instance, initialized with provided the unit definitions.
     *
     * @param unitDefinitions unit definitions used to initialize this new instance
     */
    public BaseUnitFormatter(UnitDefinition... unitDefinitions) {
        this(Arrays.asList(unitDefinitions));
    }

    /**
     * Create a new {@code BaseUnitFormatter} instance, initialized with provided the unit definitions.
     *
     * @param unitDefinitions a list of unit definitions used to initialize this new instance
     */
    public BaseUnitFormatter(List<UnitDefinition> unitDefinitions) {
        for (UnitDefinition unitDefinition : unitDefinitions) {
            Unit<?> unit = unitDefinition.getUnit();
            String unitSymbol =
                    unitDefinition.getSymbolOverride() != null ? unitDefinition.getSymbolOverride() : unit.getSymbol();

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
     * @return an immutable map that shows the units (associated with their symbols) that this unit formatter contains
     */
    @Deprecated
    public Map<Unit<?>, String> getUnitToSymbolMap() {
        return Collections.unmodifiableMap(this.unitToName);
    }

    /**
     * @return an immutable map that shows the symbols (associated with their units) that this unit formatter contains
     */
    @Deprecated
    public Map<String, Unit<?>> getSymbolToUnitMap() {
        return Collections.unmodifiableMap(this.nameToUnit);
    }

    /** Defaults to being a no-op, subclasses can override */
    protected void addUnit(Unit<?> unit) {}

    private void addUnit(Unit<?> unit, String unitSymbol, PrefixDefinition prefix) {
        this.nameToUnit.put(unitSymbol, unit);
        this.unitToName.put(unit, unitSymbol);
        Unit<?> prefixedUnit = unit.prefix(prefix.getPrefix());
        String prefixString = prefix.getPrefix().getSymbol();
        String prefixedSymbol = prefixString + unitSymbol;
        this.label(prefixedUnit, prefixedSymbol);
        for (String prefixAlias : prefix.getPrefixAliases()) {
            this.label(prefixedUnit, prefixAlias + unitSymbol);
        }
    }

    private void addAlias(Unit<?> unit, String unitSymbol, PrefixDefinition prefix) {
        this.nameToUnit.put(unitSymbol, unit);
        this.unitToName.put(unit, unitSymbol);
        Unit<?> prefixedUnit = unit.prefix(prefix.getPrefix());
        String prefixString = prefix.getPrefix().getSymbol();
        String prefixedSymbol = prefixString + unitSymbol;
        this.alias(prefixedUnit, prefixedSymbol);
        for (String prefixAlias : prefix.getPrefixAliases()) {
            this.alias(prefixedUnit, prefixAlias + unitSymbol);
        }
    }

    @Override
    public Appendable format(Unit<?> unit, Appendable appendable) throws IOException {
        return delegateFormatter.format(unit, appendable);
    }

    @Override
    public Unit<? extends Quantity> parseProductUnit(CharSequence csq, ParsePosition pos)
            throws MeasurementParseException {
        return delegateFormatter.parseProductUnit(csq, pos);
    }

    @Override
    public Unit<? extends Quantity> parseSingleUnit(CharSequence csq, ParsePosition pos)
            throws MeasurementParseException {
        return delegateFormatter.parseSingleUnit(csq, pos);
    }

    @Override
    public void label(Unit<?> unit, String label) {
        nameToUnit.put(label, unit);
        unitToName.put(unit, label);
        delegateFormatter.label(unit, label);
        addUnit(unit);
    }

    @Override
    public Unit<?> parse(CharSequence csq, ParsePosition cursor) throws IllegalArgumentException {
        return delegateFormatter.parse(csq, cursor);
    }

    @Override
    public Unit<?> parse(CharSequence csq) throws MeasurementParseException {
        return delegateFormatter.parse(csq);
    }

    @Override
    protected Unit<?> parse(CharSequence csq, int index) throws IllegalArgumentException {
        return parse(csq, new ParsePosition(index));
    }

    @Override
    public void alias(Unit<?> unit, String alias) {
        nameToUnit.put(alias, unit);
        delegateFormatter.alias(unit, alias);
    }

    /** Copy from DefaultFormatter */
    @Override
    protected boolean isValidIdentifier(String name) {
        if ((name == null) || (name.length() == 0)) return false;
        return isUnitIdentifierPart(name.charAt(0));
    }

    protected static boolean isUnitIdentifierPart(char ch) {
        return Character.isLetter(ch)
                || (!Character.isWhitespace(ch)
                        && !Character.isDigit(ch)
                        && (ch != MIDDLE_DOT)
                        && (ch != '*')
                        && (ch != '/')
                        && (ch != '(')
                        && (ch != ')')
                        && (ch != '[')
                        && (ch != ']')
                        && (ch != EXPONENT_ONE)
                        && (ch != EXPONENT_TWO)
                        && (ch != EXPONENT_THREE)
                        && (ch != '^')
                        && (ch != '+')
                        && (ch != '-'));
    }
}
