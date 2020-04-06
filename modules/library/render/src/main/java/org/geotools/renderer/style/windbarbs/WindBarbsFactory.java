/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014-2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.renderer.style.windbarbs;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.geotools.referencing.operation.transform.AffineTransform2D;
import org.geotools.renderer.style.MarkFactory;
import org.geotools.renderer.style.windbarbs.WindBarb.WindBarbDefinition;
import org.geotools.util.SoftValueHashMap;
import org.opengis.feature.Feature;
import org.opengis.filter.expression.Expression;

/**
 * Factory to produce WindBarbs. Urls for wind barbs are in the form:
 * windbarbs://default(speed_value)[units_of_measure]
 *
 * <p>TODO: We may consider adding a FLAG to say whether the arrows are toward wind (meteo
 * convention) or against wind (ocean convention)
 *
 * @author Daniele Romagnoli, GeoSolutions SAS
 */
public class WindBarbsFactory implements MarkFactory {

    static final int MAX_SPEED = 300;

    private static final int NUMBER_OF_ITEMS_IN_CACHE = MAX_SPEED / 5;

    /** WINDBARB_DEFINITION */
    private static final String WINDBARB_DEFINITION = "windbarbs://.*\\(.{1,}\\)\\[.{1,5}\\]\\??.*";

    /** SOUTHERN_EMISPHERE_FLIP */
    public static final AffineTransform SOUTHERN_EMISPHERE_FLIP =
            new AffineTransform2D(AffineTransform.getScaleInstance(-1, 1));

    /** The loggermodule. */
    private static final Logger LOGGER =
            org.geotools.util.logging.Logging.getLogger(WindBarbsFactory.class);

    public static final String WINDBARBS_PREFIX = "windbarbs://";

    private static final String DEFAULT_NAME = "default";

    private static Pattern SPEED_PATTERN =
            Pattern.compile(
                    "(.*?)\\((.{1,})\\)(.*)"); // Pattern.compile("(.*?)(\\d+\\.?\\d*)(.*)");

    private static Pattern WINDBARB_SET_PATTERN = Pattern.compile("(.*?)://(.*)\\((.*)");

    private static Pattern UNIT_PATTERN = Pattern.compile("(.*?)\\[(.*)\\](.*)");

    private static final SoftValueHashMap<WindBarbDefinition, Map<Integer, Shape>> CACHE;

    static {
        CACHE =
                new SoftValueHashMap<WindBarb.WindBarbDefinition, Map<Integer, Shape>>(
                        1); // make room for the default definition
        CACHE.put(
                WindBarb.DEFAULT_WINDBARB_DEFINITION,
                createWindBarbs(WindBarb.DEFAULT_WINDBARB_DEFINITION));
    }

    /**
     * Return a shape with the given url.
     *
     * @see org.geotools.renderer.style.MarkFactory#getShape(java.awt.Graphics2D,
     *     org.opengis.filter.expression.Expression, org.opengis.feature.Feature)
     */
    public Shape getShape(Graphics2D graphics, Expression symbolUrl, Feature feature) {

        // CHECKS
        // cannot handle a null url
        if (symbolUrl == null) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine("Provided null symbol to the WindBarbs Factory");
            }
            return null;
        }
        // cannot handle a null feature
        if (feature == null) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine("Provided null feature to the WindBarbs Factory");
            }
            return null;
        }

        //
        // START PARSING CODE
        //
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Trying to resolve symbol:" + symbolUrl.toString());
        }

        // evaluate string from feature to extract all values
        final String wellKnownName = symbolUrl.evaluate(feature, String.class);
        if (wellKnownName == null || wellKnownName.length() <= 0) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine("Unable to resolve symbol provided to WindBarbs Factory");
            }
            return null;
        }

        // //
        //
        // Basic Syntax
        //
        // //
        if (!wellKnownName.matches(WindBarbsFactory.WINDBARB_DEFINITION)) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine("Unable to resolve symbol: " + wellKnownName);
            }
            return null;
        }
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Resolved symbol " + wellKnownName);
        }

        // ok from now on we should have a real windbarb, let's lower the log level

        // //
        //
        // WindBarbs set
        //
        // //
        String windBarbName = null;
        Matcher matcher = WINDBARB_SET_PATTERN.matcher(wellKnownName);
        if (matcher.matches()) {
            try {
                windBarbName = matcher.group(2);
            } catch (Exception e) {
                if (LOGGER.isLoggable(Level.INFO)) {
                    LOGGER.log(
                            Level.INFO,
                            "Unable to parse windbarb set from string: " + wellKnownName,
                            e);
                }

                return null;
            }
        }
        if (windBarbName == null || windBarbName.length() <= 0) {
            if (LOGGER.isLoggable(Level.INFO)) {
                LOGGER.fine("Unable to parse windBarbName from string: " + wellKnownName);
            }
            return null;
        }
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Resolved windBarbName " + windBarbName);
        }

        // //
        //
        // Looking for speed
        //
        // //
        matcher = SPEED_PATTERN.matcher(wellKnownName);
        double speed = Double.NaN;
        if (matcher.matches()) {
            String speedString = "";
            try {
                speedString = matcher.group(2);
                speed = Double.parseDouble(speedString);
            } catch (Exception e) {
                if (LOGGER.isLoggable(Level.INFO)) {
                    LOGGER.log(Level.INFO, "Unable to parse speed from string: " + speedString, e);
                }
                return null;
            }
        } else {
            if (LOGGER.isLoggable(Level.INFO)) {
                LOGGER.fine("Unable to parse speed from string: " + wellKnownName);
            }
            return null;
        }

        // //
        //
        // Looking for unit value
        //
        // //
        String uom = null; // no default
        matcher = UNIT_PATTERN.matcher(wellKnownName);
        if (matcher.matches()) {
            uom = matcher.group(2);
        }
        if (uom == null || uom.length() <= 0) {
            if (LOGGER.isLoggable(Level.INFO)) {
                LOGGER.info("Unable to parse UoM from " + wellKnownName);
            }
            return null;
        }

        // so far so good
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("WindBarbs name " + windBarbName + "with Speed " + speed + "[" + uom + "]");
        }

        // //
        //
        // Params
        //
        // //
        int index = wellKnownName.indexOf('?');
        if (index > 0) {
            final Map<String, String> params = new HashMap<String, String>();
            final String kvp = wellKnownName.substring(index + 1);
            String[] pairs = kvp.split("&");
            if (pairs != null && pairs.length > 0) {
                for (String pair : pairs) {
                    // split
                    String[] splitPair = pair.split("=");
                    if (splitPair != null && splitPair.length > 0) {
                        params.put(splitPair[0].toLowerCase(), splitPair[1]);
                    } else {
                        if (LOGGER.isLoggable(Level.FINE)) {
                            LOGGER.fine("Skipping pair " + pair);
                        }
                    }
                }

                // checks
                if (!params.isEmpty()) {
                    return getWindBarb(windBarbName, speed, uom, params);
                }
            }
        } else {
            // make sure we close with ] and nothing else after
            if (!wellKnownName.endsWith("]")) {
                if (LOGGER.isLoggable(Level.INFO)) {
                    LOGGER.info("The provided symbol may be missing a ? before the KVP part.");
                }
                return null;
            }
        }

        // //
        //
        // Get shape if possible
        //
        // //
        return getWindBarb(windBarbName, speed, uom);
    }

    private static Map<Integer, Shape> createWindBarbs(WindBarbDefinition definition) {
        final Map<Integer, Shape> windBarbsMapping = new HashMap<Integer, Shape>();
        for (int i = 0; i <= NUMBER_OF_ITEMS_IN_CACHE; i++) {
            windBarbsMapping.put(
                    i, new WindBarb(definition, i * 5).build()); // pass over the knots definition
        }

        // no module x----- symbol
        windBarbsMapping.put(-1, new WindBarb(definition, -1).build());
        return windBarbsMapping;
    }

    /** */
    private Shape getWindBarb(
            String windBarbName, double speed, String units, Map<String, String> params) {
        // speed
        try {
            double knots = SpeedConverter.toKnots(speed, units);

            // shape
            return getWindBarbForKnots(windBarbName, knots, params);
        } catch (Exception e) {
            if (LOGGER.isLoggable(Level.INFO)) {
                LOGGER.log(Level.INFO, e.getLocalizedMessage(), e);
            }
            return null;
        }
    }

    /** Get the proper WindBarb related to the referred speed */
    private Shape getWindBarb(final String windBarbName, final double speed, final String units) {
        return getWindBarb(windBarbName, speed, units, null);
    }

    private Shape getWindBarbForKnots(
            final String windBarbName, final double knots, Map<String, String> params) {
        // No module is signaled by NaN
        // checking the barbs using our own limits
        int index = -1; // no wind module is -1
        if (!Double.isNaN(knots)) {
            if (knots < 3) {
                index = 0;
            } else {
                index = (int) ((knots - 3.0) / 5.0 + 1);
            }
        }

        // get the barb
        if (windBarbName.equalsIgnoreCase(DEFAULT_NAME)) {

            WindBarbDefinition definition = parseWindBarbsDefinition(params);
            Map<Integer, Shape> windbarbs = null;
            synchronized (CACHE) {
                windbarbs = CACHE.get(definition);
                if (windbarbs == null) {
                    windbarbs = createWindBarbs(definition);
                    CACHE.put(definition, windbarbs);
                }
            }

            // get shape from cached definitions.
            Shape shp = windbarbs.get(index);
            if (shp == null) {
                // No definition available. build it on the fly without caching it
                // (supposing it's a rare barb since we are caching up to MAX_SPEED)
                shp = new WindBarb(definition, (int) knots).build();
            }
            if (params == null || params.isEmpty()) {
                return shp;
            }

            if (params.containsKey("emisphere") && params.get("emisphere").equalsIgnoreCase("s")) {
                // flip shape on Y axis
                return SOUTHERN_EMISPHERE_FLIP.createTransformedShape(shp);
            }
            if (params.containsKey("hemisphere")
                    && params.get("hemisphere").equalsIgnoreCase("s")) {
                // flip shape on Y axis
                return SOUTHERN_EMISPHERE_FLIP.createTransformedShape(shp);
            }
            return shp;
        }

        throw new IllegalArgumentException("Wrong windbard name:" + windBarbName);
    }

    /** @return a {@link WindBarbDefinition} for the provided params */
    private WindBarbDefinition parseWindBarbsDefinition(Map<String, String> params) {
        final WindBarbDefinition retValue = WindBarb.DEFAULT_WINDBARB_DEFINITION;
        if (params == null || params.size() <= 0) {
            return retValue;
        }

        // parse
        String temp = null;

        // //
        //
        // vectorLength
        //
        // //
        int vectorLength = retValue.vectorLength;
        if (params.containsKey("vectorlength")) {
            // get value
            temp = params.get("vectorlength");

            // check and parse
            if (temp == null || temp.length() <= 0) {
                if (LOGGER.isLoggable(Level.INFO)) {
                    LOGGER.info(
                            "Wrong vectorLength provided: "
                                    + temp
                                    + " resorting to default wind barb definition");
                }
                return retValue; // default
            }
            try {
                vectorLength = Integer.parseInt(temp);
            } catch (Exception e) {
                if (LOGGER.isLoggable(Level.INFO)) {
                    LOGGER.info(
                            "Wrong vectorLength provided: "
                                    + temp
                                    + " resorting to default wind barb definition");
                }
                return retValue; // default
            }
            if (vectorLength <= 0) {
                if (LOGGER.isLoggable(Level.INFO)) {
                    LOGGER.info(
                            "Wrong vectorLength provided: "
                                    + temp
                                    + " resorting to default wind barb definition");
                }
                return retValue; // default
            }
        }

        // //
        //
        // basePennantLength
        //
        // //
        int basePennantLength = retValue.basePennantLength;
        if (params.containsKey("basepennantlength")) {
            // get value
            temp = params.get("basepennantlength");

            // check and parse
            if (temp == null || temp.length() <= 0) {
                if (LOGGER.isLoggable(Level.INFO)) {
                    LOGGER.info(
                            "Wrong basePennantLength provided: "
                                    + temp
                                    + " resorting to default wind barb definition");
                }
                return retValue; // default
            }
            try {
                basePennantLength = Integer.parseInt(temp);
            } catch (Exception e) {
                if (LOGGER.isLoggable(Level.INFO)) {
                    LOGGER.info(
                            "Wrong basePennantLength provided: "
                                    + temp
                                    + " resorting to default wind barb definition");
                }
                return retValue; // default
            }
            if (basePennantLength <= 0 || basePennantLength >= vectorLength) {
                if (LOGGER.isLoggable(Level.INFO)) {
                    LOGGER.info(
                            "Wrong basePennantLength provided: "
                                    + temp
                                    + " resorting to default wind barb definition");
                }
                return retValue; // default
            }
        }

        // //
        //
        // elementsSpacing
        //
        // //
        int elementsSpacing = retValue.elementsSpacing;
        if (params.containsKey("elementsspacing")) {
            // get value
            temp = params.get("elementsspacing");

            // check and parse
            if (temp == null || temp.length() <= 0) {
                if (LOGGER.isLoggable(Level.INFO)) {
                    LOGGER.info(
                            "Wrong elementsSpacing provided: "
                                    + temp
                                    + " resorting to default wind barb definition");
                }
                return retValue; // default
            }
            try {
                elementsSpacing = Integer.parseInt(temp);
            } catch (Exception e) {
                if (LOGGER.isLoggable(Level.INFO)) {
                    LOGGER.info(
                            "Wrong elementsSpacing provided: "
                                    + temp
                                    + " resorting to default wind barb definition");
                }
                return retValue; // default
            }
            if (elementsSpacing <= 0
                    || elementsSpacing >= vectorLength
                    || elementsSpacing + basePennantLength >= vectorLength) {
                if (LOGGER.isLoggable(Level.INFO)) {
                    LOGGER.info(
                            "Wrong elementsSpacing provided: "
                                    + temp
                                    + " resorting to default wind barb definition");
                }
                return retValue; // default
            }
        }

        // //
        //
        // longBarbLength
        //
        // //
        int longBarbLength = retValue.longBarbLength;
        if (params.containsKey("longbarblength")) {
            // get value
            temp = params.get("longbarblength");

            // check and parse
            if (temp == null || temp.length() <= 0) {
                if (LOGGER.isLoggable(Level.INFO)) {
                    LOGGER.info(
                            "Wrong longBarbLength provided: "
                                    + temp
                                    + " resorting to default wind barb definition");
                }
                return retValue; // default
            }
            try {
                longBarbLength = Integer.parseInt(temp);
            } catch (Exception e) {
                if (LOGGER.isLoggable(Level.INFO)) {
                    LOGGER.info(
                            "Wrong longBarbLength provided: "
                                    + temp
                                    + " resorting to default wind barb definition");
                }
                return retValue; // default
            }
            if (longBarbLength <= 0) {
                if (LOGGER.isLoggable(Level.INFO)) {
                    LOGGER.info(
                            "Wrong longBarbLength provided: "
                                    + temp
                                    + " resorting to default wind barb definition");
                }
                return retValue; // default
            }
        }

        // //
        //
        // zeroWindRadius
        //
        // //
        int zeroWindRadius = retValue.zeroWindRadius;
        if (params.containsKey("zerowindradius")) {
            // get value
            temp = params.get("zerowindradius");

            // check and parse
            if (temp == null || temp.length() <= 0) {
                if (LOGGER.isLoggable(Level.INFO)) {
                    LOGGER.info(
                            "Wrong zeroWindRadius provided: "
                                    + temp
                                    + " resorting to default wind barb definition");
                }
                return retValue; // default
            }
            try {
                zeroWindRadius = Integer.parseInt(temp);
            } catch (Exception e) {
                if (LOGGER.isLoggable(Level.INFO)) {
                    LOGGER.info(
                            "Wrong zeroWindRadius provided: "
                                    + temp
                                    + " resorting to default wind barb definition");
                }
                return retValue; // default
            }
            if (zeroWindRadius <= 0) {
                if (LOGGER.isLoggable(Level.INFO)) {
                    LOGGER.info(
                            "Wrong zeroWindRadius provided: "
                                    + temp
                                    + " resorting to default wind barb definition");
                }
                return retValue; // default
            }
        }

        // new definition
        return new WindBarbDefinition(
                vectorLength, basePennantLength, elementsSpacing, longBarbLength, zeroWindRadius);
    }
}
