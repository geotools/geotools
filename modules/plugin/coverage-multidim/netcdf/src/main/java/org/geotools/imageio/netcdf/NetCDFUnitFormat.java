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
package org.geotools.imageio.netcdf;

import static java.util.Map.entry;
import static tech.units.indriya.AbstractUnit.ONE;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.measure.Unit;
import javax.measure.format.MeasurementParseException;
import org.geotools.measure.BaseUnitFormatter;
import org.geotools.measure.UnitDefinition;
import org.geotools.measure.UnitDefinitions;
import org.geotools.util.logging.Logging;
import si.uom.NonSI;
import tech.units.indriya.function.LogConverter;

/**
 * Parser/Encoder for units expressed in the NetCDF CF syntax, with ability to configure the unit
 * syntax transformation and allow setting up custom aliases just for the NetCDF case.
 */
public final class NetCDFUnitFormat extends BaseUnitFormatter {

    // It is questionable whether a global instance is a good idea,
    // considering that this class provides mutating operations.
    public static NetCDFUnitFormat getInstance() {
        return INSTANCE;
    }

    private static final Logger LOGGER = Logging.getLogger(NetCDFUnitFormat.class);

    /** Unit aliases config file name (normally looked up in the classpath) */
    public static final String NETCDF_UNIT_ALIASES = "netcdf-unit-aliases.properties";

    /** Unit replacements config file name (normally looked up in the classpath) */
    public static final String NETCDF_UNIT_REPLACEMENTS = "netcdf-unit-replacements.properties";

    /** Hard coded replacements for common operations */
    private static final Map<String, String> CONTENT_REPLACEMENTS = Map.ofEntries(
            entry(" ", "*"), //
            entry("-", "^-"),
            entry(".", "*"));

    private static final List<UnitDefinition> UNIT_DEFINITIONS = Stream.of(
                    UnitDefinitions.DIMENSIONLESS,
                    UnitDefinitions.CONSTANTS,
                    UnitDefinitions.SI_BASE,
                    UnitDefinitions.SI_DERIVED,
                    UnitDefinitions.NON_SI,
                    UnitDefinitions.US_CUSTOMARY,
                    UnitDefinitions.GEOTOOLS)
            .flatMap(Collection::stream)
            .collect(Collectors.toUnmodifiableList());

    private static final NetCDFUnitFormat INSTANCE = createWithBuiltInConfig();

    /** Creates a NetCDFUnitFormat with the built-in defaults. */
    public static NetCDFUnitFormat createWithBuiltInConfig() {
        return new NetCDFUnitFormat(builtInReplacements(), builtInAliases());
    }

    public static NetCDFUnitFormat create(Map<String, String> replacements, Map<String, String> aliases) {
        return new NetCDFUnitFormat(replacements, aliases);
    }

    public static Map<String, String> builtInReplacements() {
        return loadPropertiesOrdered(NetCDFUnitFormat.class.getResourceAsStream(NETCDF_UNIT_REPLACEMENTS));
    }

    public static Map<String, String> builtInAliases() {
        return loadPropertiesOrdered(NetCDFUnitFormat.class.getResourceAsStream(NETCDF_UNIT_ALIASES));
    }

    private Map<String, String> REPLACEMENTS;

    private NetCDFUnitFormat(Map<String, String> replacements, Map<String, String> aliases) {
        super(UNIT_DEFINITIONS);
        setReplacements(replacements);
        setAliases(aliases);
    }

    /**
     * Loads a properties file preserving its internal order
     *
     * @param is The input stream to be read
     * @return The contents as a {@link LinkedHashMap} preserving the file contents
     */
    public static LinkedHashMap<String, String> loadPropertiesOrdered(InputStream is) {
        try {
            LinkedHashMap<String, String> result = new LinkedHashMap<>();
            Properties props = new Properties() {

                @Override
                public Object put(Object key, Object value) {
                    result.put((String) key, (String) value);
                    return super.put(key, value);
                }
            };
            props.load(new InputStreamReader(is, StandardCharsets.UTF_8));
            return result;
        } catch (IOException e) {
            throw new RuntimeException("Failed to load the build-in config file: " + NETCDF_UNIT_ALIASES + e);
        }
    }

    /**
     * Configures the string replacements to be performed before trying to parse the units.
     *
     * @param replacements The replacements to be used. It is strongly advised to use a {@link
     *     LinkedHashMap} as replacements are run from top to bottom, in order, and the order might
     *     influence the results
     */
    public void setReplacements(Map<String, String> replacements) {
        REPLACEMENTS = new LinkedHashMap<>(replacements);
    }

    /**
     * Configures the aliases to be used on the unit parser. An alias is a different name for a
     * unit.
     */
    public void setAliases(Map<String, String> aliases) {

        // missing unit that cannot be expressed via config files
        Unit<?> bel = ONE.transform(new LogConverter(10));
        addAlias(bel.divide(10), "dB");

        // register non SI units as well
        for (Unit<?> u : NonSI.getInstance().getUnits()) {
            if (u.getSymbol() != null && unknownSymbol(u.getSymbol())) {
                addAlias(u, u.getSymbol());
            }
        }

        // register a notion of unitless
        addLabel(ONE, "unitless");

        // go with the aliases (key -> value, that is, alias -> actual unit)
        for (Map.Entry<String, String> entry : aliases.entrySet()) {
            try {
                addAlias(this.parse(entry.getValue()), entry.getKey());
            } catch (MeasurementParseException ex) {
                LOGGER.log(
                        Level.WARNING,
                        "Failed to parse " + entry.getKey() + " -> " + entry.getValue() + ", skipped.",
                        ex);
            }
        }
    }

    /** Checks if the symbol in question is already known to the formatter, or not */
    private boolean unknownSymbol(String symbol) {
        try {
            parse(symbol);
            return false;
        } catch (MeasurementParseException e) {
            return true;
        }
    }

    /** Parses a unit applying the configured set of replacements and aliases */
    public Unit<?> parse(String spec) {
        // apply blind replacements
        boolean replaced = false;
        for (Map.Entry<String, String> entry : REPLACEMENTS.entrySet()) {
            // was it a full replacement or a partial one?
            replaced = spec.equals(entry.getKey());
            spec = spec.replace(entry.getKey(), entry.getValue());
            if (replaced) {
                break;
            }
        }

        // if not recognized as a hard replacement, massage the unit spec to match the unit libs
        // expectations
        if (!replaced) {
            for (Map.Entry<String, String> entry : CONTENT_REPLACEMENTS.entrySet()) {
                spec = spec.replace(entry.getKey(), entry.getValue());
            }
        }

        // do a normal parse, catch and rethrow because the default exception message does not
        // even say what was being parsed
        try {
            return super.parse(spec);
        } catch (MeasurementParseException e) {
            throw new MeasurementParseException("Failed to parse " + spec, e.getParsedString(), e.getPosition());
        }
    }
}
