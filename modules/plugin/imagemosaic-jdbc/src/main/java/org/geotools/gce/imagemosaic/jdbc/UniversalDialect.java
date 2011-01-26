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
 * This class implements a common db dialect (no spatial extensions used)
 * 
 * @author mcr
 * 
 *
 * @source $URL$
 */
public class UniversalDialect extends DBDialect {
	public UniversalDialect(Config config) {
		super(config);
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
		// return "blob";
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geotools.gce.imagemosaic.jdbc.DBDialect#getCreateSpatialTableStatement(java.lang.String)
	 */
	@Override
	protected String getCreateSpatialTableStatement(String tableName)
			throws Exception {
		String statement = " CREATE TABLE " + tableName;
		statement += (" ( " + getConfig().getKeyAttributeNameInSpatialTable() + " CHAR(64) NOT NULL,");
		statement += (getConfig().getTileMinXAttribute() + " DOUBLE NOT NULL,");
		statement += (getConfig().getTileMinYAttribute() + " DOUBLE NOT NULL,");
		statement += (getConfig().getTileMaxXAttribute() + " DOUBLE NOT NULL,");
		statement += (getConfig().getTileMaxYAttribute() + " DOUBLE NOT NULL,");
		statement += ("CONSTRAINT " + tableName + "_PK PRIMARY KEY(" + getConfig()
				.getKeyAttributeNameInSpatialTable());
		statement += "))";

		return statement;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geotools.gce.imagemosaic.jdbc.DBDialect#getCreateSpatialTableStatementJoined(java.lang.String)
	 */
	@Override
	protected String getCreateSpatialTableStatementJoined(String tableName)
			throws Exception {
		String statement = " CREATE TABLE " + tableName;
		statement += (" ( " + getConfig().getKeyAttributeNameInSpatialTable() + " CHAR(64) NOT NULL,");
		statement += (getConfig().getTileMinXAttribute() + " DOUBLE NOT NULL,");
		statement += (getConfig().getTileMinYAttribute() + " DOUBLE NOT NULL,");
		statement += (getConfig().getTileMaxXAttribute() + " DOUBLE NOT NULL,");
		statement += (getConfig().getTileMaxYAttribute() + " DOUBLE NOT NULL,");
		statement += (getConfig().getBlobAttributeNameInTileTable() + " "
				+ getBLOBSQLType() + ",");
		statement += ("CONSTRAINT " + tableName + "_PK PRIMARY KEY(" + getConfig()
				.getKeyAttributeNameInSpatialTable());
		statement += "))";

		return statement;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geotools.gce.imagemosaic.jdbc.DBDialect#getCreateIndexStatement(java.lang.String)
	 */
	@Override
	protected String getCreateIndexStatement(String tn) throws Exception {
		return "CREATE  INDEX IX_" + tn + " ON " + tn + "("
				+ getConfig().getTileMinXAttribute() + ","
				+ getConfig().getTileMinYAttribute() + ")";
	}

}
