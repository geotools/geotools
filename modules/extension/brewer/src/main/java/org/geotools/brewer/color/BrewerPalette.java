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
 * A ColorPalette with additional ColorBrewer information (suitability data and colour selection).
 *
 * @author James Macgill
 * @author Cory Horner, Refractions Research Inc.
 * @source $URL$
 */
public class BrewerPalette extends ColorPalette {
    private PaletteSuitability suitability;
    private SampleScheme sampler;
    private PaletteType type;

    /**
     * Creates a new instance of BrewerPalette
     */
    public BrewerPalette() {
    }

    /**
     * Getter for property type.
     *
     * @return Value of property type.
     */
    public PaletteType getType() {
        return this.type;
    }

    /**
     * Sets the type of palette.
     *
     * @param type new palette type
     */
    public void setType(PaletteType type) {
        this.type = type;
    }

    public Color getColor(int index, int length) {
        return getColors(length)[index];
    }

    /**
     * Getter for the colour count
     *
     * @return the most colours this palette currently supports
     */
    public int getMaxColors() {
        int countSampler = sampler.getMaxCount();
        int numColors = getCount();

        //return the lesser of countSampler and numColors
        if (countSampler < numColors) {
            return countSampler;
        } else {
            return numColors;
        }
    }

    /**
     * Getter for the colour count
     *
     * @return the minimum number of colours this palette currently supports
     */
    public int getMinColors() {
        return sampler.getMinCount();
    }

    /**
     * Obtains a set of colours from the palette.
     */
    public Color[] getColors(int length) {
        if (length < 2) {
            length = 2; //if they ask for 1 colour, give them 2 instead of crashing
        }

        int[] lookup = sampler.getSampleScheme(length);
        Color[] colors = getColors();
        Color[] result = new Color[length];

        for (int i = 0; i < length; i++) {
            result[i] = colors[lookup[i]];
        }

        return result;
    }

    public PaletteSuitability getPaletteSuitability() {
        return suitability;
    }

    public void setPaletteSuitability(PaletteSuitability suitability) {
        this.suitability = suitability;
    }

    public SampleScheme getColorScheme() {
        return sampler;
    }

    public void setColorScheme(SampleScheme scheme) {
        this.sampler = scheme;
    }
}
