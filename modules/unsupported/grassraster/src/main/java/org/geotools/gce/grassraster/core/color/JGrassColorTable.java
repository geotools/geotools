/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2006-2010, Open Source Geospatial Foundation (OSGeo)
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
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.geotools.gce.grassraster.JGrassMapEnvironment;

/**
 * Represents a GRASS rastermap color table.
 * 
 * <p>
 * Colortables for GRASS 5 and greater are supported. 
 * </p>
 * 
 * <p>Format of the color file, which is located in 
 * <b>location/mapset/colr/mapname</b>:</p>
 * <p>The first line is a % character and two numbers indicating the minimum 
 * and maximum data values which have colors. <b>Note that in JGrass after the
 * range values we add a third value for alpha support.</b></p>
 * <p>After the first line, the list of color rules appears, that can be 
 * of the following formats:
 * <ul>
 * <li><code>value1:r:g:b value2:r:g:b</code> interpolation of colors between 
 * the two values with the two colors</li> 
 * <li><code>value1:grey value2:grey</code interpolation of grayscale between 
 * the two values with the two grey values></li> 
 * <li><code>value1:r:g:b</code> assumption that it means that value1 == 
 * value2</li> 
 * <li><code>nv:r:g:b</code> novalues could also have color with such a rule.</li>
 * </ul>
 * </p>
 * 
 * @author Andrea Antonello (www.hydrologis.com)
 * @since 3.0
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/unsupported/grassraster/src/main/java/org/geotools/gce/grassraster/core/color/JGrassColorTable.java $
 */
public class JGrassColorTable {

    /**
     * The rainbow color table, used as default for non existing color table.
     */
    private final static int[][] rainbow = new int[][]{{255, 255, 0}, /* yellow */
    {0, 255, 0}, /* green */
    {0, 255, 255}, /* cyan */
    {0, 0, 255}, /* blue */
    {255, 0, 255}, /* magenta */
    {255, 0, 0} /* red */
    };

    // private final static int[][] aspect = new int[][]{{255, 255, 255}, /* white */
    // {0, 0, 0}, /* black */
    // {255, 255, 255} /* white */
    // };

    /**
     * The transparency value.
     */
    private int alpha = 255;

    /**
     * The list of colorrules to be used.
     */
    private List<String> rules = new ArrayList<String>();

    /** 
     * Creates a new instance of ColorTable 
     *
     * @param readerGrassEnv the grass environment used to identify paths.
     * @param dataRange the datarange to be used if the native one is missing.
     */
    public JGrassColorTable( JGrassMapEnvironment readerGrassEnv, double[] dataRange ) throws IOException {

        File colrFile = readerGrassEnv.getCOLR();
        if (!colrFile.exists()) {
            if (dataRange != null) {
                rules = createDefaultColorTable(dataRange, alpha);
            }
            return;
        } else {
            BufferedReader rdr = new BufferedReader(new InputStreamReader(new FileInputStream(colrFile)));
            String line = rdr.readLine();
            if (line == null) {
                rdr.close();
                if (colrFile.delete()) {
                    System.out.println("removed empty color file"); //$NON-NLS-1$
                }
                rules = createDefaultColorTable(dataRange, alpha);
                return;
            }
            line = line.trim();
            if (line.charAt(0) == '%') {
                String[] stringValues = line.split("\\s+"); //$NON-NLS-1$
                if (stringValues.length == 4) {
                    try {
                        alpha = Integer.parseInt(stringValues[3]);
                    } catch (NumberFormatException e) {
                        alpha = 255;
                    }
                } else {
                    alpha = 255;
                }
                /* Read all the color rules */
                while( (line = rdr.readLine()) != null ) {
                    rules.add(line + " " + alpha); //$NON-NLS-1$
                }
            } else {
                while( (line = rdr.readLine()) != null ) {
                    rules.add(line + " " + alpha); //$NON-NLS-1$
                }
            }
            rdr.close();
        }
    }

    /**
     * Creates a default rainbow color table given a data range.
     * 
     * @param dataRange the data range for which the color table is created
     * @return the list of color rules as <code>value1:r:g:b value2:r:g:b alpha</code>
     */
    @SuppressWarnings("nls")
    public static List<String> createDefaultColorTable( double[] dataRange, int alpha ) {
        List<String> rules = new ArrayList<String>();
        // calculate the color increment
        float rinc = (float) (dataRange[1] - dataRange[0]) / 5;
        for( int i = 0; i < 5; i++ ) {
            StringBuffer rule = new StringBuffer();
            rule.append((dataRange[0] + (i * rinc)) + ":");
            rule.append(rainbow[i][0] + ":" + rainbow[i][1] + ":" + rainbow[i][2] + " ");
            rule.append((dataRange[0] + ((i + 1) * rinc)) + ":");
            rule.append(rainbow[i + 1][0] + ":" + rainbow[i + 1][1] + ":" + rainbow[i + 1][2] + " " + alpha);
            rules.add(rule.toString());
        }
        return rules;
    }

    /**
     * Getter for the color rules.
     * 
     * @return the list of color rules.
     */
    public List<String> getColorRules() {
        return rules;
    }

    /**
     * Getter for the alpha value.
     * 
     * @return the alpha value.
     */
    public int getAlpha() {
        return alpha;
    }

    /**
     * parses a color rule.
     * 
     * <p>
     * Arrays of doubles and colors have to be passed, that will
     * be filled with the values of the color rule.
     * </p>
     * 
     * @param rule the color rule as taken from the list returned by 
     * {@link JGrassColorTable#getColorRules()}
     * @param values the array of doubles to be filled with the values.
     * @param colors the array of {@link Color} to be filled with the colors (can be null).
     */
    public static void parseColorRule( String rule, double[] values, Color[] colors ) {
        if (colors == null) {
            colors = new Color[2];
        }

        String[] ruleSplit = rule.split("\\s+"); //$NON-NLS-1$
        if (ruleSplit.length >= 2) {
            String part1 = ruleSplit[0];
            String part2 = ruleSplit[1];
            int alpha = 255;
            if (ruleSplit.length == 3)
                alpha = Integer.parseInt(ruleSplit[2]);

            String[] part1Split = part1.split(":"); //$NON-NLS-1$
            String[] part2Split = part2.split(":"); //$NON-NLS-1$

            if (part1Split.length == 2) {
                // gray scale
                values[0] = Double.parseDouble(part1Split[0]);
                colors[0] = new Color(Integer.parseInt(part1Split[1]), Integer.parseInt(part1Split[1]), Integer.parseInt(part1Split[1]), alpha);
            } else if (part1Split.length == 4) {
                // rgb
                values[0] = Double.parseDouble(part1Split[0]);
                colors[0] = new Color(Integer.parseInt(part1Split[1]), Integer.parseInt(part1Split[2]), Integer.parseInt(part1Split[3]), alpha);
            } else {
                values[0] = Double.NaN;
                colors[0] = new Color(0, 0, 0);
            }

            if (part2Split.length == 2) {
                // gray scale
                values[1] = Double.parseDouble(part2Split[0]);
                colors[1] = new Color(Integer.parseInt(part2Split[1]), Integer.parseInt(part2Split[1]), Integer.parseInt(part2Split[1]), alpha);
            } else if (part2Split.length == 4) {
                // rgb
                values[1] = Double.parseDouble(part2Split[0]);
                colors[1] = new Color(Integer.parseInt(part2Split[1]), Integer.parseInt(part2Split[2]), Integer.parseInt(part2Split[3]), alpha);
            } else {
                values[1] = Double.NaN;
                colors[1] = new Color(255, 255, 255);
            }
        } else if (ruleSplit.length >= 1) {
            String part = ruleSplit[0];
            int alpha = 255;
            if (ruleSplit.length == 2)
                alpha = Integer.parseInt(ruleSplit[1]);

            String[] partSplit = part.split(":"); //$NON-NLS-1$

            if (partSplit.length == 2) {
                // gray scale
                values[0] = Double.parseDouble(partSplit[0]);
                colors[0] = new Color(Integer.parseInt(partSplit[1]), Integer.parseInt(partSplit[1]), Integer.parseInt(partSplit[1]), alpha);
            } else if (partSplit.length == 4) {
                // rgb
                values[0] = Double.parseDouble(partSplit[0]);
                colors[0] = new Color(Integer.parseInt(partSplit[1]), Integer.parseInt(partSplit[2]), Integer.parseInt(partSplit[3]), alpha);
            } else {
                values[0] = Double.NaN;
                colors[0] = new Color(0, 0, 0);
            }
            values[1] = values[0];
            colors[1] = colors[0];
        } else {
            values[0] = -1000;
            colors[0] = new Color(0, 0, 0);
            values[1] = 1000;
            colors[1] = new Color(255, 255, 255);
        }

    }
}
