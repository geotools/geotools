/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.brewer.color;

import java.awt.Color;


/**
 * A generic palette containing colours.
 * @author Cory Horner, Refractions Research Inc.
 *
 * @source $URL$
 */
public class ColorPalette {
    private Color[] colors = new Color[0]; //15

    /**
     * Longer description of the palette
     */
    private String description;

    /**
     * Very short name describing the palette
     */
    private String name;
    private int numColors = 0;

    /**
     * Returns the number of colours contained in the palette.
     * @return int
     */
    public int getCount() {
        return numColors;
    }

    /**
     * Getter for property name.
     *
     * @return Value of property name.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Setter for property name.
     *
     * @param name New value of property name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter for property description.
     *
     * @return Value of property description.
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Setter for property description.
     *
     * @param description New value of property description.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    public void setColors(Color[] colors) {
        this.colors = colors;

        int count = 0;

        for (int i = 0; i < colors.length; i++)
            if (colors[i] != null) {
                count++;
            }

        this.numColors = count;
    }

    public Color[] getColors(int length) {
        if (length < 2) {
            length = 2; //if they ask for 1 colour, give them 2 instead of crashing
        }

        //int[] lookup = sampler.getSampleScheme(length);
        Color[] result = new Color[length];

        for (int i = 0; i < length; i++) {
            result[i] = colors[i];
        }

        return result;
    }

    /**
     * Returns all colours
     *
     * @return complete colour array
     */
    public Color[] getColors() {
        return colors;
    }
}
