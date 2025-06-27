/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2021, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.ows.wmts.client;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.logging.Logger;
import org.geotools.util.logging.Logging;

/**
 * Convenience class that creates a URL from a templateURL by exchanging {TileMatrix}, {TileCol} and {TileRow}
 *
 * @author Roar Brænden
 */
class TileURLBuilder {

    private static Logger LOGGER = Logging.getLogger(TileURLBuilder.class);

    private static final String TILEMATRIX = "{tilematrix}";
    private static final String TILECOL = "{tilecol}";
    private static final String TILEROW = "{tilerow}";

    private static final int MATRIX = 0;
    private static final int COL = 1;
    private static final int ROW = 2;

    private int[] indexes = new int[3];

    private ArrayList<Part> parts;
    private int start = 0;
    private String templateURL;

    private int urlLength = 50;

    TileURLBuilder(String templateURL) {
        if (templateURL == null) {
            throw new IllegalArgumentException("templateURL cannot be null");
        }
        final String lowerTemplate = templateURL.toLowerCase();
        indexes[MATRIX] = lowerTemplate.indexOf(TILEMATRIX);
        indexes[COL] = lowerTemplate.indexOf(TILECOL);
        indexes[ROW] = lowerTemplate.indexOf(TILEROW);

        parts = new ArrayList<>(7);
        this.templateURL = templateURL;
        if (indexes[MATRIX] < indexes[COL] && indexes[MATRIX] < indexes[ROW]) {
            addMatrixParts();
            if (indexes[COL] < indexes[ROW]) {
                addColParts();
                addRowParts();
            } else {
                addRowParts();
                addColParts();
            }
        } else if (indexes[COL] < indexes[ROW]) {
            addColParts();

            if (indexes[MATRIX] < indexes[ROW]) {
                addMatrixParts();
                addRowParts();
            } else {
                addRowParts();
                addMatrixParts();
            }
        } else {
            addRowParts();
            if (indexes[MATRIX] < indexes[COL]) {
                addMatrixParts();
                addColParts();
            } else {
                addColParts();
                addMatrixParts();
            }
        }

        addStringPart(templateURL.length(), 0);
    }

    /**
     * Create a Url with the given set of MatrixSet, TileCol and TileRow
     *
     * @param tileCol
     * @param tileRow
     * @param tileMatrix
     * @return
     */
    String createURL(String tileMatrix, int tileCol, int tileRow) {
        final StringBuilder builder = new StringBuilder(urlLength);
        try {
            String encodedtileMatrix = URLEncoder.encode(tileMatrix, "UTF-8");
            parts.forEach(part -> part.append(builder, encodedtileMatrix, tileCol, tileRow));
            final String url = builder.toString();
            urlLength = Math.max(urlLength, url.length());
            return url;
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Didn't support UTF-8", e);
        }
    }

    private void addColParts() {
        if (indexes[COL] == -1) {
            LOGGER.info("WMTSTileService templateURL doesn't contain {TileCol}");
            return;
        }
        addStringPart(indexes[COL], TILECOL.length());
        parts.add((builder, matrix, col, row) -> builder.append(col));
    }

    private void addRowParts() {
        if (indexes[ROW] == -1) {
            LOGGER.info("WMTSTileService templateURL doesn't contain {TileRow}");
            return;
        }
        addStringPart(indexes[ROW], TILEROW.length());
        parts.add((builder, matrix, col, row) -> builder.append(row));
    }

    private void addMatrixParts() {
        if (indexes[MATRIX] == -1) {
            LOGGER.info("WMTSTileService templateURL doesn't contain {TileMatrix}");
            return;
        }
        addStringPart(indexes[MATRIX], TILEMATRIX.length());
        parts.add((builder, matrix, col, row) -> builder.append(matrix));
    }

    private void addStringPart(int end, int length) {
        if (end < start) {
            throw new IllegalArgumentException("end can't be smaller than start.");
        }
        if (start < end) {
            final String part = templateURL.substring(start, end);
            parts.add((builder, matrix, col, row) -> builder.append(part));
            start = end + length;
        }
    }

    static interface Part {
        void append(StringBuilder builder, String tileMatrix, int tileCol, int tileRow);
    }
}
