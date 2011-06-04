package org.geotools.data.postgis.fidmapper;

import java.io.IOException;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.Statement;
import java.util.UUID;

import org.geotools.data.jdbc.fidmapper.AbstractFIDMapper;
import org.opengis.feature.simple.SimpleFeature;

public class UUIDFIDMapper extends AbstractFIDMapper {
    
    public UUIDFIDMapper(String colName, int type) {
        setInfo(colName, type, 36, 0, false);
    }

    public String createID(Connection conn, SimpleFeature feature, Statement statement)
            throws IOException {
        // TODO: use a separate library that can generate UUID using the network card
        return UUID.randomUUID().toString();
    }

    public String getID(Object[] attributes) {
        return attributes[0].toString();
    }

    public Object[] getPKAttributes(String FID) throws IOException {
        try {
            return new Object[] {FID};
        } catch(NumberFormatException e) {
            // to be compatible with other fid mappers, though I'm not persuaded it's a good idea
            return new Object[] {BigInteger.valueOf(-1)};
        }
    }

    public boolean isValid(String fid) {
        return fid.length() == 36;
    }

}
