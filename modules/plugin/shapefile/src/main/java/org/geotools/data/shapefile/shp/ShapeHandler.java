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

import java.nio.ByteBuffer;

/**
 * A ShapeHandler defines what is needed to construct and persist geometries
 * based upon the shapefile specification.
 * 
 * @author aaime
 * @author Ian Schneider
 *
 * @source $URL$
 * 
 */
public interface ShapeHandler {
    /**
     * Get the ShapeType of this handler.
     * 
     * @return The ShapeType.
     */
    public ShapeType getShapeType();

    /**
     * Read a geometry from the ByteBuffer. The buffer's position, byteOrder,
     * and limit are set to that which is needed. The record has been read as
     * well as the shape type integer. The handler need not worry about reading
     * unused information as the ShapefileReader will correctly adjust the
     * buffer position after this call.
     * 
     * @param buffer
     *                The ByteBuffer to read from.
     * @return A geometry object.
     */
    public Object read(ByteBuffer buffer, ShapeType type, boolean flatGeometry);

    /**
     * Write the geometry into the ByteBuffer. The position, byteOrder, and
     * limit are all set. The handler is not responsible for writing the record
     * or shape type integer.
     * 
     * @param buffer
     *                The ByteBuffer to write to.
     * @param geometry
     *                The geometry to write.
     */
    public void write(ByteBuffer buffer, Object geometry);

    /**
     * Get the length of the given geometry Object in <b>bytes</b> not 16-bit
     * words. This is easier to keep track of, since the ByteBuffer deals with
     * bytes. <b>Do not include the 8 bytes of record.</b>
     * 
     * @param geometry
     *                The geometry to analyze.
     * @return The number of <b>bytes</b> the shape will take up.
     */
    public int getLength(Object geometry);
    
}
