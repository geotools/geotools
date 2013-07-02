/*
 *    ImageI/O-Ext - OpenSource Java Image translation Library
 *    http://www.geo-solutions.it/
 *    http://java.net/projects/imageio-ext/
 *    (C) 2007 - 2009, GeoSolutions
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    either version 3 of the License, or (at your option) any later version.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.coverage.io.hdf4.aps;

/**
 * @author Alessio Fabiani, GeoSolutions S.A.S.
 * 
 */
public class ColorRampGenerator {

    private static double[] getColour(String colorRampType, double v, double vmin, double vmax) {
        double[] c = new double[] { 1.0, 1.0, 1.0 }; // white
        
        if (colorRampType.equals("hot-color-ramp")) {
            c = getHotRampColor(v, vmin, vmax);
        }
        
        return c;
    }
    /**
     * Return a RGB colour value given a scalar v in the range [vmin,vmax] In
     * this case each colour component ranges from 0 (no contribution) to 1
     * (fully saturated), modifications for other ranges is trivial. The colour
     * is clipped at the end of the scales if v is outside the range [vmin,vmax]
     */
    private static double[] getHotRampColor(double v, double vmin, double vmax) {
        double[] c = new double[] { 1.0, 1.0, 1.0 }; // white
        double dv;

        if (v < vmin)
            v = vmin;
        if (v > vmax)
            v = vmax;
        dv = vmax - vmin;

        if (v < (vmin + 0.25 * dv)) {
            c[0] = 0;
            c[1] = 4 * (v - vmin) / dv;
        } else if (v < (vmin + 0.5 * dv)) {
            c[0] = 0;
            c[2] = 1 + 4 * (vmin + 0.25 * dv - v) / dv;
        } else if (v < (vmin + 0.75 * dv)) {
            c[0] = 4 * (v - vmin - 0.5 * dv) / dv;
            c[2] = 0;
        } else {
            c[1] = 1 + 4 * (vmin + 0.75 * dv - v) / dv;
            c[2] = 0;
        }

        return c;
    }
    
    public static void main(String[] args) {
        double min = 0.0001;
        double max = 32;
        
        final boolean useLogarithm = false;
        final double logMin = Math.log10(min);
        final double logMax = Math.log10(max);

        int intervals = 250;

        String colorRampType = "hot-color-ramp";

        double res = (max - min) / intervals;
        final double logRes = (logMax-logMin)/intervals;
        final double base10 = 10;
        final double base10Res = Math.pow(base10, logRes);
        
        final double usedRes = useLogarithm? base10Res : res;
        
        System.out.println("<ColorMapEntry color=\"#000000\" quantity=\"" + (min - usedRes) + "\" opacity=\"0.0\"/>");
        for (int c = 0; c <= intervals; c++) {
            double[] color = getColour(colorRampType, min + (c * res), min, max);
            final double step = c!=0? Math.pow(base10, logMin + c * logRes):0;
            final double usedStep = useLogarithm? step: c*res;
            String r = Integer.toHexString((int) Math.round(255.0 * color[0]));
            String g = Integer.toHexString((int) Math.round(255.0 * color[1]));
            String b = Integer.toHexString((int) Math.round(255.0 * color[2]));
            String hexColor = 
                (r.length() == 2 ? r : "0" + r) + 
                (g.length() == 2 ? g : "0" + g) + 
                (b.length() == 2 ? b : "0" + b);
            System.out.println("<ColorMapEntry color=\"#" + hexColor + "\" quantity=\"" + (min + usedStep) + "\"/>");
        }
        System.out.println("<ColorMapEntry color=\"#000000\" quantity=\"" + (max + usedRes) + "\" opacity=\"0.0\"/>");
    }
    
    
    
    
}