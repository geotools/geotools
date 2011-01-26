/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.imagemosaic.jdbc;

/**
 * 
 * This class implements the db dialect for oracle loaction based services
 * 
 * @author mcr
 * 
 *
 * @source $URL$
 */
public class OracleDialect extends DBDialect {
	public OracleDialect(Config config) {
		super(config);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geotools.gce.imagemosaic.jdbc.DBDialect#getDoubleSQLType()
	 */
	@Override
	protected String getDoubleSQLType() {
		return "DOUBLE PRECISION";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geotools.gce.imagemosaic.jdbc.DBDialect#getRegisterSpatialStatement(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	protected String getRegisterSpatialStatement(String tn, String srs) {
		return "INSERT INTO user_sdo_geom_metadata (TABLE_NAME, COLUMN_NAME, DIMINFO, SRID )"
				+ "VALUES('"
				+ tn
				+ "','"
				+ config.getGeomAttributeNameInSpatialTable()
				+ "',"
				+ "MDSYS.SDO_DIM_ARRAY(MDSYS.SDO_DIM_ELEMENT('X',0,1000000,0.1),MDSYS.SDO_DIM_ELEMENT('Y',0,1000000,0.1)),"
				+ srs + ")";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geotools.gce.imagemosaic.jdbc.DBDialect#getUnregisterSpatialStatement(java.lang.String)
	 */
	@Override
	protected String getUnregisterSpatialStatement(String tn) {
		return "DELETE FROM user_sdo_geom_metadata WHERE TABLE_NAME='" + tn
				+ "' AND COLUMN_NAME='"
				+ config.getGeomAttributeNameInSpatialTable() + "'";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geotools.gce.imagemosaic.jdbc.DBDialect#getBLOBSQLType()
	 */
	@Override
	protected String getBLOBSQLType() {
		return "BLOB";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geotools.gce.imagemosaic.jdbc.DBDialect#getMultiPolygonSQLType()
	 */
	@Override
	protected String getMultiPolygonSQLType() {
		return "MDSYS.SDO_GEOMETRY";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geotools.gce.imagemosaic.jdbc.DBDialect#getCreateIndexStatement(java.lang.String)
	 */
	@Override
	protected String getCreateIndexStatement(String tn) throws Exception {
		return "CREATE INDEX IX_" + tn + " ON " + tn + "("
				+ getConfig().getGeomAttributeNameInSpatialTable()
				+ ") INDEXTYPE IS MDSYS.SPATIAL_INDEX";
	}

}
