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

import java.io.IOException;


/**
 * Contains the suitability information for a single palette with several colour schemes.
 *
 * @author Cory Horner, Refractions Research Inc.
 * @source $URL$
 */
public class PaletteSuitability {
    /** Suitability = GOOD */
    public static final int QUALITY_GOOD = 3;

    /** Suitability = UNKNOWN */
    public static final int QUALITY_UNKNOWN = 2;

    /** Suitability = DOUBTFUL */
    public static final int QUALITY_DOUBTFUL = 1;

    /** Suitability = BAD */
    public static final int QUALITY_BAD = 0;

    /** ViewerType = Suitable for the colorblind? */
    public static final int VIEWER_COLORBLIND = 0;

    /** ViewerType = Suitable for photocopiers? */
    public static final int VIEWER_PHOTOCOPY = 1;

    /** ViewerType = Suitable for overhead projectors (lcd)? */
    public static final int VIEWER_PROJECTOR = 2;

    /** ViewerType = Suitable for LCD monitors? */
    public static final int VIEWER_LCD = 3;

    /** ViewerType = Suitable for CRT monitors? */
    public static final int VIEWER_CRT = 4;

    /** ViewerType = Suitable for colour printing? */
    public static final int VIEWER_PRINT = 5;

    /**
     * Contains the suitability data for this palette.  First index is the
     * number of colors - 2.  Second index is the viewer type. Values are the
     * suitability value.
     *
     * <p>
     * Viewer Types: PaletteSuitability.COLORBLIND, PHOTOCOPY, PROJECTOR, LCD,
     * CRT, or PRINT
     * </p>
     *
     * <p>
     * Suitability: PaletteSuitability.GOOD, UNKNOWN, DOUBTFUL, or BAD
     * </p>
     */
    private int[][] paletteSuitability = new int[11][6];

    /**
     * The maximum number of colors this palette can support (minimum is
     * assumed to be 2).
     */
    private int maxColors = 0;

    public PaletteSuitability() {
    }

    /**
     * Indexed getter for property paletteSuitability. For this palette, this
     * returns an array containing the integer values for all 6 suitabilities.
     *
     * @param numClasses
     *            The number of colors to determine the suitability for
     *
     * @return int array; index = PaletteSuitability.VIEWER_COLORBLIND,
     *         VIEWER_PHOTOCOPY, VIEWER_PROJECTOR, VIEWER_LCD, VIEWER_CRT, or
     *         VIEWER_PRINT; values = PaletteSuitability.QUALITY_GOOD,
     *         QUALITY_UNKNOWN, QUALITY_DOUBTFUL, or QUALITY_BAD.
     */
    public int[] getSuitability(int numClasses) {
        return paletteSuitability[numClasses - 2];
    }

    /**
     * Indexed getter for the property paletteSuitability. For the selected
     * palette and viewerType, this returns the integer value of the
     *
     * @param numClasses
     *            number of colours in this palette
     * @param viewerType
     *            PaletteSuitability.VIEWER_COLORBLIND, VIEWER_PHOTOCOPY,
     *            VIEWER_PROJECTOR, VIEWER_LCD, VIEWER_CRT, or VIEWER_PRINT.
     *
     * @return PaletteSuitability.QUALITY_GOOD, QUALITY_UNKNOWN,
     *         QUALITY_DOUBTFUL, or QUALITY_BAD.
     */
    public int getSuitability(int numClasses, int viewerType) {
        return paletteSuitability[numClasses - 2][viewerType];
    }

    /**
     *
     *
     * @param numClasses Index of the property.
     * @param suitability New value of the property at<CODE>index</CODE>.
     *
     * @throws IOException
     */
    public void setSuitability(int numClasses, String[] suitability)
        throws IOException {
        //update max number of classes
        if (numClasses > maxColors) {
            maxColors = numClasses;
        }

        //convert G,D,B,? --> int
        if (suitability.length == 6) {
            for (int i = 0; i < 6; i++) {
                if (suitability[i].equals("G")) {
                    paletteSuitability[numClasses - 2][i] = QUALITY_GOOD;
                } else if (suitability[i].equals("D")) {
                    paletteSuitability[numClasses - 2][i] = QUALITY_DOUBTFUL;
                } else if (suitability[i].equals("B")) {
                    paletteSuitability[numClasses - 2][i] = QUALITY_BAD;
                } else {
                    paletteSuitability[numClasses - 2][i] = QUALITY_UNKNOWN;
                }
            }
        } else {
            throw new IOException("wrong number of items in suitability list");
        }
    }

    public int getMaxColors() {
        return maxColors;
    }
}
