/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2001-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage;

import java.awt.color.ColorSpace;
import org.geotools.resources.Classes;


/**
 * Color space for raster backed by floating point numbers ranging between two arbitrary values.
 *
 * <strong>NOTE:</strong>
 *       Current implementation is a copy of {@code org.geotools.image.io.ScaledColorSpace}.
 *       Future implementation will be differents (interpolate in a color table instead of
 *       computing grayscales).
 *
 * @since 2.1
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 *
 * @todo Consider extending {@link javax.media.jai.ColorSpaceJAI}.
 */
final class ScaledColorSpace extends ColorSpace {
    /**
     * Minimal normalized RGB value.
     */
    private static final float MIN_VALUE = 0f;
    
    /**
     * Maximal normalized RGB value.
     */
    private static final float MAX_VALUE = 1f;

    /**
     * The band to make visible (usually 0).
     */
    private final int band;
    
    /**
     * Facteur par lequel multiplier les pixels.
     */
    private final float scale;
    
    /**
     * Nombre à aditionner aux pixels après
     * les avoir multiplier par {@link #scale}.
     */
    private final float offset;
    
    /**
     * Construit un modèle de couleurs.
     *
     * @param band La bande à rendre visible (habituellement 0).
     * @param numComponents Nombre de composante (seule la première sera prise en compte).
     * @param minimum La valeur géophysique minimale.
     * @param maximum La valeur géophysique maximale.
     */
    public ScaledColorSpace(final int band, final int numComponents,
                            final double minimum, final double maximum)
    {
        super(TYPE_GRAY, numComponents);
        this.band = band;
        final double scale  = (maximum-minimum)/(MAX_VALUE-MIN_VALUE);
        final double offset = minimum - MIN_VALUE*scale;
        this.scale  = (float)scale;
        this.offset = (float)offset;
    }
    
    /**
     * Retourne une couleur RGB en tons de
     * gris pour le nombre réel spécifié.
     */
    public float[] toRGB(final float[] values) {
        float value = (values[band]-offset)/scale;
        if (Float.isNaN(value)) value=MIN_VALUE;
        return new float[] {value, value, value};
    }
    
    /**
     * Retourne une valeur réelle pour
     * le ton de gris spécifié.
     */
    public float[] fromRGB(final float[] RGB) {
        final float[] values = new float[getNumComponents()];
        values[band] = (RGB[0]+RGB[1]+RGB[2])/3*scale + offset;
        return values;
    }
    
    /**
     * Convertit les valeurs en couleurs dans l'espace CIEXYZ.
     */
    public float[] toCIEXYZ(final float[] values) {
        float value = (values[band]-offset)/scale;
        if (Float.isNaN(value)) value=MIN_VALUE;
        return new float[] {
            value*0.9642f,
            value*1.0000f,
            value*0.8249f
        };
    }
    
    /**
     * Convertit les couleurs de l'espace CIEXYZ en valeurs.
     */
    public float[] fromCIEXYZ(final float[] RGB) {
        final float[] values = new float[getNumComponents()];
        values[band] = (RGB[0]/0.9642f + RGB[1] + RGB[2]/0.8249f)/3*scale + offset;
        return values;
    }
    
    /**
     * Retourne la valeur minimale autorisée.
     */
    @Override
    public float getMinValue(final int component) {
        return MIN_VALUE*scale + offset;
    }
    
    /**
     * Retourne la valeur maximale autorisée.
     */
    @Override
    public float getMaxValue(final int component) {
        return MAX_VALUE*scale + offset;
    }
    
    /**
     * Returns a string representation of this color model.
     */
    @Override
    public String toString() {
        return Classes.getShortClassName(this) + '[' + getMinValue(band) + ", " + getMaxValue(band) + ']';
    }
}
