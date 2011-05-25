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
 * This class implements the db dialect for db2 spatial extender
 * 
 * @author mcr
 * 
 *
 *
 * @source $URL$
 */
public class DB2Dialect extends DBDialect {
	public DB2Dialect(Config config) {
		super(config);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geotools.gce.imagemosaic.jdbc.DBDialect#getRegisterSpatialStatement(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	protected String getRegisterSpatialStatement(String tn, String srs) {
		return "call db2gse.ST_register_spatial_column(null,'" + tn + "','"
				+ config.getGeomAttributeNameInSpatialTable() + "','" + srs
				+ "',?,?)";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geotools.gce.imagemosaic.jdbc.DBDialect#getUnregisterSpatialStatement(java.lang.String)
	 */
	@Override
	protected String getUnregisterSpatialStatement(String tn) {
		return "call db2gse.ST_unregister_spatial_column(null,'" + tn + "','"
				+ config.getGeomAttributeNameInSpatialTable() + "',?,?)";
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
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geotools.gce.imagemosaic.jdbc.DBDialect#getMultiPolygonSQLType()
	 */
	@Override
	protected String getMultiPolygonSQLType() {
		return "db2gse.st_multipolygon";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geotools.gce.imagemosaic.jdbc.DBDialect#getCreateIndexStatement(java.lang.String)
	 */
	@Override
	protected String getCreateIndexStatement(String tn) throws Exception {
		return "CREATE  INDEX IX_"
				+ tn
				+ " ON "
				+ tn
				+ "("
				+ getConfig().getGeomAttributeNameInSpatialTable()
				+ ") "
				+ " EXTEND USING db2gse.spatial_index (10000.0, 100000.0, 1000000.0)";
	}
}
