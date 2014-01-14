package org.geotools.mbtiles;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.SQLDialect;
import org.opengis.feature.type.GeometryDescriptor;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;

public class MBTilesDialect extends SQLDialect {

    protected MBTilesDialect(JDBCDataStore dataStore) {
        super(dataStore);
    }    

    @Override
    public void initializeConnection(Connection cx) throws SQLException {
        new MBTilesFile(dataStore.getDataSource()).init(cx);
    }

    @Override
    public void encodeGeometryEnvelope(String tableName, String geometryColumn, StringBuffer sql) {
        // TODO Auto-generated method stub

    }

    @Override
    public Envelope decodeGeometryEnvelope(ResultSet rs, int column, Connection cx)
            throws SQLException, IOException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Geometry decodeGeometryValue(GeometryDescriptor descriptor, ResultSet rs, String column,
            GeometryFactory factory, Connection cx) throws IOException, SQLException {
        // TODO Auto-generated method stub
        return null;
    }

}
