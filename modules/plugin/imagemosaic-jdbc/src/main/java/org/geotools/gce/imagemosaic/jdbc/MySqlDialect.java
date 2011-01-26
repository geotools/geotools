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
 * This class implements the db dialect for mysql
 * 
 * @author mcr
 * 
 *
 * @source $URL$
 */
public class MySqlDialect extends DBDialect {
	public MySqlDialect(Config config) {
		super(config);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geotools.gce.imagemosaic.jdbc.DBDialect#getDropIndexStatment(java.lang.String)
	 */
	String getDropIndexStatment(String tn) {
		return "drop index IX_" + tn + " on " + tn;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geotools.gce.imagemosaic.jdbc.DBDialect#getBLOBSQLType()
	 */
	@Override
	protected String getBLOBSQLType() {
		return "LONGBLOB";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geotools.gce.imagemosaic.jdbc.DBDialect#getMultiPolygonSQLType()
	 */
	@Override
	protected String getMultiPolygonSQLType() {
		return "MULTIPOLYGON";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geotools.gce.imagemosaic.jdbc.DBDialect#getCreateIndexStatement(java.lang.String)
	 */
	@Override
	protected String getCreateIndexStatement(String tn) throws Exception {
		// String stmt = "ALTER TABLE "+tn + " MODIFY
		// "+getConfig().getGeomAttributeNameInSpatialTable() + " "
		// + getMulitPolygonSQLType() + " NOT NULL";
		// con.prepareStatement(stmt).execute();
		return "CREATE SPATIAL INDEX IX_" + tn + " ON " + tn + "("
				+ getConfig().getGeomAttributeNameInSpatialTable() + ") ";
	}

}
