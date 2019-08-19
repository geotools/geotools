/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.renderer.style.markwkt;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.renderer.style.MarkFactory;
import org.geotools.renderer.util.ExplicitBoundsShape;
import org.opengis.feature.Feature;
import org.opengis.filter.expression.Expression;

/**
 * Adds to the well-known shapes some symbols the weathermen may find useful.
 *
 * @author Luca Morandini lmorandini@ieee.org
 * @version $Id$
 */
public class MeteoMarkFactory implements MarkFactory {

    public static final String SHAPE_PREFIX = "extshape://";

    /** The logger for the rendering module. */
    private static final Logger LOGGER =
            org.geotools.util.logging.Logging.getLogger(MeteoMarkFactory.class);

    /**
     * Key for the extshape://arrow height ratio (between 0 and 1000). Default value is 2, twice as
     * high as large
     */
    public static final String ARROW_HEIGHT_RATIO_KEY = "hr";

    /**
     * Key for the extshape://arrow base line thickness (must be between 0, just border, and 1,
     * which turns the arrow into a irregular pentagon, "little house" like). Default value is 0.2
     */
    public static final String ARROW_THICKNESS_KEY = "t";

    /**
     * Key for the extshape://arrow location of the arrowhead base, a value of 0 turns the shape
     * into a triangle, a value of 1 into a rectangle. Default value is 0.5
     */
    public static final String ARROWHEAD_BASE_KEY = "ab";

    protected static final Map<String, Shape> WELLKNOWN_SHAPES = new HashMap<String, Shape>();

    static {
        GeneralPath gp = new GeneralPath();

        gp = new GeneralPath();
        ExplicitBoundsShape bnd = null;

        gp.moveTo(-0.145f, 0.000f);
        gp.lineTo(0.000f, 0.175f);
        gp.lineTo(0.105f, 0.000f);
        gp.closePath();
        bnd = new ExplicitBoundsShape(gp);
        bnd.setBounds(new Rectangle2D.Double(-0.5, -0.5, 0.5, 0.5));
        WELLKNOWN_SHAPES.put("triangle", bnd);

        gp = new GeneralPath();
        gp.moveTo(-0.125f, 0.000f);
        gp.curveTo(-0.125f, 0.000f, 0.000f, 0.250f, 0.125f, 0.000f);
        gp.closePath();
        bnd = new ExplicitBoundsShape(gp);
        bnd.setBounds(new Rectangle2D.Double(-0.5, -0.5, 0.5, 0.5));
        WELLKNOWN_SHAPES.put("emicircle", bnd);

        gp = new GeneralPath();
        gp.moveTo(-0.395f, 0.000f);
        gp.lineTo(-0.250f, -0.175f);
        gp.lineTo(-0.145f, 0.000f);
        gp.moveTo(0.125f, 0.000f);
        gp.curveTo(0.125f, 0.000f, 0.250f, 0.250f, 0.375f, 0.000f);
        gp.closePath();
        bnd = new ExplicitBoundsShape(gp);
        bnd.setBounds(new Rectangle2D.Double(-0.5, -0.5, 1.0, 1.0));
        WELLKNOWN_SHAPES.put("triangleemicircle", bnd);

        gp = new GeneralPath();
        gp.moveTo(0f, 1f);
        gp.lineTo(0.5, 0f);
        gp.lineTo(0.1f, 0f);
        gp.lineTo(0.1f, -1f);
        gp.lineTo(-0.1f, -1f);
        gp.lineTo(-0.1f, 0f);
        gp.lineTo(-0.5f, 0f);
        gp.closePath();
        WELLKNOWN_SHAPES.put("narrow", gp);

        // South Arrow
        AffineTransform at = AffineTransform.getQuadrantRotateInstance(2);
        gp = new GeneralPath();
        gp.moveTo(0f, 1f);
        gp.lineTo(0.5, 0f);
        gp.lineTo(0.1f, 0f);
        gp.lineTo(0.1f, -1.0f);
        gp.lineTo(-0.1f, -1.0f);
        gp.lineTo(-0.1f, 0f);
        gp.lineTo(-0.5f, 0f);
        gp.closePath();
        gp.transform(at);
        WELLKNOWN_SHAPES.put("sarrow", gp);

        // a sane arrow, centered in its middle, with its actual size
        WELLKNOWN_SHAPES.put("arrow", buildDynamicArrow(2, 0.2f, 0.5f));
    }

    /*
     * Return a shape with the given url.
     *
     * @see org.geotools.renderer.style.MarkFactory#getShape(java.awt.Graphics2D,
     * org.opengis.filter.expression.Expression, org.opengis.feature.Feature)
     */
    public Shape getShape(Graphics2D graphics, Expression symbolUrl, Feature feature)
            throws Exception {
        // cannot handle a null url
        if (symbolUrl == null) return null;

        // see if it's a shape
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Trying to resolve symbol:" + symbolUrl.toString());
        }
        String wellKnownName = symbolUrl.evaluate(feature, String.class);
        if (wellKnownName == null || !wellKnownName.startsWith(SHAPE_PREFIX)) {
            // see if it's a shape
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine("Unable to resolve symbol");
            }
            return null;
        }

        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Resolved symbol");
        }
        String localName = wellKnownName.substring(SHAPE_PREFIX.length());
        if (localName.startsWith("arrow?")) {
            return buildDynamicArrow(localName);
        } else {
            return WELLKNOWN_SHAPES.get(localName);
        }
    }

    private Shape buildDynamicArrow(String name) {
        Map<String, String> params = getParams(name);
        float height = 1;
        float thickness = 0.2f;
        float arrowBase = 0.5f;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            String key = entry.getKey();
            String svalue = entry.getValue();
            float value = 0;
            try {
                value = Float.valueOf(svalue);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid numerical value " + svalue);
            }
            switch (key) {
                case ARROW_HEIGHT_RATIO_KEY:
                    validateRange(key, value, 0, 1000);
                    height = value;
                    break;
                case ARROW_THICKNESS_KEY:
                    validateRange(key, value, 0, 1);
                    thickness = value;
                    break;
                case ARROWHEAD_BASE_KEY:
                    validateRange(key, value, 0, 1);
                    arrowBase = value;
                    break;
                default:
                    LOGGER.warning(
                            "Unexpected key value pair "
                                    + key
                                    + "="
                                    + svalue
                                    + " for extshape://arrow");
            }
        }
        return buildDynamicArrow(height, thickness, arrowBase);
    }

    private static Shape buildDynamicArrow(float height, float thickness, float arrowBase) {
        GeneralPath gp = new GeneralPath();
        // start from the point of the arrow
        gp.moveTo(0f, height / 2);
        // the right base of the arrow
        float arrowBaseHeight = height / 2 - height * (1 - arrowBase);
        gp.lineTo(0.5, arrowBaseHeight);
        // back to the center
        float t2 = thickness / 2;
        if (t2 < 0.5) {
            gp.lineTo(t2, arrowBaseHeight);
        }
        // down to the base
        gp.lineTo(t2, -height / 2);
        if (t2 > 0) {
            // go the the other side of the base
            gp.lineTo(-t2, -height / 2);
        }
        // back up to the arrow base
        if (t2 < 0.5) {
            gp.lineTo(-t2, arrowBaseHeight);
        }
        gp.lineTo(-0.5f, arrowBaseHeight);
        // and finish
        gp.closePath();

        return gp;
    }

    private void validateRange(String key, double value, double min, double max) {
        if (value < min || value > max) {
            throw new IllegalArgumentException(
                    "Invalid value "
                            + value
                            + " for key "
                            + key
                            + ", should have been between "
                            + min
                            + " and "
                            + max
                            + " (extreme excluded)");
        }
    }

    private Map<String, String> getParams(String name) {
        Map<String, String> params = new HashMap<>();
        String kvpPart = name.substring(name.indexOf('?') + 1);
        String[] keyValues = kvpPart.split("&");
        for (String keyValue : keyValues) {
            String[] kv = keyValue.split("=");
            if (kv.length != 2 || kv[0].isEmpty() || kv[1].isEmpty()) {
                LOGGER.fine("Skipping invalid kvp pair " + keyValue);
            }
            params.put(kv[0], kv[1]);
        }
        return params;
    }
}
