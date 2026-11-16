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

import static java.util.Arrays.asList;
import static javax.measure.MetricPrefix.MICRO;
import static org.junit.Assert.assertEquals;
import static si.uom.SI.ASTRONOMICAL_UNIT;
import static systems.uom.common.USCustomary.ELECTRICAL_HORSEPOWER;
import static systems.uom.common.USCustomary.REVOLUTION_PER_MINUTE;
import static tech.units.indriya.unit.Units.CUBIC_METRE;
import static tech.units.indriya.unit.Units.MONTH;
import static tech.units.indriya.unit.Units.SQUARE_METRE;

import java.lang.reflect.Field;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.measure.BinaryPrefix;
import javax.measure.MetricPrefix;
import javax.measure.Unit;
import javax.measure.format.UnitFormat;
import org.junit.Test;
import si.uom.SI;
import tech.units.indriya.format.SimpleUnitFormat;
import tech.units.indriya.unit.Units;

public class UnitFormatterTest {

    @Test
    public void unitsOnlyInOld() throws Exception {
        SimpleUnitFormat simpleUnitFormat = SimpleUnitFormat.getInstance();

        List<Map.Entry<Unit<?>, String>> unitToName = toSortedList1(getUnitToNameMap(simpleUnitFormat));
        List<Map.Entry<Unit<?>, String>> unitToSymbol = toSortedList1(formatter.unitToName);

        List<Map.Entry<Unit<?>, String>> unitsOnlyInOld = unitToName.stream()
                .filter(entry -> !INDRIYA_ONLY_UNITS.contains(entry.getKey()))
                .filter(entry -> !unitToSymbol.contains(entry))
                .collect(Collectors.toList());
        List<Map.Entry<Unit<?>, String>> indriyaBug = asList(
                entry(ASTRONOMICAL_UNIT, "UA"),
                // indriya labels day, week and year as d, wk and yr, and changed the cubic metre label
                entry(Units.DAY, "d"),
                entry(ELECTRICAL_HORSEPOWER, "hp(E)"),
                entry(Units.MONTH, "mo"),
                entry(CUBIC_METRE, "m³"),
                entry(SI.REVOLUTION, "rev"),
                entry(REVOLUTION_PER_MINUTE, "rpm"),
                entry(Units.WEEK, "wk"),
                entry(Units.YEAR, "yr"),
                entry(Units.GRAM.prefix(MICRO), "µg"),
                entry(Units.LITRE.prefix(MICRO), "µl"),
                entry(Units.CELSIUS.prefix(MICRO), "µ℃"));
        List<Map.Entry<Unit<?>, String>> unitsOnlyInOldWithoutBugs = new ArrayList<>(unitsOnlyInOld);
        unitsOnlyInOldWithoutBugs.removeAll(indriyaBug);
        assertEquals(
                unitsOnlyInOldWithoutBugs.size() + " units only in old: " + unitsOnlyInOldWithoutBugs + "\n",
                indriyaBug,
                unitsOnlyInOld);
    }

    @Test
    public void unitsOnlyInNew() throws Exception {
        SimpleUnitFormat simpleUnitFormat = SimpleUnitFormat.getInstance();

        List<Map.Entry<Unit<?>, String>> unitToNameMap = toSortedList1(getUnitToNameMap(simpleUnitFormat));
        List<Map.Entry<Unit<?>, String>> unitToSymbol = toSortedList1(formatter.unitToName);

        List<Map.Entry<Unit<?>, String>> unitsOnlyInNew = unitToSymbol.stream()
                .filter(entry -> !unitToNameMap.contains(entry))
                .collect(Collectors.toList());
        List<Map.Entry<? extends Unit<?>, String>> indriyaBug = asList(
                // GeoTools keeps its own labels for day, cubic metre, week and year
                entry(Units.DAY, "day"),
                entry(CUBIC_METRE, "㎥"),
                entry(Units.WEEK, "week"),
                entry(Units.YEAR, "year"),
                // only one kind of µ is added for those special-cased units in indriya:
                entry(Units.GRAM.prefix(MICRO), "μg"),
                entry(Units.LITRE.prefix(MICRO), "μl"),
                entry(Units.CELSIUS.prefix(MICRO), "μ℃"));
        List<Map.Entry<Unit<?>, String>> unitsOnlyInNewWithoutBugs = new ArrayList<>(unitsOnlyInNew);
        unitsOnlyInNewWithoutBugs.removeAll(indriyaBug);
        assertEquals(
                unitsOnlyInNewWithoutBugs.size() + " units only in new: " + unitsOnlyInNewWithoutBugs + "\n",
                indriyaBug,
                unitsOnlyInNew);
    }

    @Test
    public void namesOnlyInOld() throws Exception {
        SimpleUnitFormat simpleUnitFormat = SimpleUnitFormat.getInstance();

        List<Map.Entry<String, Unit<?>>> nameToUnitMap = toSortedList2(getNameToUnitMap(simpleUnitFormat));
        List<Map.Entry<String, Unit<?>>> symbolToUnit = toSortedList2(formatter.nameToUnit);

        List<Map.Entry<String, Unit<?>>> unitsOnlyInOld = nameToUnitMap.stream()
                .filter(entry -> !INDRIYA_ONLY_UNITS.contains(entry.getValue()))
                .filter(entry -> !symbolToUnit.contains(entry))
                .collect(Collectors.toList());
        List<Map.Entry<String, Unit<?>>> indriyaBug = asList(
                entry("hp(E)", ELECTRICAL_HORSEPOWER),
                entry("month", MONTH),
                entry("mo", MONTH),
                entry("mon", MONTH),
                entry("m3", CUBIC_METRE),
                entry("m³", CUBIC_METRE),
                entry("rpm", REVOLUTION_PER_MINUTE),
                // indriya parses wk, y and yr, GeoTools does not
                entry("wk", Units.WEEK),
                entry("y", Units.YEAR),
                entry("yr", Units.YEAR));

        assertEquals(
                unitsOnlyInOld.size() + " names only in old: " + unitsOnlyInOld + "\n", indriyaBug, unitsOnlyInOld);
    }

    @Test
    public void namesOnlyInNew() throws Exception {
        SimpleUnitFormat simpleUnitFormat = SimpleUnitFormat.getInstance();

        List<Map.Entry<String, Unit<?>>> nameToUnitMap = toSortedList2(getNameToUnitMap(simpleUnitFormat));
        List<Map.Entry<String, Unit<?>>> symbolToUnit = toSortedList2(formatter.nameToUnit);

        List<Map.Entry<String, Unit<?>>> unitsOnlyInNew = symbolToUnit.stream()
                .filter(entry -> !nameToUnitMap.contains(entry))
                .collect(Collectors.toList());
        // only one kind of µ is added for those special-cased units in indriya:
        List<Map.Entry<String, Unit<?>>> indriyaBug = asList(
                entry("μg", Units.GRAM.prefix(MICRO)),
                entry("μl", Units.LITRE.prefix(MICRO)),
                entry("μ°C", Units.CELSIUS.prefix(MICRO)),
                entry("μ℃", Units.CELSIUS.prefix(MICRO)),
                entry("μOhm", Units.OHM.prefix(MICRO)));
        List<Map.Entry<String, Unit<?>>> unitsOnlyInNewWithoutBug = new ArrayList<>(unitsOnlyInNew);
        unitsOnlyInNewWithoutBug.removeAll(indriyaBug);
        assertEquals(
                unitsOnlyInNewWithoutBug.size() + " names only in new: " + unitsOnlyInNewWithoutBug + "\n",
                indriyaBug,
                unitsOnlyInNew);
    }

    private static <K, V> Map.Entry<K, V> entry(K key, V value) {
        return new AbstractMap.SimpleEntry<>(key, value);
    }

    @SuppressWarnings("unchecked") // reflection in use, cannot be type safe
    private static Map<Unit<?>, String> getUnitToNameMap(UnitFormat instance) throws Exception {
        Field unitToNameField = instance.getClass().getDeclaredField("unitToName");
        unitToNameField.setAccessible(true);
        return (HashMap<Unit<?>, String>) unitToNameField.get(instance);
    }

    @SuppressWarnings("unchecked") // reflection in use, cannot be type safe
    private static Map<String, Unit<?>> getNameToUnitMap(UnitFormat instance) throws Exception {
        Field nameToUnitField = instance.getClass().getDeclaredField("nameToUnit");
        nameToUnitField.setAccessible(true);
        return (HashMap<String, Unit<?>>) nameToUnitField.get(instance);
    }

    private static List<Map.Entry<Unit<?>, String>> toSortedList1(Map<Unit<?>, String> map) {
        return map.entrySet().stream()
                .sorted(Comparator.nullsFirst(
                        Comparator.comparing(x -> x.getKey().toString())))
                .collect(Collectors.toList());
    }

    private static List<Map.Entry<String, Unit<?>>> toSortedList2(Map<String, Unit<?>> map) {
        return map.entrySet().stream()
                .sorted(Comparator.nullsFirst(
                        Comparator.comparing(x -> x.getValue().toString())))
                .collect(Collectors.toList());
    }

    /** Units that indriya labels and GeoTools does not: square metres, prefixed cubic metres, binary prefixes. */
    private static final Set<Unit<?>> INDRIYA_ONLY_UNITS = indriyaOnlyUnits();

    private static Set<Unit<?>> indriyaOnlyUnits() {
        Set<Unit<?>> units = new HashSet<>(List.of(SQUARE_METRE));
        for (MetricPrefix prefix : MetricPrefix.values()) {
            units.add(SQUARE_METRE.prefix(prefix));
            units.add(CUBIC_METRE.prefix(prefix));
        }
        for (BinaryPrefix prefix : BinaryPrefix.values()) {
            for (Unit<?> unit : List.of(Units.LITRE, SQUARE_METRE, CUBIC_METRE, Units.CELSIUS, Units.OHM)) {
                units.add(unit.prefix(prefix));
            }
        }
        return units;
    }

    private static final BaseUnitFormatter formatter = new BaseUnitFormatter(Stream.of(
                    UnitDefinitions.DIMENSIONLESS,
                    UnitDefinitions.SI_BASE,
                    UnitDefinitions.CONSTANTS,
                    UnitDefinitions.SI_DERIVED,
                    UnitDefinitions.NON_SI,
                    UnitDefinitions.US_CUSTOMARY)
            .flatMap(Collection::stream)
            .collect(Collectors.toList()));
}
