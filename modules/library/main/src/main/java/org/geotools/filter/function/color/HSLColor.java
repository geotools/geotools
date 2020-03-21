/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2017, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.filter.function.color;

import java.awt.Color;

/**
 * Support class for color functions, represents a color in HSL space (based on
 * http://www.niwa.nu/2013/05/math-behind-colorspace-conversions-rgb-hsl/)
 *
 * @author Andrea Aime - GeoSolutions
 */
public class HSLColor {

    double hue;

    double saturation;

    double lightness;

    double alpha;

    /** Builds a color based on HSLA components */
    public HSLColor(double hue, double saturation, double lightness) {
        this(hue, saturation, lightness, 1d);
    }

    /** Builds a color based on HSLA components */
    public HSLColor(double hue, double saturation, double lightness, double alpha) {
        this.hue = hue;
        this.saturation = saturation;
        this.lightness = lightness;
        this.alpha = alpha;
    }

    /** Builds the HSL representation of a color */
    public HSLColor(Color color) {
        double r = color.getRed() / 255d;
        double g = color.getGreen() / 255d;
        double b = color.getBlue() / 255d;
        alpha = color.getAlpha() / 255d;

        // min and max rgb
        double min = Math.min(r, Math.min(g, b));
        double max = Math.max(r, Math.max(g, b));

        // luminance
        lightness = (max + min) / 2;

        // saturation
        saturation = 0;
        if (max == min) {
            saturation = 0;
        } else if (lightness <= .5f) {
            saturation = (max - min) / (max + min);
        } else {
            saturation = (max - min) / (2 - max - min);
        }

        // hue
        hue = 0;
        if (max == min) {
            hue = 0;
        } else if (max == r) {
            hue = ((60 * (g - b) / (max - min)) + 360) % 360;
        } else if (max == g) {
            hue = (60 * (b - r) / (max - min)) + 120;
        } else if (max == b) {
            hue = (60 * (r - g) / (max - min)) + 240;
        }
    }

    public void setHue(double hue) {
        this.hue = hue;
    }

    public void setSaturation(double saturation) {
        if (saturation < 0) {
            this.saturation = 0;
        } else if (saturation > 1) {
            this.saturation = 1;
        } else {
            this.saturation = saturation;
        }
    }

    public void setLightness(double lightness) {
        this.lightness = lightness;
    }

    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }

    public double getHue() {
        return hue;
    }

    public double getSaturation() {
        return saturation;
    }

    public double getLightness() {
        return lightness;
    }

    public double getAlpha() {
        return alpha;
    }

    /** Turns the HSL representation into a RGB one */
    public Color toRGB() {
        double q = 0;
        if (lightness < 0.5) {
            q = lightness * (1 + saturation);
        } else {
            q = (lightness + saturation) - (saturation * lightness);
        }

        double p = 2 * lightness - q;

        double r = Math.max(0, hueToRGB(p, q, hue / 360 + (1f / 3f)));
        double g = Math.max(0, hueToRGB(p, q, hue / 360));
        double b = Math.max(0, hueToRGB(p, q, hue / 360 - (1f / 3f)));

        r = Math.min(r, 1.0f);
        g = Math.min(g, 1.0f);
        b = Math.min(b, 1.0f);

        return new Color(
                (int) Math.round(r * 255),
                (int) Math.round(g * 255),
                (int) Math.round(b * 255),
                (int) Math.round(alpha * 255));
    }

    private double hueToRGB(double p, double q, double h) {
        if (h < 0) {
            h += 1;
        } else if (h > 1) {
            h -= 1;
        }

        if (6 * h < 1) {
            return p + ((q - p) * 6 * h);
        }

        if (2 * h < 1) {
            return q;
        }

        if (3 * h < 2) {
            return p + ((q - p) * 6 * ((2.0f / 3.0f) - h));
        }

        return p;
    }

    @Override
    public String toString() {
        return "HSLColor [" + hue + "," + saturation + "," + lightness + "," + alpha + "]";
    }
}
