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
package org.geotools.data.oracle;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import oracle.jdbc.OracleConnection;
import oracle.sql.STRUCT;

import org.geotools.data.DataSourceException;
import org.geotools.data.DataUtilities;
import org.geotools.data.FeatureReader;
import org.geotools.data.jdbc.JDBCTextFeatureWriter;
import org.geotools.data.jdbc.MutableFIDFeature;
import org.geotools.data.jdbc.QueryData;
import org.geotools.data.jdbc.datasource.DataSourceFinder;
import org.geotools.data.jdbc.datasource.UnWrapper;
import org.geotools.data.oracle.sdo.GeometryConverter;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.GeometryDescriptor;

import com.vividsolutions.jts.geom.Geometry;


/**
 * Subclasses JDBCTextFeatureWriter to issue Oracle transactions  directly as
 * sql text statements.  The super class takes care of all the nasty details,
 * this just returns the encoded geometry. To get some speed increases Jody
 * maintains that this class should not be used, that the updatable result
 * sets of JDBCFeatureWriter will work better.  But I couldn't get those to
 * work at all, whereas this works great for me.  We could also consider
 * putting the option for this or jdbc in the factory for OracleDataStore.
 * Should also consider using prepared statements for inserts, as they should
 * work faster - this should probably be done in the superclass.
 *
 * @author Chris Holmes, TOPP
 * @source $URL$
 * @version $Id$
 */
public class OracleFeatureWriter extends JDBCTextFeatureWriter {
	private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.geotools.data.oracle");
	
	GeometryConverter converter;
	
    public OracleFeatureWriter(FeatureReader <SimpleFeatureType, SimpleFeature> fReader, QueryData queryData )
        throws IOException {
        super(fReader, queryData);
        Connection conn = queryData.getConnection();
        if(!(conn instanceof OracleConnection)) {
            UnWrapper uw = DataSourceFinder.getUnWrapper(conn);
            if(uw != null)
                conn = uw.unwrap(conn);
        }
        OracleConnection oracleConnection = (OracleConnection) conn;
        this.converter = new GeometryConverter(oracleConnection);
    }

    protected String getGeometryInsertText(Geometry geom, int srid)
        throws IOException {
    	return "?"; // Please use a prepaired statement to insert your geometry
    	
        //String geomText = SQLEncoderOracle.toSDOGeom(geom, srid);
        //return geomText;
        }

    /**
     * Override that uses sql statements to perform the operation.
     *
     * @see org.geotools.data.jdbc.JDBCFeatureWriter#doUpdate(org.geotools.feature.Feature,
     *      org.geotools.feature.Feature)
     */
    protected void doUpdate(SimpleFeature live, SimpleFeature current)
        throws IOException, SQLException {
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("updating postgis feature " + current);
        }

        PreparedStatement statement = null;
        Connection conn = null;

        try {
            conn = queryData.getConnection();

            String sql = makeUpdateSql(live, current);
            statement = conn.prepareStatement(sql);

            SimpleFeatureType schema = current.getFeatureType();
            int position = 1;

            for (int i = 0; i < current.getAttributeCount(); i++) {
                AttributeDescriptor type = schema.getDescriptor(i);

                if (type instanceof GeometryDescriptor && !DataUtilities.attributesEqual(current.getAttribute(i), live.getAttribute(i))) {
                    Geometry geometry = (Geometry) current.getAttribute(i);

                    LOGGER.fine("ORACLE SPATIAL: geometry to be written:"
                        + geometry);
                    
                    int srid = queryData.getFeatureTypeInfo().getSRID(type.getLocalName());
                    geometry.setSRID(srid);
                    
                    STRUCT struct = converter.toSDO(geometry);
                    statement.setObject(position, struct);
                    LOGGER.fine(
                        "ORACLE SPATIAL: set geometry parameter at position:"
                        + position);
                    position++;

                    break;
                }
            }

            // System.out.println(sql);
            LOGGER.fine(sql);
            statement.execute();
        } catch (SQLException sqle) {
            String msg = "SQL Exception writing geometry column"
                + sqle.getLocalizedMessage();
            LOGGER.log(Level.SEVERE, msg, sqle);
            queryData.close(sqle);
            throw new DataSourceException(msg, sqle);
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    String msg = "Error closing JDBC Statement";
                    LOGGER.log(Level.WARNING, msg, e);
                }
            }
        }
    }

    
    /**
     * Override that uses sql prepaired statements to perform the operation.
     *
     * @see org.geotools.data.jdbc.JDBCFeatureWriter#doInsert(org.geotools.data.jdbc.MutableFIDFeature)
     */
    protected void doInsert(MutableFIDFeature current)
        throws IOException, SQLException {
        LOGGER.fine("inserting into postgis feature " + current);

        PreparedStatement statement = null;
        Connection conn = null;
        try {
            conn = queryData.getConnection();
            String sql = makeInsertSql(current);
            statement = conn.prepareStatement( sql );
            
            int position = 1;
            SimpleFeatureType schema = current.getFeatureType();
            for( int i=0; i<current.getNumberOfAttributes();i++){
            	AttributeDescriptor type = schema.getDescriptor( i );
            	if( type instanceof GeometryDescriptor ){
            		Geometry geometry = (Geometry) current.getAttribute( i );
                    
                    // set the proper SRID, otherwise insertion will fail due to issues
                    // with the spatial index
                    int srid = queryData.getFeatureTypeInfo().getSRID(type.getLocalName());
                    geometry.setSRID(srid);
                    
            		STRUCT struct = converter.toSDO( geometry );
            		statement.setObject( position, struct );
            		position++;
            	}
            }
            LOGGER.fine(sql);
            statement.execute();

            // should the ID be generated during an insert, we need to read it back
            // and set it into the feature
          if (((mapper.getColumnCount() > 0)
          && mapper.hasAutoIncrementColumns())) {
//          if (((mapper.getColumnCount() > 0))) {
                current.setID(mapper.createID(conn, current, statement));
            }
        } catch (SQLException sqle) {
            String msg = "SQL Exception writing geometry column" + sqle.getLocalizedMessage();
            LOGGER.log(Level.SEVERE, msg, sqle);
            queryData.close(sqle);
            throw new DataSourceException(msg, sqle);
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    String msg = "Error closing JDBC Statement";
                    LOGGER.log(Level.WARNING, msg, e);
                }
            }
        }
    }
}
