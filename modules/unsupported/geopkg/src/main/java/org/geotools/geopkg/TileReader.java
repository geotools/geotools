package org.geotools.geopkg;

import java.io.Closeable;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;

public class TileReader implements Iterator<Tile>, Closeable{

    ResultSet rs;
    Connection cx;
    Boolean next;

    public TileReader(ResultSet rs, Connection cx) {
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
        }
        catch(SQLException e) {
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
    public void close() throws IOException {
        Statement st;
        try {
            st = rs.getStatement();

            rs.close();
            if (st != null) {
                st.close();
            }

            cx.close();
        } catch (SQLException e) {
            throw new IOException(e);
        }
    }

}
