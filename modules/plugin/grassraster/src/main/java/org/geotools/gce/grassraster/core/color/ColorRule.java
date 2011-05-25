/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2006-2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.grassraster.core.color;

import java.awt.Color;
import java.util.List;

/**
 * <p>
 * A JGrass colorrule
 * </p>
 * @author Andrea Antonello - www.hydrologis.com
 * @author John Preston
 * @since 1.1.0
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/plugin/grassraster/src/main/java/org/geotools/gce/grassraster/core/color/ColorRule.java $
 */
public class ColorRule {
    // public static final byte[] blank = {0, 0, 0, 0};

    private float low = 0f;

    private float range = 0f;

    private byte[] catColor = null;

    private float rmul = 0f;

    private float gmul = 0f;

    private float bmul = 0f;

    /** Creates a new instance of ColorRule */
    public ColorRule( int cat, int r, int g, int b ) {
        low = cat;
        range = 0f;
        catColor = new byte[]{(byte) r, (byte) g, (byte) b, (byte) 255};
    }

    /** Creates a new instance of ColorRule */
    public ColorRule( float cat0, int r0, int g0, int b0, float cat1, int r1, int g1, int b1 ) {
        if (cat1 > cat0) {
            low = cat0;
            range = cat1 - cat0;
            catColor = new byte[]{(byte) r0, (byte) g0, (byte) b0, (byte) 255};
            rmul = (r1 - r0) / range;
            gmul = (g1 - g0) / range;
            bmul = (b1 - b0) / range;
        } else {
            low = cat1;
            range = cat0 - cat1;
            catColor = new byte[]{(byte) r1, (byte) g1, (byte) b1, (byte) 255};
            rmul = (r0 - r1) / range;
            gmul = (g0 - g1) / range;
            bmul = (b0 - b1) / range;
        }
    }

    /**
     *
     */
    public int compare( float cat ) {
        float diff = cat - low;

        if (diff < 0)
            return -1;
        else if (diff <= range)
            return 0;
        // else if (diff < 0)
        // return -1;

        return 1;
    }

    /**
     * Return the colour tupple for specified category value
     */
    public byte[] getColor( float cat ) {
        /* First check to see if the category
         * value is within the range of this rule. */
        float diff = cat - low;
        if (diff <= 0f)
            return catColor;
        // else if (diff < 0)
        // {
        // /* Category value below lowest value in this rule. */
        // return new byte[]{(byte)catColor[0], (byte)catColor[1],
        // (byte)catColor[2], (byte)catColor[3]};
        // }
        else if (diff > range) {
            return new byte[]{(byte) ((int) (rmul * range) + (int) catColor[0]),
                    (byte) ((int) (gmul * range) + (int) catColor[1]),
                    (byte) ((int) (bmul * range) + (int) catColor[2]), (byte) catColor[3]};
        }

        /* Calculate the color from the gradient */
        return new byte[]{(byte) ((int) (rmul * diff) + (int) catColor[0]),
                (byte) ((int) (gmul * diff) + (int) catColor[1]),
                (byte) ((int) (bmul * diff) + (int) catColor[2]), (byte) catColor[3]};
    }

    /**
     *
     */
    public float getLowCategoryValue() {
        return low;
    }

    /**
     *
     */
    public float getCategoryRange() {
        return range;
    }

    /**
     *
     */
    public String toString() {
        if (range == 0)
            return String.valueOf(low) + ":[" + (catColor[0] & 0xff) + "," + (catColor[1] & 0xff)
                    + "," + (catColor[2] & 0xff) + "," + (catColor[3] & 0xff) + "]";
        else
            return String.valueOf(low) + "-" + String.valueOf(low + range) + ":["
                    + (catColor[0] & 0xff) + "," + (catColor[1] & 0xff) + ","
                    + (catColor[2] & 0xff) + "," + (catColor[3] & 0xff) + "]-["
                    + ((int) (rmul * range) + (int) catColor[0]) + ","
                    + ((int) (gmul * range) + (int) catColor[1]) + ","
                    + ((int) (bmul * range) + (int) catColor[2]) + "," + (catColor[3] & 0xff) + "]";
    }

    public static void parseColorRule( String colorRule, List<Double> values, List<Color> colors ) {
        String[] ruleSplit = colorRule.split(":"); //$NON-NLS-1$

        String valuesString = ruleSplit[0];
        int indexOf = valuesString.indexOf('-');
        String firstValueStr = valuesString.substring(0, indexOf);
        String secondValueStr = valuesString.substring(indexOf + 1, valuesString.length());

        values.add(Double.parseDouble(firstValueStr));
        values.add(Double.parseDouble(secondValueStr));

        String colorsString = ruleSplit[1];
        String[] colorsSplit = colorsString.split("\\]-\\["); //$NON-NLS-1$
        String col1 = colorsSplit[0].substring(1);
        String col2 = colorsSplit[1].substring(0, colorsSplit[1].length() - 1);
        String[] rgba1 = col1.split(","); //$NON-NLS-1$
        String[] rgba2 = col2.split(","); //$NON-NLS-1$

        Color color1 = new Color(Integer.parseInt(rgba1[0]), Integer.parseInt(rgba1[1]), Integer
                .parseInt(rgba1[2]), Integer.parseInt(rgba1[3]));
        Color color2 = new Color(Integer.parseInt(rgba2[0]), Integer.parseInt(rgba2[1]), Integer
                .parseInt(rgba2[2]), Integer.parseInt(rgba2[3]));

        colors.add(color1);
        colors.add(color2);

    }
}
