/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *    (C) Copyright IBM Corporation, 2005-2007. All rights reserved.
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
 *
 */

package org.geotools.data.db2;

/**
 * This is a modified JTS WKBWriter suiting for DB2
 * 
 * @author Christian Mueller
 *
 */


import java.io.*;
import com.vividsolutions.jts.geom.*;
import com.vividsolutions.jts.io.ByteOrderValues;
import com.vividsolutions.jts.io.OutStream;
import com.vividsolutions.jts.io.OutputStreamOutStream;
import com.vividsolutions.jts.io.WKBConstants;
import com.vividsolutions.jts.io.WKBWriter;
import com.vividsolutions.jts.util.Assert;

/**
 * 
 * @author Christian Mueller
 * 
 * Version of JTS WKB Writer adjusted for DB2 
 * 
 * @see WKBWriter 
 * for JTS Java Doc
 *
 *
 * @source $URL$
 */


public class DB2WKBWriter {
    
    
    
    /**
     * returns the coordinate dimension for a geometry
     * @param Geometry g
     * @return if there is one z value != NaN, then 3 else 2
     */
    public static final int guessCoorinateDims(Geometry g) {
        int dims = 2;
        final Coordinate[] cs = g.getCoordinates();
        for (int t = cs.length - 1; t >= 0; t--) {
            if (!(Double.isNaN(cs[t].z))) {
                dims = 3;
                break;
            }
        }
        return dims;
    }

    
    
    public static String bytesToHex(byte[] bytes)
    {
      StringBuffer buf = new StringBuffer();
      for (int i = 0; i < bytes.length; i++) {
        byte b = bytes[i];
        buf.append(toHexDigit((b >> 4) & 0x0F));
        buf.append(toHexDigit(b & 0x0F));
      }
      return buf.toString();
    }

    private static char toHexDigit(int n)
    {
      if (n < 0 || n > 15)
        throw new IllegalArgumentException("Nibble value out of range: " + n);
      if (n <= 9)
        return (char) ('0' + n);
      return (char) ('A' + (n - 10));
    }

    private int outputDimension = 2;
    private int byteOrder;
    private ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
    private OutStream byteArrayOutStream = new OutputStreamOutStream(byteArrayOS);
    // holds output data values
    private byte[] buf = new byte[8];
    private boolean hasOGCWkbZTyps;

    /**
     * Creates a writer that writes {@link Geometry}s with
     * output dimension = 2 and BIG_ENDIAN byte order
     */
    public DB2WKBWriter(boolean hasOGCWkbZTyps ) {
      this(2, ByteOrderValues.BIG_ENDIAN, hasOGCWkbZTyps);
    }

    /**
     * Creates a writer that writes {@link Geometry}s with
     * the given dimension (2 or 3) for output coordinates
     * and {@link BIG_ENDIAN} byte order.
     * If the input geometry has a small coordinate dimension,
     * coordinates will be padded with {@link NULL_ORDINATE}.
     *
     * @param outputDimension the coordinate dimension to output (2 or 3)
     */
    public DB2WKBWriter(int outputDimension, boolean hasOGCWkbZTyps) {
      this(outputDimension, ByteOrderValues.BIG_ENDIAN,hasOGCWkbZTyps);
    }

    /**
     * Creates a writer that writes {@link Geometry}s with
     * the given dimension (2 or 3) for output coordinates
     * and byte order
     * If the input geometry has a small coordinate dimension,
     * coordinates will be padded with {@link NULL_ORDINATE}.
     *
     * @param outputDimension the coordinate dimension to output (2 or 3)
     * @param byteOrder the byte ordering to use
     */
    public DB2WKBWriter(int outputDimension, int byteOrder, boolean hasOGCWkbZTyps) {
      this.outputDimension = outputDimension;
      this.byteOrder = byteOrder;
      this.hasOGCWkbZTyps=hasOGCWkbZTyps;

      if (outputDimension < 2 || outputDimension > 3)
        throw new IllegalArgumentException("Output dimension must be 2 or 3");
    }

    /**
     * Writes a {@link Geometry} into a byte array.
     *
     * @param geom the geometry to write
     * @return the byte array containing the WKB
     */
    public byte[] write(Geometry geom)
    {
      try {
        byteArrayOS.reset();
        write(geom, byteArrayOutStream);
      }
      catch (IOException ex) {
        throw new RuntimeException("Unexpected IO exception: " + ex.getMessage());
      }
      return byteArrayOS.toByteArray();
    }

    /**
     * Writes a {@link Geometry} to an {@link OutStream}.
     *
     * @param geom the geometry to write
     * @param os the out stream to write to
     * @throws IOException if an I/O error occurs
     */
    public void write(Geometry geom, OutStream os) throws IOException
    {
      if (geom instanceof Point)
        writePoint((Point) geom, os);
      // LinearRings will be written as LineStrings
      else if (geom instanceof LineString)
        writeLineString((LineString) geom, os);
      else if (geom instanceof Polygon)
        writePolygon((Polygon) geom, os);
      else if (geom instanceof MultiPoint)
        writeGeometryCollection(DB2WKBConstants.wkbMultiPoint2D, (MultiPoint) geom, os);
      else if (geom instanceof MultiLineString)
        writeGeometryCollection(DB2WKBConstants.wkbMultiLineString2D,
            (MultiLineString) geom, os);
      else if (geom instanceof MultiPolygon)
        writeGeometryCollection(DB2WKBConstants.wkbMultiPolygon2D,
            (MultiPolygon) geom, os);
      else if (geom instanceof GeometryCollection)
        writeGeometryCollection(DB2WKBConstants.wkbGeomCollection2D,
            (GeometryCollection) geom, os);
      else {
        Assert.shouldNeverReachHere("Unknown Geometry type");
      }
    }

    private void writePoint(Point pt, OutStream os) throws IOException
    {
      if (pt.getCoordinateSequence().size() == 0)
        throw new IllegalArgumentException("Empty Points cannot be represented in WKB");
      writeByteOrder(os);
      writeGeometryType(DB2WKBConstants.wkbPoint2D, os);
      writeCoordinateSequence(pt.getCoordinateSequence(), false, os);
    }

    private void writeLineString(LineString line, OutStream os)
        throws IOException
    {
      writeByteOrder(os);
      writeGeometryType(DB2WKBConstants.wkbLineString2D, os);
      writeCoordinateSequence(line.getCoordinateSequence(), true, os);
    }

    private void writePolygon(Polygon poly, OutStream os) throws IOException
    {
      writeByteOrder(os);
      writeGeometryType(DB2WKBConstants.wkbPolygon2D, os);
      writeInt(poly.getNumInteriorRing() + 1, os);
      writeCoordinateSequence(poly.getExteriorRing().getCoordinateSequence(), true, os);
      for (int i = 0; i < poly.getNumInteriorRing(); i++) {
        writeCoordinateSequence(poly.getInteriorRingN(i).getCoordinateSequence(), true,
            os);
      }
    }

    private void writeGeometryCollection(int geometryType, GeometryCollection gc,
        OutStream os) throws IOException
    {
      writeByteOrder(os);
      writeGeometryType(geometryType, os);
      writeInt(gc.getNumGeometries(), os);
      for (int i = 0; i < gc.getNumGeometries(); i++) {
        write(gc.getGeometryN(i), os);
      }
    }

    private void writeByteOrder(OutStream os) throws IOException
    {
      if (byteOrder == ByteOrderValues.LITTLE_ENDIAN)
        buf[0] = WKBConstants.wkbNDR;
      else
        buf[0] = WKBConstants.wkbXDR;
      os.write(buf, 1);
    }

    private void writeGeometryType(int geometryType, OutStream os)
        throws IOException
    {
      int typeInt = geometryType;  
      if (outputDimension==3) {  // DB2 specific for z support
          if (hasOGCWkbZTyps) {
              if (geometryType==DB2WKBConstants.wkbPoint2D) typeInt=DB2WKBConstants.wkbOGCPointZ;
              if (geometryType==DB2WKBConstants.wkbLineString2D) typeInt=DB2WKBConstants.wkbOGCLineStringZ;
              if (geometryType==DB2WKBConstants.wkbPolygon2D) typeInt=DB2WKBConstants.wkbOGCPolygonZ;
              if (geometryType==DB2WKBConstants.wkbMultiPoint2D) typeInt=DB2WKBConstants.wkbOGCMultiPointZ;
              if (geometryType==DB2WKBConstants.wkbMultiLineString2D) typeInt=DB2WKBConstants.wkbOGCMultiLineStringZ;
              if (geometryType==DB2WKBConstants.wkbMultiPolygon2D) typeInt=DB2WKBConstants.wkbOGCMultiPolygonZ;
              if (geometryType==DB2WKBConstants.wkbGeomCollection2D) typeInt=DB2WKBConstants.wkbOGCGeomCollectionZ;
          }
          else {    
              if (geometryType==DB2WKBConstants.wkbPoint2D) typeInt=DB2WKBConstants.wkbPointZ;
              if (geometryType==DB2WKBConstants.wkbLineString2D) typeInt=DB2WKBConstants.wkbLineStringZ;
              if (geometryType==DB2WKBConstants.wkbPolygon2D) typeInt=DB2WKBConstants.wkbPolygonZ;
              if (geometryType==DB2WKBConstants.wkbMultiPoint2D) typeInt=DB2WKBConstants.wkbMultiPointZ;
              if (geometryType==DB2WKBConstants.wkbMultiLineString2D) typeInt=DB2WKBConstants.wkbMultiLineStringZ;
              if (geometryType==DB2WKBConstants.wkbMultiPolygon2D) typeInt=DB2WKBConstants.wkbMultiPolygonZ;
              if (geometryType==DB2WKBConstants.wkbGeomCollection2D) typeInt=DB2WKBConstants.wkbGeomCollectionZ;
          }
      }
      writeInt(typeInt, os);
    }

    private void writeInt(int intValue, OutStream os) throws IOException
    {
      ByteOrderValues.putInt(intValue, buf, byteOrder);
      os.write(buf, 4);
    }

    private void writeCoordinateSequence(CoordinateSequence seq, boolean writeSize, OutStream os)
        throws IOException
    {
      if (writeSize)
        writeInt(seq.size(), os);

      for (int i = 0; i < seq.size(); i++) {
        writeCoordinate(seq, i, os);
      }
    }

    private void writeCoordinate(CoordinateSequence seq, int index, OutStream os)
    throws IOException
    {
      ByteOrderValues.putDouble(seq.getX(index), buf, byteOrder);
      os.write(buf, 8);
      ByteOrderValues.putDouble(seq.getY(index), buf, byteOrder);
      os.write(buf, 8);
      
      // only write 3rd dim if caller has requested it for this writer
      if (outputDimension >= 3) {
        // if 3rd dim is requested, only access and write it if the CS provides is
          double ordVal = Coordinate.NULL_ORDINATE;
          if (seq.getDimension() >= 3)
                  ordVal = seq.getOrdinate(index, 2);
        ByteOrderValues.putDouble(ordVal, buf, byteOrder);
        os.write(buf, 8);
      }
    }


}
