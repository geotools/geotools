/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gml.producer;

import org.locationtech.jts.geom.CoordinateSequence;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

// import org.geotools.feature.*;

/**
 * Handles the writing of coordinates for GML.
 *
 * @author Chris Holmes
 * @author Ian Schneider
 */
public class CoordinateWriter {

    /** Internal representation of coordinate delimiter (',' for GML is default) */
    private final String coordinateDelimiter;

    /** Internal representation of tuple delimiter (' ' for GML is default) */
    private final String tupleDelimiter;

    private final AttributesImpl atts = new org.xml.sax.helpers.AttributesImpl();

    private final StringBuffer coordBuff = new StringBuffer();

    private final CoordinateFormatter coordFormatter;

    private char[] buff = new char[200];

    /** True of dummyZ should be used. */
    private final boolean useDummyZ;

    /** Dummy Z value (used to override coordinate.Z value) */
    private final double dummyZ;

    /** Dimension of expected coordinates */
    private final int D;

    /** Flag controlling wether namespaces should be ignored. */
    private boolean namespaceAware = true;
    /** Namepsace prefix + uri, default to gml */
    private String prefix = "gml";

    private String namespaceUri = GMLUtils.GML_URL;

    public CoordinateWriter() {
        this(4, false, true);
    }

    public CoordinateWriter(int numDecimals) {
        this(numDecimals, false, true);
    }

    public CoordinateWriter(int numDecimals, boolean isDummyZEnabled) {
        this(numDecimals, false, true, isDummyZEnabled);
    }

    public CoordinateWriter(int numDecimals, String tupleDelim, String coordDelim) {
        this(numDecimals, false, true, tupleDelim, coordDelim);
    }

    public CoordinateWriter(
            int numDecimals, boolean padWithZeros, boolean forceDecimalEncoding, boolean isDummyZEnabled) {
        this(numDecimals, padWithZeros, forceDecimalEncoding, " ", ",", isDummyZEnabled);
    }

    public CoordinateWriter(int numDecimals, String tupleDelim, String coordDelim, boolean useDummyZ) {
        this(numDecimals, false, true, tupleDelim, coordDelim, useDummyZ);
    }

    public CoordinateWriter(int numDecimals, boolean padWithZeros, boolean forceDecimalEncoding) {
        this(numDecimals, padWithZeros, forceDecimalEncoding, false);
    }

    public CoordinateWriter(int numDecimals, String tupleDelim, String coordDelim, boolean useDummyZ, double zValue) {
        this(numDecimals, false, true, tupleDelim, coordDelim, useDummyZ, zValue);
    }

    public CoordinateWriter(int numDecimals, boolean useDummyZ, int dimension) {
        this(numDecimals, false, true, useDummyZ, dimension);
    }

    // TODO: check gml spec - can it be strings?  Or just chars?
    public CoordinateWriter(
            int numDecimals, boolean padWithZeros, boolean forceDecimalEncoding, String tupleDelim, String coordDelim) {
        this(numDecimals, padWithZeros, forceDecimalEncoding, tupleDelim, coordDelim, false);
    }

    public CoordinateWriter(
            int numDecimals, String tupleDelim, String coordDelim, boolean useZ, double z, int dimension) {
        this(numDecimals, false, true, tupleDelim, coordDelim, useZ, z, dimension);
    }

    public CoordinateWriter(
            int numDecimals,
            boolean padWithZeros,
            boolean forceDecimalEncoding,
            String tupleDelim,
            String coordDelim,
            boolean useDummyZ) {
        this(numDecimals, padWithZeros, forceDecimalEncoding, tupleDelim, coordDelim, useDummyZ, 0);
    }

    public CoordinateWriter(
            int numDecimals,
            boolean padWithZeros,
            boolean forceDecimalEncoding,
            String tupleDelim,
            String coordDelim,
            boolean useDummyZ,
            double zValue) {
        this(numDecimals, padWithZeros, forceDecimalEncoding, tupleDelim, coordDelim, useDummyZ, 0, 2);
    }

    public CoordinateWriter(
            int numDecimals, boolean padWithZeros, boolean forceDecimalEncoding, boolean useDummyZ, int dimension) {
        this(numDecimals, padWithZeros, forceDecimalEncoding, " ", ",", useDummyZ, 0, dimension);
    }

    /**
     * Create a CoordinateWriter for outputting GML coordinates.
     *
     * <p>The use of dimension, z and useZ is based on your needs:
     *
     * <ul>
     *   <li>dimension: is from your CoordinateReferenceSystem; it is the number of axis used by the coordinate
     *   <li>useZ: is used to force the use of 3 dimensions (if needed the z value below will be used for 2D data)
     *   <li>z: the dummy z value to use if the coordinate does not have one
     * </ul>
     *
     * @param numDecimals Number of decimals to use (a speed vs accuracy trade off)
     * @param tupleDelim delimiter to use between ordinates (usually ',')
     * @param coordDelim delimiter to use between coordinates (usually ' ')
     * @param useZ true if the provided zValue should be forced
     * @param z Dummy z value to use if needed
     * @param dimension Dimension of coordinates (usually 2 or 3)
     */
    public CoordinateWriter(
            int numDecimals,
            boolean padWithZeros,
            boolean forceDecimalEncoding,
            String tupleDelim,
            String coordDelim,
            boolean useZ,
            double z,
            int dimension) {
        if (tupleDelim == null || tupleDelim.length() == 0) {
            throw new IllegalArgumentException("Tuple delimeter cannot be null or zero length");
        }
        if (coordDelim != null && coordDelim.length() == 0) {
            throw new IllegalArgumentException("Coordinate delimeter cannot be null or zero length");
        }
        D = dimension;

        tupleDelimiter = tupleDelim;
        coordinateDelimiter = coordDelim;

        coordFormatter = new CoordinateFormatter(numDecimals);
        coordFormatter.setForcedDecimal(forceDecimalEncoding);
        coordFormatter.setPadWithZeros(padWithZeros);

        String uri = namespaceUri;
        if (!namespaceAware) {
            uri = null;
        }

        atts.addAttribute(uri, "decimal", "decimal", "decimal", ".");
        atts.addAttribute(uri, "cs", "cs", "cs", coordinateDelimiter);
        atts.addAttribute(uri, "ts", "ts", "ts", tupleDelimiter);

        this.useDummyZ = useZ;
        this.dummyZ = z;
    }

    public int getNumDecimals() {
        return coordFormatter.getMaximumFractionDigits();
    }

    public boolean getPadWithZeros() {
        return coordFormatter.isPadWithZeros();
    }

    public boolean getForceDecimalEncoding() {
        return coordFormatter.isForcedDecimal();
    }

    public boolean isDummyZEnabled() {
        return useDummyZ;
    }

    public void setNamespaceAware(boolean namespaceAware) {
        this.namespaceAware = namespaceAware;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public void setNamespaceUri(String namespaceUri) {
        this.namespaceUri = namespaceUri;
    }

    /**
     * Write the provided list of coordinates out.
     *
     * <p>There are a range of constants that control exactly what is written:
     *
     * <ul>
     *   <li>useDummyZ: if true dummyZ will be added to each coordiante
     *   <li>namespaceAware: is true the prefix and namespaceUri will be used
     *   <li>
     * </ul>
     */
    public void writeCoordinates(CoordinateSequence c, ContentHandler output) throws SAXException {

        String prefix = this.prefix + ":";
        String namespaceUri = this.namespaceUri;

        if (!namespaceAware) {
            prefix = "";
            namespaceUri = null;
        }

        output.startElement(namespaceUri, "coordinates", prefix + "coordinates", atts);

        final int coordCount = c.size();
        // used to check whether the coordseq handles a third dimension or not
        final int coordSeqDimension = c.getDimension();
        double x, y, z;
        // write down a coordinate at a time
        for (int i = 0, n = coordCount; i < n; i++) {
            x = c.getOrdinate(i, 0);
            y = c.getOrdinate(i, 1);

            // clear the buffer
            coordBuff.setLength(0);

            // format x into buffer and append delimiter
            coordFormatter.format(x, coordBuff);
            coordBuff.append(coordinateDelimiter);
            // format y into buffer
            coordFormatter.format(y, coordBuff);

            boolean zAvailable = coordSeqDimension > 2
                    && !Double.isNaN(c.getOrdinate(i, 2))
                    && !Double.isInfinite(c.getOrdinate(i, 2));
            if (D == 3 || zAvailable || useDummyZ) {
                z = zAvailable ? c.getOrdinate(i, 2) : dummyZ;
                coordBuff.append(coordinateDelimiter);
                coordFormatter.format(z, coordBuff);
            }

            // if there is another coordinate, tack on a tuple delimiter
            if (i + 1 < coordCount) {
                coordBuff.append(tupleDelimiter);
            }

            // make sure our character buffer is big enough
            if (coordBuff.length() > buff.length) {
                buff = new char[coordBuff.length()];
            }
            // copy the characters
            coordBuff.getChars(0, coordBuff.length(), buff, 0);

            // finally, output
            output.characters(buff, 0, coordBuff.length());
        }
        output.endElement(namespaceUri, "coordinates", prefix + "coordinates");
    }
}
