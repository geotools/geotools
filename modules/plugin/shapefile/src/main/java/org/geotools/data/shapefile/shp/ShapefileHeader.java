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
package org.geotools.data.shapefile.shp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;

/**
 * 
 * @author jamesm
 * @author Ian Schneider
 *
 * @source $URL$
 *         http://svn.geotools.org/geotools/trunk/gt/modules/plugin/shapefile/src/main/java/org
 *         /geotools/data/shapefile/shp/ShapefileHeader.java $
 */
public class ShapefileHeader {

    public static final int MAGIC = 9994;

    public static final int VERSION = 1000;

    private int fileCode = -1;

    private int fileLength = -1;

    private int version = -1;

    private ShapeType shapeType = ShapeType.UNDEFINED;

    private double minX;

    private double maxX;

    private double minY;

    private double maxY;

    private void checkMagic(boolean strict) throws java.io.IOException {
        if (fileCode != MAGIC) {
            String message = "Wrong magic number, expected " + MAGIC + ", got " + fileCode;
            if (!strict) {
                System.err.println(message);
            } else {
                throw new java.io.IOException(message);
            }
        }
    }

    private void checkVersion(boolean strict) throws java.io.IOException {
        if (version != VERSION) {
            String message = "Wrong version, expected " + MAGIC + ", got " + version;
            if (!strict) {
                System.err.println(message);
            } else {
                throw new java.io.IOException(message);
            }
        }
    }

    public void read(ByteBuffer file, boolean strict) throws java.io.IOException {
        file.order(ByteOrder.BIG_ENDIAN);
        fileCode = file.getInt();

        checkMagic(strict);

        // skip 5 ints...
        file.position(file.position() + 20);

        fileLength = file.getInt();

        file.order(ByteOrder.LITTLE_ENDIAN);
        version = file.getInt();
        checkVersion(strict);
        shapeType = ShapeType.forID(file.getInt());

        minX = file.getDouble();
        minY = file.getDouble();
        maxX = file.getDouble();
        maxY = file.getDouble();

        // skip remaining unused bytes
        file.order(ByteOrder.BIG_ENDIAN);// well they may not be unused
        // forever...
        file.position(file.position() + 32);

    }

    public void write(ByteBuffer file, ShapeType type, int numGeoms, int length, double minX,
            double minY, double maxX, double maxY) throws IOException {
        file.order(ByteOrder.BIG_ENDIAN);

        file.putInt(MAGIC);

        for (int i = 0; i < 5; i++) {
            file.putInt(0); // Skip unused part of header
        }

        file.putInt(length);

        file.order(ByteOrder.LITTLE_ENDIAN);

        file.putInt(VERSION);
        file.putInt(type.id);

        // write the bounding box
        file.putDouble(minX);
        file.putDouble(minY);
        file.putDouble(maxX);
        file.putDouble(maxY);

        // skip remaining unused bytes
        file.order(ByteOrder.BIG_ENDIAN);
        for (int i = 0; i < 8; i++) {
            file.putInt(0); // Skip unused part of header
        }
    }

    public ShapeType getShapeType() {
        return shapeType;
    }

    public int getVersion() {
        return version;
    }

    public int getFileLength() {
        return fileLength;
    }

    public double minX() {
        return minX;
    }

    public double minY() {
        return minY;
    }

    public double maxX() {
        return maxX;
    }

    public double maxY() {
        return maxY;
    }

    public String toString() {
        String res = new String("ShapeFileHeader[ size " + fileLength + " version " + version
                + " shapeType " + shapeType + " bounds " + minX + "," + minY + "," + maxX + ","
                + maxY + " ]");
        return res;
    }

}
