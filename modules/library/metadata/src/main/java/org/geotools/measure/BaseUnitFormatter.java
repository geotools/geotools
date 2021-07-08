package org.geotools.measure;

import static java.util.Arrays.asList;

import java.util.HashMap;
import java.util.List;
import javax.measure.Unit;

public final class BaseUnitFormatter {

    /** associates units with their symbols */
    private final HashMap<Unit<?>, String> unitToSymbol = new HashMap<>();

    /** associates symbols with their units */
    private final HashMap<String, Unit<?>> symbolToUnit = new HashMap<>();

    public static BaseUnitFormatter of(UnitDefinition... unitDefinitions) {
        return new BaseUnitFormatter(asList(unitDefinitions));
    }

    public static BaseUnitFormatter of(List<UnitDefinition> unitDefinitions) {
        return new BaseUnitFormatter(unitDefinitions);
    }

    // use this as a formatter/to initialize a formatter (like SimpleUnitFormat)
    private BaseUnitFormatter(List<UnitDefinition> unitDefinitions) {
        for (UnitDefinition unitDefinition : unitDefinitions) {
            Unit<?> unit = unitDefinition.getUnit();
            String unitSymbol =
                    unitDefinition.getSymbolOverride() != null
                            ? unitDefinition.getSymbolOverride()
                            : unit.getSymbol();
            // add units
            this.unitToSymbol.put(unit, unitSymbol);
            this.symbolToUnit.put(unitSymbol, unit);
            for (PrefixDefinition prefix : unitDefinition.getPrefixes()) {
                addUnit(unit, unitSymbol, prefix);
            }

            // add labels
            for (String alias : unitDefinition.getAliases()) {
                symbolToUnit.put(alias, unit);
            }
            for (PrefixDefinition prefix : unitDefinition.getPrefixes()) {
                for (String alias : unitDefinition.getAliases()) {
                    addAlias(unit, alias, prefix);
                }
            }
        }
    }

    private void addUnit(Unit<?> unit, String unitSymbol, PrefixDefinition prefix) {
        Unit<?> prefixedUnit = unit.prefix(prefix.getPrefix());
        String prefixString =
                prefix.getPrefixOverride() != null
                        ? prefix.getPrefixOverride()
                        : prefix.getPrefix().getSymbol();
        String prefixedSymbol = prefixString + unitSymbol;
        this.unitToSymbol.put(prefixedUnit, prefixedSymbol);
        this.symbolToUnit.put(prefixedSymbol, prefixedUnit);
        for (String prefixAlias : prefix.getPrefixAliases()) {
            this.unitToSymbol.put(prefixedUnit, prefixAlias + unitSymbol);
            this.symbolToUnit.put(prefixAlias + unitSymbol, prefixedUnit);
        }
    }

    private void addAlias(Unit<?> unit, String unitSymbol, PrefixDefinition prefix) {
        Unit<?> prefixedUnit = unit.prefix(prefix.getPrefix());
        String prefixString =
                prefix.getPrefixOverride() != null
                        ? prefix.getPrefixOverride()
                        : prefix.getPrefix().getSymbol();
        String prefixedSymbol = prefixString + unitSymbol;
        this.symbolToUnit.put(prefixedSymbol, prefixedUnit);
        for (String prefixAlias : prefix.getPrefixAliases()) {
            this.symbolToUnit.put(prefixAlias + unitSymbol, prefixedUnit);
        }
    }

    public HashMap<Unit<?>, String> getUnitToSymbol() {
        return unitToSymbol;
    }

    public HashMap<String, Unit<?>> getSymbolToUnit() {
        return symbolToUnit;
    }
}
