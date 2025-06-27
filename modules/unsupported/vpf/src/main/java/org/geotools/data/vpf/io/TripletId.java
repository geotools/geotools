/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2003-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.vpf.io;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.data.vpf.VPFLogger;
import org.geotools.data.vpf.exc.VPFDataFormatException;
import org.geotools.util.logging.Logging;

/**
 * Class TripletId.java is responsible for
 *
 * @author <a href="mailto:kobit@users.sourceforge.net">Artur Hefczyc</a>
 * @author <a href="mailto:knuterik@onemap.org">Knut-Erik Johnsen</a>, Project OneMap
 * @version 1.0.0
 * @author <a href="mailto:jeff@ionicenterprise.com">Jeff Yutzler</a>
 * @source $URL$
 */
public class TripletId extends Number {
    /** serialVersionUID */
    private static final long serialVersionUID = -3584133713173893007L;
    /** The raw data that can be decomposed into as many as three separate numbers */
    private byte[] rawData = null;

    static final Logger LOGGER = Logging.getLogger(TripletId.class);
    /**
     * Creates a new <code>TripletId</code> instance.
     *
     * @param data a <code>byte[]</code> value
     */
    public TripletId(byte[] data) {
        rawData = data;
    }

    /*
     *  (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        String result = "";

        try {
            if (getIdLength() > 0) {
                result = String.valueOf(getId());
            }

            if (getTileIdLength() > 0) {
                result = result.concat("%").concat(String.valueOf(getTileId())).trim();
            }

            if (getNextIdLength() > 0) {
                result = result.concat("%").concat(String.valueOf(getNextId())).trim();
            }
        } catch (RuntimeException exp) {
            throw new VPFDataFormatException("This triplet is invalid.", exp);
        }

        return result;
    }

    /**
     * Returns the length in bytes of the ID
     *
     * @return an <code>int</code> value
     */
    private int getIdLength() {
        return rawData[0] >> 6 & 3;
    }

    /**
     * Returns the length in bytes of the Tile ID
     *
     * @return an <code>int</code> value
     */
    private int getTileIdLength() {
        return rawData[0] >> 4 & 3;
    }

    /**
     * Returns the length in bytes of the Next ID
     *
     * @return an <code>int</code> value
     */
    private int getNextIdLength() {
        return rawData[0] >> 2 & 3;
    }

    /**
     * Returns the ID value
     *
     * @return Returns the ID, the first number of the triplet
     */
    public int getId() {
        int result = 0;
        int length = getIdLength();
        int piece;

        if (length > 0) {
            try {
                for (int inx = 0; inx < length; inx++) {
                    piece = rawData[inx + 1];

                    // Convert bytes from signed to unsigned
                    if (piece < 0) {
                        piece += -2 * Byte.MIN_VALUE;
                    }

                    result += piece << 8 * inx;
                }
            } catch (RuntimeException exp) {
                LOGGER.log(Level.SEVERE, "", exp);
                result = 0;
            }
        }

        return result;
    }

    /**
     * Returns the Tile ID
     *
     * @return Returns the Tile ID, the second number of the triplet
     */
    public int getTileId() {
        int result = 0;
        int length = getTileIdLength();
        int piece;

        if (length > 0) {
            int rowIdLength = getIdLength();

            try {
                for (int inx = 0; inx < length; inx++) {
                    piece = rawData[inx + rowIdLength + 1];

                    if (piece < 0) {
                        piece += 2 * Byte.MAX_VALUE;
                    }

                    result += piece << 8 * inx;
                }
            } catch (RuntimeException exp) {
                LOGGER.log(Level.SEVERE, "", exp);
                result = 0;
            }
        }

        return result;
    }

    /**
     * Returns the Next ID
     *
     * @return Returns the Next ID, the third number of the triplet
     */
    public int getNextId() {
        int result = 0;
        int length = getTileIdLength();
        int piece;

        if (length > 0) {
            int prevLength = getIdLength() + getTileIdLength();

            try {
                for (int inx = 0; inx < length; inx++) {
                    piece = rawData[inx + prevLength + 1];

                    if (piece < 0) {
                        piece += 2 * Byte.MAX_VALUE;
                    }

                    result += piece << 8 * inx;
                }
            } catch (RuntimeException exp) {
                LOGGER.log(Level.SEVERE, "", exp);
                result = 0;
            }
        }

        return result;
    }

    /**
     * Describe <code>calculateDataSize</code> method here.
     *
     * @param definition a <code>byte</code> value indicating the details of the bytes
     * @return an <code>int</code> value
     */
    public static int calculateDataSize(byte definition) {
        int[] pieces = new int[3];
        pieces[0] = definition >> 2 & 3;
        pieces[1] = definition >> 4 & 3;
        pieces[2] = definition >> 6 & 3;

        int size = 0;

        for (int piece : pieces) {
            switch (piece) {
                case 0:
                    break;

                case 1:
                    size++;

                    break;

                case 2:
                    size += 2;

                    break;

                case 3:
                    size += 4;

                    break;

                default:
                    VPFLogger.log("Tripled id size decoding error");
                    VPFLogger.log("tripled definition: " + definition);
                    VPFLogger.log("piece 0: " + pieces[0]);
                    VPFLogger.log("piece 1: " + pieces[1]);
                    VPFLogger.log("piece 2: " + pieces[2]);

                    break;
            }
        }

        return size;
    }

    /* (non-Javadoc)
     * @see java.lang.Number#doubleValue()
     */
    @Override
    public double doubleValue() {
        return getId();
    }

    /* (non-Javadoc)
     * @see java.lang.Number#floatValue()
     */
    @Override
    public float floatValue() {
        return getId();
    }

    /* (non-Javadoc)
     * @see java.lang.Number#intValue()
     */
    @Override
    public int intValue() {
        return getId();
    }

    /* (non-Javadoc)
     * @see java.lang.Number#longValue()
     */
    @Override
    public long longValue() {
        return getId();
    }

    /* (non-Javadoc)
     * @see java.lang.Number#byteValue()
     */
    @Override
    public byte byteValue() {
        return (byte) getId();
    }

    /* (non-Javadoc)
     * @see java.lang.Number#shortValue()
     */
    @Override
    public short shortValue() {
        return (short) getId();
    }
}
