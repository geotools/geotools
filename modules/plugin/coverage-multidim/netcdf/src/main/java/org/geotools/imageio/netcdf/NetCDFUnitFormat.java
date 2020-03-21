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

import static tec.uom.se.AbstractUnit.ONE;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.measure.Unit;
import javax.measure.format.ParserException;
import org.geotools.util.GeoToolsUnitFormat;
import org.geotools.util.logging.Logging;
import si.uom.NonSI;
import tec.uom.se.AbstractUnit;
import tec.uom.se.format.SimpleUnitFormat;
import tec.uom.se.function.LogConverter;

/**
 * Parser/Encoder for units expressed in the NetCDF CF syntax, with ability to configure the unit
 * syntax transformation and allow setting up custom aliases just for the NetCDF case.
 */
public class NetCDFUnitFormat {

    static final Logger LOGGER = Logging.getLogger(NetCDFUnitFormat.class);

    /**
     * We want to use a separate instance during re-configuration and get isolation from the normal
     * formatters, this abstract class allows to do the trick.
     */
    public abstract static class AbstractNetCDFUnitFormat extends GeoToolsUnitFormat {
        static class InternalFormat extends BaseGT2Format {

            public InternalFormat() {
                super.initUnits(SimpleUnitFormat.getInstance());
            }
        }

        public static SimpleUnitFormat getNewInstance() {
            return new InternalFormat();
        }
    }

    /** The format used to parse units */
    private static SimpleUnitFormat FORMAT;

    private static Map<String, String> REPLACEMENTS;

    /** Unit aliases config file name (normally looked up in the classpath) */
    public static final String NETCDF_UNIT_ALIASES = "netcdf-unit-aliases.properties";

    /** Unit replacements config file name (normally looked up in the classpath) */
    public static final String NETCDF_UNIT_REPLACEMENTS = "netcdf-unit-replacements.properties";

    /** Hard coded replacements for common operations */
    private static Map<String, String> CONTENT_REPLACEMENTS =
            new LinkedHashMap() {
                {
                    put(" ", "*");
                    put("-", "^-");
                    put(".", "*");
                }
            };

    static {
        reset();
    }

    /** Resets the format to the built-in defaults */
    public static void reset() {
        Map<String, String> replacements = loadBuiltInConfigFile(NETCDF_UNIT_REPLACEMENTS);
        setReplacements(replacements);
        Map<String, String> aliases = loadBuiltInConfigFile(NETCDF_UNIT_ALIASES);
        setAliases(aliases);
    }

    /**
     * Configures the string replacements to be performed before trying to parse the units.
     *
     * @param replacements The replacements to be used. It is strongly advised to use a {@link
     *     LinkedHashMap} as replacements are run from top to bottom, in order, and the order might
     *     influence the results
     */
    public static void setReplacements(Map<String, String> replacements) {
        REPLACEMENTS = new LinkedHashMap<>(replacements);
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
            Properties props =
                    new Properties() {

                        public Object put(Object key, Object value) {
                            result.put((String) key, (String) value);
                            return super.put(key, value);
                        }
                    };
            props.load(new InputStreamReader(is, Charset.forName("UTF-8")));
            return result;
        } catch (IOException e) {
            throw new RuntimeException(
                    "Failed to load the build-in config file: " + NETCDF_UNIT_ALIASES + e);
        }
    }

    private static Map<String, String> loadBuiltInConfigFile(String fileName) {
        return loadPropertiesOrdered(
                org.geotools.imageio.netcdf.NetCDFUnitFormat.class.getResourceAsStream(fileName));
    }

    /**
     * Configures the aliases to be used on the unit parser. An alias is a different name for a unit
     */
    public static void setAliases(Map<String, String> aliases) {
        SimpleUnitFormat format = AbstractNetCDFUnitFormat.getNewInstance();

        // missing unit that cannot be expressed via config files
        Unit bel = ONE.transform(new LogConverter(10));
        format.alias(bel.divide(10), "dB");

        // register non SI units as well
        for (Unit u : NonSI.getInstance().getUnits()) {
            if (u.getSymbol() != null && unknownSymbol(format, u.getSymbol())) {
                format.alias(u, u.getSymbol());
            }
        }

        // register a notion of unitless
        format.label(AbstractUnit.ONE, "unitless");

        // go with the aliases (key -> value, that is, alias -> actual unit)
        for (Map.Entry<String, String> entry : aliases.entrySet()) {
            try {
                format.alias(format.parse(entry.getValue()), entry.getKey());
            } catch (ParserException ex) {
                LOGGER.log(
                        Level.WARNING,
                        "Failed to parse "
                                + entry.getKey()
                                + " -> "
                                + entry.getValue()
                                + ", skipped.",
                        ex);
            }
        }

        // replace
        FORMAT = format;
    }

    /** Checks if the symbol in question is already known to the formatter, or not */
    private static boolean unknownSymbol(SimpleUnitFormat format, String symbol) {
        try {
            format.parse(symbol);
            return false;
        } catch (ParserException e) {
            return true;
        }
    }

    /** Parses a unit applying the configured set of replacements and aliases */
    public static Unit<?> parse(String spec) {
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
            return FORMAT.parse(spec);
        } catch (ParserException e) {
            throw new ParserException(
                    "Failed to parse " + spec, e.getParsedString(), e.getPosition());
        }
    }

    /**
     * Formats the unit
     *
     * @param unit The unit to be formatted
     * @return A string representation of the same unit.
     */
    public static String format(Unit<?> unit) {
        return FORMAT.format(unit);
    }

    /** Utility class, no instantiation */
    private NetCDFUnitFormat() {};
}
