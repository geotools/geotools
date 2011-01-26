/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2003-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.oracle.attributeio;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import oracle.jdbc.OracleConnection;
import oracle.sql.STRUCT;

import org.geotools.data.DataSourceException;
import org.geotools.data.jdbc.QueryData;
import org.geotools.data.jdbc.attributeio.AttributeIO;
import org.geotools.data.jdbc.datasource.DataSourceFinder;
import org.geotools.data.jdbc.datasource.UnWrapper;
import org.geotools.data.oracle.sdo.GeometryConverter;
import org.opengis.feature.type.AttributeDescriptor;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.PrecisionModel;

/**
 * An attribute IO that uses the Oracle SDO API to read/write geometries
 * 
 * @author Andrea Aime
 * @author Sean Geoghegan, Defence Science and Technology Organisation.
 *  
 * @source $URL$
 */
public class SDOAttributeIO implements AttributeIO {

	private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.geotools.data.oracle");

	// geometry adpaters
	// private AdapterJTS adapterJTS;
	// private AdapterSDO adapterSDO;
	GeometryConverter converter;
	private QueryData queryData;
	private Class targetClazz;
	private GeometryFactory geometryFactory; 

	public SDOAttributeIO(AttributeDescriptor attributeType, QueryData queryData)
			throws DataSourceException {
		this.queryData = queryData;
		geometryFactory = null;
		try {
			String tableName = queryData.getFeatureTypeInfo()
					.getFeatureTypeName();
			String columnName = attributeType.getLocalName();
			targetClazz = attributeType.getType().getBinding();
			LOGGER.fine("About to create Geometry convertor for " + tableName
					+ "." + columnName);

			// TODO should check that it is an OracleConnection
                        Connection conn = queryData.getConnection();
                        if(!(conn instanceof OracleConnection)) {
                            UnWrapper uw = DataSourceFinder.getUnWrapper(conn);
                            if(uw != null)
                                conn = uw.unwrap(conn);
                        }
			OracleConnection oracleConnection = (OracleConnection) conn;
			//GeometryFactory gFact = null;

			int srid = queryData.getFeatureTypeInfo().getSRID(columnName);

			if (srid != -1) {
				//SRManager srManager = OraSpatialManager.getSpatialReferenceManager(conn);
				//SpatialReference sr = srManager.retrieve(srid);
				//gFact = OraSpatialManager.getGeometryFactory(sr);
				PrecisionModel pm = new PrecisionModel();
				geometryFactory = new GeometryFactory( pm, srid );
				
			} else {
				//gFact = OraSpatialManager.getGeometryFactory();
				geometryFactory = new GeometryFactory();
			}			
			//adapterSDO = new AdapterSDO(gFact, conn);
			//adapterJTS = new AdapterJTS(gFact);
			converter = new GeometryConverter( oracleConnection, geometryFactory );
				
//		catch (SQLException e) {
//			String msg = "Error setting up SDO Geometry convertor";
//			LOGGER.log(Level.SEVERE, msg, e);
//			throw new DataSourceException(msg + ":" + e.getMessage(), e);
//		}
//		catch (SRException e) {
//			throw new DataSourceException(
//					"Error setting up SDO Geometry convertor", e);
//		}
        } catch(IOException e) {
            throw new DataSourceException(e);
        } finally {
			// hold try statement in place
		}
	}

	/**
	 * @see org.geotools.data.jdbc.attributeio.AttributeIO#read(java.sql.ResultSet,
	 *      int)
	 */
	public Object read(ResultSet rs, int position) throws IOException {
		try {
			Geometry geom = null;
			Object struct = rs.getObject(position);
			// oracle.sdoapi.geom.Geometry sdoGeom = adapterSDO.importGeometry(struct);
			// geom = adapterJTS.exportGeometry(Geometry.class, sdoGeom);
			
			geom = converter.asGeometry( (STRUCT) struct );
			// in Oracle you can have polygons in a column declared to be multipolygon, and so on...
			// so we better convert geometries, since our feature model is not so lenient
			if(targetClazz.equals(MultiPolygon.class) && geom instanceof Polygon)
			    return geometryFactory.createMultiPolygon(new Polygon[] {(Polygon) geom});
			else if(targetClazz.equals(MultiPoint.class) && geom instanceof Point)
                return geometryFactory.createMultiPoint(new Point[] {(Point) geom});
			else if(targetClazz.equals(MultiLineString.class) && geom instanceof LineString)
                return geometryFactory.createMultiLineString(new LineString[] {(LineString) geom});
			else if(targetClazz.equals(GeometryCollection.class))
                return geometryFactory.createGeometryCollection(new Geometry[] {geom});
			return geom;
		} catch (SQLException e) {
			String msg = "SQL Exception reading geometry column";
			LOGGER.log(Level.SEVERE, msg, e);
			throw new DataSourceException(msg, e);
		}
//		catch (InvalidGeometryException e) {
//			String msg = "Problem with the geometry";
//			LOGGER.log(Level.SEVERE, msg, e);
//			throw new DataSourceException(msg, e);
//		} catch (GeometryInputTypeNotSupportedException e) {
//			String msg = "Geometry Conversion type error";
//			LOGGER.log(Level.SEVERE, msg, e);
//			throw new DataSourceException(msg, e);
//		} catch (GeometryOutputTypeNotSupportedException e) {
//			String msg = "Geometry Conversion type error";
//			LOGGER.log(Level.SEVERE, msg, e);
//			throw new DataSourceException(msg, e);
//		}
	}

	/**
	 * 
	 * @see org.geotools.data.jdbc.attributeio.AttributeIO#write(java.sql.ResultSet,
	 *      int, java.lang.Object)
	 */
	public void write(ResultSet rs, int position, Object value)
			throws IOException {
		try {
			//oracle.sdoapi.geom.Geometry sdoGeom = adapterJTS.importGeometry(value);
			//Object o = adapterSDO.exportGeometry(STRUCT.class, sdoGeom);
			Geometry geom = (Geometry) value;
			STRUCT struct = converter.toSDO( geom ); 
			rs.updateObject(position, struct);			
		} catch (SQLException sqlException) {
			String msg = "SQL Exception writing geometry column";
			LOGGER.log(Level.SEVERE, msg, sqlException);
			throw new DataSourceException(msg, sqlException);
		}
	}

    /**
     * @see org.geotools.data.jdbc.attributeio.AttributeIO#write(java.sql.PreparedStatement, int, java.lang.Object)
     */
    public void write(PreparedStatement ps, int position, Object value) throws IOException {
        try {
            Geometry geom = (Geometry) value;
            STRUCT struct = converter.toSDO( geom ); 
            ps.setObject(position, struct);          
        } catch (SQLException sqlException) {
            String msg = "SQL Exception writing geometry column";
            LOGGER.log(Level.SEVERE, msg, sqlException);
            throw new DataSourceException(msg, sqlException);
        }
        
    }

}
