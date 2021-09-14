/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2010, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.geopkg;

import java.io.Closeable;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;

/**
 * The TileReader reads tiles consecutively from a tile layer inside a GeoPackage.
 *
 * @author Justin Deoliveira
 * @author Niels Charlier
 */
public class TileReader implements Iterator<Tile>, Closeable {

    Statement st;
    ResultSet rs;
    Connection cx;
    Boolean next;

    public TileReader(ResultSet rs, Statement st, Connection cx) {
        this.st = st;
        this.rs = rs;
        this.cx = cx;
    }

    @Override
    public boolean hasNext() {
        if (next == null) {
            try {
                next = rs.next();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return next;
    }

    @Override
    public Tile next() {
        Tile t = new Tile();

        try {
            t.setZoom(rs.getInt("zoom_level"));
            t.setColumn(rs.getInt("tile_column"));
            t.setRow(rs.getInt("tile_row"));
            t.setData(rs.getBytes("tile_data"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        next = null;
        return t;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    @Override
    @SuppressWarnings({
        "PMD.CloseResource",
        "PMD.UseTryWithResources"
    }) // actually closes everything
    public void close() throws IOException {
        try {
            try {
                rs.close();
            } finally {
                try {
                    st.close();
                } finally {
                    cx.close();
                }
            }
        } catch (SQLException e) {
            throw new IOException(e);
        }
    }
}
