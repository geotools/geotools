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
package org.geotools.data.geometryless.attributeio;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.geotools.data.DataSourceException;
import org.geotools.data.jdbc.attributeio.AttributeIO;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;


/**
 * WARNING - this may mess up if the geometry is not the last attribute in the
 * schema.  Some one please test this and remove this/fix it.
 * 
 * <p>
 * This is a very specific AttributeIO to deal with a point from two x, y
 * columns.  This probably should be generalized, into a more generic scheme
 * to map multiple columns into one object, either geometry or non-geometry
 * columns.  Hopefully we can revisit this relatively soon.
 * </p>
 *
 * @author Chris Holmes, TOPP
 *
 * @source $URL$
 * @version $Id$
 */
public class PointXYAttributeIO implements AttributeIO {
    //TODO: I beleive we have a geotools specific geometry factory, or will
    //have one soon, that uses fancy coordinate sequences, but I'm not sure
    //where it is.  It should probably be used here.
    private static GeometryFactory gFactory = new GeometryFactory();

    /**
     * @see org.geotools.data.jdbc.attributeio.AttributeIO#read(java.sql.ResultSet,
     *      int)
     */
    public Object read(ResultSet rs, int position) throws IOException {
        try {
            //casting to a Number is safe, since we checked during the
            //buildAttributeType method to make sure the column type was 
            //a number.  Would probably be a bit faster to use straight
            //java primitives, but then we would have to hard code which
            //type we could deal with, or else make the user specify.
            Number x = (Number) rs.getObject(position);

            //+1 is safe, since the sql builder specifies the x and y columns
            //right next to eachother.
            Number y = (Number) rs.getObject(position + 1);

            Coordinate coord = new Coordinate(x.doubleValue(), y.doubleValue());

            return gFactory.createPoint(coord);
        } catch (SQLException e) {
            throw new DataSourceException("Sql problem.", e);
        }
    }

    /**
     * NOT YET IMPLEMENTED!!!  (though probably shouldn't be too hard.
     *
     * @see org.geotools.data.jdbc.attributeio.AttributeIO#write(java.sql.ResultSet,
     *      int, java.lang.Object)
     */
    public void write(ResultSet rs, int position, Object value)
        throws IOException {
        try {
            if (value == null) {
                rs.updateNull(position);
            } else {
                rs.updateObject(position, value);
            }
        } catch (Exception e) {
            throw new DataSourceException("Sql problem.", e);
        }
    }

    /**
     * NOT YET IMPLEMENTED!!!
     *
     * @see org.geotools.data.jdbc.attributeio.AttributeIO#write(java.sql.PreparedStatement,
     *      int, java.lang.Object)
     */
    public void write(PreparedStatement ps, int position, Object value)
        throws IOException {
        try {
            if (value == null) {
                ps.setNull(position, Types.OTHER);
            } else {
                ps.setObject(position, value);
            }
        } catch (Exception e) {
            throw new DataSourceException("Sql problem.", e);
        }
    }
}
