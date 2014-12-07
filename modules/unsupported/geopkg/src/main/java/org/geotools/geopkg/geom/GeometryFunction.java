package org.geotools.geopkg.geom;

import java.io.IOException;
import java.sql.SQLException;
import org.sqlite.Function;

public abstract class GeometryFunction extends Function {
        
    public abstract Object execute(GeoPkgGeomReader reader) throws IOException;

    @Override
    protected void xFunc() throws SQLException {
        if (args() != 1) {
            throw new SQLException("Geometry Function expects one argument.");
        }
        
        Object res;
        try {
            res = execute(new GeoPkgGeomReader(value_blob(0)));
        } catch (IOException e) {
            throw new SQLException(e);
        }
        
        if (res == null) {
            result();
        } else if (res instanceof Integer) {
           result((Integer) res);
        } else if (res instanceof Double) {
            result((Double) res);
        } else if (res instanceof String) {
            result((String) res);
        } else if (res instanceof Long) {
           result((Long) res);
        } else if (res instanceof byte[]) {
           result((byte[]) res);
        } else if (res instanceof Boolean) {
            result((Boolean) res ? 1 : 0 );
        }
    }

}
