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
package org.geotools.data.postgis;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Logger;

import org.geotools.data.FeatureReader;
import org.geotools.data.jdbc.FeatureTypeInfo;
import org.geotools.data.jdbc.JDBCTextFeatureWriter;
import org.geotools.data.jdbc.QueryData;
import org.geotools.filter.Filter;
import org.geotools.filter.FilterFactory;
import org.geotools.filter.FilterFactoryFinder;
import org.geotools.filter.SQLEncoderException;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.WKBWriter;
import com.vividsolutions.jts.io.WKTWriter;


/**
 * An implementation of FeatureWriter that will work over a result set.
 * 
 *
 * @source $URL$
 */
public class PostgisFeatureWriter extends JDBCTextFeatureWriter {

    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(
    "org.geotools.data.jdbc");

    /** Well Known Text writer (from JTS). */
    protected static WKTWriter geometryWriter = new WKTWriter();
    
    private boolean WKBEnabled;
    private boolean byteaWKB;
    
    private PostgisSQLBuilder sqlBuilder;

    /**
     * 
     * @param fReader
     * @param queryData
     * @param WKBEnabled
     * @param byteaWKB   -- true if you're using postgis 1.0+.  they changed how to do wkb writing.
     * @throws IOException
     */
    public PostgisFeatureWriter(
		FeatureReader<SimpleFeatureType, SimpleFeature> fReader, QueryData queryData, boolean WKBEnabled,boolean byteaWKB, PostgisSQLBuilder sqlBuilder
	) throws IOException {
    	
        super(fReader, queryData);
        this.WKBEnabled = WKBEnabled;
        this.byteaWKB = byteaWKB;
        this.sqlBuilder = sqlBuilder;
    }
    
    protected String getGeometryInsertText(Geometry geom, int srid) throws IOException {
        return getGeometryInsertText(geom, srid, 2);
    }

    protected String getGeometryInsertText(Geometry geom, int srid, int dimension) throws IOException {
    	if( geom == null ) {
    		return "null";
    	}
    	
        if(WKBEnabled) {
            String wkb = WKBWriter.bytesToHex( new WKBWriter(dimension).write( geom ) );
            if (byteaWKB)
            	return "setSRID('"+wkb+"'::geometry,"+srid+")";
	        else
            	return "GeomFromWKB('" + wkb + "', " + srid + ")";
        }
            String geoText = geometryWriter.write(geom);
            return "GeometryFromText('" + geoText + "', " + srid + ")";
        }
    

    /**
     * Returns true if the WKB format is used to transfer geometries, false
     * otherwise
     *
     */
    public boolean isWKBEnabled() {
        return WKBEnabled;
    }

    /**
     * If turned on, WKB will be used to transfer geometry data instead of  WKT
     *
     * @param enabled
     */
    public void setWKBEnabled(boolean enabled) {
        WKBEnabled = enabled;
    }
    
    /**
     * DJB: this is the javadoc from the superclass, but this wasnt being done.
     * 
     * Encodes the tableName, default is to do nothing, but postgis will
     * override and put double quotes around the tablename.
     */
    protected String encodeName(String tableName) {
    	return sqlBuilder.encodeTableName(tableName);
    }
    
    protected String encodeColumnName(String colName) {
    	return sqlBuilder.encodeColumnName(colName);
    }
    
    /**
     * For postgres >= 8.1 NOWAIT is used (meaning you get a response).
     * Prior versions will block during concurrent editing.
     */
    protected String makeSelectForUpdateSql(SimpleFeature current) {
        FeatureTypeInfo ftInfo = queryData.getFeatureTypeInfo();
        SimpleFeatureType featureType = ftInfo.getSchema();
        String tableName = featureType.getTypeName();

        FilterFactory ff = FilterFactoryFinder.createFilterFactory();
        Filter fid = ff.createFidFilter(current.getID());
        
        StringBuffer sql = new StringBuffer("SELECT ");
        //fid will be picked up automatically
        sqlBuilder.sqlColumns(sql, mapper, new AttributeDescriptor[] {});  
        sqlBuilder.sqlFrom(sql, tableName);
        try {
            sqlBuilder.sqlWhere(sql, fid);
        } catch (SQLEncoderException e) {
            e.printStackTrace();
        }
        sql.append(" FOR UPDATE");
        
        //determine if "NOWAIT" is supported (postgres >= 8.1)
        try {
            int major = queryData.getConnection().getMetaData().getDatabaseMajorVersion();
            int minor = queryData.getConnection().getMetaData().getDatabaseMinorVersion();
            if ((major > 8) || ((major == 8) && minor >= 1)) {
                sql.append(" NOWAIT"); //horray, no blocking!
            } else {
                LOGGER.warning("To fully support concurrent edits, please upgrade to postgres >= 8.1; the version currently in use will block");
            }
        } catch (SQLException e) { //we couldn't get the version :(
            LOGGER.warning("Failed to determine postgres version; assuming < 8.1");
        }

        return (sql.toString());
    }
}
